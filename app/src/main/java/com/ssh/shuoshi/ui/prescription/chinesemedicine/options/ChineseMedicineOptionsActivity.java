package com.ssh.shuoshi.ui.prescription.chinesemedicine.options;

import android.content.Intent;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ssh.shuoshi.R;
import com.ssh.shuoshi.bean.ChineseMedicineType;
import com.ssh.shuoshi.bean.ImageDiagnoseBean;
import com.ssh.shuoshi.inter.OnItemClickListener;
import com.ssh.shuoshi.ui.BaseActivity;
import com.ssh.shuoshi.ui.prescription.chinesemedicine.edit.EditChineseMedicinePrescriptionActivity;
import com.ssh.shuoshi.view.title.UniteTitle;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;

/**
 * 中药方案类型
 */
public class ChineseMedicineOptionsActivity extends BaseActivity implements ChineseMedicineOptionsContract.View {

    @Inject
    ChineseMedicineOptionsPresenter mPresenter;

    @BindView(R.id.uniteTitle)
    UniteTitle uniteTitle;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    @Override
    public int initContentView() {
        return R.layout.activity_chinese_medicine_options;
    }

    @Override
    protected void initInjector() {
        DaggerChineseMedicineOptionsComponent.builder()
                .applicationComponent(getApplicationComponent())
                .activityModule(getActivityModule())
                .build()
                .inject(this);
    }

    @Override
    protected void initUiAndListener() {
        mPresenter.attachView(this);

        uniteTitle.setBackListener(-1, v -> finish());
        initRecyclerView();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                //关闭300
                case 300:
                    int mPrescriptionId = data.getIntExtra("mPrescriptionId", -1);
                    Intent intent = new Intent();
                    intent.putExtra("prescriptionId", mPrescriptionId);
                    setResult(RESULT_OK, intent);
                    finish();
                    break;
            }
        }
    }

    private void initRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(ChineseMedicineOptionsActivity.this));
        ChineseMedicineOptionsAdapter mAdapter = new ChineseMedicineOptionsAdapter();
        mAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if (position == 0) {
                    Intent intent = new Intent(ChineseMedicineOptionsActivity.this,
                            EditChineseMedicinePrescriptionActivity.class);
                    intent.putExtra("name", mAdapter.getList().get(position).getName());
                    intent.putExtra("consultaion", (ImageDiagnoseBean.RowsBean) getIntent().getSerializableExtra("consultaion"));
                    startActivityForResult(intent, 300);
                }
            }
        });
        recyclerView.setAdapter(mAdapter);
        List<ChineseMedicineType> chineseMedicineTypes = new ArrayList<>();
        ChineseMedicineType chineseMedicineType1 = new ChineseMedicineType("颗粒剂");
        ChineseMedicineType chineseMedicineType2 = new ChineseMedicineType("中药饮片");
        ChineseMedicineType chineseMedicineType3 = new ChineseMedicineType("定制蜜丸");
        ChineseMedicineType chineseMedicineType4 = new ChineseMedicineType("定制水丸");
        ChineseMedicineType chineseMedicineType5 = new ChineseMedicineType("定制散剂");
        ChineseMedicineType chineseMedicineType6 = new ChineseMedicineType("定制膏方");
        ChineseMedicineType chineseMedicineType7 = new ChineseMedicineType("其他");
        chineseMedicineTypes.add(chineseMedicineType1);
        chineseMedicineTypes.add(chineseMedicineType2);
        chineseMedicineTypes.add(chineseMedicineType3);
        chineseMedicineTypes.add(chineseMedicineType4);
        chineseMedicineTypes.add(chineseMedicineType5);
        chineseMedicineTypes.add(chineseMedicineType6);
        chineseMedicineTypes.add(chineseMedicineType7);
        mAdapter.setList(chineseMedicineTypes);
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
}
