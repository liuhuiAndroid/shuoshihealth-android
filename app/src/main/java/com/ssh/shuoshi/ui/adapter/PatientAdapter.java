package com.ssh.shuoshi.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ssh.shuoshi.R;
import com.ssh.shuoshi.bean.PatientInfo;

import java.util.ArrayList;
import java.util.List;

public class PatientAdapter extends RecyclerView.Adapter<PatientAdapter.PatientViewHolder> {

    private List<PatientInfo> patientInfos = new ArrayList<>();

    public void setList(List<PatientInfo> patientInfos) {
        this.patientInfos = patientInfos;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public PatientViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_fragment1_patient, parent, false);
        return new PatientAdapter.PatientViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PatientViewHolder holder, int position) {
        PatientInfo patientInfo = patientInfos.get(position);
        holder.mImageAvatar.setImageResource(R.mipmap.ic_launcher);
        holder.textName.setText(patientInfo.getName());
        holder.textSex.setText(patientInfo.getSex());
        holder.textAge.setText(patientInfo.getAge() + "岁");
        holder.textType.setText("图文问诊");
        holder.textStatus.setText("待接诊");
        holder.textDetail.setText(patientInfo.getDesc());
        holder.textTime.setText(patientInfo.getTime());
    }

    @Override
    public int getItemCount() {
        return patientInfos.size();
    }

    public static class PatientViewHolder extends RecyclerView.ViewHolder {

        private ImageView mImageAvatar;
        private TextView textName;
        private TextView textSex;
        private TextView textAge;
        private TextView textType;
        private TextView textStatus;
        private TextView textDetail;
        private TextView textTime;

        public PatientViewHolder(@NonNull View itemView) {
            super(itemView);
            mImageAvatar = itemView.findViewById(R.id.mImageAvatar);
            textName = itemView.findViewById(R.id.textName);
            textSex = itemView.findViewById(R.id.textSex);
            textAge = itemView.findViewById(R.id.textAge);
            textType = itemView.findViewById(R.id.textType);
            textStatus = itemView.findViewById(R.id.textStatus);
            textDetail = itemView.findViewById(R.id.textDetail);
            textTime = itemView.findViewById(R.id.textTime);
        }
    }
}
