package com.ijoin.ihpas.utils;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;


import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.format.Colour;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;

public class ExcelUtil {

    //可写字体
    private static WritableFont arial14font = null;
    //单元格格式
    private static WritableCellFormat arial14format = null;

    private static WritableFont arial10font = null;
    private static WritableCellFormat arial10format = null;
    private static WritableFont arial12font = null;
    private static WritableCellFormat arial12format = null;
    private final static String UTF8_ENCODING = "UTF-8";

    /**
     * 单元格的格式设置 字体大小 颜色 对齐方式、背景颜色等...
     */
    private static void format() {
        try {

            //字体 ARIAL， 字号 14  bold  粗体
            arial14font = new WritableFont(WritableFont.ARIAL, 14, WritableFont.BOLD);
            //字体的颜色
            arial14font.setColour(Colour.LIGHT_BLUE);
            //初始化单元格格式
            arial14format = new WritableCellFormat(arial14font);
            //对齐方式
            arial14format.setAlignment(jxl.format.Alignment.CENTRE);
            //边框的格式
            arial14format.setBorder(jxl.format.Border.ALL, jxl.format.BorderLineStyle.THIN);
            //底色
            arial14format.setBackground(Colour.VERY_LIGHT_YELLOW);
            //字体 ARIAL， 字号 10  bold  粗体
            arial10font = new WritableFont(WritableFont.ARIAL, 10, WritableFont.BOLD);
            //初始化单元格格式
            arial10format = new WritableCellFormat(arial10font);
            //对齐方式
            arial10format.setAlignment(jxl.format.Alignment.CENTRE);
            //边框的格式
            arial10format.setBorder(jxl.format.Border.ALL, jxl.format.BorderLineStyle.THIN);
            //底色
            arial10format.setBackground(Colour.GRAY_25);

            arial12font = new WritableFont(WritableFont.ARIAL, 10);
            //初始化单元格格式
            arial12format = new WritableCellFormat(arial12font);
            //对齐格式
            arial10format.setAlignment(jxl.format.Alignment.CENTRE);
            //设置边框
            arial12format.setBorder(jxl.format.Border.ALL, jxl.format.BorderLineStyle.THIN);

        } catch (WriteException e) {
            e.printStackTrace();
        }
    }


