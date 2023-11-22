package com.lucloud.utils.jedis;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/*
 * 类名：SerializeUtil
 * 功能：对象的序列化工具类
 * 
 * 
 */
public class SerializeUtil {
	
	private static final Log logger = LogFactory.getLog(SerializeUtil.class);
	/**
     * 序列化object为byte[]
     * 
     * @param object
     * @return 
     */
    public static byte[] serialize(Object object) {
        ObjectOutputStream oos = null;
        ByteArrayOutputStream baos = null;
        try {
            // 序列化
            baos = new ByteArrayOutputStream();
            oos = new ObjectOutputStream(baos);
            oos.writeObject(object);
            byte[] bytes = baos.toByteArray();
            oos.close();
            baos.close();
            return bytes;
        } catch (Exception e) {
        		
        	logger.error("SerializeUtil类的serialize函数出现异常：",e);
        }
        return null;
    }

    /**
     * 反序列化byte[] bytes为object
     * 
     * @param bytes[]
     * @return
     */
    public static Object unserialize(byte[] bytes) {
        ByteArrayInputStream bais = null;
        try {
            // 反序列化
            bais = new ByteArrayInputStream(bytes);
            ObjectInputStream ois = new ObjectInputStream(bais);
            Object object = ois.readObject();
            
            ois.close();
            bais.close();
            
            return object;
        } catch (Exception e) {
        	logger.error("SerializeUtil类的unserialize函数出现异常：",e);
        }
        return null;
    }
}
