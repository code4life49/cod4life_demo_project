package com.ijoin.ihpas.widget;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ProgressBar;

import com.ijoin.ihpas.R;
import com.ijoin.ihpas.utils.EmailSender;
import com.ijoin.ihpas.utils.EmailGridAdapter;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class EmailDialog extends Dialog {


    private EditText recipientEditText;

    private TextView selectedFilesText;
    private Button sendButton;
    private Button cancelButton;
    private ProgressBar progressBar;
    private ListView recentEmailListView; // 假设你有一个 ListView 用于展示最近的邮箱列表
    private GridView recentEmailGridView;  // 改为GridView

    private List<File> selectedFiles;
    private OnEmailSendListener listener;

    public interface OnEmailSendListener {
        void onEmailSent();
    }

    public EmailDialog(Context context, List<File> selectedFiles, OnEmailSendListener listener) {
        super(context);
        this.selectedFiles = selectedFiles;
        this.listener = listener;

        setContentView(R.layout.dialog_send_email);
        setTitle("发送邮件");

        initViews();
        setupSelectedFiles();
//        setupRecentEmailList(); // 在这里设置最近邮箱功能

        setupRecentEmailGrid();
    }

    private void initViews() {

        recipientEditText = findViewById(R.id.recipient_edit_text);
        selectedFilesText = findViewById(R.id.selected_files_text);
        sendButton = findViewById(R.id.send_button);
        cancelButton = findViewById(R.id.cancel_button);
        progressBar = findViewById(R.id.progress_bar);
//        recentEmailListView = findViewById(R.id.recent_email_list);
        recentEmailGridView = findViewById(R.id.recent_email_grid); // 修改ID

        sendButton.setOnClickListener(v -> sendEmail());

        cancelButton.setOnClickListener(v -> dismiss());
    }

    private void setupSelectedFiles() {
        StringBuilder fileNames = new StringBuilder("已选择文件 (" + selectedFiles.size() + "):\n");
        for (File file : selectedFiles) {
            fileNames.append("• ").append(file.getName()).append("\n");
        }
        selectedFilesText.setText(fileNames.toString());
    }




    private void sendEmail() {
        String senderEmail = "pmp@i-join.cc";
        String senderPassword = "Aiqiao@2015";
        String recipient = recipientEditText.getText().toString().trim();
        String subject = "来自IKPAS的导出文件-"+ System.currentTimeMillis();
        String message = "请查收附件中的导出文件。";

        // 验证输入
        if (senderEmail.isEmpty()) {
            Toast.makeText(getContext(), "请输入发件人邮箱", Toast.LENGTH_SHORT).show();
            return;
        }

        if (senderPassword.isEmpty()) {
            Toast.makeText(getContext(), "请输入发件人邮箱密码", Toast.LENGTH_SHORT).show();
            return;
        }

        if (recipient.isEmpty()) {
            Toast.makeText(getContext(), "请输入收件人邮箱", Toast.LENGTH_SHORT).show();
            return;
        }

        if (subject.isEmpty()) {
            Toast.makeText(getContext(), "请输入邮件主题", Toast.LENGTH_SHORT).show();
            return;
        }

        // 显示进度条，禁用按钮
        progressBar.setVisibility(View.VISIBLE);
        sendButton.setEnabled(false);
        sendButton.setText("发送中...");

        // 发送邮件
        EmailSender.sendEmail(senderEmail, senderPassword, recipient, subject, message,
                selectedFiles, new EmailSender.EmailSendListener() {
                    @Override
                    public void onEmailSendStart() {
                        // 已在UI线程中处理
                    }

                    @Override
                    public void onEmailSendSuccess() {
                        progressBar.setVisibility(View.GONE);
                        sendButton.setEnabled(true);
                        sendButton.setText("发送");

                        Toast.makeText(getContext(), "邮件发送成功！", Toast.LENGTH_SHORT).show();

                        // 保存最近使用的邮箱
                        saveRecentEmail(recipient);

                        if (listener != null) {
                            listener.onEmailSent();
                        }
                        dismiss();
                    }

                    @Override
                    public void onEmailSendFailed(String error) {
                        progressBar.setVisibility(View.GONE);
                        sendButton.setEnabled(true);
                        sendButton.setText("发送");

                        Toast.makeText(getContext(), "发送失败: " + error, Toast.LENGTH_LONG).show();
                    }
                });
    }

    // 1. 保存最近邮箱（发送成功时调用，最多保存5个）
    private void saveRecentEmail(String email) {
        SharedPreferences sp = getContext().getSharedPreferences("email_pref", Context.MODE_PRIVATE);
        Set<String> emailSet = sp.getStringSet("recent_emails", new LinkedHashSet<>());
        LinkedHashSet<String> emails = new LinkedHashSet<>(emailSet);

        emails.remove(email);         // 去重
        emails.add(email);            // 添加到最后
        while (emails.size() > 5) {   // 最多保留5个
            Iterator<String> it = emails.iterator();
            it.next();
            it.remove();
        }
        sp.edit().putStringSet("recent_emails", emails).apply();
    }

    // 2. 读取最近邮箱列表
    private List<String> getRecentEmails() {
        SharedPreferences sp = getContext().getSharedPreferences("email_pref", Context.MODE_PRIVATE);
        Set<String> emailSet = sp.getStringSet("recent_emails", new LinkedHashSet<>());
        List<String> list = new ArrayList<>(emailSet);
        Collections.reverse(list); // 最新的在前
        return list;
    }

    // 在EmailDialog中处理最近邮箱功能
    private void setupRecentEmailList() {
        recipientEditText.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                List<String> emails = getRecentEmails();
                if (!emails.isEmpty()) {
                    recentEmailListView.setVisibility(View.VISIBLE);
                    recentEmailListView.setAdapter(new ArrayAdapter<>(getContext(),
                            android.R.layout.simple_list_item_1, emails));
                }
            } else {
                recentEmailListView.setVisibility(View.GONE);
            }
        });

        recentEmailListView.setOnItemClickListener((parent, view, position, id) -> {
            String selectedEmail = (String) parent.getItemAtPosition(position);
            recipientEditText.setText(selectedEmail);
            recipientEditText.setSelection(selectedEmail.length());
            recentEmailListView.setVisibility(View.GONE);
        });

        recipientEditText.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!TextUtils.isEmpty(s)) {
                    recentEmailListView.setVisibility(View.GONE);
                }
            }
            @Override public void afterTextChanged(Editable s) {}
        });
    }

    // 修改为GridView的设置方法
    private void setupRecentEmailGrid() {
        recipientEditText.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                List<String> emails = getRecentEmails();
                if (!emails.isEmpty()) {
                    recentEmailGridView.setVisibility(View.VISIBLE);

                    // 使用自定义适配器
                    EmailGridAdapter adapter = new EmailGridAdapter(getContext(), emails);
                    recentEmailGridView.setAdapter(adapter);

                    // 设置列数为2
                    recentEmailGridView.setNumColumns(2);
                }
            } else {
                recentEmailGridView.setVisibility(View.GONE);
            }
        });

        recentEmailGridView.setOnItemClickListener((parent, view, position, id) -> {
            String selectedEmail = (String) parent.getItemAtPosition(position);
            recipientEditText.setText(selectedEmail);
            recipientEditText.setSelection(selectedEmail.length());
            recentEmailGridView.setVisibility(View.GONE);
        });

        recipientEditText.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!TextUtils.isEmpty(s)) {
                    recentEmailGridView.setVisibility(View.GONE);
                }
            }
            @Override public void afterTextChanged(Editable s) {}
        });
    }

}
