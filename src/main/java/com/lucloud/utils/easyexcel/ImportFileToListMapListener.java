package com.lucloud.utils.easyexcel;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.excel.metadata.CellExtra;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 解析导入文件数据listener，结果输出为List<LinkedHashMap<Integer,String>>
 * @author jack
 * @since 2022/10/10
 */
@Slf4j
public class ImportFileToListMapListener extends AnalysisEventListener<LinkedHashMap<Integer,String>> {
    private static final Logger logger = LoggerFactory.getLogger(ImportFileToListMapListener.class);

    public ImportFileToListMapListener(List<LinkedHashMap<Integer,String>> hashMaps,Map head) {
        this.head = head;
        this.data = hashMaps;
    }

    //表头行数
    private Integer headRowNumber;
    //结果容器
    private List<LinkedHashMap<Integer,String>> data;

    private Map head;

    /**
     * 合并单元格读取处理
     */
    @Override
    public void invoke(LinkedHashMap<Integer,String> map , AnalysisContext analysisContext) {
        data.add(map);
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {

    }

    @Override
    public void invokeHeadMap(Map headMap, AnalysisContext context) {
        head.putAll(headMap);
        log.info("解析到的表头数据: {}", headMap);
    }

    @Override
    public void extra(CellExtra extra, AnalysisContext context) {
    }

}
