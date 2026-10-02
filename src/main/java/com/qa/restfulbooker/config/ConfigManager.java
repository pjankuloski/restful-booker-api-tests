package com.qa.restfulbooker.config;

import java.io.InputStream;
import java.util.Properties;

/**
 * Loads src/test/resources/config.properties once and exposes typed getters.
 * Kept as a simple thread-safe singleton (no external config library needed
 * for a project this size).
 */
public class ConfigManager {

    private static final ConfigManager INSTANCE = new ConfigManager();
    private final Properties properties = new Properties();

    private ConfigManager() {
        try (InputStream is = Thread.currentThread()
                .getContextClassLoader()
                .getResourceAsStream("config.properties")) {

            if (is == null) {
                throw new RuntimeException("config.properties not found on the classpath");
            }
            properties.load(is);
        } catch (Exception e) {
            throw new RuntimeException("Failed to load config.properties", e);
        }
    }

    public static ConfigManager getInstance() {
        return INSTANCE;
    }

    public String getBaseUrl() {
        return properties.getProperty("base.url");
    }

    public String getUsername() {
        return properties.getProperty("auth.username");
    }

    public String getPassword() {
        return properties.getProperty("auth.password");
    }
}
