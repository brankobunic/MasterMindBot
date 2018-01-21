package Utils;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;

public class Settings {
    private static String token;
    private static String path = "config.properties";

    public static String getToken() {

        Properties properties = new Properties();
        InputStream inputStream;

        try {
            inputStream = new FileInputStream(path);
            properties.load(inputStream);

            token = properties.getProperty("token");

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            if (token != null) {
                return token;
            }
        }
        return null;
    }
}
