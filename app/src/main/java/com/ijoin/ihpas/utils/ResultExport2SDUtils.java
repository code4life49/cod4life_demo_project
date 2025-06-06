package com.ijoin.ihpas.utils;

import android.content.Context;
import android.os.Environment;
import android.support.annotation.NonNull;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;

/**
 * 关于数据保存的操作工具类
 * Created by admin on 2017/4/24.
 */
public class ResultExport2SDUtils {


    public interface DirCallBack {
        void dir(File imgFile);
    }

    /**
     * Created by zengtiantian on 2023/03/28
     * 保存为Excel格式
     * @param    measureDataA,gaugenameA,cutRollAngleA,cutPitchingAngleA,cutAzimuthAngleA,timeRollAngleA,timePitchingAngleA,timeAzimuthAngleA,
     *           magnetismRadiiShortA,timeMagnetismRadiiA,cutMagnetismRadiiA  待写入的list
     * @param     measureDataB,gaugenameB,cutRollAngleB,cutPitchingAngleB,cutAzimuthAngleB,timeRollAngleB,timePitchingAngleB,timeAzimuthAngleB,
 *           magnetismRadiiShortB,timeMagnetismRadiiB,cutMagnetismRadiiB  待写入的list
     * @param mContext
     * @return 是否成功写入到sd卡中
     */
    public static boolean saveExcel(@NonNull ArrayList<String> measureDataA, @NonNull ArrayList<String> gaugenameA , @NonNull ArrayList<String> cutRollAngleA,
                                    @NonNull ArrayList<String> cutPitchingAngleA, @NonNull  ArrayList<String> cutAzimuthAngleA,
                                    @NonNull  ArrayList<String> timeRollAngleA, @NonNull  ArrayList<String> timePitchingAngleA,
                                    @NonNull  ArrayList<String> timeAzimuthAngleA, @NonNull  ArrayList<String> magnetismRadiiShortA,
                                    @NonNull  ArrayList<String> timeMagnetismRadiiA, @NonNull  ArrayList<String> cutMagnetismRadiiA,
                                    @NonNull ArrayList<String> measureDataB, @NonNull ArrayList<String> gaugenameB , @NonNull ArrayList<String>cutRollAngleB,
                                    @NonNull ArrayList<String> cutPitchingAngleB, @NonNull  ArrayList<String> cutAzimuthAngleB,
                                    @NonNull  ArrayList<String> timeRollAngleB, @NonNull  ArrayList<String> timePitchingAngleB,
                                    @NonNull  ArrayList<String> timeAzimuthAngleB, @NonNull  ArrayList<String> magnetismRadiiShortB,
                                    @NonNull  ArrayList<String> timeMagnetismRadiiB, @NonNull  ArrayList<String> cutMagnetismRadiiB,String savecurrentDate
                                    ,Context mContext){


        File excelfile = new File(Environment.getExternalStorageDirectory() + "/爱乔医疗交互软件(IHPAS)/"+savecurrentDate);
        String fileName = savecurrentDate+"测量数据"+".xlsx";
        if (!excelfile.exists()){
            excelfile.mkdirs();
        }
        File file = new File(excelfile, fileName);
        //设置Excel第一行表头
        String[] title = {"测量时间", "产品名称","截取的横滚角", "截取的俯仰角","截取的方位角","实时的横滚角", "实时的俯仰角","实时的方位角",
                "磁力半径差","实时磁力半径","校准磁力半径"
                ,"测量时间", "产品名称","截取的横滚角", "截取的俯仰角","截取的方位角","实时的横滚角", "实时的俯仰角","实时的方位角",
                "磁力半径差","实时磁力半径","校准磁力半径"};

        String sheetName = "假体位置测量仪测量数据";
        String filePaths = excelfile + "/"+fileName ;
        ExcelUtil.initExcel(filePaths, sheetName, title);
        ExcelUtil.writeObjListToExcel(measureDataA,gaugenameA,cutRollAngleA,cutPitchingAngleA,cutAzimuthAngleA,
                timeRollAngleA,timePitchingAngleA,timeAzimuthAngleA,magnetismRadiiShortA,timeMagnetismRadiiA,
                cutMagnetismRadiiA,measureDataB,gaugenameB,cutRollAngleB,cutPitchingAngleB,cutAzimuthAngleB,
                timeRollAngleB,timePitchingAngleB,timeAzimuthAngleB,magnetismRadiiShortB,timeMagnetismRadiiB,
                cutMagnetismRadiiB, filePaths, mContext);

        return true;
    }



    /**
     * 将结果导出到SD卡当中
     *
     * @param time 操作时候的时间
     * @return 是否成功保存
     */
    public static boolean saveGSResultFile(String time ,float defaultTopRake, float defaultAbductionAngle,
                                           String measureTopRake,String measureAbductionAngle ) {
        String path = Environment.getExternalStorageDirectory() + "/爱乔医疗交互软件(IHPAS)/" + time;
        String fileName = time + "测量数据" + ".docx";
        String  filenum = "时间："+time+"\n产品："+"髋关节定位系统"+"\n预设值：\n"+"   前倾角："+defaultTopRake+"°"+"\n   外展角："+defaultAbductionAngle+"°"+
                "\n测量值：\n"+"   前倾角："+measureTopRake+"°"+"\n   外展角："+measureAbductionAngle+"°";

        try {

            File dir = new File(path);
            if (dir.exists() || dir.mkdirs()) {
                File file = new File(path, fileName);
                file.setReadOnly();
                FileOutputStream outputStream = new FileOutputStream(file);

                outputStream.write(filenum.getBytes());
                outputStream.close();
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
        }



    }
