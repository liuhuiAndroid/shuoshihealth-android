package com.ssh.shuoshi.ui.imagediagnose.imagedai;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.ssh.shuoshi.R;
import com.ssh.shuoshi.bean.ImageDiagnoseBean;
import com.ssh.shuoshi.eventbus.ChangeTuNumEvent;
import com.ssh.shuoshi.library.adapter.CommonAdapter;
import com.ssh.shuoshi.library.adapter.MultiItemTypeAdapter;
import com.ssh.shuoshi.library.adapter.base.ViewHolder;
import com.ssh.shuoshi.library.adapter.wrapper.LoadMoreWrapper;
import com.ssh.shuoshi.library.widget.MyPtrClassicFrameLayout;
import com.ssh.shuoshi.ui.BaseFragment;
import com.ssh.shuoshi.ui.graphicinquiry.GraphicInquiryActivity;
import com.ssh.shuoshi.ui.imagediagnose.main.ImageDiagnoseComponent;
import com.ssh.shuoshi.util.WeekUtil;
import com.tencent.imsdk.TIMConversation;
import com.tencent.imsdk.TIMConversationType;
import com.tencent.imsdk.TIMManager;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;

/**
 * created by hwt on 2020/12/9
 */
public class ImageDiagnoseDaiFragment extends BaseFragment implements PtrHandler, ImageDiagnoseDaiContract.View, LoadMoreWrapper.OnLoadMoreListener {

    @BindView(R.id.recycle)
    RecyclerView mRecyclerView;
    @BindView(R.id.ptr_layout)
    MyPtrClassicFrameLayout mPtrLayout;

    @Inject
    ImageDiagnoseDaiPresenter mPresenter;
    @BindView(R.id.miss_tu)
    LinearLayout missTu;
    private CommonAdapter mCommonAdapter;
    private LoadMoreWrapper mLoadMoreWrapper;
    private List<ImageDiagnoseBean.RowsBean> mData = new ArrayList<>();

    @Override
    public void initInjector() {
        getComponent(ImageDiagnoseComponent.class).inject(this);
    }

    @Override
    public int initContentView() {
        return R.layout.fragment_image_dai;
    }

    @Override
    public void getBundle(Bundle bundle) {

    }

    @Override
    public void onSupportVisible() {
        super.onSupportVisible();
        mPresenter.onRefresh();
    }

