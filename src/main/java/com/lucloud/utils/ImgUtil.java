package com.lucloud.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import sun.misc.BASE64Encoder;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 *
 * @author Max
 * @since 2018/9/11
 */
@Slf4j
public class ImgUtil {

    /**
     * 获取图片md5
     * @param imgURL 图片地址
     * @return String 图片加密MD5值
     * @author Max
     * @since 2018/9/11
     */
    public static String getImgBase64MD5(String imgURL) {
        ByteArrayOutputStream outPut = new ByteArrayOutputStream();
        byte[] data = new byte[1024];
        try {
            // 创建URL
            URL url = new URL(imgURL);
            // 创建链接
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(10 * 1000);
            if(conn.getResponseCode() != HttpURLConnection.HTTP_OK) {
                //连接失败/链接失效/图片不存在
                return null;
            }
            InputStream inStream = conn.getInputStream();
            int len = -1;
            while ((len = inStream.read(data)) != -1) {
                outPut.write(data, 0, len);
            }
            inStream.close();
        } catch (IOException e) {
            log.error("获取http图片异常",e);
            return null;
        }
        // 对字节数组Base64编码
        BASE64Encoder encoder = new BASE64Encoder();
        return MD5Utils.getMD5Code(encoder.encode(outPut.toByteArray()));
    }

    /**
     * 获取图片Base64
     * @return String 图片加密MD5值
     * @author Max
     * @since 2023/4/11
     */
    public static String getImgBase64(String imgUrl,String type){
        CloseableHttpClient httpClient = null;
        CloseableHttpResponse response = null;
        HttpEntity httpEntity = null;
        byte[] result = null;
        String base64 = null;
        try {
            httpClient = HttpClients.createDefault();
            HttpGet httpGet = new HttpGet(imgUrl);
            response = httpClient.execute(httpGet);
            httpEntity = response.getEntity();
            result = EntityUtils.toByteArray(httpEntity);
            base64 = Base64.encodeBase64String(result);
        } catch (IOException e) {
            log.error("获取图片Base64异常！",e);
        } finally {
            if (httpClient != null){
                try {
                    httpClient.close();
                } catch (IOException e) {
                    log.error("获取图片Base64异常！ httpClient",e);
                }
            }
            if (response != null){
                try {
                    response.close();
                } catch (IOException e) {
                    log.error("获取图片Base64异常！response",e);
                }
            }
        }
        return "data:image/"+type+";base64,"+base64;
    }

    public static void main(String[] args) throws Exception {
        System.out.println(getImgBase64MD5("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1564117883504&di=a7dbf3e7d1b2a40f165aa622b6867ac9&imgtype=0&src=http%3A%2F%2Fupload.mnw.cn%2F2017%2F0818%2F1503024063109.jpg"));
    }
}
