package com.ssh.shuoshi.ui.videocall;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.constraintlayout.widget.Group;

import com.blankj.utilcode.util.PermissionUtils;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.gyf.barlibrary.ImmersionBar;
import com.lzf.easyfloat.EasyFloat;
import com.lzf.easyfloat.enums.ShowPattern;
import com.lzf.easyfloat.enums.SidePattern;
import com.ssh.shuoshi.Constants;
import com.ssh.shuoshi.R;
import com.ssh.shuoshi.bean.IMBaseMessage;
import com.ssh.shuoshi.bean.IMVideoMessage;
import com.ssh.shuoshi.eventbus.ChatEvent;
import com.ssh.shuoshi.library.util.ToastUtil;
import com.ssh.shuoshi.library.util.imageloader.ImageLoaderUtil;
import com.ssh.shuoshi.model.TRTCCalling;
import com.ssh.shuoshi.model.TRTCCallingDelegate;
import com.ssh.shuoshi.model.impl.TRTCCallingImpl;
import com.ssh.shuoshi.ui.BaseActivity;
import com.ssh.shuoshi.ui.graphicinquiry.GraphicInquiryActivity;
import com.ssh.shuoshi.ui.videocall.videolayout.TRTCVideoLayout;
import com.ssh.shuoshi.ui.videocall.videolayout.TRTCVideoLayoutManager;
import com.tencent.imsdk.v2.V2TIMAdvancedMsgListener;
import com.tencent.imsdk.v2.V2TIMManager;
import com.tencent.imsdk.v2.V2TIMMessage;
import com.tencent.imsdk.v2.V2TIMMessageReceipt;

import org.greenrobot.eventbus.EventBus;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * ???????????????????????????????????????????????????????????????????????????????????????????????????
 *
 * @author guanyifeng
 */
public class TRTCVideoCallActivity extends AppCompatActivity {

    public static final int TYPE_BEING_CALLED = 1;
    public static final int TYPE_CALL = 2;

    public static final String PARAM_TYPE = "type";
    public static final String PARAM_SELF_INFO = "self_info";
    public static final String PARAM_USER = "user_model";
    public static final String PARAM_GROUP = "user_group";
    public static final String PARAM_ROOM = "user_room";
    public static final String PARAM_BEINGCALL_USER = "beingcall_user_model";
    public static final String PARAM_OTHER_INVITING_USER = "other_inviting_user_model";
    private static final int MAX_SHOW_INVITING_USER = 4;

    private ImageView ivReceivePic;
    private ImageView ivShrink;
    private AppCompatTextView tvReceiveName;
    private Group groupReceiveUser;
    private Group groupCalling;
    private LinearLayout llImChat;
    private LinearLayout llSwitchCamera;

    private LinearLayout mHangupLl;
    private TextView mTvHangupLl;
    private TRTCVideoLayoutManager mLayoutManagerTrtc;
    private Group mInvitingGroup;
    private LinearLayout mImgContainerLl;
    private TextView mTimeTv;
    private ImageView mSponsorAvatarImg;
    private TextView mSponsorUserNameTv;
    private Group mSponsorGroup;
    private Runnable mTimeRunnable;
    private int mTimeCount;
    private Handler mTimeHandler;
    private HandlerThread mTimeHandlerThread;

    /**
     * ????????????????????????
     */
    private UserInfo mSelfModel;
    private List<UserInfo> mCallUserInfoList = new ArrayList<>(); // ?????????
    private Map<String, UserInfo> mCallUserModelMap = new HashMap<>();
    private UserInfo mSponsorUserInfo;                      // ?????????
    private List<UserInfo> mOtherInvitingUserInfoList;
    private int mCallType;
    private TRTCCalling mTRTCCalling;
    private boolean isHandsFree = true;
    private boolean isMuteMic = false;

    private boolean isFrontCamera = true;
    private boolean isHasUserEnter = false;

    private View mFloatingWindow;

    //????????????
    private InnerRecevier innerReceiver = new InnerRecevier();

