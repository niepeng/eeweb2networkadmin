package com.chengqianyun.eeweb2networkadmin.biz.bean.export;


import java.util.Collection;
import lombok.Data;

/**
 * @author 聂鹏
 * @version 1.0
 * @date 18/12/20
 */
@Data
public class ExportHelperBean<T> {

  /**
   * excel 的 sheet名称
   */
  private String sheetName;

  /**
   * 表格内容中:标题名
   */
  private String title;

  /**
   *  表格头部的内容(多行)headerDataList 和 标题栏列数等信
   */
  private HeaderContentBean headerContentBean;

  /**
   * 表格数据列标题行
   */
  private String[] dataHeaders;

  /**
   * 针对dataset中的具体的列
   */
  private String[] dataCols;

  /**
   *  需要显示的数据集合,集合中一定要放置符合javabean风格的类的对象。此方法支持的
   *  javabean属性的数据类型有基本数据类型及String,Date,byte[](图片数据)
   */
  private Collection<T> dataset;

}