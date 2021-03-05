package com.ssh.shuoshi.ui.videodiagnose.main;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.viewpager.widget.ViewPager;

import com.ssh.shuoshi.R;
import com.ssh.shuoshi.bean.count.DiagnoseCountBean;
import com.ssh.shuoshi.eventbus.ChangeTuNumEvent;
import com.ssh.shuoshi.injector.HasComponent;
import com.ssh.shuoshi.ui.BaseActivity;
import com.ssh.shuoshi.ui.BaseFragment;
import com.ssh.shuoshi.ui.adapter.ImagePageAdapter;
import com.ssh.shuoshi.ui.dialog.LoadingDialog;
import com.ssh.shuoshi.ui.videodiagnose.videodai.VideoDiagnoseDaiFragment;
import com.ssh.shuoshi.ui.videodiagnose.videoend.VideoDiagnoseEndFragment;
import com.ssh.shuoshi.ui.videodiagnose.videoing.VideoDiagnoseIngFragment;
import com.ssh.shuoshi.view.title.UniteTitle;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * created by hwt on 2020/12/9
 */
public class VideoDiagnoseActivity extends BaseActivity implements VideoDiagnoseContract.View,
        HasComponent<VideoDiagnoseComponent>, View.OnClickListener {

    @BindView(R.id.title)
    UniteTitle title;
    @BindView(R.id.tv_dai)
    TextView tvDai;
    @BindView(R.id.tv_ing)
    TextView tvIng;
    @BindView(R.id.tv_end)
    TextView tvEnd;
    @BindView(R.id.viewpager)
    ViewPager viewpager;
    private VideoDiagnoseComponent mComponent;
    private LoadingDialog mLoadingDialog;
    private ImagePageAdapter mAdapter;
    @Inject
    VideoDiagnosePresenter mPresenter;

    @Override
    public int initContentView() {
        return R.layout.activity_video_diagnose;
    }

    @Override
    protected void initInjector() {
        mComponent = DaggerVideoDiagnoseComponent.builder()
                .applicationComponent(getApplicationComponent())
                .activityModule(getActivityModule())
                .videoDiagnoseModule(new VideoDiagnoseModule(this))
                .build();
        mComponent.inject(this);
    }

    @Override
    protected void initUiAndListener() {
        mPresenter.attachView(this);

        title.setBackListener(-1, v -> finish());

        mPresenter.getCountMap();

        tvDai.setOnClickListener(this);
        tvIng.setOnClickListener(this);
        tvEnd.setOnClickListener(this);

        List<BaseFragment> fragments = new ArrayList<>();
        fragments.add(new VideoDiagnoseDaiFragment());
        fragments.add(new VideoDiagnoseIngFragment());
        fragments.add(new VideoDiagnoseEndFragment());
        mAdapter = new ImagePageAdapter(getSupportFragmentManager(), fragments);
        viewpager.setAdapter(mAdapter);
        viewpager.setOffscreenPageLimit(3);
        viewpager.addOnPageChangeListener(new MyVideoOnPageChangeListener());
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventRate(ChangeTuNumEvent event) {
        if (event != null && event.getState() == 2) {
            String type = event.getType();
            int num = event.getNum();
            if (type.equals("first")) {
                if (num != 0) {
                    tvDai.setText("待接诊(" + num + ")");
                } else {
                    tvDai.setText("待接诊");
                }
            } else if (type.equals("second")) {
                if (num != 0) {
                    tvIng.setText("咨询中(" + num + ")");
                } else {
                    tvIng.setText("咨询中");
                }
            } else if (type.equals("third")) {
                if (num != 0) {
                    tvEnd.setText("已结束(" + num + ")");
                } else {
                    tvEnd.setText("已结束");
                }
            }
        }
    }
    @Override
    public void showLoading() {
        if (mLoadingDialog != null) {
            hideLoading();
        }
        mLoadingDialog = LoadingDialog.show(VideoDiagnoseActivity.this, "");
    }

    @Override
    public void hideLoading() {
        if (mLoadingDialog != null && mLoadingDialog.isShowing()) {
            mLoadingDialog.dismiss();
            mLoadingDialog = null;
        }
    }

    public class MyVideoOnPageChangeListener implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            Log.i("hwt", "选中的是" + position);
            switch (position) {
                case 0:
                    tvDai.setTextColor(getResources().getColor(R.color.orange));
                    tvIng.setTextColor(getResources().getColor(R.color.yiJiuBlack));
                    tvEnd.setTextColor(getResources().getColor(R.color.yiJiuBlack));
                    break;
                case 1:
                    tvDai.setTextColor(getResources().getColor(R.color.yiJiuBlack));
                    tvIng.setTextColor(getResources().getColor(R.color.orange));
                    tvEnd.setTextColor(getResources().getColor(R.color.yiJiuBlack));
                    break;
                case 2:
                    tvDai.setTextColor(getResources().getColor(R.color.yiJiuBlack));
                    tvIng.setTextColor(getResources().getColor(R.color.yiJiuBlack));
                    tvEnd.setTextColor(getResources().getColor(R.color.orange));
                    break;
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_dai:
                tvDai.setTextColor(getResources().getColor(R.color.orange));
                tvIng.setTextColor(getResources().getColor(R.color.yiJiuBlack));
                tvEnd.setTextColor(getResources().getColor(R.color.yiJiuBlack));
                viewpager.setCurrentItem(0);
                break;

            case R.id.tv_ing:
                tvDai.setTextColor(getResources().getColor(R.color.yiJiuBlack));
                tvIng.setTextColor(getResources().getColor(R.color.orange));
                tvEnd.setTextColor(getResources().getColor(R.color.yiJiuBlack));
                viewpager.setCurrentItem(1);
                break;

            case R.id.tv_end:
                tvDai.setTextColor(getResources().getColor(R.color.yiJiuBlack));
                tvIng.setTextColor(getResources().getColor(R.color.yiJiuBlack));
                tvEnd.setTextColor(getResources().getColor(R.color.orange));
                viewpager.setCurrentItem(2);
                break;
        }
    }

    @Override
    public VideoDiagnoseComponent getComponent() {
        return mComponent;
    }

    @Override
    public void onError(Throwable throwable) {
        loadError(throwable);
    }

    @Override
    public void setDiagnoseCount(DiagnoseCountBean beans) {
        if (beans.getWaitList() != 0) {
            tvDai.setText("待接诊(" + beans.getWaitList() + ")");
        }

        if (beans.getIngList() != 0) {
            tvIng.setText("咨询中(" + beans.getIngList() + ")");
        }

        if (beans.getEndList() != 0) {
            tvEnd.setText("已结束(" + beans.getEndList() + ")");
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

}
