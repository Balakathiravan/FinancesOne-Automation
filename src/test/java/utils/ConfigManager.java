package utils;

import java.io.FileInputStream;
import java.util.Properties;

public class ConfigManager {

    private static Properties prop;

    public static String get(String key) {
        if (prop == null) {
            prop = new Properties();
            try {
                FileInputStream fis = new FileInputStream("src/test/resources/config.properties");
                prop.load(fis);
            } catch (Exception e) {
                throw new RuntimeException("Cannot load config.properties");
            }
        }
        return prop.getProperty(key);
    }
}
