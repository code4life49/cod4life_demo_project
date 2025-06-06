package com.study.xuan.gifshow1.widget.stlview.widget;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.PointF;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;

import com.study.xuan.gifshow1.widget.stlview.callback.OnReadCallBack;
import com.study.xuan.gifshow1.widget.stlview.callback.OnReadListener;
import com.study.xuan.gifshow1.widget.stlview.model.STLModel;
import com.study.xuan.gifshow1.widget.stlview.util.MyStlUtils;
import com.study.xuan.gifshow1.widget.stlview.util.STLUtils;

/**
 * Author : xuan.
 * Date : 2017/12/10.
 * Description : 自定义展示器
 */

public class MeasureAdjustSTLView extends GLSurfaceView {
    private BaseRenderer stlRenderer;
    private Context mContext;
    private OnReadCallBack onReadCallBack;
    private OnReadCallBack bOnReadCallBack;
    private OnReadCallBack cOnReadCallBack;
    //双指缩放
    //这里将偏移数值降低
    private final float TOUCH_SCALE_FACTOR = 180.0f / 1080 / 2;
    private float previousX;
    private float previousY;
    // zoom rate (larger > 1.0f > smaller)
    private float pinchScale = 1.0f;
    private PointF pinchStartPoint = new PointF();
    private float pinchStartZ = 0.0f;
    private float pinchStartDistance = 0.0f;
    private float pinchMoveX = 0.0f;
    private float pinchMoveY = 0.0f;

    // for touch event handling
    private static final int TOUCH_NONE = 0;
    private static final int TOUCH_DRAG = 1;
    private static final int TOUCH_ZOOM = 2;
    private int touchMode = TOUCH_NONE;
    //传感器
    private float timestamp;
    // 创建常量，把纳秒转换为秒。
    private static final float NS2S = 1.0f / 1000000000.0f;
    private SensorManager sensorManager;
    private Sensor gyroscopeSensor;
    private SensorEventListener sensorEventListener;
    //感应开关
    private boolean isSensor;
    private boolean isTouch;
    private boolean isRotate;
    private boolean isScale;


    public MeasureAdjustSTLView(Context context) {
        this(context, null);
    }

