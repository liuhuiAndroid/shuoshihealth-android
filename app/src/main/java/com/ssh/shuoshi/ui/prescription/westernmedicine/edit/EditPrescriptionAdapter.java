package com.ssh.shuoshi.ui.prescription.westernmedicine.edit;

import android.content.Context;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ssh.shuoshi.R;
import com.ssh.shuoshi.bean.DrugBean;
import com.ssh.shuoshi.util.EditTextWatch;

import java.util.List;

/**
 * 编辑处方药品Adapter
 * created by hwt on 2020/12/19
 */
public class EditPrescriptionAdapter extends RecyclerView.Adapter<EditPrescriptionAdapter.ViewHolder> {

    private Context mContext;
    private List<DrugBean.RowsBean> mData;
    private ItemListener mItemListener;

    public List<DrugBean.RowsBean> getmData() {
        return mData;
    }

    public void setmData(List<DrugBean.RowsBean> data) {
        if (mData != null) {
            mData.clear();
        }
        this.mData = data;
        notifyDataSetChanged();
    }

    public EditPrescriptionAdapter(Context context, List<DrugBean.RowsBean> list) {
        mContext = context;
        mData = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.edit_prescription_item, null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        DrugBean.RowsBean bean = mData.get(position);

        holder.textViewDrugName.setText(bean.getPhamAliasName());
        holder.textViewSpecifications.setText(bean.getPhamSpec());
        holder.textViewNumber.setText(bean.getNum() + "");

        if (position == mData.size() - 1) {
            holder.viewBottom.setVisibility(View.GONE);
        } else {
            holder.viewBottom.setVisibility(View.VISIBLE);
        }

        if (!TextUtils.isEmpty(bean.getDosageUnits())) {
            holder.textViewSingleQuantity.setText(bean.getDosageUnits() + "/次");
        } else {
            holder.textViewSingleQuantity.setText("");
        }
        holder.textViewUsage.setText(bean.getAdministration());

        holder.textViewFrequency.setText(bean.getFrequency());

//        if (bean.isShowFrequency()) {
//            holder.editFrequency.setVisibility(View.VISIBLE);
//        } else {
//            holder.editFrequency.setVisibility(View.GONE);
//        }
//
//        if (holder.editFrequency.getTag() instanceof TextWatcher) {
//            holder.editFrequency.removeTextChangedListener((TextWatcher) holder.editFrequency.getTag());
//        }
//
//        TextWatcher watcher2 = new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//                if (TextUtils.isEmpty(s.toString())) {
//                    bean.setOtherFrequency("");
//                } else {
//                    bean.setOtherFrequency(holder.editFrequency.getText().toString());
//                }
//            }
//        };
//
//        holder.editFrequency.addTextChangedListener(watcher2);
//        holder.editFrequency.setTag(watcher2);


        if (holder.editSingleNum.getTag() instanceof TextWatcher) {
            holder.editSingleNum.removeTextChangedListener((TextWatcher) holder.editSingleNum.getTag());
        }
        String dosage = bean.getDosage();

        if (dosage != null) {
            if (dosage.length() > 2 && dosage.substring(dosage.length() - 2).contains(".0")) {
                String substring = dosage.substring(0, dosage.length() - 2);
                holder.editSingleNum.setText(substring);
            } else {
                holder.editSingleNum.setText(dosage);
            }
        } else {
            holder.editSingleNum.setText("");
        }


//        EditTextInput.InputWatch(holder.editSingleNum, mContext, 999, 1);
        holder.editSingleNum.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        holder.editSingleNum.addTextChangedListener(new EditTextWatch(holder.editSingleNum, 999, 0));

        TextWatcher watcher1 = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (TextUtils.isEmpty(s.toString())) {
                    bean.setDosage("0");
                } else {
                    bean.setDosage(holder.editSingleNum.getText().toString());
                }
            }
        };

        holder.editSingleNum.addTextChangedListener(watcher1);
        holder.editSingleNum.setTag(watcher1);

        holder.rlDel.setOnClickListener(v -> {
            if (mItemListener != null) {
                mItemListener.minusNumber(position, 1);
            }
        });

        holder.rlAdd.setOnClickListener(v -> {
            if (mItemListener != null) {
                mItemListener.minusNumber(position, 2);
            }
        });

        holder.relativeLayoutFrequency.setOnClickListener(v -> {
            if (mItemListener != null) {
                mItemListener.dictNum(position);
            }
        });

        holder.relativeLayoutSingleQuantity.setOnClickListener(v -> {
            if (mItemListener != null) {
                mItemListener.dosageUnits(position);
            }
        });

        holder.relativeLayoutUsage.setOnClickListener(v -> {
            if (mItemListener != null) {
                mItemListener.usage(position);
            }
        });

        holder.buttonDelete.setOnClickListener(v -> {
            if (mItemListener != null) {
                mItemListener.delete(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mData == null ? 0 : mData.size();
    }

    public void addData(DrugBean.RowsBean rowsBean) {
        mData.add(rowsBean);
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView textViewDrugName;
        private TextView textViewSpecifications;
        private RelativeLayout rlDel;
        private RelativeLayout rlAdd;
        private TextView textViewNumber;
        private TextView textViewFrequency;
        private TextView textViewSingleQuantity;
        private TextView textViewUsage;
        private Button buttonDelete;
        private RelativeLayout relativeLayoutUsage;
        private RelativeLayout relativeLayoutSingleQuantity;
        private RelativeLayout relativeLayoutFrequency;
        private EditText editSingleNum;
        private EditText editFrequency;
        private View viewBottom;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewDrugName = itemView.findViewById(R.id.textViewDrugName);
            textViewSpecifications = itemView.findViewById(R.id.textViewSpecifications);
            rlDel = itemView.findViewById(R.id.rlDel);
            rlAdd = itemView.findViewById(R.id.rlAdd);
            textViewNumber = itemView.findViewById(R.id.textViewNumber);
            textViewFrequency = itemView.findViewById(R.id.textViewFrequency);
            textViewSingleQuantity = itemView.findViewById(R.id.textViewSingleQuantity);
            textViewUsage = itemView.findViewById(R.id.textViewUsage);
            buttonDelete = itemView.findViewById(R.id.buttonDelete);
            relativeLayoutUsage = itemView.findViewById(R.id.relativeLayoutUsage);
            relativeLayoutSingleQuantity = itemView.findViewById(R.id.relativeLayoutSingleQuantity);
            relativeLayoutFrequency = itemView.findViewById(R.id.relativeLayoutFrequency);
            editSingleNum = itemView.findViewById(R.id.editSingleNum);
            editFrequency = itemView.findViewById(R.id.editFrequency);
            viewBottom = itemView.findViewById(R.id.viewLine01);
        }
    }

    public interface AddNumberListener {
        void addNumber(String num, int position);
    }

    public interface MinusNumberListener {
        void minusNumber(String num, int position);
    }

    public interface NumChangeListener {
        void minusNumber(int position, int type);
    }

    public interface ItemListener {
        void minusNumber(int position, int type);

        void dictNum(int position);

        void usage(int position);

        void dosageUnits(int position);

        void delete(int position);
    }

    public void setChangeListener(ItemListener itemListener) {
        this.mItemListener = itemListener;
    }

}
