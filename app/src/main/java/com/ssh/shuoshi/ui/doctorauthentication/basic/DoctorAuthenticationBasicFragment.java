package com.ssh.shuoshi.ui.doctorauthentication.basic;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.ssh.shuoshi.R;
import com.ssh.shuoshi.bean.AreaBean;
import com.ssh.shuoshi.bean.HisDoctorBean;
import com.ssh.shuoshi.bean.pickview.SysDeptNameBean;
import com.ssh.shuoshi.bean.pickview.SysTitleNameBean;
import com.ssh.shuoshi.components.storage.UserStorage;
import com.ssh.shuoshi.eventbus.ChangeEvent;
import com.ssh.shuoshi.library.util.ToastUtil;
import com.ssh.shuoshi.ui.BaseFragment;
import com.ssh.shuoshi.ui.authority.goodat.AuthorityGoodAtActivity;
import com.ssh.shuoshi.ui.authority.info.AuthorityInfoActivity;
import com.ssh.shuoshi.ui.dialog.LoadingDialog;
import com.ssh.shuoshi.ui.doctorauthentication.DoctorAuthenticationComponent;
import com.ssh.shuoshi.view.pickview.builder.OptionsPickerBuilder;
import com.ssh.shuoshi.view.pickview.view.OptionsPickerView;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import butterknife.BindView;

public class DoctorAuthenticationBasicFragment extends BaseFragment implements View.OnClickListener, DoctorAuthenticationBasicContract.View {

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
    @BindView(R.id.buttonSave)
    Button buttonSave;

    private List<SysDeptNameBean.RowsBean> sysDeptRows;        //平台科室
    private List<SysTitleNameBean.RowsBean> doctorTitleRows;    //医生职务
    private int mSysDept = -1;  //科室ID
    private int mDoctorTitle = -1;//医生职务ID
    private int mCityId = -1;   //城市ID
    private LoadingDialog mLoadingDialog;

    @Inject
    DoctorAuthenticationBasicPresenter mPresenter;
    @Inject
    UserStorage mUserStorage;
    private List<AreaBean> mBean;
    ArrayList<List<String>> mCityBean = new ArrayList<>();
    private OnButtonClick onButtonClick;
    private HisDoctorBean doctorInfo;


    @Override
    public void initInjector() {
        getComponent(DoctorAuthenticationComponent.class).inject(this);
    }

    @Override
    public int initContentView() {
        return R.layout.fragment_doctor_authentication_basic;
    }

    @Override
    public void getBundle(Bundle bundle) {

    }

    @Override
    public void initUI(View view) {
        mPresenter.attachView(this);

        //获取科室
        mPresenter.getSysDept();
        //查询医生职务列表
        mPresenter.getDoctorTitleDict();
        //查询城市信息列表
        mPresenter.getProvinceDict();

        doctorInfo = mUserStorage.getDoctorInfo();
        etName.setText(doctorInfo.getName());
        tvAddress.setText(doctorInfo.getAreaCNNmae());
        etHospital.setText(doctorInfo.getHospitalName());
        tvOffice.setText(doctorInfo.getHisSysDeptName());
        tvJob.setText(doctorInfo.getDoctorTitleName());
        tvInfo.setText(doctorInfo.getBriefIntroduction());
        tvGood.setText(doctorInfo.getGoodAt());

        initData();
        rlAddress.setOnClickListener(this);
        rlOffice.setOnClickListener(this);
        rlJob.setOnClickListener(this);
        rlInfo.setOnClickListener(this);
        rlGood.setOnClickListener(this);
        buttonSave.setOnClickListener(this);
    }

