package com.study.xuan.gifshow1.widget.stlview.util;

import android.content.Context;
import android.util.Log;

import com.study.xuan.gifshow1.widget.stlview.callback.OnReadListener;
import com.study.xuan.gifshow1.widget.stlview.model.STLModel;
import com.study.xuan.gifshow1.widget.stlview.widget.MeasureBeforeSTLView;
import com.study.xuan.gifshow1.widget.stlview.widget.STLViewBuilder;

/**
 * @author 刘成龙
 * @date 2021/1/21 09:28
 * @desc 3D模型中心数据工具类
 */
public class MeasureBeforeStlUtils {


    private static MeasureBeforeSTLView stlView;
    private static Context context;

    /**
     * 默认左腿平躺
     */
    private static MeasureBeforeStlDataEnum stlDataEnum = MeasureBeforeStlDataEnum.LC;


    public static void setStlDataEnum(MeasureBeforeStlDataEnum stlDataEnum) {
        if (MeasureBeforeStlUtils.stlDataEnum != stlDataEnum) {
            MeasureBeforeStlUtils.stlDataEnum = stlDataEnum;
            loadModel();
        }
    }

    public static MeasureBeforeStlDataEnum getStlDataEnum() {
        return stlDataEnum;
    }

    public static void init(Context context) {
        MeasureBeforeStlUtils.context = context;
    }

    public static void setStlFile(String fileName, String bFileName, String cFileName) {
         MeasureBeforeStlUtils.fileName = fileName;
        MeasureBeforeStlUtils.bFileName = bFileName;
         MeasureBeforeStlUtils.cFileName = cFileName;
        loadModel();
    }

    public static void setStlView(MeasureBeforeSTLView stlView) {
        MeasureBeforeStlUtils.stlView = stlView;
        show();
    }


    private static STLModel stlModel;
    private static STLModel bStlModel;
    private static STLModel cStlModel;

    public static STLModel getStlModel() {
        return stlModel;
    }

    public static STLModel getbStlModel() {
        return bStlModel;
    }

    public static STLModel getcStlModel() {
        return cStlModel;
    }

    private static String fileName;
    private static String bFileName;
    private static String cFileName;

    private static void loadModel() {
        if (context == null) {
            throw new RuntimeException("没有使用init()初始化");
        }
        if (fileName == null || bFileName == null) {
            throw new RuntimeException("要加载的文件名为空");
        }
        STLViewBuilder.init(new OnReadListener() {
                    @Override
                    public void onstart() {

                    }

                    @Override
                    public void onLoading(int cur, int total) {

                    }

                    @Override
                    public void onFinished(STLModel model) {
                        MeasureBeforeStlUtils.stlModel = model;
                        show();
                    }

                    @Override
                    public void onFailure(Exception e) {

                    }
                }, new OnReadListener() {
                    @Override
                    public void onstart() {

                    }

                    @Override
                    public void onLoading(int cur, int total) {
                    }

                    @Override
                    public void onFinished(STLModel model) {
                        Log.d("1111111", "onFinished: " + model);
                        MeasureBeforeStlUtils.bStlModel = model;
                        show();
                    }

                    @Override
                    public void onFailure(Exception e) {

                    }
                },new OnReadListener() {
                    @Override
                    public void onstart() {

                    }

                    @Override
                    public void onLoading(int cur, int total) {
                    }

                    @Override
                    public void onFinished(STLModel model) {
                        Log.d("333333", "onFinished: " + model);
                        MeasureBeforeStlUtils.cStlModel = model;
                        show();
                    }

                    @Override
                    public void onFailure(Exception e) {

                    }
                }).assets(context, fileName)
                .bAssets(context, bFileName)
                .cAssets(context, cFileName)
                .build(3);

    }

    private static void show() {
        if (stlView != null) {
            stlView.setModel();
        }
    }
}
