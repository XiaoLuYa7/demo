
package com.lucloud.utils.obs;

import com.lucloud.entity.Result;
import com.lucloud.utils.obs.client.ObsClient;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import static com.lucloud.utils.obs.client.ObsClient.mid;


/*
 * @description: 对象存储操作工具类
 * @author: sam
 * @Date: 2022-09-13 09:42:30
 */
public class ObsUtil {
    
    private static ObsClient obs = ObsClient.newInstance();
    
    /**
     * @description: 上传全路径文件为对象存储中的指定目录路径文件。
     * @param {String} obsPathFileName obs中的指定目录路径文件 例如：res/image/20220706/a1.jpg
     * @param {String} srcFileName  全路径文件名  例如：E:/word/a.doc
     * @return {*} null表示失败，其他表示成功上传后的url
     * @author: sam
     * @Date: 2022-07-06 11:32:11
     */    
    public static String uploadFile(String obsPathFileName, String  srcFileName){
        return obs.uploadFile(obsPathFileName,srcFileName);
    }
    /**
     * @description: 上传全路径文件为obs中的指定目录路径文件。
     * @param {String} obsPathFileName obs中的指定目录路径文件 例如：res/image/20220706/a1.jpg
     * @param {InputStream} in  输入流  
     * @return {*} null表示失败，其他表示成功上传后的url
     * @author: sam
     * @Date: 2022-07-06 11:32:11
     */    
    public static String uploadFile(String obsPathFileName, InputStream  in){
        return obs.uploadFile( obsPathFileName,   in);
    }    
    /**
     * @description: 读取obs中的文件
     * @param {String} obsUrl obs的全路径url
     * @return {InputStream}  null表示失败，其他表示成功
     * @author: sam
     * @Date: 2022-07-06 13:53:21
     */    
    public static InputStream readFile(String obsUrl){
        return obs.readFile(obsUrl);
    }
    /**
     * @description: 下载obs中的文件
     * @param {String} obsUrl obs的全路径url
     * @param {String} outputFileName 下载的文件保存的全路径文件名
     * @return {boolean}  false表示失败，true表示成功
     * @author: sam
     * @Date: 2022-07-06 13:53:21
     */ 
    public static boolean downloadFile(String obsUrl,String outputFileName){
        return obs.downloadFile(obsUrl, outputFileName);
    }
    /**
     * @description: 下载obs中的文件
     * @param {String} obsUrl obs的全路径url
     * @param {OutputStream} out 下载的文件输出流
     * @return {boolean}  false表示失败，true表示成功
     * @author: sam
     * @Date: 2022-07-06 13:53:21
     */
    public static boolean downloadFile(String obsUrl,OutputStream out){
        return obs.downloadFile(obsUrl, out);
    }
    /**
     * @description: 删除obs中的指定文件
     * @param {String} obsUrl obs全路径url
     * @return {*} true表示删除成功，false表示删除失败
     * @author: sam
     * @Date: 2022-09-13 11:41:23
     */    
    public static boolean deleteFile(String obsUrl){
        return obs.deleteFile(obsUrl);
    }
    /**
     * @description: 生成签名url
     * @param {path} obs中的相对路径
     * @param {String} filename 原文件名
     * @param {int} expiryMinutes X分钟
     * @return {*} null表示失败，其他表示成功
     * @author: sam
     * @Date: 2022-07-05 16:20:33
     */
    public static String getSignUrl(String path,String filename,int expiryMinutes){
        return obs.getSignUrl(path,filename,expiryMinutes);
    }

    /**
     * @description: 生成签名数据(用于form post方式)
     * @param {String} path obs中的目录（上传的文件保存的目录）
     * @param {Integer} expiry 失效时间，分钟
     * @return {*} 签名数据
     * {
     *      "key": dir obs的保存目录，不含域名和bucket name
            "obsSignedUrl":ossSignedUrl 授权签名过的url，上传时向此url提交数据
            "obsPathUrl":ossPathUrl 包含域名和bucket name，但是不含最终文件名的obs全路径url，例如：http://xxx.xxx.xxx/xxx/xxx/2022/
            "others":Map<String,String> 真正的签名数据
     *  
     * 
     * 
     * }
     * @author: sam
     * @Date: 2022-07-08 13:55:05
     */    
    public static Map<String,Object> getSignData(String path,Integer expiry){
        return obs.getSignData(path, expiry);
    }
    /**
     * 获取临时链接 (默认30分钟有效)
     * @param obsUrl obs的url或者obs的全路径文件名 例如：http://api.minio.tb21.cn/ata-projects/gdtk/res/image/20220706/a1.jpg 或者 不含bucket的gdtk/res/image/20220706/a1.jpg     
     * @return String null失败,其他成功
     * @author sam
     * @since 2022/6/30
     */
    public static String getPublicUrl(String obsUrl){
        return obs.getPublicUrl(obsUrl);
    }
    /**
     * 获取临时链接
     * @param obsUrl obs的url或者obs的全路径文件名 例如：http://api.minio.tb21.cn/ata-projects/gdtk/res/image/20220706/a1.jpg 或者 不含bucket的gdtk/res/image/20220706/a1.jpg
     * @param expire 过期时间（分钟）
     * @return String null失败,其他成功
     * @author sam
     * @since 2022/6/30
     */
    public static String getPublicUrl(String obsUrl,Integer expire){
        return obs.getPublicUrl(obsUrl, expire);
    }

