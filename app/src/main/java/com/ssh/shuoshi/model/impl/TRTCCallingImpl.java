package com.ssh.shuoshi.model.impl;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.ssh.shuoshi.Constants;
import com.ssh.shuoshi.bean.IMVideoMessage;
import com.ssh.shuoshi.bean.VideoExtra;
import com.ssh.shuoshi.model.TRTCCalling;
import com.ssh.shuoshi.model.TRTCCallingDelegate;
import com.ssh.shuoshi.model.impl.base.CallModel;
import com.ssh.shuoshi.model.impl.base.MessageCustom;
import com.ssh.shuoshi.model.impl.base.OfflineMessageBean;
import com.ssh.shuoshi.model.impl.base.OfflineMessageContainerBean;
import com.ssh.shuoshi.model.impl.base.TRTCInternalListenerManager;
import com.ssh.shuoshi.model.impl.base.TRTCLogger;
import com.tencent.imsdk.TIMConversationType;
import com.tencent.imsdk.session.SessionWrapper;
import com.tencent.imsdk.v2.V2TIMCallback;
import com.tencent.imsdk.v2.V2TIMManager;
import com.tencent.imsdk.v2.V2TIMMessage;
import com.tencent.imsdk.v2.V2TIMOfflinePushInfo;
import com.tencent.imsdk.v2.V2TIMSDKConfig;
import com.tencent.imsdk.v2.V2TIMSDKListener;
import com.tencent.imsdk.v2.V2TIMSendCallback;
import com.tencent.imsdk.v2.V2TIMSignalingListener;
import com.tencent.imsdk.v2.V2TIMUserFullInfo;
import com.tencent.imsdk.v2.V2TIMValueCallback;
import com.tencent.liteav.beauty.TXBeautyManager;
import com.tencent.rtmp.ui.TXCloudVideoView;
import com.tencent.trtc.TRTCCloud;
import com.tencent.trtc.TRTCCloudDef;
import com.tencent.trtc.TRTCCloudListener;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

/**
 * 视频/语音通话的具体实现
 * 本功能使用腾讯云实时音视频 / 腾讯云即时通信IM 组合实现
 */
public class TRTCCallingImpl extends TRTCCalling {

    private static final String TAG = "TRTCCallingImpl";
    /**
     * 超时时间，单位秒
     */
    public static final int TIME_OUT_COUNT = 10;

    /**
     * room id 的取值范围
     */
    private static final int ROOM_ID_MIN = 1;
    private static final int ROOM_ID_MAX = Integer.MAX_VALUE;
    private final Context mContext;

    /**
     * 底层SDK调用实例
     */
    private TRTCCloud mTRTCCloud;

    /**
     * 当前IM登录用户名
     */
    private String mCurUserId = "";
    private int mSdkAppId;
    private String mCurUserSig;
    /**
     * 是否首次邀请
     */
    private boolean isOnCalling = false;
    private String mCurCallID = "";
    private int mCurRoomID = 0;
    /**
     * 当前是否在TRTC房间中
     */
    private boolean mIsInRoom = false;
    private long mEnterRoomTime = 0;
    /**
     * 当前邀请列表
     * C2C通话时会记录自己邀请的用户
     * IM群组通话时会同步群组内邀请的用户
     * 当用户接听、拒绝、忙线、超时会从列表中移除该用户
     */
    private List<String> mCurInvitedList = new ArrayList<>();
    /**
     * 当前语音通话中的远端用户
     */
    private Set<String> mCurRoomRemoteUserSet = new HashSet<>();

    /**
     * C2C通话的邀请人
     * 例如A邀请B，B存储的mCurSponsorForMe为A
     */
    private String mCurSponsorForMe = "";

    /**
     * 当前通话的类型
     */
    private int mCurCallType = TYPE_UNKNOWN;
    /**
     * 当前群组通话的群组ID
     */
    private String mCurGroupId = "";
    /**
     * 最近使用的通话信令，用于快速处理
     */
    private CallModel mLastCallModel = new CallModel();
    /**
     * 上层传入回调
     */
    private TRTCInternalListenerManager mTRTCInternalListenerManager;
    private String mNickName;
    private String mFaceUrl;

    private boolean mIsUseFrontCamera;

    private boolean mWaitingLastActivityFinished;
    private boolean mIsInitIMSDK;

    private int timeCount = 0;

    public boolean isWaitingLastActivityFinished() {
        return mWaitingLastActivityFinished;
    }

    public void setWaitingLastActivityFinished(boolean waiting) {
        mWaitingLastActivityFinished = waiting;
    }

