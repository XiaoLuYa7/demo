package com.lucloud.utils;

import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;
import java.io.*;
import java.net.URLEncoder;
import java.security.Key;

public class DESUtil {

	private static final Logger logger = LoggerFactory.getLogger(DESUtil.class);
	
	/**
     * 偏移变量，固定占8位字节
     */
    private final static String IV_PARAMETER = "12345678";
    /**
     * 密钥算法
     */
    private static final String ALGORITHM = "DES";
    /**
     * 加密/解密算法-工作模式-填充模式
     */
    private static final String CIPHER_ALGORITHM = "DES/CBC/PKCS5Padding";
    /**
     * 默认编码
     */
    private static final String CHARSET = "utf-8";
 
    /**
     * 生成key
     *
     * @param password
     * @return
     * @throws Exception
     */
    private static Key generateKey(String password) throws Exception {
        DESKeySpec dks = new DESKeySpec(password.getBytes(CHARSET));
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(ALGORITHM);
        return keyFactory.generateSecret(dks);
    }
 
 
    /**
     * DES加密字符串
     *
     * @param password 加密密码，长度不能够小于8位
     * @param data 待加密字符串
     * @return 加密后内容
     */
    public static String encrypt(String password, String data) {
        if (password== null || password.length() < 8) {
            throw new RuntimeException("加密失败，key不能小于8位");
        }
        if (data == null)
            return null;
        try {
            Key secretKey = generateKey(password);
            Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
            IvParameterSpec iv = new IvParameterSpec(IV_PARAMETER.getBytes(CHARSET));
            cipher.init(Cipher.ENCRYPT_MODE, secretKey, iv);
            byte[] bytes = cipher.doFinal(data.getBytes(CHARSET));
            
            return new String(Base64.encodeBase64(bytes));
 
        } catch (Exception e) {
        	logger.error("",e);
            return data;
        }
    }
 
    /**
     * DES解密字符串
     *
     * @param password 解密密码，长度不能够小于8位
     * @param data 待解密字符串
     * @return 解密后内容
     */
    public static String decrypt(String password, String data) {
        if (password== null || password.length() < 8) {
            throw new RuntimeException("加密失败，key不能小于8位");
        }
        if (data == null)
            return null;
        try {
            Key secretKey = generateKey(password);
            Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
            IvParameterSpec iv = new IvParameterSpec(IV_PARAMETER.getBytes(CHARSET));
            cipher.init(Cipher.DECRYPT_MODE, secretKey, iv);
            return new String(cipher.doFinal(Base64.decodeBase64(data.getBytes(CHARSET))), CHARSET);            
        } catch (Exception e) {
        	logger.error("",e);
            return data;
        }
    }
 
    /**
     * DES加密文件
     *
     * @param srcFile  待加密的文件
     * @param destFile 加密后存放的文件路径
     * @return true：加密成功，false：加密失败
     */
    public static boolean encryptFile(String password, String srcFile, String destFile) {
 
        if (password== null || password.length() < 8) {
            throw new RuntimeException("加密失败，key不能小于8位");
        }
        try {
            IvParameterSpec iv = new IvParameterSpec(IV_PARAMETER.getBytes(CHARSET));
            Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, generateKey(password), iv);
            InputStream is = new FileInputStream(srcFile);
            OutputStream out = new FileOutputStream(destFile);
            CipherInputStream cis = new CipherInputStream(is, cipher);
            byte[] buffer = new byte[1024];
            int r;
            while ((r = cis.read(buffer)) > 0) {
                out.write(buffer, 0, r);
            }
            cis.close();
            is.close();
            out.close();
            return true;
        } catch (Exception e) {
        	logger.error("",e);
        }
        return false;
    }
 
    /**
     * DES解密文件
     *
     * @param srcFile  已加密的文件
     * @param destFile 解密后存放的文件路径
     * @return true：解密成功，false：解密失败
     */
    public static boolean  decryptFile(String password, String srcFile, String destFile) {
        if (password== null || password.length() < 8) {
            throw new RuntimeException("加密失败，key不能小于8位");
        }
        try {
            File file = new File(destFile);
            if (!file.exists()) {
                file.getParentFile().mkdirs();
                file.createNewFile();
            }
            IvParameterSpec iv = new IvParameterSpec(IV_PARAMETER.getBytes(CHARSET));
            Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, generateKey(password), iv);
            InputStream is = new FileInputStream(srcFile);
            OutputStream out = new FileOutputStream(destFile);
            CipherOutputStream cos = new CipherOutputStream(out, cipher);
            byte[] buffer = new byte[1024];
            int r;
            while ((r = is.read(buffer)) >= 0) {
                cos.write(buffer, 0, r);
            }
            cos.close();
            is.close();
            out.close();
            return true;
        } catch (Exception e) {
        	logger.error("",e);
        }
        return false;
    }

	
	public static void main(String[] args) throws UnsupportedEncodingException {
		
		/*
		//密钥需要大于等于8个字符
		String password="1234567890asdfghtttt";
		
		
		String data = "asdasd我的好人啊67867$%Y$HRTHGRHGRTH23r2dfb f 5er4y 4h4 h4 h。";
		System.out.println("明文是："+data);
		
		String encryptData = DESUtil.encrypt(password, data);
		System.out.println("加密后的密文是："+encryptData);
		
		
		String text = DESUtil.decrypt(password, encryptData);
		System.out.println("解密后的明文是："+text);
		if(text.equals(data)==true){
			System.out.println("解密成功");
		}else{
			System.out.println("解密失败");
		}
		
		
		String srcFile = "f:/xx/c.txt";
		String destFile = "f:/xx/mi.txt";
		
		if(DESUtil.encryptFile(password, srcFile, destFile)==false){
			System.out.println("加密失败");
		}else{
			System.out.println("加密成功");
		}
		
		String outFile = "f:/xx/min.txt";
		if(DESUtil.decryptFile(password, destFile, outFile)==false){
			System.out.println("解密失败");
		}else{
			System.out.println("解密成功");
		}
		*/
        String st = "[{\"idcard\":\"510922200502040699\",\"name\":\"张三\",\"subject\":\"测试\"}]";
        String str = DESUtil.encrypt("3F52755A0956C2BE",st);
        System.out.println(URLEncoder.encode(str));
        //str = URLEncoder.encode(str,"utf-8");
        //System.out.println((str));

    }

}
