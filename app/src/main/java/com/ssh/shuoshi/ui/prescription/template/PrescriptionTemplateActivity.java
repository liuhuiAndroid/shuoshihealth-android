package com.ssh.shuoshi.ui.prescription.template;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.viewpager.widget.ViewPager;

import com.ssh.shuoshi.R;
import com.ssh.shuoshi.bean.HisDoctorBean;
import com.ssh.shuoshi.components.storage.UserStorage;
import com.ssh.shuoshi.injector.HasComponent;
import com.ssh.shuoshi.ui.BaseActivity;
import com.ssh.shuoshi.ui.BaseFragment;
import com.ssh.shuoshi.ui.adapter.ImagePageAdapter;
import com.ssh.shuoshi.ui.prescription.template.commonlyprescriptionchinese.CommonlyPrescriptionChineseFragment;
import com.ssh.shuoshi.ui.prescription.template.commonlyprescriptionwestern.CommonlyPrescriptionWesternFragment;
import com.ssh.shuoshi.ui.prescription.template.commonlywesternmedicine.CommonlyWesternMedicineFragment;
import com.ssh.shuoshi.view.title.UniteTitle;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 处方模版
 */
public class PrescriptionTemplateActivity extends BaseActivity implements View.OnClickListener,
        HasComponent<PrescriptionTemplateComponent>, PrescriptionTemplateContract.View {

    @BindView(R.id.uniteTitle)
    UniteTitle uniteTitle;
    @BindView(R.id.viewPager)
    ViewPager viewPager;
    @BindView(R.id.tvTab01)
    TextView tvTab01;
    @BindView(R.id.tvTab02)
    TextView tvTab02;
    @BindView(R.id.tvTab03)
    TextView tvTab03;

    @Inject
    PrescriptionTemplatePresenter mPresenter;

    @Inject
    UserStorage mUserStorage;
    @BindView(R.id.view3)
    View view3;

    private ImagePageAdapter mAdapter;

    private PrescriptionTemplateComponent mComponent;

    @Override
    public int initContentView() {
        return R.layout.activity_prescription_template;
    }

    @Override
    protected void initInjector() {
        mComponent = DaggerPrescriptionTemplateComponent.builder()
                .applicationComponent(getApplicationComponent())
                .activityModule(getActivityModule())
                .prescriptionTemplateMoudle(new PrescriptionTemplateMoudle(this))
                .build();
        mComponent.inject(this);
    }

    @Override
    public PrescriptionTemplateComponent getComponent() {
        return mComponent;
    }

    @Override
    protected void initUiAndListener() {
        mPresenter.attachView(this);

        HisDoctorBean doctorInfo = mUserStorage.getDoctorInfo();
        Integer deptTypeId = doctorInfo.getDeptTypeId();

        uniteTitle.setBackListener(-1, v -> finish());
        tvTab01.setOnClickListener(this);
        tvTab02.setOnClickListener(this);
        tvTab03.setOnClickListener(this);

        List<BaseFragment> fragments = new ArrayList<>();
        fragments.add(new CommonlyWesternMedicineFragment());
        fragments.add(new CommonlyPrescriptionWesternFragment());
        if (deptTypeId != null) {
            if (deptTypeId == 1) {
                view3.setVisibility(View.GONE);
                tvTab03.setVisibility(View.GONE);
            } else {
                view3.setVisibility(View.VISIBLE);
                tvTab03.setVisibility(View.VISIBLE);
                fragments.add(new CommonlyPrescriptionChineseFragment());
            }
        }

        mAdapter = new ImagePageAdapter(getSupportFragmentManager(), fragments);
        viewPager.setAdapter(mAdapter);
        viewPager.setOffscreenPageLimit(3);
        viewPager.addOnPageChangeListener(new MyOnPageChangeListener());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvTab01:
                tvTab01.setTextColor(getResources().getColor(R.color.orange));
                tvTab02.setTextColor(getResources().getColor(R.color.yiJiuBlack));
                tvTab03.setTextColor(getResources().getColor(R.color.yiJiuBlack));
                viewPager.setCurrentItem(0);
                break;

            case R.id.tvTab02:
                tvTab01.setTextColor(getResources().getColor(R.color.yiJiuBlack));
                tvTab02.setTextColor(getResources().getColor(R.color.orange));
                tvTab03.setTextColor(getResources().getColor(R.color.yiJiuBlack));
                viewPager.setCurrentItem(1);
                break;

            case R.id.tvTab03:
                tvTab01.setTextColor(getResources().getColor(R.color.yiJiuBlack));
                tvTab02.setTextColor(getResources().getColor(R.color.yiJiuBlack));
                tvTab03.setTextColor(getResources().getColor(R.color.orange));
                viewPager.setCurrentItem(2);
                break;
        }
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void onError(Throwable throwable) {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }


    public class MyOnPageChangeListener implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            switch (position) {
                case 0:
                    tvTab01.setTextColor(getResources().getColor(R.color.orange));
                    tvTab02.setTextColor(getResources().getColor(R.color.yiJiuBlack));
                    tvTab03.setTextColor(getResources().getColor(R.color.yiJiuBlack));
                    break;
                case 1:
                    tvTab01.setTextColor(getResources().getColor(R.color.yiJiuBlack));
                    tvTab02.setTextColor(getResources().getColor(R.color.orange));
                    tvTab03.setTextColor(getResources().getColor(R.color.yiJiuBlack));
                    break;
                case 2:
                    tvTab01.setTextColor(getResources().getColor(R.color.yiJiuBlack));
                    tvTab02.setTextColor(getResources().getColor(R.color.yiJiuBlack));
                    tvTab03.setTextColor(getResources().getColor(R.color.orange));
                    break;
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }
}
