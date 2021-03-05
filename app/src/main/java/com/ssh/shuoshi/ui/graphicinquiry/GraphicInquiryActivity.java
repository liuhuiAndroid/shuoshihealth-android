package com.ssh.shuoshi.ui.graphicinquiry;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.lzf.easyfloat.EasyFloat;
import com.permissionx.guolindev.PermissionX;
import com.ssh.shuoshi.Constants;
import com.ssh.shuoshi.R;
import com.ssh.shuoshi.ToolTm.CustomMessageShortView;
import com.ssh.shuoshi.bean.ConsultationSummaryBean;
import com.ssh.shuoshi.bean.HisDoctorBean;
import com.ssh.shuoshi.bean.IMBaseMessage;
import com.ssh.shuoshi.bean.IMQuestionMessage;
import com.ssh.shuoshi.bean.IMTipMessage;
import com.ssh.shuoshi.bean.IMVideoMessage;
import com.ssh.shuoshi.bean.ImageDiagnoseBean;
import com.ssh.shuoshi.bean.TipExtra;
import com.ssh.shuoshi.bean.common.SystemTypeBean;
import com.ssh.shuoshi.bean.im.RecipeCardBean;
import com.ssh.shuoshi.bean.im.SummaryCardBean;
import com.ssh.shuoshi.bean.im.TeamPersonBean;
import com.ssh.shuoshi.bean.team.HisDoctorExpertTeamMemberDtosBean;
import com.ssh.shuoshi.components.storage.UserStorage;
import com.ssh.shuoshi.eventbus.ChatEvent;
import com.ssh.shuoshi.eventbus.IMLoginSuccessEvent;
import com.ssh.shuoshi.library.adapter.CommonAdapter;
import com.ssh.shuoshi.library.adapter.base.ViewHolder;
import com.ssh.shuoshi.model.TRTCCalling;
import com.ssh.shuoshi.model.impl.TRTCCallingImpl;
import com.ssh.shuoshi.ui.BaseActivity;
import com.ssh.shuoshi.ui.consultationsummary.ConsultationSummaryActivity;
import com.ssh.shuoshi.ui.dialog.ChooseRecipeDialog;
import com.ssh.shuoshi.ui.dialog.CommonDialog;
import com.ssh.shuoshi.ui.imagegallery.ImageGalleryActivity;
import com.ssh.shuoshi.ui.medicalhistory.MedicalHistoryActivity;
import com.ssh.shuoshi.ui.myprescription.detail.MyPrescriptionDetailActivity;
import com.ssh.shuoshi.ui.prescription.chinesemedicine.edit.EditChineseMedicinePrescriptionActivity;
import com.ssh.shuoshi.ui.prescription.chinesemedicine.options.ChineseMedicineOptionsActivity;
import com.ssh.shuoshi.ui.prescription.westernmedicine.edit.EditPrescriptionActivity;
import com.ssh.shuoshi.ui.verified.submit.PrescriptionSubmitCheckActivity;
import com.ssh.shuoshi.ui.videocall.TRTCVideoCallActivity;
import com.ssh.shuoshi.util.DateUtil;
import com.ssh.shuoshi.util.WeekUtil;
import com.ssh.shuoshi.view.pickview.builder.OptionsPickerBuilder;
import com.ssh.shuoshi.view.pickview.view.OptionsPickerView;
import com.tencent.imsdk.v2.V2TIMConversation;
import com.tencent.imsdk.v2.V2TIMCustomElem;
import com.tencent.imsdk.v2.V2TIMManager;
import com.tencent.imsdk.v2.V2TIMMessage;
import com.tencent.mmkv.MMKV;
import com.tencent.qcloud.tim.uikit.base.IUIKitCallBack;
import com.tencent.qcloud.tim.uikit.component.CircleImageView;
import com.tencent.qcloud.tim.uikit.component.TitleBarLayout;
import com.tencent.qcloud.tim.uikit.modules.chat.ChatLayout;
import com.tencent.qcloud.tim.uikit.modules.chat.base.ChatInfo;
import com.tencent.qcloud.tim.uikit.modules.chat.layout.input.InputLayout;
import com.tencent.qcloud.tim.uikit.modules.chat.layout.message.MessageLayout;
import com.tencent.qcloud.tim.uikit.modules.chat.layout.message.holder.ICustomMessageViewGroup;
import com.tencent.qcloud.tim.uikit.modules.chat.layout.message.holder.IOnCustomMessageDrawListener;
import com.tencent.qcloud.tim.uikit.modules.chat.layout.message.holder.MessageContentHolder;
import com.tencent.qcloud.tim.uikit.modules.chat.layout.message.holder.MessageCustomHolder;
import com.tencent.qcloud.tim.uikit.modules.message.MessageInfo;
import com.tencent.qcloud.tim.uikit.modules.message.MessageInfoUtil;
import com.tencent.qcloud.tim.uikit.utils.BackgroundTasks;
import com.tencent.qcloud.tim.uikit.utils.ToastUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import butterknife.BindView;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;

import static com.ssh.shuoshi.ToolTm.GenerateTestIMUserSig.SDKAPPID;

/**
 * 图文问诊/视频问诊
 */
public class GraphicInquiryActivity extends BaseActivity implements GraphicInquiryContract.View, View.OnClickListener {

    // 图文问诊
    public static int TYPE_GRAPHIC = 1;
    // 视频问诊
    public static int TYPE_VIDEO = 2;
    // 专家团队图文问诊
    public static int TYPE_GRAPHIC_EXPORT = 3;
    // 专家团队视频
    public static int TYPE_VIDEO_EXPORT = 4;

    @Inject
    UserStorage mUserStorage;

    @BindView(R.id.imgBack)
    ImageView imgBack;
    @BindView(R.id.textTitle)
    TextView textTitle;
    @BindView(R.id.tvRightEnd)
    TextView tvRightEnd;
    @BindView(R.id.imgPull)
    ImageView imgPull;
    @BindView(R.id.rlHead)
    RelativeLayout rlHead;
    @BindView(R.id.imgClock)
    ImageView imgClock;
    @BindView(R.id.textState)
    TextView textState;
    @BindView(R.id.textCountDown)
    TextView textCountDown;
    @BindView(R.id.ll_head)
    LinearLayout llHead;
    @BindView(R.id.recyclerViewTeam)
    RecyclerView recyclerViewTeam;
    @BindView(R.id.rlTeam)
    RelativeLayout rlTeam;
    @BindView(R.id.textTeamName)
    TextView textTeamName;

    // TYPE
    private int type;

