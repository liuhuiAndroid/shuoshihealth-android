package com.ssh.shuoshi.ui.doctorauthentication;

import android.content.Intent;
import android.graphics.Color;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;

import com.ssh.shuoshi.R;
import com.ssh.shuoshi.eventbus.ChangeEvent;
import com.ssh.shuoshi.injector.HasComponent;
import com.ssh.shuoshi.ui.BaseActivity;
import com.ssh.shuoshi.ui.BaseFragment;
import com.ssh.shuoshi.ui.adapter.ImagePageAdapter;
import com.ssh.shuoshi.ui.doctorauthentication.auth.DoctorAuthenticationAuthFragment;
import com.ssh.shuoshi.ui.doctorauthentication.basic.DoctorAuthenticationBasicFragment;
import com.ssh.shuoshi.view.title.UniteTitle;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import butterknife.BindView;

/**
 * 医生认证
 */
public class DoctorAuthenticationActivity extends BaseActivity implements View.OnClickListener,
        HasComponent<DoctorAuthenticationComponent>, DoctorAuthenticationContract.View,
        DoctorAuthenticationBasicFragment.OnButtonClick {

    @BindView(R.id.uniteTitle)
    UniteTitle uniteTitle;
    @BindView(R.id.basicInfo)
    TextView basicInfo;
    @BindView(R.id.authorizationInfo)
    TextView authorizationInfo;
    @BindView(R.id.viewpager)
    ViewPager viewpager;

    @Inject
    DoctorAuthenticationPresenter mPresenter;

    private DoctorAuthenticationComponent mComponent;
    private ImagePageAdapter mAdapter;
    private List<BaseFragment> fragments = new ArrayList<>();
    private DoctorAuthenticationAuthFragment authFragment;

    @Override
    public int initContentView() {
        return R.layout.activity_doctor_authentication;
    }

    @Override
    protected void initInjector() {
        mComponent = DaggerDoctorAuthenticationComponent.builder()
                .applicationComponent(getApplicationComponent())
                .activityModule(getActivityModule())
                .doctorAuthenticationMoudle(new DoctorAuthenticationMoudle(this))
                .build();
        mComponent.inject(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventRate(ChangeEvent event) {
        if (event != null) {
            int type = event.getChangeType();
            if (type == 1) {
                mPresenter.getDoctorInfo();
            }
        }
    }

    @Override
    protected void initUiAndListener() {
        mPresenter.attachView(this);
        uniteTitle.setBackListener(-1, v -> finish());
        basicInfo.setOnClickListener(this);
        authorizationInfo.setOnClickListener(this);
        DoctorAuthenticationBasicFragment basicFragment = new DoctorAuthenticationBasicFragment();
        authFragment = new DoctorAuthenticationAuthFragment();
        fragments.add(basicFragment);
        fragments.add(authFragment);
        mAdapter = new ImagePageAdapter(getSupportFragmentManager(), fragments);
        viewpager.setAdapter(mAdapter);
        viewpager.setOffscreenPageLimit(2);
        viewpager.addOnPageChangeListener(new MyOnPageChangeListener());

        basicFragment.setOnButtonClick(this);
    }

    @Override
    public DoctorAuthenticationComponent getComponent() {
        return mComponent;
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void onError(Throwable throwable) {
        loadError(throwable);
    }

    @Override
    public void onClick(Map<String, Object> map) {
        basicInfo.setTextColor(Color.parseColor("#191919"));
        authorizationInfo.setTextColor(Color.parseColor("#FF824D"));
        viewpager.setCurrentItem(1);
        authFragment.setData(map);
    }

    public class MyOnPageChangeListener implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            switch (position) {
                case 0:
                    basicInfo.setTextColor(Color.parseColor("#FF824D"));
                    authorizationInfo.setTextColor(Color.parseColor("#191919"));
                    break;
                case 1:
                    basicInfo.setTextColor(Color.parseColor("#191919"));
                    authorizationInfo.setTextColor(Color.parseColor("#FF824D"));
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
            case R.id.basicInfo:
                basicInfo.setTextColor(Color.parseColor("#FF824D"));
                authorizationInfo.setTextColor(Color.parseColor("#191919"));
                viewpager.setCurrentItem(0);
                break;
            case R.id.authorizationInfo:
                basicInfo.setTextColor(Color.parseColor("#191919"));
                authorizationInfo.setTextColor(Color.parseColor("#FF824D"));
                viewpager.setCurrentItem(1);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (!fragments.isEmpty()) {
            for (int i = 0; i < fragments.size(); i++) {
                fragments.get(i).onActivityResult(requestCode, resultCode, data);
            }
        }
    }
}
