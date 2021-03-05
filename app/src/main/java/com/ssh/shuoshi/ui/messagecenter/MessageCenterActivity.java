package com.ssh.shuoshi.ui.messagecenter;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ssh.shuoshi.R;
import com.ssh.shuoshi.bean.ImageDiagnoseBean;
import com.ssh.shuoshi.bean.JPushSysMsgRecordBean;
import com.ssh.shuoshi.library.adapter.CommonAdapter;
import com.ssh.shuoshi.library.adapter.MultiItemTypeAdapter;
import com.ssh.shuoshi.library.adapter.base.ViewHolder;
import com.ssh.shuoshi.library.adapter.wrapper.LoadMoreWrapper;
import com.ssh.shuoshi.library.util.InputUtil;
import com.ssh.shuoshi.library.util.ToastUtil;
import com.ssh.shuoshi.library.widget.MyPtrClassicFrameLayout;
import com.ssh.shuoshi.ui.BaseActivity;
import com.ssh.shuoshi.ui.dialog.FollowUpTimeDialog;
import com.ssh.shuoshi.ui.dialog.LoadingDialog;
import com.ssh.shuoshi.ui.main.MainActivity;
import com.ssh.shuoshi.ui.worksetting.WorkSettingActivity;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;
import timber.log.Timber;

/**
 * 消息中心
 */
