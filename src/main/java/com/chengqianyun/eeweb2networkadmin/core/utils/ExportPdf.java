package com.chengqianyun.eeweb2networkadmin.core.utils;

import com.chengqianyun.eeweb2networkadmin.biz.bean.export.HeaderContentBean;
import com.lowagie.text.BadElementException;
import com.lowagie.text.Cell;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.PageSize;
import com.lowagie.text.Phrase;
import com.lowagie.text.Table;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfWriter;
import java.awt.Color;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;

/**
 * @author 聂鹏
 * @version 1.0
 * @date 18/7/27
 */

public class ExportPdf<T> {

  private String widthPercent;

  /**
   * 这是一个通用的方法，利用了JAVA的反射机制，可以将放置在JAVA集合中并且符号一定条件的数据以pdf 的形式输出到指定IO设备上
   *
   * @param title
   *            表格标题名
   * @param headerContentBean
   *            表格头部的内容(多行)headerDataList 和 标题栏列数等信
   * @param dataHeaders
   *            表格数据列标题行
   * @param dataCols
   *            针对dataset中的具体的列
   * @param dataset
   *            需要显示的数据集合,集合中一定要放置符合javabean风格的类的对象。此方法支持的
   *            javabean属性的数据类型有基本数据类型及String,Date,byte[](图片数据)
   * @param out
   *            与输出设备关联的流对象，可以将EXCEL文档导出到本地文件或者网络中
   */
  public void exportPdf(String title, HeaderContentBean headerContentBean ,String[] dataHeaders, String[] dataCols, Collection<T> dataset, OutputStream out) throws Exception {

    // 最终表格的最大列的数量
    int colNum = Math.max(headerContentBean.getTitleCol(),dataHeaders.length);
    widthPercent = 100 / colNum + "%";

    // 创建Document对象(页面的大小为A4,左、右、上、下的页边距为10)
    Document document = new Document(PageSize.A4, 10, 10, 10, 10);
    // 建立书写器
    PdfWriter.getInstance(document, out);
    // 设置相关的参数
    setParameters(document, title);
    // 打开文档
    document.open();

    Font normalFont = getFont(8);
    //创建一个有colNum列的表格
    Table table = new Table(colNum);
    table.setBorderWidth(1);
    table.setBorderColor(new Color(0, 0, 0));
    table.setPadding(2);
    Cell emptyCell = genCell("", normalFont, widthPercent);

    // 1.表格标题
    Cell titleCell = genCell(title, getFont(15), widthPercent);
    titleCell.setColspan(colNum);
    table.addCell(titleCell);

    // 2.表格头部内容
    List<String[]> headDataList = headerContentBean.getHeadDataList();
    for (int i = 0, size = headDataList.size(); i < size; i++) {
      for (short k = 0; k < headDataList.get(i).length; k++) {
        table.addCell(genCell(headDataList.get(i)[k], normalFont, widthPercent));
      }
      // 补充空元素
      int tmpCol = headDataList.get(i).length;
      while (tmpCol < colNum) {
        table.addCell(emptyCell);
        tmpCol++;
      }
    }

    // 3.1空一行
    Cell emptyCellLine = genCell("", normalFont, widthPercent);
    emptyCellLine.setColspan(colNum);
    table.addCell(emptyCellLine);

    // 3.2 数据标题行
    for (short j = 0; j < dataHeaders.length; j ++) {
      table.addCell(genCell(dataHeaders[j], normalFont, widthPercent));
    }
    // 补充空元素
    int tmpCol = dataHeaders.length;
    while (tmpCol < colNum) {
      table.addCell(emptyCell);
      tmpCol++;
    }

    // 固定头部
    table.endHeaders();


    Iterator<T> it = dataset.iterator();
    String tmpValue = null;
    String tmpMin = null;
    String tmpMax = null;

    int num = 1;
    while (it.hasNext()) {
      T t = (T) it.next();
      // 利用反射，根据javabean属性的先后顺序，动态调用getXxx()方法得到属性值
      for (short j = 0; j < dataCols.length; j ++) {
        // 需要找到标记最低和最高的样式颜色
        if (j == 0) {
          tmpValue = String.valueOf(num++);
          table.addCell(genCell(tmpValue, normalFont, widthPercent));
          continue;
        }

        tmpValue = ReflectUtil.getStringValue(t, dataCols[j]);
        if (dataCols[j].indexOf("MinStr") > 0) {
          tmpMin = ReflectUtil.getStringValue(headerContentBean, dataCols[j].substring(0, dataCols[j].indexOf("MinStr")) + "Min");
          if (tmpValue.equals(tmpMin)) {
            table.addCell(genCell(tmpValue, getFont(8, Color.blue), widthPercent));
            continue;
          }
          table.addCell(genCell(tmpValue, normalFont, widthPercent));
          continue;
        }

        if (dataCols[j].indexOf("MaxStr") > 0) {
          tmpMax = ReflectUtil.getStringValue(headerContentBean, dataCols[j].substring(0, dataCols[j].indexOf("MaxStr")) + "Max");
          if (tmpValue.equals(tmpMax)) {
            table.addCell(genCell(tmpValue, getFont(8, Color.red), widthPercent));
            continue;
          }
          table.addCell(genCell(tmpValue, normalFont, widthPercent));
          continue;
        }

        table.addCell(genCell(tmpValue, normalFont, widthPercent));
      }

      // 补充空元素
      tmpCol = dataCols.length;
      while (tmpCol < colNum) {
        table.addCell(emptyCell);
        tmpCol++;
      }
    }


    document.add(table);
    // 关闭文档
    document.close();




  }

  /**
   * 设置cell
   * @param name
   * @return
   * @throws BadElementException
   */
  public static Cell genCell(String name, Font font, String widthPercent) throws BadElementException{

    Cell cell = new Cell(new Phrase(name, font));
    cell.setWidth(widthPercent);
    //单元格水平对齐方式
    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
    //单元格垂直对齐方式
    cell.setVerticalAlignment(Element.ALIGN_CENTER);
//        cell.setHeader(true);
    cell.setBackgroundColor(Color.white);
    return cell;
  }


  public static Font getFont(int size, Color color) {
    BaseFont baseFont = null;
    try {
      baseFont = BaseFont.createFont("STSong-Light", "UniGB-UCS2-H", BaseFont.NOT_EMBEDDED);
    } catch (DocumentException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }
    Font font = new Font(baseFont, size, Font.NORMAL, color);
    return font;
  }

  public static Font getFont(int size) {
    return getFont(size, Color.black);
  }


  /**
   * 设置相关参数
   * @param document
   * @return
   */
  public static void setParameters(Document document, String title){
    // 设置标题
    document.addTitle(title);
    // 设置主题
    document.addSubject("eeweb导出数据");
    // 设置作者
    document.addKeywords("eeweb数据");
    // 设置作者
    document.addAuthor("eeweb");
    // 设置创建者
    document.addCreator("eeweb");
    // 设置生产者
    document.addProducer();
    // 设置创建日期
    document.addCreationDate();
  }


}