    @BindView(R.id.chatLayout)
    ChatLayout chatLayout;
    @BindView(R.id.ll_accepts_state)
    LinearLayout llAcceptsState;
    @BindView(R.id.tv_accepts_hint)
    TextView tvAcceptsHint;
    @BindView(R.id.btn_reject)
    Button btnReject;
    @BindView(R.id.btn_accept)
    Button btnAccept;
    @BindView(R.id.ll_verdict)
    LinearLayout llVerdict;
    @BindView(R.id.ll_prescribe)
    LinearLayout llPrescribe;
    @BindView(R.id.tv_prescribe)
    TextView tvPrescribe;
    @BindView(R.id.ll_test_question)
    LinearLayout llTestQuestion;
    @BindView(R.id.rl_bottom)
    RelativeLayout rlBottom;
    @BindView(R.id.textViewQuestion)
    TextView textViewQuestion;

    @Inject
    GraphicInquiryPresenter mPresenter;
    public static int mId;                            //从列表带进来的问诊ID
    private InputLayout inputLayout;
    //问诊、问诊订单详细信息
    private ImageDiagnoseBean.RowsBean mConsultaionInfoBean = new ImageDiagnoseBean.RowsBean();
    private String deptType;
    private MessageLayout messageLayout;

    //退诊理由
    private List<SystemTypeBean.RowsBean> reasonList = new ArrayList<>();

    private boolean hasSendWritePrescription = false;

    private HisDoctorBean doctorInfo;

    // 是否有问诊小结
    private boolean hasCmr = false;
    // 是否有开处方
    private boolean hasPres = false;
    //专家团队adapter
    private CommonAdapter mTeamAdapter;
    //专家团队信息
    private List<HisDoctorExpertTeamMemberDtosBean> teamMemberDtos;

    @Override
    public int initContentView() {
        return R.layout.activity_graphic_inquiry;
    }

    @Override
    protected void initInjector() {
        DaggerGraphicInquiryComponent.builder()
                .applicationComponent(getApplicationComponent())
                .activityModule(getActivityModule())
                .build()
                .inject(this);
    }

    @Override
    protected void initUiAndListener() {
        EventBus.getDefault().register(this);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        mPresenter.attachView(this);

        //清空MMKV，防止数据错乱
        MMKV.defaultMMKV().removeValueForKey("prescriptionType");
        setIsDispatch(false);
        doctorInfo = mUserStorage.getDoctorInfo();

        deptType = doctorInfo.getDeptType();
        recyclerViewTeam.setLayoutManager(new GridLayoutManager(this, 4));
        Intent intent = getIntent();
        ImageDiagnoseBean.RowsBean rowsBean = (ImageDiagnoseBean.RowsBean) intent.getSerializableExtra("rowsBean");
        mId = rowsBean.getId();
        textTitle.setText(rowsBean.getPatientName());
        int loginStatus = V2TIMManager.getInstance().getLoginStatus();
        if (loginStatus == V2TIMManager.V2TIM_STATUS_LOGINED) {
            Log.i("GraphTest", "alread login");
            //获取问诊、问诊订单详细信息
            mPresenter.getConsultationInfo(mId, true);
            //获取退诊原因
            mPresenter.getReturnDiagnoseReason();
        } else {
            Log.i("GraphTest", "not login");
        }
        imgBack.setOnClickListener(this);
        TitleBarLayout titleBarLayout = chatLayout.getTitleBar();
        titleBarLayout.setVisibility(View.GONE);
    }

