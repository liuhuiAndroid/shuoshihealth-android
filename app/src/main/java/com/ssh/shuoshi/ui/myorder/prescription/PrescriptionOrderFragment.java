package com.ssh.shuoshi.ui.myorder.prescription;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ssh.shuoshi.R;
import com.ssh.shuoshi.bean.ConsultationBillBean;
import com.ssh.shuoshi.bean.TimeBean;
import com.ssh.shuoshi.library.adapter.CommonAdapter;
import com.ssh.shuoshi.library.adapter.base.ViewHolder;
import com.ssh.shuoshi.library.adapter.wrapper.LoadMoreWrapper;
import com.ssh.shuoshi.library.widget.MyPtrClassicFrameLayout;
import com.ssh.shuoshi.ui.BaseFragment;
import com.ssh.shuoshi.ui.myorder.MyOrderComponent;
import com.ssh.shuoshi.ui.myorder.consultation.ConsultationOrderContract;
import com.ssh.shuoshi.view.pickview.builder.OptionsPickerBuilder;
import com.ssh.shuoshi.view.pickview.view.OptionsPickerView;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;

/**
 * 处方订单
 */
public class PrescriptionOrderFragment extends BaseFragment implements PrescriptionOrderContract.View,
        LoadMoreWrapper.OnLoadMoreListener, PtrHandler {

    @Inject
    PrescriptionOrderPresenter mPresenter;

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.textTime)
    TextView textTime;
    @BindView(R.id.textAmount)
    TextView textAmount;
    @BindView(R.id.ptr_layout)
    MyPtrClassicFrameLayout mPtrLayout;

    private List<ConsultationBillBean.ConsultationBean> mData = new ArrayList<>();
    private LoadMoreWrapper mLoadMoreWrapper;
    private CommonAdapter mCommonAdapter;

    @Override
    public void initInjector() {
        getComponent(MyOrderComponent.class).inject(this);
    }

    @Override
    public int initContentView() {
        return R.layout.fragment_prescription_order;
    }

    @Override
    public void getBundle(Bundle bundle) {

    }

    @Override
    public void initUI(View view) {
        mPresenter.attachView(this);
        textTime.setOnClickListener(v -> chooseTime());
        initRecyclerView();

        mPtrLayout.setPtrHandler(this);
        //显示时间
        mPtrLayout.setLastUpdateTimeRelateObject(this);
        //禁止下拉
        mPtrLayout.disableWhenHorizontalMove(true);

        Calendar cd = Calendar.getInstance();
        int year = cd.get(Calendar.YEAR);
        int month = cd.get(Calendar.MONTH) + 1;
        textTime.setText(year + "年" + month + "月");

        mPresenter.setData(year + "-" + month);
        mPresenter.loadData();
    }



    private List<TimeBean> mBean;
    ArrayList<List<String>> mCityBean = new ArrayList<>();

    private void chooseTime() {
        OptionsPickerView pvOptions = new OptionsPickerBuilder(getActivity(), (options1, options2, options3, v) -> {
            //返回的分别是三个级别的选中位置
            String opt1tx = mBean.size() > 0 ?
                    mBean.get(options1).getPickerViewText() : "";
            String opt2tx = mCityBean.size() > 0
                    && mCityBean.get(options1).size() > 0 ?
                    mCityBean.get(options1).get(options2) : "";
            String address = opt1tx + "年" + opt2tx + "月";
            textTime.setText(address);

            mPresenter.setData(opt1tx + "-" + opt2tx);
            mPresenter.onRefresh();
        })
                .setTitleText("请选择年月")
                .setCancelText(" ")
                .setDividerColor(Color.BLACK)
                .setContentTextSize(20)
                .build();
        pvOptions.setPicker(mBean, mCityBean);
        pvOptions.show();
    }

    private void initRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(),
                DividerItemDecoration.VERTICAL));
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
        if (isClear) {
            mData.clear();
        }
        mData.addAll(beans.getHisConsultationBillDtos().getRows());

        double total = 0.0;
        for (int i = 0; i < mData.size(); i++) {
            double amount = mData.get(i).getAmount();
            total += amount;
        }

        BigDecimal bg = new BigDecimal(total);
        double f1 = bg.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
        textAmount.setText("总计 " + f1);

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

                    textName.setText(bean.getPatientName());
                    textSex.setText(bean.getSexName());
                    textAge.setText(bean.getPatientAge() + "岁");
                    if (bean.getConsultationState() == 1) {
                        textTag.setText("图文问诊");
                    } else if (bean.getConsultationState() == 2) {
                        textTag.setText("视频问诊");
                    } else if (bean.getConsultationState() == 3) {
                        textTag.setText("专家团队问诊");
                    } else if (bean.getConsultationState() == 4) {
                        textTag.setText("专家团队视频");
                    }
                    textPrice.setText(bean.getAmount() + "");
                    textTime.setText(bean.getCreateTime());
                }
            };

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
        mBean = new ArrayList<>();
        List<TimeBean.MonthBean> monthBeanList = new ArrayList<>();
        TimeBean.MonthBean monthBean1 = new TimeBean.MonthBean("1");
        TimeBean.MonthBean monthBean2 = new TimeBean.MonthBean("2");
        TimeBean.MonthBean monthBean3 = new TimeBean.MonthBean("3");
        TimeBean.MonthBean monthBean4 = new TimeBean.MonthBean("4");
        TimeBean.MonthBean monthBean5 = new TimeBean.MonthBean("5");
        TimeBean.MonthBean monthBean6 = new TimeBean.MonthBean("6");
        TimeBean.MonthBean monthBean7 = new TimeBean.MonthBean("7");
        TimeBean.MonthBean monthBean8 = new TimeBean.MonthBean("8");
        TimeBean.MonthBean monthBean9 = new TimeBean.MonthBean("9");
        TimeBean.MonthBean monthBean10 = new TimeBean.MonthBean("10");
        TimeBean.MonthBean monthBean11 = new TimeBean.MonthBean("11");
        TimeBean.MonthBean monthBean12 = new TimeBean.MonthBean("12");
        monthBeanList.add(monthBean1);
        monthBeanList.add(monthBean2);
        monthBeanList.add(monthBean3);
        monthBeanList.add(monthBean4);
        monthBeanList.add(monthBean5);
        monthBeanList.add(monthBean6);
        monthBeanList.add(monthBean7);
        monthBeanList.add(monthBean8);
        monthBeanList.add(monthBean9);
        monthBeanList.add(monthBean10);
        monthBeanList.add(monthBean11);
        monthBeanList.add(monthBean12);

        TimeBean timeBean2018 = new TimeBean("2018");
        timeBean2018.setMonths(monthBeanList);
        mBean.add(timeBean2018);

        TimeBean timeBean2019 = new TimeBean("2019");
        timeBean2019.setMonths(monthBeanList);
        mBean.add(timeBean2019);

        TimeBean timeBean2020 = new TimeBean("2020");
        timeBean2020.setMonths(monthBeanList);
        mBean.add(timeBean2020);

        TimeBean timeBean2021 = new TimeBean("2021");
        timeBean2021.setMonths(monthBeanList);
        mBean.add(timeBean2021);

        TimeBean timeBean2022 = new TimeBean("2022");
        timeBean2022.setMonths(monthBeanList);
        mBean.add(timeBean2022);

        for (int i = 0; i < mBean.size(); i++) {     //遍历省份
            List<String> cityList = new ArrayList<>();
            for (int j = 0; j < mBean.get(i).getMonths().size(); j++) {
                cityList.add(mBean.get(i).getMonths().get(j).getMonth());
            }
            mCityBean.add(cityList);
        }
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
