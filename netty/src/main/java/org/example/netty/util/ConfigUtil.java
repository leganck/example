package org.example.netty.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * @author leganck
 * @date 2021/4/27 15:03
 **/
public class ConfigUtil {

    public static final String SERVER_PORT = "server.port";
    public static final String SERVER_HOST = "server.host";
    public static final String CONFIG_FILE_NAME = "config.properties";

    public static String readConfig(String key) {

        Properties properties = new Properties();
        try {
            properties.load(getResourceStream(CONFIG_FILE_NAME));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return properties.getProperty(key);
    }


    public static InputStream getResourceStream(String filename) {
        return ConfigUtil.class.getResourceAsStream("/" + filename);
    }

    public static int getServerPort() {
        return Integer.parseInt(readConfig(SERVER_PORT));
    }

    public static String getServerHost() {
        return readConfig(SERVER_HOST);
    }
}
