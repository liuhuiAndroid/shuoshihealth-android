package com.ssh.shuoshi.ui.prescription.chinesemedicine.edit;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ssh.shuoshi.R;
import com.ssh.shuoshi.bean.DrugBean;
import com.ssh.shuoshi.inter.OnItemClickListener;

import java.util.ArrayList;
import java.util.List;

public class EditChineseMedicinePrescriptionHerbsAdapter extends
        RecyclerView.Adapter<EditChineseMedicinePrescriptionHerbsAdapter.ChineseMedicineMedicalEditHerbsViewHolder> {

    private List<DrugBean.RowsBean> list = new ArrayList<>();

    private OnItemClickListener mOnItemClickListener;

    public void setList(List<DrugBean.RowsBean> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    public List<DrugBean.RowsBean> getList() {
        return list;
    }

    @NonNull
    @Override
    public ChineseMedicineMedicalEditHerbsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_chinese_medicine_medical_edit_add_herbs, parent, false);
        return new EditChineseMedicinePrescriptionHerbsAdapter
                .ChineseMedicineMedicalEditHerbsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChineseMedicineMedicalEditHerbsViewHolder holder, int position) {
        DrugBean.RowsBean info = list.get(position);
        holder.textViewName.setText(info.getPhamAliasName());
        holder.textViewWeight.setText(info.getNumAddDrug() + "g");
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ChineseMedicineMedicalEditHerbsViewHolder extends RecyclerView.ViewHolder {

        private TextView textViewName;
        private TextView textViewWeight;

        public ChineseMedicineMedicalEditHerbsViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewName = itemView.findViewById(R.id.textViewName);
            textViewWeight = itemView.findViewById(R.id.textViewWeight);
        }
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
    }

}
