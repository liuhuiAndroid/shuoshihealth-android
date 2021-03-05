package com.ssh.shuoshi.ui.prescription.template.commonlyprescriptionwestern.templatedetail;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ssh.shuoshi.R;
import com.ssh.shuoshi.bean.template.HisPrescriptionTemplateDetailDtosBean;
import com.ssh.shuoshi.bean.template.PrescriptionTemplateBean;
import com.ssh.shuoshi.library.adapter.CommonAdapter;
import com.ssh.shuoshi.library.adapter.base.ViewHolder;
import com.ssh.shuoshi.ui.BaseActivity;
import com.ssh.shuoshi.ui.prescription.template.add.AddCommonlyTempalteActivity;
import com.ssh.shuoshi.view.title.UniteTitle;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 处方模版详情
 * created by hwt on 2020/12/22
 */
public class PrescriptionTemplateDetailActivity extends BaseActivity {
    @BindView(R.id.uniteTitle)
    UniteTitle uniteTitle;
    @BindView(R.id.textResult)
    TextView textResult;
    @BindView(R.id.textViewPatientTitle)
    TextView textViewPatientTitle;
    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;
    private PrescriptionTemplateBean.RowsBean mBean;
    private int mPhamCategoryId;

    @Override
    public int initContentView() {
        return R.layout.activity_prescription_template_detail;
    }

    @Override
    protected void initInjector() {

    }

    @SuppressLint("WrongConstant")
    @Override
    protected void initUiAndListener() {
        uniteTitle.setBackListener(-1, v -> finish());

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayout.VERTICAL, false));
        mBean = (PrescriptionTemplateBean.RowsBean) getIntent().getSerializableExtra("template");
        mPhamCategoryId = getIntent().getIntExtra("mPhamCategoryId", -1);
        if (mBean != null) {
            initData();
        }

        boolean isShow = getIntent().getBooleanExtra("isShow", false);
        if (isShow) {
            uniteTitle.setRightButton("编辑处方", v -> {
                Intent intent = new Intent(PrescriptionTemplateDetailActivity.this, AddCommonlyTempalteActivity.class);
                intent.putExtra("template", mBean);
                intent.putExtra("type", "2");
                if (mPhamCategoryId != -1) {
                    intent.putExtra("mPhamCategoryId", mPhamCategoryId);
                }
                startActivity(intent);
                finish();
            });
        }
    }

    private void initData() {
        textResult.setText(mBean.getTemplateName());
        List<HisPrescriptionTemplateDetailDtosBean> detailDtos = mBean.getHisPrescriptionTemplateDetailDtos();
        CommonAdapter commonAdapter = new CommonAdapter<HisPrescriptionTemplateDetailDtosBean>(this, R.layout.item_prescription_detail, detailDtos) {
            @Override
            protected void convert(ViewHolder holder, final HisPrescriptionTemplateDetailDtosBean bean, final int pos) {
                TextView textName = holder.getView(R.id.textName);
                TextView textSpec = holder.getView(R.id.textSpec);
                TextView textSize = holder.getView(R.id.textSize);
                TextView textSpecification = holder.getView(R.id.textSpecification);
                textName.setText(bean.getPhamName() + "(");
                textSpec.setText(bean.getPhamSpec() + ")");
                textSize.setText("x" + bean.getAmount());
                textSpecification.setText("用法用量：" + bean.getAdministration() + ", 每次" +
                        bean.getDosage() + bean.getDosageUnits() + ", " + bean.getFrequency());
            }
        };
        mRecyclerView.setAdapter(commonAdapter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}
