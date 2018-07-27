//package com.chengqianyun.eeweb2networkadmin.test;
//
//
//import com.itextpdf.text.Document;
//import com.itextpdf.text.Font;
//import com.itextpdf.text.Paragraph;
//import com.itextpdf.text.pdf.BaseFont;
//import com.itextpdf.text.pdf.PdfWriter;
//import java.io.FileOutputStream;
//
///**
// * @author 聂鹏
// * @version 1.0
// * @date 18/7/27
// */
//
//public class TestPdf {
//
//  public static void main(String[] args) {
//    TestPdf t = new TestPdf();
//    t.test1_1();
//  }
//
//  public void test1_1() {
//    BaseFont bf;
//    Font font = null;
//    try {
//      bf = BaseFont.createFont("STSong-Light", "UniGB-UCS2-H",BaseFont.NOT_EMBEDDED);//创建字体
//      font = new Font(bf, 12);//使用字体
//    } catch (Exception e) {
//      e.printStackTrace();
//    }
//    Document document = new Document();
//    try {
//      PdfWriter.getInstance(document, new FileOutputStream("/Users/lsb/data/tmp/pdfFolder/2.pdf"));
//      document.open();
//      document.add(new Paragraph("hello word 你好 世界", font));//引用字体
//      document.close();
//    } catch (Exception e) {
//      System.out.println("file create exception");
//    }
//  }
//
//
//}