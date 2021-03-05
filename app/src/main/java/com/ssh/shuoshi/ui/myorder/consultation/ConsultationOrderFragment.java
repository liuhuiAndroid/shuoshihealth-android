package com.ssh.shuoshi.ui.myorder.consultation;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ssh.shuoshi.R;
import com.ssh.shuoshi.bean.ConsultationBillBean;
import com.ssh.shuoshi.library.adapter.CommonAdapter;
import com.ssh.shuoshi.library.adapter.MultiItemTypeAdapter;
import com.ssh.shuoshi.library.adapter.base.ViewHolder;
import com.ssh.shuoshi.library.adapter.wrapper.LoadMoreWrapper;
import com.ssh.shuoshi.library.widget.MyPtrClassicFrameLayout;
import com.ssh.shuoshi.ui.BaseFragment;
import com.ssh.shuoshi.ui.myorder.MyOrderComponent;
import com.ssh.shuoshi.ui.myorder.consultationdetail.ConsultationDetailActivity;
import com.ssh.shuoshi.view.pickview.builder.TimePickerBuilder;
import com.ssh.shuoshi.view.pickview.listener.OnTimeSelectListener;
import com.ssh.shuoshi.view.pickview.view.TimePickerView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;

import static com.ssh.shuoshi.library.util.TimeUtils.getTime;

/**
 * 问诊订单
 */
