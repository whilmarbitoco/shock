package org.whilmarbitoco.core.utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * Loads and provides access to config.properties.
 *
 * All configuration keys:
 * <pre>
 *   db.url          JDBC URL (e.g. jdbc:mysql://localhost:3306/mydb)
 *   db.user         Database username
 *   db.password     Database password
 *   db.pool.size    Connection pool size (default: 5)
 *   db.debug        Enable SQL debug logging (default: false)
 *   server.port     HTTP server port
 *   view.template   Default layout template filename
 * </pre>
 */
public class Config {

    private static final String CONFIG_PATH = System.getProperty("user.dir") + "/src/main/resources/config.properties";
    private static Properties props = loadProperties();

    private static Properties loadProperties() {
        Properties p = new Properties();
        try (FileInputStream fis = new FileInputStream(CONFIG_PATH)) {
            p.load(fis);
        } catch (IOException e) {
            throw new RuntimeException("Error loading config.properties from " + CONFIG_PATH, e);
        }
        return p;
    }

    /**
     * Reload configuration from disk. Useful for tests.
     */
    public static void reload() {
        props = loadProperties();
    }

    public static int serverPort() {
        return Integer.parseInt(props.getProperty("server.port", "8080"));
    }

    public static String dbUrl() {
        return props.getProperty("db.url");
    }

    public static String dbUser() {
        return props.getProperty("db.user");
    }

    public static String dbPassword() {
        return props.getProperty("db.password");
    }

    public static int poolSize() {
        try {
            return Integer.parseInt(props.getProperty("db.pool.size", "5"));
        } catch (NumberFormatException e) {
            return 5;
        }
    }

    public static boolean debug() {
        return "true".equalsIgnoreCase(props.getProperty("db.debug", "false"));
    }

    public static String defaultViewTemplate() {
        return props.getProperty("view.template", "template.html");
    }

    public static String viewPath() {
        return resources() + "view/";
    }

    public static String resources() {
        return System.getProperty("user.dir") + "/src/main/resources/";
    }

    /**
     * Get a raw property value by key.
     */
    public static String get(String key) {
        return props.getProperty(key);
    }

    /**
     * Get a raw property value by key with a default.
     */
    public static String get(String key, String defaultValue) {
        return props.getProperty(key, defaultValue);
    }
}
