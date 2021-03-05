package com.ssh.shuoshi.ui.team.doctorteam;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.switchmaterial.SwitchMaterial;
import com.ssh.shuoshi.R;
import com.ssh.shuoshi.bean.HisDoctorBean;
import com.ssh.shuoshi.bean.team.DoctorListBean;
import com.ssh.shuoshi.bean.team.DoctorTeamDetailBean;
import com.ssh.shuoshi.bean.team.HisDoctorExpertTeamMemberDtosBean;
import com.ssh.shuoshi.components.storage.UserStorage;
import com.ssh.shuoshi.library.adapter.CommonAdapter;
import com.ssh.shuoshi.library.adapter.base.ViewHolder;
import com.ssh.shuoshi.library.util.ToastUtil;
import com.ssh.shuoshi.ui.BaseActivity;
import com.ssh.shuoshi.ui.dialog.CommonDialog;
import com.ssh.shuoshi.ui.dialog.LoadingDialog;
import com.ssh.shuoshi.ui.dialog.TuServiceDialog;
import com.ssh.shuoshi.ui.team.addF.DoctorAddFActivity;
import com.ssh.shuoshi.ui.team.introduce.TeamIntroduceActivity;
import com.ssh.shuoshi.util.SoftKeyboardUtil;
import com.ssh.shuoshi.view.title.UniteTitle;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 我的专家团队
 * created by hwt on 2021/1/3
 */
public class MyDoctorTeamActivity extends BaseActivity implements MyDoctorTeamContract.View, View.OnClickListener {

