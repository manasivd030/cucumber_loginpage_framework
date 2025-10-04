package config;

import java.io.InputStream;
import java.util.Properties;

public class Config {
    private static final Properties properties = new Properties();

    static {
        try (InputStream is = Config.class.getClassLoader().getResourceAsStream("config.properties")) {
            if (is == null) throw new RuntimeException("config.properties not found on classpath");
            properties.load(is);
        } catch (Exception e) {
            throw new RuntimeException("Failed to load config.properties", e);
        }
    }

    public static String get(String key) { return properties.getProperty(key); }

    public static String baseUrl() {
        return properties.getProperty("baseUrl") + properties.getProperty("loginPath");
    }

    public static String username_id()
    {
        return properties.getProperty("username_id");
    }

    public static String passwordId() {
        return properties.getProperty("password_id");
    }

    public static boolean headless() {
        return Boolean.parseBoolean(properties.getProperty("headless", "false"));
    }
}