    /**
     * ???????????????
     */
    private TRTCCallingDelegate mTRTCCallingDelegate = new TRTCCallingDelegate() {

        @Override
        public void onError(int code, String msg) {
            //??????????????????????????????????????????
            ToastUtil.showToast(getString(R.string.trtccalling_toast_call_error_msg, code, msg));
            stopCameraAndFinish();
        }

        @Override
        public void onInvited(String sponsor, List<String> userIdList, boolean isFromGroup, int callType) {

        }

        @Override
        public void onGroupCallInviteeListUpdate(List<String> userIdList) {

        }

        @Override
        public void onUserEnter(final String userId) {
            isHasUserEnter = true;
            groupReceiveUser.setVisibility(View.GONE);
            groupCalling.setVisibility(View.VISIBLE);

            runOnUiThread(() -> {
                showCallingView();
                //1.?????????????????????????????????????????????
                UserInfo model = new UserInfo();
                model.userId = userId;
                model.userName = userId;
                model.userAvatar = "";
                mCallUserInfoList.add(model);
                mCallUserModelMap.put(model.userId, model);
                TRTCVideoLayout videoLayout = addUserToManager(model);
                if (videoLayout == null) {
                    return;
                }
                videoLayout.setVideoAvailable(false);
            });
        }

        @Override
        public void onUserLeave(final String userId) {
            runOnUiThread(() -> {
                //1. ??????????????????
                mLayoutManagerTrtc.recyclerCloudViewView(userId);
                //2. ????????????model
                UserInfo userInfo = mCallUserModelMap.remove(userId);
                if (userInfo != null) {
                    mCallUserInfoList.remove(userInfo);
                }
            });
        }

        @Override
        public void onReject(final String userId) {
            runOnUiThread(() -> {
                if (mCallUserModelMap.containsKey(userId)) {
                    // ??????????????????
                    //1. ??????????????????
                    mLayoutManagerTrtc.recyclerCloudViewView(userId);
                    //2. ????????????model
                    UserInfo userInfo = mCallUserModelMap.remove(userId);
                    if (userInfo != null) {
                        mCallUserInfoList.remove(userInfo);
                        ToastUtil.showToast(getString(R.string.trtccalling_toast_user_reject_call, userInfo.userName));
                    }
                }
            });
        }

        @Override
        public void onNoResp(final String userId) {
            runOnUiThread(() -> {
                if (mCallUserModelMap.containsKey(userId)) {
                    // ?????????????????????
                    //1. ??????????????????
                    mLayoutManagerTrtc.recyclerCloudViewView(userId);
                    //2. ????????????model
                    UserInfo userInfo = mCallUserModelMap.remove(userId);
                    if (userInfo != null) {
                        mCallUserInfoList.remove(userInfo);
                        ToastUtil.showToast(getString(R.string.trtccalling_toast_user_not_response, userInfo.userName));
                    }
                }
            });
        }

        @Override
        public void onLineBusy(String userId) {
            if (mCallUserModelMap.containsKey(userId)) {
                // ?????????????????????
                //1. ??????????????????
                mLayoutManagerTrtc.recyclerCloudViewView(userId);
                //2. ????????????model
                UserInfo userInfo = mCallUserModelMap.remove(userId);
                if (userInfo != null) {
                    mCallUserInfoList.remove(userInfo);
                    ToastUtil.showToast(getString(R.string.trtccalling_toast_user_busy, userInfo.userName));
                }
            }
        }

        @Override
        public void onCallingCancel() {
            if (mSponsorUserInfo != null) {
                ToastUtil.showToast(getString(R.string.trtccalling_toast_user_cancel_call, mSponsorUserInfo.userName));
            }
            stopCameraAndFinish();
        }

        @Override
        public void onCallingTimeout() {
            if (mSponsorUserInfo != null) {
                ToastUtil.showToast(getString(R.string.trtccalling_toast_user_timeout, mSponsorUserInfo.userName));
            }
            stopCameraAndFinish();
        }

        @Override
        public void onCallEnd() {
            if (mSponsorUserInfo != null) {
                ToastUtil.showToast(getString(R.string.trtccalling_toast_user_end, mSponsorUserInfo.userName));
            }
            stopCameraAndFinish();
        }

        @Override
        public void onUserVideoAvailable(final String userId, final boolean isVideoAvailable) {
            //???????????????????????????
            TRTCVideoLayout layout = mLayoutManagerTrtc.findCloudViewView(userId);
            if (layout != null) {
                layout.setVideoAvailable(isVideoAvailable);
                if (isVideoAvailable) {
                    mTRTCCalling.startRemoteView(userId, layout.getVideoView());
                } else {
                    mTRTCCalling.stopRemoteView(userId);
                }
            } else {

            }
        }

        @Override
        public void onUserAudioAvailable(String userId, boolean isVideoAvailable) {

        }

        @Override
        public void onUserVoiceVolume(Map<String, Integer> volumeMap) {
            for (Map.Entry<String, Integer> entry : volumeMap.entrySet()) {
                String userId = entry.getKey();
                TRTCVideoLayout layout = mLayoutManagerTrtc.findCloudViewView(userId);
                if (layout != null) {
                    layout.setAudioVolumeProgress(entry.getValue());
                }
            }
        }
    };

