package com.ijoin.ihpas.utils;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.ijoin.ihpas.App;
import com.ijoin.ihpas.R;

public class ToastUtils {
    private static Toast toast;
    public static void showMessage(String message) {
        if (toast == null) {
            toast = Toast.makeText(App.getApplication(), message, Toast.LENGTH_SHORT);
        } else {
            toast.setText(message);
        }
        toast.show();
    }
}
