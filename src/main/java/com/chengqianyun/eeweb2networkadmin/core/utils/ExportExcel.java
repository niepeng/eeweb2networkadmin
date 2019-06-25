package com.chengqianyun.eeweb2networkadmin.core.utils;

import com.chengqianyun.eeweb2networkadmin.biz.bean.export.ExportBatchBean;
import com.chengqianyun.eeweb2networkadmin.biz.bean.export.ExportHelperBean;
import com.chengqianyun.eeweb2networkadmin.biz.bean.export.HeaderContentBean;
import com.chengqianyun.eeweb2networkadmin.biz.bean.export.HistoryListBean;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFClientAnchor;
import org.apache.poi.hssf.usermodel.HSSFComment;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFPatriarch;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.hssf.util.HSSFColor.BLUE;
import org.apache.poi.hssf.util.HSSFColor.RED;
import org.apache.poi.hssf.util.Region;

/**
 * @author 聂鹏
 * @version 1.0
 * @date 17/10/25
 */
@Slf4j
public class ExportExcel<T> {

  public void write(HSSFWorkbook book, OutputStream out) {
    try {
      book.write(out);
      out.flush();
      out.close();
      book.close();
    } catch (IOException e) {
      log.info("export excel,{}",e);
      e.printStackTrace();
    }
  }

  public HSSFWorkbook exportExcel2(HSSFWorkbook workbook, ExportBatchBean exportHelperBean) {

    // 生成一个表格
    HSSFSheet sheet = workbook.createSheet(exportHelperBean.getSheetName());
    // 设置表格默认列宽度为15个字节
    sheet.setDefaultColumnWidth((short) 30);
    // 生成一个样式
    HSSFCellStyle style = workbook.createCellStyle();
    style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
    HSSFFont font = workbook.createFont();
    font.setFontHeightInPoints((short) 10);
//    font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
    // 把字体应用到当前的样式
    style.setFont(font);


    // 生成并设置另一个样式
    HSSFCellStyle style2 = workbook.createCellStyle();
    style2.setAlignment(HSSFCellStyle.ALIGN_CENTER);
    style2.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
    // 生成另一个字体
    HSSFFont font2 = workbook.createFont();
//    font2.setBoldweight(HSSFFont.BOLDWEIGHT_NORMAL);
    // 把字体应用到当前的样式
    style2.setFont(font2);


    HSSFCellStyle lowStyle = workbook.createCellStyle();
    lowStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
    HSSFFont lowFont = workbook.createFont();
    lowFont.setColor(BLUE.index);
    lowStyle.setFont(lowFont);

    HSSFCellStyle highStyle = workbook.createCellStyle();
    highStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
    HSSFFont heighFont = workbook.createFont();
    heighFont.setColor(RED.index);
    highStyle.setFont(heighFont);


    // 声明一个画图的顶级管理器
    HSSFPatriarch patriarch = sheet.createDrawingPatriarch();
    // 定义注释的大小和位置,详见文档
    HSSFComment comment = patriarch.createComment(new HSSFClientAnchor(0, 0, 0, 0, (short) 4, 2, (short) 6, 5));
    // 设置注释内容
    comment.setString(new HSSFRichTextString("导出数据"));
    // 设置注释作者，当鼠标移动到单元格上是可以在状态栏中看到该内容.
    comment.setAuthor("eeweb");

    // 1.产生表格标题行
    int rowIndex = 0;
    HSSFRow row = sheet.createRow(rowIndex++);
    HSSFCell titleCell = row.createCell((short) (0));
    titleCell.setCellStyle(style);
    titleCell.setCellValue(exportHelperBean.getTitle());
//    sheet.addMergedRegion(new Region(
//        1, //first row (0-based)
//        (short)1, //first column  (0-based)
//        2, //last row (0-based)
//        (short)1  //last column  (0-based)
//    ));
    // // 四个参数分别是：起始行，起始列，结束行，结束列
    sheet.addMergedRegion(new Region(0,(short)0,0, (short)(exportHelperBean.getTitleMergeCol() -1)));

    // 2.表格头部内容
    List<String[]> headDataList = exportHelperBean.getHeaders();
    if(headDataList != null) {
      for (int i = 0, size = headDataList.size(); i < size; i++) {
        HSSFRow rowHeadData = sheet.createRow(rowIndex++);
        for (short k = 0; k < headDataList.get(i).length; k++) {
          HSSFCell cellHeadData = rowHeadData.createCell(k);
          cellHeadData.setCellStyle(style);
          cellHeadData.setCellValue(headDataList.get(i)[k]);
        }
      }
    }

    // 3.填充表格数据内容
    // 3.1 空一行
    sheet.createRow(rowIndex++);

    // 3.2 数据标题行
    HSSFRow cellHeadDataTitle = sheet.createRow(rowIndex++);
    for (short j = 0; j < exportHelperBean.getDataHeaders().length; j ++) {
      HSSFCell cellTemp = cellHeadDataTitle.createCell(j);
      cellTemp.setCellStyle(style2);
      cellTemp.setCellValue(exportHelperBean.getDataHeaders()[j]);
    }

    if(exportHelperBean.getDataValue() != null) {
      int num = 1;
      for(String[] dataArray : exportHelperBean.getDataValue()) {
        row = sheet.createRow(rowIndex++);
        for (int j = 0; j < dataArray.length + 1; j++) {
          HSSFCell cellTemp = row.createCell(j);
          cellTemp.setCellStyle(style2);
          if (j == 0) {
            cellTemp.setCellValue(String.valueOf(num++));
            continue;
          }
          cellTemp.setCellValue(dataArray[j-1]);
        }
      }
    }

//    // 3.3 遍历集合数据，产生数据行
//    Iterator<T> it = exportHelperBean.getDataset().iterator();
//    Object tmpValue = null;
//    String tmpMin = null;
//    String tmpMax = null;
//
//    int num = 1;
//    while (it.hasNext()) {
//      row = sheet.createRow(rowIndex++);
//      T t = (T) it.next();
//      // 利用反射，根据javabean属性的先后顺序，动态调用getXxx()方法得到属性值
//      for (short j = 0; j < exportHelperBean.getDataCols().length; j ++) {
//        // 需要找到标记最低和最高的样式颜色
//        HSSFCell cellTemp = row.createCell(j);
//        cellTemp.setCellStyle(style2);
//        if(j == 0) {
//          cellTemp.setCellValue(String.valueOf(num++));
//          continue;
//        }
//        tmpValue = ReflectUtil.getValue(t, exportHelperBean.getDataCols()[j]);
//        if(exportHelperBean.getDataCols()[j].indexOf("temp") >= 0) {
//          cellTemp.setCellValue(UnitUtil.chu100((Integer)(tmpValue)));
//          continue;
//        }
//        if(exportHelperBean.getDataCols()[j].indexOf("humi") >= 0) {
//          cellTemp.setCellValue(UnitUtil.chu100((Integer)(tmpValue)));
//          continue;
//        }
//        cellTemp.setCellValue(String.valueOf(tmpValue));
//      }
//    }

    return workbook;
  }

