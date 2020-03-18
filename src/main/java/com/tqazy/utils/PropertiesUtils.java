package com.tqazy.utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * @author 散场前的温柔
 */
public class PropertiesUtils {

    /**
     * 传入properties文件地址和要读取的key值list集合，返回由key值和读取数据组成的map集合
     *
     * @param path
     * @param list
     * @return Map&lt;key, value&gt;
     */
    public static Map<String, String> readProperties(String path, List<String> list) {
        if (list == null || list.isEmpty() || path == null || path.isEmpty()) {
            return new HashMap<String, String>(0);
        }
        Map<String, String> map = new HashMap<String, String>(10);
        PropertiesUtils propertiesUtils = new PropertiesUtils();
        Properties properties = propertiesUtils.readProperties(path);
        if (properties == null) {
            return new HashMap<String, String>(0);
        }
        for (String str : list) {
            map.put(str, properties.getProperty(str));
        }
        return map;
    }

    /**
     * 根据地址读取Properties文件并生成实例
     *
     * @param path 文件地址
     * @return Properties
     * @throws IOException
     */
    private Properties readProperties(String path) {
        Properties properties = null;
        try {
            InputStream inputStream = getClass().getClassLoader().getResourceAsStream(path);
            InputStreamReader reader = new InputStreamReader(inputStream);
            // 此处需要使用JDK1.6以上的版本
            properties = new Properties();
            properties.load(reader);
        } catch (NullPointerException e) {
            // 读取文件异常
            e.printStackTrace();
            System.err.println("读取Properties文件异常，请检查文件路径和文件名:" + path);
        } catch (IOException e) {
            // 字符流写入异常
            e.printStackTrace();
            System.err.println("字符流写入Properties实例异常");
        }
        return properties;
    }

}
