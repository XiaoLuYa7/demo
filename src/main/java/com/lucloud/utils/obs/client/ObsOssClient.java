
package com.lucloud.utils.obs.client;


import com.aliyun.oss.HttpMethod;
import com.aliyun.oss.OSSClient;
import com.aliyun.oss.common.utils.BinaryUtil;
import com.aliyun.oss.model.MatchMode;
import com.aliyun.oss.model.PolicyConditions;
import com.lucloud.utils.PropertyUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/*
 * @description: oss client操作实现
 * @author: sam
 * @Date: 2022-09-13 09:41:09
 */
public class ObsOssClient extends ObsClient{
    private static final Logger logger = LoggerFactory.getLogger(ObsOssClient.class);

    private static OSSClient ossClient = null;
    
    //得到一个可用的MinioClient对象
    private  OSSClient getOSSClient(){
        if(ObsOssClient.ossClient != null){
            return ObsOssClient.ossClient ;
        }
        String host = PropertyUtils.getProperty("minio_oss_server_endpoint");
        if(host.indexOf("http://")<0 && host.indexOf("https://")<0){
            host = "https://"+host;
        }
        ObsOssClient.ossClient = new OSSClient(host, PropertyUtils.getProperty("minio_oss_access_key"),PropertyUtils.getProperty("minio_oss_access_secret"));

        return ObsOssClient.ossClient;
    }

    /**
     * @description: 上传全路径文件为oss中的指定目录路径文件。
     * @param {String} ossPathFileName oss中的指定目录路径文件 例如：res/image/20220706/a1.jpg
     * @param {String} srcFileName  全路径文件名  例如：E:/word/a.doc
     * @return {*} null表示失败，其他表示成功上传后的url
     * @author: sam
     * @Date: 2022-07-06 11:32:11
     */    
    public  String uploadFile(String ossPathFileName, String  srcFileName){        
        try{           
            return uploadFile(ossPathFileName,new FileInputStream(srcFileName));
        }catch(Exception e){
            logger.error("", e);
        }
        return null;
    }
    /**
     * @description: 上传全路径文件为oss中的指定目录路径文件。
     * @param {String} ossPathFileName oss中的指定目录路径文件 例如：res/image/20220706/a1.jpg
     * @param {InputStream} in  输入流  
     * @return {*} null表示失败，其他表示成功上传后的url
     * @author: sam
     * @Date: 2022-07-06 11:32:11
     */    
    public  String uploadFile(String ossPathFileName, InputStream  in){
        try{
            ossPathFileName = mid(mid(PropertyUtils.getProperty("minio_oss_base_path"))+"/"+mid(ossPathFileName));
            getOSSClient().putObject(PropertyUtils.getProperty("minio_oss_bucket_name"), ossPathFileName, in);
            return mid(PropertyUtils.getProperty("minio_oss_endpoint"))+"/"+ossPathFileName;
        }catch(Exception e){
            logger.error("", e);
        }finally{
            closeStream(in,null);
        }
        return null;
    }

    /**
     * @description: 读取oss中的文件
     * @param {String} ossUrl oss的全路径url
     * @return {InputStream}  null表示失败，其他表示成功
     * @author: sam
     * @Date: 2022-07-06 13:53:21
     */    
    public  InputStream readFile(String ossUrl){
        try{
            String key = mid(ossUrl.replace(mid(PropertyUtils.getProperty("minio_oss_endpoint")), ""));
            return getOSSClient().getObject(PropertyUtils.getProperty("minio_oss_bucket_name"), key).getObjectContent();
        }catch(Exception e){
            logger.error("", e);
        }
        return null;
    }

    /**
     * @description: 下载oss中的文件
     * @param {String} ossUrl oss的全路径url
     * @param {String} outputFileName 下载的文件保存的全路径文件名
     * @return {boolean}  false表示失败，true表示成功
     * @author: sam
     * @Date: 2022-07-06 13:53:21
     */ 
    public  boolean downloadFile(String ossUrl,String outputFileName){
        try{
          return downloadFile(ossUrl,new FileOutputStream(outputFileName));
        }catch(Exception e){
            logger.error("", e);
        }
        return false;
    }
    /**
     * @description: 下载oss中的文件
     * @param {String} ossUrl oss的全路径url
     * @param {OutputStream} out 下载的文件输出流
     * @return {boolean}  false表示失败，true表示成功
     * @author: sam
     * @Date: 2022-07-06 13:53:21
     */
    public  boolean downloadFile(String ossUrl,OutputStream out){
        InputStream in =  readFile(ossUrl);
        if(in==null){
            return false;
        }

        boolean isSuccess = false;
        try{
            byte[] buffer = new byte[1024];
            while(true){
                int len = in.read(buffer);
                if(len<0){
                    break;
                }
                out.write(buffer, 0, len);
            }
            isSuccess = true;            
        }catch(Exception e){
            logger.error("", e);  
        }
        closeStream(in, out);          
        return isSuccess;
    }

