package com.lucloud.utils.obs.client;

import com.lucloud.utils.PropertyUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;


/*
 * @description:对象存储接口 
 * @author: sam
 * @Date: 2022-09-13 09:39:15
 */
public abstract class  ObsClient {
    private static final Logger logger = LoggerFactory.getLogger(ObsClient.class);

    /**
     * @description: 上传全路径文件为对象存储中的指定目录路径文件。
     * @param {String} obsPathFileName obs中的指定目录路径文件 例如：res/image/20220706/a1.jpg
     * @param {String} srcFileName  全路径文件名  例如：E:/word/a.doc
     * @return {*} null表示失败，其他表示成功上传后的url
     * @author: sam
     * @Date: 2022-07-06 11:32:11
     */    
    public abstract String uploadFile(String obsPathFileName, String  srcFileName);
    /**
     * @description: 上传全路径文件为obs中的指定目录路径文件。
     * @param {String} obsPathFileName obs中的指定目录路径文件 例如：res/image/20220706/a1.jpg
     * @param {InputStream} in  输入流  
     * @return {*} null表示失败，其他表示成功上传后的url
     * @author: sam
     * @Date: 2022-07-06 11:32:11
     */    
    public abstract String uploadFile(String obsPathFileName, InputStream  in);     
    /**
     * @description: 读取obs中的文件
     * @param {String} obsUrl obs的全路径url
     * @return {InputStream}  null表示失败，其他表示成功
     * @author: sam
     * @Date: 2022-07-06 13:53:21
     */    
    public abstract InputStream readFile(String obsUrl);
    /**
     * @description: 下载obs中的文件
     * @param {String} obsUrl obs的全路径url
     * @param {String} outputFileName 下载的文件保存的全路径文件名
     * @return {boolean}  false表示失败，true表示成功
     * @author: sam
     * @Date: 2022-07-06 13:53:21
     */ 
    public abstract boolean downloadFile(String obsUrl,String outputFileName);
    /**
     * @description: 下载obs中的文件
     * @param {String} obsUrl obs的全路径url
     * @param {OutputStream} out 下载的文件输出流
     * @return {boolean}  false表示失败，true表示成功
     * @author: sam
     * @Date: 2022-07-06 13:53:21
     */
    public abstract boolean downloadFile(String obsUrl,OutputStream out);
    /**
     * @description: 删除obs中的指定文件
     * @param {String} obsUrl obs全路径url
     * @return {*} true表示删除成功，false表示删除失败
     * @author: sam
     * @Date: 2022-09-13 11:41:23
     */    
    public abstract boolean deleteFile(String obsUrl);
    /**
     * @description: 生成签名url
     * @param {path} obs中的相对路径
     * @param {String} filename 原文件名
     * @param {int} expiryMinutes X分钟
     * @return {*} null表示失败，其他表示成功
     * @author: sam
     * @Date: 2022-07-05 16:20:33
     */
    public abstract String getSignUrl(String path,String filename,int expiryMinutes);

    /**
     * @description: 生成签名数据(用于form post方式)
     * @param {String} path obs中的目录（上传的文件保存的目录）
     * @param {Integer} expiry 失效时间，分钟
     * @return {*} 签名数据
     * @author: sam
     * @Date: 2022-07-08 13:55:05
     */    
    public abstract Map<String,Object> getSignData(String path,Integer expiry);
    /**
     * 获取临时链接 (默认30分钟有效)
     * @param obsUrl obs的url或者obs的全路径文件名 例如：http://api.minio.tb21.cn/ata-projects/gdtk/res/image/20220706/a1.jpg 或者 不含bucket的gdtk/res/image/20220706/a1.jpg     
     * @return String null失败,其他成功
     * @author sam
     * @since 2022/6/30
     */
    public abstract String getPublicUrl(String obsUrl);
    /**
     * 获取临时链接
     * @param obsUrl obs的url或者obs的全路径文件名 例如：http://api.minio.tb21.cn/ata-projects/gdtk/res/image/20220706/a1.jpg 或者 不含bucket的gdtk/res/image/20220706/a1.jpg
     * @param expire 过期时间（分钟）
     * @return String null失败,其他成功
     * @author sam
     * @since 2022/6/30
     */
    public abstract String getPublicUrl(String obsUrl,Integer expire);
    //===============================以上为所有对外的开放的对象存储操作函数=======================================

