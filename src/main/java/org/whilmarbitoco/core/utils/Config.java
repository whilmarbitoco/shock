package org.whilmarbitoco.core.utils;


import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class Config {

    private static final Properties props = Config.loadProperties(getPath());


    private static Properties loadProperties(String path) {
        Properties props = new Properties();
        try (FileInputStream fis = new FileInputStream(path)) {
            props.load(fis);
        } catch (IOException e) {
            throw new RuntimeException("Error loading database properties", e);
        }
        return props;
    }

    public static int serverPort() {
        return Integer.parseInt(props.getProperty("server.port"));
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

    public static String viewTemplate() {
        return props.getProperty("view.template");
    }


    public static boolean debug() {
        return props.getProperty("db.debug").equals("true");
    }

    private static String getPath() {
        return (System.getProperty("user.dir") + "/src/main/resources/config.properties");
    }
}