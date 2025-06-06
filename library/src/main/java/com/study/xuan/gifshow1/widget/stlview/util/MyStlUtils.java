package com.study.xuan.gifshow1.widget.stlview.util;

import android.content.Context;
import android.util.Log;

import com.study.xuan.gifshow1.widget.stlview.callback.OnReadListener;
import com.study.xuan.gifshow1.widget.stlview.model.STLModel;
import com.study.xuan.gifshow1.widget.stlview.widget.STLView;
import com.study.xuan.gifshow1.widget.stlview.widget.STLViewBuilder;

/**
 * @author 刘成龙
 * @date 2021/1/21 09:28
 * @desc 3D模型中心数据工具类
 */
public class MyStlUtils {


    private static STLView stlView;
    private static Context context;

    /**
     * 默认左腿平躺
     */
    private static StlDataEnum stlDataEnum = StlDataEnum.LC;


    public static void setStlDataEnum(StlDataEnum stlDataEnum) {
        if (MyStlUtils.stlDataEnum != stlDataEnum) {
            MyStlUtils.stlDataEnum = stlDataEnum;
            loadModel();
        }
    }

    public static StlDataEnum getStlDataEnum() {
        return stlDataEnum;
    }

    public static void init(Context context) {
        MyStlUtils.context = context;
    }

    public static void setStlFile(String fileName, String bFileName) {
         MyStlUtils.fileName = fileName;
        MyStlUtils.bFileName = bFileName;
        loadModel();
    }

    public static void setStlView(STLView stlView) {
        MyStlUtils.stlView = stlView;
        show();
    }


    private static STLModel stlModel;
    private static STLModel bStlModel;

    public static STLModel getStlModel() {
        return stlModel;
    }

    public static STLModel getbStlModel() {
        return bStlModel;
    }

    private static String fileName;
    private static String bFileName;

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
                        MyStlUtils.stlModel = model;
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
                        MyStlUtils.bStlModel = model;
                        show();
                    }

                    @Override
                    public void onFailure(Exception e) {

                    }
                },null)
                .assets(context, fileName)
                .bAssets(context, bFileName)
                .build(1);

    }

    private static void show() {
        if (stlView != null) {
            stlView.setModel();
        }
    }
}
