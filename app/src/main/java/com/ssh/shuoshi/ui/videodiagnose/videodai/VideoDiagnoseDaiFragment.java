package com.ssh.shuoshi.ui.videodiagnose.videodai;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
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
import com.ssh.shuoshi.ui.videodiagnose.main.VideoDiagnoseComponent;
import com.ssh.shuoshi.util.WeekUtil;

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
public class VideoDiagnoseDaiFragment extends BaseFragment implements PtrHandler, VideoDiagnoseDaiContract.View, LoadMoreWrapper.OnLoadMoreListener {

    @BindView(R.id.recycle)
    RecyclerView mRecyclerView;
    @BindView(R.id.ptr_layout)
    MyPtrClassicFrameLayout mPtrLayout;

    @Inject
    VideoDiagnoseDaiPresenter mPresenter;
    @BindView(R.id.miss_tu)
    LinearLayout missTu;
    private CommonAdapter mCommonAdapter;
    private LoadMoreWrapper mLoadMoreWrapper;
    private List<ImageDiagnoseBean.RowsBean> mData = new ArrayList<>();

    @Override
    public void initInjector() {
        getComponent(VideoDiagnoseComponent.class).inject(this);
    }

    @Override
    public int initContentView() {
        return R.layout.fragment_video_list;
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
        //显示时间
        mPtrLayout.setLastUpdateTimeRelateObject(this);
        //禁止下拉
        mPtrLayout.disableWhenHorizontalMove(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayout.VERTICAL, false));

//        mPresenter.onRefresh();
    }

    @Override
    public void initData() {

    }

    @Override
    public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
        return PtrDefaultHandler.checkContentCanBePulledDown(frame, mRecyclerView, header);
    }

    //在刷新前需要准备什么工作
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
        if (requestCode == 200) {
            mPresenter.onRefresh();
        }
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
        EventBus.getDefault().post(new ChangeTuNumEvent("first", beans.getTotal(), 2));
        if (isClear) {
            mData.clear();
        }
        mData.addAll(beans.getRows());

        if (mCommonAdapter == null) {
            mCommonAdapter = new CommonAdapter<ImageDiagnoseBean.RowsBean>(getActivity(), R.layout.fragment_video_item, mData) {
                @Override
                protected void convert(ViewHolder holder, final ImageDiagnoseBean.RowsBean bean, int position) {
                    TextView tvName = holder.getView(R.id.tv_name);
                    TextView tvSex = holder.getView(R.id.tv_sex);
                    TextView tvAge = holder.getView(R.id.tv_age);
                    TextView tvType = holder.getView(R.id.tv_type);
                    TextView tvDescribe = holder.getView(R.id.tv_describe);
                    TextView tvSubscribeTime = holder.getView(R.id.tv_subscribe_time);

                    tvName.setText(bean.getPatientName());
                    tvSex.setText(bean.getSexName());
                    tvAge.setText(bean.getPatientAge() + "岁");
                    tvType.setText(bean.getConsultationTypeName());
                    tvDescribe.setText("病情描述: " + bean.getIllnessDesc());

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
                    tvSubscribeTime.setText("预约时间：" + timeDate + " " + timeWeek + " " + timeStart + "-" + timeEnd);
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
