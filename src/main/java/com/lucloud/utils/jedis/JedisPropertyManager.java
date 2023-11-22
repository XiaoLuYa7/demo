package com.lucloud.utils.jedis;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.Enumeration;
import java.util.Properties;
import java.util.ResourceBundle;


/*
 * 类名：JedisPropertyManager
 * 功能：加载jedis.properties中配置信息
 * */
public class JedisPropertyManager {

	private static final Log logger = LogFactory.getLog(JedisPropertyManager.class);
	
	//是否已经初始化过 false表示未初始化过 ；true表示已经初始化。
	private static boolean m_bInited = false;   
    //配置文件中的配置项目。
    protected static Properties m_vProperties = new Properties();
    
    static{
    	//从sPropertyFile配置文件中，加载配置文件的配置项目
    	loadPropertyFile("jedis");
    }

    //从sPropertyFile配置文件中，加载配置文件的配置项目
    protected static  void loadPropertyFile(String propertyFile)
    {
        //如果已经初始化过了，则不再进行初始化。
        if ( m_bInited )
            return;
        
        ResourceBundle bundle =null;
      //把配置信息转换成Properties
        try{
        	//读取配置文件
        	bundle = ResourceBundle.getBundle(propertyFile);       
	        Enumeration vEnumeration = bundle.getKeys();
	        Object oKey = null;
	        Object oValue = null;
	        while( vEnumeration.hasMoreElements() )
	        {
	            oKey = vEnumeration.nextElement();
	            oValue = bundle.getString( oKey.toString() );
	            m_vProperties.put( oKey, oValue );
	        }
	        bundle = null;
	        m_bInited = true;
	        
        }catch(Exception e){        	
        	bundle = null;
	        m_bInited = false;
	        logger.error("JedisPropertyManager的loadPropertyFile函数出现异常：",e);
        }        
        
    }

    //根据key获取配置信息的内容
    public static String getProperty( String key )
    {
        return m_vProperties.getProperty( key );
    }
    
    //根据key获取配置信息的内容（带有默认值）
    public static String getProperty( String key, String defaultValue )
    {
        return m_vProperties.getProperty( key, defaultValue );
    }
}