    /**
     * 初始化Excel表格
     *  写入字段名称，表名
     *
     * @param filePath  存放excel文件的路径（path/demo.xls）
     * @param sheetName Excel表格的表名
     * @param colName   excel中包含的列名（可以有多个）
     */
    public static void initExcel(String filePath, String sheetName, String[] colName) {
        format();
        //创建一个工作薄，就是整个Excel文档
        WritableWorkbook workbook = null;
        try {
            File file = new File(filePath);
            if (!file.exists()) {
                file.createNewFile();
            } else {
                return;
            }
            //使用Workbook创建一个工作薄，就是整个Excel文档
            workbook = Workbook.createWorkbook(file);
            //设置表格的名称(两个参数分别是工作表名字和插入位置，这个位置从0开始)
            WritableSheet sheet = workbook.createSheet(sheetName, 0);
            //创建label标签：实际就是单元格的标签（三个参数分别是：col + 1列，row + 1行， 内容， 单元格格式）
            //设置第一行的单元格标签为：标题
            Label label = new Label(0, 0, filePath, arial14format);
            //将标签加入到工作表中
            sheet.addCell(label);
            //再同一个单元格中写入数据，上一个数据会被下一个数据覆盖
            for (int col = 0; col < colName.length; col++) {
                sheet.addCell(new Label(col, 0, colName[col], arial10format));
            }
            //设置行高 参数的意义为（第几行， 行高）
            sheet.setRowView(0, 340);
            // 写入数据
            workbook.write();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (workbook != null) {
                try {
                    // 关闭文件
                    workbook.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 将制定类型的List写入Excel中
     *
     * @param <T>
     * @param  measureDataA,gaugenameA,cutRollAngleA,cutPitchingAngleA,cutAzimuthAngleA,timeRollAngleA,timePitchingAngleA,timeAzimuthAngleA,
     *         magnetismRadiiShortA,timeMagnetismRadiiA,cutMagnetismRadiiA  待写入的list
     * @param  measureDataB,gaugenameB,cutRollAngleB,cutPitchingAngleB,cutAzimuthAngleB,timeRollAngleB,timePitchingAngleB,timeAzimuthAngleB,
     *         magnetismRadiiShortB,timeMagnetismRadiiB,cutMagnetismRadiiB  待写入的list
     * @param fileName
     * @param mContext
     */
    @SuppressWarnings("unchecked")
    public static <T> void writeObjListToExcel(@NonNull ArrayList<String> measureDataA, @NonNull ArrayList<String> gaugenameA , @NonNull ArrayList<String> cutRollAngleA,
                                               @NonNull ArrayList<String> cutPitchingAngleA, @NonNull  ArrayList<String> cutAzimuthAngleA,
                                               @NonNull  ArrayList<String> timeRollAngleA, @NonNull  ArrayList<String> timePitchingAngleA,
                                               @NonNull  ArrayList<String> timeAzimuthAngleA, @NonNull  ArrayList<String> magnetismRadiiShortA,
                                               @NonNull  ArrayList<String> timeMagnetismRadiiA, @NonNull  ArrayList<String> cutMagnetismRadiiA,
                                               @NonNull ArrayList<String> measureDataB, @NonNull ArrayList<String> gaugenameB , @NonNull ArrayList<String>cutRollAngleB,
                                               @NonNull ArrayList<String> cutPitchingAngleB, @NonNull  ArrayList<String> cutAzimuthAngleB,
                                               @NonNull  ArrayList<String> timeRollAngleB, @NonNull  ArrayList<String> timePitchingAngleB,
                                               @NonNull  ArrayList<String> timeAzimuthAngleB, @NonNull  ArrayList<String> magnetismRadiiShortB,
                                               @NonNull  ArrayList<String> timeMagnetismRadiiB, @NonNull  ArrayList<String> cutMagnetismRadiiB, String fileName, Context mContext ) {
        int listsize = 0;
        if (measureDataA != null && measureDataA.size() > 0 && gaugenameA != null && gaugenameA.size() > 0
                && cutPitchingAngleA != null && cutPitchingAngleA.size() > 0 && timePitchingAngleA != null && timePitchingAngleA.size() > 0
                && magnetismRadiiShortA != null && magnetismRadiiShortA.size() > 0
                && measureDataB != null && measureDataB.size() > 0 && gaugenameB != null && gaugenameB.size() > 0
                && cutPitchingAngleB != null && cutPitchingAngleB.size() > 0 && timePitchingAngleB != null && timePitchingAngleB.size() > 0
                && magnetismRadiiShortB != null && magnetismRadiiShortB.size() > 0 ){
            //创建一个工作薄，就是整个Excel文档
            WritableWorkbook writebook = null;
            InputStream in = null;
            try {
                WorkbookSettings setEncode = new WorkbookSettings();
                setEncode.setEncoding(UTF8_ENCODING);

                in = new FileInputStream(new File(fileName));
                //创建一个工作薄，就是整个Excel文档
                Workbook workbook = Workbook.getWorkbook(in);
                //创建一个工作薄，就是整个Excel文档
                writebook = Workbook.createWorkbook(new File(fileName), workbook);
                //读取表格
                WritableSheet sheet = writebook.getSheet(0);

                if(measureDataA.size()>=cutRollAngleA.size()){
                    listsize =cutRollAngleA.size();
                }else{
                    listsize =measureDataA.size();
                }
                for (int j = 0; j < listsize; j++) {
                    List<String> list = new ArrayList<>();
                    // MyDataDTO 自定义实体类
                    list.add(measureDataA.get(j));
                    list.add(gaugenameA.get(j));
                    list.add(cutRollAngleA.get(j));
                    list.add(cutPitchingAngleA.get(j));
                    list.add(cutAzimuthAngleA.get(j));
                    list.add(timeRollAngleA.get(j));
                    list.add(timePitchingAngleA.get(j));
                    list.add(timeAzimuthAngleA.get(j));
                    list.add(magnetismRadiiShortA.get(j));
                    list.add(timeMagnetismRadiiA.get(j));
                    list.add(cutMagnetismRadiiA.get(j));

                    list.add(measureDataB.get(j));
                    list.add(gaugenameB.get(j));
                    list.add(cutRollAngleB.get(j));
                    list.add(cutPitchingAngleB.get(j));
                    list.add(cutAzimuthAngleB.get(j));
                    list.add(timeRollAngleB.get(j));
                    list.add(timePitchingAngleB.get(j));
                    list.add(timeAzimuthAngleB.get(j));
                    list.add(magnetismRadiiShortB.get(j));
                    list.add(timeMagnetismRadiiB.get(j));
                    list.add(cutMagnetismRadiiB.get(j));

                    for (int i = 0; i < list.size(); i++) {
                        sheet.addCell(new Label(i, j + 1, list.get(i), arial12format));
                        if (list.get(i).length() <= 4) {
                            //设置列宽
                            sheet.setColumnView(i, list.get(i).length() + 10);
                        } else {
                            //设置列宽
                            sheet.setColumnView(i, list.get(i).length() + 10);
                        }
                    }
                    //设置行高
                    sheet.setRowView(j + 1, 350);
                }

                writebook.write();
                workbook.close();
                Log.e("ExcelUtil","导出Excel成功");
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (writebook != null) {
                    try {
                        writebook.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
                if (in != null) {
                    try {
                        in.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

        }
    }

}

