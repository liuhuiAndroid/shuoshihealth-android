package com.ssh.shuoshi.ui.worksetting.servicenote;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.ssh.shuoshi.Constants;
import com.ssh.shuoshi.R;
import com.ssh.shuoshi.library.util.ToastUtil;
import com.ssh.shuoshi.ui.BaseActivity;
import com.ssh.shuoshi.ui.dialog.LoadingDialog;
import com.ssh.shuoshi.view.title.UniteTitle;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * created by hwt on 2020/12/11
 */
public class ServiceNoteActivity extends BaseActivity implements ServiceNoteContract.View {

    @BindView(R.id.title)
    UniteTitle title;
    @BindView(R.id.btn_open)
    Button btnOpen;
    @Inject
    ServiceNotePresenter mPresenter;
    @BindView(R.id.textOne)
    TextView textOne;
    @BindView(R.id.textTwo)
    TextView textTwo;
    @BindView(R.id.textThree)
    TextView textThree;

    private LoadingDialog mLoadingDialog;
    private int consultationType;

    @Override
    public int initContentView() {
        return R.layout.activity_service_note;
    }

    @Override
    protected void initInjector() {
        DaggerServiceNoteComponent.builder()
                .applicationComponent(getApplicationComponent())
                .activityModule(getActivityModule())
                .build()
                .inject(this);
    }

    @Override
    protected void initUiAndListener() {
        consultationType = getIntent().getIntExtra("consultationType", -1);
        mPresenter.attachView(this);
        title.setBackListener(-1, v -> finish());
        if (consultationType == Constants.CONSULTATION_TYPE_PICTURE) {
            title.setTitleName("开通图文问诊服务");

            textOne.setText(getResources().getString(R.string.image_text_one));
            textTwo.setText(getResources().getString(R.string.image_text_two));
            textThree.setText(getResources().getString(R.string.image_text_three));

        } else if (consultationType == Constants.CONSULTATION_TYPE_VIDEO) {
            title.setTitleName("开通视频问诊服务");

            textOne.setText(getResources().getString(R.string.video_text_one));
            textTwo.setText(getResources().getString(R.string.video_text_two));
            textThree.setText(getResources().getString(R.string.video_text_three));
        }
        btnOpen.setOnClickListener(v -> {
            mPresenter.addDoctorConsultation(consultationType);
        });
    }

    @Override
    public void showLoading() {
        if (mLoadingDialog != null) {
            hideLoading();
        }
        mLoadingDialog = LoadingDialog.show(ServiceNoteActivity.this, "");
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

    }

    @Override
    public void onAddDoctorConsultationSuccess() {
        ToastUtil.showToast("开通成功");
        setResult(RESULT_OK, new Intent());
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.detachView();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}