    @SuppressLint("WrongConstant")
    @Override
    public void initUI(View view) {
        mPresenter.attachView(this);
        mPtrLayout.setPtrHandler(this);
        //????????????
        mPtrLayout.setLastUpdateTimeRelateObject(this);
        //????????????
        mPtrLayout.disableWhenHorizontalMove(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayout.VERTICAL, false));


    }

    @Override
    public void initData() {

    }

    @Override
    public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
        return PtrDefaultHandler.checkContentCanBePulledDown(frame, mRecyclerView, header);
    }

    //????????????????????????????????????
    @Override
    public void onRefreshBegin(PtrFrameLayout frame) {
        mPresenter.onRefresh();
    }

    @Override
    public void onError(Throwable throwable) {
        loadError(throwable);
        if (mPtrLayout != null && mPtrLayout.isRefreshing()) {
            mPtrLayout.refreshComplete();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == 200) {
//            mPresenter.onRefresh();
//        }
    }

    @Override
    public void onRefreshCompleted(ImageDiagnoseBean beans, boolean isClear) {
        if (beans == null || beans.getRows().size() == 0) {
            missTu.setVisibility(View.VISIBLE);
            mRecyclerView.setVisibility(View.GONE);
        } else {
            missTu.setVisibility(View.GONE);
            mRecyclerView.setVisibility(View.VISIBLE);
        }
        EventBus.getDefault().post(new ChangeTuNumEvent("first", beans.getTotal(), 1));
        if (isClear) {
            mData.clear();
        }
        mData.addAll(beans.getRows());

        if (mCommonAdapter == null) {
            mCommonAdapter = new CommonAdapter<ImageDiagnoseBean.RowsBean>(getActivity(), R.layout.fragment_image_dai_item, mData) {
                @Override
                protected void convert(ViewHolder holder, final ImageDiagnoseBean.RowsBean bean, int position) {

                    ImageView ivHead = holder.getView(R.id.iv_header);
                    TextView tvName = holder.getView(R.id.tv_name);
                    TextView tvSex = holder.getView(R.id.tv_sex);
                    TextView tvAge = holder.getView(R.id.tv_age);
                    TextView tvType = holder.getView(R.id.tv_type);
                    TextView tvDescribe = holder.getView(R.id.tv_describe);
                    TextView tvTime = holder.getView(R.id.tv_time);

                    tvName.setText(bean.getPatientName());
                    tvSex.setText(bean.getSexName());
                    tvAge.setText(bean.getPatientAge() + "???");

                    if (bean.getConsultationTypeId() == 1) {
                        // ????????????
                        tvType.setText("????????????");
                        tvType.setTextColor(Color.parseColor("#FFFF824D"));
                        tvType.setBackground(getResources().getDrawable(R.drawable.all_orange_round));
                    } else if (bean.getConsultationTypeId() == 2) {
                        // ????????????
                        tvType.setText("????????????");
                        tvType.setTextColor(Color.parseColor("#FF60B2FF"));
                        tvType.setBackground(getResources().getDrawable(R.drawable.all_orange_round_3));
                    } else if (bean.getConsultationTypeId() == 3) {
                        // ????????????
                        tvType.setText("????????????");
                        tvType.setTextColor(Color.parseColor("#FF34D386"));
                        tvType.setBackground(getResources().getDrawable(R.drawable.all_orange_round_2));
                    }

                    tvDescribe.setText("????????????: " + bean.getIllnessDesc());
                    Glide.with(getActivity()).load(R.drawable.default_head).into(ivHead);

                    TextView tvSubscribeTime = holder.getView(R.id.tv_subscribe_time);
                    if (bean.getConsultationTypeId() == 2) {
                        tvSubscribeTime.setVisibility(View.VISIBLE);
                        String startTime = bean.getStartTime();
                        String endTime = bean.getEndTime();
                        String timeDate = "";
                        String timeWeek = "";
                        String timeStart = "";
                        String timeEnd = "";
                        if (!TextUtils.isEmpty(startTime) && !TextUtils.isEmpty(endTime)) {
                            timeDate = startTime.substring(5, 10);
                            timeWeek = WeekUtil.getWeekOfString(startTime.substring(0, 10));
                            timeStart = startTime.substring(startTime.length() - 5);
                            timeEnd = endTime.substring(endTime.length() - 5);
                        }
                        tvSubscribeTime.setText("???????????????" + timeDate + " " + timeWeek + " " + timeStart + "-" + timeEnd);
                        tvTime.setText("");
                    } else {
                        //?????????????????????
                        tvTime.setVisibility(View.VISIBLE);
                        tvTime.setText(bean.getPayTime());
                        tvSubscribeTime.setText(" ");
                    }
                }
            };

            mCommonAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                    Intent intent = new Intent(getActivity(), GraphicInquiryActivity.class);
                    ImageDiagnoseBean.RowsBean rowsBean = mData.get(position);
                    intent.putExtra("rowsBean", rowsBean);
                    if (rowsBean.getConsultationTypeId() == 1) {
                        intent.putExtra("type", GraphicInquiryActivity.TYPE_GRAPHIC);
                    } else if (rowsBean.getConsultationTypeId() == 2) {
                        intent.putExtra("type", GraphicInquiryActivity.TYPE_VIDEO);
                    } else if (rowsBean.getConsultationTypeId() == 3) {
                        intent.putExtra("type", GraphicInquiryActivity.TYPE_GRAPHIC_EXPORT);
                    } else if (rowsBean.getConsultationTypeId() == 4) {
                        intent.putExtra("type", GraphicInquiryActivity.TYPE_VIDEO_EXPORT);
                    }
                    startActivityForResult(intent, 200);
                }

                @Override
                public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                    return false;
                }
            });

            mLoadMoreWrapper = new LoadMoreWrapper(mCommonAdapter);
            mLoadMoreWrapper.setLoadMoreView(LayoutInflater.from(getActivity())
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
    public void onLoadMoreRequested() {
        mPresenter.onLoadMore();
    }
}
