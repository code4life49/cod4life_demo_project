package com.study.xuan.gifshow1.widget.stlview.widget;

import android.content.Context;
import android.util.Log;

import com.study.xuan.gifshow1.widget.stlview.callback.OnReadListener;
import com.study.xuan.gifshow1.widget.stlview.operate.BInstallSTLReader;
import com.study.xuan.gifshow1.widget.stlview.operate.BSTLReader;
import com.study.xuan.gifshow1.widget.stlview.operate.BeforeBSTLReader;
import com.study.xuan.gifshow1.widget.stlview.operate.BeforeCSTLReader;
import com.study.xuan.gifshow1.widget.stlview.operate.BeforeSTLReader;
import com.study.xuan.gifshow1.widget.stlview.operate.CSTLReader;
import com.study.xuan.gifshow1.widget.stlview.operate.ISTLReader;
import com.study.xuan.gifshow1.widget.stlview.operate.InstallSTLReader;
import com.study.xuan.gifshow1.widget.stlview.operate.ReaderHandler;
import com.study.xuan.gifshow1.widget.stlview.operate.STLReader;
import com.study.xuan.gifshow1.widget.stlview.util.IOUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Author : xuan.
 * Date : 2017/12/14.
 * Description :3dView 构造器
 */

public class STLViewBuilder {
    private STLView stlView;
    private static final int TYPE_FILE = 0;
    private static final int TYPE_BYTE = 1;
    private static final int TYPE_STREAM = 2;
    private ReaderHandler handler;
    private ReaderHandler bHandler;
    private ReaderHandler cHandler;
    private OnReadListener listener;
    private OnReadListener bListener;
    private OnReadListener cListener;
    private File file;
    private byte[] bytes;
    private InputStream is;
    private InputStream bis;
    private InputStream cis;
    private ISTLReader reader;
    private ISTLReader bReader;
    private ISTLReader cReader;
    private boolean hasSource;
    private boolean bHasSource;
    private boolean cHasSource;
    private int type;
    private int bType;
    private int cType;
    private Object obj;

    public STLViewBuilder(STLView stlView) {
        this.stlView = stlView;
        this.listener = stlView.getReadListener();
        this.bListener = stlView.getBReadListener();
        this.cListener = stlView.getCReadListener();
    }

    public STLViewBuilder(OnReadListener listener, OnReadListener bListener, OnReadListener cListener) {
        this.listener = listener;
        this.bListener = bListener;
        if(cListener != null) this.cListener = cListener;
    }

    public static STLViewBuilder init(STLView stlView) {
        return new STLViewBuilder(stlView);
    }

    public static STLViewBuilder init(OnReadListener listener, OnReadListener bListener, OnReadListener cListener) {
        return new STLViewBuilder(listener, bListener,cListener);
    }

    public STLViewBuilder Reader(ISTLReader reader) {
        this.reader = reader;
        this.reader.setCallBack(this.listener);
        return this;
    }

    public STLViewBuilder Byte(byte[] bytes) {
        hasSource = true;
        type = TYPE_BYTE;
        this.bytes = bytes;
        return this;
    }

    public STLViewBuilder File(File file) {
        type = TYPE_FILE;
        hasSource = true;
        this.file = file;
        return this;
    }

    public STLViewBuilder assets(Context context, String fileName) {
        try {
            return InputStream(context.getAssets().open(fileName));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return this;
    }

    public STLViewBuilder bAssets(Context context, String fileName) {
        try {
            return bInputStream(context.getAssets().open(fileName));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return this;
    }

    public STLViewBuilder cAssets(Context context, String fileName) {
        if(fileName == null)return this;

        try {
            return cInputStream(context.getAssets().open(fileName));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return this;
    }

    public STLViewBuilder bInputStream(InputStream inputStream) {
        bType = TYPE_STREAM;
        bHasSource = true;
        this.bis = inputStream;
        return this;
    }

    public STLViewBuilder cInputStream(InputStream inputStream) {
        cType = TYPE_STREAM;
        cHasSource = true;
        this.cis = inputStream;
        return this;
    }

    public STLViewBuilder InputStream(InputStream inputStream) {
        type = TYPE_STREAM;
        hasSource = true;
        this.is = inputStream;
        return this;
    }

    public STLViewBuilder build(int typeReader) {
        if (hasSource) {
            if (reader == null) {
                switch (typeReader){
                    case 1:
                        reader =  new InstallSTLReader();
                        break;
                    case 2:
                        reader =  new STLReader();
                        break;
                    case 3:
                        reader =  new BeforeSTLReader();
                        break;
                }
                reader.setCallBack(this.listener);
            }
            handler = new ReaderHandler(reader, listener);
            try {
                switch (type) {
                    case TYPE_BYTE:
                        handler.read(bytes);
                        break;
                    case TYPE_FILE:
                        handler.read(IOUtils.toByteArray(new FileInputStream(file)));
                        break;
                    case TYPE_STREAM:
                        handler.read(IOUtils.toByteArray(is));
                        break;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (bHasSource) {
            if (bReader == null) {
                switch (typeReader){
                    case 1:
                        bReader =  new BInstallSTLReader();
                        break;
                    case 2:
                        bReader =  new BSTLReader();
                        break;
                    case 3:
                        bReader =  new BeforeBSTLReader();
                        break;
                }
                bReader.setCallBack(this.bListener);
            }
            bHandler = new ReaderHandler(bReader, bListener);

            try {
                switch (bType) {
                    case TYPE_BYTE:
                        bHandler.read(bytes);
                        break;
                    case TYPE_FILE:
                        bHandler.read(IOUtils.toByteArray(new FileInputStream(file)));
                        break;
                    case TYPE_STREAM:
                        bHandler.read(IOUtils.toByteArray(bis));
                        break;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if(cHasSource) {
            if (cReader == null) {
                switch (typeReader){
                    case 2:
                        cReader =  new CSTLReader();
                        break;
                    case 3:
                        cReader =  new BeforeCSTLReader();
                        break;
                }
                cReader.setCallBack(this.cListener);
            }
            cHandler = new ReaderHandler(cReader, cListener);

            try {
                switch (cType) {
                    case TYPE_BYTE:
                        cHandler.read(bytes);
                        break;
                    case TYPE_FILE:
                        cHandler.read(IOUtils.toByteArray(new FileInputStream(file)));
                        break;
                    case TYPE_STREAM:
                        cHandler.read(IOUtils.toByteArray(cis));
                        break;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return this;
    }
}
