package com.ssh.shuoshi.ui.myorder.consultationdetail;

import android.text.TextUtils;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ssh.shuoshi.R;
import com.ssh.shuoshi.bean.ConsultationBillBean;
import com.ssh.shuoshi.ui.BaseActivity;
import com.ssh.shuoshi.view.title.UniteTitle;

import java.io.Serializable;
import java.math.BigDecimal;

import butterknife.BindView;

/**
 * 问诊订单详情
 */
public class ConsultationDetailActivity extends BaseActivity {

    @BindView(R.id.uniteTitle)
    UniteTitle uniteTitle;
    @BindView(R.id.orderType)
    TextView orderType;
    @BindView(R.id.orderNo)
    TextView orderNo;
    @BindView(R.id.orderTime)
    TextView orderTime;
    @BindView(R.id.orderFee)
    TextView orderFee;
    @BindView(R.id.orderPersonInfo)
    TextView orderPersonInfo;
    @BindView(R.id.orderInfoDetail)
    TextView orderInfoDetail;
    @BindView(R.id.orderHappening)
    TextView orderHappening;
    @BindView(R.id.orderError)
    TextView orderError;
    @BindView(R.id.rlError)
    RelativeLayout rlError;

    @Override
    public int initContentView() {
        return R.layout.activity_consultation_detail;
    }

    @Override
    protected void initInjector() {

    }

    @Override
    protected void initUiAndListener() {

        ConsultationBillBean.ConsultationBean data = (ConsultationBillBean.ConsultationBean)
                getIntent().getSerializableExtra("data");

        uniteTitle.setBackListener(-1, v -> finish());

//        if (data.getConsultationState() == 1) {
//            orderType.setText("图文问诊");
//        } else if (data.getConsultationState() == 2) {
//            orderType.setText("视频问诊");
//        } else if (data.getConsultationState() == 3) {
//            orderType.setText("专家团队问诊");
//        } else if (data.getConsultationState() == 4) {
//            orderType.setText("专家团队视频");
//        }

        orderType.setText(data.getConsultationTypeName());
        orderNo.setText(data.getConsultationOrderNo());
        orderTime.setText(data.getConsultationCreateTime());

        BigDecimal bg = new BigDecimal(data.getAmount());
//        double f1 = bg.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
//        orderFee.setText("¥" + f1);

        double amount = data.getAmount();
        if (amount < 0) {
            String amountS = String.valueOf(amount);
            String[] split = amountS.split("-");
            if (split != null && split.length == 2) {
                orderFee.setText("¥" + split[1]);
            }
        } else {
            orderFee.setText("¥" + amount);
        }

        orderPersonInfo.setText(data.getPatientName() + " " + data.getSexName() + " " + data.getPatientAge() + "岁");
        orderInfoDetail.setText(data.getIllnessDesc());

        // 是否到医院就诊过，0否，1是
        rlError.setVisibility(View.GONE);
        if (data.getVisitedFlag() == 0) {
            orderHappening.setText("未去医院就诊过");
        } else if (data.getVisitedFlag() == 1) {
            orderHappening.setText("去医院就诊过");

            if (!TextUtils.isEmpty(data.getVisitHospitalDiag())) {
                rlError.setVisibility(View.VISIBLE);
                orderError.setText(data.getVisitHospitalDiag());
            }
        }

    }
}
