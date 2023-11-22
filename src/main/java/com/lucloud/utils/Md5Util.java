/*
 * @Description: MD5工具类
 * @Author: sam
 * @Date: 2021-08-23 10:07:28
 */
package com.lucloud.utils;

import lombok.SneakyThrows;

import java.net.URLEncoder;
import java.security.MessageDigest;


public class Md5Util {
    private final static String[] hexDigits = {"0","1","2","3","4","5","6","7","8","9","A","B","C","D","E","F"};

    /**对字符串进行MD5编码*/
    public static String md5(String originString){
        if (originString!=null) {
            try {
                //创建具有指定算法名称的信息摘要
                MessageDigest md5 = MessageDigest.getInstance("MD5");
                //使用指定的字节数组对摘要进行最后更新，然后完成摘要计算
                byte[] results = md5.digest(originString.getBytes());
                //将得到的字节数组变成字符串返回
                String result = byteArrayToHexString(results);
                return result;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * 轮换字节数组为十六进制字符串
     * @param b 字节数组
     * @return 十六进制字符串
     */
    private static String byteArrayToHexString(byte[] b){
        StringBuffer resultSb = new StringBuffer();
        for(int i=0;i<b.length;i++){
            resultSb.append(byteToHexString(b[i]));
        }
        return resultSb.toString();
    }

    //将一个字节转化成十六进制形式的字符串
    private static String byteToHexString(byte b){
        int n = b;
        if(n<0)
            n=256+n;
        int d1 = n/16;
        int d2 = n%16;
        return hexDigits[d1] + hexDigits[d2];
    }

    @SneakyThrows
    public static void main(String[] args) {
//        System.out.println(md5("123456aa"));
        System.out.println(URLEncoder.encode("10011100"));
        System.out.println(URLEncoder.encode(Md5Util.md5("10011100"+"ADMIN_KEY")));

        String aa="ylF19uqmoFSprvQLh8ZoiJ+51W8LwLmpzvIckum1cSOV4A3CtkL+WHqUvafJieiurV14JJFIs63Ig9Wy/950ExWpKJ+yfCb7nICi2tr8NdOD1afW+7WCBJnie/ggqnVRTcSQnWCOyDWx4Sf8D+Vpk6Qi50zS7soD+EippNELC70Owj6EeVOBFfLHhl/qfifsR+48eKwimn0SKJTxNKbYZACsWQ1PYHpg3+WGZ1vM8rU=";
//        //得到密文先base64解码
//        aa=Base64Utils.decode(aa);
//        System.out.println(aa);
        //DES解密
        aa = DESUtil.decrypt("ADMIN_KEY", aa);
        System.out.println(aa);
    }
}