    /**
     * ???????????????????????????
     *
     * @param context
     * @param selfInfo
     * @param callUserInfoList
     * @param groupId
     */
    public static void startCallSomeone(Context context, UserInfo selfInfo,
                                        List<UserInfo> callUserInfoList, String groupId, int roomId) {
        Intent starter = new Intent(context, TRTCVideoCallActivity.class);
        starter.putExtra(PARAM_TYPE, TYPE_CALL);
        starter.putExtra(PARAM_SELF_INFO, selfInfo);
        starter.putExtra(PARAM_USER, new IntentParams(callUserInfoList));
        starter.putExtra(PARAM_GROUP, groupId);
        starter.putExtra(PARAM_ROOM, roomId);
        starter.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(starter);
    }

    /**
     * ??????????????????
     *
     * @param context
     * @param beingCallUserInfo
     */
    public static void startBeingCall(Context context, UserInfo selfUserInfo, UserInfo beingCallUserInfo, List<UserInfo> otherInvitingUserInfo) {
        Intent starter = new Intent(context, TRTCVideoCallActivity.class);
        starter.putExtra(PARAM_TYPE, TYPE_BEING_CALLED);
        starter.putExtra(PARAM_SELF_INFO, selfUserInfo);
        starter.putExtra(PARAM_BEINGCALL_USER, beingCallUserInfo);
        starter.putExtra(PARAM_OTHER_INVITING_USER, new IntentParams(otherInvitingUserInfo));
        starter.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(starter);
    }