public class ConsultationOrderFragment extends BaseFragment implements ConsultationOrderContract.View,
        LoadMoreWrapper.OnLoadMoreListener, PtrHandler, View.OnClickListener {

    @Inject
    ConsultationOrderPresenter mPresenter;

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.textTime)
    TextView textTime;
    @BindView(R.id.textAmount)
    TextView textAmount;
    @BindView(R.id.ptr_layout)
    MyPtrClassicFrameLayout mPtrLayout;
    @BindView(R.id.miss_tu)
    LinearLayout missTu;
    @BindView(R.id.imgArrow)
    ImageView imgArrow;

    private List<ConsultationBillBean.ConsultationBean> mData = new ArrayList<>();
    private LoadMoreWrapper mLoadMoreWrapper;
    private CommonAdapter mCommonAdapter;

    @Override
    public void initInjector() {
        getComponent(MyOrderComponent.class).inject(this);
    }

    @Override
    public int initContentView() {
        return R.layout.fragment_consultation_order;
    }

    @Override
    public void getBundle(Bundle bundle) {

    }

    @Override
    public void initUI(View view) {
        mPresenter.attachView(this);

        textTime.setOnClickListener(this);
        imgArrow.setOnClickListener(this);
        initRecyclerView();

        mPtrLayout.setPtrHandler(this);
        //显示时间
        mPtrLayout.setLastUpdateTimeRelateObject(this);
        //禁止下拉
        mPtrLayout.disableWhenHorizontalMove(true);

        Calendar cd = Calendar.getInstance();
        int year = cd.get(Calendar.YEAR);
        int month = cd.get(Calendar.MONTH) + 1;
        String monthS = "";
        if (month < 10) {
            monthS = "0" + month;
        } else {
            monthS = month + "";
        }
        textTime.setText(year + "年" + monthS + "月");

        mPresenter.setData(year + "-" + monthS);
        mPresenter.loadData();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.textTime:
            case R.id.imgArrow:
                chooseTime();
                imgArrow.setImageDrawable(getResources().getDrawable(R.drawable.up));
                break;
        }
    }

    private void chooseTime() {

        Calendar startDate = Calendar.getInstance();
        startDate.set(2018, 1, 23);
        Calendar endDate = Calendar.getInstance();

        TimePickerView pickerView = new TimePickerBuilder(getActivity(), new OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {
                imgArrow.setImageDrawable(getResources().getDrawable(R.drawable.down));
                String time = getTime(date);
                String opt1tx = time.substring(0, 4);
                String opt2tx = time.substring(5, 7);
                String address = opt1tx + "年" + opt2tx + "月";
                textTime.setText(address);

                mPresenter.setData(opt1tx + "-" + opt2tx);
                mPresenter.onRefresh();
            }
        }).addOnCancelClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imgArrow.setImageDrawable(getResources().getDrawable(R.drawable.down));
            }
        }).setOutSideCancelable(false)
                .setTitleText("选择时间")
                .setContentTextSize(16)
                .setDate(endDate)
                .setRangDate(startDate, endDate)
                .setType(new boolean[]{true, true, false, false, false, false})
                .build();
        pickerView.show();
    }

    private void initRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    @Override
    public void onError(Throwable throwable) {
        loadError(throwable);
        if (mPtrLayout != null && mPtrLayout.isRefreshing()) {
            mPtrLayout.refreshComplete();
        }
    }

    @Override
    public void onRefreshCompleted(ConsultationBillBean beans, boolean isClear) {

        if (beans == null || beans.getHisConsultationBillDtos().getRows().size() == 0) {
            missTu.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        } else {
            missTu.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        }

        if (isClear) {
            mData.clear();
        }
        mData.addAll(beans.getHisConsultationBillDtos().getRows());

        textAmount.setText("总计  " + beans.getTotalPrice());

        if (mCommonAdapter == null) {
            mCommonAdapter = new CommonAdapter<ConsultationBillBean.ConsultationBean>(getActivity(),
                    R.layout.item_consultation_order, mData) {
                @Override
                protected void convert(ViewHolder holder, final ConsultationBillBean.ConsultationBean bean, int position) {
                    TextView textName = holder.getView(R.id.textName);
                    TextView textSex = holder.getView(R.id.textSex);
                    TextView textAge = holder.getView(R.id.textAge);
                    TextView textTag = holder.getView(R.id.textTag);
                    TextView textPrice = holder.getView(R.id.textPrice);
                    TextView textTime = holder.getView(R.id.textTime);
                    TextView textState = holder.getView(R.id.textState);
                    textName.setText(bean.getPatientName());
                    textSex.setText(bean.getSexName());
                    textAge.setText(bean.getPatientAge() + "岁");
                    textTag.setText(bean.getConsultationTypeName());
                    double amount = bean.getAmount();
                    textPrice.setText(amount + "");
                    textTime.setText(bean.getConsultationCreateTime());
                    if (amount < 0) {
                        textState.setText("已退款");
                    } else {
                        textState.setText("");
                    }
                }
            };

            mCommonAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                    ConsultationBillBean.ConsultationBean consultationBean = mData.get(position);
                    Intent intent = new Intent(getActivity(), ConsultationDetailActivity.class);
                    intent.putExtra("data", consultationBean);
                    startActivity(intent);
                }

                @Override
                public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                    return false;
                }
            });


            mLoadMoreWrapper = new LoadMoreWrapper(mCommonAdapter);
            mLoadMoreWrapper.setLoadMoreView(LayoutInflater.from(getActivity())
                    .inflate(R.layout.footer_view_load_more, recyclerView, false));
            mLoadMoreWrapper.setOnLoadMoreListener(this);
            recyclerView.setAdapter(mLoadMoreWrapper);
        } else {
            if (recyclerView.getScrollState() == RecyclerView.SCROLL_STATE_IDLE ||
                    (!recyclerView.isComputingLayout())) {
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
    public void onDestroy() {
        super.onDestroy();
        mPresenter.detachView();
    }


    @Override
    public void initData() {

    }

    @Override
    public void onLoadMoreRequested() {
        mPresenter.onLoadMore();
    }

    @Override
    public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
        return PtrDefaultHandler.checkContentCanBePulledDown(frame, recyclerView, header);
    }

    @Override
    public void onRefreshBegin(PtrFrameLayout frame) {
        mPresenter.onRefresh();
    }

}