    /**
     * 信令监听器
     */
    private V2TIMSignalingListener mTIMSignallingListener = new V2TIMSignalingListener() {
        @Override
        public void onReceiveNewInvitation(String inviteID, String inviter, String groupID, List<String> inviteeList, String data) {
            TRTCLogger.d(TAG, "onReceiveNewInvitation inviteID:" + inviteID + ", inviter:" + inviter + " data:" + data);
            if (!isCallingData(data)) {
                return;
            }
            processInvite(inviteID, inviter, groupID, inviteeList, data);
        }

        @Override
        public void onInviteeAccepted(String inviteID, String invitee, String data) {
            TRTCLogger.d(TAG, "onInviteeAccepted inviteID:" + inviteID + ", invitee:" + invitee + " data:" + data);
            if (!isCallingData(data)) {
                return;
            }
            mCurInvitedList.remove(invitee);
        }

        @Override
        public void onInviteeRejected(String inviteID, String invitee, String data) {
            TRTCLogger.d(TAG, "onInviteeRejected inviteID:" + inviteID + ", invitee:" + invitee + " data:" + data);
            if (!isCallingData(data)) {
                return;
            }
            if (mCurCallID.equals(inviteID)) {
                try {
                    Map rejectData = new Gson().fromJson(data, Map.class);
                    mCurInvitedList.remove(invitee);
                    if (rejectData != null && rejectData.containsKey(CallModel.SIGNALING_EXTRA_KEY_LINE_BUSY)) {
                        if (mTRTCInternalListenerManager != null) {
                            mTRTCInternalListenerManager.onLineBusy(invitee);
                        }
                    } else {
                        if (mTRTCInternalListenerManager != null) {
                            mTRTCInternalListenerManager.onReject(invitee);
                        }
                    }
                    preExitRoom(null);
                } catch (JsonSyntaxException e) {
                    TRTCLogger.e(TAG, "onReceiveNewInvitation JsonSyntaxException:" + e);
                }
            }
        }

        @Override
        public void onInvitationCancelled(String inviteID, String inviter, String data) {
            TRTCLogger.d(TAG, "onInvitationCancelled inviteID:" + inviteID + " data:" + data);
            if (!isCallingData(data)) {
                return;
            }
            if (inviteID.equals(mCurCallID)) {
                stopCall();
                if (mTRTCInternalListenerManager != null) {
                    mTRTCInternalListenerManager.onCallingCancel();
                }
            }
        }

        @Override
        public void onInvitationTimeout(String inviteID, List<String> inviteeList) {
            TRTCLogger.d(TAG, "onInvitationTimeout inviteID:" + inviteID);
            if (!inviteID.equals(mCurCallID)) {
                return;
            }
            if (TextUtils.isEmpty(mCurSponsorForMe)) {
                // 邀请者
                for (String userID : inviteeList) {
                    if (mTRTCInternalListenerManager != null) {
                        mTRTCInternalListenerManager.onNoResp(userID);
                    }
                    mCurInvitedList.remove(userID);
                }
            } else {
                // 被邀请者
                if (inviteeList.contains(mCurUserId)) {
                    stopCall();
                    if (mTRTCInternalListenerManager != null) {
                        mTRTCInternalListenerManager.onCallingTimeout();
                    }
                }
                mCurInvitedList.removeAll(inviteeList);
            }
            // 每次超时都需要判断当前是否需要结束通话
            preExitRoom(null);
        }
    };

    private boolean isCallingData(String data) {
        try {
            JSONObject jsonObject = new JSONObject(data);
            if (jsonObject.has(CallModel.SIGNALING_EXTRA_KEY_CALL_TYPE)) {
                return true;
            }
        } catch (Exception e) {
            TRTCLogger.i(TAG, "isCallingData json parse error");
            return false;
        }
        return false;
    }

    private void processInvite(String inviteID, String inviter, String groupID, List<String> inviteeList, String data) {
        CallModel callModel = new CallModel();
        callModel.callId = inviteID;
        callModel.groupId = groupID;
        callModel.action = CallModel.VIDEO_CALL_ACTION_DIALING;
        callModel.invitedList = inviteeList;

        Map<String, Object> extraMap = null;
        try {
            extraMap = new Gson().fromJson(data, Map.class);
            if (extraMap == null) {
                TRTCLogger.e(TAG, "onReceiveNewInvitation extraMap is null, ignore");
                return;
            }
            if (extraMap.containsKey(CallModel.SIGNALING_EXTRA_KEY_VERSION)) {
                callModel.version = ((Double) extraMap.get(CallModel.SIGNALING_EXTRA_KEY_VERSION)).intValue();
            }
            if (extraMap.containsKey(CallModel.SIGNALING_EXTRA_KEY_CALL_TYPE)) {
                callModel.callType = ((Double) extraMap.get(CallModel.SIGNALING_EXTRA_KEY_CALL_TYPE)).intValue();
                mCurCallType = callModel.callType;
            }
            if (extraMap.containsKey(CallModel.SIGNALING_EXTRA_KEY_CALL_END)) {
                preExitRoom(null);
                return;
            }
            if (extraMap.containsKey(CallModel.SIGNALING_EXTRA_KEY_ROOM_ID)) {
                callModel.roomId = ((Double) extraMap.get(CallModel.SIGNALING_EXTRA_KEY_ROOM_ID)).intValue();
            }
        } catch (JsonSyntaxException e) {
            TRTCLogger.e(TAG, "onReceiveNewInvitation JsonSyntaxException:" + e);
        }
        handleDialing(callModel, inviter);

        if (mCurCallID.equals(callModel.callId)) {
            mLastCallModel = (CallModel) callModel.clone();
        }
    }

