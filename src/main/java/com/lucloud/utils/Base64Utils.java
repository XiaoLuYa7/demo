/*
 * @Description: BASE64工具类
 * @Author: sam
 * @Date: 2021-08-23 10:07:28
 */
package com.lucloud.utils;

import org.apache.commons.codec.binary.Base64;

/**
 * Base64加解码工具类
 * @author Zero
 * @since 2017-9-22
 */
public class Base64Utils {

    
    /**
     * @Description: 加密
     * @param {String} str 要加密的明文
     * @return {*} 加密后的密文
     * @Author: sam
     * @Date: 2021-08-23 11:06:18
     */    
    public static String encode(String str) throws Exception {
        return new String(Base64.encodeBase64String(str.getBytes("UTF-8")));
    }

    
    /**
     * @Description: 解密
     * @param {String} str 要解密的密文
     * @return {*} 解密后的明文
     * @Author: sam
     * @Date: 2021-08-23 11:06:45
     */    
    public static String decode(String str) throws Exception {
        return new String(Base64.decodeBase64(str),"UTF-8");
    }

}
