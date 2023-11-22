package com.lucloud.utils;

/**
 * 生成随机字符串的工具类
 */
public class NonceStrUtils {

	public static String generateNumStr(int num){
		if(num>0){
			StringBuffer bs=new StringBuffer();
			for(int i=0;i<num;i++){
				bs.append(radom(1,9));
			}
			return bs.toString();
		}
		return "";
	}
	
	public static String generateCommonStr(int num){
		String baseStr="123456789ABCDEFGHIJKLMNPQRSTUVWXYZ";
		if(num>0){
			StringBuffer bs=new StringBuffer();
			for(int i=0;i<num;i++){
				bs.append(baseStr.charAt(radom(0,baseStr.length()-1)));
			}
			return bs.toString();
		}
		return "";
	}
	
	public static String generateCharStr(int num){
		String baseStr="ABCDEFGHIJKLMNPQRSTUVWXYZ";
		if(num>0){
			StringBuffer bs=new StringBuffer();
			for(int i=0;i<num;i++){
				bs.append(baseStr.charAt(radom(0,baseStr.length()-1)));
			}
			return bs.toString();
		}
		return "";
	}
	
	public static int radom(int min,int max){
        return (int)(Math.random()*(max - min) + min);
    }

	public static void main(String[] args) {
		System.out.println(generateCommonStr(20));
	}
	
}
