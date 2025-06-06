package com.ijoin.ihpas;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.ijoin.ihpas.file.FileGridAdapter;
import com.ijoin.ihpas.file.FileSendStatusManager;
import com.ijoin.ihpas.file.FolderAdapter;
import com.ijoin.ihpas.utils.DialogUtils;
import com.ijoin.ihpas.utils.Verify;
import com.ijoin.ihpas.widget.EmailDialog;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import butterknife.ButterKnife;


public class MainActivity extends BaseActivity implements
        FolderAdapter.OnFolderClickListener, FileGridAdapter.OnFileSelectionListener {

    private static final int PERMISSION_REQUEST_CODE = 100;

    private RecyclerView folderRecyclerView;
    private RecyclerView fileRecyclerView;
    private TextView currentFolderText;
    private Button sendEmailButton;

    private FolderAdapter folderAdapter;
    private FileGridAdapter fileAdapter;

    private List<File> folderList;
    private List<File> fileList;
    private List<File> selectedFiles;

    // 添加成员变量
    private EditText searchEditText;
    private EditText inputSearch;
    private List<File> allFileList; // 保存所有文件的原始列表
    private List<File> allFolderList; // 保存所有文件夹的原始列表


    private File rootDirectory;


    DialogUtils dialogUtils;
    Verify verify;

    // 添加状态管理器
    private FileSendStatusManager statusManager;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        dialogUtils = new DialogUtils(this);
        verify = Verify.getInstance();

        folderRecyclerView = findViewById(R.id.folder_recycler_view);
        fileRecyclerView = findViewById(R.id.file_recycler_view);
        currentFolderText = findViewById(R.id.current_folder_text);
        sendEmailButton = findViewById(R.id.send_email_button);

        folderList = new ArrayList<>();
        fileList = new ArrayList<>();
        selectedFiles = new ArrayList<>();

        // 在现有代码中添加
        searchEditText = findViewById(R.id.search_edit_text);
        allFileList = new ArrayList<>();
        inputSearch = findViewById(R.id.input_search);
        allFolderList = new ArrayList<>();

        // 设置搜索功能
        setupSearchFunction();

        // 设置RecyclerView
        folderRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // 设置文件RecyclerView (网格布局，一行两个)
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        fileRecyclerView.setLayoutManager(gridLayoutManager);


        // 初始化状态管理器
        statusManager = FileSendStatusManager.getInstance(this);


        folderAdapter = new FolderAdapter(folderList, this);
        fileAdapter = new FileGridAdapter(fileList, this);

        // 设置状态管理器到适配器
        fileAdapter.setStatusManager(statusManager);

        folderRecyclerView.setAdapter(folderAdapter);
        fileRecyclerView.setAdapter(fileAdapter);

        sendEmailButton.setOnClickListener(v -> showEmailDialog());

        rootDirectory = new File(Environment.getExternalStorageDirectory() + "/爱乔医疗交互软件（IKPAS）/定位系统");

        // 如果目录不存在则创建
        if (!rootDirectory.exists()) {
            rootDirectory.mkdirs();
        }

        checkPermissions();
    }

    private void setupSearchFunction() {
        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterFiles(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        inputSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterFolders(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

    }

    @SuppressLint("NotifyDataSetChanged")
    private void filterFiles(String query) {
        fileList.clear();

        if (query.trim().isEmpty()) {
            fileList.addAll(allFileList);
        } else {
            String lowerQuery = query.toLowerCase().trim();
            for (File file : allFileList) {
                if (file.getName().toLowerCase().contains(lowerQuery)) {
                    fileList.add(file);
                }
            }
        }

        fileAdapter.notifyDataSetChanged();
    }

    private void filterFolders(String query) {
        folderList.clear(); // 清空文件夹列表

        if (query.trim().isEmpty()) {
            folderList.addAll(allFolderList); // 显示所有文件夹
        } else {
            String lowerQuery = query.toLowerCase().trim();
            for (File folder : allFolderList) {
                if (folder.getName().toLowerCase().contains(lowerQuery)) {
                    folderList.add(folder);
                }
            }
        }

        folderAdapter.notifyDataSetChanged(); // 通知文件夹适配器更新
    }



    @Override
    protected void onStart() {
        super.onStart();

    }

    public static void actionStart(Context context) {
        Intent intent = new Intent(context, MainActivity.class);
        context.startActivity(intent);
    }


    private void checkPermissions() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    PERMISSION_REQUEST_CODE);
        } else {
            loadFolders();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                loadFolders();
            } else {
                Toast.makeText(this, "需要存储权限才能使用此功能", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void loadFolders() {
        allFolderList.clear(); // 清空原始文件夹列表

        if (rootDirectory.exists() && rootDirectory.isDirectory()) {
            File[] files = rootDirectory.listFiles();
            if (files != null) {
                for (File file : files) {
                    if (file.isDirectory()) {
                        allFolderList.add(file); // 添加到原始列表
                    }
                }
            }
        }

        // ===== 加入这段排序代码 =====
        allFolderList.sort((f1, f2) -> Long.compare(f2.lastModified(), f1.lastModified()));
        // ==========================

        // 清空搜索框并显示所有文件夹
        inputSearch.setText("");
        filterFolders(""); // 显示所有文件夹
    }

    // 实现 FolderAdapter.OnFolderClickListener 接口
    @Override
    public void onFolderClick(File folder) {
        currentFolderText.setText("当前文件夹: " + folder.getName());
        loadFiles(folder);
        selectedFiles.clear();
        fileAdapter.clearSelection(); // 清除文件选择状态
        updateSendButton();
    }

    private void loadFiles(File folder) {
        allFileList.clear(); // 修改这里，先保存到原始列表

        if (folder.exists() && folder.isDirectory()) {
            File[] files = folder.listFiles();
            if (files != null) {
                for (File file : files) {
                    if (file.isFile()) {
                        allFileList.add(file);
                    }
                }
            }
        }

        // ===== 加入这段排序代码 =====
        allFileList.sort((f1, f2) -> Long.compare(f2.lastModified(), f1.lastModified()));
        // ==========================

        // 清空搜索框并显示所有文件
        searchEditText.setText("");
        filterFiles("");
    }

    // 实现 FileAdapter.OnFileSelectionListener 接口
    @Override
    public void onFileSelectionChanged(File file, boolean isSelected) {
        if (isSelected) {
            if (!selectedFiles.contains(file)) {
                selectedFiles.add(file);
            }
        } else {
            selectedFiles.remove(file);
        }
        updateSendButton();
    }

    private void updateSendButton() {
        sendEmailButton.setEnabled(!selectedFiles.isEmpty());
        if (selectedFiles.isEmpty()) {
            sendEmailButton.setText("发送邮件");
        } else {
            sendEmailButton.setText("发送邮件 (" + selectedFiles.size() + ")");
        }
    }

    private void showEmailDialog() {
        if (selectedFiles.isEmpty()) {
            Toast.makeText(this, "请选择要发送的文件", Toast.LENGTH_SHORT).show();
            return;
        }

        EmailDialog dialog = new EmailDialog(this, selectedFiles, () -> {
            // 标记文件为已发送
            statusManager.markFilesAsSent(selectedFiles);

            // 邮件发送成功后的处理
            selectedFiles.clear();
            fileAdapter.clearSelection();
            updateSendButton();

            Toast.makeText(MainActivity.this, "邮件发送完成", Toast.LENGTH_SHORT).show();
        });
        dialog.show();
    }


}
