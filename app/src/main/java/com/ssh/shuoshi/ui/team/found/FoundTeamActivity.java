package com.ssh.shuoshi.ui.team.found;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.gyf.barlibrary.ImmersionBar;
import com.ssh.shuoshi.R;
import com.ssh.shuoshi.bean.HisDoctorBean;
import com.ssh.shuoshi.bean.team.DoctorListBean;
import com.ssh.shuoshi.components.storage.UserStorage;
import com.ssh.shuoshi.library.util.ToastUtil;
import com.ssh.shuoshi.ui.BaseActivity;
import com.ssh.shuoshi.ui.dialog.LoadingDialog;
import com.ssh.shuoshi.ui.team.addF.DoctorAddFActivity;
import com.ssh.shuoshi.ui.team.introduce.TeamIntroduceActivity;
import com.ssh.shuoshi.view.title.UniteTitle;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import butterknife.BindView;

/**
 * 创建专家团队
 * created by hwt on 2021/1/3
 */
public class FoundTeamActivity extends BaseActivity implements FoundTeamContract.View, View.OnClickListener {

    @Inject
    FoundTeamPresenter mPresenter;
    @BindView(R.id.title)
    UniteTitle title;
    @BindView(R.id.editName)
    EditText editName;
    @BindView(R.id.editIntroduce)
    TextView editIntroduce;
    @BindView(R.id.text3)
    TextView text3;
    @BindView(R.id.ImageDoctorAvatar)
    ImageView ImageDoctorAvatar;
    @BindView(R.id.textName)
    TextView textName;
    @BindView(R.id.text4)
    TextView text4;
    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.btn_save)
    Button btnSave;

    @Inject
    UserStorage mUserStorage;

    private LoadingDialog mLoadingDialog;
    private HisDoctorBean doctorInfo;
    private FoundTeamAdapter foundTeamAdapter;

    private List<DoctorListBean.RowsBean> beans = new ArrayList<>();

    @Override
    public int initContentView() {
        return R.layout.activity_found_team;
    }

    @Override
    protected void initInjector() {
        DaggerFoundTeamComponent.builder()
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
        doctorInfo = mUserStorage.getDoctorInfo();
        textName.setText(doctorInfo.getName());

        editIntroduce.setOnClickListener(this);
        btnSave.setOnClickListener(this);
        initAdapter();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.editIntroduce:
                Intent intent = new Intent(this, TeamIntroduceActivity.class);
                intent.putExtra("introduction", editIntroduce.getText().toString().trim());
                startActivityForResult(intent, 200);
                break;

            case R.id.btn_save:
                String name = editName.getText().toString().trim();
                String introduce = editIntroduce.getText().toString().trim();

                if (TextUtils.isEmpty(name)) {
                    ToastUtil.showToast("请输入团队名称");
                    return;
                }

                if (TextUtils.isEmpty(introduce)) {
                    ToastUtil.showToast("请输入团队介绍");
                    return;
                }

                if (beans.size() == 0) {
                    ToastUtil.showToast("请至少添加一名医生");
                    return;
                }

                Map<String, Object> map = new HashMap<>();
                map.put("name", name);
                map.put("introduction", introduce);
                List<Map<String, Object>> list = new ArrayList<>();
                for (int i = 0; i < beans.size(); i++) {
                    Map<String, Object> map2 = new HashMap<>();
                    map2.put("doctorId", beans.get(i).getId());
                    list.add(map2);
                }
                map.put("hisDoctorExpertTeamMemberDtos", list);

                showLoading();
                mPresenter.addDoctorTeam(map);

                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case 200:
                    String info = data.getStringExtra("info");
                    editIntroduce.setText(info);
                    break;

                case 201:
                    DoctorListBean.RowsBean bean = (DoctorListBean.RowsBean) data.getSerializableExtra("bean");
                    if (bean.getId() == doctorInfo.getId()) {
                        ToastUtil.showToast("不可添加自己");
                        return;
                    }
                    for (int i = 0; i < beans.size(); i++) {
                        if (bean.getId() == beans.get(i).getId()) {
                            ToastUtil.showToast("不可重复添加相同成员");
                            return;
                        }
                    }
                    beans.add(bean);
                    foundTeamAdapter.notifyDataSetChanged();
                    break;
            }
        }
    }

    private void initAdapter() {
        mRecyclerView.setLayoutManager(new GridLayoutManager(this, 4));
        foundTeamAdapter = new FoundTeamAdapter(this, beans);

        foundTeamAdapter.setOnItemClickListener(new FoundTeamAdapter.OnItemClickListener() {
            @Override
            public void onItemClickEvent(View view) {
                Intent intent = new Intent(FoundTeamActivity.this, DoctorAddFActivity.class);
                startActivityForResult(intent, 201);
            }
        });

        mRecyclerView.setAdapter(foundTeamAdapter);
    }

    @Override
    public void addDoctorTeamSuccess(String bean) {
        ToastUtil.showToast("创建团队成功");
        setResult(RESULT_OK);
        finish();
    }

    @Override
    public void showLoading() {
        if (mLoadingDialog != null) {
            hideLoading();
        }
        mLoadingDialog = LoadingDialog.show(FoundTeamActivity.this, "");
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


}
