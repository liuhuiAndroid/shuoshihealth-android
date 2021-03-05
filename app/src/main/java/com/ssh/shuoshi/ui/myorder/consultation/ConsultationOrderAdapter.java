package com.ssh.shuoshi.ui.myorder.consultation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ssh.shuoshi.R;
import com.ssh.shuoshi.inter.OnItemClickListener;

import java.util.ArrayList;
import java.util.List;

public class ConsultationOrderAdapter extends RecyclerView.Adapter<ConsultationOrderAdapter.PatientViewHolder> {

    private List<String> list = new ArrayList<>();

    private OnItemClickListener mOnItemClickListener;

    public void setList(List<String> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    public List<String> getList() {
        return list;
    }

    @NonNull
    @Override
    public PatientViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_consultation_order, parent, false);
        return new ConsultationOrderAdapter.PatientViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PatientViewHolder holder, int position) {
        String info = list.get(position);
        holder.textName.setText("张三");
        holder.textSex.setText("女");
        holder.textAge.setText("66岁");
        holder.textTag.setText("视频问诊");
        holder.textPrice.setText("20.00");
        holder.textTime.setText("2020-12-10 18:03");

        holder.itemView.setOnClickListener(v -> {
            if(mOnItemClickListener!=null){
                mOnItemClickListener.onItemClick(v,position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class PatientViewHolder extends RecyclerView.ViewHolder {

        private TextView textName;
        private TextView textSex;
        private TextView textAge;
        private TextView textTag;
        private TextView textPrice;
        private TextView textTime;

        public PatientViewHolder(@NonNull View itemView) {
            super(itemView);
            textName = itemView.findViewById(R.id.textName);
            textSex = itemView.findViewById(R.id.textSex);
            textAge = itemView.findViewById(R.id.textAge);
            textTag = itemView.findViewById(R.id.textTag);
            textPrice = itemView.findViewById(R.id.textPrice);
            textTime = itemView.findViewById(R.id.textTime);
        }
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
    }
}