    /**
     * TRTC的监听器
     */
    private TRTCCloudListener mTRTCCloudListener = new TRTCCloudListener() {
        @Override
        public void onError(int errCode, String errMsg, Bundle extraInfo) {
            TRTCLogger.e(TAG, "onError: " + errCode + " " + errMsg);
            stopCall();
            if (mTRTCInternalListenerManager != null) {
                mTRTCInternalListenerManager.onError(errCode, errMsg);
            }
        }

        @Override
        public void onEnterRoom(long result) {
            TRTCLogger.d(TAG, "onEnterRoom result:" + result);
            if (result < 0) {
                stopCall();
            } else {
                mIsInRoom = true;
            }
        }

        @Override
        public void onExitRoom(int reason) {
            TRTCLogger.d(TAG, "onExitRoom reason:" + reason);
        }

        @Override
        public void onRemoteUserEnterRoom(String userId) {
            TRTCLogger.d(TAG, "onRemoteUserEnterRoom userId:" + userId);
            mCurRoomRemoteUserSet.add(userId);
            // 只有单聊这个时间才是正确的，因为单聊只会有一个用户进群，群聊这个时间会被后面的人重置
            mEnterRoomTime = System.currentTimeMillis();
            if (mTRTCInternalListenerManager != null) {
                mTRTCInternalListenerManager.onUserEnter(userId);
            }
        }

        @Override
        public void onRemoteUserLeaveRoom(String userId, int reason) {
            TRTCLogger.d(TAG, "onRemoteUserLeaveRoom userId:" + userId + ", reason:" + reason);
            mCurRoomRemoteUserSet.remove(userId);
            mCurInvitedList.remove(userId);
            // 远端用户退出房间，需要判断本次通话是否结束
            if (mTRTCInternalListenerManager != null) {
                mTRTCInternalListenerManager.onUserLeave(userId);
            }
            preExitRoom(userId);
        }

        @Override
        public void onUserVideoAvailable(String userId, boolean available) {
            TRTCLogger.d(TAG, "onUserVideoAvailable userId:" + userId + ", available:" + available);
            if (mTRTCInternalListenerManager != null) {
                mTRTCInternalListenerManager.onUserVideoAvailable(userId, available);
            }
        }

        @Override
        public void onUserAudioAvailable(String userId, boolean available) {
            TRTCLogger.d(TAG, "onUserAudioAvailable userId:" + userId + ", available:" + available);
            if (mTRTCInternalListenerManager != null) {
                mTRTCInternalListenerManager.onUserAudioAvailable(userId, available);
            }
        }

        @Override
        public void onUserVoiceVolume(ArrayList<TRTCCloudDef.TRTCVolumeInfo> userVolumes, int totalVolume) {
            Map<String, Integer> volumeMaps = new HashMap<>();
            for (TRTCCloudDef.TRTCVolumeInfo info : userVolumes) {
                String userId = "";
                if (info.userId == null) {
                    userId = mCurUserId;
                } else {
                    userId = info.userId;
                }
                volumeMaps.put(userId, info.volume);
            }
            mTRTCInternalListenerManager.onUserVoiceVolume(volumeMaps);
        }
    };


    public TRTCCallingImpl(Context context) {
        mContext = context;
        mTRTCCloud = TRTCCloud.sharedInstance(context);
        mTRTCInternalListenerManager = new TRTCInternalListenerManager();
        mLastCallModel.version = CallModel.VALUE_PROTOCOL_VERSION;
    }

    private void startCall() {
        isOnCalling = true;
    }

    /**
     * 停止此次通话，把所有的变量都会重置
     */
    public void stopCall() {
        isOnCalling = false;
        mIsInRoom = false;
        mEnterRoomTime = 0;
        mCurCallID = "";
        mCurRoomID = 0;
        mCurInvitedList.clear();
        mCurRoomRemoteUserSet.clear();
        mCurSponsorForMe = "";
        mLastCallModel = new CallModel();
        mLastCallModel.version = CallModel.VALUE_PROTOCOL_VERSION;
        mCurGroupId = "";
        mCurCallType = TYPE_UNKNOWN;
    }

    /**
     * 这里会初始化IM，如果您的项目中已经使用了腾讯云IM，可以删除，不需要再次初始化
     */
    private void initIM() {
        if (SessionWrapper.isMainProcess(mContext.getApplicationContext())) {

            V2TIMSDKConfig config = new V2TIMSDKConfig();
            config.setLogLevel(V2TIMSDKConfig.V2TIM_LOG_DEBUG);
            //初始化 SDK
            mIsInitIMSDK = V2TIMManager.getInstance().initSDK(mContext.getApplicationContext(), mSdkAppId, config, new V2TIMSDKListener() {
                @Override
                public void onConnecting() {
                }

                @Override
                public void onConnectSuccess() {
                }

                @Override
                public void onConnectFailed(int code, String error) {
                    TRTCLogger.e(TAG, "init im sdk error.");
                }
            });
        }
    }

    public void handleDialing(CallModel callModel, String user) {
        if (!TextUtils.isEmpty(mCurCallID)) {
            // 正在通话时，收到了一个邀请我的通话请求,需要告诉对方忙线
            if (isOnCalling && callModel.invitedList.contains(mCurUserId)) {
                sendModel(user, CallModel.VIDEO_CALL_ACTION_LINE_BUSY, callModel);
                return;
            }
            // 与对方处在同一个群中，此时收到了邀请群中其他人通话的请求，界面上展示连接动画
            if (!TextUtils.isEmpty(mCurGroupId) && !TextUtils.isEmpty(callModel.groupId)) {
                if (mCurGroupId.equals(callModel.groupId)) {
                    mCurInvitedList.addAll(callModel.invitedList);
                    if (mTRTCInternalListenerManager != null) {
                        mTRTCInternalListenerManager.onGroupCallInviteeListUpdate(mCurInvitedList);
                    }
                    return;
                }
            }
        }

        // 虽然是群组聊天，但是对方并没有邀请我，我不做处理
        if (!TextUtils.isEmpty(callModel.groupId) && !callModel.invitedList.contains(mCurUserId)) {
            return;
        }

        // 开始接通电话
        startCall();
        mCurCallID = callModel.callId;
        mCurRoomID = callModel.roomId;
        mCurCallType = callModel.callType;
        mCurSponsorForMe = user;
        mCurGroupId = callModel.groupId;
        // 邀请列表中需要移除掉自己
        callModel.invitedList.remove(mCurUserId);
        List<String> onInvitedUserListParam = callModel.invitedList;
        if (!TextUtils.isEmpty(mCurGroupId)) {
            mCurInvitedList.addAll(callModel.invitedList);
        }
        if (mTRTCInternalListenerManager != null) {
            mTRTCInternalListenerManager.onInvited(user, onInvitedUserListParam, !TextUtils.isEmpty(mCurGroupId), mCurCallType);
        }
    }

