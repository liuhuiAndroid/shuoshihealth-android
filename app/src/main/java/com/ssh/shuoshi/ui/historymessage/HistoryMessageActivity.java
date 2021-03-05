package com.ssh.shuoshi.ui.historymessage;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.ssh.shuoshi.Constants;
import com.ssh.shuoshi.R;
import com.ssh.shuoshi.bean.ConsultationSummaryBean;
import com.ssh.shuoshi.bean.HisDoctorBean;
import com.ssh.shuoshi.bean.ImageDiagnoseBean;
import com.ssh.shuoshi.bean.history.ImGetHistoryBean;
import com.ssh.shuoshi.bean.history.PayloadBean;
import com.ssh.shuoshi.bean.history.TextBean;
import com.ssh.shuoshi.bean.history.VirtualDomBean;
import com.ssh.shuoshi.bean.team.HisDoctorExpertTeamMemberDtosBean;
import com.ssh.shuoshi.components.storage.UserStorage;
import com.ssh.shuoshi.library.adapter.CommonAdapter;
import com.ssh.shuoshi.library.adapter.MultiItemTypeAdapter;
import com.ssh.shuoshi.library.adapter.base.ItemViewDelegate;
import com.ssh.shuoshi.library.adapter.base.ViewHolder;
import com.ssh.shuoshi.library.adapter.wrapper.LoadMoreWrapper;
import com.ssh.shuoshi.library.widget.MyPtrClassicFrameLayout;
import com.ssh.shuoshi.ui.BaseActivity;
import com.ssh.shuoshi.ui.consultationsummary.ConsultationSummaryActivity;
import com.ssh.shuoshi.ui.dialog.LoadingDialog;
import com.ssh.shuoshi.ui.imagegallery.ImageGalleryActivity;
import com.ssh.shuoshi.ui.medicalhistory.MedicalHistoryActivity;
import com.ssh.shuoshi.ui.myprescription.detail.MyPrescriptionDetailActivity;
import com.ssh.shuoshi.util.DateUtil;
import com.ssh.shuoshi.util.WeekUtil;
import com.tencent.mmkv.MMKV;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import de.hdodenhof.circleimageview.CircleImageView;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;

import static com.ssh.shuoshi.Constants.HEALTH_OPENRXFAIL;
import static com.ssh.shuoshi.Constants.HEALTH_PRESCRIPTIONCARD;
import static com.ssh.shuoshi.Constants.HEALTH_RXAPPROVAL;
import static com.ssh.shuoshi.Constants.HEALTH_RXAPPROVAL2;
import static com.ssh.shuoshi.Constants.HEALTH_SUMMARYCARD;
import static com.ssh.shuoshi.Constants.HEALTH_VIDEO;
import static com.ssh.shuoshi.ui.graphicinquiry.GraphicInquiryActivity.TYPE_GRAPHIC;
import static com.ssh.shuoshi.ui.graphicinquiry.GraphicInquiryActivity.TYPE_GRAPHIC_EXPORT;
import static com.ssh.shuoshi.ui.graphicinquiry.GraphicInquiryActivity.TYPE_VIDEO;

/**
 * 患者消息历史记录
 */
