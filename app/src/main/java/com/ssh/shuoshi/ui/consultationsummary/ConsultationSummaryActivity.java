package com.ssh.shuoshi.ui.consultationsummary;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.gyf.barlibrary.ImmersionBar;
import com.ssh.shuoshi.R;
import com.ssh.shuoshi.bean.ConsultationSummaryBean;
import com.ssh.shuoshi.bean.EmrBean;
import com.ssh.shuoshi.bean.HisDoctorBean;
import com.ssh.shuoshi.bean.ImageDiagnoseBean;
import com.ssh.shuoshi.bean.im.SummaryCardBean;
import com.ssh.shuoshi.components.storage.UserStorage;
import com.ssh.shuoshi.inter.MyTextWatcher;
import com.ssh.shuoshi.library.util.ToastUtil;
import com.ssh.shuoshi.ui.BaseActivity;
import com.ssh.shuoshi.ui.diagnosissearch.DiagnosisSearchActivity;
import com.ssh.shuoshi.ui.dialog.LoadingDialog;
import com.ssh.shuoshi.view.title.UniteTitle;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import butterknife.BindView;

/**
 * 问诊小结
 */
public class ConsultationSummaryActivity extends BaseActivity implements ConsultationSummaryContract.View {

    @Inject
    ConsultationSummaryPresenter mPresenter;

    @BindView(R.id.uniteTitle)
    UniteTitle uniteTitle;
    @BindView(R.id.textNo)
    TextView textNo;
    @BindView(R.id.textTime)
    TextView textTime;
    @BindView(R.id.textName)
    TextView textName;
    @BindView(R.id.textSex)
    TextView textSex;
    @BindView(R.id.textAge)
    TextView textAge;
    @BindView(R.id.textDepartment)
    TextView textDepartment;
    @BindView(R.id.textViewPreliminaryDiagnosisChoose)
    TextView textViewPreliminaryDiagnosisChoose;

    @BindView(R.id.editTextPatient)
    EditText editTextPatient;
    @BindView(R.id.textViewPatientWordCount)
    TextView textViewPatientWordCount;

    @BindView(R.id.linearLayoutPreliminaryDiagnosisChoose)
    RelativeLayout linearLayoutPreliminaryDiagnosisChoose;

    @BindView(R.id.editTextSuggest)
    EditText editTextSuggest;
    @BindView(R.id.textViewSuggestWordCount)
    TextView textViewSuggestWordCount;

    @BindView(R.id.buttonSave)
    Button buttonSave;

    @Inject
    UserStorage mUserStorage;

    private int requestCode = 1001;
    private int mId;
    private LoadingDialog mLoadingDialog;
    private EmrBean mBean;
    private int diagnosisId = -1;    //诊断ID
    private ConsultationSummaryBean rowsBean;
    private int emrId;               //问诊小结ID是否存在
    private String currentTime;      //当前日期
    private String emrTime;          //问诊日期
    private HisDoctorBean doctorInfo;
    private int emrDoctorId;
    private boolean notChange;

    @Override
    public int initContentView() {
        return R.layout.activity_consultation_summary;
    }

    @Override
    protected void initInjector() {
        DaggerConsultationSummaryComponent.builder()
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
        uniteTitle.setBackListener(-1, v -> finish());

        doctorInfo = mUserStorage.getDoctorInfo();

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        currentTime = simpleDateFormat.format(new Date());
        Intent intent = getIntent();
        rowsBean = (ConsultationSummaryBean) intent.getSerializableExtra("rowsBean");
        notChange = intent.getBooleanExtra("notChange", false);
        emrDoctorId = intent.getIntExtra("emrDoctorId", -1);
        emrId = intent.getIntExtra("emrId", -1);
        if (rowsBean != null) {
            textTime.setText(currentTime);
            textName.setText("姓名：" + rowsBean.getPatientName());
            textSex.setText("性别：" + rowsBean.getSexName());
            textAge.setText("年龄：" + rowsBean.getPatientAge() + "岁");
            mId = rowsBean.getId();
        }
        textDepartment.setText("科室：" + doctorInfo.getHisSysDeptName());
        if (emrId != -1) {
            //获取问诊小结数据
            showLoading();
            mPresenter.getEmr(mId);
        }
        editTextPatient.addTextChangedListener((MyTextWatcher) s -> {
            textViewPatientWordCount.setText(s.length() + "/500");
        });
        editTextSuggest.addTextChangedListener((MyTextWatcher) s -> {
            textViewSuggestWordCount.setText(s.length() + "/500");
        });

        linearLayoutPreliminaryDiagnosisChoose.setOnClickListener(v -> {
            startActivityForResult(new Intent(ConsultationSummaryActivity.this,
                    DiagnosisSearchActivity.class), requestCode);
        });

        buttonSave.setOnClickListener(v -> {
            uploadData();
        });

        initScrollHandler();
    }

