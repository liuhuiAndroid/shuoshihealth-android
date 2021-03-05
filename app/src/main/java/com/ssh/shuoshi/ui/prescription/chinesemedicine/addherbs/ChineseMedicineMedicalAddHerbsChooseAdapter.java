package com.ssh.shuoshi.ui.prescription.chinesemedicine.addherbs;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ssh.shuoshi.R;
import com.ssh.shuoshi.bean.DrugBean;
import com.ssh.shuoshi.inter.MyTextWatcher;
import com.ssh.shuoshi.inter.OnItemClickListener;
import com.ssh.shuoshi.util.EditTextInput;

import java.util.ArrayList;
import java.util.List;

import static com.ssh.shuoshi.library.util.ToastUtil.mContext;

public class ChineseMedicineMedicalAddHerbsChooseAdapter extends
        RecyclerView.Adapter<ChineseMedicineMedicalAddHerbsChooseAdapter.ChineseMedicineMedicalAddHerbsChooseViewHolder> {

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
    public ChineseMedicineMedicalAddHerbsChooseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_chinese_medicine_medical_modify_herbs, parent, false);
        return new ChineseMedicineMedicalAddHerbsChooseAdapter
                .ChineseMedicineMedicalAddHerbsChooseViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChineseMedicineMedicalAddHerbsChooseViewHolder holder, int position) {
        DrugBean.RowsBean info = list.get(position);
        holder.textViewName.setText(info.getPhamAliasName());


        holder.imgDelete.setOnClickListener(v -> {
            list.remove(position);
            notifyDataSetChanged();
        });

        if (holder.editTextWeight.getTag() instanceof TextWatcher) {
            holder.editTextWeight.removeTextChangedListener((TextWatcher) holder.editTextWeight.getTag());
        }
        if (info.getNumAddDrug() != null && info.getNumAddDrug() != 0) {
            holder.editTextWeight.setText(info.getNumAddDrug() + "");
        } else {
            holder.editTextWeight.setText("");
        }

        EditTextInput.InputWatch(holder.editTextWeight, mContext, 999, 1);

        TextWatcher watcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                String trim = holder.editTextWeight.getText().toString().trim();
                if (!trim.isEmpty()) {
                    int parseInt = Integer.parseInt(trim);
                    info.setNumAddDrug(parseInt);
                }
            }
        };

        holder.editTextWeight.addTextChangedListener(watcher);
        holder.editTextWeight.setTag(watcher);

        if (position == list.size() - 1) {
            holder.editTextWeight.requestFocus();
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ChineseMedicineMedicalAddHerbsChooseViewHolder extends RecyclerView.ViewHolder {

        private TextView textViewName;
        private EditText editTextWeight;
        private ImageView imgDelete;

        public ChineseMedicineMedicalAddHerbsChooseViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewName = itemView.findViewById(R.id.textViewName);
            editTextWeight = itemView.findViewById(R.id.editTextWeight);
            imgDelete = itemView.findViewById(R.id.img_delete);
        }
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
    }

}
