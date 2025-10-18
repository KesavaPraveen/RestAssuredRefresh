package RequestResponseApi;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public class ConfigReader {

    private static Properties properties;

    public static Properties initiateProp() {
        properties = new Properties();
        try {
            FileInputStream fileInputStream = new FileInputStream("./src/test/resources/Config.properties");
            properties.load(fileInputStream);
        } catch (FileNotFoundException fileNotFoundException) {
            fileNotFoundException.printStackTrace();
            throw new RuntimeException("failed to find the config properties file....");
        } catch (IOException ioException) {
            ioException.printStackTrace();
            throw new RuntimeException("failed to read properties in the file....");
        }
        return properties;
    }
}
