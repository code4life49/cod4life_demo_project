package com.ijoin.ihpas.utils;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;

import com.ijoin.ihpas.widget.CreateDialog;
import com.ijoin.ihpas.widget.ImmersiveDialogFragment;

import java.lang.ref.WeakReference;


/**
 * 关于全局的Dialog的整合
 */
public class DialogUtils {

    private WeakReference<Activity> reference;
    private Dialog dialog;


    public DialogUtils(Activity activity) {
        reference = new WeakReference<>(activity);
    }

    /**
     * 提示对话框
     *
     * @param title          标题
     * @param message        信息
     * @param okListener     确定的监听
     * @param cancelListener 取消的监听
     */
    public void showAlertDialog(
            final String title,
            final String message,
            final String cancelDesc,
            final DialogInterface.OnClickListener okListener,
            final DialogInterface.OnClickListener cancelListener) {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
        if (reference.get() != null) {

            new ImmersiveDialogFragment().showImmersive(reference.get(), () -> {
                        dialog = new AlertDialog.Builder(reference.get())
                                .setTitle(title)
                                .setMessage(message)
                                .setCancelable(false)
                                .setPositiveButton("是", okListener)
                                .setNegativeButton(cancelDesc, cancelListener)
                                .create();
                        return dialog;
                    }
            );
        }
    }

    /**
     * 进度提示框
     *
     * @param message 显示的消息
     */
    public void showProgressDialog(final String message) {

        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
        if (reference.get() != null) {
            new ImmersiveDialogFragment().showImmersive(reference.get(), () -> {
                        if (dialog == null) {
                            dialog = CustomProgressDialog.createLoadingDialog(reference.get(), message);
                        }
                        dialog.setCancelable(false);
                        dialog.setCanceledOnTouchOutside(false);
                        dialog.show();
                        return dialog;
                    }
            );
        }

    }

    /**
     * 关闭对话框
     */
    public void hideDialog() {
        if (dialog != null) {
            if (dialog != null) {
                dialog.dismiss();
                dialog = null;
            }
        }
    }

}
