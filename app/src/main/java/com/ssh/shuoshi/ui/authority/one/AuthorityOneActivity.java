package com.ssh.shuoshi.ui.authority.one;

import android.content.Intent;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.gyf.barlibrary.ImmersionBar;
import com.ssh.shuoshi.R;
import com.ssh.shuoshi.bean.AreaBean;
import com.ssh.shuoshi.bean.HisDoctorBean;
import com.ssh.shuoshi.bean.pickview.SysDeptNameBean;
import com.ssh.shuoshi.bean.pickview.SysTitleNameBean;
import com.ssh.shuoshi.components.storage.UserStorage;
import com.ssh.shuoshi.library.util.ToastUtil;
import com.ssh.shuoshi.ui.BaseActivity;
import com.ssh.shuoshi.ui.authority.goodat.AuthorityGoodAtActivity;
import com.ssh.shuoshi.ui.authority.info.AuthorityInfoActivity;
import com.ssh.shuoshi.ui.authority.two.AuthorityTwoActivity;
import com.ssh.shuoshi.ui.dialog.LoadingDialog;
import com.ssh.shuoshi.view.pickview.builder.OptionsPickerBuilder;
import com.ssh.shuoshi.view.pickview.view.OptionsPickerView;
import com.ssh.shuoshi.view.title.UniteTitle;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;

/**
 * created by hwt on 2020/12/10
 */
public class AuthorityOneActivity extends BaseActivity implements AuthorityOneContract.View, View.OnClickListener {

    @Inject
    AuthorityOnePresent mPresenter;
    @BindView(R.id.title)
    UniteTitle title;
    @BindView(R.id.image1)
    ImageView image1;
    @BindView(R.id.image2)
    ImageView image2;
    @BindView(R.id.image3)
    ImageView image3;
    @BindView(R.id.et_name)
    EditText etName;
    @BindView(R.id.tv_address)
    TextView tvAddress;
    @BindView(R.id.rl_address)
    RelativeLayout rlAddress;
    @BindView(R.id.et_hospital)
    EditText etHospital;
    @BindView(R.id.tv_office)
    TextView tvOffice;
    @BindView(R.id.rl_office)
    RelativeLayout rlOffice;
    @BindView(R.id.tv_job)
    TextView tvJob;
    @BindView(R.id.rl_job)
    RelativeLayout rlJob;
    @BindView(R.id.tv_info)
    TextView tvInfo;
    @BindView(R.id.rl_info)
    RelativeLayout rlInfo;
    @BindView(R.id.tv_good)
    TextView tvGood;
    @BindView(R.id.rl_good)
    RelativeLayout rlGood;
    @BindView(R.id.btn_next)
    Button btnNext;

    @Inject
    UserStorage mUserStorage;

    private LoadingDialog mLoadingDialog;
    private List<SysDeptNameBean.RowsBean> sysDeptRows;        //平台科室
    private List<SysTitleNameBean.RowsBean> doctorTitleRows;    //医生职务
    private int mSysDept = -1;  //科室ID
    private int mDoctorTitle = -1;//医生职务ID
    private int mCityId = -1;   //城市ID
    private List<AreaBean> mBean;
    ArrayList<List<String>> mCityBean = new ArrayList<>();

    private HisDoctorBean doctorInfo;
    private boolean isShow;

    @Override
    public int initContentView() {
        return R.layout.activity_authority_one;
    }

    @Override
    protected void initInjector() {
        DaggerAuthorityOneComponent.builder()
                .applicationComponent(getApplicationComponent())
                .activityModule(getActivityModule())
                .build()
                .inject(this);
    }

    @Override
    public void setStatusBar() {
        ImmersionBar.with(this).statusBarColor(R.color.white)
                .statusBarDarkFont(true)
                .fitsSystemWindows(true).init();
    }