    public ImmersionBar mImmersionBar;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mImmersionBar = ImmersionBar.with(this);
        mImmersionBar.transparentStatusBar()  //???????????????????????????????????????
                .statusBarDarkFont(true)   //????????????????????????????????????????????????
                .init();
        // ?????????????????????????????????????????????
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.trtccalling_videocall_activity_call_main);

        initView();
        initData();
        initListener();

        //??????????????????
        IntentFilter intentFilter = new IntentFilter(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
        //????????????
        registerReceiver(innerReceiver, intentFilter);
    }

    @Override
    public void onBackPressed() {
        mTRTCCalling.hangup(mTimeCount);
        stopCameraAndFinish();
        super.onBackPressed();
    }

    private void stopCameraAndFinish() {
        EventBus.getDefault().post(new ChatEvent());
        mTRTCCalling.closeCamera();
        mTRTCCalling.removeDelegate(mTRTCCallingDelegate);
        startActivity(new Intent(TRTCVideoCallActivity.this, GraphicInquiryActivity.class));
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        hideFloatingWindow();
        stopTimeCount();
        mTimeHandlerThread.quit();
        V2TIMManager.getMessageManager().removeAdvancedMsgListener(cV2TIMSimpleMsgListener);
        if (timeOutDisposable != null) {
            timeOutDisposable.dispose();
        }
        unregisterReceiver(innerReceiver);
    }

    private void initListener() {
        ivShrink.setOnClickListener(v -> setBackToPastPageLogic());
        llImChat.setOnClickListener(v -> setBackToPastPageLogic());
        llSwitchCamera.setOnClickListener(v -> {
            isFrontCamera = !isFrontCamera;
            mTRTCCalling.switchCamera(isFrontCamera);
        });
        V2TIMManager.getMessageManager().addAdvancedMsgListener(cV2TIMSimpleMsgListener);
    }

    private void setBackToPastPageLogic() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!PermissionUtils.isGrantedDrawOverlays()) {
                ToastUtil.showToast("???????????????????????????");
                PermissionUtils.requestDrawOverlays(new PermissionUtils.SimpleCallback() {
                    @Override
                    public void onGranted() {
                        startScreenCapture();
                    }

                    @Override
                    public void onDenied() {
                        ToastUtil.showToast("???????????????????????????");
                    }
                });
            } else {
                startScreenCapture();
            }
        } else {
            startScreenCapture();
        }
    }

    private void initData() {
        // ?????????????????????
        mTRTCCalling = TRTCCallingImpl.sharedInstance(this);
        mTRTCCalling.addDelegate(mTRTCCallingDelegate);
        mTimeHandlerThread = new HandlerThread("time-count-thread");
        mTimeHandlerThread.start();
        mTimeHandler = new Handler(mTimeHandlerThread.getLooper());
        // ?????????????????????????????????
        Intent intent = getIntent();
        //???????????????
        mSelfModel = (UserInfo) intent.getSerializableExtra(PARAM_SELF_INFO);
        mCallType = intent.getIntExtra(PARAM_TYPE, TYPE_BEING_CALLED);
        if (mCallType == TYPE_BEING_CALLED) {
            // ????????????
            mSponsorUserInfo = (UserInfo) intent.getSerializableExtra(PARAM_BEINGCALL_USER);
            IntentParams params = (IntentParams) intent.getSerializableExtra(PARAM_OTHER_INVITING_USER);
            if (params != null) {
                mOtherInvitingUserInfoList = params.mUserInfos;
            }
            showWaitingResponseView();
        } else {
            // ?????????
            IntentParams params = (IntentParams) intent.getSerializableExtra(PARAM_USER);
            String groupId = intent.getStringExtra(PARAM_GROUP);
            int roomId = intent.getIntExtra(PARAM_ROOM, -1);
            if (params != null) {
                mCallUserInfoList = params.mUserInfos;
                for (UserInfo userInfo : mCallUserInfoList) {
                    mCallUserModelMap.put(userInfo.userId, userInfo);
                }
                startInviting(groupId, roomId);
                showInvitingView();
                startInvitingTimeOutLogic();
            }
        }

    }

    private Disposable timeOutDisposable;

    /**
     * ??????????????????
     */
    private void startInvitingTimeOutLogic() {
        timeOutDisposable = Flowable.intervalRange(0, 60 + 1, 0, 1, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnComplete(() -> {
                    if (!isHasUserEnter) {
                        ToastUtil.showToast("???????????????");
                        mTRTCCalling.timeOut();
                        stopCameraAndFinish();
                    }
                })
                .subscribe();
    }

    private void startInviting(String groupId, int roomId) {
        List<String> list = new ArrayList<>();
        for (UserInfo userInfo : mCallUserInfoList) {
            list.add(userInfo.userId);
        }
        mTRTCCalling.groupCall(list, TRTCCalling.TYPE_VIDEO_CALL, groupId, roomId);
    }

    private void initView() {
        llImChat = (LinearLayout) findViewById(R.id.ll_im_chat);
        llSwitchCamera = (LinearLayout) findViewById(R.id.ll_switch_camera);
        groupReceiveUser = (Group) findViewById(R.id.group_receive_user);
        groupCalling = (Group) findViewById(R.id.group_calling);
        ivShrink = (ImageView) findViewById(R.id.iv_shrink);
        ivReceivePic = (ImageView) findViewById(R.id.iv_receive_pic);
        tvReceiveName = (AppCompatTextView) findViewById(R.id.tv_receive_name);
        mHangupLl = (LinearLayout) findViewById(R.id.ll_hangup);
        mTvHangupLl = (TextView) findViewById(R.id.tv_hangup);
        mLayoutManagerTrtc = (TRTCVideoLayoutManager) findViewById(R.id.trtc_layout_manager);
        mInvitingGroup = (Group) findViewById(R.id.group_inviting);
        mImgContainerLl = (LinearLayout) findViewById(R.id.ll_img_container);
        mTimeTv = (TextView) findViewById(R.id.tv_time);
        mSponsorAvatarImg = (ImageView) findViewById(R.id.iv_sponsor_avatar);
        mSponsorUserNameTv = (TextView) findViewById(R.id.tv_sponsor_user_name);
        mSponsorGroup = (Group) findViewById(R.id.group_sponsor);
    }

    /**
     * ??????????????????
     */
    public void showWaitingResponseView() {
        if (mSelfModel == null || mLayoutManagerTrtc == null) {
            return;
        }
        //1. ?????????????????????
        mLayoutManagerTrtc.setMySelfUserId(mSelfModel.userId);
        TRTCVideoLayout videoLayout = addUserToManager(mSelfModel);
        if (videoLayout == null) {
            return;
        }
        videoLayout.setVideoAvailable(true);
        mTRTCCalling.openCamera(isFrontCamera, videoLayout.getVideoView());

        //2. ??????????????????????????????
        mSponsorGroup.setVisibility(View.VISIBLE);
        mSponsorAvatarImg.setImageResource(R.drawable.default_head);
        mSponsorUserNameTv.setText(mSponsorUserInfo.userName);

        //3. ????????????????????????
        mHangupLl.setVisibility(View.VISIBLE);
        //3. ???????????????listener
        mHangupLl.setOnClickListener(v -> {
            mTRTCCalling.reject(mTimeCount);
            stopCameraAndFinish();
        });
        //4. ????????????????????????
        showOtherInvitingUserView();
    }

    /**
     * ??????????????????
     */
    public void showInvitingView() {
        //1. ?????????????????????
        mLayoutManagerTrtc.setMySelfUserId(mSelfModel.userId);
        TRTCVideoLayout videoLayout = addUserToManager(mSelfModel);
        if (videoLayout == null) {
            return;
        }
        videoLayout.setVideoAvailable(true);
        mTRTCCalling.openCamera(true, videoLayout.getVideoView());
        //2. ???????????????
        mHangupLl.setVisibility(View.VISIBLE);
        mHangupLl.setOnClickListener(v -> {
            mTRTCCalling.hangup(mTimeCount);
            stopCameraAndFinish();
        });
        //3. ??????????????????????????????
        hideOtherInvitingUserView();
        //4. sponsor???????????????
        mSponsorGroup.setVisibility(View.GONE);
        //5. ??????????????????
        if (mCallUserModelMap.keySet().iterator().hasNext()) {
            groupReceiveUser.setVisibility(View.VISIBLE);
            String key = mCallUserModelMap.keySet().iterator().next();
            ivReceivePic.setImageResource(R.drawable.default_head);
            tvReceiveName.setText(mCallUserModelMap.get(key).userName);
        }
    }

    /**
     * ????????????????????????
     */
    public void showCallingView() {
        //1. ????????????
        mSponsorGroup.setVisibility(View.GONE);
        //2. ???????????????
        mHangupLl.setVisibility(View.VISIBLE);

        mTvHangupLl.setText("??????");
        mHangupLl.setOnClickListener(v -> {
            mTRTCCalling.hangup(mTimeCount);
            stopCameraAndFinish();
        });
        showTimeCount();
        hideOtherInvitingUserView();
    }

    private void showTimeCount() {
        if (mTimeRunnable != null) {
            return;
        }
        mTimeCount = 0;
        mTimeTv.setText(getShowTime(mTimeCount));
        if (mTimeRunnable == null) {
            mTimeRunnable = () -> {
                mTimeCount++;
                if (mTimeTv != null) {
                    runOnUiThread(() -> mTimeTv.setText(getShowTime(mTimeCount)));
                }
                mTimeHandler.postDelayed(mTimeRunnable, 1000);
            };
        }
        mTimeHandler.postDelayed(mTimeRunnable, 1000);
    }

    private void stopTimeCount() {
        mTimeHandler.removeCallbacks(mTimeRunnable);
        mTimeRunnable = null;
    }

    private String getShowTime(int count) {
        return getString(R.string.trtccalling_called_time_format, count / 60, count % 60);
    }

    private void showOtherInvitingUserView() {
        if (mOtherInvitingUserInfoList == null || mOtherInvitingUserInfoList.size() == 0) {
            return;
        }
        mInvitingGroup.setVisibility(View.VISIBLE);
        int squareWidth = getResources().getDimensionPixelOffset(R.dimen.trtccalling_small_image_size);
        int leftMargin = getResources().getDimensionPixelOffset(R.dimen.trtccalling_small_image_left_margin);
        for (int index = 0; index < mOtherInvitingUserInfoList.size() && index < MAX_SHOW_INVITING_USER; index++) {
            UserInfo userInfo = mOtherInvitingUserInfoList.get(index);
            ImageView imageView = new ImageView(this);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(squareWidth, squareWidth);
            if (index != 0) {
                layoutParams.leftMargin = leftMargin;
            }
            imageView.setLayoutParams(layoutParams);
            imageView.setImageResource(R.drawable.default_head);
            mImgContainerLl.addView(imageView);
        }
    }

    private void hideOtherInvitingUserView() {
        mInvitingGroup.setVisibility(View.GONE);
    }

    private TRTCVideoLayout addUserToManager(UserInfo userInfo) {
        TRTCVideoLayout layout = mLayoutManagerTrtc.allocCloudVideoView(userInfo.userId);
        if (layout == null) {
            return null;
        }
        layout.getUserNameTv().setText(userInfo.userName);
        layout.getHeadImg().setImageResource(R.drawable.default_head);
        return layout;
    }

    private static class IntentParams implements Serializable {
        public List<UserInfo> mUserInfos;

        public IntentParams(List<UserInfo> userInfos) {
            mUserInfos = userInfos;
        }
    }

    public static class UserInfo implements Serializable {
        public String userId;
        public String userAvatar;
        public String userName;
    }

    /**
     * ?????????????????????
     */
    private V2TIMAdvancedMsgListener cV2TIMSimpleMsgListener = new V2TIMAdvancedMsgListener() {

        @Override
        public void onRecvNewMessage(V2TIMMessage msg) {
            super.onRecvNewMessage(msg);
            try {
                byte[] customData = msg.getCustomElem().getData();
                IMBaseMessage baseData = new Gson().fromJson(new String(customData), IMBaseMessage.class);
                if (baseData.getKey() != null) {
                    switch (baseData.getKey()) {
                        case Constants.HEALTH_VIDEO:
                            IMVideoMessage data = new Gson().fromJson(new String(customData), IMVideoMessage.class);
                            // 0??????????????? 1???????????? 2???????????? 3??????????????????????????? 4???????????? 5???????????? 6???????????????
                            if (data.getContent().getAction() == 1) {
                                mTRTCCalling.hangup(-1010);
                                stopCameraAndFinish();
                            } else if (data.getContent().getAction() == 2) {
                                mTRTCCalling.hangup(-1010);
                                stopCameraAndFinish();
                            } else if (data.getContent().getAction() == 3) {
                                mTRTCCalling.hangup(-1010);
                                stopCameraAndFinish();
                            } else if (data.getContent().getAction() == 5) {
                                mTRTCCalling.hangup(-1010);
                                stopCameraAndFinish();
                            } else if (data.getContent().getAction() == 6) {
                                mTRTCCalling.hangup(-1010);
                                stopCameraAndFinish();
                            }
                            break;
                    }
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        @Override
        public void onRecvC2CReadReceipt(List<V2TIMMessageReceipt> receiptList) {
            super.onRecvC2CReadReceipt(receiptList);
        }

        @Override
        public void onRecvMessageRevoked(String msgID) {
            super.onRecvMessageRevoked(msgID);
        }
    };

    class InnerRecevier extends BroadcastReceiver {

        final String SYSTEM_DIALOG_REASON_KEY = "reason";

        final String SYSTEM_DIALOG_REASON_RECENT_APPS = "recentapps";

        final String SYSTEM_DIALOG_REASON_HOME_KEY = "homekey";

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (Intent.ACTION_CLOSE_SYSTEM_DIALOGS.equals(action)) {
                String reason = intent.getStringExtra(SYSTEM_DIALOG_REASON_KEY);
                if (reason != null) {
                    if (reason.equals(SYSTEM_DIALOG_REASON_HOME_KEY)) {
                        // HOME
                        if (mFloatingWindow == null) {
                            setBackToPastPageLogic();
                        }
                    } else if (reason.equals(SYSTEM_DIALOG_REASON_RECENT_APPS)) {
                        // ?????????
                    }
                }
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        hideFloatingWindow();
    }

    private void startScreenCapture() {
        moveTaskToBack(true);
        showFloatingWindow();
    }

    private void showFloatingWindow() {
        EasyFloat.with(this)
                .setLayout(R.layout.screen_capture_floating_window2, view ->
                        view.findViewById(R.id.imageView).setOnClickListener(view1 -> {
                            Intent mainIntent = new Intent(TRTCVideoCallActivity.this, GraphicInquiryActivity.class);
                            Intent targetIntent = new Intent(TRTCVideoCallActivity.this, TRTCVideoCallActivity.class);
                            Intent[] intents = new Intent[2];
                            intents[0] = mainIntent;
                            intents[1] = targetIntent;
                            startActivities(intents);
                        }))
                .setGravity(Gravity.END, 0, 100)
                // ?????????????????????????????????????????????Activity???????????????????????????????????????????????????????????????
                .setShowPattern(ShowPattern.ALL_TIME)
                // ????????????????????????15????????????????????????SidePattern
                .setSidePattern(SidePattern.RESULT_RIGHT)
                // ?????????????????????????????????????????????
                .setDragEnable(true)
                .setTag("aaa")
                // ????????????????????????EditText??????????????????????????????????????????
                .hasEditText(false)
                .show();
    }

    private void hideFloatingWindow() {
        EasyFloat.dismissAppFloat("aaa");
    }

}