    @Override
    public void destroy() {
        //必要的清除逻辑
        V2TIMManager.getSignalingManager().removeSignalingListener(mTIMSignallingListener);
        mTRTCCloud.stopLocalPreview();
        mTRTCCloud.stopLocalAudio();
        mTRTCCloud.exitRoom();
    }

    @Override
    public void addDelegate(TRTCCallingDelegate delegate) {
        mTRTCInternalListenerManager.addDelegate(delegate);
    }

    @Override
    public void removeDelegate(TRTCCallingDelegate delegate) {
        mTRTCInternalListenerManager.removeDelegate(delegate);
    }

    @Override
    public void login(int sdkAppId, final String userId, final String userSig, final TRTCCalling.ActionCallBack callback) {
        TRTCLogger.i(TAG, "start login, sdkAppId:" + sdkAppId + " userId:" + userId + " sign is empty:" + TextUtils.isEmpty(userSig));
        if (sdkAppId == 0 || TextUtils.isEmpty(userId) || TextUtils.isEmpty(userSig)) {
            TRTCLogger.e(TAG, "start login fail. params invalid.");
            if (callback != null) {
                callback.onError(-1, "login fail, params is invalid.");
            }
            return;
        }
        mSdkAppId = sdkAppId;
        //1. 未初始化 IM 先初始化 IM
        if (!mIsInitIMSDK) {
            initIM();
        }
        //2. 需要将监听器添加到IM上
        V2TIMManager.getSignalingManager().addSignalingListener(mTIMSignallingListener);

        String loginUser = V2TIMManager.getInstance().getLoginUser();
        if (loginUser != null && loginUser.equals(userId)) {
            TRTCLogger.d(TAG, "IM already login user：" + loginUser);
            mCurUserId = loginUser;
            mCurUserSig = userSig;
            if (callback != null) {
                callback.onSuccess();
            }
            return;
        }

        V2TIMManager.getInstance().login(userId, userSig, new V2TIMCallback() {
            @Override
            public void onError(int i, String s) {
                if (callback != null) {
                    callback.onError(i, s);
                }
            }

            @Override
            public void onSuccess() {
                mCurUserId = userId;
                mCurUserSig = userSig;
                if (callback != null) {
                    callback.onSuccess();
                }
            }
        });
    }

    @Override
    public void logout(final TRTCCalling.ActionCallBack callBack) {
        V2TIMManager.getInstance().logout(new V2TIMCallback() {
            @Override
            public void onError(int i, String s) {
                if (callBack != null) {
                    callBack.onError(i, s);
                }
            }

            @Override
            public void onSuccess() {
                if (callBack != null) {
                    callBack.onSuccess();
                }
            }
        });
        stopCall();
        exitRoom();
        mNickName = "";
        mFaceUrl = "";
    }

    @Override
    public void call(final String userId, int type) {
        TRTCLogger.i(TAG, "start single call " + userId + ", type " + type);
        if (TextUtils.isEmpty(userId)) {
            return;
        }
        List<String> list = new ArrayList<>();
        list.add(userId);
        internalCall(list, type, "", -1);
    }

    @Override
    public void groupCall(final List<String> userIdList, int type, String groupId, int roomId) {
        if (isCollectionEmpty(userIdList)) {
            return;
        }
        internalCall(userIdList, type, groupId, roomId);
    }

    /**
     * 统一的拨打逻辑
     *
     * @param userIdList 需要邀请的用户列表
     * @param type       邀请类型
     * @param groupId    群组通话的group id，如果是C2C需要传 ""
     * @param roomId
     */
    private void internalCall(final List<String> userIdList, int type, String groupId, int roomId) {
        final boolean isGroupCall = !TextUtils.isEmpty(groupId);
        if (!isOnCalling) {
            // 首次拨打电话，生成id，并进入trtc房间
            mCurRoomID = roomId;
            mCurGroupId = groupId;
            mCurCallType = type;
            TRTCLogger.d(TAG, "First calling, generate room id " + mCurRoomID);
            enterTRTCRoom();
            startCall();
        }
        // 非首次拨打，不能发起新的groupId通话
        if (!TextUtils.equals(mCurGroupId, groupId)) {
            return;
        }

        // 过滤已经邀请的用户id
        List<String> filterInvitedList = new ArrayList<>();
        for (String id : userIdList) {
            if (!mCurInvitedList.contains(id)) {
                filterInvitedList.add(id);
            }
        }
        // 如果当前没有需要邀请的id则返回
//        if (isCollectionEmpty(filterInvitedList)) {
//            return;
//        }

        mCurInvitedList.addAll(filterInvitedList);
        TRTCLogger.i(TAG, "groupCall: filter:" + filterInvitedList + " all:" + mCurInvitedList);
        // 填充通话信令的model
        mLastCallModel.action = CallModel.VIDEO_CALL_ACTION_DIALING;
        mLastCallModel.invitedList = mCurInvitedList;
        mLastCallModel.roomId = mCurRoomID;
        mLastCallModel.groupId = mCurGroupId;
        mLastCallModel.callType = mCurCallType;

        // 首次拨打电话，生成id
        if (!TextUtils.isEmpty(mCurGroupId)) {
            // 群聊发送群消息
            mCurCallID = sendModel("", CallModel.VIDEO_CALL_ACTION_DIALING);
        } else {
            // 单聊发送C2C消息
            for (final String userId : filterInvitedList) {
                mCurCallID = sendModel(userId, CallModel.VIDEO_CALL_ACTION_DIALING);
            }
        }
        mLastCallModel.callId = mCurCallID;
    }

