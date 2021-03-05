package com.ssh.shuoshi.ui.prescription.template.commonlyprescriptionchinese;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ssh.shuoshi.R;
import com.ssh.shuoshi.inter.OnItemClickListener;
import com.ssh.shuoshi.ui.dialog.DeleteTemplateDialog;

import java.util.ArrayList;
import java.util.List;

public class CommonlyPrescriptionChineseAdapter extends RecyclerView.Adapter<CommonlyPrescriptionChineseAdapter.PatientViewHolder> {

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
                .inflate(R.layout.item_commonly_prescription_chinese, parent, false);
        return new CommonlyPrescriptionChineseAdapter.PatientViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PatientViewHolder holder, int position) {
        String info = list.get(position);
        holder.textView01.setText("常用处方 1");
        holder.textView02.setText("黄芪 10g 黄芪 10g 黄芪 10g 黄芪 10g");
        holder.buttonDelete.setOnClickListener(v -> {
            DeleteTemplateDialog deleteTemplateDialog = new DeleteTemplateDialog(holder.buttonDelete.getContext());
            deleteTemplateDialog.setOnItemClickListener(new DeleteTemplateDialog.ItemClickListener() {

                @Override
                public void cancel() {
                    deleteTemplateDialog.dismiss();
                }

                @Override
                public void confirm() {
                    deleteTemplateDialog.dismiss();
                }
            });
            deleteTemplateDialog.show();
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class PatientViewHolder extends RecyclerView.ViewHolder {

        private TextView textView01;
        private TextView textView02;
        private Button buttonDelete;

        public PatientViewHolder(@NonNull View itemView) {
            super(itemView);
            textView01 = itemView.findViewById(R.id.textView01);
            textView02 = itemView.findViewById(R.id.textView02);
            buttonDelete = itemView.findViewById(R.id.buttonDelete);
        }
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
    }
}
