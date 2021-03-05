package com.ssh.shuoshi.ui.fragment1;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.gson.Gson;
import com.huawei.hms.aaid.HmsInstanceId;
import com.huawei.hms.common.ApiException;
import com.ssh.shuoshi.Constants;
import com.ssh.shuoshi.R;
import com.ssh.shuoshi.bean.HisDoctorBean;
import com.ssh.shuoshi.bean.IMPushMessageFromWeb;
import com.ssh.shuoshi.bean.ImageDiagnoseBean;
import com.ssh.shuoshi.bean.JPushMessageExtra;
import com.ssh.shuoshi.components.storage.UserStorage;
import com.ssh.shuoshi.eventbus.AuthSuccessEvent;
import com.ssh.shuoshi.eventbus.ChangeStateEvent;
import com.ssh.shuoshi.eventbus.IMLoginSuccessEvent;
import com.ssh.shuoshi.eventbus.NotificationExtras2Event;
import com.ssh.shuoshi.eventbus.RefreshInfoEvent;
import com.ssh.shuoshi.library.adapter.CommonAdapter;
import com.ssh.shuoshi.library.adapter.MultiItemTypeAdapter;
import com.ssh.shuoshi.library.adapter.base.ViewHolder;
import com.ssh.shuoshi.library.adapter.wrapper.LoadMoreWrapper;
import com.ssh.shuoshi.library.util.ThreadManager;
import com.ssh.shuoshi.library.util.ToastUtil;
import com.ssh.shuoshi.library.widget.MyPtrClassicFrameLayout;
import com.ssh.shuoshi.model.TRTCCalling;
import com.ssh.shuoshi.model.impl.TRTCCallingImpl;
import com.ssh.shuoshi.ui.BaseFragment;
import com.ssh.shuoshi.ui.adapter.HomeAdapter;
import com.ssh.shuoshi.ui.addpatient.AddPatientActivity;
import com.ssh.shuoshi.ui.authority.one.AuthorityOneActivity;
import com.ssh.shuoshi.ui.comment.PatientCommentActivity;
import com.ssh.shuoshi.ui.dialog.PhysicianCertificationDialog;
import com.ssh.shuoshi.ui.doctorauthentication.DoctorAuthenticationActivity;
import com.ssh.shuoshi.ui.followup.FollowUpManageActivity;
import com.ssh.shuoshi.ui.graphicinquiry.GraphicInquiryActivity;
import com.ssh.shuoshi.ui.headimg.DoctorHeadActivity;
import com.ssh.shuoshi.ui.imagediagnose.main.ImageDiagnoseActivity;
import com.ssh.shuoshi.ui.login.LoginActivity;
import com.ssh.shuoshi.ui.main.MainComponent;
import com.ssh.shuoshi.ui.messagecenter.MessageCenterActivity;
import com.ssh.shuoshi.ui.patient.PatientManageActivity;
import com.ssh.shuoshi.ui.prescription.template.PrescriptionTemplateActivity;
import com.ssh.shuoshi.ui.videodiagnose.main.VideoDiagnoseActivity;
import com.ssh.shuoshi.ui.web.WebActivity;
import com.ssh.shuoshi.ui.worksetting.WorkSettingActivity;
import com.ssh.shuoshi.util.WeekUtil;
import com.tencent.imsdk.TIMConversation;
import com.tencent.imsdk.TIMConversationType;
import com.tencent.imsdk.TIMManager;
import com.tencent.imsdk.TIMMessage;
import com.tencent.imsdk.TIMMessageListener;
import com.tencent.imsdk.utils.IMFunc;
import com.tencent.imsdk.v2.V2TIMCallback;
import com.tencent.imsdk.v2.V2TIMManager;
import com.tencent.imsdk.v2.V2TIMOfflinePushConfig;
import com.tencent.mmkv.MMKV;
import com.tencent.qcloud.tim.uikit.TUIKit;
import com.tencent.qcloud.tim.uikit.base.IMEventListener;
import com.tencent.qcloud.tim.uikit.component.CircleImageView;
import com.xiaomi.mipush.sdk.MiPushClient;
import com.xiaomi.mipush.sdk.MiPushMessage;
import com.xiaomi.mipush.sdk.PushMessageHelper;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import javax.inject.Inject;

import butterknife.BindView;
import cn.jpush.android.api.JPushInterface;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;
import timber.log.Timber;

import static com.ssh.shuoshi.ToolTm.GenerateTestIMUserSig.SDKAPPID;