    @Override
    protected void initUiAndListener() {
        mPresenter.attachView(this);
        title.setBackListener(-1, v -> finish());

        //获取科室
        mPresenter.getSysDept();
        //查询医生职务列表
        mPresenter.getDoctorTitleDict();
        //查询城市信息列表
        mPresenter.getProvinceDict();

        isShow = getIntent().getBooleanExtra("isShow", false);

        rlAddress.setOnClickListener(this);
        rlOffice.setOnClickListener(this);
        rlJob.setOnClickListener(this);
        rlInfo.setOnClickListener(this);
        rlGood.setOnClickListener(this);
        btnNext.setOnClickListener(this);

        if (isShow) {
            doctorInfo = mUserStorage.getDoctorInfo();
            etName.setText(doctorInfo.getName());
            tvAddress.setText(doctorInfo.getAreaCNNmae());
            etHospital.setText(doctorInfo.getHospitalName());
            tvOffice.setText(doctorInfo.getHisSysDeptName());
            tvJob.setText(doctorInfo.getDoctorTitleName());
            tvInfo.setText(doctorInfo.getBriefIntroduction());
            tvGood.setText(doctorInfo.getGoodAt());

            mCityId = doctorInfo.getAreaId();
            mSysDept = doctorInfo.getDeptId();
            mDoctorTitle = doctorInfo.getTitleId();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_address:
                if (mBean == null) {
                    ToastUtil.showToast("所在地暂时没有信息");
                    return;
                }
                showAddressDialog();
                break;
            case R.id.rl_office:
                if (sysDeptRows == null) {
                    ToastUtil.showToast("平台科室暂时没有信息");
                    return;
                }

                showDeptNameDialog();
                break;
            case R.id.rl_job:
                if (doctorTitleRows == null) {
                    ToastUtil.showToast("医生职称暂时没有信息");
                    return;
                }

                showTitleNameDialog();
                break;
            case R.id.rl_info:
                Intent intent1 = new Intent(AuthorityOneActivity.this, AuthorityInfoActivity.class);
                intent1.putExtra("type", "1");
                intent1.putExtra("info", tvInfo.getText().toString());
                startActivityForResult(intent1, 100);
                break;
            case R.id.rl_good:
                Intent intent2 = new Intent(AuthorityOneActivity.this, AuthorityGoodAtActivity.class);
                intent2.putExtra("type", "1");
                intent2.putExtra("info", tvGood.getText().toString());
                startActivityForResult(intent2, 101);
                break;
            case R.id.btn_next:
                check();
                break;
        }
    }

    //选择职称
    private void showTitleNameDialog() {
        OptionsPickerView pvOptions = new OptionsPickerBuilder(this, (options1, options2, options3, v) -> {
            String titleName = doctorTitleRows.get(options1).getTitleName();
            tvJob.setText(titleName);
            mDoctorTitle = doctorTitleRows.get(options1).getId();
        })
                .setTitleText("请选择您的职称")
                .setCancelText(" ")
                .setDividerColor(Color.BLACK)
                .setContentTextSize(20)
                .build();
        pvOptions.setPicker(doctorTitleRows);
        pvOptions.show();
    }

    //平台科室
    private void showDeptNameDialog() {
        OptionsPickerView pvOptions = new OptionsPickerBuilder(this, (options1, options2, options3, v) -> {
            String deptName = sysDeptRows.get(options1).getDeptName();
            tvOffice.setText(deptName);
            mSysDept = sysDeptRows.get(options1).getId();
        })
                .setTitleText("请选择平台科室")
                .setCancelText(" ")
                .setDividerColor(Color.BLACK)
                .setContentTextSize(20)
                .build();
        pvOptions.setPicker(sysDeptRows);
        pvOptions.show();
    }