    //===============================以下为工具函数=============================================================
    //关闭输入，输出流
    public static void closeStream(InputStream in,OutputStream out){
        try{
            if(in!=null){
                in.close();                
            }
            if(out!=null){
                out.close();                
            }
        }catch(Exception e){
            logger.error("", e);
        }
    }
    
    /**
     * 根据传入的文件名，生成以这个文件名的后缀为后缀的随机文件名
     * @param fileName 文件名
     * @return string 随机文件名
     * @author sam
     * @since 2022/7/4
     */
    public static String randomFilename(String fileName){
        int pos = fileName.lastIndexOf(".");
        if(pos<0){
            return System.currentTimeMillis()+"_"+ generateCommonStr(10);
        }
        return System.currentTimeMillis()+"_"+ generateCommonStr(10) + fileName.substring(pos);
    }

    //对value的左右两边去掉 / 或者  \\
    public static String mid(String value){
        return midLeft(midRight(value));
    }
    //对value的右边去掉 / 或者  \\
    public static String midRight(String value){
        if(value.length()>0){
            if(value.charAt(value.length()-1) == '/' || value.charAt(value.length()-1) == '\\'){
                value = value.substring(0, value.length()-1);
            }
        }
        return value;
    }

    //对value的左边去掉 / 或者  \\
    public static String midLeft(String value){
        if(value.length()>0){
            if(value.charAt(0) == '/' || value.charAt(0) == '\\'){
                value = value.substring(1);
            }               
        }
        return value;
    }

    /**
     * @description: 提取path中的后缀
     * @param {String} path   
     * @return {*} 如果存在 . 则返回 .后面的字符串，否则返回""
     * @author: sam
     * @Date: 2022-07-05 17:08:49
     */    
    public static String getSuffixName(String path){
        int pos = path.lastIndexOf(".");
        if(pos>0){
           return path.substring(pos+1);
        }
        return "";
    }

    //得到今天的日期，例如：20220701 
    public static String getDate(){
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
        return format.format(new Date());
    }
    
    
    /**
     * @Description: 生成随机字符串（全是0-9之间的数字）
    * @param {int} num 随机字符串的长度
    * @return {*}  num 如果为0，则返回""
    * @Author: sam
    * @Date: 2021-08-23 10:38:02
    */	
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
	
    /**
     * @Description: 生成随机字符串（由0-9，A-Z的字符组成）
    * @param {int} num 随机字符串的长度
    * @return {*} num 如果为0，则返回""
    * @Author: sam
    * @Date: 2021-08-23 10:39:38
    */	
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
	
    /**
     * @Description: 生成随机字符串（由A-Z的字符组成）
    * @param {int} num 随机字符串的长度
    * @return {*} num 如果为0，则返回""
    * @Author: sam
    * @Date: 2021-08-23 10:40:41
    */	
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
	
    /**
     * @Description: 生成随机整数（在min和max之间）
    * @param {int} min 最小值
    * @param {int} max 最大值
    * @return {*}  生成的随机整数
    * @Author: sam
    * @Date: 2021-08-23 10:41:05
    */	
	public static int radom(int min,int max){
        return (int)(Math.random()*(max - min) + min);
    }
    
    /**
     * @description: 实例化配置好的对象存储客户端类
     * @return {*} null：表示失败，其他：表示成功
     * @author: sam
     * @Date: 2022-09-14 09:35:36
     */    
    public static ObsClient newInstance(){
        try{
            return (ObsClient)Class.forName(PropertyUtils.getProperty("obs_client_class")).newInstance();
        }catch(Exception e){
            logger.error("", e);
        }
        return null;
    }
}
