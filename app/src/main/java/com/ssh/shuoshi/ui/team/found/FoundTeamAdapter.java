package com.ssh.shuoshi.ui.team.found;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ssh.shuoshi.R;
import com.ssh.shuoshi.bean.team.DoctorListBean;

import java.util.List;

/**
 * created by hwt on 2021/1/3
 */
public class FoundTeamAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<DoctorListBean.RowsBean> mData;
    private Context context;
    //布局标识集合
    private static final int TYPE_ONE = 0;//普通头像
    private static final int TYPE_TWO = 1;//添加按钮

    private OnItemClickListener mOnItemClickListener;

    public FoundTeamAdapter(Context context, List<DoctorListBean.RowsBean> beans) {
        this.context = context;
        this.mData = beans;
    }

    public List<DoctorListBean.RowsBean> getData() {
        return mData;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == TYPE_ONE) {
            View mView1 = LayoutInflater.from(context).inflate(R.layout.item_found_team_head, null);
            ViewHolderHead viewHolderHead = new ViewHolderHead(mView1);
            return viewHolderHead;
        } else {
            View mView2 = LayoutInflater.from(context).inflate(R.layout.item_found_team_add, null);
            ViewHolderAdd viewHolderHead = new ViewHolderAdd(mView2);
            return viewHolderHead;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        if (holder instanceof ViewHolderHead) {
            DoctorListBean.RowsBean bean = mData.get(position);
            ((ViewHolderHead) holder).textName.setText(bean.getName());
        } else if (holder instanceof ViewHolderAdd) {
            //添加按钮点击
            ((ViewHolderAdd) holder).rlAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mOnItemClickListener != null) {
                        mOnItemClickListener.onItemClickEvent(v);
                    }
                }
            });

            if (mData.size() >= 10) {
                ((ViewHolderAdd) holder).rlAdd.setVisibility(View.GONE);
            } else {
                ((ViewHolderAdd) holder).rlAdd.setVisibility(View.VISIBLE);
            }
        }
    }

    public interface OnItemClickListener {
        void onItemClickEvent(View view);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
    }

    @Override
    public int getItemCount() {
        return mData == null ? 1 : mData.size() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        return mData.size() == position ? TYPE_TWO : TYPE_ONE;
    }

    class ViewHolderHead extends RecyclerView.ViewHolder {
        private ImageView ImageDoctorAvatar;
        private TextView textName;

        public ViewHolderHead(@NonNull View itemView) {
            super(itemView);
            ImageDoctorAvatar = itemView.findViewById(R.id.ImageDoctorAvatar);
            textName = itemView.findViewById(R.id.textName);

        }
    }

    class ViewHolderAdd extends RecyclerView.ViewHolder {
        private RelativeLayout rlAdd;

        private ViewHolderAdd(@NonNull View itemView) {
            super(itemView);
            rlAdd = itemView.findViewById(R.id.rlAdd);
        }
    }


}
