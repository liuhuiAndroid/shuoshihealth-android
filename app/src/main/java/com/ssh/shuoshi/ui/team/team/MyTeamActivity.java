package com.ssh.shuoshi.ui.team.team;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ssh.shuoshi.R;
import com.ssh.shuoshi.bean.HisDoctorBean;
import com.ssh.shuoshi.bean.team.DoctorTeamBean;
import com.ssh.shuoshi.components.storage.UserStorage;
import com.ssh.shuoshi.library.adapter.CommonAdapter;
import com.ssh.shuoshi.library.adapter.base.ViewHolder;
import com.ssh.shuoshi.ui.BaseActivity;
import com.ssh.shuoshi.ui.dialog.LoadingDialog;
import com.ssh.shuoshi.ui.team.doctorteam.MyDoctorTeamActivity;
import com.ssh.shuoshi.ui.team.found.FoundTeamActivity;
import com.ssh.shuoshi.view.title.UniteTitle;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 我的团队
 * created by hwt on 2021/1/3
 */
public class MyTeamActivity extends BaseActivity implements MyTeamContract.View {
    @Inject
    MyTeamPresenter mPresenter;
    @BindView(R.id.title)
    UniteTitle title;
    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.btnFound)
    Button btnFound;
    @BindView(R.id.llFound)
    LinearLayout llFound;

    @Inject
    UserStorage mUserStorage;
    @BindView(R.id.miss_tu)
    LinearLayout missTu;
    @BindView(R.id.scrollView)
    ScrollView scrollView;

    private HisDoctorBean doctorInfo;

    private LoadingDialog mLoadingDialog;
    private CommonAdapter mCommonAdapter;
    private List<DoctorTeamBean.RowsBean> rowsBeans = new ArrayList<>();

    @Override
    public int initContentView() {
        return R.layout.activity_my_team;
    }

    @Override
    protected void initInjector() {
        DaggerMyTeamComponent.builder()
                .applicationComponent(getApplicationComponent())
                .activityModule(getActivityModule())
                .build()
                .inject(this);
    }

    @SuppressLint("WrongConstant")
    @Override
    protected void initUiAndListener() {
        mPresenter.attachView(this);

        title.setBackListener(-1, v -> finish());

        doctorInfo = mUserStorage.getDoctorInfo();
        String doctorTitleName = doctorInfo.getDoctorTitleName();
        if (doctorTitleName.equals("主任医师")) {
            llFound.setVisibility(View.VISIBLE);
        } else {
            llFound.setVisibility(View.GONE);
        }
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayout.VERTICAL, false));
        initAdapter();
        btnFound.setOnClickListener(v -> {
            Intent intent = new Intent(this, FoundTeamActivity.class);
            startActivityForResult(intent, 200);
        });

        mPresenter.getDoctorListTeam();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == 200) {
                mPresenter.getDoctorListTeam();
            }
        }
    }

    private void initAdapter() {
        mCommonAdapter = new CommonAdapter<DoctorTeamBean.RowsBean>(this, R.layout.item_my_team, rowsBeans) {
            @Override
            protected void convert(ViewHolder holder, final DoctorTeamBean.RowsBean bean, int pos) {
                RelativeLayout container = holder.getView(R.id.container);
                TextView textName = holder.getView(R.id.textName);

                textName.setText(bean.getDoctorExpertTeamName() + " (" + bean.getTeamCount() + "人)");

                container.setOnClickListener(v -> {
                    Intent intent = new Intent(MyTeamActivity.this, MyDoctorTeamActivity.class);
                    intent.putExtra("id", bean.getExpertTeamId());
                    startActivityForResult(intent, 200);
                });
            }
        };
        mRecyclerView.setAdapter(mCommonAdapter);
    }

    @Override
    public void showLoading() {
        if (mLoadingDialog != null) {
            hideLoading();
        }
        mLoadingDialog = LoadingDialog.show(MyTeamActivity.this, "");
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
    public void getDoctorListTeamSuccess(DoctorTeamBean bean) {
        rowsBeans = bean.getRows();
        if (rowsBeans == null || rowsBeans.size() == 0) {
            missTu.setVisibility(View.VISIBLE);
            mRecyclerView.setVisibility(View.GONE);
            scrollView.setBackgroundColor(getResources().getColor(R.color.white));
        } else {
            missTu.setVisibility(View.GONE);
            mRecyclerView.setVisibility(View.VISIBLE);
            scrollView.setBackgroundColor(getResources().getColor(R.color.fQi_grey));
        }
        mCommonAdapter.setDatas(rowsBeans);
        for (int i = 0; i < rowsBeans.size(); i++) {
            if (rowsBeans.get(i).getLevel() == 1) {
                llFound.setVisibility(View.GONE);
                break;
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}
