package com.ijoin.ihpas.widget;

import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.WindowManager;

/**
 * 为解决沉浸式封装的Dialog类
 */
public class ImmersiveDialogFragment extends DialogFragment {

    private CreateDialog mCreateDialog;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        Dialog dialog = null;
        if (mCreateDialog != null) {
            dialog = mCreateDialog.ToCreateDialog();

            // Temporarily set the dialogs window to not focusable to prevent the short
            // popup of the navigation bar.
            dialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE);
        }

        return dialog;

    }

    public void showImmersive(Activity activity, CreateDialog createDialog) {

        mCreateDialog = createDialog;

        // Show the dialog.
        show(activity.getFragmentManager(), null);

        // It is necessary to call executePendingTransactions() on the FragmentManager
        // before hiding the navigation bar, because otherwise getWindow() would raise a
        // NullPointerException since the window was not yet created.
        getFragmentManager().executePendingTransactions();

        // Hide the navigation bar. It is important to do this after show() was called.
        // If we would do this in onCreateDialog(), we would get a requestFeature()
        // error.
        getDialog().getWindow().getDecorView().setSystemUiVisibility(
                getActivity().getWindow().getDecorView().getSystemUiVisibility()
        );

        // Make the dialogs window focusable again.
        getDialog().getWindow().clearFlags(
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
        );

    }

}
