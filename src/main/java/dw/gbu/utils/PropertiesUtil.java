package dw.gbu.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertiesUtil {
    private static Properties prop = new Properties();
    private String path;

    public PropertiesUtil(String path) {
        this.path = path;
        InputStream in = null;
        try
        {
            in = PropertiesUtil.class.getResourceAsStream(this.path);
            prop.load(in);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (in != null)
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
        }
    }

    public static String getValueByKey(String key) {
        String value = prop.getProperty(key);
        return value;
    }


}
