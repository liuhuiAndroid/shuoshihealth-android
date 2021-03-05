package com.ssh.shuoshi.ui.myprescription.main;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.viewpager.widget.ViewPager;

import com.ssh.shuoshi.R;
import com.ssh.shuoshi.injector.HasComponent;
import com.ssh.shuoshi.ui.BaseActivity;
import com.ssh.shuoshi.ui.BaseFragment;
import com.ssh.shuoshi.ui.adapter.ImagePageAdapter;
import com.ssh.shuoshi.ui.dialog.LoadingDialog;
import com.ssh.shuoshi.ui.myprescription.fail.MyPrescriptionFailFragment;
import com.ssh.shuoshi.ui.myprescription.ing.MyPrescriptionIngFragment;
import com.ssh.shuoshi.ui.myprescription.success.MyPrescriptionSuccessFragment;
import com.ssh.shuoshi.ui.myprescription.wait.MyPrescriptionWaitFragment;
import com.ssh.shuoshi.view.title.UniteTitle;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 我的处方
 * created by hwt on 2020/12/31
 */
public class MyPrescriptionActivity extends BaseActivity implements MyPrescriptionContract.View,
        HasComponent<MyPrescriptionComponent>, View.OnClickListener {
    @BindView(R.id.title)
    UniteTitle title;
    @BindView(R.id.tvFail)
    TextView tvFail;
    @BindView(R.id.tvIng)
    TextView tvIng;
    @BindView(R.id.tvSuccess)
    TextView tvSuccess;
    @BindView(R.id.viewpager)
    ViewPager viewpager;

    @Inject
    MyPrescriptionPresenter mPresenter;
    @BindView(R.id.tvWait)
    TextView tvWait;

    private MyPrescriptionComponent mComponent;
    private ImagePageAdapter mAdapter;
    private LoadingDialog mLoadingDialog;

    @Override
    public int initContentView() {
        return R.layout.activity_my_prescription;
    }

    @Override
    protected void initInjector() {
        mComponent = DaggerMyPrescriptionComponent.builder()
                .applicationComponent(getApplicationComponent())
                .activityModule(getActivityModule())
                .myPrescriptionModule(new MyPrescriptionModule(this))
                .build();
        mComponent.inject(this);
    }

    @Override
    protected void initUiAndListener() {
        mPresenter.attachView(this);

        title.setBackListener(-1, v -> finish());

        tvFail.setOnClickListener(this);
        tvWait.setOnClickListener(this);
        tvIng.setOnClickListener(this);
        tvSuccess.setOnClickListener(this);

        List<BaseFragment> fragments = new ArrayList<>();
        fragments.add(new MyPrescriptionFailFragment());
        fragments.add(new MyPrescriptionWaitFragment());
        fragments.add(new MyPrescriptionIngFragment());
        fragments.add(new MyPrescriptionSuccessFragment());
        mAdapter = new ImagePageAdapter(getSupportFragmentManager(), fragments);
        viewpager.setAdapter(mAdapter);
        viewpager.setOffscreenPageLimit(3);
        viewpager.addOnPageChangeListener(new MyOnPageChangeListener());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvFail:
                tvFail.setTextColor(getResources().getColor(R.color.orange));
                tvWait.setTextColor(getResources().getColor(R.color.yiJiuBlack));
                tvIng.setTextColor(getResources().getColor(R.color.yiJiuBlack));
                tvSuccess.setTextColor(getResources().getColor(R.color.yiJiuBlack));
                viewpager.setCurrentItem(0);
                break;

            case R.id.tvWait:
                tvFail.setTextColor(getResources().getColor(R.color.yiJiuBlack));
                tvWait.setTextColor(getResources().getColor(R.color.orange));
                tvIng.setTextColor(getResources().getColor(R.color.yiJiuBlack));
                tvSuccess.setTextColor(getResources().getColor(R.color.yiJiuBlack));
                viewpager.setCurrentItem(1);
                break;

            case R.id.tvIng:
                tvFail.setTextColor(getResources().getColor(R.color.yiJiuBlack));
                tvWait.setTextColor(getResources().getColor(R.color.yiJiuBlack));
                tvIng.setTextColor(getResources().getColor(R.color.orange));
                tvSuccess.setTextColor(getResources().getColor(R.color.yiJiuBlack));
                viewpager.setCurrentItem(2);
                break;

            case R.id.tvSuccess:
                tvFail.setTextColor(getResources().getColor(R.color.yiJiuBlack));
                tvWait.setTextColor(getResources().getColor(R.color.yiJiuBlack));
                tvIng.setTextColor(getResources().getColor(R.color.yiJiuBlack));
                tvSuccess.setTextColor(getResources().getColor(R.color.orange));
                viewpager.setCurrentItem(3);
                break;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    private class MyOnPageChangeListener implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            switch (position) {
                case 0:
                    tvFail.setTextColor(getResources().getColor(R.color.orange));
                    tvWait.setTextColor(getResources().getColor(R.color.yiJiuBlack));
                    tvIng.setTextColor(getResources().getColor(R.color.yiJiuBlack));
                    tvSuccess.setTextColor(getResources().getColor(R.color.yiJiuBlack));
                    break;
                case 1:
                    tvFail.setTextColor(getResources().getColor(R.color.yiJiuBlack));
                    tvWait.setTextColor(getResources().getColor(R.color.orange));
                    tvIng.setTextColor(getResources().getColor(R.color.yiJiuBlack));
                    tvSuccess.setTextColor(getResources().getColor(R.color.yiJiuBlack));
                    break;
                case 2:
                    tvFail.setTextColor(getResources().getColor(R.color.yiJiuBlack));
                    tvWait.setTextColor(getResources().getColor(R.color.yiJiuBlack));
                    tvIng.setTextColor(getResources().getColor(R.color.orange));
                    tvSuccess.setTextColor(getResources().getColor(R.color.yiJiuBlack));
                    break;
                case 3:
                    tvFail.setTextColor(getResources().getColor(R.color.yiJiuBlack));
                    tvWait.setTextColor(getResources().getColor(R.color.yiJiuBlack));
                    tvIng.setTextColor(getResources().getColor(R.color.yiJiuBlack));
                    tvSuccess.setTextColor(getResources().getColor(R.color.orange));
                    break;
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }

    @Override
    public void showLoading() {
        if (mLoadingDialog != null) {
            hideLoading();
        }
        mLoadingDialog = LoadingDialog.show(MyPrescriptionActivity.this, "");
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
    public MyPrescriptionComponent getComponent() {
        return mComponent;
    }
}
