package com.ssh.shuoshi.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ssh.shuoshi.R;
import com.ssh.shuoshi.inter.OnItemClickListener;

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.ViewHolder> {

    private Context mContext;
    private String[] mModuleName;
    private int[] mModuleIcon;
    private OnItemClickListener mOnItemClickListener;

    public HomeAdapter(Context context, String[] moduleName, int[] moduleIcon) {
        this.mContext = context;
        this.mModuleName = moduleName;
        this.mModuleIcon = moduleIcon;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_fragment1_tag, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.imageViewTag.setImageResource(mModuleIcon[position]);
        holder.textViewTag.setText(mModuleName[position]);
        holder.itemView.setOnClickListener(v -> {
            if (mOnItemClickListener != null) {
                mOnItemClickListener.onItemClick(v, position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mModuleName.length;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private final ImageView imageViewTag;
        private final TextView textViewTag;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageViewTag = itemView.findViewById(R.id.imageViewTag);
            textViewTag = itemView.findViewById(R.id.textViewTag);
        }
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
    }
}

