package com.ijoin.ihpas.file;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.ijoin.ihpas.R;

import java.io.File;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class FileGridAdapter extends RecyclerView.Adapter<FileGridAdapter.FileViewHolder> {

    private List<File> fileList;
    private OnFileSelectionListener listener;
    private Set<File> selectedFiles;
    private FileSendStatusManager statusManager; // 添加状态管理器

    public interface OnFileSelectionListener {
        void onFileSelectionChanged(File file, boolean isSelected);
    }

    public FileGridAdapter(List<File> fileList, OnFileSelectionListener listener) {
        this.fileList = fileList;
        this.listener = listener;
        this.selectedFiles = new HashSet<>();
    }

    // 添加设置状态管理器的方法
    public void setStatusManager(FileSendStatusManager statusManager) {
        this.statusManager = statusManager;
    }

    @NonNull
    @Override
    public FileViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_file_grid, parent, false);
        return new FileViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FileViewHolder holder, int position) {
        final File file = fileList.get(position);

        holder.fileName.setText(file.getName());

        // 设置文件图标
        setFileIcon(holder.fileIcon, file);

        // 设置发送状态标记
        if (statusManager != null && statusManager.isFileSent(file.getAbsolutePath())) {
            holder.sentIcon.setVisibility(View.VISIBLE);
            holder.sentLabel.setVisibility(View.VISIBLE);
            // 可以设置不同的背景色或者透明度来表示已发送
            holder.itemView.setAlpha(0.7f);
        } else {
            holder.sentIcon.setVisibility(View.GONE);
            holder.sentLabel.setVisibility(View.GONE);
            holder.itemView.setAlpha(1.0f);
        }

        // 设置复选框状态
        boolean isSelected = selectedFiles.contains(file);
        holder.fileCheckbox.setChecked(isSelected);

        // 设置背景选中状态
        holder.itemView.setSelected(isSelected);

        // 复选框点击事件
        holder.fileCheckbox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                selectedFiles.add(file);
            } else {
                selectedFiles.remove(file);
            }

            // 更新背景状态
            holder.itemView.setSelected(isChecked);

            if (listener != null) {
                listener.onFileSelectionChanged(file, isChecked);
            }
        });

        // 整个条目点击也能切换选中状态
        holder.itemView.setOnClickListener(v -> holder.fileCheckbox.setChecked(!holder.fileCheckbox.isChecked()));
    }

    private void setFileIcon(ImageView imageView, File file) {
        String fileName = file.getName().toLowerCase();

//        if (fileName.endsWith(".pdf")) {
//            imageView.setImageResource(R.drawable.ic_pdf);
//        } else if (fileName.endsWith(".doc") || fileName.endsWith(".docx")) {
//            imageView.setImageResource(R.drawable.ic_doc);
//        } else if (fileName.endsWith(".jpg") || fileName.endsWith(".jpeg") ||
//                fileName.endsWith(".png") || fileName.endsWith(".gif")) {
//            imageView.setImageResource(R.drawable.ic_image);
//        } else if (fileName.endsWith(".txt")) {
//            imageView.setImageResource(R.drawable.ic_text);
//        } else if (fileName.endsWith(".zip") || fileName.endsWith(".rar")) {
//            imageView.setImageResource(R.drawable.ic_archive);
//        } else if (fileName.endsWith(".mp4") || fileName.endsWith(".avi") ||
//                fileName.endsWith(".mov")) {
//            imageView.setImageResource(R.drawable.ic_video);
//        } else if (fileName.endsWith(".mp3") || fileName.endsWith(".wav")) {
//            imageView.setImageResource(R.drawable.ic_audio);
//        } else {
//            imageView.setImageResource(R.drawable.ic_file);
//        }
        imageView.setImageResource(R.drawable.ic_pdf);
    }

    @Override
    public int getItemCount() {
        return fileList.size();
    }

    public void clearSelection() {
        selectedFiles.clear();
        notifyDataSetChanged();
    }

    // 添加刷新发送状态的方法
    public void refreshSendStatus() {
        notifyDataSetChanged();
    }

    static class FileViewHolder extends RecyclerView.ViewHolder {
        CheckBox fileCheckbox;
        ImageView fileIcon;
        TextView fileName;
        ImageView sentIcon; // 新增：已发送图标
        TextView sentLabel;  // 新增：已发送标签


        public FileViewHolder(@NonNull View itemView) {
            super(itemView);
            fileCheckbox = itemView.findViewById(R.id.file_checkbox);
            fileIcon = itemView.findViewById(R.id.file_icon);
            fileName = itemView.findViewById(R.id.file_name);
            sentIcon = itemView.findViewById(R.id.sent_icon);
            sentLabel = itemView.findViewById(R.id.sent_label);
        }
    }
}