    private void showAddressDialog() {
        OptionsPickerView pvOptions = new OptionsPickerBuilder(this, (options1, options2, options3, v) -> {
            //返回的分别是三个级别的选中位置
            String opt1tx = mBean.size() > 0 ?
                    mBean.get(options1).getPickerViewText() : "";
            String opt2tx = mCityBean.size() > 0
                    && mCityBean.get(options1).size() > 0 ?
                    mCityBean.get(options1).get(options2) : "";
            String address = opt1tx + opt2tx;
            mCityId = mBean.get(options1).getCity().get(options2).getId();
            tvAddress.setText(address);
        })
                .setTitleText("请选择省市")
                .setCancelText(" ")
                .setDividerColor(Color.BLACK)
                .setContentTextSize(20)
                .build();
        pvOptions.setPicker(mBean, mCityBean);
        pvOptions.show();
    }


    private void check() {
        String name = etName.getText().toString().trim();
        String address = tvAddress.getText().toString();
        String hospital = etHospital.getText().toString().trim();
        String office = tvOffice.getText().toString();
        String job = tvJob.getText().toString();
        String info = tvInfo.getText().toString();
        String good = tvGood.getText().toString();

        if (TextUtils.isEmpty(name)) {
            ToastUtil.showToast("姓名不能为空");
            return;
        }

        if (TextUtils.isEmpty(address)) {
            ToastUtil.showToast("所在地不能为空");
            return;
        }

        if (TextUtils.isEmpty(hospital)) {
            ToastUtil.showToast("医院不能为空");
            return;
        }

        if (TextUtils.isEmpty(office)) {
            ToastUtil.showToast("科室不能为空");
            return;
        }

        if (TextUtils.isEmpty(job)) {
            ToastUtil.showToast("职称不能为空");
            return;
        }

        if (TextUtils.isEmpty(info)) {
            ToastUtil.showToast("个人简介不能为空");
            return;
        }

        if (TextUtils.isEmpty(good)) {
            ToastUtil.showToast("擅长领域不能为空");
            return;
        }

        Intent intent = new Intent(AuthorityOneActivity.this, AuthorityTwoActivity.class);
        intent.putExtra("name", name);
        intent.putExtra("areaId", mCityId);
        intent.putExtra("hospitalName", hospital);
        intent.putExtra("deptId", mSysDept);
        intent.putExtra("titleId", mDoctorTitle);
        intent.putExtra("briefIntroduction", info);
        intent.putExtra("goodAt", good);
        intent.putExtra("isShow", isShow);
        startActivityForResult(intent, 102);
        overridePendingTransition(0, 0);
    }

    @Override
    public void showLoading() {
        if (mLoadingDialog != null) {
            hideLoading();
        }
        mLoadingDialog = LoadingDialog.show(AuthorityOneActivity.this, "");
    }

    @Override
    public void hideLoading() {
        if (mLoadingDialog != null && mLoadingDialog.isShowing()) {
            mLoadingDialog.dismiss();
            mLoadingDialog = null;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case 100:
                    String info = data.getStringExtra("info");
                    tvInfo.setText(info);
                    break;
                case 101:
                    String good = data.getStringExtra("good");
                    tvGood.setText(good);
                    break;
                case 102:
                    finish();
                    break;
            }
        }
    }

    @Override
    public void onError(Throwable throwable) {
        loadError(throwable);
    }

    @Override
    public void getSysDeptSuccess(SysDeptNameBean bean) {
        sysDeptRows = bean.getRows();

    }

    @Override
    public void getDoctorTitleDictSuccess(SysTitleNameBean bean) {
        doctorTitleRows = bean.getRows();
    }

    @Override
    public void getAddressSuccess(List<AreaBean> bean) {
        this.mBean = bean;
        for (int i = 0; i < mBean.size(); i++) {     //遍历省份
            List<String> cityList = new ArrayList<>();
            for (int j = 0; j < mBean.get(i).getCity().size(); j++) {
                mBean.get(i).getCity().get(j).getCityNameZh();
                cityList.add(mBean.get(i).getCity().get(j).getCityNameZh());
            }
            mCityBean.add(cityList);
        }
    }
}
