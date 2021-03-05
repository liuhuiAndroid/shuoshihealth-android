package com.ssh.shuoshi.ui.myorder.prescriptiondetail;

import android.widget.TextView;

import com.ssh.shuoshi.R;
import com.ssh.shuoshi.ui.BaseActivity;
import com.ssh.shuoshi.view.title.UniteTitle;

import butterknife.BindView;

/**
 * 处方订单详情
 */
public class PrescriptionDetailActivity extends BaseActivity {

    @BindView(R.id.uniteTitle)
    UniteTitle uniteTitle;
    @BindView(R.id.orderNo)
    TextView orderNo;
    @BindView(R.id.orderTime)
    TextView orderTime;
    @BindView(R.id.orderFee)
    TextView orderFee;
    @BindView(R.id.orderPersonInfo)
    TextView orderPersonInfo;
    @BindView(R.id.orderInfoTime)
    TextView orderInfoTime;
    @BindView(R.id.orderDepartment)
    TextView orderDepartment;
    @BindView(R.id.orderDoctors)
    TextView orderDoctors;

    @Override
    public int initContentView() {
        return R.layout.activity_prescription_detail;
    }

    @Override
    protected void initInjector() {

    }

    @Override
    protected void initUiAndListener() {

        uniteTitle.setBackListener(-1, v -> finish());

        orderNo.setText("32345987653243324");
        orderTime.setText("2020-12-10 19:08:03");
        orderFee.setText("¥50.00");
        orderPersonInfo.setText("张三 女 40岁");
        orderInfoTime.setText("2020-12-10");
        orderDepartment.setText("科室");
        orderDoctors.setText("张三");

    }

}
