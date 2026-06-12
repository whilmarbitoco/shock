package org.whilmarbitoco.core.database;

import org.whilmarbitoco.core.utils.Config;

import java.io.*;
import java.sql.Connection;
import java.sql.Statement;

/**
 * Simple migration runner. Reads SQL migration files from classpath
 * and executes them in order, tracking applied versions.
 *
 * Dialect-aware — uses the dialect auto-detected from config.dbUrl()
 * for the migration tracking table schema.
 *
 * Migration files should be named: V{version}__{description}.sql
 * and placed in src/main/resources/migrations/.
 */
public class MigrationRunner {

    private static final String MIGRATION_PATH = "migrations/";

    /**
     * Run all pending migrations using the dialect from config.
     */
    public static void run() {
        Dialect dialect = Dialect.fromUrl(Config.dbUrl());
        run(dialect);
    }

    /**
     * Run all pending migrations using the specified dialect.
     */
    public static void run(Dialect dialect) {
        Connection conn = DBConnection.getConnection();
        try {
            createMigrationsTable(conn, dialect);
            runPendingMigrations(conn, dialect);
        } catch (Exception e) {
            throw new RuntimeException("[Migration] Failed: " + e.getMessage(), e);
        }
    }

    private static void createMigrationsTable(Connection conn, Dialect dialect) throws Exception {
        String sql = dialect.createMigrationsTable("schema_migrations");
        try (Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        }
    }

    private static void runPendingMigrations(Connection conn, Dialect dialect) throws Exception {
        var resources = MigrationRunner.class.getClassLoader().getResources(MIGRATION_PATH);
        while (resources.hasMoreElements()) {
            var url = resources.nextElement();
            if ("file".equals(url.getProtocol())) {
                File dir = new File(url.toURI());
                File[] files = dir.listFiles((d, name) -> name.endsWith(".sql"));
                if (files == null) continue;
                java.util.Arrays.sort(files);
                for (File file : files) {
                    int version = extractVersion(file.getName());
                    if (version > 0 && !isApplied(conn, dialect, version)) {
                        applyMigration(conn, dialect, file, version);
                    }
                }
            }
        }
    }

    private static int extractVersion(String filename) {
        try {
            if (filename.startsWith("V") && filename.contains("__")) {
                String num = filename.substring(1, filename.indexOf("__"));
                return Integer.parseInt(num);
            }
        } catch (NumberFormatException e) { /* skip */ }
        return -1;
    }

    private static boolean isApplied(Connection conn, Dialect dialect, int version) throws Exception {
        String sql = dialect.checkMigrationVersion("schema_migrations", version);
        try (Statement stmt = conn.createStatement();
             var rs = stmt.executeQuery(sql)) {
            return rs.next();
        }
    }

    private static void applyMigration(Connection conn, Dialect dialect, File file, int version) throws Exception {
        String sql = new String(java.nio.file.Files.readAllBytes(file.toPath()));
        try (Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
            stmt.execute(dialect.insertMigrationVersion("schema_migrations", version));
        }
        if (Config.debug()) {
            System.out.println("[Migration] Applied V" + version + ": " + file.getName());
        }
    }
}
