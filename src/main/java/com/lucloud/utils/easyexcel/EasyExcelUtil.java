package com.lucloud.utils.easyexcel;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.support.ExcelTypeEnum;
import com.alibaba.excel.write.metadata.style.WriteCellStyle;
import com.alibaba.excel.write.metadata.style.WriteFont;
import com.alibaba.excel.write.style.HorizontalCellStyleStrategy;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.List;

/**
 * @author Max
 * @since 2020/5/26
 */
public class EasyExcelUtil {
    private static final Logger logger = LoggerFactory.getLogger(EasyExcelUtil.class);

    /**
     * 读取Excel
     *
     * @param ossPath oss文件地址
     * @param clazz   实体类
     * @return List<T>
     * @author Max
     * @since 2020/5/26 18:11
     */
    public static <T> List<T> readExcel(String ossPath, Class<T> clazz) {
        EasyExcelListener listener = new EasyExcelListener();
        return EasyExcel.read(EasyExcelListener.getFileInputStream(ossPath), clazz, listener).sheet().doReadSync();
    }

    /**
     * 读取Excel
     *
     * @param ossPath oss文件地址
     * @param clazz   实体类
     * @return EasyExcelListener 监听器：  包含 isCorrectFormat：解析是否正确，resultMessage：解析结果提示信息，list：解析数据列表
     * @author lemon
     * @since 2020/5/27
     */
    public static <T> EasyExcelListener readExcel2(String ossPath, Class<T> clazz) {
        EasyExcelListener listener = new EasyExcelListener();

        try {
            EasyExcel.read(EasyExcelListener.getFileInputStream(ossPath), clazz, listener).sheet().doRead();
        } catch (Exception e) {
            logger.info("读取网络文件异常！" + e);
            listener.setCorrectFormat(false);
            listener.setResultMessage("上传文件有误，请刷新后按照要求重新上传！");
        }
        return listener;
    }

    /**
     * 无填充方式导出并下载xlsx
     *
     * @param excelName 文件名
     * @param response
     * @param cla       导出的模板class
     * @param list      模板list数据
     * @return void
     * @author jack
     * @since 2022/10/8 15:49
     **/
    public static void downloadUnfilledToXlsx(String excelName, HttpServletResponse response, Class cla, List list) throws Exception {
        // 表头样式
        WriteCellStyle headWriteCellStyle = new WriteCellStyle();
        // 设置表头居中对齐 字体颜色为白色 字号 11
        headWriteCellStyle.setHorizontalAlignment(HorizontalAlignment.CENTER);
        WriteFont headWriteFont = new WriteFont();
        headWriteFont.setFontHeightInPoints((short)11);
        headWriteFont.setBold(true);
        headWriteFont.setColor(IndexedColors.WHITE.getIndex());
        headWriteCellStyle.setWriteFont(headWriteFont);
        // 内容样式
        WriteCellStyle contentWriteCellStyle = new WriteCellStyle();
        // 设置内容靠左对齐
        contentWriteCellStyle.setHorizontalAlignment(HorizontalAlignment.LEFT);
        HorizontalCellStyleStrategy horizontalCellStyleStrategy =
                new HorizontalCellStyleStrategy(headWriteCellStyle, contentWriteCellStyle);
        EasyExcel.write(getOutputStream(excelName, response), cla)
                .excelType(ExcelTypeEnum.XLSX).sheet("sheet1")
                .registerWriteHandler(horizontalCellStyleStrategy)
                //设置默认样式及写入头信息开始的行数
                .useDefaultStyle(true).relativeHeadRowIndex(2)
                .doWrite(list);

    }

    public static OutputStream getOutputStream(String fileName, HttpServletResponse response) throws Exception {
        fileName = URLEncoder.encode(fileName, "UTF-8");
        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding("utf-8");
        response.setHeader("Content-disposition", "attachment;filename=" + fileName + ".xlsx");
        //响应头添加一个文件名，用于前台js下载的时候使用
        response.addHeader("filename", fileName);
        return response.getOutputStream();
    }


}