    @Override
    public void initData() {
        etName.addTextChangedListener(new ChangeTextWatcher());
        etHospital.addTextChangedListener(new ChangeTextWatcher());
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
                Intent intent1 = new Intent(getActivity(), AuthorityInfoActivity.class);
                intent1.putExtra("info", tvInfo.getText().toString());
                intent1.putExtra("type", "2");
                startActivityForResult(intent1, 100);
                break;
            case R.id.rl_good:
                Intent intent2 = new Intent(getActivity(), AuthorityGoodAtActivity.class);
                intent2.putExtra("info", tvGood.getText().toString());
                intent2.putExtra("type", "2");
                startActivityForResult(intent2, 101);
                break;
            case R.id.buttonSave:
                check();
                break;
        }
    }

    //选择职称
    private void showTitleNameDialog() {
        OptionsPickerView pvOptions = new OptionsPickerBuilder(getActivity(), (options1, options2, options3, v) -> {
            String titleName = doctorTitleRows.get(options1).getTitleName();
            tvJob.setText(titleName);
            mDoctorTitle = doctorTitleRows.get(options1).getId();
        })
                .setTitleText("请选择您的职称")
                .setCancelText(" ")
                .setDividerColor(Color.BLACK)
                .setContentTextSize(15)
                .build();
        pvOptions.setPicker(doctorTitleRows);
        pvOptions.show();
    }

    //平台科室
    private void showDeptNameDialog() {
        OptionsPickerView pvOptions = new OptionsPickerBuilder(getActivity(), (options1, options2, options3, v) -> {
            String deptName = sysDeptRows.get(options1).getDeptName();
            tvOffice.setText(deptName);
            mSysDept = sysDeptRows.get(options1).getId();
        })
                .setTitleText("请选择平台科室")
                .setCancelText(" ")
                .setDividerColor(Color.BLACK)
                .setContentTextSize(15)
                .build();
        pvOptions.setPicker(sysDeptRows);
        pvOptions.show();
    }

    //定义接口变量的set方法
    public void setOnButtonClick(OnButtonClick onButtonClick) {
        this.onButtonClick = onButtonClick;
    }

    //1、定义接口
    public interface OnButtonClick {
        void onClick(Map<String, Object> mData);
    }

    private void check() {
        String name = etName.getText().toString().trim();
        String hospital = etHospital.getText().toString().trim();

        if (TextUtils.isEmpty(name)) {
            ToastUtil.showToast("姓名不能为空");
            return;
        }

        if (TextUtils.isEmpty(hospital)) {
            ToastUtil.showToast("医院不能为空");
            return;
        }

        Map<String, Object> mData = new HashMap<>();
        if (!name.equals(doctorInfo.getName())) {
            mData.put("name", name);
        }
        if (mCityId != -1) {
            mData.put("areaId", mCityId);
        }
        if (!hospital.equals(doctorInfo.getHospitalName())) {
            mData.put("hospitalName", hospital);
        }
        if (mSysDept != -1) {
            mData.put("deptId", mSysDept);
        }
        if (mDoctorTitle != -1) {
            mData.put("titleId", mDoctorTitle);
        }
        if (onButtonClick != null) {
            onButtonClick.onClick(mData);
        }
    }

    private void showAddressDialog() {
        OptionsPickerView pvOptions = new OptionsPickerBuilder(getActivity(), (options1, options2, options3, v) -> {
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
                .setContentTextSize(15)
                .build();
        pvOptions.setPicker(mBean, mCityBean);
        pvOptions.show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
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
            }
        }
    }

    @Override
    public void showLoading() {
        if (mLoadingDialog != null) {
            hideLoading();
        }
        mLoadingDialog = LoadingDialog.show(getActivity(), "");
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

    @Override
    public void uploadInfoSuccess(String bean) {
        ToastUtil.showToast("保存成功");
        EventBus.getDefault().post(new ChangeEvent(1));
//        getActivity().finish();
    }


    private class ChangeTextWatcher implements TextWatcher {

        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        @Override
        //主要是重置文本改变事件,判断当前输入的内容
        public void afterTextChanged(Editable edt) {
            buttonSave.setEnabled(true);
        }
    }
}
