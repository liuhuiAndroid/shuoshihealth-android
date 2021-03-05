package com.ssh.shuoshi.ui.prescription.westernmedicine.commonlyprescription;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ssh.shuoshi.R;
import com.ssh.shuoshi.bean.CommonlyPrescriptionInfo;
import com.ssh.shuoshi.inter.OnItemClickListener;

import java.util.ArrayList;
import java.util.List;

public class CommonlyPrescriptionAdapter extends RecyclerView.Adapter<CommonlyPrescriptionAdapter.PatientViewHolder> {

    private List<CommonlyPrescriptionInfo> list = new ArrayList<>();

    private OnItemClickListener mOnItemClickListener;

    public void setList(List<CommonlyPrescriptionInfo> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    public List<CommonlyPrescriptionInfo> getList() {
        return list;
    }

    @NonNull
    @Override
    public PatientViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_commonly_prescription, parent, false);
        return new CommonlyPrescriptionAdapter.PatientViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PatientViewHolder holder, int position) {
        CommonlyPrescriptionInfo info = list.get(position);
        holder.textViewResult.setText(info.getResult());
        holder.textViewRp.setText(info.getRp());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class PatientViewHolder extends RecyclerView.ViewHolder {

        private TextView textViewResult;
        private TextView textViewRp;

        public PatientViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewResult = itemView.findViewById(R.id.textViewResult);
            textViewRp = itemView.findViewById(R.id.textViewRp);
        }
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
    }
}
