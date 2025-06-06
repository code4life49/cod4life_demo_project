package com.study.xuan.gifshow1.widget.stlview.util;

/**
 * @author 刘成龙
 * @date 2021/1/22 09:19
 * @desc Stl的各数据
 */
@SuppressWarnings("all")
public enum StlDataEnum {

    LZ("左腿平躺", 150f, -100f, -685f, -45f, 90f, 0f, 153.630875f, 173.752472f, -166.764496f, -15f, -45f, 0f),
    LC("左腿侧卧", 150f, -100f, -685f, -135f, 0f, 0f, 153.630875f, 173.752472f, -166.764496f, -15f, 45f, 0f),
    RZ("右腿平躺", 160f, 70f, -700f, 45f, -135f, -90f, 153.630875f, 173.752472f, -166.764496f, 15f, 45f, -90f),
    RC("右腿侧卧", 160f, 70f, -700f, 45f, 180f, 0f, 153.630875f, 173.752472f, -166.764496f, 15f, 45f, 0f);


    StlDataEnum(float centerX) {
        this.centerX = centerX;
    }

    //骨盆的中心数据
    private float centerX;
    private float centerY;
    private float centerZ;
    //骨盆的旋转数据
    private float rotateX;
    private float rotateY;
    private float rotateZ;
    //髋臼杯中心数据
    private float cx;
    private float cy;
    private float cz;
    //髋臼杯的旋转数据
    private float rx;
    private float ry;
    private float rz;


    private String desc;

    StlDataEnum(String desc, float centerX, float centerY, float centerZ, float rotateX, float rotateY, float rotateZ, float cx, float cy, float cz, float rx, float ry, float rz) {
        this.centerX = centerX;
        this.centerY = centerY;
        this.centerZ = centerZ;
        this.rotateX = rotateX;
        this.rotateY = rotateY;
        this.rotateZ = rotateZ;
        this.cx = cx;
        this.cy = cy;
        this.cz = cz;
        this.rx = rx;
        this.ry = ry;
        this.rz = rz;
        this.desc = desc;
    }

    public float getCenterX() {
        return centerX;
    }

    public float getCenterY() {
        return centerY;
    }

    public float getCenterZ() {
        return centerZ;
    }

    public float getRotateX() {
        return rotateX;
    }

    public float getRotateY() {
        return rotateY;
    }

    public float getRotateZ() {
        return rotateZ;
    }

    public float getCx() {
        return cx;
    }

    public float getCy() {
        return cy;
    }

    public float getCz() {
        return cz;
    }

    public float getRx() {
        return rx;
    }

    public float getRy() {
        return ry;
    }

    public float getRz() {
        return rz;
    }

}