    /**
     * @description: 删除oss中的指定文件
     * @param {String} ossUrl oss全路径url
     * @return {*} true表示删除成功，false表示删除失败
     * @author: sam
     * @Date: 2022-09-13 11:41:23
     */    
    public boolean deleteFile(String ossUrl){
        try{
            String key = mid(ossUrl.replace(mid(PropertyUtils.getProperty("minio_oss_endpoint")), ""));
            getOSSClient().deleteObject(PropertyUtils.getProperty("minio_oss_bucket_name"), key);
            return true;
        }catch(Exception e){
            logger.error("", e);  
        }
        return false;
    }

    /**
     * @description: 生成签名url
     * @param {path} oss中的相对路径
     * @param {String} filename 原文件名
     * @param {int} expiryMinutes X分钟
     * @return {*} null表示失败，其他表示成功
     * @author: sam
     * @Date: 2022-07-05 16:20:33
     */
    public String getSignUrl(String path,String filename,int expiryMinutes){        
        try{
            String ossFileName = randomFilename(filename);
            String ossPathFileName = mid(mid(path)+"/"+getDate()+"/"+ossFileName);
            Date expiration = new Date(System.currentTimeMillis() + expiryMinutes * 60 * 1000);
            String key = mid(PropertyUtils.getProperty("minio_oss_base_path"))+"/"+mid(ossPathFileName);
            return toOSSUrl(getOSSClient().generatePresignedUrl(PropertyUtils.getProperty("minio_oss_bucket_name"), key, expiration, HttpMethod.PUT).toString());            
        }catch(Exception e){
            logger.error("", e);
        }
        return null;
    }

    /**
     * @description: 生成签名数据(用于form post方式)
     * @param {String} path oss中的目录（上传的文件保存的目录）
     * @param {Integer} expiry 失效时间，分钟
     * @return {*} 签名数据
     * @author: sam
     * @Date: 2022-07-08 13:55:05
     */    
    public  Map<String,Object> getSignData(String path,Integer expiry){
        Map<String,Object> signDataMap = new HashMap<String,Object>();
        try{
            String endpoint = mid(PropertyUtils.getProperty("minio_oss_server_endpoint"));
            endpoint = endpoint.replaceAll("http://", "");
            endpoint = endpoint.replaceAll("https://", "");         

            String dir = mid(PropertyUtils.getProperty("minio_oss_base_path"));//"user-dir-prefix/"; // 用户上传文件时指定的目录。        
            if(path.length()>0){
                dir = dir+"/"+mid(path);
            }
            //key
            String key = dir+"/"+getDate()+"/";

            String ossSignedUrl = String.format("https://%s.%s",PropertyUtils.getProperty("minio_oss_bucket_name"),endpoint);//真正的上传url 格式为 bucketname.endpoint; https://bucketname.endpoint;
            String ossPathUrl = mid(PropertyUtils.getProperty("minio_oss_endpoint"))+"/"+key;
            
            long expireTime = expiry*60;//失效时间3600秒一小时
            long expireEndTime = System.currentTimeMillis() + expireTime * 1000;
            Date expiration = new Date(expireEndTime);
            PolicyConditions policyConds = new PolicyConditions();
            policyConds.addConditionItem(PolicyConditions.COND_CONTENT_LENGTH_RANGE, 0, 1048576000);
            policyConds.addConditionItem(MatchMode.StartWith, PolicyConditions.COND_KEY, key);

            String postPolicy = getOSSClient().generatePostPolicy(expiration, policyConds);
            byte[] binaryData = postPolicy.getBytes("utf-8");
            String encodedPolicy = BinaryUtil.toBase64String(binaryData);            
            String signData = getOSSClient().calculatePostSignature(postPolicy);
            if(signData == null || signData.length()<1){                
                return null;
            }

            signDataMap.put("key", key); 
            signDataMap.put("obsSignedUrl",ossSignedUrl);
            signDataMap.put("obsPathUrl",ossPathUrl);    

            Map<String,String> othersMap = new HashMap<String,String>();
            othersMap.put("policy", encodedPolicy);
            othersMap.put("signature", signData);
            othersMap.put("OSSAccessKeyId", PropertyUtils.getProperty("minio_oss_access_key"));            
            signDataMap.put("others", othersMap);

            return signDataMap;
        }catch(Exception e){
            logger.error("", e);  
        }
        return null;
    }

