package com.ssh.shuoshi.ui.medicalhistory;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.ssh.shuoshi.R;
import com.ssh.shuoshi.bean.patient.MedicalHistoryBean;
import com.ssh.shuoshi.bean.prescription.HisPrescriptionDetailDtosBean;
import com.ssh.shuoshi.bean.prescription.HisPrescriptionDtoBean;
import com.ssh.shuoshi.bean.prescription.HisPrescriptionTcmDetailsBean;
import com.ssh.shuoshi.library.adapter.CommonAdapter;
import com.ssh.shuoshi.library.adapter.base.ViewHolder;
import com.ssh.shuoshi.library.adapter.wrapper.LoadMoreWrapper;
import com.ssh.shuoshi.library.widget.MyPtrClassicFrameLayout;
import com.ssh.shuoshi.ui.BaseActivity;
import com.ssh.shuoshi.ui.dialog.LoadingDialog;
import com.ssh.shuoshi.ui.imagegallery.ImageGalleryActivity;
import com.ssh.shuoshi.view.title.UniteTitle;
import com.tencent.mmkv.MMKV;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;

/**
 * 患者病历
 * created by hwt on 2020/12/27
 */
public class MedicalHistoryActivity extends BaseActivity implements MedicalHistoryContract.View,
        PtrHandler, LoadMoreWrapper.OnLoadMoreListener {

    @Inject
    MedicalHistoryPresenter mPresenter;
    @BindView(R.id.uniteTitle)
    UniteTitle uniteTitle;
    @BindView(R.id.textName)
    TextView textName;
    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.ptr_layout)
    MyPtrClassicFrameLayout mPtrLayout;
    @BindView(R.id.rlHead)
    RelativeLayout rlHead;
    @BindView(R.id.nestedSc)
    NestedScrollView mNestedSc;

    private LoadingDialog mLoadingDialog;
    private CommonAdapter<MedicalHistoryBean.RowsBean> mCommonAdapter;
    private LoadMoreWrapper mLoadMoreWrapper;
    private List<MedicalHistoryBean.RowsBean> mData = new ArrayList<>();
    //患者ID
    private int mPatientId;
    private int mScrollY;

    @Override
    public int initContentView() {
        return R.layout.activity_medical_history;
    }

    @Override
    protected void initInjector() {
        DaggerMedicalHistoryComponent.builder()
                .applicationComponent(getApplicationComponent())
                .activityModule(getActivityModule())
                .build()
                .inject(this);
    }

    @SuppressLint("WrongConstant")
    @Override
    protected void initUiAndListener() {
        mPresenter.attachView(this);

        mPtrLayout.setPtrHandler(this);
        //显示时间
        mPtrLayout.setLastUpdateTimeRelateObject(this);
        //禁止下拉
        mPtrLayout.disableWhenHorizontalMove(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayout.VERTICAL, false));
        uniteTitle.setBackListener(-1, v -> finish());

        mPatientId = getIntent().getIntExtra("patientId", -1);

        mPresenter.onRefresh(mPatientId);

        mNestedSc.setOnScrollChangeListener((NestedScrollView.OnScrollChangeListener)
                (v, scrollX, scrollY, oldScrollX, oldScrollY) -> mScrollY = scrollY);
    }

    @Override
    public void onRefreshCompleted(MedicalHistoryBean beans, boolean isClear) {
        if (isClear) {
            mData.clear();
        }
        mData.addAll(beans.getRows());

        if (mData.size() == 0) {
            rlHead.setVisibility(View.GONE);
            return;
        } else {
            MedicalHistoryBean.RowsBean rowsBean = mData.get(0);
            textName.setText(rowsBean.getPatientName() + " (" + rowsBean.getSexName() + " " + rowsBean.getPatientAge() + "岁)");
            rlHead.setVisibility(View.VISIBLE);
        }

        if (mCommonAdapter == null) {
            mCommonAdapter = new CommonAdapter<MedicalHistoryBean.RowsBean>(this, R.layout.item_medical_history, mData) {
                @SuppressLint("WrongConstant")
                @Override
                protected void convert(ViewHolder holder, final MedicalHistoryBean.RowsBean bean, int pos) {
                    TextView textTypeName = holder.getView(R.id.textTypeName);
                    TextView textTime = holder.getView(R.id.textTime);
                    TextView textDesc = holder.getView(R.id.textDesc);
                    TextView textDiagnose = holder.getView(R.id.textDiagnose);
                    TextView textDigest = holder.getView(R.id.textDigest);
                    TextView textNoduleDiagnose = holder.getView(R.id.textNoduleDiagnose);
                    TextView textOpinion = holder.getView(R.id.textOpinion);
                    TextView textMethodDesc = holder.getView(R.id.textMethodDesc);
                    TextView textUseDesc = holder.getView(R.id.textUseDesc);
                    TextView textAdviceDesc = holder.getView(R.id.textAdviceDesc);
                    RecyclerView recyclerViewPhoto = holder.getView(R.id.recyclerViewPhoto);
                    RecyclerView recyclerViewWest = holder.getView(R.id.recyclerViewWest);
                    RecyclerView recyclerViewChinese = holder.getView(R.id.recyclerViewChinese);
                    RelativeLayout rlEmrDiagDesc = holder.getView(R.id.rlEmrDiagDesc);
                    RelativeLayout rlChinese = holder.getView(R.id.rl_chinese);
                    RelativeLayout rlPhoto = holder.getView(R.id.rlPhoto);
                    RelativeLayout rlNodule = holder.getView(R.id.rlNodule);
                    RelativeLayout rlRp = holder.getView(R.id.rlRp);

                    if (bean.getVisitedFlag() == 0) {
                        rlEmrDiagDesc.setVisibility(View.GONE);
                    } else {
                        rlEmrDiagDesc.setVisibility(View.VISIBLE);
                    }

                    List<String> images = bean.getPhotoImages();
                    List<String> imgList = new ArrayList<>();
                    List<String> checkImgs = bean.getCheckImgs();
                    if (checkImgs != null) {
                        imgList.addAll(checkImgs);
                    }

                    if (!TextUtils.isEmpty(bean.getFaceImg())) {
                        imgList.add(bean.getFaceImg());
                    }
                    if (!TextUtils.isEmpty(bean.getFurImg())) {
                        imgList.add(bean.getFurImg());
                    }

                    if (imgList.size() == 0) {
                        rlPhoto.setVisibility(View.GONE);
                    } else {
                        rlPhoto.setVisibility(View.VISIBLE);
                    }

                    if (images == null || images.size() == 0) {
                        //请求图片数据
                        requestPhoto(imgList, pos);
                    }
//                    if (bean.getCheckImgs() == null || bean.getCheckImgs().size() == 0) {
//                        rlPhoto.setVisibility(View.GONE);
//                    } else {
//                        rlPhoto.setVisibility(View.VISIBLE);
//                    }

                    if (bean.getEmrId() == null) {
                        rlNodule.setVisibility(View.GONE);
                    } else {
                        rlNodule.setVisibility(View.VISIBLE);
                    }

                    String date = bean.getActualStartTime();
                    if (!TextUtils.isEmpty(date) && date.length() >= 10) {
                        date = date.substring(0, 10);
                    }
                    textTypeName.setText(bean.getConsultationTypeName());
                    textTime.setText(date);
                    textDesc.setText(bean.getIllnessDesc());
                    textDiagnose.setText(bean.getVisitHospitalDiag());
                    textDigest.setText(bean.getEmrIllnessDesc());
                    textNoduleDiagnose.setText(bean.getEmrDiagDesc());
                    textOpinion.setText(bean.getEmrOrders());


                    if (images != null) {
                        recyclerViewPhoto.setLayoutManager(new GridLayoutManager(MedicalHistoryActivity.this, 3));
                        CommonAdapter<String> childAdapter = new CommonAdapter<String>(MedicalHistoryActivity.this, R.layout.item_medical_history_photo, images) {
                            @Override
                            protected void convert(ViewHolder holder, String path, int position) {
                                ImageView imgPhoto = holder.getView(R.id.imgPhoto);
                                Glide.with(MedicalHistoryActivity.this).load(path).into(imgPhoto);

                                imgPhoto.setOnClickListener(v -> {
                                    Intent intent = new Intent(MedicalHistoryActivity.this, ImageGalleryActivity.class);
                                    MMKV.defaultMMKV().putString("imageUrls", new Gson().toJson(images));
                                    startActivity(intent);
                                });

                            }
                        };
                        recyclerViewPhoto.setAdapter(childAdapter);
                    }


                    //底部列表hisPrescriptionDto
                    HisPrescriptionDtoBean dto = bean.getHisPrescriptionDto();
                    if (dto == null) {
                        rlRp.setVisibility(View.GONE);
                        return;
                    } else {
                        rlRp.setVisibility(View.VISIBLE);
                    }
                    List<HisPrescriptionDetailDtosBean> detailDtos = dto.getHisPrescriptionDetailDtos();
                    if (detailDtos == null || detailDtos.size() == 0) {
                        return;
                    }
                    HisPrescriptionDetailDtosBean detailDtosBean = detailDtos.get(0);

                    List<HisPrescriptionTcmDetailsBean> tcmDetails = detailDtosBean.getHisPrescriptionTcmDetails();
                    if (tcmDetails == null || tcmDetails.size() == 0) {
                        recyclerViewWest.setVisibility(View.VISIBLE);
                        rlChinese.setVisibility(View.GONE);
                        recyclerViewWest.setLayoutManager(new LinearLayoutManager(MedicalHistoryActivity.this, LinearLayout.VERTICAL, false));
                        CommonAdapter<HisPrescriptionDetailDtosBean> commonAdapter1 = new CommonAdapter<HisPrescriptionDetailDtosBean>(MedicalHistoryActivity.this, R.layout.item_my_prescription_detail_west_item, detailDtos) {
                            @Override
                            protected void convert(ViewHolder holder, final HisPrescriptionDetailDtosBean bean, final int pos) {
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
                        recyclerViewWest.setAdapter(commonAdapter1);
                    } else {
                        recyclerViewWest.setVisibility(View.GONE);
                        rlChinese.setVisibility(View.VISIBLE);
                        recyclerViewChinese.setLayoutManager(new GridLayoutManager(MedicalHistoryActivity.this, 3));

                        CommonAdapter<HisPrescriptionTcmDetailsBean> commonAdapter2 = new CommonAdapter<HisPrescriptionTcmDetailsBean>(MedicalHistoryActivity.this, R.layout.item_my_prescription_detail_chinese_item, tcmDetails) {
                            @Override
                            protected void convert(ViewHolder holder, final HisPrescriptionTcmDetailsBean bean, final int pos) {
                                TextView textName = holder.getView(R.id.textName);
                                textName.setText(bean.getPhamName() + bean.getAmount() + bean.getUnits());
                            }
                        };
                        recyclerViewChinese.setAdapter(commonAdapter2);

                        textUseDesc.setText("共" + detailDtosBean.getAmount() + detailDtosBean.getDosageUnits() +
                                ", 每次" + detailDtosBean.getDosage() + detailDtosBean.getDosageUnits() + ", " +
                                detailDtosBean.getFrequency() + "," + detailDtosBean.getAdministration());
                        textMethodDesc.setText(detailDtosBean.getPhamName());
                        textAdviceDesc.setText(detailDtosBean.getFreqDetail());
                    }
                }
            };

            mLoadMoreWrapper = new LoadMoreWrapper(mCommonAdapter);
            mLoadMoreWrapper.setLoadMoreView(LayoutInflater.from(this)
                    .inflate(R.layout.footer_view_load_more, mRecyclerView, false));
            mLoadMoreWrapper.setOnLoadMoreListener(this);
            mRecyclerView.setAdapter(mLoadMoreWrapper);
        } else {
            if (mRecyclerView.getScrollState() == RecyclerView.SCROLL_STATE_IDLE || (!mRecyclerView.isComputingLayout())) {
                mLoadMoreWrapper.notifyDataSetChanged();
            }
        }

        if (mPtrLayout != null && mPtrLayout.isRefreshing()) {
            mPtrLayout.refreshComplete();
        }
    }

    //请求图片数据
    private void requestPhoto(List<String> checkImgs, int position) {
        if (checkImgs != null && checkImgs.size() > 0) {
            String[] photoPath = checkImgs.toArray(new String[checkImgs.size()]);
            mPresenter.getImagePath(photoPath, position);
        }
    }

    @Override
    public void imgDownload(List<String> bean, int position) {
        mData.get(position).setPhotoImages(bean);
        mCommonAdapter.setDatas(mData);
//        mLoadMoreWrapper.notifyDataSetChanged();
        mLoadMoreWrapper.notifyItemChanged(position);
    }

    @Override
    public void onLoadCompleted(boolean isLoadAll) {
        mLoadMoreWrapper.setLoadAll(isLoadAll);
    }

    @Override
    public void onLoadMoreRequested() {
        mPresenter.onLoadMore();
    }

    @Override
    public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
        if (mScrollY > 0) {
            return false;
        }
        return PtrDefaultHandler.checkContentCanBePulledDown(frame, mRecyclerView, header);
    }

    @Override
    public void onRefreshBegin(PtrFrameLayout frame) {
        mPresenter.onRefresh(mPatientId);
    }

    @Override
    public void showLoading() {
        if (mLoadingDialog != null) {
            hideLoading();
        }
        mLoadingDialog = LoadingDialog.show(MedicalHistoryActivity.this, "");
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
        if (mPtrLayout != null && mPtrLayout.isRefreshing()) {
            mPtrLayout.refreshComplete();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}