    /**
     * 重要：用于判断是否需要结束本次通话
     * 在用户超时、拒绝、忙线、有人退出房间时需要进行判断
     */
    private void preExitRoom(String leaveUser) {
        TRTCLogger.i(TAG, "preExitRoom: " + mCurRoomRemoteUserSet + " " + mCurInvitedList);
        if (mCurRoomRemoteUserSet.isEmpty() && mCurInvitedList.isEmpty() && mIsInRoom) {
            // 当没有其他用户在房间里了，则结束通话。
            if (!TextUtils.isEmpty(leaveUser)) {
                if (TextUtils.isEmpty(mCurGroupId)) {
                    sendModel(leaveUser, CallModel.VIDEO_CALL_ACTION_HANGUP);
                } else {
                    sendModel("", CallModel.VIDEO_CALL_ACTION_HANGUP);
                }
            }
            exitRoom();
            stopCall();
            if (mTRTCInternalListenerManager != null) {
                mTRTCInternalListenerManager.onCallEnd();
            }
        }
    }

    /**
     * trtc 退房
     */
    private void exitRoom() {
        mTRTCCloud.stopLocalPreview();
        mTRTCCloud.stopLocalAudio();
        mTRTCCloud.exitRoom();
    }

    @Override
    public void accept() {
        sendModel(mCurSponsorForMe, CallModel.VIDEO_CALL_ACTION_ACCEPT);
        enterTRTCRoom();
    }

    /**
     * trtc 进房
     */
    private void enterTRTCRoom() {
        if (mCurCallType == TRTCCalling.TYPE_VIDEO_CALL) {
            // 开启基础美颜
            TXBeautyManager txBeautyManager = mTRTCCloud.getBeautyManager();
            // 自然美颜
            txBeautyManager.setBeautyStyle(1);
            txBeautyManager.setBeautyLevel(6);
            // 进房前需要设置一下关键参数
            TRTCCloudDef.TRTCVideoEncParam encParam = new TRTCCloudDef.TRTCVideoEncParam();
            encParam.videoResolution = TRTCCloudDef.TRTC_VIDEO_RESOLUTION_960_540;
            encParam.videoFps = 15;
            encParam.videoBitrate = 1000;
            encParam.videoResolutionMode = TRTCCloudDef.TRTC_VIDEO_RESOLUTION_MODE_PORTRAIT;
            encParam.enableAdjustRes = true;
            mTRTCCloud.setVideoEncoderParam(encParam);
        }
        TRTCLogger.i(TAG, "enterTRTCRoom: " + mCurUserId + " room:" + mCurRoomID);
        TRTCCloudDef.TRTCParams TRTCParams = new TRTCCloudDef.TRTCParams(mSdkAppId, mCurUserId, mCurUserSig, mCurRoomID, "", "");
        TRTCParams.role = TRTCCloudDef.TRTCRoleAnchor;
        mTRTCCloud.enableAudioVolumeEvaluation(300);
        mTRTCCloud.setAudioRoute(TRTCCloudDef.TRTC_AUDIO_ROUTE_SPEAKER);
        mTRTCCloud.startLocalAudio();
        // 收到来电，开始监听 trtc 的消息
        mTRTCCloud.setListener(mTRTCCloudListener);
        mTRTCCloud.enterRoom(TRTCParams, mCurCallType == TRTCCalling.TYPE_VIDEO_CALL ? TRTCCloudDef.TRTC_APP_SCENE_VIDEOCALL : TRTCCloudDef.TRTC_APP_SCENE_AUDIOCALL);
    }

    @Override
    public void reject(int currentTimeCount) {
        timeCount = currentTimeCount;
        sendModel(mCurSponsorForMe, CallModel.VIDEO_CALL_ACTION_REJECT);
        stopCall();
    }

    @Override
    public void timeOut() {
        sendModel(mCurSponsorForMe, CallModel.VIDEO_CALL_TIMEOUT_CANCEL);
        stopCall();
        exitRoom();
    }

    @Override
    public void hangup(int currentTimeCount) {
        // 1. 如果还没有在通话中，说明还没有接通，所以直接拒绝了
        if (!isOnCalling) {
            if (currentTimeCount != -1010) {
                reject(0);
            }
            return;
        }
        boolean fromGroup = (!TextUtils.isEmpty(mCurGroupId));
        if (fromGroup) {
            timeCount = currentTimeCount;
            if (currentTimeCount != -1010) {
                if (currentTimeCount == 0) {
                    sendModel(mCurSponsorForMe, CallModel.VIDEO_CALL_ACTION_SPONSOR_CANCEL);
                } else {
                    sendModel(mCurSponsorForMe, CallModel.VIDEO_CALL_ACTION_HANGUP);
                }
            }
            TRTCLogger.d(TAG, "groupHangup");
            groupHangup();
        } else {
            TRTCLogger.d(TAG, "singleHangup");
            singleHangup();
        }
    }

    private void groupHangup() {
//        if (isCollectionEmpty(mCurRoomRemoteUserSet)) {
//            // 当前以及没有人在通话了，直接向群里发送一个取消消息
//            sendModel("", CallModel.VIDEO_CALL_ACTION_SPONSOR_CANCEL);
//        }
        stopCall();
        exitRoom();
    }

