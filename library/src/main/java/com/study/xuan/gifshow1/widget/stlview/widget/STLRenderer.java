package com.study.xuan.gifshow1.widget.stlview.widget;

import android.opengl.GLSurfaceView.Renderer;
import android.opengl.GLU;
import android.util.Log;

import com.study.xuan.gifshow1.widget.stlview.model.STLModel;
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


public class STLRenderer extends BaseRenderer {
    private static final String TAG = "STLReader";


    //当前展示
    private float scale_rember = 1.0f;
    private static int bufferCounter = FRAME_BUFFER_COUNT;



    public STLRenderer() {
        setTransLation_Z();
        setBTransLation_Z();
    }

    /**
     * 简单重绘（适用于旋转等）
     */
    @Override
    public void requestRedraw() {
        bufferCounter = FRAME_BUFFER_COUNT;
    }

    /**
     * 复杂重绘 （适用于更换文件）
     *
     * @param
     */
    @Override
    public void requestRedrawM() {
        setTransLation_Z();
        setBTransLation_Z();
        bufferCounter = FRAME_BUFFER_COUNT;
    }


    @Override
    public void onDrawFrame(GL10 gl) {

        if (bufferCounter < 1) {
            return;
        }
        bufferCounter--;
        gl.glLoadIdentity();
        gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);


        gl.glTranslatef(0, 0, translation_z * 1f);
        gl.glScalef(scale_rember, scale_rember, scale_rember);
        gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);

        drawLines(gl);

        gl.glMatrixMode(GL10.GL_MODELVIEW);
        if (displayGrids) {
//            drawGrids(gl);
        }