    @Inject
    MyDoctorTeamPresenter mPresenter;
    @BindView(R.id.title)
    UniteTitle title;
    @BindView(R.id.ImageAvatar)
    ImageView ImageAvatar;
    @BindView(R.id.textTeamName)
    TextView textTeamName;
    @BindView(R.id.textInvite)
    TextView textInvite;
    @BindView(R.id.text1)
    TextView text1;
    @BindView(R.id.textEdit)
    TextView textEdit;
    @BindView(R.id.textTeamIntroduce)
    TextView textTeamIntroduce;
    @BindView(R.id.text2)
    TextView text2;
    @BindView(R.id.tv_tu)
    TextView tvTu;
    @SuppressLint("UseSwitchCompatOrMaterialCode")
    @BindView(R.id.switch_tu)
    Switch switchTu;
    @BindView(R.id.view1)
    View view1;
    @BindView(R.id.tv_price_des)
    TextView tvPriceDes;
    @BindView(R.id.textPrice)
    TextView textPrice;
    @BindView(R.id.text3)
    TextView text3;
    @BindView(R.id.ImageDoctorAvatar)
    ImageView ImageDoctorAvatar;
    @BindView(R.id.textDoctorName)
    TextView textDoctorName;
    @BindView(R.id.textOffice)
    TextView textOffice;
    @BindView(R.id.textJob)
    TextView textJob;
    @BindView(R.id.textHospital)
    TextView textHospital;
    @BindView(R.id.view2)
    View view2;
    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.textExit)
    TextView textExit;
    @BindView(R.id.textTeamCount)
    TextView textTeamCount;
    @BindView(R.id.llExit)
    LinearLayout llExit;

    @Inject
    UserStorage mUserStorage;

    private LoadingDialog mLoadingDialog;
    private int teamId;             //专家团队ID
    private CommonAdapter mCommonAdapter;
    private List<HisDoctorExpertTeamMemberDtosBean> list = new ArrayList<>();
    private DoctorTeamDetailBean mBean;     //专家详情全部信息
    private HisDoctorBean doctorInfo;

    private boolean isLead = false;
    private List<HisDoctorExpertTeamMemberDtosBean> dtos;

    @Override
    public int initContentView() {
        return R.layout.activity_my_doctor_team;
    }

    @Override
    protected void initInjector() {
        DaggerMyDoctorTeamComponent.builder()
                .applicationComponent(getApplicationComponent())
                .activityModule(getActivityModule())
                .build()
                .inject(this);
    }

    @Override
    protected void initUiAndListener() {
        mPresenter.attachView(this);
        doctorInfo = mUserStorage.getDoctorInfo();
        title.setBackListener(-1, v -> finish());
        textPrice.setOnClickListener(this);
        textEdit.setOnClickListener(this);
        textExit.setOnClickListener(this);
        textInvite.setOnClickListener(this);
        teamId = getIntent().getIntExtra("id", -1);
        initAdapter();
        mPresenter.getDoctorTeamDetail(teamId);

        switchTu.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (!buttonView.isPressed()) {
                return;
            }

            if (TextUtils.isEmpty(textPrice.getText().toString().trim())) {
                ToastUtil.showToast("请先设置问诊价格");
                switchTu.setChecked(false);
                return;
            }

            Map<String, Object> map = new HashMap<>();
            map.put("id", mBean.getId());
            map.put("enableFlag", isChecked ? 1 : 0);
            mPresenter.putDoctorTeam(map);
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.textEdit:
                if (!isLead) {
                    return;
                }
                Intent intent = new Intent(MyDoctorTeamActivity.this, TeamIntroduceActivity.class);
                intent.putExtra("introduction", textTeamIntroduce.getText().toString());
                intent.putExtra("id", mBean.getId());
                intent.putExtra("isUpdate", true);
                startActivityForResult(intent, 200);
                break;

            //邀请医生
            case R.id.textInvite:
                if (list.size() == 10) {
                    ToastUtil.showToast("团队成员已满");
                    return;
                }
                Intent addIntent = new Intent(MyDoctorTeamActivity.this, DoctorAddFActivity.class);
                startActivityForResult(addIntent, 201);
                break;

            case R.id.textExit:
                CommonDialog commonDialog = new CommonDialog(MyDoctorTeamActivity.this, "确定退出该专家团队吗？",R.style.dialog_physician_certification);
                commonDialog.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
                commonDialog.getWindow().getDecorView().setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener() {
                    @Override
                    public void onSystemUiVisibilityChange(int visibility) {
                        int uiOptions = View.SYSTEM_UI_FLAG_LAYOUT_STABLE |
                                //布局位于状态栏下方
                                View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION |
                                //全屏
                                View.SYSTEM_UI_FLAG_FULLSCREEN |
                                //隐藏导航栏
                                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION |
                                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN;
                        uiOptions |= 0x00001000;
                        commonDialog.getWindow().getDecorView().setSystemUiVisibility(uiOptions);
                    }
                });
                commonDialog.setOnItemClickListener(new CommonDialog.ItemClickListener() {
                    @Override
                    public void cancel() {
                        commonDialog.dismiss();
                    }

                    @Override
                    public void save() {
                        mPresenter.exitDoctorTeam(teamId);
                        commonDialog.dismiss();
                    }
                });
                commonDialog.show();
                break;

            case R.id.textPrice:
                if (!isLead) {
                    return;
                }

                TuServiceDialog tuServiceDialog = new TuServiceDialog(MyDoctorTeamActivity.this, "专家团队问诊价格",R.style.dialog_physician_certification);
                tuServiceDialog.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
                tuServiceDialog.getWindow().getDecorView().setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener() {
                    @Override
                    public void onSystemUiVisibilityChange(int visibility) {
                        int uiOptions = View.SYSTEM_UI_FLAG_LAYOUT_STABLE |
                                //布局位于状态栏下方
                                View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION |
                                //全屏
                                View.SYSTEM_UI_FLAG_FULLSCREEN |
                                //隐藏导航栏
                                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION |
                                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN;
                        uiOptions |= 0x00001000;
                        tuServiceDialog.getWindow().getDecorView().setSystemUiVisibility(uiOptions);
                    }
                });
                tuServiceDialog.setOnItemClickListener(new TuServiceDialog.ItemClickListener() {
                    @Override
                    public void cancel() {
                        tuServiceDialog.dismiss();
                    }

                    @Override
                    public void save(String number) {
                        SoftKeyboardUtil.hideSoftKeyboard(MyDoctorTeamActivity.this);
                        textPrice.setText(number + "元");
                        tuServiceDialog.dismiss();
                        showLoading();
                        Map<String, Object> map = new HashMap<>();
                        map.put("id", mBean.getId());
                        map.put("price", number);
                        mPresenter.putDoctorTeam(map);
                    }
                });
                tuServiceDialog.show();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case 200:
                    mPresenter.getDoctorTeamDetail(teamId);
                    break;

                case 201:
                    DoctorListBean.RowsBean bean = (DoctorListBean.RowsBean) data.getSerializableExtra("bean");
                    for (int i = 0; i < dtos.size(); i++) {
                        if (bean.getId() == dtos.get(i).getDoctorId()) {
                            ToastUtil.showToast("不可重复添加相同成员");
                            return;
                        }
                    }
                    Map<String, Object> map = new HashMap<>();
                    map.put("id", mBean.getId());
                    List<Map<String, Object>> list = new ArrayList<>();
                    Map<String, Object> map2 = new HashMap<>();
                    map2.put("doctorId", bean.getId());
                    list.add(map2);

                    map.put("hisDoctorExpertTeamMemberDtos", list);
                    mPresenter.putDoctorTeam(map);
                    break;
            }
        }
    }

    @SuppressLint("WrongConstant")
    private void initAdapter() {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayout.VERTICAL, false));
        mCommonAdapter = new CommonAdapter<HisDoctorExpertTeamMemberDtosBean>(this, R.layout.item_my_doctor_team, list) {
            @Override
            protected void convert(ViewHolder holder, final HisDoctorExpertTeamMemberDtosBean bean, int pos) {
                TextView textDoctorName = holder.getView(R.id.textDoctorName);
                TextView textOffice = holder.getView(R.id.textOffice);
                TextView textJob = holder.getView(R.id.textJob);
                TextView textHospital = holder.getView(R.id.textHospital);

                textDoctorName.setText(bean.getDoctorName());
                textOffice.setText(bean.getDeptName());
                textJob.setText(bean.getTitleName());
                textHospital.setText(bean.getHospitalName());
            }
        };
        mRecyclerView.setAdapter(mCommonAdapter);
    }

    @Override
    public void getDoctorTeamDetailSuccess(DoctorTeamDetailBean bean) {
        if (bean == null) {
            return;
        }
        list.clear();
        this.mBean = bean;
        textTeamName.setText(bean.getName());
        textTeamIntroduce.setText(bean.getIntroduction());

        if (bean.getEnableFlag() == 0) {
            switchTu.setChecked(false);
        } else {
            switchTu.setChecked(true);
        }

        if (bean.getPrice() != null) {
            String price = bean.getPrice().toString();
            textPrice.setText(price + "元");
        }

        dtos = bean.getHisDoctorExpertTeamMemberDtos();
        if (dtos == null || dtos.size() == 0) {
            return;
        }

        for (int i = 0; i < dtos.size(); i++) {
            if (dtos.get(i).getLevel() == 1) {
                textDoctorName.setText(dtos.get(i).getDoctorName());
                textOffice.setText(dtos.get(i).getDeptName());
                textJob.setText(dtos.get(i).getTitleName());
                textHospital.setText(dtos.get(i).getHospitalName());

                if (dtos.get(i).getDoctorId() == doctorInfo.getId()) {
                    isLead = true;
                    llExit.setVisibility(View.GONE);
                    if (bean.getPrice() != null) {
                        String price = bean.getPrice().toString();
                        if (!TextUtils.isEmpty(price)) {
                            switchTu.setClickable(true);
                        }
                    }
                } else {
                    isLead = false;
                    switchTu.setClickable(false);
                    llExit.setVisibility(View.VISIBLE);
                }
            } else {
                list.add(dtos.get(i));
            }
        }

        textTeamCount.setText("团队其他成员（" + list.size() + "人）");
        mCommonAdapter.setDatas(list);

    }

    @Override
    public void putDoctorTeamSuccess(Integer bean) {
//        ToastUtil.showToast("修改成功");
        mPresenter.getDoctorTeamDetail(teamId);
    }

    @Override
    public void exitDoctorTeamSuccess(Integer bean) {
        ToastUtil.showToast("退出成功");
        setResult(RESULT_OK, getIntent());
        finish();
    }

    @Override
    public void showLoading() {
        if (mLoadingDialog != null) {
            hideLoading();
        }
        mLoadingDialog = LoadingDialog.show(MyDoctorTeamActivity.this, "");
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
        hideLoading();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

}