    private void singleHangup() {
        for (String id : mCurInvitedList) {
            sendModel(id, CallModel.VIDEO_CALL_ACTION_SPONSOR_CANCEL);
        }
        stopCall();
        exitRoom();
    }

    @Override
    public void openCamera(boolean isFrontCamera, TXCloudVideoView txCloudVideoView) {
        if (txCloudVideoView == null) {
            return;
        }
        mIsUseFrontCamera = isFrontCamera;
        mTRTCCloud.startLocalPreview(isFrontCamera, txCloudVideoView);
    }

    @Override
    public void closeCamera() {
        mTRTCCloud.stopLocalPreview();
    }

    @Override
    public void startRemoteView(String userId, TXCloudVideoView txCloudVideoView) {
        if (txCloudVideoView == null) {
            return;
        }
        mTRTCCloud.startRemoteView(userId, txCloudVideoView);
    }

    @Override
    public void stopRemoteView(String userId) {
        mTRTCCloud.stopRemoteView(userId);
    }

    @Override
    public void switchCamera(boolean isFrontCamera) {
        if (mIsUseFrontCamera == isFrontCamera) {
            return;
        }
        mIsUseFrontCamera = isFrontCamera;
        mTRTCCloud.switchCamera();
    }

    @Override
    public void setMicMute(boolean isMute) {
        mTRTCCloud.muteLocalAudio(isMute);
    }

    @Override
    public void setHandsFree(boolean isHandsFree) {
        if (isHandsFree) {
            mTRTCCloud.setAudioRoute(TRTCCloudDef.TRTC_AUDIO_ROUTE_SPEAKER);
        } else {
            mTRTCCloud.setAudioRoute(TRTCCloudDef.TRTC_AUDIO_ROUTE_EARPIECE);
        }
    }

    private String sendModel(final String user, int action) {
        return sendModel(user, action, null);
    }

    private void sendOnlineMessageWithOfflinePushInfo(final String user, final CallModel model) {
        List<String> users = new ArrayList<String>();
        users.add(V2TIMManager.getInstance().getLoginUser());
        if (!TextUtils.isEmpty(mNickName)) {
            sendOnlineMessageWithOfflinePushInfo(user, mNickName, model);
            return;
        }
        V2TIMManager.getInstance().getUsersInfo(users, new V2TIMValueCallback<List<V2TIMUserFullInfo>>() {

            @Override
            public void onError(int code, String desc) {
                TRTCLogger.e(TAG, "getUsersInfo err code = " + code + ", desc = " + desc);
                sendOnlineMessageWithOfflinePushInfo(user, null, model);
            }

            @Override
            public void onSuccess(List<V2TIMUserFullInfo> v2TIMUserFullInfos) {
                if (v2TIMUserFullInfos == null || v2TIMUserFullInfos.size() == 0) {
                    sendOnlineMessageWithOfflinePushInfo(user, null, model);
                    return;
                }
                mNickName = v2TIMUserFullInfos.get(0).getNickName();
                mFaceUrl = v2TIMUserFullInfos.get(0).getFaceUrl();
                sendOnlineMessageWithOfflinePushInfo(user, v2TIMUserFullInfos.get(0).getNickName(), model);
            }
        });
    }

    private String getCallId() {
        return mCurCallID;
    }

    private void sendOnlineMessageWithOfflinePushInfo(String userId, String nickname, CallModel model) {
        OfflineMessageContainerBean containerBean = new OfflineMessageContainerBean();
        OfflineMessageBean entity = new OfflineMessageBean();
        entity.content = new Gson().toJson(model);
        entity.sender = V2TIMManager.getInstance().getLoginUser(); // 发送者肯定是登录账号
        entity.action = OfflineMessageBean.REDIRECT_ACTION_CALL;
        entity.sendTime = System.currentTimeMillis() / 1000;
        entity.nickname = nickname;
        entity.faceUrl = mFaceUrl;
        containerBean.entity = entity;
        List<String> invitedList = new ArrayList<>();
        final boolean isGroup = (!TextUtils.isEmpty(model.groupId));
        if (isGroup) {
            entity.chatType = TIMConversationType.Group.value();
            invitedList.addAll(model.invitedList);
        } else {
            invitedList.add(userId);
        }

        V2TIMOfflinePushInfo v2TIMOfflinePushInfo = new V2TIMOfflinePushInfo();
        v2TIMOfflinePushInfo.setExt(new Gson().toJson(containerBean).getBytes());
        // OPPO必须设置ChannelID才可以收到推送消息，这个channelID需要和控制台一致
        v2TIMOfflinePushInfo.setAndroidOPPOChannelID("tuikit");
        v2TIMOfflinePushInfo.setDesc("您有一个通话请求");
        v2TIMOfflinePushInfo.setTitle(nickname);
        MessageCustom customMessage = new MessageCustom();
        customMessage.businessID = MessageCustom.BUSINESS_ID_AV_CALL;
        V2TIMMessage messageV2TIMMessage = V2TIMManager.getMessageManager().createCustomMessage(new Gson().toJson(customMessage).getBytes());

        for (String receiver : invitedList) {
            TRTCLogger.i(TAG, "sendOnlineMessage to " + receiver);
            V2TIMManager.getMessageManager().sendMessage(messageV2TIMMessage, receiver, null, V2TIMMessage.V2TIM_PRIORITY_DEFAULT,
                    true, v2TIMOfflinePushInfo, new V2TIMSendCallback<V2TIMMessage>() {

                        @Override
                        public void onError(int code, String desc) {
                            TRTCLogger.e(TAG, "sendOnlineMessage failed, code:" + code + ", desc:" + desc);
                        }

                        @Override
                        public void onSuccess(V2TIMMessage v2TIMMessage) {
                            TRTCLogger.i(TAG, "sendOnlineMessage msgId:" + v2TIMMessage.getMsgID());
                        }

                        @Override
                        public void onProgress(int progress) {

                        }
                    });
        }
    }

