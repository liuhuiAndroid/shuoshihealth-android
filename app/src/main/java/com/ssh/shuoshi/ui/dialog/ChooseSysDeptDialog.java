package com.ssh.shuoshi.ui.dialog;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ssh.shuoshi.R;
import com.ssh.shuoshi.bean.SysDeptBean;
import com.ssh.shuoshi.library.adapter.CommonAdapter;
import com.ssh.shuoshi.library.adapter.MultiItemTypeAdapter;
import com.ssh.shuoshi.library.adapter.base.ViewHolder;

import java.util.List;

/**
 * 查询科室,医生职务
 * created by hwt on 2020/12/10
 */
public class ChooseSysDeptDialog extends Dialog implements View.OnClickListener {

    private int mType;
    private String mTitle;
    private List<SysDeptBean.RowsBean> mData;
    private Context mContext;
    private ItemClickListener mItemClickListener;

    public ChooseSysDeptDialog(@NonNull Context context, String title, List<SysDeptBean.RowsBean> list, int type) {
        super(context);
        this.mContext = context;
        this.mData = list;
        this.mTitle = title;
        this.mType = type;
    }

    public ChooseSysDeptDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }

    protected ChooseSysDeptDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        this.mContext = context;
    }

    @SuppressLint("WrongConstant")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.choose_item_dialog);

        TextView tvTitle = findViewById(R.id.tv_tile);
        tvTitle.setText(mTitle);

        ImageView imgClose = findViewById(R.id.img_close);
        imgClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mItemClickListener != null) {
                    mItemClickListener.cancel();
                }
            }
        });
        RecyclerView recyclerView = findViewById(R.id.recycle);
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayout.VERTICAL, false));
        CommonAdapter mCommonAdapter = new CommonAdapter<SysDeptBean.RowsBean>(mContext, R.layout.choose_item_dialog_item, mData) {

            @Override
            protected void convert(ViewHolder holder, final SysDeptBean.RowsBean rowsBean, int position) {
                if (mType == 1) {
                    holder.setText(R.id.tv_name, rowsBean.getDeptName());
                } else {
                    holder.setText(R.id.tv_name, rowsBean.getTitleName());
                }

            }
        };

        mCommonAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                if (mItemClickListener != null) {
                    if (mType == 1) {
                        mItemClickListener.choose(mData.get(position).getDeptName(), mData.get(position).getId());
                    } else {
                        mItemClickListener.choose(mData.get(position).getTitleName(), mData.get(position).getId());
                    }
                }
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });
        recyclerView.setAdapter(mCommonAdapter);

        final Window window = getWindow();
        assert window != null;
        window.setLayout(-1, -2);//这2行,和上面的一样,注意顺序就行;
        //设置背景颜色,只有设置了这个属性,宽度才能全屏MATCH_PARENT
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        WindowManager.LayoutParams mWindowAttributes = window.getAttributes();
        mWindowAttributes.height = WindowManager.LayoutParams.WRAP_CONTENT;

        Window dialogWindow = getWindow();
        assert dialogWindow != null;
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        dialogWindow.setGravity(Gravity.BOTTOM);
        lp.x = 0;
        lp.y = 0;
        lp.width = ViewGroup.LayoutParams.MATCH_PARENT;
        dialogWindow.setAttributes(lp);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

        }
    }

    public void setOnItemClickListener(ItemClickListener itemClickListener) {
        this.mItemClickListener = itemClickListener;
    }

    public interface ItemClickListener {

        void cancel();

        void choose(String name, int id);
    }

}