public class Fragment1 extends BaseFragment implements Fragment1Contract.View, View.OnClickListener,
        PtrHandler, LoadMoreWrapper.OnLoadMoreListener, TIMMessageListener {

    @BindView(R.id.mImageAvatar)
    CircleImageView mImageAvatar;
    @BindView(R.id.textName)
    TextView textName;
    @BindView(R.id.textDepartment)
    TextView textDepartment;
    @BindView(R.id.textJobTitle)
    TextView textJobTitle;
    @BindView(R.id.textHospitalName)
    TextView textHospitalName;
    @BindView(R.id.textPatientNum)
    TextView textPatientNum;
    @BindView(R.id.textPraiseNum)
    TextView textPraiseNum;

    @BindView(R.id.recyclerViewTab)
    RecyclerView recyclerViewTab;
    @BindView(R.id.recyclerViewPatient)
    RecyclerView recyclerViewPatient;
    @BindView(R.id.textViewMyInfo)
    TextView textViewMyInfo;
    @BindView(R.id.imageViewMessage)
    ImageView imageViewMessage;
    @BindView(R.id.imageViewEdit)
    ImageView imageViewEdit;
    @BindView(R.id.textPatientNumHint)
    TextView textPatientNumHint;
    @BindView(R.id.textPraiseNumHint)
    TextView textPraiseNumHint;
    @BindView(R.id.rl_have_info)
    RelativeLayout rlHaveInfo;
    @BindView(R.id.tv_login)
    TextView tvLogin;
    @BindView(R.id.rl_no_info)
    RelativeLayout rlNoInfo;
    @BindView(R.id.viewDividingLine)
    View viewDividingLine;
    @BindView(R.id.textViewPatientTitle)
    TextView textViewPatientTitle;

    @Inject
    UserStorage mUserStorage;
    @Inject
    Fragment1Presenter mPresenter;
    @BindView(R.id.tv_apply_state)
    TextView tvApplyState;
    @BindView(R.id.rl_no_state)
    RelativeLayout rlNoState;
    @BindView(R.id.ptr_layout)
    MyPtrClassicFrameLayout mPtrLayout;
    @BindView(R.id.nestedSc)
    NestedScrollView mNestedSc;
    @BindView(R.id.textHint)
    TextView textHint;
    @BindView(R.id.textHint2)
    TextView textHint2;
    @BindView(R.id.frameLeft)
    FrameLayout frameLeft;
    @BindView(R.id.frameRight)
    FrameLayout frameRight;

    private LoadMoreWrapper mLoadMoreWrapper;
    private CommonAdapter mCommonAdapter;
    private List<ImageDiagnoseBean.RowsBean> mData = new ArrayList<>();


    private String[] moduleName = new String[]{
            "问诊管理", "随访管理", "患者管理", "邀请患者", "处方模版", "工作室设置",
    };

    private int[] moduleIcon = new int[]{
            R.drawable.home_tu, R.drawable.home_three, R.drawable.home_four,
            R.drawable.home_add, R.drawable.home_six, R.drawable.work_setting
    };
    private HisDoctorBean doctorInfo;
    private int mScrollY;
    //用户头像
    private String mHeadPath;

    public static BaseFragment newInstance() {
        return new Fragment1();
    }

    @Override
    public void initInjector() {
        getComponent(MainComponent.class).inject(this);
    }

    @Override
    public int initContentView() {
        return R.layout.fragment_1;
    }

    @Override
    public void onSupportVisible() {
        super.onSupportVisible();
        doctorInfo = mUserStorage.getDoctorInfo();
        if (TextUtils.isEmpty(mUserStorage.getToken())) {
            rlNoInfo.setVisibility(View.VISIBLE);
            rlHaveInfo.setVisibility(View.GONE);
            rlNoState.setVisibility(View.GONE);
        }
        if (!TextUtils.isEmpty(mUserStorage.getToken()) && doctorInfo != null) {
            changeState();
            mPresenter.onRefresh();
        }
    }

    @Override
    public void getBundle(Bundle bundle) {

    }

    @SuppressLint("WrongConstant")
    @Override
    public void initUI(View view) {
        EventBus.getDefault().register(this);
        mPresenter.attachView(this);
        mPtrLayout.setPtrHandler(this);
        //显示时间
        mPtrLayout.setLastUpdateTimeRelateObject(this);
        //禁止下拉
        mPtrLayout.disableWhenHorizontalMove(true);

        recyclerViewPatient.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayout.VERTICAL, false));
        if (TextUtils.isEmpty(mUserStorage.getToken())) {
            rlNoInfo.setVisibility(View.VISIBLE);
            rlHaveInfo.setVisibility(View.GONE);
            rlNoState.setVisibility(View.GONE);
            textViewPatientTitle.setVisibility(View.GONE);
            viewDividingLine.setVisibility(View.GONE);
        }

        if (!TextUtils.isEmpty(mUserStorage.getToken())) {
            mPresenter.getDoctorInfo();
            mPresenter.onRefresh();
        }

        rlNoInfo.setOnClickListener(this);
        rlNoState.setOnClickListener(this);
        textPraiseNumHint.setOnClickListener(this);
        textPraiseNum.setOnClickListener(this);
        imageViewEdit.setOnClickListener(this);
        mImageAvatar.setOnClickListener(this);
        frameLeft.setOnClickListener(this);
        frameRight.setOnClickListener(this);
        initRecyclerViewTab();

        mNestedSc.setOnScrollChangeListener((NestedScrollView.OnScrollChangeListener)
                (v, scrollX, scrollY, oldScrollX, oldScrollY) -> mScrollY = scrollY);
        imageViewMessage.setOnClickListener(this);
    }


    /**
     * 点击跳转聊天挪到这来了，是因为需要登录！！！
     *
     * @param intent
     */
    private void initPush(Intent intent) {
        // 腾讯即时通讯IM推送
        Log.i("initPush", "initPush:-------1 intent.getExtras:" + intent.getExtras());
        if (intent.getExtras() != null) {
            String extValue = "";
            if (IMFunc.isBrandXiaoMi()) {
                Bundle bundle = intent.getExtras();
                MiPushMessage miPushMessage = (MiPushMessage) bundle.getSerializable(PushMessageHelper.KEY_MESSAGE);
                if (miPushMessage != null) {
                    Map extra = miPushMessage.getExtra();
                    if (extra != null) {
                        extValue = (String) extra.get("ext");
                    }
                }
            } else {
                Bundle bundle = intent.getExtras();
                extValue = bundle.getString("ext");
            }
            Log.i("initPush", "initPush:-------1 intent.extValue:" + extValue);
            if (!TextUtils.isEmpty(extValue)) {
                Log.i("initPush", "initPush:------- start 001");
                IMPushMessageFromWeb imPushMessageFromWeb = new Gson().fromJson(extValue, IMPushMessageFromWeb.class);
                if (!TextUtils.isEmpty(imPushMessageFromWeb.getGroupId())) {
                    Log.i("initPush", "initPush:------- start 002");
                    int currentId = Integer.parseInt(imPushMessageFromWeb.getGroupId().split("_")[1]);
                    if (currentId != GraphicInquiryActivity.mId) {
                        Log.i("initPush", "initPush:------- start 003");
                        Intent newIntent = new Intent(getActivity(), GraphicInquiryActivity.class);
                        ImageDiagnoseBean.RowsBean rowsBean = new ImageDiagnoseBean.RowsBean();
                        rowsBean.setId(currentId);
                        newIntent.putExtra("rowsBean", rowsBean);
                        startActivity(newIntent);
                        Log.i("initPush", "initPush:------- start 003");
                    }
                }
            }
        }

        // 极光推送点击事件处理
        String data = null;
        if (intent.getData() != null) {
            data = intent.getData().toString();
            Log.i("initPush", "initPush:-------2 intent.data:" + data);
        }
        //获取fcm、小米、oppo、vivo平台附带的jpush信息
        if (TextUtils.isEmpty(data) && intent.getExtras() != null) {
            data = intent.getExtras().getString("JMessageExtra");
        }
        if (TextUtils.isEmpty(data)) return;
        try {
            Log.i("initPush", "initPush:-------2 开始解析");
            JPushMessageExtra jPushMessageExtra = new Gson().fromJson(data, JPushMessageExtra.class);
            JPushMessageExtra.NExtrasBean n_extras = jPushMessageExtra.getN_extras();
            if (n_extras != null) {
                Log.i("initPush", "initPush:-------2 n_extras not null");
                JPushMessageExtra.NExtrasBean.ExtraBeanBean extraBean = n_extras.getExtraBean();
                if (extraBean != null) {
                    Log.i("initPush", "initPush:-------2 extraBean not null");
                    jumpFromJPush(extraBean.getMsgType(), extraBean.getBussinessId());
                }
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    private void jumpFromJPush(int msgType, Integer bussinessId) {
        if (msgType == 5) {
            // 点击PUSH的消息通知，进入与该患者的咨询对话页面。
            Log.i("initPush", "initPush:-------2 msgType == 5");
            mPresenter.getConsultationInfoJpush(bussinessId);
        } else if (msgType == 6) {
            // 点击PUSH的消息通知，进入与该患者的咨询对话页面。
            mPresenter.getConsultationInfoJpush(bussinessId);
        }
    }

    private void changeState() {
        if (doctorInfo.getApprovalState() == 0) {
            tvApplyState.setText("请进行资质认证");
            textHint.setText("立即开启您的线上工作室吧～");
            rlNoState.setVisibility(View.VISIBLE);
            rlNoInfo.setVisibility(View.GONE);
            rlHaveInfo.setVisibility(View.GONE);
        } else if (doctorInfo.getApprovalState() == 1) {
            tvApplyState.setText("认证审核中 ");
            textHint.setText("请耐心等待资质认证审核哦~");
            rlNoState.setVisibility(View.VISIBLE);
            rlNoInfo.setVisibility(View.GONE);
            rlHaveInfo.setVisibility(View.GONE);
        } else if (doctorInfo.getApprovalState() == 3) {
            tvApplyState.setText("认证审核不通过");
            textHint.setText("请修改资料后重新提交");
            rlNoState.setVisibility(View.VISIBLE);
            rlNoInfo.setVisibility(View.GONE);
            rlHaveInfo.setVisibility(View.GONE);
        } else {
            rlNoState.setVisibility(View.GONE);
            rlNoInfo.setVisibility(View.GONE);
            rlHaveInfo.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_no_info:
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);
                break;

            case R.id.rl_no_state:
                if (doctorInfo.getApprovalState() == 0 || doctorInfo.getApprovalState() == 3) {
                    showPhysicianCertificationDialog(doctorInfo.getApprovalState());
                }
                break;

            //修改头像
            case R.id.mImageAvatar:
                if (eachHandle()) {
                    return;
                }
                Intent intent1 = new Intent(getActivity(), DoctorHeadActivity.class);
                if (TextUtils.isEmpty(mHeadPath)) {
                    intent1.putExtra("headPath", false);
                } else {
                    intent1.putExtra("headPath", true);
                }
                startActivityForResult(intent1, 200);
                break;

            case R.id.imageViewMessage:
                if (eachHandle()) {
                    return;
                }
                startActivity(new Intent(getActivity(), MessageCenterActivity.class));
                break;

            case R.id.imageViewEdit:
                if (eachHandle()) {
                    return;
                }
                startActivity(new Intent(getActivity(), DoctorAuthenticationActivity.class));
                break;

            case R.id.textPraiseNumHint:
            case R.id.textPraiseNum:
                startActivity(new Intent(getActivity(), PatientCommentActivity.class));
                break;

            case R.id.frameLeft:
                Intent intent3 = new Intent(getActivity(), WebActivity.class);
                intent3.putExtra("url", Constants.WEB_004);
                startActivity(intent3);
                break;

            case R.id.frameRight:
                Intent intent4 = new Intent(getActivity(), WebActivity.class);
                intent4.putExtra("url", Constants.WEB_005);
                startActivity(intent4);
                break;
        }
    }

    //针对不同状态做处理
    private boolean eachHandle() {
        if (TextUtils.isEmpty(mUserStorage.getToken())) {
            Intent intent = new Intent(getActivity(), LoginActivity.class);
            startActivity(intent);
            return true;
        }

        if (doctorInfo.getApprovalState() == 0 || doctorInfo.getApprovalState() == 3) {
            showPhysicianCertificationDialog(doctorInfo.getApprovalState());
            return true;
        }

        if (doctorInfo.getApprovalState() == 1) {
            ToastUtil.showToast("您提交的资质认证材料，正在审核中，请耐心等耐。");
            return true;
        }

        return false;
    }

    /**
     * 显示医师认证弹框
     */
    private void showPhysicianCertificationDialog(int approvalState) {
        PhysicianCertificationDialog dialog = new PhysicianCertificationDialog(
                getActivity(), R.style.dialog_physician_certification);

        dialog.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        dialog.getWindow().getDecorView().setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener() {
            @Override
            public void onSystemUiVisibilityChange(int visibility) {
                int uiOptions = View.SYSTEM_UI_FLAG_LAYOUT_STABLE |
                        //布局位于状态栏下方
                        View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION |
                        //全屏
                        View.SYSTEM_UI_FLAG_FULLSCREEN |
                        //隐藏导航栏
                        View.SYSTEM_UI_FLAG_HIDE_NAVIGATION |
                        View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN;
                uiOptions |= 0x00001000;
                dialog.getWindow().getDecorView().setSystemUiVisibility(uiOptions);
            }
        });

        dialog.setBtnGoClick(v -> {
            Intent intent = new Intent(getActivity(), AuthorityOneActivity.class);
            if (approvalState == 3) {
                intent.putExtra("isShow", true);
            }
            startActivity(intent);
            dialog.dismiss();

        });
        dialog.show();
    }

    @Override
    public void getDoctorInfoSuccess(HisDoctorBean bean) {

        Log.i("GraphTest", "getUserSigByUserNo for login");
        mPresenter.getUserSigByUserNo(bean.getDoctorNo(), bean);
    }

    @Override
    public void onRefreshCompleted(ImageDiagnoseBean beans, boolean isClear) {
        if (isClear) {
            mData.clear();
        }
        mData.addAll(beans.getRows());
        if (doctorInfo.getApprovalState() != 2) {
            recyclerViewPatient.setVisibility(View.GONE);
            textViewPatientTitle.setVisibility(View.GONE);
            viewDividingLine.setVisibility(View.GONE);
        } else {
            recyclerViewPatient.setVisibility(View.VISIBLE);
            if (mData.size() > 0) {
                textViewPatientTitle.setVisibility(View.VISIBLE);
                viewDividingLine.setVisibility(View.VISIBLE);
            } else {
                textViewPatientTitle.setVisibility(View.GONE);
                viewDividingLine.setVisibility(View.GONE);
            }
        }
        if (mCommonAdapter == null) {
            mCommonAdapter = new CommonAdapter<ImageDiagnoseBean.RowsBean>(getActivity(), R.layout.fragment_one_item, mData) {
                @Override
                protected void convert(ViewHolder holder, final ImageDiagnoseBean.RowsBean bean, int position) {
                    //获取会话扩展实例
                    TIMConversation con = TIMManager.getInstance().getConversation(TIMConversationType.Group, bean.getGroupId());
                    //获取会话未读数
                    long num = con.getUnreadMessageNum();
                    TextView tvNum = holder.getView(R.id.tv_num);
                    if (num > 0) {
                        tvNum.setText(num + "");
                        tvNum.setVisibility(View.VISIBLE);
                    } else {
                        tvNum.setVisibility(View.GONE);
                    }
                    ImageView ivHead = holder.getView(R.id.iv_header);
                    View viewBottom = holder.getView(R.id.view_bottom);
                    TextView tvName = holder.getView(R.id.tv_name);
                    TextView tvState = holder.getView(R.id.tv_state);
                    tvState.setVisibility(View.VISIBLE);
                    TextView tvSex = holder.getView(R.id.tv_sex);
                    TextView tvAge = holder.getView(R.id.tv_age);
                    TextView tvType = holder.getView(R.id.tv_type);
                    TextView tvDescribe = holder.getView(R.id.tv_describe);
                    TextView tvSubscribeTime = holder.getView(R.id.tv_subscribe_time);
                    TextView tvTime = holder.getView(R.id.tv_time);
                    switch (bean.getState()) {
                        case 0:
                            tvState.setText("待支付");
                            break;
                        case 1:
                            tvState.setText("待接诊");
                            break;
                        case 2:
                            tvState.setText("咨询中");
                            break;
                        case 3:
                            tvState.setText("已完成");
                            break;
                        case 4:
                            tvState.setText("已退款");
                            break;
                        case 5:
                            tvState.setText("退诊中");
                            break;
                        case 6:
                            tvState.setText("已退诊");
                            break;

                    }

                    if (position == mData.size() - 1) {
                        viewBottom.setVisibility(View.GONE);
                    } else {
                        viewBottom.setVisibility(View.VISIBLE);
                    }

                    tvName.setText(bean.getPatientName());
                    tvSex.setText(bean.getSexName());
                    tvAge.setText(bean.getPatientAge() + "岁");
                    tvDescribe.setText("病情描述: " + bean.getIllnessDesc());

                    if (bean.getConsultationTypeId() == 1) {
                        // 图文问诊
                        tvType.setText("图文问诊");
                        tvType.setTextColor(Color.parseColor("#FFFF824D"));
                        tvType.setBackground(getResources().getDrawable(R.drawable.all_orange_round));
                    } else if (bean.getConsultationTypeId() == 2) {
                        // 视频问诊
                        tvType.setText("视频问诊");
                        tvType.setTextColor(Color.parseColor("#FF60B2FF"));
                        tvType.setBackground(getResources().getDrawable(R.drawable.all_orange_round_3));
                    } else if (bean.getConsultationTypeId() == 3) {
                        // 团队问诊
                        tvType.setText("团队问诊");
                        tvType.setTextColor(Color.parseColor("#FF34D386"));
                        tvType.setBackground(getResources().getDrawable(R.drawable.all_orange_round_2));
                    }

                    if (bean.getConsultationTypeId() == 2) {
                        tvSubscribeTime.setVisibility(View.VISIBLE);
                        String startTime = bean.getStartTime();
                        String endTime = bean.getEndTime();
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
                        tvSubscribeTime.setText("预约时间：" + timeDate + " " + timeWeek + " " + timeStart + "-" + timeEnd);
                        tvTime.setText("");
                    } else {
                        //图文，专家图文
                        tvTime.setVisibility(View.VISIBLE);
                        tvTime.setText(bean.getPayTime());
                        tvSubscribeTime.setText(" ");
                    }
                }
            };

            mCommonAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                    Intent intent = new Intent(getActivity(), GraphicInquiryActivity.class);
                    ImageDiagnoseBean.RowsBean rowsBean = mData.get(position);
                    intent.putExtra("rowsBean", rowsBean);
                    if (rowsBean.getConsultationTypeId() == 1) {
                        intent.putExtra("type", GraphicInquiryActivity.TYPE_GRAPHIC);
                    } else if (rowsBean.getConsultationTypeId() == 2) {
                        intent.putExtra("type", GraphicInquiryActivity.TYPE_VIDEO);
                    } else if (rowsBean.getConsultationTypeId() == 3) {
                        intent.putExtra("type", GraphicInquiryActivity.TYPE_GRAPHIC_EXPORT);
                    } else if (rowsBean.getConsultationTypeId() == 4) {
                        intent.putExtra("type", GraphicInquiryActivity.TYPE_VIDEO_EXPORT);
                    }
                    startActivity(intent);
                }

                @Override
                public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                    return false;
                }
            });

            mLoadMoreWrapper = new LoadMoreWrapper(mCommonAdapter);
            mLoadMoreWrapper.setLoadMoreView(LayoutInflater.from(getActivity())
                    .inflate(R.layout.footer_view_load_more, recyclerViewPatient, false));
            mLoadMoreWrapper.setOnLoadMoreListener(this);

            recyclerViewPatient.setAdapter(mLoadMoreWrapper);
        } else {
            if (recyclerViewPatient.getScrollState() == RecyclerView.SCROLL_STATE_IDLE || (!recyclerViewPatient.isComputingLayout())) {
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

    private void initRecyclerViewTab() {
        recyclerViewTab.setLayoutManager(new GridLayoutManager(getActivity(), 4));
        HomeAdapter mAdapter = new HomeAdapter(getActivity(), moduleName, moduleIcon);
        recyclerViewTab.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener((view1, position) -> {
            if (TextUtils.isEmpty(mUserStorage.getToken())) {
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);
                return;
            }

            if (doctorInfo.getApprovalState() == 0 || doctorInfo.getApprovalState() == 3) {
                showPhysicianCertificationDialog(doctorInfo.getApprovalState());
                return;
            }

            if (doctorInfo.getApprovalState() == 1) {
                ToastUtil.showToast("您提交的资质认证材料，正在审核中，请耐心等耐。");
                return;
            }

            switch (position) {
                //图文问诊
                case 0:
                    startActivity(new Intent(getActivity(), ImageDiagnoseActivity.class));
                    // startActivity(new Intent(getActivity(), VideoDiagnoseActivity.class));
                    break;
                //随访管理
                case 1:
                    startActivity(new Intent(getActivity(), FollowUpManageActivity.class));
                    break;
                //患者管理
                case 2:
                    startActivity(new Intent(getActivity(), PatientManageActivity.class));
                    break;
                //添加患者
                case 3:
                    startActivity(new Intent(getActivity(), AddPatientActivity.class));
                    break;
                //处方模版
                case 4:
                    startActivity(new Intent(getActivity(), PrescriptionTemplateActivity.class));
                    break;
                //工作室设置
                case 5:
                    startActivity(new Intent(getActivity(), WorkSettingActivity.class));
                    break;
                default:
                    break;
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case 200:
                    mPresenter.getDoctorInfo();
                    EventBus.getDefault().post(new RefreshInfoEvent());
                    break;
            }
        }
    }

    @Override
    public void onError(Throwable throwable, int type) {
        loadError(throwable);
        if (mPtrLayout != null && mPtrLayout.isRefreshing()) {
            mPtrLayout.refreshComplete();
        }
    }

    @Override
    public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
        if (mScrollY > 0) {
            return false;
        }
        return PtrDefaultHandler.checkContentCanBePulledDown(frame, recyclerViewPatient, header);
    }

    @Override
    public void onRefreshBegin(PtrFrameLayout frame) {
        if (!TextUtils.isEmpty(mUserStorage.getToken()) && doctorInfo != null) {
            Log.i("GraphTest", "getDoctorInfo for login");
            mPresenter.onRefresh();
            mPresenter.getDoctorInfo();
        } else {
            mPtrLayout.refreshComplete();
        }
    }

    @Override
    public void onLoadMoreRequested() {
        mPresenter.onLoadMore();
    }


    @Override
    public void initData() {

    }

    @Override
    public void getUserSigByUserNoSuccess(String userSig, HisDoctorBean bean2) {
        Log.i("GraphTest", "getUserSigByUserNoSuccess for login");
        MMKV.defaultMMKV().putString("userId", doctorInfo.getDoctorNo());
        MMKV.defaultMMKV().putString("userSig", userSig);

        int loginStatus = V2TIMManager.getInstance().getLoginStatus();
        if (loginStatus == V2TIMManager.V2TIM_STATUS_LOGOUT) {
            TRTCCallingImpl.sharedInstance(getActivity())
                    .login(SDKAPPID, doctorInfo.getDoctorNo(), userSig, new TRTCCalling.ActionCallBack() {
                        @Override
                        public void onError(int i, String s) {
                            // 登录腾讯组件失败
                            ToastUtil.showToast("登录腾讯组件失败，所有功能不可用[" + i + "]" + s);
                        }

                        @Override
                        public void onSuccess() {
                            initPush(getActivity().getIntent());
                            EventBus.getDefault().post(new IMLoginSuccessEvent());
                            JPushInterface.setAlias(getActivity(),
                                    Integer.parseInt((System.currentTimeMillis() + "").substring(5)),
                                    doctorInfo.getId() + "");
                            // 登录腾讯组件成功
                            Timber.i("登录腾讯组件成功");
                            setDoctorInfoAndRefreshUI(bean2);
                            initIMListener();

                            // 注册小米
                            ThreadManager.getInstance().createShortPool().execute(() -> {
                                // 将证书 ID 和 Push Token 上报到即时通信 IM 服务端。
                                MiPushClient.registerPush(getActivity(), Constants.XIAOMI_APPID, Constants.XIAOMI_APPKEY);
                            });
                            if (IMFunc.isBrandHuawei()) {
                                // 即时通讯IM 华为推送 集成推送SDK
                                ThreadManager.getInstance().createShortPool().execute(() -> {
                                    try {
                                        // 向服务端请求应用的唯一标识 Push Token，Push Token 为当前设备上当前 App 的唯一标识
                                        String token = HmsInstanceId.getInstance(getActivity()).getToken(Constants.HUAWEI_APPID, "HCM");
                                        if (!TextUtils.isEmpty(token)) {
                                            // 将证书 ID 和 Push Token 上报到即时通信 IM 服务端。
                                            V2TIMManager.getOfflinePushManager()
                                                    .setOfflinePushConfig(new V2TIMOfflinePushConfig(Constants.HUAWEI_BUSINESSID, token),
                                                            new V2TIMCallback() {
                                                                @Override
                                                                public void onError(int code, String desc) {
                                                                    Log.d("TPush", "注册即时通讯IM 华为推送 onError");
                                                                }

                                                                @Override
                                                                public void onSuccess() {
                                                                    Log.d("TPush", "注册即时通讯IM 华为推送 onSuccess");
                                                                }
                                                            });
                                        }
                                    } catch (ApiException e) {
                                        e.printStackTrace();
                                    }
                                });

                            }

                        }

                    });
        } else {
            setDoctorInfoAndRefreshUI(bean2);
        }
    }

    private void setDoctorInfoAndRefreshUI(HisDoctorBean bean) {
        doctorInfo = mUserStorage.getDoctorInfo();
        if (doctorInfo.getApprovalState() != 2) {
            recyclerViewPatient.setVisibility(View.GONE);
            textViewPatientTitle.setVisibility(View.GONE);
            viewDividingLine.setVisibility(View.GONE);
        } else {
            recyclerViewPatient.setVisibility(View.VISIBLE);
            if (mData.size() > 0) {
                textViewPatientTitle.setVisibility(View.VISIBLE);
                viewDividingLine.setVisibility(View.VISIBLE);
            } else {
                textViewPatientTitle.setVisibility(View.GONE);
                viewDividingLine.setVisibility(View.GONE);
            }
        }
        if (doctorInfo != null) {
            changeState();
        }
        if (bean == null) {
            return;
        }
        if (bean.getHeadImg() != null) {
            String[] photoPath = new String[]{bean.getHeadImg()};
            mPresenter.getImagePath(photoPath);
        }
        textName.setText(bean.getName());
        textDepartment.setText(bean.getHisSysDeptName());
        textJobTitle.setText(bean.getDoctorTitleName());
        textHospitalName.setText(bean.getHospitalName());
        textPatientNum.setText(bean.getPatientCount() + "");
        textPraiseNum.setText(bean.getPraiseRate());
    }

    @Override
    public void imgDownload(List<String> bean) {
        if (bean == null || bean.size() == 0) {
            return;
        }
        mHeadPath = bean.get(0);
        Glide.with(Objects.requireNonNull(getActivity())).load(bean.get(0))
                .placeholder(getResources().getDrawable(R.drawable.default_img))
                .skipMemoryCache(true).diskCacheStrategy(DiskCacheStrategy.NONE).into(mImageAvatar);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventRate(RefreshInfoEvent event) {
        mPresenter.getDoctorInfo();
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventRate(AuthSuccessEvent event) {
        mPresenter.getDoctorInfo();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventChange(ChangeStateEvent event) {
        Glide.with(Objects.requireNonNull(getActivity())).load(getResources().getDrawable(R.drawable.default_img))
                .skipMemoryCache(true).diskCacheStrategy(DiskCacheStrategy.NONE).into(mImageAvatar);
        recyclerViewPatient.setVisibility(View.GONE);
        textViewPatientTitle.setVisibility(View.GONE);
    }

    @Override
    public void getConsultationInfoSuccess(ImageDiagnoseBean.RowsBean rowsBean) {

    }

    private void initIMListener() {
        TUIKit.addIMEventListener(currentIMEventListener);

        TIMManager.getInstance().addMessageListener(this);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        TUIKit.removeIMEventListener(currentIMEventListener);

        TIMManager.getInstance().removeMessageListener(this);
    }

    private IMEventListener currentIMEventListener = new IMEventListener() {
        @Override
        public void onForceOffline() {
            super.onForceOffline();
            reLogin();
        }

        @Override
        public void onUserSigExpired() {
            super.onUserSigExpired();
            reLogin();
        }
    };

    private void reLogin() {
        mUserStorage.logout();
        MMKV.defaultMMKV().putBoolean("firstLogin", false);
        JPushInterface.deleteAlias(getContext(),
                Integer.parseInt((System.currentTimeMillis() + "").substring(5)));
        ToastUtil.showToast("用户已在其他设备上登录。");
        Intent intent = new Intent(getActivity(), LoginActivity.class);
        startActivity(intent);
    }

    @Override
    public boolean onNewMessages(List<TIMMessage> msgs) {
        if (msgs.size() > 0) {
            String peer = msgs.get(0).getConversation().getPeer();
            for (int i = 0; i < mData.size(); i++) {
                if (mData.get(i).getGroupId().equals(peer)) {
                    mLoadMoreWrapper.notifyItemChanged(i);
                }
            }
        }
        return false;
    }

    @Override
    public void getConsultationInfoJpushSuccess(ImageDiagnoseBean.RowsBean rowsBean, int mId) {
        Intent intent = new Intent(getActivity(), GraphicInquiryActivity.class);
        intent.putExtra("rowsBean", rowsBean);
        if (rowsBean.getConsultationTypeId() == 1) {
            intent.putExtra("type", GraphicInquiryActivity.TYPE_GRAPHIC);
        } else if (rowsBean.getConsultationTypeId() == 2) {
            intent.putExtra("type", GraphicInquiryActivity.TYPE_VIDEO);
        } else if (rowsBean.getConsultationTypeId() == 3) {
            intent.putExtra("type", GraphicInquiryActivity.TYPE_GRAPHIC_EXPORT);
        } else if (rowsBean.getConsultationTypeId() == 4) {
            intent.putExtra("type", GraphicInquiryActivity.TYPE_VIDEO_EXPORT);
        }
        startActivity(intent);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventRate(NotificationExtras2Event event) {
        getActivity().runOnUiThread(() -> {
            jumpFromJPush(event.getMsgType(), event.getBussinessId());
        });
    }

}