public class MessageCenterActivity extends BaseActivity implements MessageCenterContract.View,
        PtrHandler, LoadMoreWrapper.OnLoadMoreListener, View.OnClickListener {

    private Boolean currentBoolean = null;

    @Inject
    MessageCenterPresenter mPresenter;
    @BindView(R.id.imgBack)
    ImageView imgBack;
    @BindView(R.id.tvRight)
    TextView tvRight;
    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.ptr_layout)
    MyPtrClassicFrameLayout mPtrLayout;
    @BindView(R.id.cbAllChoose)
    CheckBox cbAllChoose;
    @BindView(R.id.buttonDelete)
    Button buttonDelete;
    @BindView(R.id.rlAllChoose)
    RelativeLayout rlAllChoose;

    private CommonAdapter mCommonAdapter;
    private LoadMoreWrapper mLoadMoreWrapper;
    private List<JPushSysMsgRecordBean.RowsBean> mData = new ArrayList<>();
    private LoadingDialog mLoadingDialog;

    @Override
    public int initContentView() {
        return R.layout.activity_message_center;
    }

    @Override
    protected void initInjector() {
        DaggerMessageCenterComponent.builder()
                .applicationComponent(getApplicationComponent())
                .activityModule(getActivityModule())
                .build()
                .inject(this);
    }

    @SuppressLint("WrongConstant")
    @Override
    protected void initUiAndListener() {
        mPresenter.attachView(this);
        mPtrLayout.setPtrHandler(this);
        //显示时间
        mPtrLayout.setLastUpdateTimeRelateObject(this);
        //禁止下拉
        mPtrLayout.disableWhenHorizontalMove(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayout.VERTICAL, false));
        imgBack.setOnClickListener(v -> finish());

        mPresenter.onRefresh();

        tvRight.setOnClickListener(this);

        cbAllChoose.setOnCheckedChangeListener((buttonView, isChecked) -> {
            for (int i = 0; i < mData.size(); i++) {
                mData.get(i).setChecked(isChecked);
            }
            mLoadMoreWrapper.notifyDataSetChanged();
        });

        buttonDelete.setOnClickListener(v -> {
            String ids = "";
            for (int i = 0; i < mData.size(); i++) {
                if (mData.get(i).isChecked()) {
                    ids += mData.get(i).getId();
                    ids += ",";
                }
            }
            if (ids.endsWith(",")) {
                ids.substring(0, ids.length() - 1);
            }
            if (!TextUtils.isEmpty(ids)) {
                mPresenter.deleteJpushSysMsgRecord(ids);
            } else {
                ToastUtil.showToast("请勾选需要删除的消息");
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvRight:
                if (tvRight.getText().toString().equals("编辑")) {
                    currentBoolean = false;
                    rlAllChoose.setVisibility(View.VISIBLE);
                    tvRight.setTextColor(getResources().getColor(R.color.orange));
                    tvRight.setText("取消");

                    for (int i = 0; i < mData.size(); i++) {
                        mData.get(i).setChecked(false);
                    }
                } else {
                    currentBoolean = null;
                    rlAllChoose.setVisibility(View.GONE);
                    tvRight.setTextColor(getResources().getColor(R.color.yiJiuBlack));
                    tvRight.setText("编辑");

                    for (int i = 0; i < mData.size(); i++) {
                        mData.get(i).setChecked(null);
                    }
                }
                mLoadMoreWrapper.notifyDataSetChanged();
                break;
        }
    }

    @Override
    public void onRefreshCompleted(JPushSysMsgRecordBean beans, boolean isClear) {
        if (isClear) {
            mData.clear();
        }
        for (int i = 0; i < beans.getRows().size(); i++) {
            beans.getRows().get(i).setChecked(currentBoolean);
        }
        mData.addAll(beans.getRows());

        if (mCommonAdapter == null) {
            mCommonAdapter = new CommonAdapter<JPushSysMsgRecordBean.RowsBean>(this, R.layout.item_message_center, mData) {
                @Override
                protected void convert(ViewHolder holder, final JPushSysMsgRecordBean.RowsBean bean, int pos) {
                    CheckBox cbChoose = holder.getView(R.id.cbChoose);
                    cbChoose.setOnCheckedChangeListener(null);
                    if (bean.isChecked() != null) {
                        cbChoose.setVisibility(View.VISIBLE);
                        if (bean.isChecked()) {
                            cbChoose.setChecked(true);
                        } else {
                            cbChoose.setChecked(false);
                        }
                    } else {
                        cbChoose.setVisibility(View.GONE);
                    }
                    cbChoose.setOnCheckedChangeListener((buttonView, isChecked) -> {
                        mData.get(pos).setChecked(isChecked);
                        int size = mData.size();
                        Timber.i("size:" + size);
                    });

                    ImageView imageView = holder.getView(R.id.imageView);
                    TextView textName = holder.getView(R.id.textName);
                    TextView tvType = holder.getView(R.id.tvType);
                    TextView textMessage = holder.getView(R.id.textMessage);
                    TextView textTime = holder.getView(R.id.textTime);
                    TextView textSetting = holder.getView(R.id.textSetting);
                    textSetting.setText("");
                    tvType.setVisibility(View.GONE);
                    // 消息类型，1资质认证提醒，2资质认证审核通过，3资质认证审核不通过，4设置随访提醒，5新咨询订单通知，6患者咨询提醒，7处方审核不通过提醒
                    if (bean.getMsgType() == 1) {
                        textName.setText("资质认证提醒");
                    } else if (bean.getMsgType() == 2) {
                        textName.setText("资质认证审核通过");
                    } else if (bean.getMsgType() == 3) {
                        textName.setText("资质认证审核不通过");
                    } else if (bean.getMsgType() == 4) {
                        tvType.setVisibility(View.VISIBLE);
                        textName.setText("设置随访提醒");
                        if (bean.isDealFlag() != null) {
                            if (bean.isDealFlag()) {
                                tvType.setText("已设置");
                                tvType.setBackgroundColor(Color.parseColor("#FFF7F7F7"));
                                tvType.setTextColor(Color.parseColor("#FFB9B9B9"));
                            } else {
                                textSetting.setText("设置随访时间");
                                tvType.setText("未设置");
                                tvType.setBackgroundColor(Color.parseColor("#1AFF824D"));
                                tvType.setTextColor(Color.parseColor("#FFFF824D"));
                            }
                        } else {
                            tvType.setText("已失效");
                            tvType.setBackgroundColor(Color.parseColor("#1AFF824D"));
                            tvType.setTextColor(Color.parseColor("#FFB9B9B9"));
                        }
                    } else if (bean.getMsgType() == 5) {
                        textName.setText("新咨询订单通知");
                    } else if (bean.getMsgType() == 6) {
                        textName.setText("患者咨询提醒");
                    } else if (bean.getMsgType() == 7) {
                        textName.setText("处方审核不通过提醒");
                    }
                    textMessage.setText(bean.getPushContent());
                    textTime.setText(bean.getPushTime());
                }
            };
            mCommonAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {

                @Override
                public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                    JPushSysMsgRecordBean.RowsBean rowsBean = mData.get(position);
                    if (rowsBean.getMsgType() == 2) {
                        Intent i = new Intent(MessageCenterActivity.this, WorkSettingActivity.class);
                        startActivity(i);
                    } else if (rowsBean.getMsgType() == 4 && rowsBean.isDealFlag() != null && !rowsBean.isDealFlag()) {
                        Integer pushContentId = rowsBean.getPushContentId();
                        if (pushContentId != null) {
                            mPresenter.getConsultationInfo(pushContentId.intValue(), rowsBean.getId());
                        }
                    }
                }

                @Override
                public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                    return false;
                }
            });
            mLoadMoreWrapper = new LoadMoreWrapper(mCommonAdapter);
            mLoadMoreWrapper.setLoadMoreView(LayoutInflater.from(this)
                    .inflate(R.layout.footer_view_load_more, mRecyclerView, false));
            mLoadMoreWrapper.setOnLoadMoreListener(this);
            mRecyclerView.setAdapter(mLoadMoreWrapper);
        } else {
            if (mRecyclerView.getScrollState() == RecyclerView.SCROLL_STATE_IDLE || (!mRecyclerView.isComputingLayout())) {
                mLoadMoreWrapper.notifyDataSetChanged();
            }
        }

        if (mPtrLayout != null && mPtrLayout.isRefreshing()) {
            mPtrLayout.refreshComplete();
        }
    }

    @Override
    public void onLoadCompleted(boolean isLoadAll) {
        mLoadMoreWrapper.setLoadAll(isLoadAll);
    }

    @Override
    public void deleteJpushSysMsgRecordSuccess(String bean) {
        rlAllChoose.setVisibility(View.GONE);
        tvRight.setTextColor(getResources().getColor(R.color.yiJiuBlack));
        tvRight.setText("编辑");
        mPresenter.onRefresh();
    }

    @Override
    public void onLoadMoreRequested() {
        mPresenter.onLoadMore();
    }

    @Override
    public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
        return PtrDefaultHandler.checkContentCanBePulledDown(frame, mRecyclerView, header);
    }

    @Override
    public void onRefreshBegin(PtrFrameLayout frame) {
        rlAllChoose.setVisibility(View.GONE);
        tvRight.setTextColor(getResources().getColor(R.color.yiJiuBlack));
        tvRight.setText("编辑");

        if (mData != null && mData.size() > 0) {
            for (int i = 0; i < mData.size(); i++) {
                mData.get(i).setChecked(null);
            }
        }
        mPresenter.onRefresh();
    }

    @Override
    public void showLoading() {
        if (mLoadingDialog != null) {
            hideLoading();
        }
        mLoadingDialog = LoadingDialog.show(MessageCenterActivity.this, "");
    }

    @Override
    public void hideLoading() {
        if (mLoadingDialog != null && mLoadingDialog.isShowing()) {
            mLoadingDialog.dismiss();
            mLoadingDialog = null;
        }
    }

    @Override
    public void onError(Throwable throwable) {
        loadError(throwable);
        if (mPtrLayout != null && mPtrLayout.isRefreshing()) {
            mPtrLayout.refreshComplete();
        }
    }

    @Override
    public void getConsultationInfoSuccess(ImageDiagnoseBean.RowsBean bean, int id) {
        FollowUpTimeDialog followUpTimeDialog = new FollowUpTimeDialog(MessageCenterActivity.this, R.style.dialog_physician_certification, bean);
        followUpTimeDialog.setOnItemClickListener(new FollowUpTimeDialog.ItemClickListener() {

            @Override
            public void cancel() {
                followUpTimeDialog.dismiss();
            }

            @Override
            public void confirm(String name) {
                followUpTimeDialog.dismiss();
                mPresenter.consultationEditFollowTime(Integer.parseInt(name), id);
            }

        });
        followUpTimeDialog.show();
    }

    @Override
    public void getConsultationEditFollowTimeSuccess(String bean) {
        mPresenter.onRefresh();
        InputUtil.hideSoftInput(MessageCenterActivity.this);
    }

}
