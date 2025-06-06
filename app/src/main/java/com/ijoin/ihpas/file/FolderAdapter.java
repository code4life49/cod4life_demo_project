package com.ijoin.ihpas.file;

import android.annotation.SuppressLint;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ijoin.ihpas.R;

import java.io.File;
import java.util.List;

public class FolderAdapter extends RecyclerView.Adapter<FolderAdapter.FolderViewHolder> {

    private List<File> folderList;
    private OnFolderClickListener listener;
    private int selectedPosition = -1;

    public interface OnFolderClickListener {
        void onFolderClick(File folder);
    }

    public FolderAdapter(List<File> folderList, OnFolderClickListener listener) {
        this.folderList = folderList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public FolderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_folder, parent, false);
        return new FolderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FolderViewHolder holder, @SuppressLint("RecyclerView") int position) {
        File folder = folderList.get(position);
        holder.folderName.setText(folder.getName());

        // 设置选中状态
        holder.itemView.setSelected(position == selectedPosition);
        holder.itemView.setBackgroundResource(position == selectedPosition ?
                R.color.selected_item_background : android.R.color.transparent);

        holder.itemView.setOnClickListener(v -> {
            int oldPosition = selectedPosition;
            selectedPosition = position;
            notifyItemChanged(oldPosition);
            notifyItemChanged(selectedPosition);

            if (listener != null) {
                listener.onFolderClick(folder);
            }
        });
    }

    @Override
    public int getItemCount() {
        return folderList.size();
    }

    static class FolderViewHolder extends RecyclerView.ViewHolder {
        TextView folderName;

        public FolderViewHolder(@NonNull View itemView) {
            super(itemView);
            folderName = itemView.findViewById(R.id.folder_name);
        }
    }
}
