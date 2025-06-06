package com.ijoin.ihpas.utils;

import android.app.Dialog;
import android.content.Context;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ijoin.ihpas.R;


/**
 * 自定义进度对话框
 * Created by admin on 2017/5/3.
 */

public class CustomProgressDialog {

    public static Dialog createLoadingDialog(Context context, String msg) {

        // 首先得到整个View
        View view = LayoutInflater.from(context).inflate(
                R.layout.dialog_progress_custom, null);
        // 页面中显示文本
        TextView tipText = (TextView) view.findViewById(R.id.tv_reminder);
        // 显示文本
        tipText.setText(msg);
        // 创建自定义样式的Dialog
        Dialog loadingDialog = new Dialog(context, R.style.custom_progress_dialog);
        // 设置返回键无效
        loadingDialog.setCancelable(false);
        loadingDialog.setContentView(view, new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.MATCH_PARENT));
        return loadingDialog;
    }
}