    /**
     * 这是一个通用的方法，利用了JAVA的反射机制，可以将放置在JAVA集合中并且符号一定条件的数据以EXCEL 的形式输出到指定IO设备上
     *
     */
  public HSSFWorkbook exportExcel2(HSSFWorkbook workbook, ExportHelperBean<T> exportHelperBean) {

//    String title, HeaderContentBean headerContentBean ,String[] dataHeaders, String[] dataCols, Collection<T> dataset, String sheetName

    // 生成一个表格
    HSSFSheet sheet = workbook.createSheet(exportHelperBean.getSheetName());
    // 设置表格默认列宽度为15个字节
    sheet.setDefaultColumnWidth((short) 30);
    // 生成一个样式
    HSSFCellStyle style = workbook.createCellStyle();
    style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
    HSSFFont font = workbook.createFont();
    font.setFontHeightInPoints((short) 10);
//    font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
    // 把字体应用到当前的样式
    style.setFont(font);


    // 生成并设置另一个样式
    HSSFCellStyle style2 = workbook.createCellStyle();
    style2.setAlignment(HSSFCellStyle.ALIGN_CENTER);
    style2.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
    // 生成另一个字体
    HSSFFont font2 = workbook.createFont();
//    font2.setBoldweight(HSSFFont.BOLDWEIGHT_NORMAL);
    // 把字体应用到当前的样式
    style2.setFont(font2);


    HSSFCellStyle lowStyle = workbook.createCellStyle();
    lowStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
    HSSFFont lowFont = workbook.createFont();
    lowFont.setColor(BLUE.index);
    lowStyle.setFont(lowFont);

    HSSFCellStyle highStyle = workbook.createCellStyle();
    highStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
    HSSFFont heighFont = workbook.createFont();
    heighFont.setColor(RED.index);
    highStyle.setFont(heighFont);


    // 声明一个画图的顶级管理器
    HSSFPatriarch patriarch = sheet.createDrawingPatriarch();
    // 定义注释的大小和位置,详见文档
    HSSFComment comment = patriarch.createComment(new HSSFClientAnchor(0, 0, 0, 0, (short) 4, 2, (short) 6, 5));
    // 设置注释内容
    comment.setString(new HSSFRichTextString("导出数据"));
    // 设置注释作者，当鼠标移动到单元格上是可以在状态栏中看到该内容.
    comment.setAuthor("eeweb");

    // 1.产生表格标题行
    int rowIndex = 0;
    HSSFRow row = sheet.createRow(rowIndex++);
    HSSFCell titleCell = row.createCell((short) (0));
    titleCell.setCellStyle(style);
    titleCell.setCellValue(exportHelperBean.getTitle());
//    sheet.addMergedRegion(new Region(
//        1, //first row (0-based)
//        (short)1, //first column  (0-based)
//        2, //last row (0-based)
//        (short)1  //last column  (0-based)
//    ));
    // // 四个参数分别是：起始行，起始列，结束行，结束列
    sheet.addMergedRegion(new Region(0,(short)0,0, (short)(exportHelperBean.getHeaderContentBean().getTitleCol() -1)));

    // 2.表格头部内容
    List<String[]> headDataList = exportHelperBean.getHeaderContentBean().getHeadDataList();
    for (int i = 0, size = headDataList.size(); i < size; i++) {
      HSSFRow rowHeadData = sheet.createRow(rowIndex++);
      for (short k = 0; k < headDataList.get(i).length; k++) {
        HSSFCell cellHeadData = rowHeadData.createCell(k);
        cellHeadData.setCellStyle(style);
        cellHeadData.setCellValue(headDataList.get(i)[k]);
      }
    }

    // 3.填充表格数据内容
    // 3.1 空一行
    sheet.createRow(rowIndex++);

    // 3.2 数据标题行
    HSSFRow cellHeadDataTitle = sheet.createRow(rowIndex++);
    for (short j = 0; j < exportHelperBean.getDataHeaders().length; j ++) {
      HSSFCell cellTemp = cellHeadDataTitle.createCell(j);
      cellTemp.setCellStyle(style2);
      cellTemp.setCellValue(exportHelperBean.getDataHeaders()[j]);
    }

    // 3.3 遍历集合数据，产生数据行
    Iterator<T> it = exportHelperBean.getDataset().iterator();
    String tmpValue = null;
    String tmpMin = null;
    String tmpMax = null;

    int num = 1;
    while (it.hasNext()) {
      row = sheet.createRow(rowIndex++);
      T t = (T) it.next();
      // 利用反射，根据javabean属性的先后顺序，动态调用getXxx()方法得到属性值
      for (short j = 0; j < exportHelperBean.getDataCols().length; j ++) {
        // 需要找到标记最低和最高的样式颜色
        HSSFCell cellTemp = row.createCell(j);
        cellTemp.setCellStyle(style2);
        if(j == 0) {
          cellTemp.setCellValue(String.valueOf(num++));
          continue;
        }
        tmpValue = ReflectUtil.getStringValue(t, exportHelperBean.getDataCols()[j]);

        if(exportHelperBean.getDataCols()[j].indexOf("MinStr") > 0) {
          tmpMin = ReflectUtil.getStringValue(exportHelperBean.getHeaderContentBean(), exportHelperBean.getDataCols()[j].substring(0,exportHelperBean.getDataCols()[j].indexOf("MinStr")) + "Min");
          if(tmpValue.equals(tmpMin)) {
            cellTemp.setCellStyle(lowStyle);
          }
        } else if (exportHelperBean.getDataCols()[j].indexOf("MaxStr") > 0) {
          tmpMax = ReflectUtil.getStringValue(exportHelperBean.getHeaderContentBean(), exportHelperBean.getDataCols()[j].substring(0,exportHelperBean.getDataCols()[j].indexOf("MaxStr")) + "Max");
          if(tmpValue.equals(tmpMax)) {
            cellTemp.setCellStyle(highStyle);
          }
        }
        cellTemp.setCellValue(tmpValue);
      }
    }

//    sheet.autoSizeColumn((short)0); //调整第一列宽度
//    sheet.autoSizeColumn((short)1); //调整第二列宽度
//    sheet.autoSizeColumn((short)2);
//    sheet.autoSizeColumn((short)3);
//    sheet.autoSizeColumn((short)4);

    return workbook;
  }





//  public void exportExcel(String title, String fileName, String[] headers, Collection<T> dataset,  HttpServletResponse response, String pattern) {
//    try {
//      OutputStream os = response.getOutputStream();
//      response.reset();
//      response.setHeader("Content-disposition", "attachment; filename=" + new String(fileName.getBytes("GB2312"), "ISO8859-1"));
//      response.setContentType("application/msexcel");// 定义输出类型
//      exportExcel(title, headers, dataset, os, pattern);
//    } catch (Exception e) {
//      log.info("export excel,{}",e);
//    }
//  }
//
//  /**
//   * 这是一个通用的方法，利用了JAVA的反射机制，可以将放置在JAVA集合中并且符号一定条件的数据以EXCEL 的形式输出到指定IO设备上
//   *
//   * @param title
//   *            表格标题名
//   * @param headers
//   *            表格属性列名数组
//   * @param dataset
//   *            需要显示的数据集合,集合中一定要放置符合javabean风格的类的对象。此方法支持的
//   *            javabean属性的数据类型有基本数据类型及String,Date,byte[](图片数据)
//   * @param out
//   *            与输出设备关联的流对象，可以将EXCEL文档导出到本地文件或者网络中
//   * @param pattern
//   *            如果有时间数据，设定输出格式。默认为"yyy-MM-dd"
//   */
//  public void exportExcel(String title, String[] headers, Collection<T> dataset, OutputStream out, String pattern) {
//    // 声明一个工作薄
//    HSSFWorkbook workbook = new HSSFWorkbook();
//    // 生成一个表格
//    HSSFSheet sheet = workbook.createSheet(title);
//    // 设置表格默认列宽度为15个字节
//    sheet.setDefaultColumnWidth((short) 15);
//    // 生成一个样式
//    HSSFCellStyle style = workbook.createCellStyle();
//    // 设置这些样式
//    style.setFillForegroundColor(HSSFColor.SKY_BLUE.index);
//    style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
//    style.setBorderBottom(HSSFCellStyle.BORDER_THIN);
//    style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
//    style.setBorderRight(HSSFCellStyle.BORDER_THIN);
//    style.setBorderTop(HSSFCellStyle.BORDER_THIN);
//    style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
//    // 生成一个字体
//    HSSFFont font = workbook.createFont();
//    font.setColor(HSSFColor.VIOLET.index);
//    font.setFontHeightInPoints((short) 12);
//    font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
//    // 把字体应用到当前的样式
//    style.setFont(font);
//    // 生成并设置另一个样式
//    HSSFCellStyle style2 = workbook.createCellStyle();
//    style2.setFillForegroundColor(HSSFColor.LIGHT_YELLOW.index);
//    style2.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
//    style2.setBorderBottom(HSSFCellStyle.BORDER_THIN);
//    style2.setBorderLeft(HSSFCellStyle.BORDER_THIN);
//    style2.setBorderRight(HSSFCellStyle.BORDER_THIN);
//    style2.setBorderTop(HSSFCellStyle.BORDER_THIN);
//    style2.setAlignment(HSSFCellStyle.ALIGN_CENTER);
//    style2.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
//    // 生成另一个字体
//    HSSFFont font2 = workbook.createFont();
//    font2.setBoldweight(HSSFFont.BOLDWEIGHT_NORMAL);
//    // 把字体应用到当前的样式
//    style2.setFont(font2);
//
//    // 声明一个画图的顶级管理器
//    HSSFPatriarch patriarch = sheet.createDrawingPatriarch();
//    // 定义注释的大小和位置,详见文档
//    HSSFComment comment = patriarch.createComment(new HSSFClientAnchor(0, 0, 0, 0, (short) 4, 2, (short) 6, 5));
//    // 设置注释内容
//    comment.setString(new HSSFRichTextString("金钱桔导出数据"));
//    // 设置注释作者，当鼠标移动到单元格上是可以在状态栏中看到该内容.
//    comment.setAuthor("huodian");
//
//    // 产生表格标题行
//    HSSFRow row = sheet.createRow(0);
//    for (short i = 0; i < headers.length; i++) {
//      HSSFCell cell = row.createCell(i);
//      cell.setCellStyle(style);
//      HSSFRichTextString text = new HSSFRichTextString(headers[i]);
//      cell.setCellValue(text);
//    }
//
//    if (pattern == null || pattern.length() == 0) {
//      pattern = default_date_format;
//    }
//
//    // 遍历集合数据，产生数据行
//    Iterator<T> it = dataset.iterator();
//    int index = 0;
//    while (it.hasNext()) {
//      index++;
//      row = sheet.createRow(index);
//      T t = (T) it.next();
//      // 利用反射，根据javabean属性的先后顺序，动态调用getXxx()方法得到属性值
//      Field[] fields = t.getClass().getDeclaredFields();
//      for (short i = 0; i < fields.length; i++) {
//        HSSFCell cell = row.createCell(i);
//        cell.setCellStyle(style2);
//        Field field = fields[i];
//        String fieldName = field.getName();
//        String getMethodName = "get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
//        try {
//          Class tCls = t.getClass();
//          Method getMethod = tCls.getMethod(getMethodName,new Class[]{});
//          Object value = getMethod.invoke(t, new Object[] {});
//          // 判断值的类型后进行强制类型转换
//          String textValue = null;
//          if (value instanceof Boolean) {
//            Boolean bValue = (Boolean) value;
//            if(bValue != null && bValue.booleanValue()) {
//              textValue = "true";
//            } else {
//              textValue = "false";
//            }
//          } else if (value instanceof Date) {
//            Date date = (Date) value;
//            SimpleDateFormat sdf = new SimpleDateFormat(pattern);
//            textValue = sdf.format(date);
//          } else if (value instanceof byte[]) {
//            // 有图片时，设置行高为60px;
//            row.setHeightInPoints(60);
//            // 设置图片所在列宽度为80px,注意这里单位的一个换算
//            sheet.setColumnWidth(i, (short) (35.7 * 80));
//            // sheet.autoSizeColumn(i);
//            byte[] bsValue = (byte[]) value;
//            HSSFClientAnchor anchor = new HSSFClientAnchor(0, 0, 1023, 255, (short) 6, index, (short) 6, index);
//            anchor.setAnchorType(2);
//            patriarch.createPicture(anchor, workbook.addPicture(bsValue, HSSFWorkbook.PICTURE_TYPE_JPEG));
//          } else {
//            // 其它数据类型都当作字符串简单处理
//            textValue = value == null ? "" : value.toString();
//          }
//          // 如果不是图片数据，就利用正则表达式判断textValue是否全部由数字组成
//          if (textValue != null) {
//            Matcher matcher = p.matcher(textValue);
//            if (matcher.matches()) {
//              // 是数字当作double处理
//              cell.setCellValue(Double.parseDouble(textValue));
//            } else {
//              HSSFRichTextString richString = new HSSFRichTextString(textValue);
//              HSSFFont font3 = workbook.createFont();
//              font3.setColor(HSSFColor.BLUE.index);
//              richString.applyFont(font3);
//              cell.setCellValue(richString);
////              cell.setCellValue(textValue);
//            }
//          }
//        } catch (SecurityException e) {
//          log.info("export excel,{}",e);
//          e.printStackTrace();
//        } catch (NoSuchMethodException e) {
//          log.info("export excel,{}",e);
//          e.printStackTrace();
//        } catch (IllegalArgumentException e) {
//          log.info("export excel,{}",e);
//          e.printStackTrace();
//        } catch (IllegalAccessException e) {
//          log.info("export excel,{}",e);
//          e.printStackTrace();
//        } catch (InvocationTargetException e) {
//          log.info("export excel,{}",e);
//          e.printStackTrace();
//        } catch(Exception e){
//          log.info("export excel,{}",e);
//        } finally{
//          // 清理资源
//        }
//      }
//    }
//    try {
//      workbook.write(out);
//      out.flush();
//      out.close();
//      workbook.close();
//    } catch (IOException e) {
//      log.info("export excel,{}",e);
//      e.printStackTrace();
//    }
//  }

}