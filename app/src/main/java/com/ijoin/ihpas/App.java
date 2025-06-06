package com.ijoin.ihpas;

import android.app.Application;

import com.clj.fastble.BleManager;
import com.kongzue.dialog.v2.DialogSettings;
import com.study.xuan.gifshow1.widget.stlview.util.MeasureAdjustStlUtils;
import com.study.xuan.gifshow1.widget.stlview.util.MeasureBeforeStlUtils;
import com.study.xuan.gifshow1.widget.stlview.util.MyStlUtils;

import java.util.concurrent.Executors;


/**
 * @author chenglongliu
 */
@SuppressWarnings("all")
public class App extends Application {
    private static Application app;

    @Override
    public void onCreate() {
        super.onCreate();
        app = this;
        MyStlUtils.init(App.getApplication());
        MeasureAdjustStlUtils.init(App.getApplication());
        MeasureBeforeStlUtils.init(App.getApplication());
        MyStlUtils.setStlFile("kuangjiubei.STL", "gupen.stl");
//        CrashHandler.getInstance().init(getApplicationContext());
        Executors.newSingleThreadExecutor().execute(() -> {
            DialogSettings.style = DialogSettings.STYLE_IOS;
            DialogSettings.tip_theme = DialogSettings.THEME_LIGHT;         //设置提示框主题为亮色主题
            DialogSettings.dialog_theme = DialogSettings.THEME_LIGHT;       //设置对话框主题为暗色主题
            DialogSettings.use_blur = false;
            BleManager.getInstance().init(this);
            BleManager.getInstance()
                    .enableLog(true)
                    .setReConnectCount(1, 5000)
                    .setSplitWriteNum(20)
                    .setConnectOverTime(10000)
                    .setOperateTimeout(5000);
        });
    }

    public static Application getApplication() {
        return app;
    }
}
