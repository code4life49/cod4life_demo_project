package com.ijoin.ihpas.file;

import android.content.Context;
import android.content.SharedPreferences;
import java.io.File;
import java.util.HashSet;
import java.util.Set;

public class FileSendStatusManager {
    private static final String PREF_NAME = "sent_files_status";
    private static final String KEY_SENT_FILES = "sent_files_set";

    private SharedPreferences prefs;
    private static FileSendStatusManager instance;

    private FileSendStatusManager(Context context) {
        prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    public static synchronized FileSendStatusManager getInstance(Context context) {
        if (instance == null) {
            instance = new FileSendStatusManager(context.getApplicationContext());
        }
        return instance;
    }

    /**
     * 标记文件为已发送
     */
    public void markFileAsSent(String filePath) {
        String fileId = generateFileId(filePath);
        Set<String> sentFiles = getSentFiles();
        sentFiles.add(fileId);
        saveSentFiles(sentFiles);
    }

    /**
     * 批量标记文件为已发送
     */
    public void markFilesAsSent(java.util.List<File> files) {
        Set<String> sentFiles = getSentFiles();
        for (File file : files) {
            String fileId = generateFileId(file.getAbsolutePath());
            sentFiles.add(fileId);
        }
        saveSentFiles(sentFiles);
    }

    /**
     * 检查文件是否已发送
     */
    public boolean isFileSent(String filePath) {
        String fileId = generateFileId(filePath);
        return getSentFiles().contains(fileId);
    }

    /**
     * 取消文件的已发送标记
     */
    public void unmarkFileAsSent(String filePath) {
        String fileId = generateFileId(filePath);
        Set<String> sentFiles = getSentFiles();
        sentFiles.remove(fileId);
        saveSentFiles(sentFiles);
    }

    /**
     * 获取已发送文件数量
     */
    public int getSentFilesCount() {
        return getSentFiles().size();
    }

    /**
     * 清除所有发送记录
     */
    public void clearAllSentRecords() {
        prefs.edit().clear().apply();
    }

    /**
     * 获取所有已发送文件的ID集合
     */
    private Set<String> getSentFiles() {
        Set<String> defaultSet = new HashSet<>();
        return new HashSet<>(prefs.getStringSet(KEY_SENT_FILES, defaultSet));
    }

    /**
     * 保存已发送文件集合
     */
    private void saveSentFiles(Set<String> sentFiles) {
        prefs.edit().putStringSet(KEY_SENT_FILES, sentFiles).apply();
    }

    /**
     * 生成文件唯一标识
     * 基于文件路径、大小和最后修改时间
     */
    private String generateFileId(String filePath) {
        try {
            File file = new File(filePath);
            String combined = filePath + "_" + file.length() + "_" + file.lastModified();
            return String.valueOf(combined.hashCode());
        } catch (Exception e) {
            return String.valueOf(filePath.hashCode());
        }
    }
}