    //解决滑动冲突
    private void initScrollHandler() {
        editTextPatient.setOnTouchListener(new View.OnTouchListener() {
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                //canScrollVertically()方法为判断指定方向上是否可以滚动,参数为正数或负数,负数检查向上是否可以滚动,正数为检查向下是否可以滚动
                if (editTextPatient.canScrollVertically(1) || editTextPatient.canScrollVertically(-1)) {
                    v.getParent().requestDisallowInterceptTouchEvent(true);//requestDisallowInterceptTouchEvent();要求父类布局不在拦截触摸事件
                    if (event.getAction() == MotionEvent.ACTION_UP) { //判断是否松开
                        v.getParent().requestDisallowInterceptTouchEvent(false); //requestDisallowInterceptTouchEvent();让父类布局继续拦截触摸事件
                    }
                }
                return false;
            }
        });
        editTextSuggest.setOnTouchListener(new View.OnTouchListener() {
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (editTextSuggest.canScrollVertically(1) || editTextSuggest.canScrollVertically(-1)) {
                    v.getParent().requestDisallowInterceptTouchEvent(true);
                    if (event.getAction() == MotionEvent.ACTION_UP) {
                        v.getParent().requestDisallowInterceptTouchEvent(false);
                    }
                }
                return false;
            }
        });
    }

    private void uploadData() {
        if (TextUtils.isEmpty(textViewPreliminaryDiagnosisChoose.getText().toString())) {
            ToastUtil.showToast("请选择初步诊断");
            return;
        }

        //新增问诊小结
        Map<String, Object> mData = new HashMap<>();
        mData.put("illnessDesc", editTextPatient.getText().toString().trim());
        mData.put("consultationId", mId);
        mData.put("diagDesc", textViewPreliminaryDiagnosisChoose.getText().toString().trim());
        if (diagnosisId != -1) {
            mData.put("diagId", diagnosisId);
        }
        mData.put("orders", editTextSuggest.getText().toString());

        showLoading();
        if (emrId == -1) {
            mPresenter.addEmr(mData);
        } else {
            //判断是否超过当天
            if (!currentTime.equals(emrTime)) {
                ToastUtil.showToast("超过当天不允许修改");
                hideLoading();
                return;
            }
            mData.put("id", mBean.getId());
            mPresenter.changeEmr(mData);
        }
    }

    @Override
    public void showLoading() {
        if (mLoadingDialog != null) {
            hideLoading();
        }
        mLoadingDialog = LoadingDialog.show(ConsultationSummaryActivity.this, "");
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
        hideLoading();
        loadError(throwable);
    }

    @Override
    public void getEmrSuccess(EmrBean bean) {
        this.mBean = bean;
        textNo.setText(bean.getEmrNo());
        textDepartment.setText("科室：" + bean.getDoctorDeptName());
        diagnosisId = bean.getDoctorId();
        editTextPatient.setText(bean.getIllnessDesc());
        textViewPreliminaryDiagnosisChoose.setText(bean.getDiagDesc());
        buttonSave.setEnabled(true);
        editTextSuggest.setText(bean.getOrders());
        String createTime = bean.getCreateTime();
        if (!TextUtils.isEmpty(createTime) && createTime.length() >= 10) {
            emrTime = createTime.substring(0, 10);
        }

        if (emrDoctorId != -1 && emrDoctorId != doctorInfo.getId()) {
            editTextPatient.setEnabled(false);
            linearLayoutPreliminaryDiagnosisChoose.setClickable(false);
            editTextSuggest.setEnabled(false);
            buttonSave.setEnabled(false);
        }

        if (notChange) {
            editTextPatient.setEnabled(false);
            linearLayoutPreliminaryDiagnosisChoose.setClickable(false);
            editTextSuggest.setEnabled(false);
            buttonSave.setEnabled(false);
        }
    }

    @Override
    public void addEmrSuccess(Integer bean) {
        callBackData(bean);
    }

    private void callBackData(Integer bean) {
        Intent data = new Intent();
        SummaryCardBean summaryCardBean = new SummaryCardBean();
        summaryCardBean.setKey("health_summaryCard");
        SummaryCardBean.Content content = new SummaryCardBean.Content();
        content.setId(mId);
        content.setPatientName(rowsBean.getPatientName());
        content.setDoctorName(mUserStorage.getDoctorInfo().getName());
        summaryCardBean.setContent(content);
        data.putExtra("summaryCard", summaryCardBean);
        setResult(RESULT_OK, data);
        finish();
    }

    @Override
    public void changeEmrSuccess(Integer bean) {
        callBackData(bean);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.detachView();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            String diagnosisName = data.getStringExtra("Name");
            diagnosisId = data.getIntExtra("id", -1);
            textViewPreliminaryDiagnosisChoose.setText(diagnosisName);
            buttonSave.setEnabled(true);
        }
    }

}
