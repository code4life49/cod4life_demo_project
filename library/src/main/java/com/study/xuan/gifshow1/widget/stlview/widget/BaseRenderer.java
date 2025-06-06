package com.study.xuan.gifshow1.widget.stlview.widget;

import android.opengl.GLSurfaceView.Renderer;
import android.opengl.GLU;
import android.util.Log;

import com.study.xuan.gifshow1.widget.stlview.util.MyStlUtils;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.List;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * Author : xuan.
 * Date : 2017/12/14.
 * Description : 渲染器
 */


public abstract class BaseRenderer implements Renderer {
    private static final String TAG = "STLReader";

    public static final int FRAME_BUFFER_COUNT = 5;
    //ontouch
    public float angleX;
    public float angleY;
    public float angleZ;

    //set
    public float sAngleX;
    public float sAngleY;
    public float sAngleZ;
    public float positionX = 0f;
    public float positionY = 0f;

    public float forwardAngle = 0f;
    public float abductionAngle = 0f;

    public float pitchAngleAdjust = 0f; // y
    public float rollAngleAdjust = 0f; //x
    public float azimuthAngleAdjust = 0f; // z

    public float forwardAngleAdjust = 0f;
    public float abductionAngleAdjust = 0f;
    public float alphaValue = 1f;

    public float initPosition1X = 0f;
    public float initPosition1Y = 0f;
    public float initPosition1Z = 0f;
    public float initPosition2X = 0f;
    public float initPosition2Y = 0f;
    public float initPosition2Z = 0f;
    public float beforeAngleX = 0f;
    public float beforeAngleY = 0f;
    public float beforeAngleZ = 0f;

    //scale
    //外部控制
    public float scale = 1.0f;
    public float scale_object = 1.0f;

    public float translation_z;
    public float translation_y;
    public float Btranslation_y;
    public float bTranslation_z;
    public float cTranslation_z;

    public static float red;
    public static float green;
    public static float blue;
    public static float alpha;
    public static boolean displayAxes = true;
    public static boolean displayGrids = false;


    public BaseRenderer() {}

    /**
     * 简单重绘（适用于旋转等）
     */
    public abstract void requestRedraw();

    /**
     * 复杂重绘 （适用于更换文件）
     *
     * @param
     */
    public abstract void requestRedrawM();


    @Override
    public void onDrawFrame(GL10 gl) {}


    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {

    }

    public abstract void delete();

    /**
     * 固定缩放比例
     */
    public abstract void setsclae() ;

}
