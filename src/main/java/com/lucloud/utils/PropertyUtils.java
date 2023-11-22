package com.lucloud.utils;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

@Slf4j
public class PropertyUtils {

	private static String configFileName = "config.properties";
	private static Properties p = null;
	
	static
	{
		p = new Properties();
		try
        {
            InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream(configFileName);
            p.load(is);
            is.close();
        }
        catch (IOException e)
        {
        	log.error("读取"+configFileName+"配置文件出错:", e);
        }
	}
	
	public static Properties load(String fileName)
    {
		Properties properties = new Properties();
        try
        {
            InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream(fileName);
            properties.load(is);
            is.close();
            System.out.print(1);
        }
        catch (IOException e)
        {
        	log.error("读取"+fileName+"配置文件出错:", e);
        }
        return properties;
    }
	
	
	public static String getProperty(String key)
	{
		return p.getProperty(key);
	}
	public static String getProperty(String key, String defaultVal)
	{
		return p.getProperty(key,defaultVal);
	}
	

}
