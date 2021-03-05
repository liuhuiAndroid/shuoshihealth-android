package com.ssh.shuoshi.ui.myorder;

import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.viewpager.widget.ViewPager;

import com.ssh.shuoshi.R;
import com.ssh.shuoshi.bean.count.DiagnoseCountBean;
import com.ssh.shuoshi.injector.HasComponent;
import com.ssh.shuoshi.ui.BaseActivity;
import com.ssh.shuoshi.ui.BaseFragment;
import com.ssh.shuoshi.ui.adapter.ImagePageAdapter;
import com.ssh.shuoshi.ui.dialog.LoadingDialog;
import com.ssh.shuoshi.ui.imagediagnose.main.ImageDiagnoseContract;
import com.ssh.shuoshi.ui.myorder.consultation.ConsultationOrderFragment;
import com.ssh.shuoshi.view.title.UniteTitle;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;

/**
 * 我的订单
 */
public class MyOrderActivity extends BaseActivity implements ImageDiagnoseContract.View,
        View.OnClickListener, HasComponent<MyOrderComponent> {

    private MyOrderComponent mComponent;

    @Inject
    MyOrderPresenter mPresenter;

    @BindView(R.id.title)
    UniteTitle title;
    @BindView(R.id.viewpager)
    ViewPager viewpager;
    @BindView(R.id.tv001)
    TextView tv001;
    @BindView(R.id.tv002)
    TextView tv002;

    private LoadingDialog mLoadingDialog;
    private ImagePageAdapter mAdapter;

    @Override
    public int initContentView() {
        return R.layout.activity_my_order;
    }

    @Override
    protected void initInjector() {
        mComponent = DaggerMyOrderComponent.builder()
                .applicationComponent(getApplicationComponent())
                .activityModule(getActivityModule())
                .build();
        mComponent.inject(this);
    }

    @Override
    protected void initUiAndListener() {
        mPresenter.attachView(this);
        title.setBackListener(-1, v -> finish());

        tv001.setOnClickListener(this);
        tv002.setOnClickListener(this);

        List<BaseFragment> fragments = new ArrayList<>();
        fragments.add(new ConsultationOrderFragment());
        mAdapter = new ImagePageAdapter(getSupportFragmentManager(), fragments);
        viewpager.setAdapter(mAdapter);
        viewpager.setOffscreenPageLimit(2);
        viewpager.addOnPageChangeListener(new MyOnPageChangeListener());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.detachView();
    }

    @Override
    public void showLoading() {
        if (mLoadingDialog != null) {
            hideLoading();
        }
        mLoadingDialog = LoadingDialog.show(MyOrderActivity.this, "");
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
    }

    @Override
    public void setDiagnoseCount(DiagnoseCountBean beans) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv001:
                tv001.setTextColor(getResources().getColor(R.color.orange));
                tv002.setTextColor(getResources().getColor(R.color.yiJiuBlack));
                viewpager.setCurrentItem(0);
                break;

            case R.id.tv002:
                tv001.setTextColor(getResources().getColor(R.color.yiJiuBlack));
                tv002.setTextColor(getResources().getColor(R.color.orange));
                viewpager.setCurrentItem(1);
                break;

        }
    }

    @Override
    public MyOrderComponent getComponent() {
        return mComponent;
    }

    public class MyOnPageChangeListener implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            Log.i("hwt", "选中的是" + position);
            switch (position) {
                case 0:
                    tv001.setTextColor(getResources().getColor(R.color.orange));
                    tv002.setTextColor(getResources().getColor(R.color.yiJiuBlack));
                    break;
                case 1:
                    tv001.setTextColor(getResources().getColor(R.color.yiJiuBlack));
                    tv002.setTextColor(getResources().getColor(R.color.orange));
                    break;
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }

}
