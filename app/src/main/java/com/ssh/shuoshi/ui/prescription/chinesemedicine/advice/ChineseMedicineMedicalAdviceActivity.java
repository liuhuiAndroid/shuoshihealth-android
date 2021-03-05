package com.ssh.shuoshi.ui.prescription.chinesemedicine.advice;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.ssh.shuoshi.R;
import com.ssh.shuoshi.inter.MyTextWatcher;
import com.ssh.shuoshi.library.util.ToastUtil;
import com.ssh.shuoshi.ui.BaseActivity;
import com.ssh.shuoshi.view.title.UniteTitle;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;
import com.zhy.view.flowlayout.TagView;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 中药医嘱
 */
public class ChineseMedicineMedicalAdviceActivity extends BaseActivity implements ChineseMedicineMedicalAdviceContract.View {

    @Inject
    ChineseMedicineMedicalAdvicePresenter mPresenter;
    @BindView(R.id.flowLayoutTime)
    TagFlowLayout flowLayoutTime;
    @BindView(R.id.flowLayoutTaboo)
    TagFlowLayout flowLayoutTaboo;

    @BindView(R.id.editTextAdvice)
    EditText editTextAdvice;
    @BindView(R.id.textViewAdvice)
    TextView textViewAdvice;
    @BindView(R.id.tv_back)
    TextView tvBack;
    @BindView(R.id.tv_save)
    TextView tvSave;

    private LayoutInflater mInflater = null;

    private String[] mValTimes = new String[]{"饭前1小时服用", "饭后1小时服用", "空腹服用", "晨起服用"};
    private String[] mValsTaboo = new String[]{"忌辛辣", "忌油腻", "忌生冷", "忌海鲜", "忌辛辣", "忌刺激性的食物"
            , "孕妇禁用", "忌难以消化的食物", "忌烟酒", "忌与西药同服", "忌辛辣", "忌油腻"};

    @Override
    public int initContentView() {
        return R.layout.activity_chinese_medicine_advice;
    }

    @Override
    protected void initInjector() {
        DaggerChineseMedicineMedicalAdviceComponent.builder()
                .applicationComponent(getApplicationComponent())
                .activityModule(getActivityModule())
                .build()
                .inject(this);
    }

    @Override
    protected void initUiAndListener() {
        mInflater = LayoutInflater.from(this);
        mPresenter.attachView(this);

        String oldData = getIntent().getStringExtra("oldData");
        editTextAdvice.setText(oldData);

        tvBack.setOnClickListener(v -> finish());
        tvSave.setOnClickListener(v -> {
            String advice = editTextAdvice.getText().toString().trim();
            if (advice.isEmpty()) {
                ToastUtil.showToast("请输入医嘱");
                return;
            }
            Intent intent = new Intent();
            intent.putExtra("advice", advice);
            setResult(RESULT_OK, intent);
            finish();
        });
        initEditText();
        initTagFlowLayoutTime();
        initTagFlowLayoutTaboo();
    }

    public void initEditText() {
        editTextAdvice.addTextChangedListener((MyTextWatcher) s -> {
            textViewAdvice.setText(s.length() + "/100");
        });
    }

    public void initTagFlowLayoutTime() {
        flowLayoutTime.setAdapter(new TagAdapter<String>(mValTimes) {
            @Override
            public View getView(FlowLayout parent, int position, String s) {
                TextView tv = (TextView) mInflater.inflate(R.layout.item_chinese_medicine_advice,
                        flowLayoutTime, false);
                tv.setText(s);
                return tv;
            }
        });
        flowLayoutTime.setOnTagClickListener(new TagFlowLayout.OnTagClickListener() {
            @Override
            public boolean onTagClick(View view, int position, FlowLayout parent) {
                boolean selected = ((TagView) parent.getChildAt(position)).isChecked();
                if (selected) {
                    String advice = editTextAdvice.getText().toString().trim();
                    if (!advice.isEmpty()) {
                        advice += ",";
                    }
                    editTextAdvice.setText(advice + mValTimes[position]);
                }
                return false;
            }
        });
    }

    public void initTagFlowLayoutTaboo() {
        flowLayoutTaboo.setAdapter(new TagAdapter<String>(mValsTaboo) {
            @Override
            public View getView(FlowLayout parent, int position, String s) {
                TextView tv = (TextView) mInflater.inflate(R.layout.item_chinese_medicine_advice,
                        flowLayoutTaboo, false);
                tv.setText(s);
                return tv;
            }
        });
        flowLayoutTaboo.setOnTagClickListener(new TagFlowLayout.OnTagClickListener() {
            @Override
            public boolean onTagClick(View view, int position, FlowLayout parent) {
                boolean selected = ((TagView) parent.getChildAt(position)).isChecked();
                if (selected) {
                    String advice = editTextAdvice.getText().toString().trim();
                    if (!advice.isEmpty()) {
                        advice += ",";
                    }
                    editTextAdvice.setText(advice + mValsTaboo[position]);
                }
                return false;
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.detachView();
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
}