    public static void main(String[] args) throws Exception{

        //String obsUrl = "https://nwk.oss-cn-hangzhou.aliyuncs.com/obs/test/res/doc/20220913/bbb.doc";
        //System.out.println(ObsUtil.getPublicUrl(obsUrl));
        //FileInputStream in= new FileInputStream("F:/word/out.doc");
        //System.out.println(ObsUtil.uploadFile("res/doc/20220913/ccc.doc", in));
        //System.out.println(ObsUtil.uploadFile("res/doc/20220914/a1.doc", "F:/word/out.doc"));
        //System.out.println( ObsUtil.getSignUrl("res/doc", "x1.doc",30));
        //InputStream in =  ObsUtil.readFile("http://demo.local.tb21.cn/projectdemo/js_jzkw_system/js_jzkw_system_test/res/doc/20220913/ccc.doc");
        //System.out.println(ObsUtil.downloadFile("http://demo.local.tb21.cn/projectdemo/js_jzkw_system/js_jzkw_system_test/res/doc/20220913/ccc.doc", new FileOutputStream("F:/abc.doc")));
        //System.out.println(ObsUtil.downloadFile("http://demo.local.tb21.cn/projectdemo/js_jzkw_system/js_jzkw_system_test/res/doc/20220914/a1.doc", "F:/mm.doc"));
        //System.out.println(ObsUtil.getPublicUrl("http://demo.local.tb21.cn/projectdemo/js_jzkw_system/js_jzkw_system_test/res/doc/20220913/ccc.doc"));
        //System.out.println(ObsUtil.getPublicUrl("http://demo.local.tb21.cn/projectdemo/js_jzkw_system/js_jzkw_system_test/res/doc/20220913/ccc.doc",60));
    }

    /**
     * 上传指定目录的数据到obs的指定目录中
     * @param folderPath 要上传的目录(全路径目录) 例如：F:/pub
     * @param obsFolderName 要上传到obs上的目录名下; 例如： okteb或者okteb/2022/02这样的目录名
     * @return Result
     *  Result.code ==1 成功，其他：失败
     *  Result.data ：上传后的obs的url和原目录文件的对应关系 Map<String,String>
     *   Map.key -> 原文件全路径名
     *   Map.value -> obs Url
     *
     */
    public static Result uploadFolder(String folderPath, String obsFolderName){
        //判断是否是目录
        File folder = new File(folderPath);
        if(!folder.isDirectory()){
            return Result.error("无效的目录："+folderPath);
        }

        //上传目录
        Map<String,String> uploadedFileMap = new HashMap<String,String>();
        File[] files = folder.listFiles();
        for(File file:files){
            Result result = uploadFolder(file,mid(obsFolderName),uploadedFileMap);
            if(!result.checkIsOk()){
                return result;
            }
        }//end for

        //返回
        return Result.ok(uploadedFileMap);
    }

    private static Result uploadFolder(File file, String obsFolderName, Map<String,String> uploadedFileMap){

        //如果是目录
        if(file.isDirectory()){
            File[] files = file.listFiles();
            for(File subfile:files){
                Result result = uploadFolder(subfile,obsFolderName+"/"+file.getName(),uploadedFileMap);
                if(!result.checkIsOk()){
                    return result;
                }
            }//end for
            return Result.ok();
        }

        //是文件
        String obsPathFileName = obsFolderName+"/"+file.getName();
        String  srcFileName = file.getAbsolutePath();
        String obsUrl = uploadFile(obsPathFileName, srcFileName);
        if(obsUrl == null){
            String message = "上传失败：上传【"+srcFileName+"】出现异常。";
            return Result.error(message);
        }
        //保存原文件全路径名和obsUrl
        uploadedFileMap.put(srcFileName, obsUrl);
        return Result.ok();
    }
}