//        gl.glPushMatrix();
//        gl.glRotatef(angleX, 0, 1, 0);
//        gl.glRotatef(angleY, 1, 0, 0);
//        gl.glRotatef(angleZ, 0, 0, 1);
//        Log.d(TAG, "onDrawFrame: angleX=" + angleX + ", angleY=" + angleY);
//        if (displayAxes) {
//            drawLines(gl);
//        }
//        gl.glPopMatrix();

        gl.glMaterialfv(GL10.GL_FRONT, GL10.GL_AMBIENT, new float[]{0.75f, 0.75f, 0.75f, 1.0f}, 0);
        gl.glMaterialfv(GL10.GL_FRONT, GL10.GL_DIFFUSE, new float[]{0.75f, 0.75f, 0.75f, 1.0f}, 0);
        gl.glEnable(GL10.GL_COLOR_MATERIAL);
        gl.glColor4f(220 / 256f, 223 / 256f, 227 / 256f, 1.0f);

        if (MyStlUtils.getStlModel() != null) {
            gl.glPushMatrix();
            switch (MyStlUtils.getStlDataEnum()) {

                case LZ:

                    gl.glRotatef(angleX + 45, 1, 0, 0);
                    gl.glRotatef(+MyStlUtils.getStlDataEnum().getRx() + sAngleX, 1, 0f, (float) (1-Math.tan(sAngleY/180*Math.PI)));
                    Log.i(TAG, "sAngleY:"+sAngleY);
                    gl.glRotatef(MyStlUtils.getStlDataEnum().getRy() + sAngleY, 0, 1, 0);

                    break;
                case LC:
                case RC:
                    gl.glPushMatrix();
                    gl.glRotatef(angleX - 45, 1, 0, 0);
                    gl.glRotatef(sAngleX + MyStlUtils.getStlDataEnum().getRx(), 0, 1,(float) (1-Math.tan(sAngleY/180*Math.PI)));
                    Log.i(TAG, "sAngleY:"+sAngleY);
                    gl.glRotatef(sAngleY + MyStlUtils.getStlDataEnum().getRy(), 1, 0, 0);

                    break;
                case RZ:
                    gl.glPushMatrix();
                    gl.glRotatef(angleX + 45, 1, 0, 0);
                    gl.glRotatef(-45, 0, 1, 0);
                    gl.glRotatef(MyStlUtils.getStlDataEnum().getRx()+  sAngleX,  -1 , 0f,(float)(1-Math.tan(-sAngleY/180*Math.PI)));
                    Log.i(TAG, "sAngleY:"+sAngleY);
                    gl.glRotatef(sAngleY + MyStlUtils.getStlDataEnum().getRy(), 0, 1, 0);

                    break;
                default:
                    break;
            }
            gl.glRotatef(-9.5f, 1, 0, 0);
            gl.glRotatef(42f, 0, 1, 0);
            MyStlUtils.getStlModel().draw(gl);
            gl.glPopMatrix();
        }


        gl.glColor4f(red, green, blue, 1.0f);
        if (MyStlUtils.getbStlModel() != null) {
            gl.glPushMatrix();

            gl.glRotatef(angleX + MyStlUtils.getStlDataEnum().getRotateX(), 1, 0, 0);
            gl.glRotatef(MyStlUtils.getStlDataEnum().getRotateY(), 0, 1, 0);
            gl.glRotatef(MyStlUtils.getStlDataEnum().getRotateZ(), 0, 0, 1);

            MyStlUtils.getbStlModel().draw(gl);
            gl.glPopMatrix();
        }
        gl.glDisable(GL10.GL_COLOR_MATERIAL);


    }

    private FloatBuffer getFloatBufferFromArray(float[] vertexArray) {
        ByteBuffer vbb = ByteBuffer.allocateDirect(vertexArray.length * 4);
        vbb.order(ByteOrder.nativeOrder());
        FloatBuffer triangleBuffer = vbb.asFloatBuffer();
        triangleBuffer.put(vertexArray);
        triangleBuffer.position(0);
        return triangleBuffer;
    }

    private FloatBuffer getFloatBufferFromList(List<Float> vertexList) {
        ByteBuffer vbb = ByteBuffer.allocateDirect(vertexList.size() * 4);
        vbb.order(ByteOrder.nativeOrder());
        FloatBuffer triangleBuffer = vbb.asFloatBuffer();
        float[] array = new float[vertexList.size()];
        for (int i = 0; i < vertexList.size(); i++) {
            array[i] = vertexList.get(i);
        }
        triangleBuffer.put(array);
        triangleBuffer.position(0);
        return triangleBuffer;
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        if (MyStlUtils.getStlModel() == null) {
            return;
        }
        float aspectRatio = (float) width / height;

        gl.glViewport(0, 0, width, height);

        gl.glLoadIdentity();
        gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);

        GLU.gluPerspective(gl, 45f, aspectRatio, 1f, 5000f);// (stlObject.maxZ - stlObject.minZ) * 10f + 100f);

        gl.glMatrixMode(GL10.GL_MODELVIEW);
        GLU.gluLookAt(gl, 0, 0, 100f, 0, 0, 0, 0, 1f, 0);
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {

        if (MyStlUtils.getStlModel() == null) {
            return;
        }
//		gl.glClearColor(0f, 0f, 0f, 0.5f);


        //gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
        gl.glEnable(GL10.GL_BLEND);
//		 gl.glEnable(GL10.GL_TEXTURE_2D);
//		 gl.glBlendFunc(GL10.GL_ONE, GL10.GL_SRC_COLOR);
        // FIXME This line seems not to be needed?
        gl.glClearDepthf(1.0f);
        gl.glEnable(GL10.GL_DEPTH_TEST);
        gl.glDepthFunc(GL10.GL_LEQUAL);
        gl.glHint(3152, 4354);
        gl.glEnable(GL10.GL_NORMALIZE);
        gl.glShadeModel(GL10.GL_SMOOTH);

        gl.glMatrixMode(GL10.GL_PROJECTION);

        // Lighting
        gl.glEnable(GL10.GL_LIGHTING);
        gl.glLightModelfv(GL10.GL_LIGHT_MODEL_AMBIENT, getFloatBufferFromArray(new float[]{0.5f, 0.5f, 0.5f, 1.0f}));// 全局环境光
        gl.glLightfv(GL10.GL_LIGHT0, GL10.GL_AMBIENT_AND_DIFFUSE, new float[]{0.3f, 0.3f, 0.3f, 1.0f}, 0);
        gl.glLightfv(GL10.GL_LIGHT0, GL10.GL_POSITION, new float[]{0f, -500f, 1000f, 0.5f}, 0);
        gl.glEnable(GL10.GL_LIGHT0);

    }

    @Override
    public void delete() {

    }

    /**
     * 调整Z轴平移位置    （目的式为了模型展示大小适中）
     */
    private void setTransLation_Z() {
        //算x、y轴差值
        float distance_x = MyStlUtils.getStlModel().maxX - MyStlUtils.getStlModel().minX;
        float distance_y = MyStlUtils.getStlModel().maxY - MyStlUtils.getStlModel().minY;
        float distance_z = MyStlUtils.getStlModel().maxZ - MyStlUtils.getStlModel().minZ;
        translation_z = distance_x;
        if (translation_z < distance_y) {
            translation_z = distance_y;
        }
        if (translation_z < distance_z) {
            translation_z = distance_z;
        }


        translation_z *= -2;
        Log.i("AAAAAA", "Z:" + translation_z);

    }

    private void setBTransLation_Z() {
        //算x、y轴差值
        float distance_x = MyStlUtils.getbStlModel().maxX - MyStlUtils.getbStlModel().minX;
        float distance_y = MyStlUtils.getbStlModel().maxY - MyStlUtils.getbStlModel().minY;
        float distance_z = MyStlUtils.getbStlModel().maxZ - MyStlUtils.getbStlModel().minZ;
        bTranslation_z = distance_x;
        if (bTranslation_z < distance_y) {
            bTranslation_z = distance_y;
        }
        if (bTranslation_z < distance_z) {
            bTranslation_z = distance_z;
        }
        bTranslation_z *= -2;
    }

    /**
     * 固定缩放比例
     */
    @Override
    public void setsclae() {
        scale_rember = 1.0f;
        scale = 1.0f;
    }


    /**
     * 画网格
     *
     * @param gl
     */
    private void drawGrids(GL10 gl) {
        List<Float> lineList = new ArrayList<Float>();

        for (int x = -100; x <= 100; x += 5) {
            lineList.add((float) x);
            lineList.add(-100f);
            lineList.add(0f);
            lineList.add((float) x);
            lineList.add(100f);
            lineList.add(0f);
        }
        for (int y = -100; y <= 100; y += 5) {
            lineList.add(-100f);
            lineList.add((float) y);
            lineList.add(0f);
            lineList.add(100f);
            lineList.add((float) y);
            lineList.add(0f);
        }

        FloatBuffer lineBuffer = getFloatBufferFromList(lineList);
        gl.glVertexPointer(3, GL10.GL_FLOAT, 0, lineBuffer);

        gl.glLineWidth(1f);
        gl.glMaterialfv(GL10.GL_FRONT_AND_BACK, GL10.GL_AMBIENT, new float[]{0.5f, 0.5f, 0.5f, 1.0f}, 0);
        gl.glMaterialfv(GL10.GL_FRONT_AND_BACK, GL10.GL_DIFFUSE, new float[]{0.5f, 0.5f, 0.5f, 1.0f}, 0);
        gl.glDrawArrays(GL10.GL_LINES, 0, lineList.size() / 3);
    }

    /**
     * 画坐标
     *
     * @param gl
     */
    private void drawLines(GL10 gl) {
        gl.glLineWidth(2f);
        float[] vertexArray = {-100, 0, 0, 100, 0, 0, 0, -100, 0, 0, 100, 0, 0, 0, -100, 0, 0, 100};
        FloatBuffer lineBuffer = getFloatBufferFromArray(vertexArray);
        gl.glVertexPointer(3, GL10.GL_FLOAT, 0, lineBuffer);

        // X : red
        gl.glMaterialfv(GL10.GL_FRONT_AND_BACK, GL10.GL_AMBIENT, new float[]{1.0f, 0f, 0f, 0.75f}, 0);
        gl.glMaterialfv(GL10.GL_FRONT_AND_BACK, GL10.GL_DIFFUSE, new float[]{1.0f, 0f, 0f, 0.5f}, 0);
        gl.glDrawArrays(GL10.GL_LINES, 0, 2);

        // Y : blue
        gl.glMaterialfv(GL10.GL_FRONT_AND_BACK, GL10.GL_AMBIENT, new float[]{0f, 0f, 1.0f, 0.75f}, 0);
        gl.glMaterialfv(GL10.GL_FRONT_AND_BACK, GL10.GL_DIFFUSE, new float[]{0f, 0f, 1.0f, 0.5f}, 0);
        gl.glDrawArrays(GL10.GL_LINES, 2, 2);

        // Z : green
        gl.glMaterialfv(GL10.GL_FRONT_AND_BACK, GL10.GL_AMBIENT, new float[]{0f, 1.0f, 0f, 0.75f}, 0);
        gl.glMaterialfv(GL10.GL_FRONT_AND_BACK, GL10.GL_DIFFUSE, new float[]{0f, 1.0f, 0f, 0.5f}, 0);
        gl.glDrawArrays(GL10.GL_LINES, 4, 2);
    }
    /**
     * 画坐标
     *
     * @param gl
     */
    private void drawLines1(GL10 gl) {
        gl.glLineWidth(2f);
        float[] vertexArray = {-100, 0, 0, 100, 0, 0, 0, -100, 0, 0, 100, 0, 0, 0, -100, 0, 0, 100};
        FloatBuffer lineBuffer = getFloatBufferFromArray(vertexArray);
        gl.glVertexPointer(3, GL10.GL_FLOAT, 0, lineBuffer);

        // X : red
        gl.glMaterialfv(GL10.GL_FRONT_AND_BACK, GL10.GL_AMBIENT, new float[]{1.0f, 0f, 0f, 0.75f}, 0);
        gl.glMaterialfv(GL10.GL_FRONT_AND_BACK, GL10.GL_DIFFUSE, new float[]{1.0f, 0f, 0f, 0.5f}, 0);
        gl.glDrawArrays(GL10.GL_LINES, 0, 2);

        // Y : blue
        gl.glMaterialfv(GL10.GL_FRONT_AND_BACK, GL10.GL_AMBIENT, new float[]{0f, 0f, 1.0f, 0.75f}, 0);
        gl.glMaterialfv(GL10.GL_FRONT_AND_BACK, GL10.GL_DIFFUSE, new float[]{0f, 0f, 1.0f, 0.5f}, 0);
        gl.glDrawArrays(GL10.GL_LINES, 2, 2);

        // Z : green
        gl.glMaterialfv(GL10.GL_FRONT_AND_BACK, GL10.GL_AMBIENT, new float[]{0f, 1.0f, 0f, 0.75f}, 0);
        gl.glMaterialfv(GL10.GL_FRONT_AND_BACK, GL10.GL_DIFFUSE, new float[]{0f, 1.0f, 0f, 0.5f}, 0);
        gl.glDrawArrays(GL10.GL_LINES, 4, 2);
    }


}