    /**
     * 获取临时链接 (默认30分钟有效)
     * @param ossUrl oss的url或者oss的全路径文件名 例如：http://api.minio.tb21.cn/ata-projects/gdtk/res/image/20220706/a1.jpg 或者 不含bucket的gdtk/res/image/20220706/a1.jpg     
     * @return String null失败,其他成功
     * @author sam
     * @since 2022/6/30
     */
    public  String getPublicUrl(String ossUrl){
        return getPublicUrl(ossUrl,30);
    }
    /**
     * 获取临时链接
     * @param ossUrl oss的url或者oss的全路径文件名 例如：http://api.minio.tb21.cn/ata-projects/gdtk/res/image/20220706/a1.jpg 或者 不含bucket的gdtk/res/image/20220706/a1.jpg
     * @param expire 过期时间（分钟）
     * @return String null失败,其他成功
     * @author sam
     * @since 2022/6/30
     */
    public  String getPublicUrl(String ossUrl,Integer expire){      
        try{ 
        String key = mid(ossUrl.replace(mid(PropertyUtils.getProperty("minio_oss_endpoint")), ""));
        Date expiration = new Date(System.currentTimeMillis() + expire * 60  * 1000);

        URL url = getOSSClient().generatePresignedUrl(PropertyUtils.getProperty("minio_oss_bucket_name"), key, expiration, HttpMethod.GET);
        return toOSSUrl(url.toString());
        }catch(Exception e){
            logger.error("", e);
        }
        return null;
    }

    /**
     * @description: 把http://92.168.0.171:9000/ata-projects/gdtk/image/a1.jpg这样的转换为http://minio.tb21.cn/ata-projects/gdtk/image/a1.jpg
     * @param {String} privateUrl 例如：http://92.168.0.171:9000/ata-projects/gdtk/image/a1.jpg
     * @return {*} 例如：http://minio.tb21.cn/ata-projects/gdtk/image/a1.jpg
     * @author: sam
     * @Date: 2022-07-12 17:23:33
     */    
    private  String toOSSUrl(String privateUrl){
        String host = PropertyUtils.getProperty("minio_oss_server_endpoint");
        host = host.replaceAll("http://", "");
        host = PropertyUtils.getProperty("minio_oss_bucket_name")+"."+mid(host.replaceAll("https://", ""));
                
        String endpoint = PropertyUtils.getProperty("minio_oss_endpoint");
        endpoint = endpoint.replace("http://", "");
        endpoint = mid(endpoint.replace("https://", ""));

        return privateUrl.replaceFirst(host, endpoint);
    }

    

    public static void main(String[] args){

        //String url = OssUtil.uploadFile("res/202209/a.jpg", "F:/tmp/x2.jpg");
        //System.out.println(url);

        //boolean ret = OssUtil.downloadFile("https://ata-chuangxin.oss-cn-beijing.aliyuncs.com/js_jzkw_system/js_jzkw_system_test/res/202209/a.jpg","F:/tmp/x2----oss--download.jpg");
        //System.out.println(ret);
        //Map<String,String> signDataMap =  OssUtil.getSignData("res/image",10);
        //System.out.println(signDataMap.toString());
        //System.out.println(OssUtil.getPublicUrl("https://ata-chuangxin.oss-cn-beijing.aliyuncs.com/js_jzkw_system/js_jzkw_system_test/res/202209/a.jpg",10));
        //System.out.println(new ObsOssClient().getSignUrl("res/2021/x2.jpg",10));
        //System.out.println(new ObsOssClient().deleteFile("https://ata-chuangxin.oss-cn-beijing.aliyuncs.com/js_jzkw_system/js_jzkw_system_test/res/202209/a.jpg"));
    }   
}
