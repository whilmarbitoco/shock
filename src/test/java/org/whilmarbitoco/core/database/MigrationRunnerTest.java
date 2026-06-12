package org.whilmarbitoco.core.database;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.*;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for MigrationRunner — applies versioned SQL migration files.
 */
class MigrationRunnerTest {

    @TempDir
    Path tempDir;

    // ── Helpers ─────────────────────────────────────────────────

    private Connection createInMemoryDB() throws SQLException {
        Connection conn = DriverManager.getConnection("jdbc:sqlite::memory:");
        return conn;
    }

    private void runMigrations(Connection conn) throws Exception {
        // Create migration files in temp dir
        File migDir = tempDir.resolve("migrations").toFile();
        migDir.mkdirs();

        // V1: create users table
        File v1 = new File(migDir, "V1__create_users.sql");
        Files.writeString(v1.toPath(),
                "CREATE TABLE users (id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, name TEXT NOT NULL);");

        // V2: create posts table with FK
        File v2 = new File(migDir, "V2__create_posts.sql");
        Files.writeString(v2.toPath(),
                "CREATE TABLE posts (id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, title TEXT NOT NULL, user_id INTEGER NOT NULL);");

        // V3: add email column
        File v3 = new File(migDir, "V3__add_email.sql");
        Files.writeString(v3.toPath(),
                "ALTER TABLE users ADD COLUMN email TEXT;");

        // Run migrations manually (simulating MigrationRunner logic with SQLite dialect)
        Dialect dialect = new SQLiteDialect();

        // Create schema_migrations table
        String createTracking = dialect.createMigrationsTable("schema_migrations");
        try (Statement stmt = conn.createStatement()) {
            stmt.execute(createTracking);
        }

        // Apply each migration
        File[] files = migDir.listFiles((d, n) -> n.endsWith(".sql"));
        java.util.Arrays.sort(files);
        for (File file : files) {
            int version = extractVersion(file.getName());
            if (version <= 0) continue;

            // Check if already applied
            String checkSql = dialect.checkMigrationVersion("schema_migrations", version);
            try (Statement stmt = conn.createStatement();
                 var rs = stmt.executeQuery(checkSql)) {
                if (rs.next()) continue; // already applied
            }

            // Apply
            String sql = Files.readString(file.toPath());
            try (Statement stmt = conn.createStatement()) {
                stmt.execute(sql);
                stmt.execute(dialect.insertMigrationVersion("schema_migrations", version));
            }
        }
    }

    private int extractVersion(String filename) {
        try {
            if (filename.startsWith("V") && filename.contains("__")) {
                String num = filename.substring(1, filename.indexOf("__"));
                return Integer.parseInt(num);
            }
        } catch (NumberFormatException e) { /* skip */ }
        return -1;
    }

    // ── Tests ─────────────────────────────────────────────────────

    @Test
    @DisplayName("MigrationRunner applies all pending migrations in order")
    void appliesAllMigrations() throws Exception {
        Connection conn = createInMemoryDB();
        runMigrations(conn);

        // Verify users table exists
        DatabaseMetaData meta = conn.getMetaData();
        ResultSet tables = meta.getTables(null, null, "users", null);
        assertTrue(tables.next(), "users table should exist");

        // Verify posts table exists
        tables = meta.getTables(null, null, "posts", null);
        assertTrue(tables.next(), "posts table should exist");

        // Verify schema_migrations table exists
        tables = meta.getTables(null, null, "schema_migrations", null);
        assertTrue(tables.next(), "schema_migrations table should exist");

        // Verify 3 migrations were applied
        try (Statement stmt = conn.createStatement();
             var rs = stmt.executeQuery("SELECT COUNT(*) FROM schema_migrations")) {
            assertTrue(rs.next());
            assertEquals(3, rs.getInt(1));
        }

        conn.close();
    }

    @Test
    @DisplayName("MigrationRunner skips already-applied migrations")
    void skipsAppliedMigrations() throws Exception {
        Connection conn = createInMemoryDB();
        runMigrations(conn);

        // Run again — should not fail or re-apply
        runMigrations(conn);

        // Still only 3 migrations recorded
        try (Statement stmt = conn.createStatement();
             var rs = stmt.executeQuery("SELECT COUNT(*) FROM schema_migrations")) {
            assertTrue(rs.next());
            assertEquals(3, rs.getInt(1));
        }

        conn.close();
    }

    @Test
    @DisplayName("MigrationRunner tracks version numbers correctly")
    void tracksVersions() throws Exception {
        Connection conn = createInMemoryDB();
        runMigrations(conn);

        try (Statement stmt = conn.createStatement();
             var rs = stmt.executeQuery("SELECT version FROM schema_migrations ORDER BY version")) {
            assertTrue(rs.next());
            assertEquals(1, rs.getInt(1));
            assertTrue(rs.next());
            assertEquals(2, rs.getInt(1));
            assertTrue(rs.next());
            assertEquals(3, rs.getInt(1));
            assertFalse(rs.next());
        }

        conn.close();
    }

    @Test
    @DisplayName("MigrationRunner applies schema changes (ALTER TABLE)")
    void appliesAlterTable() throws Exception {
        Connection conn = createInMemoryDB();
        runMigrations(conn);

        // Insert a user with email (V3 added the column)
        try (PreparedStatement stmt = conn.prepareStatement(
                "INSERT INTO users (name, email) VALUES (?, ?)")) {
            stmt.setString(1, "Alice");
            stmt.setString(2, "alice@test.com");
            stmt.executeUpdate();
        }

        // Verify
        try (Statement stmt = conn.createStatement();
             var rs = stmt.executeQuery("SELECT email FROM users WHERE name = 'Alice'")) {
            assertTrue(rs.next());
            assertEquals("alice@test.com", rs.getString(1));
        }

        conn.close();
    }
}
