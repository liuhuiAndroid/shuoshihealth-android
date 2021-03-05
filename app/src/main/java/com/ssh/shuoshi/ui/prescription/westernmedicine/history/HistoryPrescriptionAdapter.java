package com.ssh.shuoshi.ui.prescription.westernmedicine.history;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ssh.shuoshi.R;
import com.ssh.shuoshi.bean.DrugsInfo;
import com.ssh.shuoshi.inter.OnItemClickListener;

import java.util.ArrayList;
import java.util.List;

public class HistoryPrescriptionAdapter extends RecyclerView.Adapter<HistoryPrescriptionAdapter.PatientViewHolder> {

    private List<DrugsInfo> list = new ArrayList<>();

    private OnItemClickListener mOnItemClickListener;

    public void setList(List<DrugsInfo> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    public List<DrugsInfo> getList() {
        return list;
    }

    @NonNull
    @Override
    public PatientViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_history_drugs, parent, false);
        return new HistoryPrescriptionAdapter.PatientViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PatientViewHolder holder, int position) {
        DrugsInfo info = list.get(position);
        holder.textName.setText(info.getName());
        holder.textSize.setText(info.getPrice());
        holder.textSpecification.setText(info.getSpecification());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class PatientViewHolder extends RecyclerView.ViewHolder {

        private TextView textName;
        private TextView textSize;
        private TextView textSpecification;

        public PatientViewHolder(@NonNull View itemView) {
            super(itemView);
            textName = itemView.findViewById(R.id.textName);
            textSize = itemView.findViewById(R.id.textSize);
            textSpecification = itemView.findViewById(R.id.textSpecification);
        }
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
    }
}