    @Override
    public void getConsultationInfoSuccess(ImageDiagnoseBean.RowsBean bean, boolean init) {
        if (bean.getEmrId() != null) {
            hasCmr = true;
        }
        Integer prescriptionState = bean.getPrescriptionState();

        if (bean.getPrescriptionId() != null && (prescriptionState == 2 || prescriptionState == 3)) {
            hasPres = true;
            tvPrescribe.setText("查看处方");
        }

        //处方不通过
        if (prescriptionState != null && prescriptionState == 5) {
            hasPres = true;
            tvPrescribe.setText("查看处方");
        }

        this.mConsultaionInfoBean = bean;
        type = mConsultaionInfoBean.getConsultationTypeId();
        List<HisDoctorExpertTeamMemberDtosBean> memberDtos = mConsultaionInfoBean.getHisDoctorExpertTeamMemberDtos();
        TeamPersonBean teamPersonBean = new TeamPersonBean();
        if (type == 1) {
            teamPersonBean.setKey("image");
        } else if (type == 2) {
            teamPersonBean.setKey("video");
        } else if (type == 3) {
            teamPersonBean.setKey("team");
        }
        List<TeamPersonBean.Team> info = new ArrayList<>();
        TeamPersonBean.Team team1 = new TeamPersonBean.Team();
        team1.setName(mConsultaionInfoBean.getPatientName());
        team1.setGroupId(mConsultaionInfoBean.getGroupId());
        info.add(team1);
        if (memberDtos != null) {
            for (int i = 0; i < memberDtos.size(); i++) {
                TeamPersonBean.Team team2 = new TeamPersonBean.Team();
                team2.setName(memberDtos.get(i).getDoctorName());
                team2.setGroupId(memberDtos.get(i).getDoctorNo());
                team2.setTitleName(memberDtos.get(i).getTitleName());
                info.add(team2);
            }
        }
        teamPersonBean.setInfo(info);
        MMKV.defaultMMKV().putString("imType", new Gson().toJson(teamPersonBean));
        int state = mConsultaionInfoBean.getState();
        //标题名
        if (mConsultaionInfoBean.getDoctorExperTeamId() == null) {
            textTitle.setText(mConsultaionInfoBean.getPatientName());
        } else {
            textTitle.setText(mConsultaionInfoBean.getPatientName() + "-团队问诊");
            textTitle.setOnClickListener(this);
            imgPull.setOnClickListener(this);
            rlTeam.setOnClickListener(this);
            imgPull.setVisibility(View.VISIBLE);
        }

        if (init) {
            initListener();
            initChatLayout();
            llVerdict.setOnClickListener(this);
            llPrescribe.setOnClickListener(this);
            tvRightEnd.setOnClickListener(this);
            btnReject.setOnClickListener(v -> {
                if (reasonList.size() == 0) {
                    com.ssh.shuoshi.library.util.ToastUtil.showToast("暂无退诊理由");
                    return;
                }
                mPresenter.getConsultationInfoForExit(mId);
            });
            btnAccept.setOnClickListener(v -> {
                if (type == TYPE_GRAPHIC || type == TYPE_GRAPHIC_EXPORT) {
                    mPresenter.receiveConsultation(mId);
                } else if (type == TYPE_VIDEO || type == TYPE_VIDEO_EXPORT) {
                    try {
                        String startTime = mConsultaionInfoBean.getStartTime();
                        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm");
                        Date startDate = format.parse(startTime);
                        Date currentDate = new Date();
                        if (startDate.getTime() > currentDate.getTime()) {
                            com.ssh.shuoshi.library.util.ToastUtil.showToast("请在预约时间内接诊");
                        } else {
                            mPresenter.receiveConsultation(mId);
                        }
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }

            });

            if (type == TYPE_GRAPHIC || type == TYPE_GRAPHIC_EXPORT) {
                llTestQuestion.setVisibility(View.GONE);
            } else if (type == TYPE_VIDEO || type == TYPE_VIDEO_EXPORT) {
                llTestQuestion.setVisibility(View.VISIBLE);
                textViewQuestion.setText("视频问诊");
                llTestQuestion.setOnClickListener(v -> {
                            //  开视频需要权限
                            PermissionX.init(GraphicInquiryActivity.this)
                                    .permissions(Manifest.permission.RECORD_AUDIO, Manifest.permission.CAMERA,
                                            Manifest.permission.WRITE_EXTERNAL_STORAGE)
                                    .request((allGranted, grantedList, deniedList) -> {
                                        if (allGranted) {
                                            // 调用视频通话提醒接口
                                            mPresenter.pushVideoConsulationNotice(mId);
                                            TRTCVideoCallActivity.UserInfo selfInfo = new TRTCVideoCallActivity.UserInfo();
                                            selfInfo.userAvatar = doctorInfo.getHeadImg();
                                            selfInfo.userId = doctorInfo.getDoctorNo();
                                            selfInfo.userName = doctorInfo.getName();
                                            List<TRTCVideoCallActivity.UserInfo> callUserInfoList = new ArrayList<>();
                                            TRTCVideoCallActivity.UserInfo callInfo = new TRTCVideoCallActivity.UserInfo();
                                            callInfo.userId = mConsultaionInfoBean.getPatientId() + "";
                                            callInfo.userName = mConsultaionInfoBean.getPatientName();
                                            callInfo.userAvatar = "";
                                            callUserInfoList.add(callInfo);
                                            String groupId = chatLayout.getChatInfo().getId();

                                            // 因为视频页面可能会最小化，显示弹窗。得测试下
                                            if (!isExistMainActivity(TRTCVideoCallActivity.class)) {
                                                TRTCVideoCallActivity.startCallSomeone(GraphicInquiryActivity.this,
                                                        selfInfo, callUserInfoList, groupId, mConsultaionInfoBean.getId());
                                            }
                                        } else {
                                            com.ssh.shuoshi.library.util.ToastUtil.showToast("没有摄像头权限，无法进行视频通话");
                                        }
                                    });
                        }
                );
            }
        }
        refreshUiByStatus(state);

        //专家团队
        teamMemberDtos = mConsultaionInfoBean.getHisDoctorExpertTeamMemberDtos();
        if (teamMemberDtos == null) {
            return;
        }

        for (int i = 0; i < teamMemberDtos.size(); i++) {
            if (!TextUtils.isEmpty(teamMemberDtos.get(i).getDoctorHeadImg())) {
                mPresenter.getTeamImagePath(new String[]{teamMemberDtos.get(i).getDoctorHeadImg()}, i);
            }
        }

        textTeamName.setText(mConsultaionInfoBean.getDoctorExperTeamName() + "(" + teamMemberDtos.size() + "人)");
        mTeamAdapter = new CommonAdapter<HisDoctorExpertTeamMemberDtosBean>(this, R.layout.item_found_team_head, teamMemberDtos) {
            @Override
            protected void convert(ViewHolder holder, final HisDoctorExpertTeamMemberDtosBean bean, int pos) {
                CircleImageView imgHead = holder.getView(R.id.ImageDoctorAvatar);

                String doctorBaseImg = bean.getDoctorBaseImg();
                if (!TextUtils.isEmpty(doctorBaseImg)) {
                    Glide.with(GraphicInquiryActivity.this).load(doctorBaseImg).into(imgHead);
                } else {
                    Glide.with(GraphicInquiryActivity.this).load(getResources().getDrawable(R.drawable.default_img)).into(imgHead);
                }

                TextView textName = holder.getView(R.id.textName);
                textName.setText(bean.getDoctorName());
            }
        };
        recyclerViewTeam.setAdapter(mTeamAdapter);
    }

    @Override
    public void getConsultationInfoSuccessForExit(ImageDiagnoseBean.RowsBean bean) {
        // state:0申请问诊 1待接诊 2咨询中 3已完成 4已退诊 5已取消
        if (bean.getState() == 1) {
            showReasonListDialog();
        } else {
            com.ssh.shuoshi.library.util.ToastUtil.showToast("仅待接诊状态下才可退诊");
        }
    }

    //退诊dialog
    private void showReasonListDialog() {
        OptionsPickerView pvOptions = new OptionsPickerBuilder(this, (options1, options2, options3, v) -> {
            String name = reasonList.get(options1).getDictLabel();
            MessageInfo reasoninfo = MessageInfoUtil.buildTextMessage(name);
            chatLayout.getChatManager().sendMessage(reasoninfo, false, new IUIKitCallBack() {
                @Override
                public void onSuccess(Object data) {
                    BackgroundTasks.getInstance().runOnUiThread(() -> {
                        refreshUiByStatus(5);
                        IMTipMessage imQuestionMessage = new IMTipMessage();
                        imQuestionMessage.setKey(Constants.HEALTH_RETURNTICKETTIPS);
                        TipExtra content = new TipExtra();
                        content.setContent("退诊成功，问诊费用退回患者帐号");
                        imQuestionMessage.setContent(content);
                        String string = new Gson().toJson(imQuestionMessage);
                        MessageInfo info = MessageInfoUtil.buildCustomMessage(string);
                        chatLayout.getChatManager().sendMessage(info, false, new IUIKitCallBack() {
                            @Override
                            public void onSuccess(Object data) {
                                BackgroundTasks.getInstance().runOnUiThread(() -> {
                                    mPresenter.exitConsultation(mId, name);
                                    chatLayout.scrollToEnd();
                                });
                            }

                            @Override
                            public void onError(String module, int errCode, String errMsg) {
                                ToastUtil.toastLongMessage(errMsg);
                            }
                        });
                    });
                }

                @Override
                public void onError(String module, int errCode, String errMsg) {
                    ToastUtil.toastLongMessage(errMsg);
                }
            });

        })
                .setTitleText("请选择退诊原因")
                .setCancelText(" ")
                .setDividerColor(Color.BLACK)
                .setContentTextSize(16)
                .build();
        pvOptions.setPicker(reasonList);
        pvOptions.show();
    }

    @Override
    public void getConsultationInfoSuccessForPrescriptionCard(ImageDiagnoseBean.RowsBean bean) {
        // 已经开处方进入历史处方
        Intent intent = new Intent(GraphicInquiryActivity.this, MyPrescriptionDetailActivity.class);
        intent.putExtra("prescriptionId", bean.getPrescriptionId());
        startActivityForResult(intent, 300);
    }

    private void initChatLayout() {
        // 单聊面板的默认 UI 和交互初始化
        chatLayout.initDefault();
        chatLayout.setBackgroundColor(Color.parseColor("#F7F7F7"));

        // 从 ChatLayout 里获取 InputLayout
        inputLayout = chatLayout.getInputLayout();
        inputLayout.setVisibility(View.VISIBLE);
        // 隐藏固有功能 // TODO 点击接诊再显示InputLayout
        inputLayout.disableCaptureAction(true);
        inputLayout.disableSendFileAction(true);
        inputLayout.disableSendPhotoAction(true);
        inputLayout.disableVideoRecordAction(true);
        inputLayout.disableEmojiInput(true);
        inputLayout.disableMoreInput(true);
        inputLayout.disableAudioInput(true);

        messageLayout = chatLayout.getMessageLayout();
        // 设置自定义的消息渲染时的回调
        messageLayout.setOnCustomMessageDrawListener(new CustomMessageDraw());

        // 设置头像
        messageLayout.setAvatar(R.drawable.default_img);
        messageLayout.setAvatarRadius(40);
        messageLayout.setAvatarSize(new int[]{40, 40});

        // 设置昵称样式
        messageLayout.setLeftNameVisibility(View.VISIBLE);
        messageLayout.setRightNameVisibility(View.VISIBLE);
        // 设置气泡
        messageLayout.setLeftBubble(ContextCompat.getDrawable(GraphicInquiryActivity.this,
                R.drawable.bg_im_message_left));
        messageLayout.setRightBubble(ContextCompat.getDrawable(GraphicInquiryActivity.this,
                R.drawable.bg_im_message_right));
        // 设置字体颜色
        messageLayout.setChatContextFontSize(15);
        messageLayout.setLeftChatContentFontColor(Color.parseColor("#191919"));
        messageLayout.setRightChatContentFontColor(Color.parseColor("#191919"));
        // 设置聊天时间
        messageLayout.setChatTimeFontSize(13);
        messageLayout.setChatTimeFontColor(Color.parseColor("#C3C3C3"));
        // 设置提示语
        messageLayout.setTipsMessageBubble(new ColorDrawable(0xFFFFFF));
        messageLayout.setTipsMessageFontSize(14);
        messageLayout.setTipsMessageFontColor(0x999999);
    }

    @Override
    public void getUserSigByUserNoSuccess(String userSig) {
        Log.i("GraphTest", "getUserSigByUserNoSuccess");
        TRTCCallingImpl.sharedInstance(GraphicInquiryActivity.this)
                .login(SDKAPPID, doctorInfo.getDoctorNo(), userSig, new TRTCCalling.ActionCallBack() {
                    @Override
                    public void onError(int i, String s) {
                        Log.i("GraphTest", "登录失败");
                        // 登录腾讯组件失败
                        com.ssh.shuoshi.library.util.ToastUtil.showToast("登录腾讯组件失败，所有功能不可用[" + i + "]" + s);
                    }

                    @Override
                    public void onSuccess() {

                    }
                });
    }

    @Override
    public void getTeamImagePathSuccess(List<String> imgList, int position) {
        if (mTeamAdapter != null) {
            teamMemberDtos.get(position).setDoctorBaseImg(imgList.get(0));
            mTeamAdapter.notifyItemChanged(position);
        }
    }

    private void initListener() {
        ChatInfo chatInfo = new ChatInfo();
        chatInfo.setType(V2TIMConversation.V2TIM_GROUP);
        chatInfo.setId(mConsultaionInfoBean.getGroupId());
        chatInfo.setChatName(mConsultaionInfoBean.getPatientName());
        chatLayout.setChatInfo(chatInfo);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
//        new Thread(() -> {
//            try {
//                Thread.sleep(2000);
//                chatLayout.loadMessages();
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        }).start();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imgBack:
                boolean appFloatIsShow = EasyFloat.appFloatIsShow("aaa");
                if (!appFloatIsShow) {
                    finish();
                }
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

