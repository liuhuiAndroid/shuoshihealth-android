package com.ssh.shuoshi.ui.prescription.westernmedicine.add;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ssh.shuoshi.R;
import com.ssh.shuoshi.bean.DrugsInfo;
import com.ssh.shuoshi.inter.OnItemClickListener;

import java.util.ArrayList;
import java.util.List;

public class NewDrugsAdapter extends RecyclerView.Adapter<NewDrugsAdapter.PatientViewHolder> {

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
                .inflate(R.layout.item_new_drugs, parent, false);
        return new NewDrugsAdapter.PatientViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PatientViewHolder holder, int position) {
        DrugsInfo info = list.get(position);
        holder.imageView.setImageResource(R.mipmap.ic_launcher);
        holder.textName.setText(info.getName());
        holder.textSpecification.setText(info.getSpecification());
        holder.textPrice.setText(info.getPrice());
        holder.mContainer.setOnClickListener(v -> {
            if (mOnItemClickListener != null) {
                mOnItemClickListener.onItemClick(v, position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class PatientViewHolder extends RecyclerView.ViewHolder {

        private RelativeLayout mContainer;
        private ImageView imageView;
        private TextView textName;
        private TextView textSpecification;
        private TextView textPrice;
        private TextView buttonAdd;

        public PatientViewHolder(@NonNull View itemView) {
            super(itemView);
            mContainer = itemView.findViewById(R.id.mContainer);
            imageView = itemView.findViewById(R.id.imageView);
            textName = itemView.findViewById(R.id.textName);
            textSpecification = itemView.findViewById(R.id.textSpecification);
            textPrice = itemView.findViewById(R.id.textPrice);
            buttonAdd = itemView.findViewById(R.id.buttonAdd);
        }
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
    }
}
