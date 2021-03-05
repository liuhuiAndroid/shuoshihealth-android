package com.ssh.shuoshi.ui.prescription.chinesemedicine.addherbs;

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

import com.ssh.shuoshi.bean.*;

import butterknife.OnClick;

public class ChineseMedicineMedicalAddHerbsSearchAdapter extends
        RecyclerView.Adapter<ChineseMedicineMedicalAddHerbsSearchAdapter.ChineseMedicineMedicalAddHerbsSearchViewHolder> {

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
    public ChineseMedicineMedicalAddHerbsSearchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_chinese_medicine_medical_add_herbs, parent, false);
        return new ChineseMedicineMedicalAddHerbsSearchAdapter
                .ChineseMedicineMedicalAddHerbsSearchViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChineseMedicineMedicalAddHerbsSearchViewHolder holder, int position) {
        DrugBean.RowsBean info = list.get(position);
        holder.textViewName.setText(info.getPhamAliasName());
        holder.textViewName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnItemClickListener != null) {
                    mOnItemClickListener.onItemClick(v, position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ChineseMedicineMedicalAddHerbsSearchViewHolder extends RecyclerView.ViewHolder {

        private TextView textViewName;

        public ChineseMedicineMedicalAddHerbsSearchViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewName = itemView.findViewById(R.id.textViewName);
        }
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
    }
}
