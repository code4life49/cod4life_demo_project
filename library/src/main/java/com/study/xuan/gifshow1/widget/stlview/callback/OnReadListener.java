package com.study.xuan.gifshow1.widget.stlview.callback;

import com.study.xuan.gifshow1.widget.stlview.model.STLModel;

public interface OnReadListener {
    void onstart();

    void onLoading(int cur, int total);

    void onFinished(STLModel model);

    void onFailure(Exception e);
}