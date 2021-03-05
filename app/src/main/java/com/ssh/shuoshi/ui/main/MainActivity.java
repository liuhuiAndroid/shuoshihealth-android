package com.ssh.shuoshi.ui.main;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.core.app.NotificationCompat;

import com.google.gson.Gson;
import com.gyf.barlibrary.ImmersionBar;
import com.ssh.shuoshi.R;
import com.ssh.shuoshi.bean.ImageDiagnoseBean;
import com.ssh.shuoshi.bean.JPushMessageExtra;
import com.ssh.shuoshi.bean.team.HisDoctorExpertTeamMemberDtosBean;
import com.ssh.shuoshi.eventbus.AliasOperatorEvent;
import com.ssh.shuoshi.eventbus.ChangeEvent;
import com.ssh.shuoshi.eventbus.NotificationExtras2Event;
import com.ssh.shuoshi.eventbus.NotificationExtrasEvent;
import com.ssh.shuoshi.eventbus.SwitchEvent;
import com.ssh.shuoshi.injector.HasComponent;
import com.ssh.shuoshi.ui.BaseActivity;
import com.ssh.shuoshi.ui.BaseMainFragment;
import com.ssh.shuoshi.ui.authority.one.AuthorityOneActivity;
import com.ssh.shuoshi.ui.dialog.AgreementTipsDialog;
import com.ssh.shuoshi.ui.fragment1.FirstFragment;
import com.ssh.shuoshi.ui.fragment2.SecondFragment;
import com.ssh.shuoshi.ui.graphicinquiry.GraphicInquiryActivity;
import com.ssh.shuoshi.ui.messagecenter.MessageCenterActivity;
import com.ssh.shuoshi.ui.myprescription.main.MyPrescriptionActivity;
import com.ssh.shuoshi.ui.worksetting.WorkSettingActivity;
import com.ssh.shuoshi.util.SPUtil;
import com.tencent.imsdk.utils.IMFunc;
import com.tencent.imsdk.v2.V2TIMAdvancedMsgListener;
import com.tencent.imsdk.v2.V2TIMManager;
import com.tencent.imsdk.v2.V2TIMMessage;
import com.tencent.imsdk.v2.V2TIMMessageReceipt;
import com.tencent.mmkv.MMKV;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Set;

import javax.inject.Inject;

import butterknife.BindView;
import me.yokeyword.fragmentation.SupportFragment;

import static com.ssh.shuoshi.MyApplication.CHAT;