            //结束问诊
            case R.id.tvRightEnd:
                CommonDialog commonDialog = new CommonDialog(GraphicInquiryActivity.this, "确认结束问诊吗？", R.style.dialog_physician_certification);
                commonDialog.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
                commonDialog.getWindow().getDecorView().setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener() {
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
                        commonDialog.getWindow().getDecorView().setSystemUiVisibility(uiOptions);
                    }
                });
                commonDialog.setOnItemClickListener(new CommonDialog.ItemClickListener() {
                    @Override
                    public void cancel() {
                        commonDialog.dismiss();
                    }

                    @Override
                    public void save() {
                        if (mConsultaionInfoBean.getEmrId() == null) {
                            com.ssh.shuoshi.library.util.ToastUtil.showToast("请先填写问诊小结");
                            return;
                        }
                        // 结束问诊
                        IMBaseMessage endMessage = new IMBaseMessage();
                        endMessage.setKey(Constants.HEALTH_INQUIRYOVERTIPS);
                        String string = new Gson().toJson(endMessage);
                        MessageInfo info = MessageInfoUtil.buildCustomMessage(string);

                        chatLayout.getChatManager().sendMessage(info, false, new IUIKitCallBack() {
                            @Override
                            public void onSuccess(Object data) {
                                BackgroundTasks.getInstance().runOnUiThread(() -> {
                                    mPresenter.endConsultation(mId);
                                    chatLayout.scrollToEnd();
                                });
                            }

                            @Override
                            public void onError(String module, int errCode, String errMsg) {
                                ToastUtil.toastLongMessage(errMsg);
                            }
                        });
                        commonDialog.dismiss();
                    }
                });
                commonDialog.show();
                break;

            //问诊小结,如果开过问诊小结，则不会再开，只能点击卡片查看
            case R.id.ll_verdict:
                if (hasCmr) {
                    com.ssh.shuoshi.library.util.ToastUtil.showToast("已有问诊小结");
                    return;
                }
                if (mConsultaionInfoBean.getEmrId() != null) {
                    com.ssh.shuoshi.library.util.ToastUtil.showToast("已有问诊小结");
                    return;
                }
                Intent intent = new Intent(GraphicInquiryActivity.this, ConsultationSummaryActivity.class);
                ConsultationSummaryBean cBean = new ConsultationSummaryBean(mConsultaionInfoBean.getId(),
                        mConsultaionInfoBean.getPatientName(), mConsultaionInfoBean.getSexName(), mConsultaionInfoBean.getPatientAge());
                intent.putExtra("rowsBean", cBean);
                if (mConsultaionInfoBean.getEmrId() != null) {
                    intent.putExtra("emrId", mConsultaionInfoBean.getEmrId());
                }
                startActivityForResult(intent, 101);
                break;

            //开处方,已开过处方，则显示查看处方
            case R.id.ll_prescribe:
                if (hasPres) {
                    mPresenter.getConsultationInfoForPrescriptionCard(mConsultaionInfoBean.getId());
                    return;
                }
                if (mConsultaionInfoBean.getEmrId() == null) {
                    com.ssh.shuoshi.library.util.ToastUtil.showToast("请先填写问诊小结");
                    return;
                }

                Integer perscriptionTypeId = mConsultaionInfoBean.getPerscriptionTypeId();
                Integer state = mConsultaionInfoBean.getPrescriptionState();
                //发送开处方提示消息
                if (perscriptionTypeId == null && !hasSendWritePrescription) {
                    hasSendWritePrescription = true;
                    IMTipMessage imTipMessage = new IMTipMessage();
                    imTipMessage.setKey("health_openRxTips");
                    TipExtra content = new TipExtra();
                    content.setContent("我正在为您开具处方，预计2-3分钟，请稍候");
                    imTipMessage.setContent(content);
                    String string = new Gson().toJson(imTipMessage);
                    MessageInfo info = MessageInfoUtil.buildCustomMessage(string);
                    chatLayout.sendMessage(info, false);
                }
                goPrescribe(perscriptionTypeId, state);
                break;
        }
    }

    //开处方的判断  处方类别ID,1:西医，2中医
    private void goPrescribe(Integer perscriptionTypeId, Integer state) {
        //之前开过处方
        if (perscriptionTypeId != null) {
            goOldPrescribe(perscriptionTypeId, state);
        } else {
            goNewPrescribe();
        }
    }

    //之前没有开过处方
    private void goNewPrescribe() {
        if (deptType.equals("西医")) {
            List<HisDoctorExpertTeamMemberDtosBean> dtos = mConsultaionInfoBean.getHisDoctorExpertTeamMemberDtos();
            if (dtos != null) {
                for (int i = 0; i < dtos.size(); i++) {
                    dtos.get(i).setDoctorBaseImg("");
                }
            }

            Intent intent2 = new Intent(GraphicInquiryActivity.this, EditPrescriptionActivity.class);
            intent2.putExtra("consultaion", mConsultaionInfoBean);
            intent2.putExtra("isRequest", false);
            startActivityForResult(intent2, 300);
        } else {
            //走中医
            ChooseRecipeDialog dialog = new ChooseRecipeDialog(GraphicInquiryActivity.this, R.style.dialog_physician_certification);
            dialog.setCanceledOnTouchOutside(false);
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
            dialog.setOnItemClickListener(new ChooseRecipeDialog.ItemClickListener() {
                @Override
                public void cancel() {
                    dialog.dismiss();
                }

                @Override
                public void choose(Integer choose) {
                    if (choose == null) {
                        return;
                    }
                    List<HisDoctorExpertTeamMemberDtosBean> dtos = mConsultaionInfoBean.getHisDoctorExpertTeamMemberDtos();
                    if (dtos != null) {
                        for (int i = 0; i < dtos.size(); i++) {
                            dtos.get(i).setDoctorBaseImg("");
                        }
                    }
                    if (choose == 1) {
                        Intent intent2 = new Intent(GraphicInquiryActivity.this, EditPrescriptionActivity.class);
                        intent2.putExtra("consultaion", mConsultaionInfoBean);
                        //中医身份需要添加ID
                        intent2.putExtra("isRequest", false);
                        startActivityForResult(intent2, 300);
                        dialog.dismiss();
                    } else {
                        //走中医的方法
                        Intent intent2 = new Intent(GraphicInquiryActivity.this, ChineseMedicineOptionsActivity.class);
                        intent2.putExtra("consultaion", mConsultaionInfoBean);
                        intent2.putExtra("isRequest", false);
                        startActivityForResult(intent2, 300);
                        dialog.dismiss();
                    }
                }
            });
            dialog.show();
        }
    }

    //之前开过处方
    //处方状态 0待签署，执行编辑处方
    //1待提交审核 跳转处方提交审核界面
    //2待审方 您已开具处方
    private void goOldPrescribe(Integer perscriptionTypeId, Integer state) {
        List<HisDoctorExpertTeamMemberDtosBean> dtos = mConsultaionInfoBean.getHisDoctorExpertTeamMemberDtos();
        if (dtos != null) {
            for (int i = 0; i < dtos.size(); i++) {
                dtos.get(i).setDoctorBaseImg("");
            }
        }
        //西医
        if (perscriptionTypeId == 1) {
            //prescriptionState	处方状态，0待签署，1待提交审核，2待审方，3有效，4过期作废，5处方审批未通过作废
            if (state == null || state == 0 || state == 5) {
                Intent intent22 = new Intent(GraphicInquiryActivity.this, EditPrescriptionActivity.class);
                intent22.putExtra("consultaion", mConsultaionInfoBean);
                intent22.putExtra("isRequest", true);
                startActivityForResult(intent22, 300);
            } else if (state == 1) {
                MMKV.defaultMMKV().putString("prescriptionType", "西药");
                Intent intent = new Intent(GraphicInquiryActivity.this, PrescriptionSubmitCheckActivity.class);
                intent.putExtra("prescriptionId", mConsultaionInfoBean.getPrescriptionId());
                startActivityForResult(intent, 300);
            } else if (state == 2 || state == 3) {
                com.ssh.shuoshi.library.util.ToastUtil.showToast("您已开具过电子处方");
            }
            //走中医的方法
        } else if (perscriptionTypeId == 2) {
            if (state == null || state == 0 || state == 5) {
                Intent intent22 = new Intent(GraphicInquiryActivity.this, EditChineseMedicinePrescriptionActivity.class);
                intent22.putExtra("consultaion", mConsultaionInfoBean);
                intent22.putExtra("isRequest", true);
                startActivityForResult(intent22, 300);
            } else if (state == 1) {
                //进行判断认证状态
                MMKV.defaultMMKV().putString("prescriptionType", "中药");
                // TODO: 2021/1/6  跳转处方提交审核界面
                Intent intent = new Intent(GraphicInquiryActivity.this, PrescriptionSubmitCheckActivity.class);
                intent.putExtra("prescriptionId", mConsultaionInfoBean.getPrescriptionId());
                startActivityForResult(intent, 300);
            } else if (state == 2 || state == 3) {
                com.ssh.shuoshi.library.util.ToastUtil.showToast("您已开具过电子处方");
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mPresenter.getConsultationInfo(mId, false);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                //问诊小结返回
                case 101:
                    //发送消息
                    if (!hasCmr) {
                        SummaryCardBean cardBean = (SummaryCardBean) data.getSerializableExtra("summaryCard");
                        cardBean.setKey(Constants.HEALTH_SUMMARYCARD);
                        String string = new Gson().toJson(cardBean);
                        MessageInfo infoSummaryCard = MessageInfoUtil.buildCustomMessage(string);
                        chatLayout.sendMessage(infoSummaryCard, false);
                    }
                    break;
                //开处方返回
                case 300:
                    break;
            }
        }
    }

    /**
     * 自定义消息，根据不同的key，进行不同界面展示
     */
    private class CustomMessageDraw implements IOnCustomMessageDrawListener {

        @Override
        public void onDraw(ICustomMessageViewGroup parent, MessageInfo info) {
            ((MessageContentHolder) parent).setChatHint("");
            try {
                // 获取到自定义消息的json数据
                if (info.getTimMessage().getElemType() != V2TIMMessage.V2TIM_ELEM_TYPE_CUSTOM) {
                    return;
                }
                V2TIMCustomElem elem = info.getTimMessage().getCustomElem();

                IMBaseMessage baseData = new Gson().fromJson(new String(elem.getData()), IMBaseMessage.class);
                switch (baseData.getKey()) {
                    //  患者问诊卡片
                    case Constants.HEALTH_INQUIRYCARD:
                        CustomMessageShortView.refresh(parent);
                        // {"key":"health_inquiryCard","content":{"detail":"杨哈哈(男48岁)","consultationId":303,"consultationTypeName":"视频问诊"}}
                        IMQuestionMessage data = new Gson().fromJson(new String(elem.getData()), IMQuestionMessage.class);

                        View view = LayoutInflater.from(GraphicInquiryActivity.this)
                                .inflate(R.layout.item_message_receive_question, null, false);
                        TextView textVisited = view.findViewById(R.id.textVisited);
                        TextView textDetail = view.findViewById(R.id.textDetail);
                        TextView textViewTitle = view.findViewById(R.id.textViewTitle);
                        TextView textViewInfo = view.findViewById(R.id.textViewInfo);
                        TextView textViewDetail = view.findViewById(R.id.textViewDetail);
                        TextView textViewTime = view.findViewById(R.id.textViewTime);
                        textViewTitle.setText("患者向您发起了" + data.getContent().getConsultationTypeName());
                        textViewInfo.setText(data.getContent().getDetail());
                        textViewDetail.setText("病情描述：" + mConsultaionInfoBean.getIllnessDesc());
                        LinearLayout llImages = view.findViewById(R.id.ll_images);
                        LinearLayout llVisited = view.findViewById(R.id.ll_visited);
                        ImageView imageView1 = view.findViewById(R.id.imageView1);
                        ImageView imageView2 = view.findViewById(R.id.imageView2);
                        ImageView imageView3 = view.findViewById(R.id.imageView3);

                        if (type == TYPE_GRAPHIC || type == TYPE_GRAPHIC_EXPORT) {
                            textViewTime.setVisibility(View.GONE);
                        } else if (type == TYPE_VIDEO || type == TYPE_VIDEO_EXPORT) {
                            textViewTime.setVisibility(View.VISIBLE);
                            String startTime = mConsultaionInfoBean.getStartTime();
                            String endTime = mConsultaionInfoBean.getEndTime();
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
                            textViewTime.setText("预约时间：" + timeDate + " " + timeWeek + " " + timeStart + "-" + timeEnd);
                        }

                        if (mConsultaionInfoBean.getVisitedFlag() != null && mConsultaionInfoBean.getVisitedFlag() == 1) {
                            llVisited.setVisibility(View.VISIBLE);
                            textVisited.setText("确诊疾病：" + mConsultaionInfoBean.getVisitHospitalDiag());
                        } else {
                            llVisited.setVisibility(View.GONE);
                        }

                        textDetail.setOnClickListener(v -> {
                            Intent intent = new Intent(GraphicInquiryActivity.this, MedicalHistoryActivity.class);
                            intent.putExtra("patientId", mConsultaionInfoBean.getPatientId());
                            startActivity(intent);
                        });

                        if (type == TYPE_VIDEO || type == TYPE_VIDEO_EXPORT) {
                            // 视频问诊需要显示温馨提示
                            ((MessageContentHolder) parent)
                                    .setChatHint("温馨提示：\n· 患者购买了您的视频问诊，请在患者预约时间内与患者联系\n· 如在问诊结束前，您未与患者视频沟通，患者可要求全额退款\n· 点击下方视频，等待患者接入之后即可开始问诊");
                        }

                        parent.addMessageContentView(view);

                        List<String> images = new ArrayList<>();
                        if (mConsultaionInfoBean.getCheckImgs() != null
                                && mConsultaionInfoBean.getCheckImgs().size() > 0) {
                            images.addAll(mConsultaionInfoBean.getCheckImgs());
                        }
                        if (!TextUtils.isEmpty(mConsultaionInfoBean.getFaceImg())) {
                            images.add(mConsultaionInfoBean.getFaceImg());
                        }
                        if (!TextUtils.isEmpty(mConsultaionInfoBean.getFurImg())) {
                            images.add(mConsultaionInfoBean.getFurImg());
                        }
                        if (images.size() > 0) {
                            String[] photoPath = images.toArray(new String[images.size()]);
                            mPresenter.getImagePath(photoPath, imageView1, imageView2, imageView3,
                                    GraphicInquiryActivity.this);
                            llImages.setVisibility(View.VISIBLE);
                        } else {
                            llImages.setVisibility(View.GONE);
                        }
                        break;
                    // 温馨提示
                    case Constants.HEALTH_INQUIRYTIPS:
                        if (type == TYPE_GRAPHIC || type == TYPE_GRAPHIC_EXPORT) {
                            CustomMessageShortView.refreshNone(parent);
                            // 图文问诊不显示温馨提示
                            parent.addMessageContentView(new View(GraphicInquiryActivity.this));
                            ((MessageCustomHolder) parent).rightUserIcon.setVisibility(View.GONE);
                            ((MessageCustomHolder) parent).leftUserIcon.setVisibility(View.GONE);
                        } else if (type == TYPE_VIDEO || type == TYPE_VIDEO_EXPORT) {
                            CustomMessageShortView.onDrawTipsMessgaeWXTS(chatLayout.getContext(), parent,
                                    "温馨提示：\n· 患者购买了您的视频问诊，请在患者预约时间内与患者联系\n· 如在问诊结束前，您位于患者视频沟通，患者可要求全额退款\n· 点击下方视频，等待患者接入之后即可开始问诊",
                                    false);
                        }
                        break;
                    // 问诊小结
                    case Constants.HEALTH_SUMMARYCARD:
                        hasCmr = true;
                        SummaryCardBean dataCard = new Gson().fromJson(new String(elem.getData()), SummaryCardBean.class);
                        CustomMessageShortView.onDrawCard(GraphicInquiryActivity.this,
                                parent, dataCard, mConsultaionInfoBean, true, info.isSelf());
                        break;
                    // 医生开具电子处方完成提示
                    case Constants.HEALTH_RXSUCCESSTIPS:
                        IMTipMessage dataTipRxApproval = new Gson().fromJson("已为你开具处方，药师审方后即可查看，请稍后", IMTipMessage.class);
                        CustomMessageShortView.onDrawTipsMessageTZ(chatLayout.getContext(), parent, dataTipRxApproval);
                        break;
                    // 医生开具电子处方提示  要区分开
                    case Constants.HEALTH_OPENRXTIPS:
                        IMTipMessage dataTip1 = new Gson().fromJson(new String(elem.getData()), IMTipMessage.class);
                        CustomMessageShortView.onDrawTipsMessage(chatLayout.getContext(), parent, dataTip1);
                        break;
                    // 接诊
                    case Constants.HEALTH_RECEIVETIPS:
                        if (llAcceptsState.getVisibility() == View.VISIBLE) {
                            llAcceptsState.setVisibility(View.GONE);
                        }
                        if (rlBottom.getVisibility() == View.GONE) {
                            rlBottom.setVisibility(View.VISIBLE);
                        }
                        if (inputLayout.getVisibility() == View.GONE) {
                            inputLayout.setVisibility(View.VISIBLE);
                        }
                        mPresenter.getConsultationInfo(mId, false);
                        // 退诊
                        IMTipMessage dataTip = new Gson().fromJson(new String(elem.getData()), IMTipMessage.class);
                        CustomMessageShortView.onDrawTipsMessage(chatLayout.getContext(), parent, dataTip);
                        break;
                    // 问诊医生主动退诊
                    case Constants.HEALTH_RETURNTICKETTIPS:
                        IMTipMessage dataTip2 = new Gson().fromJson(new String(elem.getData()), IMTipMessage.class);
                        CustomMessageShortView.onDrawTipsMessageTZ(chatLayout.getContext(), parent, dataTip2);
                        break;
                    // 电子处方卡片
                    case Constants.HEALTH_RXAPPROVAL:
                    case Constants.HEALTH_RXAPPROVAL2:
                    case Constants.HEALTH_PRESCRIPTIONCARD:
                        // 电子处方已退诊卡片
                    case Constants.HEALTH_OPENRXFAIL:
                        RecipeCardBean prescriptionCard = new Gson().fromJson(new String(elem.getData()), RecipeCardBean.class);

                        CustomMessageShortView.onDrawPrescriptionCard(GraphicInquiryActivity.this, parent,
                                prescriptionCard, true, info.isSelf());
                        hasPres = true;
                        tvPrescribe.setText("查看处方");
                        break;
                    // 问诊结束
                    case Constants.HEALTH_INQUIRYOVERTIPS:
                        llAcceptsState.setVisibility(View.GONE);
                        rlBottom.setVisibility(View.GONE);
                        inputLayout.setVisibility(View.GONE);
                        textState.setTextColor(Color.parseColor("#FF999999"));
                        imgClock.setImageDrawable(getResources().getDrawable(R.drawable.grey_clock));
                        textCountDown.setVisibility(View.GONE);
                        CustomMessageShortView.onDrawEndMessgae(chatLayout.getContext(), parent, "咨询已结束");
                        break;
                    // 视频消息
                    case Constants.HEALTH_VIDEO:
                        IMVideoMessage videoMessage = new Gson().fromJson(new String(elem.getData()), IMVideoMessage.class);
                        CustomMessageShortView.onDrawVideoMessgae(chatLayout.getContext(), parent, videoMessage, info.isSelf());
                        break;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onError(Throwable throwable) {
        loadError(throwable);
    }

    @Override
    public void consultationSuccess(String bean) {
        refreshUiByStatus(2);
        IMTipMessage imQuestionMessage = new IMTipMessage();
        imQuestionMessage.setKey(Constants.HEALTH_RECEIVETIPS);
        TipExtra content = new TipExtra();
        if (type == TYPE_GRAPHIC || type == TYPE_GRAPHIC_EXPORT) {
            content.setContent("问诊已开始，本次问诊可持续24小时");
        } else {
            content.setContent("问诊已开始");
        }
        imQuestionMessage.setContent(content);
        String string = new Gson().toJson(imQuestionMessage);
        MessageInfo info = MessageInfoUtil.buildCustomMessage(string);
        chatLayout.sendMessage(info, false);
        if (type == TYPE_GRAPHIC || type == TYPE_GRAPHIC_EXPORT) {
            startTimeCount();
        }

    }

    @Override
    public void exitConsultationSuccess(String bean, String reason) {
        refreshUiByStatus(6);
    }

    @Override
    public void endConsultationSuccess(String bean) {
        refreshUiByStatus(3);
    }

    @Override
    public void getImagePathSuccess(List<String> imgList, ImageView imageView1, ImageView imageView2, ImageView imageView3) {
        if (imgList != null) {
            if (imgList.size() > 0) {
                if (imageView1.getDrawable() == null) {
                    Glide.with(GraphicInquiryActivity.this).load(imgList.get(0)).into(imageView1);
                }
                imageView1.setOnClickListener(v -> {
                    Intent intent = new Intent(GraphicInquiryActivity.this, ImageGalleryActivity.class);
                    // intent.putExtra("imageUrls", new Gson().toJson(imgList));
                    MMKV.defaultMMKV().putString("imageUrls", new Gson().toJson(imgList));
                    startActivity(intent);
                });
            }
            if (imgList.size() > 1) {
                if (imageView2.getDrawable() == null) {
                    Glide.with(GraphicInquiryActivity.this).load(imgList.get(1)).into(imageView2);
                }
                imageView2.setOnClickListener(v -> {
                    Intent intent = new Intent(GraphicInquiryActivity.this, ImageGalleryActivity.class);
                    MMKV.defaultMMKV().putString("imageUrls", new Gson().toJson(imgList));
                    startActivity(intent);
                });
            }
            if (imgList.size() > 2) {
                if (imageView3.getDrawable() == null) {
                    Glide.with(GraphicInquiryActivity.this).load(imgList.get(2)).into(imageView3);
                }
                imageView3.setOnClickListener(v -> {
                    Intent intent = new Intent(GraphicInquiryActivity.this, ImageGalleryActivity.class);
                    MMKV.defaultMMKV().putString("imageUrls", new Gson().toJson(imgList));
                    startActivity(intent);
                });
            }
        }
    }

    //退诊理由
    @Override
    public void getReasonList(SystemTypeBean bean) {
        if (bean == null || bean.getRows() == null) {
            return;
        }
        reasonList = bean.getRows();
    }

    /**
     * 根据问诊状态跳转UI
     *
     * @param state
     */
    private void refreshUiByStatus(int state) {
        // 问诊状态，0待支付、1待接诊、2咨询中、3已完成、4已退款、5退诊中，6已退诊
        if (state == 0) {
            llAcceptsState.setVisibility(View.GONE);
            rlBottom.setVisibility(View.GONE);
            tvRightEnd.setVisibility(View.GONE);
            textState.setText("待支付");
            textState.setTextColor(Color.parseColor("#FF999999"));
            imgClock.setVisibility(View.GONE);
            textCountDown.setVisibility(View.GONE);
        }
        if (state == 1) {
            llAcceptsState.setVisibility(View.VISIBLE);
            if (type == TYPE_GRAPHIC || type == TYPE_GRAPHIC_EXPORT) {
                tvAcceptsHint.setText("请在24小时内接诊，超时将自动退款");
            } else if (type == TYPE_VIDEO || type == TYPE_VIDEO_EXPORT) {
                tvAcceptsHint.setText("请在预约时间内接诊，超时将自动退款");
            }
            rlBottom.setVisibility(View.GONE);
            tvRightEnd.setVisibility(View.GONE);
            textState.setText("待接诊");
            imgClock.setVisibility(View.VISIBLE);
            textCountDown.setVisibility(View.GONE);
            inputLayout.setVisibility(View.GONE);
        } else if (state == 2) {
            llAcceptsState.setVisibility(View.GONE);
            rlBottom.setVisibility(View.VISIBLE);
            tvRightEnd.setVisibility(View.VISIBLE);
            textState.setText("咨询中");
            imgClock.setVisibility(View.VISIBLE);
            inputLayout.setVisibility(View.VISIBLE);
            textCountDown.setVisibility(View.VISIBLE);
            if (type == TYPE_GRAPHIC || type == TYPE_GRAPHIC_EXPORT) {
                startTimeCount();
            }
        } else if (state == 3) {
            llAcceptsState.setVisibility(View.GONE);
            rlBottom.setVisibility(View.GONE);
            tvRightEnd.setVisibility(View.GONE);
            textState.setText("已结束");
            textState.setTextColor(Color.parseColor("#FF999999"));
            imgClock.setImageDrawable(getResources().getDrawable(R.drawable.grey_clock));
            inputLayout.setVisibility(View.GONE);
            textCountDown.setVisibility(View.GONE);
        } else if (state == 4) {
            llAcceptsState.setVisibility(View.GONE);
            rlBottom.setVisibility(View.GONE);
            tvRightEnd.setVisibility(View.GONE);

            textState.setText("已退款");
            textState.setTextColor(Color.parseColor("#FF999999"));
            imgClock.setImageDrawable(getResources().getDrawable(R.drawable.grey_clock));

            inputLayout.setVisibility(View.GONE);
            textCountDown.setVisibility(View.GONE);
        } else if (state == 5) {
            llAcceptsState.setVisibility(View.GONE);
            rlBottom.setVisibility(View.GONE);
            tvRightEnd.setVisibility(View.GONE);
            textState.setText("退诊中");
            textState.setTextColor(Color.parseColor("#FF999999"));
            imgClock.setImageDrawable(getResources().getDrawable(R.drawable.grey_clock));

            inputLayout.setVisibility(View.GONE);
            textCountDown.setVisibility(View.GONE);
        } else if (state == 6) {
            llAcceptsState.setVisibility(View.GONE);
            rlBottom.setVisibility(View.GONE);
            tvRightEnd.setVisibility(View.GONE);

            textState.setText("已退诊");
            textState.setTextColor(Color.parseColor("#FF999999"));
            imgClock.setImageDrawable(getResources().getDrawable(R.drawable.grey_clock));

            inputLayout.setVisibility(View.GONE);
            textCountDown.setVisibility(View.GONE);
        }
    }

    private Disposable mDisposableTimeCount;

    //倒计时
    private void startTimeCount() {
        try {
            String startTime = mConsultaionInfoBean.getActualStartTime();
            Date tomorrowDate = null;
            if (startTime != null) {
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm");
                Date currentDate = format.parse(startTime);
                Calendar calendar = new GregorianCalendar();
                calendar.setTime(currentDate);
                calendar.add(calendar.DATE, 1);
                tomorrowDate = calendar.getTime();
            } else {
                Calendar calendar = new GregorianCalendar();
                calendar.setTime(new Date());
                calendar.add(calendar.DATE, 1);
                tomorrowDate = calendar.getTime();
            }
            final Date finalTomorrowDate = tomorrowDate;
            if (mDisposableTimeCount != null && !mDisposableTimeCount.isDisposed()) {
                mDisposableTimeCount.dispose();
            }
            mDisposableTimeCount = Observable.interval(1, TimeUnit.SECONDS)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(aLong -> {
                        Date currentDate = new Date();
                        if (currentDate.getTime() < finalTomorrowDate.getTime()) {
                            textCountDown.setText(DateUtil.dateDiff(new Date(), finalTomorrowDate));
                        }
                    });
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void finish() {
        if (mDisposableTimeCount != null) {
            mDisposableTimeCount.dispose();
        }
        super.finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mDisposableTimeCount != null) {
            mDisposableTimeCount.dispose();
        }
        EventBus.getDefault().unregister(this);
        mId = -1;
    }

    //标题下拉展示
    @SuppressLint("UseCompatLoadingForDrawables")
    private void showPopupWindow() {
        if (rlTeam.getVisibility() == View.GONE) {
            rlTeam.setVisibility(View.VISIBLE);
            imgPull.setImageDrawable(getResources().getDrawable(R.drawable.pull_up));
        } else {
            rlTeam.setVisibility(View.GONE);
            imgPull.setImageDrawable(getResources().getDrawable(R.drawable.pull_down));
        }
    }

    //判断某一个类是否存在任务栈里面
    private boolean isExistMainActivity(Class<?> activity) {
        Intent intent = new Intent(this, activity);
        ComponentName cmpName = intent.resolveActivity(getPackageManager());
        boolean flag = false;
        if (cmpName != null) { // 说明系统中存在这个activity
            ActivityManager am = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
            List<ActivityManager.RunningTaskInfo> taskInfoList = am.getRunningTasks(10);  //获取从栈顶开始往下查找的10个activity
            for (ActivityManager.RunningTaskInfo taskInfo : taskInfoList) {
                if (taskInfo.baseActivity.equals(cmpName)) { // 说明它已经启动了
                    flag = true;
                    break;  //跳出循环，优化效率
                }
            }
        }
        return flag;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onSwitchEvent(IMLoginSuccessEvent event) {
        Log.i("GraphTest", "登录成功");
        //获取问诊、问诊订单详细信息
        mPresenter.getConsultationInfo(mId, true);
        //获取退诊原因
        mPresenter.getReturnDiagnoseReason();
    }

    @Override
    public void onBackPressedSupport() {
        boolean appFloatIsShow = EasyFloat.appFloatIsShow("aaa");
        if (!appFloatIsShow) {
            super.onBackPressedSupport();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventRate(ChatEvent event) {
        ChatInfo chatInfo = new ChatInfo();
        chatInfo.setType(V2TIMConversation.V2TIM_GROUP);
        chatInfo.setId(mConsultaionInfoBean.getGroupId());
        chatInfo.setChatName(mConsultaionInfoBean.getPatientName());
        chatLayout.setChatInfo(chatInfo);
    }

}
