package com.ijoin.ihpas;


import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.ijoin.ihpas.utils.ActivityCollector;
import com.ijoin.ihpas.utils.ToastUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.ButterKnife;
import io.reactivex.Observable;

/**
 * @author 刘成龙
 * @date 2020/4/21
 */
public class SplashActivity extends BaseActivity {
    List<String> mPermissionList = new ArrayList<>();
    BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    boolean enable = false;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ButterKnife.bind(this);
        pess();
    }

    private void pess() {
        if (bluetoothAdapter != null) {
            if (!(enable = bluetoothAdapter.isEnabled())) {
                enable = bluetoothAdapter.enable();
                if (!enable) {
                    exit();
                }
            }
        }

        if (enable && mPermissionList.size() == 0) {
            String path = Environment.getExternalStorageDirectory() + "/文件发送系统操作数据";
            File file = new File(path);

            if (!file.exists()) {
                file.mkdir();
            }
            next();
        }
    }

    @SuppressLint("CheckResult")
    private void next() {
        ToastUtils.showMessage("运行环境符合要求");
        Observable.timer(3, TimeUnit.SECONDS)
                .subscribe(l -> {
                    MainActivity.actionStart(mContext);
                    finish();
                });
    }

    @SuppressLint("CheckResult")
    private void exit() {
        ToastUtils.showMessage("运行环境不符合要求");
        Observable.timer(3, TimeUnit.SECONDS)
                .subscribe(o -> {
                    ActivityCollector.finshAll();
                });
    }

    @SuppressLint("CheckResult")
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1:
                boolean success = false;
                if (grantResults.length > 0) {
                    for (int i : grantResults) {
                        if (i == PackageManager.PERMISSION_GRANTED) {
                            success = true;
                        } else {
                            success = false;
                            break;
                        }
                    }
                    if (success && enable) {
                        ToastUtils.showMessage("运行环境符合要求");
                        String path = Environment.getExternalStorageDirectory() + "/文件发送系统操作数据";
                        File file = new File(path);
                        if (!file.exists()) {
                            file.mkdir();
                        }
                        next();
                    } else {
                        exit();
                    }
                }
                break;

        }
    }

}
