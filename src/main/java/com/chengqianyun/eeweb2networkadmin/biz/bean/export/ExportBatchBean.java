package com.chengqianyun.eeweb2networkadmin.biz.bean.export;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class ExportBatchBean {

    /**
     * excel 的 sheet名称
     */
    private String sheetName;

    /**
     * 表格内容中:标题名
     */
    private String title;

    /**
     * 表格内容中:标题合并列数量
     */
    private int titleMergeCol;

    /**
     * 表格中头部分汇总内容，可以为空
     */
    private List<String[]> headers;

    /**
     * 表格数据列标题行
     */
    private String[] dataHeaders;

    /**
     * 数据值
     */
    private List<String[]> dataValue;

    /**
     * 需要标记的颜色的数据，key 是 "行下标,列下标"  值是 对象(方便扩展)
     */
    private Map<String, MarkStyleBean> markStyleBeanMap;

}