public class MainActivity extends BaseActivity implements MainContract.View,
        HasComponent<MainComponent>, View.OnClickListener, BaseMainFragment.OnBackToFirstListener {

    @BindView(R.id.frame_layout)
    FrameLayout frameLayout;
    @BindView(R.id.home_imag)
    ImageView homeImag;
    @BindView(R.id.home)
    LinearLayout home;
    @BindView(R.id.me_imag)
    ImageView meImag;
    @BindView(R.id.me)
    LinearLayout me;
    @BindView(R.id.linear)
    LinearLayout linear;

    @Inject
    MainPresenter mPresenter;
    @Inject
    SPUtil mSPUtil;

    public static final int FIRST = 0;
    public static final int SECOND = 1;

    private SupportFragment[] mFragments = new SupportFragment[2];
    private int lastfragment; //用于记录上个选择的fragment
    private MainComponent mMainComponent;

    @Override
    public int initContentView() {
        return R.layout.activity_main;
    }

    @Override
    public void setStatusBar() {
        mImmersionBar = ImmersionBar.with(this);
        mImmersionBar.transparentStatusBar()  //透明状态栏，不写默认透明色
                .statusBarDarkFont(true)   //状态栏字体是深色，不写默认为亮色
                .init();
    }

    @Override
    protected void initInjector() {
        mMainComponent = DaggerMainComponent.builder()
                .applicationComponent(getApplicationComponent())
                .activityModule(getActivityModule())
                .mainModule(new MainModule(this))
                .build();
        mMainComponent.inject(this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        initPush(intent);
    }

    @Override
    protected void initUiAndListener() {
        V2TIMManager.getMessageManager().addAdvancedMsgListener(cV2TIMSimpleMsgListener);
        EventBus.getDefault().register(this);
        mPresenter.attachView(this);
        home.setSelected(true);
        home.setOnClickListener(this);
        me.setOnClickListener(this);

        lastfragment = 0;
        SupportFragment firstFragment = findFragment(FirstFragment.class);
        if (firstFragment == null) {
            mFragments[FIRST] = FirstFragment.newInstance();
            mFragments[SECOND] = SecondFragment.newInstance();

            loadMultipleRootFragment(R.id.frame_layout, FIRST,
                    mFragments[FIRST],
                    mFragments[SECOND]
            );
        } else {
            mFragments[FIRST] = firstFragment;
            mFragments[SECOND] = findFragment(SecondFragment.class);
        }

        initPush(getIntent());
        showAgreementDialog();
    }

    private void showAgreementDialog() {
        boolean notFirstLogin = MMKV.defaultMMKV().getBoolean("notFirstLogin", false);
        if (!notFirstLogin) {
            AgreementTipsDialog dialog = new AgreementTipsDialog(MainActivity.this, R.style.dialog_physician_certification);
            dialog.setCanceledOnTouchOutside(false);
            dialog.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
            dialog.getWindow().getDecorView().setOnSystemUiVisibilityChangeListener(visibility -> {
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
            });
            dialog.setOnItemClickListener(new AgreementTipsDialog.ItemClickListener() {
                @Override
                public void cancel() {
                    dialog.dismiss();
                    finish();
                }

                @Override
                public void choose() {
                    dialog.dismiss();
                    MMKV.defaultMMKV().putBoolean("notFirstLogin", true);
                }
            });
            dialog.show();
        }

    }

    private void initPush(Intent intent) {
        // 极光推送点击事件处理
        String data = null;
        if (intent.getData() != null) {
            data = intent.getData().toString();
            Log.i("initPush", "initPush:-------2 intent.data:" + data);
        }
        //获取fcm、小米、oppo、vivo平台附带的jpush信息
        if (TextUtils.isEmpty(data) && getIntent().getExtras() != null) {
            data = getIntent().getExtras().getString("JMessageExtra");
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

        testPushLog(intent);
    }

    private void testPushLog(Intent intent) {
        // 打印推送日志
        Log.i("initPush", "initPush:-------2 intent.getExtras:" + intent.getExtras());
        if (intent.getExtras() != null) {
            Log.i("initPush", "initPush:-------2 intent.getExtras:" + intent.getExtras());
            Set<String> strings = intent.getExtras().keySet();
            for (String str : strings) {
                Log.i("initPush", "initPush:-------2 intent.getExtras:strings:" + str);
            }
            String jMessageExtra = intent.getExtras().getString("JMessageExtra");
            Log.i("initPush", "initPush:-------2 intent jMessageExtra:" + jMessageExtra);
            if (jMessageExtra != null) {
                Log.i("initPush", "initPush:-------3");
                Log.i("initPush", "jMessageExtra:" + jMessageExtra);
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onSwitchEvent(SwitchEvent event) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (event != null) {
                    if (event.getType().equals("change")) {
                        me.setSelected(true);
                        home.setSelected(false);
                        if (lastfragment != 1) {
                            showHideFragment(mFragments[SECOND], mFragments[lastfragment]);
                            lastfragment = 1;
                        }
                    }
                }
            }
        });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventRate(ChangeEvent event) {
        if (event != null) {
            int type = event.getChangeType();
            if (type == 2) {
                mPresenter.getDoctorInfo();
            }
        }
    }

    @Override
    public MainComponent getComponent() {
        return mMainComponent;
    }

    @Override
    public void onError(Throwable throwable) {
        loadError(throwable);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.detachView();
        EventBus.getDefault().unregister(this);
        V2TIMManager.getMessageManager().removeAdvancedMsgListener(cV2TIMSimpleMsgListener);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.home:
                home.setSelected(true);
                me.setSelected(false);
                if (lastfragment != 0) {
                    showHideFragment(mFragments[FIRST], mFragments[lastfragment]);
                    lastfragment = 0;
                }
                break;
            case R.id.me:
                me.setSelected(true);
                home.setSelected(false);
                if (lastfragment != 1) {
                    showHideFragment(mFragments[SECOND], mFragments[lastfragment]);
                    lastfragment = 1;
                }
                break;
        }
    }

    @Override
    public void onBackToFirstFragment() {

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventRate(AliasOperatorEvent event) {
        boolean firstLogin = MMKV.defaultMMKV().getBoolean("firstLogin", false);
        if (firstLogin) {
            mPresenter.jpushSysMsgRecordPushNewDoctor();
            MMKV.defaultMMKV().putBoolean("firstLogin", false);
        }
    }

    private void jumpFromJPush(int msgType, Integer bussinessId) {
        Log.i("initPush", "jumpFromJPush:-------msgType == " + msgType);
        //  1资质认证提醒，2资质认证审核通过，3资质认证审核不通过，4设置随访提醒，5新咨询订单通知，6患者咨询提醒，7处方审核不通过提醒
        if (msgType == 1) {
            Intent i = new Intent(MainActivity.this, AuthorityOneActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(i);
        } else if (msgType == 2) {
            Intent i = new Intent(MainActivity.this, WorkSettingActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(i);
        } else if (msgType == 3) {
            Intent i = new Intent(MainActivity.this, AuthorityOneActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            i.putExtra("isShow", true);
            startActivity(i);
        } else if (msgType == 4) {
            Intent i = new Intent(MainActivity.this, MessageCenterActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(i);
        } else if (msgType == 5) {
            // 点击PUSH的消息通知，进入与该患者的咨询对话页面。
            int loginStatus = V2TIMManager.getInstance().getLoginStatus();
            if (loginStatus == V2TIMManager.V2TIM_STATUS_LOGINED) {
                mPresenter.getConsultationInfoJpush(bussinessId);
            } else {
                EventBus.getDefault().post(new NotificationExtras2Event(msgType, bussinessId));
            }
        } else if (msgType == 6) {
            int loginStatus = V2TIMManager.getInstance().getLoginStatus();
            if (loginStatus == V2TIMManager.V2TIM_STATUS_LOGINED) {
                mPresenter.getConsultationInfoJpush(bussinessId);
            } else {
                EventBus.getDefault().post(new NotificationExtras2Event(msgType, bussinessId));
            }
        } else if (msgType == 7) {
            Intent i = new Intent(MainActivity.this, MyPrescriptionActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(i);
        }
    }

    /**
     * 自定义消息监听
     */
    private V2TIMAdvancedMsgListener cV2TIMSimpleMsgListener = new V2TIMAdvancedMsgListener() {

        @Override
        public void onRecvNewMessage(V2TIMMessage msg) {
            super.onRecvNewMessage(msg);
            int currentId = Integer.parseInt(msg.getGroupID().split("_")[1]);
            // 离线消息标题和内容
            String title = msg.getOfflinePushInfo().getTitle();
            String content = msg.getOfflinePushInfo().getDesc();
            if (TextUtils.isEmpty(title) || TextUtils.isEmpty(content) || content.equals("[自定义消息]")) {
                // 有一个为空不弹出来
            } else {
                if (currentId != GraphicInquiryActivity.mId) {
                    int pushId = 1;
                    NotificationManager mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                    NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(MainActivity.this, CHAT);
                    Intent notificationIntent = new Intent(MainActivity.this, GraphicInquiryActivity.class);
                    ImageDiagnoseBean.RowsBean rowsBean = new ImageDiagnoseBean.RowsBean();
                    rowsBean.setId(currentId);
                    notificationIntent.putExtra("rowsBean", rowsBean);
                    PendingIntent intent = PendingIntent.getActivity(MainActivity.this, 0,
                            notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                    mBuilder.setContentTitle(title)//设置通知栏标题
                            .setContentText(content)
                            .setContentIntent(intent) //设置通知栏单击意图
                            .setTicker(title + ":" + content) //通知首次出现在通知栏，带上升动画效果的
                            .setWhen(System.currentTimeMillis())//通知产生的时间，会在通知信息里显示，一般是系统获取到的时间
                            .setDefaults(Notification.DEFAULT_ALL)//向通知添加声音、闪灯和振动效果的最简单、最一致的方式是使用当前的用户默认设置，使用 defaults 属性，可以组合
                            .setSmallIcon(R.mipmap.youan)//设置通知小 ICON
                            .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.youan));//设置通知小 ICON
                    Notification notify = mBuilder.build();
                    notify.flags |= Notification.FLAG_AUTO_CANCEL;
                    mNotificationManager.notify(pushId, notify);

                    if (IMFunc.isBrandXiaoMi()) {
                        try {
                            Field field = notify.getClass().getDeclaredField("extraNotification");
                            Object extraNotification = field.get(notify);
                            Method method = extraNotification.getClass().getDeclaredMethod("setMessageCount", int.class);
                            method.invoke(extraNotification, 0);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
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

    @Override
    public void getConsultationInfoSuccess(ImageDiagnoseBean.RowsBean bean, int mId) {
        String senderStr = "咨询消息提醒";
        String contentStr = "患者" + bean.getPatientName() + "给您发送了信息，请及时查看";
        int pushId = 1;
        NotificationManager mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(MainActivity.this, CHAT);
        Intent notificationIntent = new Intent(MainActivity.this, GraphicInquiryActivity.class);
        ImageDiagnoseBean.RowsBean rowsBean = new ImageDiagnoseBean.RowsBean();
        rowsBean.setId(mId);
        notificationIntent.putExtra("rowsBean", rowsBean);
        PendingIntent intent = PendingIntent.getActivity(MainActivity.this, 0,
                notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder.setContentTitle(senderStr)//设置通知栏标题
                .setContentText(contentStr)
                .setContentIntent(intent) //设置通知栏单击意图
                .setTicker(senderStr + ":" + contentStr) //通知首次出现在通知栏，带上升动画效果的
                .setWhen(System.currentTimeMillis())//通知产生的时间，会在通知信息里显示，一般是系统获取到的时间
                .setDefaults(Notification.DEFAULT_ALL)//向通知添加声音、闪灯和振动效果的最简单、最一致的方式是使用当前的用户默认设置，使用 defaults 属性，可以组合
                .setSmallIcon(R.mipmap.youan)//设置通知小 ICON
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.youan));//设置通知小 ICON
        Notification notify = mBuilder.build();
        notify.flags |= Notification.FLAG_AUTO_CANCEL;
        mNotificationManager.notify(pushId, notify);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventRate(NotificationExtrasEvent event) {
        runOnUiThread(() -> jumpFromJPush(event.getMsgType(), event.getBussinessId()));
    }

    @Override
    public void getConsultationInfoJpushSuccess(ImageDiagnoseBean.RowsBean rowsBean, int mId) {
        Intent intent = new Intent(MainActivity.this, GraphicInquiryActivity.class);
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

}