public class HistoryMessageActivity extends BaseActivity implements HistoryMessageContract.View,
        PtrHandler, LoadMoreWrapper.OnLoadMoreListener, View.OnClickListener {

    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.ptr_layout)
    MyPtrClassicFrameLayout mPtrLayout;

    @Inject
    UserStorage mUserStorage;

    @Inject
    HistoryMessagePresenter mPresenter;
    @BindView(R.id.imgBack)
    ImageView imgBack;
    @BindView(R.id.imgPull)
    ImageView imgPull;
    @BindView(R.id.rlHead)
    RelativeLayout rlHead;
    @BindView(R.id.textTeamName)
    TextView textTeamName;
    @BindView(R.id.recyclerViewTeam)
    RecyclerView recyclerViewTeam;
    @BindView(R.id.rlTeam)
    RelativeLayout rlTeam;
    @BindView(R.id.textTitle)
    TextView textTitle;

    private String conversationId;

    private List<ImGetHistoryBean.RowsBean> mData = new ArrayList<>();

    private LoadingDialog mLoadingDialog;

    private MultiItemTypeAdapter<ImGetHistoryBean.RowsBean> mCommonAdapter;
    private LoadMoreWrapper mLoadMoreWrapper;

    private ImageDiagnoseBean.RowsBean rowsBean;

    private ImageDiagnoseBean.RowsBean consultaionInfoBean;

    // TYPE
    private int type;
    private List<String> photoPath = new ArrayList<>();
    //用户头像
    private String mHeadPath;
    //专家团队信息
    private List<HisDoctorExpertTeamMemberDtosBean> teamMemberDtos;
    //专家团队adapter
    private CommonAdapter mTeamAdapter;

    @Override
    public int initContentView() {
        return R.layout.activity_history_message;
    }

    @Override
    protected void initInjector() {
        DaggerHistoryMessageComponent.builder()
                .applicationComponent(getApplicationComponent())
                .activityModule(getActivityModule())
                .build()
                .inject(this);
    }

    @Override
    protected void initUiAndListener() {

        HisDoctorBean doctorInfo = mUserStorage.getDoctorInfo();
        if (doctorInfo.getHeadImg() != null) {
            photoPath.add(doctorInfo.getHeadImg());
        }
        imgBack.setOnClickListener(this);
        mPresenter.attachView(this);
        recyclerViewTeam.setLayoutManager(new GridLayoutManager(this, 4));
        mPtrLayout.setPtrHandler(this);
        //显示时间
        mPtrLayout.setLastUpdateTimeRelateObject(this);
        //禁止下拉
        mPtrLayout.disableWhenHorizontalMove(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        rowsBean = (ImageDiagnoseBean.RowsBean) getIntent().getSerializableExtra("rowsBean");

        conversationId = getIntent().getStringExtra("conversationId");
        mPresenter.onRefresh(conversationId, rowsBean.getId(), photoPath);
        type = getIntent().getIntExtra("type", -1);
        Log.i("TAG", "type" + type);
        if (type == 1 || type == 2) {
            textTitle.setText(rowsBean.getPatientName());
        } else {
            textTitle.setText(rowsBean.getPatientName() + "-团队问诊");
            imgPull.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void showLoading() {
        if (mLoadingDialog != null) {
            hideLoading();
        }
        mLoadingDialog = LoadingDialog.show(HistoryMessageActivity.this, "");
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

    //取出视频时长为0，重复问诊小结、电子处方
    @Override
    public void onRefreshCompleted(ImGetHistoryBean beans, boolean isClear) {
        if (isClear) {
            mData.clear();
        }
        boolean isHavePrescription = false;
        boolean isHaveSummaryCard = false;
        //当前数据，对比已有数据，相同删除
        List<ImGetHistoryBean.RowsBean> rows = beans.getRows();
        Collections.reverse(rows);
        for (int i = 0; i < rows.size(); i++) {
            //取出视频
            VirtualDomBean virtualDom = rows.get(i).getVirtualDom();
            if (virtualDom == null) {
                continue;
            }
            TextBean text = virtualDom.getText();
            if (text == null) {
                continue;
            }
            String key = text.getKey();
            //如果是视频,取出聊天时长为0的元素
            if (key.equals(HEALTH_VIDEO)) {
                if (text.getContent().getAction() == 5) {
                    String duration = text.getContent().getDuration();
                    if (TextUtils.isEmpty(duration)) {
                        rows.remove(i);
                        i--;
                    } else {
                        if (duration.contains(":")) {
                            duration = duration.split(":")[0];
                        }
                        if (Integer.parseInt(duration) == 0) {
                            rows.remove(i);
                            i--;
                        }
                    }
                } else if (text.getContent().getAction() == 0 ||
                        text.getContent().getAction() == 3 ||
                        text.getContent().getAction() == 4 ||
                        text.getContent().getAction() == 6) {
                    rows.remove(i);
                    i--;
                }
            }

            boolean prescription = key.equals(HEALTH_PRESCRIPTIONCARD) || key.equals(HEALTH_OPENRXFAIL) ||
                    key.equals(HEALTH_RXAPPROVAL) || key.equals(HEALTH_RXAPPROVAL2);

            boolean summaryCard = key.equals(HEALTH_SUMMARYCARD);

            if (prescription) {
                if (isHavePrescription) {
                    rows.remove(i);
                    i--;
                }
                isHavePrescription = true;
            }

            if (summaryCard) {
                if (isHaveSummaryCard) {
                    rows.remove(i);
                    i--;
                }
                isHaveSummaryCard = true;
            }

            if (mData == null) {
                continue;
            }
            if (prescription) {
                for (int j = 0; j < mData.size(); j++) {
                    VirtualDomBean virtualDom1 = mData.get(j).getVirtualDom();
                    if (virtualDom1 == null) {
                        continue;
                    }
                    TextBean text1 = virtualDom1.getText();
                    if (text1 == null) {
                        continue;
                    }
                    String key1 = text1.getKey();
                    if (key1.equals(HEALTH_PRESCRIPTIONCARD) || key1.equals(HEALTH_OPENRXFAIL) ||
                            key1.equals(HEALTH_RXAPPROVAL) || key1.equals(HEALTH_RXAPPROVAL2)) {
                        mData.remove(j);
                        j--;
                    }
                }
            }

            if (summaryCard) {
                for (int j = 0; j < mData.size(); j++) {
                    VirtualDomBean virtualDom1 = mData.get(j).getVirtualDom();
                    if (virtualDom1 == null) {
                        continue;
                    }
                    TextBean text1 = virtualDom1.getText();
                    if (text1 == null) {
                        continue;
                    }
                    String key1 = text1.getKey();
                    if (key1.equals(HEALTH_SUMMARYCARD)) {
                        mData.remove(j);
                        j--;
                    }
                }
            }
        }
        Collections.reverse(rows);
        mData.addAll(rows);

        if (mCommonAdapter == null) {
            mCommonAdapter = new MultiItemTypeAdapter(this, mData);
            mCommonAdapter.addItemViewDelegate(new InquiryCardDelagate());
            mCommonAdapter.addItemViewDelegate(new InquiryTipsDelagate());
            mCommonAdapter.addItemViewDelegate(new TIMTextElemDelagate());
            mCommonAdapter.addItemViewDelegate(new TIMImageElemDelagate());
            mCommonAdapter.addItemViewDelegate(new SummaryCardDelagate());
            mCommonAdapter.addItemViewDelegate(new PrescriptionCardDelagate());
            mCommonAdapter.addItemViewDelegate(new RxSuccessTipsDelagate());
            mCommonAdapter.addItemViewDelegate(new OpenRxTipsDelagate());
            mCommonAdapter.addItemViewDelegate(new InquiryOverTipsDelagate());
            mCommonAdapter.addItemViewDelegate(new VideoDelagate());

            // item_history_message
            mLoadMoreWrapper = new LoadMoreWrapper(mCommonAdapter);
            mLoadMoreWrapper.setLoadMoreView(LayoutInflater.from(HistoryMessageActivity.this)
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

    @Override
    public void onLoadCompleted(boolean isLoadAll) {
        mLoadMoreWrapper.setLoadAll(isLoadAll);
    }

    @Override
    public void getConsultationInfoSuccess(ImageDiagnoseBean.RowsBean bean) {
        this.consultaionInfoBean = bean;
        if (type == 3) {
            textTitle.setOnClickListener(this);
            imgPull.setOnClickListener(this);
            rlTeam.setOnClickListener(this);
        }

        //专家团队
        teamMemberDtos = consultaionInfoBean.getHisDoctorExpertTeamMemberDtos();
        if (teamMemberDtos == null) {
            return;
        }

        for (int i = 0; i < teamMemberDtos.size(); i++) {
            if (!TextUtils.isEmpty(teamMemberDtos.get(i).getDoctorHeadImg())) {
                mPresenter.getTeamImagePath(new String[]{teamMemberDtos.get(i).getDoctorHeadImg()}, i);
            }
        }

        textTeamName.setText(consultaionInfoBean.getDoctorExperTeamName() + "(" + teamMemberDtos.size() + "人)");
        mTeamAdapter = new CommonAdapter<HisDoctorExpertTeamMemberDtosBean>(this, R.layout.item_found_team_head, teamMemberDtos) {
            @Override
            protected void convert(ViewHolder holder, final HisDoctorExpertTeamMemberDtosBean bean, int pos) {
                com.tencent.qcloud.tim.uikit.component.CircleImageView imgHead = holder.getView(R.id.ImageDoctorAvatar);

                String doctorBaseImg = bean.getDoctorBaseImg();
                if (!TextUtils.isEmpty(doctorBaseImg)) {
                    Glide.with(HistoryMessageActivity.this).load(doctorBaseImg).into(imgHead);
                } else {
                    Glide.with(HistoryMessageActivity.this).load(getResources().getDrawable(R.drawable.default_img)).into(imgHead);
                }

                TextView textName = holder.getView(R.id.textName);
                textName.setText(bean.getDoctorName());
            }
        };
        recyclerViewTeam.setAdapter(mTeamAdapter);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imgBack:
                finish();
                break;

            case R.id.rlTeam:
                if (rlTeam.getVisibility() == View.VISIBLE) {
                    rlTeam.setVisibility(View.GONE);
                    imgPull.setImageDrawable(getResources().getDrawable(R.drawable.pull_down));
                }
                break;

            //下拉框
            case R.id.textTitle:
            case R.id.imgPull:
                showPopupWindow();
                break;
        }
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private void showPopupWindow() {
        //标题下拉展示
        if (rlTeam.getVisibility() == View.GONE) {
            rlTeam.setVisibility(View.VISIBLE);
            imgPull.setImageDrawable(getResources().getDrawable(R.drawable.pull_up));
        } else {
            rlTeam.setVisibility(View.GONE);
            imgPull.setImageDrawable(getResources().getDrawable(R.drawable.pull_down));
        }
    }

    @Override
    public void getImagePathSuccess(List<String> imgList, ImageView imageView1, ImageView imageView2, ImageView imageView3) {
        if (imgList != null) {
            if (imgList.size() > 0) {
                Glide.with(HistoryMessageActivity.this).load(imgList.get(0)).into(imageView1);
                imageView1.setOnClickListener(v -> {
                    Intent intent = new Intent(HistoryMessageActivity.this, ImageGalleryActivity.class);
                    MMKV.defaultMMKV().putString("imageUrls", new Gson().toJson(imgList));
                    HistoryMessageActivity.this.startActivity(intent);
                });
            }
            if (imgList.size() > 1) {
                Glide.with(HistoryMessageActivity.this).load(imgList.get(1)).into(imageView2);
                imageView2.setOnClickListener(v -> {
                    Intent intent = new Intent(HistoryMessageActivity.this, ImageGalleryActivity.class);
                    MMKV.defaultMMKV().putString("imageUrls", new Gson().toJson(imgList));
                    HistoryMessageActivity.this.startActivity(intent);
                });
            }
            if (imgList.size() > 2) {
                Glide.with(HistoryMessageActivity.this).load(imgList.get(2)).into(imageView3);
                imageView3.setOnClickListener(v -> {
                    Intent intent = new Intent(HistoryMessageActivity.this, ImageGalleryActivity.class);
                    MMKV.defaultMMKV().putString("imageUrls", new Gson().toJson(imgList));
                    HistoryMessageActivity.this.startActivity(intent);
                });
            }
        }
    }

    @Override
    public void getImageSingleSuccess(List<String> imgList, ImageView imageView1) {
        if (imgList != null && imgList.size() > 0) {
            Glide.with(HistoryMessageActivity.this).load(imgList.get(0)).into(imageView1);
            imageView1.setOnClickListener(v -> {
                Intent intent = new Intent(HistoryMessageActivity.this, ImageGalleryActivity.class);
                MMKV.defaultMMKV().putString("imageUrls", new Gson().toJson(imgList));
                startActivity(intent);
            });
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.detachView();
    }

    @Override
    public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
        return PtrDefaultHandler.checkContentCanBePulledDown(frame, mRecyclerView, header);
    }

    @Override
    public void onRefreshBegin(PtrFrameLayout frame) {
        mPresenter.onRefresh(conversationId, consultaionInfoBean.getId(), photoPath);
    }

    @Override
    public void onLoadMoreRequested() {
        mPresenter.onLoadMore();
    }

    /**
     * 问诊卡片
     */
    private class InquiryCardDelagate implements ItemViewDelegate<ImGetHistoryBean.RowsBean> {
        @Override
        public int getItemViewLayoutId() {
            return R.layout.item_inquiry_card;
        }

        @Override
        public boolean isForViewType(ImGetHistoryBean.RowsBean item, int position) {
            return item.getType().equals("TIMCustomElem") && item.getVirtualDom() != null && item.getVirtualDom().getName().equals("custom")
                    && item.getVirtualDom().getText().getKey().equals(Constants.HEALTH_INQUIRYCARD);
        }

        @Override
        public void convert(ViewHolder holder, ImGetHistoryBean.RowsBean rowsBean, int position) {
            boolean out = rowsBean.getFlow().equals("out");
            CircleImageView leftUserIconView = holder.itemView.findViewById(R.id.left_user_icon_view);
            CircleImageView rightUserIconView = holder.itemView.findViewById(R.id.right_user_icon_view);
            TextView textName = holder.itemView.findViewById(R.id.textName);
            holder.itemView.findViewById(R.id.left_user_icon_view)
                    .setVisibility(out ? View.GONE : View.VISIBLE);
            holder.itemView.findViewById(R.id.right_user_icon_view)
                    .setVisibility(out ? View.VISIBLE : View.GONE);
            Glide.with(HistoryMessageActivity.this)
                    .load(R.drawable.default_head)
                    .into(leftUserIconView);
            if (TextUtils.isEmpty(mHeadPath)) {
                Glide.with(HistoryMessageActivity.this)
                        .load(R.drawable.default_head)
                        .into(rightUserIconView);
            } else {
                Glide.with(HistoryMessageActivity.this)
                        .load(mHeadPath)
                        .placeholder(R.drawable.default_head)
                        .into(rightUserIconView);
            }
            if (out) {
                holder.itemView.findViewById(R.id.linearContainer)
                        .setBackground(ContextCompat.getDrawable(HistoryMessageActivity.this, R.drawable.bg_im_message_right_white));
            } else {
                holder.itemView.findViewById(R.id.linearContainer)
                        .setBackground(ContextCompat.getDrawable(HistoryMessageActivity.this, R.drawable.bg_im_message_left));
            }

            ((TextView) holder.itemView.findViewById(R.id.textViewTitle)).setText(
                    "患者向您发起了" + rowsBean.getVirtualDom().getText().getContent().getConsultationTypeName()
            );
            ((TextView) holder.itemView.findViewById(R.id.textViewInfo)).setText(
                    rowsBean.getVirtualDom().getText().getContent().getDetail()
            );

            ((TextView) holder.itemView.findViewById(R.id.textViewDetail))
                    .setText("病情描述：" + consultaionInfoBean.getIllnessDesc());

            textName.setText(consultaionInfoBean.getPatientName());

            if (type == TYPE_GRAPHIC || type == TYPE_GRAPHIC_EXPORT) {
                holder.itemView.findViewById(R.id.textViewTime).setVisibility(View.GONE);
            } else if (type == TYPE_VIDEO) {
                holder.itemView.findViewById(R.id.textViewTime).setVisibility(View.VISIBLE);
                String startTime = consultaionInfoBean.getStartTime();
                String endTime = consultaionInfoBean.getEndTime();
                String timeDate = "";
                String timeWeek = "";
                String timeStart = "";
                String timeEnd = "";
                if (!TextUtils.isEmpty(startTime) && !TextUtils.isEmpty(endTime)) {
                    timeDate = startTime.substring(5, 10);
                    timeWeek = WeekUtil.getWeekOfString(startTime.substring(0, 10));
                    timeStart = startTime.substring(startTime.length() - 5);
                    timeEnd = endTime.substring(endTime.length() - 5);
                }
                ((TextView) holder.itemView.findViewById(R.id.textViewTime))
                        .setText("预约时间：" + timeDate + " " + timeWeek + " " + timeStart + "-" + timeEnd);
            }


            TextView textVisited = holder.itemView.findViewById(R.id.textVisited);
            TextView textDetail = holder.itemView.findViewById(R.id.textDetail);
            ImageView imageView1 = holder.itemView.findViewById(R.id.imageView1);
            ImageView imageView2 = holder.itemView.findViewById(R.id.imageView2);
            ImageView imageView3 = holder.itemView.findViewById(R.id.imageView3);
            LinearLayout llImages = holder.itemView.findViewById(R.id.ll_images);
            LinearLayout llVisited = holder.itemView.findViewById(R.id.ll_visited);

            textDetail.setOnClickListener(v -> {
                Intent intent = new Intent(HistoryMessageActivity.this, MedicalHistoryActivity.class);
                intent.putExtra("patientId", consultaionInfoBean.getPatientId());
                startActivity(intent);
            });


            if (consultaionInfoBean.getVisitedFlag() != null && consultaionInfoBean.getVisitedFlag() == 1) {
                llVisited.setVisibility(View.VISIBLE);
                textVisited.setText("确诊疾病：" + consultaionInfoBean.getVisitHospitalDiag());
            } else {
                llVisited.setVisibility(View.GONE);
            }

            List<String> images = new ArrayList<>();
            if (consultaionInfoBean.getCheckImgs() != null
                    && consultaionInfoBean.getCheckImgs().size() > 0) {
                images.addAll(consultaionInfoBean.getCheckImgs());
            }
            if (!TextUtils.isEmpty(consultaionInfoBean.getFaceImg())) {
                images.add(consultaionInfoBean.getFaceImg());
            }
            if (!TextUtils.isEmpty(consultaionInfoBean.getFurImg())) {
                images.add(consultaionInfoBean.getFurImg());
            }
            if (images.size() > 0) {
                String[] photoPath = images.toArray(new String[images.size()]);
                mPresenter.getImagePath(photoPath, imageView1, imageView2, imageView3,
                        HistoryMessageActivity.this);
                llImages.setVisibility(View.VISIBLE);
            } else {
                llImages.setVisibility(View.GONE);
            }
        }
    }


    /**
     * 温馨提示
     */
    private class InquiryTipsDelagate implements ItemViewDelegate<ImGetHistoryBean.RowsBean> {
        @Override
        public int getItemViewLayoutId() {
            return R.layout.item_inquiry_tips;
        }

        @Override
        public boolean isForViewType(ImGetHistoryBean.RowsBean item, int position) {
            return item.getType().equals("TIMCustomElem") && item.getVirtualDom() != null && item.getVirtualDom().getName().equals("custom")
                    && item.getVirtualDom().getText().getKey().equals(Constants.HEALTH_INQUIRYTIPS);
        }

        @Override
        public void convert(ViewHolder holder, ImGetHistoryBean.RowsBean rowsBean, int position) {
            if (type == TYPE_GRAPHIC || type == TYPE_GRAPHIC_EXPORT) {
                holder.itemView.findViewById(R.id.relativeLayoutContainer).setVisibility(View.GONE);
                holder.itemView.findViewById(R.id.msg_content_ll).setVisibility(View.GONE);
            } else if (type == TYPE_VIDEO) {
                holder.itemView.findViewById(R.id.relativeLayoutContainer).setVisibility(View.VISIBLE);
                holder.itemView.findViewById(R.id.msg_content_ll).setVisibility(View.VISIBLE);
                ((TextView) holder.itemView.findViewById(R.id.tv_text)).setText(
                        "温馨提示：\n· 患者购买了您的视频问诊，请在患者预约时间内与患者联系\n· 如在问诊结束前，您位于患者视频沟通，患者可要求全额退款\n· 点击下方视频，等待患者接入之后即可开始问诊"
                );
            }
        }
    }

    /**
     * 普通文本消息
     */
    private class TIMTextElemDelagate implements ItemViewDelegate<ImGetHistoryBean.RowsBean> {
        @Override
        public int getItemViewLayoutId() {
            return R.layout.item_tim_text_elem;
        }

        @Override
        public boolean isForViewType(ImGetHistoryBean.RowsBean item, int position) {
            return item.getType().equals("TIMTextElem");
        }

        @Override
        public void convert(ViewHolder holder, ImGetHistoryBean.RowsBean rowsBean, int position) {
            boolean out = rowsBean.getFlow().equals("out");
            String from = rowsBean.getFrom();
            CircleImageView leftUserIconView = holder.itemView.findViewById(R.id.left_user_icon_view);
            CircleImageView rightUserIconView = holder.itemView.findViewById(R.id.right_user_icon_view);
            LinearLayout msgContentLl = holder.itemView.findViewById(R.id.msg_content_ll);
            TextView msgBody = holder.itemView.findViewById(R.id.msg_body_tv);

            TextView textName = holder.itemView.findViewById(R.id.textName);

            holder.itemView.findViewById(R.id.left_user_icon_view)
                    .setVisibility(out ? View.INVISIBLE : View.VISIBLE);
            holder.itemView.findViewById(R.id.right_user_icon_view)
                    .setVisibility(out ? View.VISIBLE : View.INVISIBLE);

            textName.setVisibility(out ? View.GONE : View.VISIBLE);
            Glide.with(HistoryMessageActivity.this)
                    .load(R.drawable.default_head)
                    .into(leftUserIconView);
            if (type == TYPE_GRAPHIC || type == TYPE_VIDEO) {
                textName.setText(consultaionInfoBean.getPatientName());
            } else {
                textName.setText("");
                List<HisDoctorExpertTeamMemberDtosBean> memberDtos = consultaionInfoBean.getHisDoctorExpertTeamMemberDtos();
                for (int i = 0; i < memberDtos.size(); i++) {
                    if (memberDtos.get(i).getDoctorNo().equals(from)) {
                        textName.setText(memberDtos.get(i).getDoctorName() + " " + memberDtos.get(i).getTitleName());
                        Glide.with(HistoryMessageActivity.this)
                                .load(memberDtos.get(i).getDoctorBaseImg())
                                .placeholder(R.drawable.default_img)
                                .into(leftUserIconView);
                    }
                }

                if (TextUtils.isEmpty(textName.getText().toString())) {
                    textName.setText(consultaionInfoBean.getPatientName());
                }
            }


            if (TextUtils.isEmpty(mHeadPath)) {
                Glide.with(HistoryMessageActivity.this)
                        .load(R.drawable.default_img)
                        .into(rightUserIconView);
            } else {
                Glide.with(HistoryMessageActivity.this)
                        .load(mHeadPath)
                        .placeholder(R.drawable.default_img)
                        .into(rightUserIconView);
            }

            if (rowsBean.getPayload() != null && rowsBean.getPayload().getText() != null) {
                msgBody.setText(rowsBean.getPayload().getText());
            }
            if (out) {
                msgContentLl.setGravity(Gravity.RIGHT);
                holder.itemView.findViewById(R.id.linearContainer)
                        .setBackground(ContextCompat.getDrawable(HistoryMessageActivity.this, R.drawable.bg_im_message_right));

            } else {
                msgContentLl.setGravity(Gravity.LEFT);
                holder.itemView.findViewById(R.id.linearContainer)
                        .setBackground(ContextCompat.getDrawable(HistoryMessageActivity.this, R.drawable.bg_im_message_left));
            }
        }
    }

    /**
     * 普通图片消息
     */
    private class TIMImageElemDelagate implements ItemViewDelegate<ImGetHistoryBean.RowsBean> {
        @Override
        public int getItemViewLayoutId() {
            return R.layout.item_image_history;
        }

        @Override
        public boolean isForViewType(ImGetHistoryBean.RowsBean item, int position) {
            return item.getType().equals("TIMImageElem");
        }

        @Override
        public void convert(ViewHolder holder, ImGetHistoryBean.RowsBean rowsBean, int position) {

            boolean out = rowsBean.getFlow().equals("out");

            CircleImageView leftUserIconView = holder.itemView.findViewById(R.id.left_user_icon_view);
            CircleImageView rightUserIconView = holder.itemView.findViewById(R.id.right_user_icon_view);

            holder.itemView.findViewById(R.id.left_user_icon_view)
                    .setVisibility(out ? View.INVISIBLE : View.VISIBLE);
            holder.itemView.findViewById(R.id.right_user_icon_view)
                    .setVisibility(out ? View.VISIBLE : View.INVISIBLE);
            TextView textName = holder.itemView.findViewById(R.id.textName);
            textName.setText(consultaionInfoBean.getPatientName());
            Glide.with(HistoryMessageActivity.this)
                    .load(R.drawable.default_head)
                    .into(leftUserIconView);
            if (TextUtils.isEmpty(mHeadPath)) {
                Glide.with(HistoryMessageActivity.this)
                        .load(R.drawable.default_img)
                        .into(rightUserIconView);
            } else {
                Glide.with(HistoryMessageActivity.this)
                        .load(mHeadPath)
                        .placeholder(R.drawable.default_img)
                        .into(rightUserIconView);
            }

            if (rowsBean.getPayload() != null && rowsBean.getPayload().getImageInfoArray() != null
                    && rowsBean.getPayload().getImageInfoArray().size() > 0) {
                PayloadBean.ImageInfoBean imageInfoBean =
                        rowsBean.getPayload().getImageInfoArray().get(0);
                String url = rowsBean.getPayload().getImageInfoArray().get(0).getUrl();

                RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                params.addRule(RelativeLayout.CENTER_IN_PARENT);
                holder.itemView.findViewById(R.id.content_image_iv).setLayoutParams(params);

                holder.itemView.findViewById(R.id.content_image_iv).setLayoutParams(
                        getImageParams(holder.itemView.findViewById(R.id.content_image_iv).getLayoutParams(), imageInfoBean));

                List<String> images = new ArrayList<>();
                images.add(url);
                String[] photoPath = images.toArray(new String[images.size()]);
                mPresenter.getImagePathSingle(photoPath, holder.itemView.findViewById(R.id.content_image_iv));
            }

        }
    }


    private static final int DEFAULT_MAX_SIZE = 540;

    private ViewGroup.LayoutParams getImageParams(ViewGroup.LayoutParams params,
                                                  final PayloadBean.ImageInfoBean msg) {
        if (msg.getWidth() == 0 || msg.getHeight() == 0) {
            return params;
        }
        if (msg.getWidth() > msg.getHeight()) {
            params.width = DEFAULT_MAX_SIZE;
            params.height = DEFAULT_MAX_SIZE * msg.getHeight() / msg.getWidth();
        } else {
            params.width = DEFAULT_MAX_SIZE * msg.getWidth() / msg.getHeight();
            params.height = DEFAULT_MAX_SIZE;
        }
        return params;
    }

    /**
     * 问诊小结卡片
     */
    private class SummaryCardDelagate implements ItemViewDelegate<ImGetHistoryBean.RowsBean> {

        @Override
        public int getItemViewLayoutId() {
            return R.layout.item_summary_card_history;
        }

        @Override
        public boolean isForViewType(ImGetHistoryBean.RowsBean item, int position) {
            return item.getType().equals("TIMCustomElem") && item.getVirtualDom() != null && item.getVirtualDom().getName().equals("custom")
                    && item.getVirtualDom().getText().getKey().equals(HEALTH_SUMMARYCARD);
        }

        @Override
        public void convert(ViewHolder holder, ImGetHistoryBean.RowsBean rowsBean, int position) {
            boolean out = rowsBean.getFlow().equals("out");
            String from = rowsBean.getFrom();
            CircleImageView leftUserIconView = holder.itemView.findViewById(R.id.left_user_icon_view);
            CircleImageView rightUserIconView = holder.itemView.findViewById(R.id.right_user_icon_view);

            holder.itemView.findViewById(R.id.left_user_icon_view)
                    .setVisibility(out ? View.INVISIBLE : View.VISIBLE);
            holder.itemView.findViewById(R.id.right_user_icon_view)
                    .setVisibility(out ? View.VISIBLE : View.INVISIBLE);
            Glide.with(HistoryMessageActivity.this)
                    .load(R.drawable.default_head)
                    .into(leftUserIconView);
            if (TextUtils.isEmpty(mHeadPath)) {
                Glide.with(HistoryMessageActivity.this)
                        .load(R.drawable.default_img)
                        .into(rightUserIconView);
            } else {
                Glide.with(HistoryMessageActivity.this)
                        .load(mHeadPath)
                        .placeholder(R.drawable.default_img)
                        .into(rightUserIconView);
            }

            TextView textName = holder.itemView.findViewById(R.id.textName);
            if (out) {
                textName.setVisibility(View.GONE);
                holder.itemView.findViewById(R.id.linearContainer)
                        .setBackground(ContextCompat.getDrawable(HistoryMessageActivity.this, R.drawable.bg_im_message_right_white));
            } else {

                List<HisDoctorExpertTeamMemberDtosBean> memberDtos = consultaionInfoBean.getHisDoctorExpertTeamMemberDtos();
                if (memberDtos != null) {
                    for (int i = 0; i < memberDtos.size(); i++) {
                        if (memberDtos.get(i).getDoctorNo().equals(from)) {
                            textName.setText(memberDtos.get(i).getDoctorName() + " " + memberDtos.get(i).getTitleName());
                            Glide.with(HistoryMessageActivity.this)
                                    .load(memberDtos.get(i).getDoctorBaseImg())
                                    .placeholder(R.drawable.default_img)
                                    .into(leftUserIconView);
                            break;
                        }
                    }
                }
                holder.itemView.findViewById(R.id.linearContainer)
                        .setBackground(ContextCompat.getDrawable(HistoryMessageActivity.this, R.drawable.bg_im_message_left));
            }
            // 标题
            TextView tvTitle = holder.itemView.findViewById(R.id.message_short_title);
            tvTitle.setText("问诊小结");
            // 就诊人
            TextView tvName = holder.itemView.findViewById(R.id.tv_name);
            tvName.setText(rowsBean.getVirtualDom().getText().getContent().getPatientName());
            // 医师
            TextView tvDiagdesc = holder.itemView.findViewById(R.id.tv_diagdesc);
            tvDiagdesc.setText(rowsBean.getVirtualDom().getText().getContent().getDoctorName());
            // 查看详情
            TextView detail = holder.itemView.findViewById(R.id.message_short_detail);
            detail.setOnClickListener(v -> {
                Intent intent = new Intent(HistoryMessageActivity.this, ConsultationSummaryActivity.class);
                ConsultationSummaryBean cBean = new ConsultationSummaryBean(consultaionInfoBean.getId(),
                        consultaionInfoBean.getPatientName(), consultaionInfoBean.getSexName(), consultaionInfoBean.getPatientAge());
                intent.putExtra("rowsBean", cBean);
                intent.putExtra("emrId", rowsBean.getVirtualDom().getText().getContent().getId());
                intent.putExtra("notChange", true);
                HistoryMessageActivity.this.startActivity(intent);
            });
        }
    }

    /**
     * 电子处方卡片
     */
    private class PrescriptionCardDelagate implements ItemViewDelegate<ImGetHistoryBean.RowsBean> {

        @Override
        public int getItemViewLayoutId() {
            return R.layout.item_prescription_card_history;
        }

        @Override
        public boolean isForViewType(ImGetHistoryBean.RowsBean item, int position) {
            return item.getType().equals("TIMCustomElem") && item.getVirtualDom() != null && item.getVirtualDom().getName().equals("custom")
                    && (item.getVirtualDom().getText().getKey().equals(HEALTH_PRESCRIPTIONCARD)
                    || item.getVirtualDom().getText().getKey().equals(HEALTH_OPENRXFAIL)
                    || item.getVirtualDom().getText().getKey().equals(HEALTH_RXAPPROVAL)
                    || item.getVirtualDom().getText().getKey().equals(HEALTH_RXAPPROVAL2));
        }

        @Override
        public void convert(ViewHolder holder, ImGetHistoryBean.RowsBean rowsBean, int position) {
            boolean out = rowsBean.getFlow().equals("out");
            String from = rowsBean.getFrom();
            CircleImageView leftUserIconView = holder.itemView.findViewById(R.id.left_user_icon_view);
            CircleImageView rightUserIconView = holder.itemView.findViewById(R.id.right_user_icon_view);
            holder.itemView.findViewById(R.id.left_user_icon_view)
                    .setVisibility(out ? View.INVISIBLE : View.VISIBLE);
            holder.itemView.findViewById(R.id.right_user_icon_view)
                    .setVisibility(out ? View.VISIBLE : View.INVISIBLE);
            Glide.with(HistoryMessageActivity.this)
                    .load(R.drawable.default_head)
                    .into(leftUserIconView);
            TextView textName = holder.itemView.findViewById(R.id.textName);
            if (out) {
                textName.setVisibility(View.GONE);
                holder.itemView.findViewById(R.id.linearContainer)
                        .setBackground(ContextCompat.getDrawable(HistoryMessageActivity.this, R.drawable.bg_im_message_right_white));
            } else {
                List<HisDoctorExpertTeamMemberDtosBean> memberDtos = consultaionInfoBean.getHisDoctorExpertTeamMemberDtos();
                if (memberDtos != null) {
                    for (int i = 0; i < memberDtos.size(); i++) {
                        if (memberDtos.get(i).getDoctorNo().equals(from)) {
                            textName.setText(memberDtos.get(i).getDoctorName() + " " + memberDtos.get(i).getTitleName());
                            Glide.with(HistoryMessageActivity.this)
                                    .load(memberDtos.get(i).getDoctorBaseImg())
                                    .placeholder(R.drawable.default_img)
                                    .into(leftUserIconView);
                            break;
                        }
                    }
                }
                holder.itemView.findViewById(R.id.linearContainer)
                        .setBackground(ContextCompat.getDrawable(HistoryMessageActivity.this, R.drawable.bg_im_message_left));
            }

            if (TextUtils.isEmpty(mHeadPath)) {
                Glide.with(HistoryMessageActivity.this)
                        .load(R.drawable.default_img)
                        .into(rightUserIconView);
            } else {
                Glide.with(HistoryMessageActivity.this)
                        .load(mHeadPath)
                        .placeholder(R.drawable.default_img)
                        .into(rightUserIconView);
            }
            // 标题
            TextView tvTitle = holder.itemView.findViewById(R.id.message_short_title);
            tvTitle.setText("Rp.电子处方");
            // 就诊人
            TextView tvName = holder.itemView.findViewById(R.id.tv_name);
            tvName.setText(rowsBean.getVirtualDom().getText().getContent().getPatientName());
            // 医师
            TextView tvDiagdesc = holder.itemView.findViewById(R.id.tv_diagdesc);
            tvDiagdesc.setText(rowsBean.getVirtualDom().getText().getContent().getDoctorName());
            // 查看详情
            TextView detail = holder.itemView.findViewById(R.id.message_short_detail);
            detail.setOnClickListener(v -> {
                Intent intent = new Intent(HistoryMessageActivity.this, MyPrescriptionDetailActivity.class);
                intent.putExtra("prescriptionId", rowsBean.getVirtualDom().getText().getContent().getId());
                startActivity(intent);
            });
        }
    }

    /**
     * 普通的提示信息
     */
    private class OpenRxTipsDelagate implements ItemViewDelegate<ImGetHistoryBean.RowsBean> {

        @Override
        public int getItemViewLayoutId() {
            return R.layout.item_im_open_rx_tips;
        }

        @Override
        public boolean isForViewType(ImGetHistoryBean.RowsBean item, int position) {
            return item.getType().equals("TIMCustomElem") && item.getVirtualDom() != null && item.getVirtualDom().getName().equals("custom")
                    && (item.getVirtualDom().getText().getKey().equals(Constants.HEALTH_OPENRXTIPS)
                    || item.getVirtualDom().getText().getKey().equals(Constants.HEALTH_RECEIVETIPS)
                    || item.getVirtualDom().getText().getKey().equals(Constants.HEALTH_RXSUCCESSTIPS));
        }

        @Override
        public void convert(ViewHolder holder, ImGetHistoryBean.RowsBean rowsBean, int position) {
            if (rowsBean.getVirtualDom().getText().getKey().equals(Constants.HEALTH_OPENRXTIPS)) {
                ((TextView) holder.itemView.findViewById(R.id.tv_text)).setText(
                        rowsBean.getVirtualDom().getText().getContent().getContent()
                );
            } else if (rowsBean.getVirtualDom().getText().getKey().equals(Constants.HEALTH_RECEIVETIPS)) {
                ((TextView) holder.itemView.findViewById(R.id.tv_text)).setText(
                        rowsBean.getVirtualDom().getText().getContent().getContent()
                );
            } else if (rowsBean.getVirtualDom().getText().getKey().equals(Constants.HEALTH_RXSUCCESSTIPS)) {
                ((TextView) holder.itemView.findViewById(R.id.tv_text)).setText(
                        rowsBean.getVirtualDom().getText().getContent().getContent()
                );
            }
        }
    }

    /**
     * 退诊单独写一个View
     */
    private class RxSuccessTipsDelagate implements ItemViewDelegate<ImGetHistoryBean.RowsBean> {

        @Override
        public int getItemViewLayoutId() {
            return R.layout.item_im_open_rx_success_tips;
        }

        @Override
        public boolean isForViewType(ImGetHistoryBean.RowsBean item, int position) {
            return item.getType().equals("TIMCustomElem") && item.getVirtualDom() != null && item.getVirtualDom().getName().equals("custom")
                    && (item.getVirtualDom().getText().getKey().equals(Constants.HEALTH_RETURNTICKETTIPS)
                    || item.getVirtualDom().getText().getKey().equals(Constants.HEALTH_INQUIRYOVERTIMETIPS)
            );
        }

        @Override
        public void convert(ViewHolder holder, ImGetHistoryBean.RowsBean rowsBean, int position) {
            if (rowsBean.getVirtualDom().getText().getKey().equals(Constants.HEALTH_RETURNTICKETTIPS)) {
                ((TextView) holder.itemView.findViewById(R.id.tv_text)).setText(
                        rowsBean.getVirtualDom().getText().getContent().getContent()
                );
            } else if (rowsBean.getVirtualDom().getText().getKey().equals(Constants.HEALTH_INQUIRYOVERTIMETIPS)) {
                ((TextView) holder.itemView.findViewById(R.id.tv_text)).setText(
                        "因超时未接诊，已自动退诊，问诊费用退回患者账户"
                );
            }
        }
    }

    /**
     * Video消息
     */
    private class VideoDelagate implements ItemViewDelegate<ImGetHistoryBean.RowsBean> {

        public int getItemViewLayoutId() {
            return R.layout.item_im_video_history;
        }

        @Override
        public boolean isForViewType(ImGetHistoryBean.RowsBean item, int position) {
            return item.getType().equals("TIMCustomElem") && item.getVirtualDom() != null && item.getVirtualDom().getName().equals("custom")
                    && item.getVirtualDom().getText().getKey().equals(HEALTH_VIDEO);
        }

        @Override
        public void convert(ViewHolder holder, ImGetHistoryBean.RowsBean rowsBean, int position) {
            boolean out = rowsBean.getFlow().equals("out");
            CircleImageView leftUserIconView = holder.itemView.findViewById(R.id.left_user_icon_view);
            CircleImageView rightUserIconView = holder.itemView.findViewById(R.id.right_user_icon_view);

            holder.itemView.findViewById(R.id.left_user_icon_view)
                    .setVisibility(out ? View.INVISIBLE : View.VISIBLE);
            holder.itemView.findViewById(R.id.right_user_icon_view)
                    .setVisibility(out ? View.VISIBLE : View.INVISIBLE);

            TextView textName = holder.itemView.findViewById(R.id.textName);
            textName.setVisibility(out ? View.GONE : View.VISIBLE);
            textName.setText(consultaionInfoBean.getPatientName());
            Glide.with(HistoryMessageActivity.this)
                    .load(R.drawable.default_head)
                    .into(leftUserIconView);
            if (TextUtils.isEmpty(mHeadPath)) {
                Glide.with(HistoryMessageActivity.this)
                        .load(R.drawable.default_img)
                        .into(rightUserIconView);
            } else {
                Glide.with(HistoryMessageActivity.this)
                        .load(mHeadPath)
                        .placeholder(R.drawable.default_img)
                        .into(rightUserIconView);
            }

            if (out) {
                holder.itemView.findViewById(R.id.linearContainer)
                        .setBackground(ContextCompat.getDrawable(HistoryMessageActivity.this, R.drawable.bg_im_message_right_white));
            } else {
                holder.itemView.findViewById(R.id.linearContainer)
                        .setBackground(ContextCompat.getDrawable(HistoryMessageActivity.this, R.drawable.bg_im_message_left));
            }

            if (out) {
                ((LinearLayout) holder.itemView.findViewById(R.id.msg_content_ll))
                        .setGravity(Gravity.RIGHT);
            } else {
                ((LinearLayout) holder.itemView.findViewById(R.id.msg_content_ll))
                        .setGravity(Gravity.LEFT);
            }
            if (rowsBean.getVirtualDom().getText().getContent().getAction() == 1) {
                ((TextView) holder.itemView.findViewById(R.id.tv_text)).setText("取消视频");
            } else if (rowsBean.getVirtualDom().getText().getContent().getAction() == 2) {
                ((TextView) holder.itemView.findViewById(R.id.tv_text)).setText("已拒绝");
            } else if (rowsBean.getVirtualDom().getText().getContent().getAction() == 5) {
                String duration = rowsBean.getVirtualDom().getText().getContent().getDuration();
                if (!TextUtils.isEmpty(duration)) {
                    if (duration.contains(":")) {
                        duration = duration.split(":")[0];
                    }
                    if (rowsBean.getVirtualDom().getText().getKey().equals(HEALTH_VIDEO)) {
                        ((TextView) holder.itemView.findViewById(R.id.tv_text)).setText(
                                "视频聊天时长 " + DateUtil.secToTime(Integer.parseInt(duration))
                        );
                    }
                }
            }

        }
    }


    /**
     * 问诊结束消息
     */
    private class InquiryOverTipsDelagate implements ItemViewDelegate<ImGetHistoryBean.RowsBean> {

        public int getItemViewLayoutId() {
            return R.layout.item_text_short_with_line;
        }

        @Override
        public boolean isForViewType(ImGetHistoryBean.RowsBean item, int position) {
            return item.getType().equals("TIMCustomElem") && item.getVirtualDom() != null && item.getVirtualDom().getName().equals("custom")
                    && item.getVirtualDom().getText().getKey().equals(Constants.HEALTH_INQUIRYOVERTIPS);
        }

        @Override
        public void convert(ViewHolder holder, ImGetHistoryBean.RowsBean rowsBean, int position) {
            if (rowsBean.getVirtualDom().getText().getKey().equals(Constants.HEALTH_INQUIRYOVERTIPS)) {
                ((TextView) holder.itemView.findViewById(R.id.tv_text)).setText("咨询已结束");
            }
        }
    }

    @Override
    public void imgDownload(List<String> bean) {
        if (bean == null || bean.size() == 0) {
            return;
        }
        mHeadPath = bean.get(0);
    }

    @Override
    public void getTeamImagePathSuccess(List<String> imgList, int position) {
        if (mTeamAdapter != null) {
            teamMemberDtos.get(position).setDoctorBaseImg(imgList.get(0));
            mTeamAdapter.notifyItemChanged(position);
        }
    }

}
