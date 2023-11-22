package com.lucloud.utils.easyexcel;


import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.excel.exception.ExcelDataConvertException;
import com.alibaba.excel.metadata.Head;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.lang.reflect.Field;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 监听器工具类
 * @author lemon
 * @since 2020/5/26 0026
 */
@Service
public class EasyExcelListener<T> extends AnalysisEventListener<T> {

    private static final Logger logger = LoggerFactory.getLogger(EasyExcelListener.class);
    
    private Map<Integer, String> headMap = new HashMap<>(); // 导入模板头内容
    private boolean isCorrectFormat= true; //结果标记 默认错误
    private String resultMessage= ""; //结果信息
    private List<T> list = new ArrayList<T>(); //导入Excel数据内容



    public EasyExcelListener(){

    }

/*
    */
/*自定义头*//*

    public EasyExcelListener(Map<Integer, String> headMap){
        this.headMap = headMap;
    }
*/


    /*监听读取导入表的每一行数据*/
    public void invoke(T object, AnalysisContext analysisContext) {
        if(!isCorrectFormat){ //格式是否正确
            return;
        }

        //如果改行 未被全部删除
        if(!isAllFieldNull(object)){
            logger.info("解析到一条数据:{}", object);
            list.add(object); //加入list
        }



    }

    /* 最终都会执行的方法 */
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {
        if(isCorrectFormat){ //表头是否正确
            resultMessage = "导入所有数据完成！";
            logger.info("导入解析所有数据完成！");
        }
    }

    /**
     * 返回头数据,可以处理头文件
     *
     */
    @Override
    public void invokeHeadMap(Map<Integer, String> headMap, AnalysisContext context) {

        //是否手动传入表头信息 没有则自动读取实体类头信息
        try {
            if(this.headMap  == null || this.headMap.size() <= 0){
                Map<Integer, Head> map = context.currentReadHolder().excelReadHeadProperty().getHeadMap();
                for (int i = 0; i < map.size(); i++) {
                    //封装成headMap
                    this.headMap.put(map.get(i).getColumnIndex(),map.get(i).getHeadNameList().get(0));//加入模板头
                }
            }
        }catch (Exception e){
            isCorrectFormat = false; //导入错误
            resultMessage = "导入失败，请联系管理员！";
            logger.info("未自定义Excel头信息，自动读取实体类注解的头信息，缺少注解name或index值！",e);
            return;
        }


        logger.info("导入的表头：" + headMap);
        logger.info("模板的表头：" + this.headMap);
        if(this.headMap == null || headMap == null || this.headMap.size() != headMap.size()){
            isCorrectFormat = false; //导入表头错误
            resultMessage = "导入表的表头与模板表头不符!";
            logger.info("导入表的表头与模板表头不符！");
        }else {
            for (int i = 0; i < this.headMap.size(); i++) { //循环遍历判断表格头是否一一对应
                if(!this.headMap.get(i).equals(headMap.get(i))){
                    isCorrectFormat = false; //导入表头错误
                    resultMessage = "导入表的表头与模板表头不符!";
                    logger.info("导入表的表头与模板表头不符！");
                    break;
                }
            }
        }

    }


    /**
     * 在转换异常 获取其他异常下会调用本接口。抛出异常则停止读取。如果这里不抛出异常则 继续读取下一行。
     */
    @Override
    public void onException(Exception exception, AnalysisContext analysisContext) {
//        logger.error("解析失败，但是继续解析下一行:{}", exception.getMessage());
        // 如果是某一个单元格的转换异常 能获取到具体行号
        // 如果要获取头的信息 配合invokeHeadMap使用
        if (exception instanceof ExcelDataConvertException) { //表格内容转换异常
            ExcelDataConvertException excelException = (ExcelDataConvertException)exception;
            logger.error("第{}行，第{}列解析异常", excelException.getRowIndex() + 1, excelException.getColumnIndex() + 1);
            resultMessage = "第" + (excelException.getRowIndex() + 1) + "行，第" + (excelException.getColumnIndex() + 1) + "列，内容格式错误！";
            list.clear();
        }else {
            logger.error("导入解析异常！",exception);
            resultMessage = "导入异常！，请联系管理员";
            list.clear();
            isCorrectFormat = false;
        }

    }


    public List<T> getList() {
        return list;
    }

    public Map<Integer, String> getHeadMap() {
        return headMap;
    }

    public void setHeadList(Map<Integer, String> headMap) {
        this.headMap = headMap;
    }

    public boolean isCorrectFormat() {
        return isCorrectFormat;
    }

    public String getResultMessage() {
        return resultMessage;
    }

    public void setCorrectFormat(boolean correctFormat) {
        isCorrectFormat = correctFormat;
    }

    public void setResultMessage(String resultMessage) {
        this.resultMessage = resultMessage;
    }

    /*读取网络文件*/
    public static InputStream getFileInputStream(String path) {
        URL url = null;
        try {
            url = new URL(path);
            HttpURLConnection conn = (HttpURLConnection)url.openConnection();
            //设置超时间为3秒
            conn.setConnectTimeout(3*1000);
            //防止屏蔽程序抓取而返回403错误
            conn.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");
            //得到输入流
            return conn.getInputStream();
        } catch (Exception e) {
            logger.error("读取网络文件异常:"+path);
        }
        return null;
    }
    
    /**
     * 判断对象的所有属性是否为空
     * @author lemon
     * @since 2020/8/13
     */
    private boolean isAllFieldNull(Object obj){

        boolean flag = true;
        try {
            Class stuCla = (Class) obj.getClass();// 得到类对象
            Field[] fs = stuCla.getDeclaredFields();//得到属性集合

            for (Field f : fs) {//遍历属性
                f.setAccessible(true); // 设置属性是可以访问的(私有的也可以)
                Object val = f.get(obj);// 得到此属性的值
                if(val!=null) {//只要有1个属性不为空,那么就不是所有的属性值都为空
                    flag = false;
                    break;
                }
            }
        }catch (Exception e){
            logger.info("判断对象的所有属性是否为空 错误！",e);
        }
        return flag;
    }

}
