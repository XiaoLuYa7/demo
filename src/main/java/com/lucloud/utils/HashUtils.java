/*
 * @description: 
 * @author: sam
 * @Date: 2022-09-23 14:52:42
 */
package com.lucloud.utils;

import java.security.MessageDigest;

public class HashUtils {

    
    //Java代码实现MD5消息加密
	public static String MD5(String data) {
        try{
            //返回实现指定摘要算法的MessageDigest对象。
            MessageDigest md = MessageDigest.getInstance("MD5");
            //使用指定的字节更新摘要
            md.update(data.getBytes("UTF-8"));
            //通过执行最后的操作（如填充）来完成哈希计算
            byte[] array = md.digest();
            StringBuilder sb = new StringBuilder();
            //对哈希数进行密码散列计算
            for (byte item : array) {
                sb.append(Integer.toHexString((item & 0xFF)|0x100).substring(1, 3));                
            }
            return sb.toString().toUpperCase();
        }catch(Exception e){
            e.printStackTrace();
        }
        return null;
	}

     //Java代码实现SHA-256消息加密
	public static String SHA256(String data) {
        try{
            //返回实现指定摘要算法的MessageDigest对象。
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            //使用指定的字节更新摘要
            md.update(data.getBytes("UTF-8"));
            //通过执行最后的操作（如填充）来完成哈希计算
            byte[] array = md.digest();
            StringBuilder sb = new StringBuilder();
            //对哈希数进行密码散列计算
            for (byte item : array) {
                sb.append(Integer.toHexString((item & 0xFF)|0x100).substring(1, 3));
            }
            return sb.toString().toUpperCase();
        }catch(Exception e){
            e.printStackTrace();
        }
        return null;
	}

    //Java代码实现SHA-512消息加密
	public static String SHA512(String data) {
        try{
            //返回实现指定摘要算法的MessageDigest对象。
            MessageDigest md = MessageDigest.getInstance("SHA-512");
            //使用指定的字节更新摘要
            md.update(data.getBytes("UTF-8"));
            //通过执行最后的操作（如填充）来完成哈希计算
            byte[] array = md.digest();
            StringBuilder sb = new StringBuilder();
            //对哈希数进行密码散列计算
            for (byte item : array) {
                sb.append(Integer.toHexString((item & 0xFF)|0x100).substring(1, 3));
            }
            return sb.toString().toUpperCase();
        }catch(Exception e){
            e.printStackTrace();
        }
        return null;
	}

    public static void main(String[] args) {
        System.out.println(HashUtils.MD5("124"));
    }
}
