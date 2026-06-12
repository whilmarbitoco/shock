package org.whilmarbitoco.core.database;

import org.whilmarbitoco.core.utils.Config;

import java.io.*;
import java.sql.Connection;
import java.sql.Statement;

/**
 * Simple migration runner.
 * Reads SQL migration files from classpath (src/main/resources/migrations/)
 * that haven't been applied yet, and executes them in order.
 *
 * A `schema_migrations` table is created automatically to track which
 * migrations have been applied. Migration files should be named:
 *   V1__create_users_table.sql
 *   V2__add_email_to_users.sql
 *   etc.
 *
 * The version number is extracted from the filename (the number after V and before __).
 */
public class MigrationRunner {

    private static final String MIGRATION_PATH = "migrations/";
    private static final String TABLE = "schema_migrations";

    public static void run() {
        Connection conn = DBConnection.getConnection();
        try {
            createMigrationsTable(conn);
            runPendingMigrations(conn);
        } catch (Exception e) {
            throw new RuntimeException("[Migration] Failed: " + e.getMessage(), e);
        }
    }

    private static void createMigrationsTable(Connection conn) throws Exception {
        String sql = "CREATE TABLE IF NOT EXISTS " + TABLE + " ("
                + "version INT PRIMARY KEY,"
                + "applied_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP"
                + ")";
        try (Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        }
    }

    private static void runPendingMigrations(Connection conn) throws Exception {
        var resources = MigrationRunner.class.getClassLoader().getResources(MIGRATION_PATH);
        while (resources.hasMoreElements()) {
            var url = resources.nextElement();
            // Handle both file: and jar: URLs
            if ("file".equals(url.getProtocol())) {
                File dir = new File(url.toURI());
                File[] files = dir.listFiles((d, name) -> name.endsWith(".sql"));
                if (files == null) continue;
                java.util.Arrays.sort(files);
                for (File file : files) {
                    int version = extractVersion(file.getName());
                    if (version > 0 && !isApplied(conn, version)) {
                        applyMigration(conn, file, version);
                    }
                }
            }
        }
    }

    private static int extractVersion(String filename) {
        // V1__description.sql -> 1
        try {
            if (filename.startsWith("V") && filename.contains("__")) {
                String num = filename.substring(1, filename.indexOf("__"));
                return Integer.parseInt(num);
            }
        } catch (NumberFormatException e) {
            // skip
        }
        return -1;
    }

    private static boolean isApplied(Connection conn, int version) throws Exception {
        String sql = "SELECT 1 FROM " + TABLE + " WHERE version = " + version;
        try (Statement stmt = conn.createStatement();
             var rs = stmt.executeQuery(sql)) {
            return rs.next();
        }
    }

    private static void applyMigration(Connection conn, File file, int version) throws Exception {
        String sql = new String(java.nio.file.Files.readAllBytes(file.toPath()));
        try (Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
            stmt.execute("INSERT INTO " + TABLE + " (version) VALUES (" + version + ")");
        }
        if (Config.debug()) {
            System.out.println("[Migration] Applied V" + version + ": " + file.getName());
        }
    }
}