    public MeasureAdjustSTLView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        if (STLUtils.checkSupported(mContext)) {
            init();
        } else {
            Log.e("ERROR", "the phone can't support OpenGl ES2!");
        }
    }


    private void init() {
        SharedPreferences colorConfig = mContext.getSharedPreferences("colors", Activity.MODE_PRIVATE);
        STLRenderer.red = colorConfig.getFloat("red", 0.8352f);
        STLRenderer.green = colorConfig.getFloat("green", 0.7686f);
        STLRenderer.blue = colorConfig.getFloat("blue", 0.6313f);
        STLRenderer.alpha = colorConfig.getFloat("alpha", 0.5f);

        stlRenderer = new MeasureAdjustSTLRenderer();
        setRenderer(stlRenderer);
    }

    public void setOnReadCallBack(OnReadCallBack onReadCallBack) {
        this.onReadCallBack = onReadCallBack;
    }

    public void setbOnReadCallBack(OnReadCallBack bOnReadCallBack) {
        this.bOnReadCallBack = bOnReadCallBack;
    }

    public void setcOnReadCallBack(OnReadCallBack cOnReadCallBack) {
        this.cOnReadCallBack = cOnReadCallBack;
    }


    public OnReadListener getReadListener() {
        return readListener;
    }

    public OnReadListener getBReadListener() {
        return bReadListener;
    }

    public OnReadListener getCReadListener() {
        return cReadListener;
    }

    private final OnReadListener readListener = new OnReadListener() {
        @Override
        public void onstart() {
            if (onReadCallBack != null) {
                onReadCallBack.onStart();
            }
        }

        @Override
        public void onLoading(int cur, int total) {
            if (onReadCallBack != null) {
                onReadCallBack.onReading(cur, total);
            }
        }

        @Override
        public void onFinished(STLModel model) {
            if (isSensor) {
                initSensor();
            }
            if (onReadCallBack != null) {
                onReadCallBack.onFinish();
            }
            stlRenderer.requestRedrawM();
        }

        @Override
        public void onFailure(Exception e) {

        }
    };
    private final OnReadListener bReadListener = new OnReadListener() {
        @Override
        public void onstart() {
            if (bOnReadCallBack != null) {
                bOnReadCallBack.onStart();
            }
        }

        @Override
        public void onLoading(int cur, int total) {
            if (bOnReadCallBack != null) {
                bOnReadCallBack.onReading(cur, total);
            }
        }

        @Override
        public void onFinished(STLModel model) {
            if (isSensor) {
                initSensor();
            }
            if (bOnReadCallBack != null) {
                bOnReadCallBack.onFinish();
            }
            stlRenderer.requestRedrawM();
        }

        @Override
        public void onFailure(Exception e) {

        }
    };

    private final OnReadListener cReadListener = new OnReadListener() {
        @Override
        public void onstart() {
            if (cOnReadCallBack != null) {
                cOnReadCallBack.onStart();
            }
        }

        @Override
        public void onLoading(int cur, int total) {
            if (cOnReadCallBack != null) {
                cOnReadCallBack.onReading(cur, total);
            }
        }

        @Override
        public void onFinished(STLModel model) {
            if (isSensor) {
                initSensor();
            }
            if (cOnReadCallBack != null) {
                cOnReadCallBack.onFinish();
            }
            stlRenderer.requestRedrawM();
        }

        @Override
        public void onFailure(Exception e) {

        }
    };

    private void changeDistance(float scale) {
        stlRenderer.scale = scale;

    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (!isTouch) {
            return true;
        }
        //双指缩放
        if (isScale) {
            zoomScale(event);
        }
        //单指旋转
        if (isRotate) {
            rotateModel(event);
        }
        return true;
    }

    /**
     * 单指旋转model
     */
    private void rotateModel(MotionEvent event) {
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            // start drag
            case MotionEvent.ACTION_DOWN:
                registerSensor(false);
                if (touchMode == TOUCH_NONE && event.getPointerCount() == 1) {
                    touchMode = TOUCH_DRAG;
                    previousX = event.getX();
                    previousY = event.getY();
                }
                break;

            case MotionEvent.ACTION_MOVE:
                if (touchMode == TOUCH_DRAG) {
                    float x = event.getX();
                    float y = event.getY();

                    float dx = x - previousX;
                    float dy = y - previousY;
                    //一次只移动一个方向
                    previousX = x;
                    previousY = y;

                    if (isRotate) {
                        if (Math.abs(dx) > Math.abs(dy)) {
                            stlRenderer.angleY = (stlRenderer.angleY + dx * TOUCH_SCALE_FACTOR) %
                                    360.0f;

                        } else {
                            stlRenderer.angleX = (stlRenderer.angleX + dy * TOUCH_SCALE_FACTOR) %
                                    360.0f;
                        }
                    } else {
                        // change view point
                        stlRenderer.positionX += dx * TOUCH_SCALE_FACTOR / 5;
                        stlRenderer.positionY += dy * TOUCH_SCALE_FACTOR / 5;
                    }
                    stlRenderer.requestRedraw();
                    requestRender();
                }
                break;

            // end drag
            case MotionEvent.ACTION_UP:
                registerSensor(true);
                if (touchMode == TOUCH_DRAG) {
                    touchMode = TOUCH_NONE;
                    break;
                }
                stlRenderer.setsclae();
        }
    }


    public void rotate(float x, float y, float z) {
        stlRenderer.forwardAngleAdjust = x;
        stlRenderer.abductionAngleAdjust = y;
//        stlRenderer.sAngleZ = z;
        requestRedraw();
    }

    public void changeForwardAngle(float forwardAngle) {
        stlRenderer.forwardAngle = forwardAngle - 15;
        requestRedraw();
    }

    public void changeAbductionAngle(float abductionAngle) {
        stlRenderer.abductionAngle = abductionAngle - 45;
        requestRedraw();
    }

    public void changePitchAngleAdjust(float pitchAngleAdjust) {
        stlRenderer.pitchAngleAdjust = pitchAngleAdjust;
        requestRedraw();
    }

    public void changeRollAngleAdjust(float rollAngleAdjust) {
        stlRenderer.rollAngleAdjust = rollAngleAdjust;
        requestRedraw();
    }

    public void changeAzimuthAngleAdjust(float azimuthAngleAdjust) {
        stlRenderer.azimuthAngleAdjust = azimuthAngleAdjust;
        requestRedraw();
    }

    public void changeAlphaValue(float value) {
        stlRenderer.alphaValue = value;
        requestRedraw();
    }


    /**
     * 双指缩放大小
     */
    private void zoomScale(MotionEvent event) {
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            // starts pinch
            case MotionEvent.ACTION_POINTER_DOWN:
                registerSensor(false);
                if (event.getPointerCount() >= 2) {
                    pinchStartDistance = getPinchDistance(event);
                    //pinchStartZ = pinchStartDistance;
                    if (pinchStartDistance > 50f) {
                        getPinchCenterPoint(event, pinchStartPoint);
                        previousX = pinchStartPoint.x;
                        previousY = pinchStartPoint.y;
                        touchMode = TOUCH_ZOOM;
                    }
                }
                break;

            case MotionEvent.ACTION_MOVE:
                if (touchMode == TOUCH_ZOOM && pinchStartDistance > 0) {
                    // on pinch
                    PointF pt = new PointF();

                    getPinchCenterPoint(event, pt);
                    pinchMoveX = pt.x - previousX;
                    pinchMoveY = pt.y - previousY;
                    float dx = pinchMoveX;
                    float dy = pinchMoveY;
                    previousX = pt.x;
                    previousY = pt.y;

                    if (isRotate) {
                        stlRenderer.angleY += dx * TOUCH_SCALE_FACTOR;
                        stlRenderer.angleX += dy * TOUCH_SCALE_FACTOR;
                    } else {
                        // change view point
                        stlRenderer.positionX += dx * TOUCH_SCALE_FACTOR / 5;
                        stlRenderer.positionY += dy * TOUCH_SCALE_FACTOR / 5;
                    }

                    pinchScale = getPinchDistance(event) / pinchStartDistance;
                    changeDistance(pinchScale);
                    stlRenderer.requestRedraw();
                    invalidate();
                }
                break;

            // end pinch
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_POINTER_UP:
                registerSensor(true);
                pinchScale = 0;
                pinchStartZ = 0;
                if (touchMode == TOUCH_ZOOM) {
                    touchMode = TOUCH_NONE;

                    pinchMoveX = 0.0f;
                    pinchMoveY = 0.0f;
                    pinchScale = 1.0f;
                    pinchStartPoint.x = 0.0f;
                    pinchStartPoint.y = 0.0f;
                    invalidate();
                }
                break;
        }
    }

    /**
     * 传感器注册事件
     */
    private void registerSensor(boolean register) {
        if (sensorManager != null) {
            if (register) {
                sensorManager.registerListener(sensorEventListener, gyroscopeSensor, SensorManager
                        .SENSOR_DELAY_GAME);
            } else {
                sensorManager.unregisterListener(sensorEventListener);
            }
        }
    }

    /**
     * @param event
     * @return pinched distance
     */
    private float getPinchDistance(MotionEvent event) {
        float x = 0;
        float y = 0;
        try {
            x = event.getX(0) - event.getX(1);
            y = event.getY(0) - event.getY(1);
        } catch (IllegalArgumentException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return (float) Math.sqrt(x * x + y * y);
    }

    private void initSensor() {
        sensorManager = (SensorManager) mContext.getSystemService(Context.SENSOR_SERVICE);
        gyroscopeSensor = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        sensorEventListener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent sensorEvent) {
                if (sensorEvent.sensor.getType() == Sensor.TYPE_GYROSCOPE) {
                    if (timestamp != 0) {
                        final float dT = (sensorEvent.timestamp - timestamp) * NS2S;
                        stlRenderer.angleY += sensorEvent.values[0] * dT * 180.0f % 360.0f;
                        stlRenderer.angleX += sensorEvent.values[1] * dT * 180.0f % 360.0f;
                        stlRenderer.requestRedraw();
                        requestRender();
                    }
                    timestamp = sensorEvent.timestamp;
                }
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {

            }
        };
        sensorManager.registerListener(sensorEventListener, gyroscopeSensor, SensorManager
                .SENSOR_DELAY_GAME);
    }

    public void setSensor(boolean sensor) {
        isSensor = sensor;
    }

    public void setTouch(boolean touch) {
        isTouch = touch;
    }

    public void setRotate(boolean rotate) {
        isTouch = true;
        isRotate = rotate;
    }

    public void setScale(boolean scale) {
        isTouch = true;
        isScale = scale;
    }

    /**
     * @param event
     * @param pt    pinched point
     */
    private void getPinchCenterPoint(MotionEvent event, PointF pt) {
        pt.x = (event.getX(0) + event.getX(1)) * 0.5f;
        pt.y = (event.getY(0) + event.getY(1)) * 0.5f;
    }

    @Override
    protected Parcelable onSaveInstanceState() {
        Bundle bundle = new Bundle();
        bundle.putParcelable("super_state", super.onSaveInstanceState());
        bundle.putBoolean("isRotate", isRotate);
        bundle.putBoolean("isScale", isScale);
        bundle.putBoolean("isSensor", isSensor);
        bundle.putBoolean("isTouch", isTouch);
        return bundle;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        if (state instanceof Bundle) {
            Bundle bundle = (Bundle) state;
            setTouch(bundle.getBoolean("isTouch"));
            setRotate(bundle.getBoolean("isRotate"));
            setScale(bundle.getBoolean("isScale"));
            setSensor(bundle.getBoolean("isSensor"));
//            setNewSTLObject((STLModel) bundle.getParcelable("model"));
            super.onRestoreInstanceState(bundle.getParcelable("super_state"));
            return;
        }
        super.onRestoreInstanceState(state);
    }


    /**
     * 刷新界面
     */
    public void requestRedraw() {
        stlRenderer.requestRedraw();
    }

    public void delete() {
        if (stlRenderer != null) {
            stlRenderer.delete();
        }
    }

    public void setModel() {
        stlRenderer.requestRedrawM();
    }
}