    /**
     * 信令发送函数，当CallModel 存在groupId时会向群组发送信令
     *
     * @param user
     * @param action
     * @param model
     */
    private String sendModel(final String user, int action, CallModel model) {
        String callID = null;
        final CallModel realCallModel;
        if (model != null) {
            realCallModel = (CallModel) model.clone();
            realCallModel.action = action;
        } else {
            realCallModel = generateModel(action);
        }

        final boolean isGroup = (!TextUtils.isEmpty(realCallModel.groupId));
        if (action == CallModel.VIDEO_CALL_ACTION_HANGUP && mEnterRoomTime != 0 && !isGroup) {
            realCallModel.duration = (int) (System.currentTimeMillis() - mEnterRoomTime) / 1000;
            mEnterRoomTime = 0;
        }
        String receiver = "";
        String groupId = "";
        if (isGroup) {
            groupId = realCallModel.groupId;
        } else {
            receiver = user;
        }
        Map<String, Object> customMap = new HashMap();
        customMap.put(CallModel.SIGNALING_EXTRA_KEY_VERSION, CallModel.VALUE_PROTOCOL_VERSION);
        customMap.put(CallModel.SIGNALING_EXTRA_KEY_CALL_TYPE, realCallModel.callType);
        // signalling
        switch (realCallModel.action) {
            case CallModel.VIDEO_CALL_ACTION_DIALING:
                customMap.put(CallModel.SIGNALING_EXTRA_KEY_ROOM_ID, realCallModel.roomId);
                String dialingDataStr = new Gson().toJson(customMap);
                if (isGroup) {
                    V2TIMOfflinePushInfo info = new V2TIMOfflinePushInfo();
                    info.setDesc("desc");
                    // 之前是 信令消息 ，根据小程序业务逻辑需要改成发送 自定义消息
                    VideoExtra message = new VideoExtra(receiver, 3, mCurRoomID, 0,
                            Long.parseLong(timeCount + ""), new ArrayList<>());
                    IMVideoMessage imVideoMessage = new IMVideoMessage();
                    imVideoMessage.setKey(Constants.HEALTH_VIDEO);
                    imVideoMessage.setContent(message);
                    String jsonString = new Gson().toJson(imVideoMessage);
                    callID = V2TIMManager.getInstance().sendGroupCustomMessage(jsonString.getBytes(),
                            groupId, V2TIMMessage.V2TIM_PRIORITY_HIGH, new V2TIMValueCallback<V2TIMMessage>() {
                                @Override
                                public void onError(int i, String s) {
                                    TRTCLogger.e(TAG, "send c2c msg fail, code:" + i + " msg:" + s);
                                }

                                @Override
                                public void onSuccess(V2TIMMessage v2TIMMessage) {
                                    TRTCLogger.i(TAG, "send c2c msg success.");
                                    realCallModel.callId = getCallId();
                                    realCallModel.timeout = TIME_OUT_COUNT;
                                    realCallModel.version = CallModel.VALUE_PROTOCOL_VERSION;
                                }
                            });
                }
                break;
            case CallModel.VIDEO_CALL_ACTION_ACCEPT:
                String acceptDataStr = new Gson().toJson(customMap);
                V2TIMManager.getSignalingManager().accept(realCallModel.callId, acceptDataStr, new V2TIMCallback() {
                    @Override
                    public void onError(int code, String desc) {
                        TRTCLogger.e(TAG, "accept callID:" + realCallModel.callId + ", error:" + code + " desc:" + desc);
                    }

                    @Override
                    public void onSuccess() {
                        TRTCLogger.d(TAG, "accept success callID:" + realCallModel.callId);
                    }
                });
                break;

            case CallModel.VIDEO_CALL_ACTION_REJECT:
                VideoExtra message = new VideoExtra(receiver, 3, mCurRoomID, 5, 0L, new ArrayList<>());
                IMVideoMessage imVideoMessage = new IMVideoMessage();
                imVideoMessage.setKey(Constants.HEALTH_VIDEO);
                imVideoMessage.setContent(message);
                String jsonString = new Gson().toJson(imVideoMessage);
                callID = V2TIMManager.getInstance().sendGroupCustomMessage(jsonString.getBytes(),
                        groupId, V2TIMMessage.V2TIM_PRIORITY_HIGH, new V2TIMValueCallback<V2TIMMessage>() {
                            @Override
                            public void onError(int i, String s) {
                                TRTCLogger.e(TAG, "reject fail, code:" + i + " msg:" + s);
                            }

                            @Override
                            public void onSuccess(V2TIMMessage v2TIMMessage) {
                                TRTCLogger.d(TAG, "reject success callID:" + realCallModel.callId);
                            }
                        });
                break;

            case CallModel.VIDEO_CALL_TIMEOUT_CANCEL:
                VideoExtra messageTimeOutCancel = new VideoExtra(receiver, 3, mCurRoomID, 3, 0L, new ArrayList<>());
                IMVideoMessage imVideoMessageTimeOutCancel = new IMVideoMessage();
                imVideoMessageTimeOutCancel.setKey(Constants.HEALTH_VIDEO);
                imVideoMessageTimeOutCancel.setContent(messageTimeOutCancel);
                String jsonStringTimeOutCancel = new Gson().toJson(imVideoMessageTimeOutCancel);
                callID = V2TIMManager.getInstance().sendGroupCustomMessage(jsonStringTimeOutCancel.getBytes(),
                        groupId, V2TIMMessage.V2TIM_PRIORITY_HIGH, new V2TIMValueCallback<V2TIMMessage>() {
                            @Override
                            public void onError(int i, String s) {
                                TRTCLogger.e(TAG, "reject fail, code:" + i + " msg:" + s);
                            }

                            @Override
                            public void onSuccess(V2TIMMessage v2TIMMessage) {
                                TRTCLogger.d(TAG, "reject success callID:" + realCallModel.callId);
                            }
                        });
                break;

            case CallModel.VIDEO_CALL_ACTION_LINE_BUSY:
                customMap.put(CallModel.SIGNALING_EXTRA_KEY_LINE_BUSY, CallModel.SIGNALING_EXTRA_KEY_LINE_BUSY);
                String lineBusyMapStr = new Gson().toJson(customMap);
                V2TIMManager.getSignalingManager().reject(realCallModel.callId, lineBusyMapStr, new V2TIMCallback() {
                    @Override
                    public void onError(int code, String desc) {
                        TRTCLogger.e(TAG, "reject  callID:" + realCallModel.callId + ", error:" + code + " desc:" + desc);
                    }

                    @Override
                    public void onSuccess() {
                        TRTCLogger.d(TAG, "reject success callID:" + realCallModel.callId);
                    }
                });
                break;

            case CallModel.VIDEO_CALL_ACTION_SPONSOR_CANCEL:
                VideoExtra messageCancel = new VideoExtra(receiver, 3, mCurRoomID, 1, 0L, new ArrayList<>());
                IMVideoMessage imVideoMessageCancel = new IMVideoMessage();
                imVideoMessageCancel.setKey(Constants.HEALTH_VIDEO);
                imVideoMessageCancel.setContent(messageCancel);
                String jsonStringCancel = new Gson().toJson(imVideoMessageCancel);
                callID = V2TIMManager.getInstance().sendGroupCustomMessage(jsonStringCancel.getBytes(),
                        groupId, V2TIMMessage.V2TIM_PRIORITY_HIGH, new V2TIMValueCallback<V2TIMMessage>() {
                            @Override
                            public void onError(int i, String s) {
                                TRTCLogger.e(TAG, "cancel fail, code:" + i + " msg:" + s);
                            }

                            @Override
                            public void onSuccess(V2TIMMessage v2TIMMessage) {
                                TRTCLogger.d(TAG, "cancel success callID:" + realCallModel.callId);
                            }
                        });
                break;

            case CallModel.VIDEO_CALL_ACTION_HANGUP:
                customMap.put(CallModel.SIGNALING_EXTRA_KEY_CALL_END, realCallModel.duration);
                String hangupMapStr = new Gson().toJson(customMap);
                if (isGroup) {
                    VideoExtra messageHangUp = new VideoExtra(receiver, 3, mCurRoomID, 5, Long.parseLong(timeCount + ""), new ArrayList<>());
                    IMVideoMessage imVideoMessageHangUp = new IMVideoMessage();
                    imVideoMessageHangUp.setKey(Constants.HEALTH_VIDEO);
                    imVideoMessageHangUp.setContent(messageHangUp);
                    String jsonStringHangUp = new Gson().toJson(imVideoMessageHangUp);
                    callID = V2TIMManager.getInstance().sendGroupCustomMessage(jsonStringHangUp.getBytes(),
                            groupId, V2TIMMessage.V2TIM_PRIORITY_HIGH, new V2TIMValueCallback<V2TIMMessage>() {
                                @Override
                                public void onError(int i, String s) {
                                    TRTCLogger.e(TAG, "reject fail, code:" + i + " msg:" + s);
                                }

                                @Override
                                public void onSuccess(V2TIMMessage v2TIMMessage) {
                                    TRTCLogger.d(TAG, "reject success callID:" + realCallModel.callId);
                                }
                            });
                } else {
                    V2TIMOfflinePushInfo info = new V2TIMOfflinePushInfo();
                    info.setDesc("desc");
                    V2TIMManager.getSignalingManager().invite(receiver, hangupMapStr, false, info, 0, new V2TIMCallback() {
                        @Override
                        public void onError(int code, String desc) {
                            TRTCLogger.e(TAG, "invite-->hangup callID: " + realCallModel.callId + ", error:" + code + " desc:" + desc);
                        }

                        @Override
                        public void onSuccess() {
                            TRTCLogger.d(TAG, "invite-->hangup success callID:" + realCallModel.callId);
                        }
                    });
                }
                break;

            default:
                break;
        }

        // 最后需要重新赋值
        if (realCallModel.action != CallModel.VIDEO_CALL_ACTION_REJECT &&
                realCallModel.action != CallModel.VIDEO_CALL_ACTION_HANGUP &&
                realCallModel.action != CallModel.VIDEO_CALL_ACTION_SPONSOR_CANCEL &&
                model == null) {
            mLastCallModel = (CallModel) realCallModel.clone();
        }
        return callID;
    }

    private CallModel generateModel(int action) {
        CallModel callModel = (CallModel) mLastCallModel.clone();
        callModel.action = action;
        return callModel;
    }

    private static boolean isCollectionEmpty(Collection coll) {
        return coll == null || coll.size() == 0;
    }

    private static int generateRoomID() {
        Random random = new Random();
        return random.nextInt(ROOM_ID_MAX - ROOM_ID_MIN + 1) + ROOM_ID_MIN;
    }

}
