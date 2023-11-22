package com.lucloud.utils;

import java.net.URLEncoder;
import java.security.MessageDigest;

public class MD5Utils {
	// 全局数组
    private final static String[] strDigits = { "0", "1", "2", "3", "4", "5",
            "6", "7", "8", "9", "a", "b", "c", "d", "e", "f" };

    public MD5Utils() {
    }

    // 返回形式为数字跟字符串
    private static String byteToArrayString(byte bByte) {
        int iRet = bByte;
        // System.out.println("iRet="+iRet);
        if (iRet < 0) {
            iRet += 256;
        }
        int iD1 = iRet / 16;
        int iD2 = iRet % 16;
        return strDigits[iD1] + strDigits[iD2];
    }

    // 转换字节数组为16进制字串
    private static String byteToString(byte[] bByte) {
        StringBuilder sBuffer = new StringBuilder();
        for (byte aBByte : bByte) {
            sBuffer.append(byteToArrayString(aBByte));
        }
        return sBuffer.toString();
    }

    public static String getMD5Code(String strObj) {
        String resultString = null;
        try {
            resultString = strObj;
            MessageDigest md = MessageDigest.getInstance("MD5");
            // md.digest() 该函数返回值为存放哈希值结果的byte数组
            resultString = byteToString(md.digest(strObj.getBytes("UTF-8")));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return resultString;
    }

    public static void main(String[] args) { //2.7 0.5
        System.out.println(MD5Utils.getMD5Code("2026测试批次202612345678a2023-11-12 09:00:002023-11-12 11:00:003F52755A0956C2BE").toUpperCase());
        System.out.println(URLEncoder.encode(MD5Utils.getMD5Code("2024测试批次202412345678a2023-11-12 09:00:002023-11-12 11:00:003F52755A0956C2BE").toUpperCase()));
        System.out.println(MD5Utils.getMD5Code("100111999ADMIN_KEY"));
    }

}
