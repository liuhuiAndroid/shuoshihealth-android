package com.ssh.shuoshi.ui.prescription.chinesemedicine.options;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ssh.shuoshi.R;
import com.ssh.shuoshi.bean.ChineseMedicineType;
import com.ssh.shuoshi.inter.OnItemClickListener;

import java.util.ArrayList;
import java.util.List;

public class ChineseMedicineOptionsAdapter extends RecyclerView.Adapter<ChineseMedicineOptionsAdapter.ChineseMedicineOptionsViewHolder> {

    private List<ChineseMedicineType> list = new ArrayList<>();

    private OnItemClickListener mOnItemClickListener;

    public void setList(List<ChineseMedicineType> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    public List<ChineseMedicineType> getList() {
        return list;
    }

    @NonNull
    @Override
    public ChineseMedicineOptionsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_chinese_medicine_options, parent, false);
        return new ChineseMedicineOptionsAdapter.ChineseMedicineOptionsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChineseMedicineOptionsViewHolder holder, int position) {
        ChineseMedicineType info = list.get(position);
        if (position == 0) {
            holder.textViewName.setTextColor(Color.parseColor("#191919"));
        } else {
            holder.textViewName.setTextColor(Color.parseColor("#999999"));
        }
        holder.textViewName.setText(info.getName());
        holder.textViewName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnItemClickListener != null) {
                    mOnItemClickListener.onItemClick(v, position);
                }
            }
        });

        if (position == list.size() - 1) {
            holder.bottomLine.setVisibility(View.GONE);
        } else {
            holder.bottomLine.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ChineseMedicineOptionsViewHolder extends RecyclerView.ViewHolder {

        private TextView textViewName;
        private View bottomLine;

        public ChineseMedicineOptionsViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewName = itemView.findViewById(R.id.textViewName);
            bottomLine = itemView.findViewById(R.id.bottom_line);
        }
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
    }
}
