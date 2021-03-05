package com.ssh.shuoshi;

/**
 * created by hwt on 2020/12/8
 */
public class Constants {

    public static final String HUAWEI_APPID = "103645731";
    public static final int HUAWEI_BUSINESSID = 15109; // 证书ID
    public static final String XIAOMI_APPKEY = "5761896819072";
    public static final String XIAOMI_APPID = "2882303761518968072";
    public static final int XIAOMI_BUSINESSID = 15108; // 证书ID

    //接口正常
    public static final int OK = 200;
    //用户未登录
    public static final int NO_USER = -99;
    //token过期
    public static final int TOKEN_EXPIRED = -999;
    //token失效
    public static final int TOKEN_INVALID = -111;
    //在其他地方登录了
    public static final int TOKEN_EXIST = -112;
    //用户被锁定
    public static final int TOKEN_FREEZE = -113;
    //token过期
    public static final int USER_SHOULD_RELOGIN = 401;

    public static String LH_LOG_PATH = "/Log/";// 日志默认保存目录

    public static final String BASE_URL = "http://health.s-sbio.com:8015/"; //测试地址   李彪
    //    public static final String BASE_URL = "http://health.s-sbio.com:8016/"; //测试地址  经理
    //    public static final String BASE_URL = "https://health.s-sbio.com/fat-api/"; //测试地址  经理

    // app类型：1:ios,2:andriod
    public static final int APPTYPE = 2;

    //服务端自定义API key、value
    public static String app_key = "B272F43387B8504C";
    public static String app_value = "70BAE8B491362AB39042B77C7653199D";

    public static final String PARAMETERS_CHECK_PASSWORD = "^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{6,16}$";//6-16位数字和字母的组合
    public static final String PARAMETERS_CHECK_LOGIN_NAME = "^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{3,16}$";//3-16位数字和字母的组合
    public static final String PARAMETERS_CHECK_IDENTITY_NO = "^(\\d{15}$|^\\d{18}$|^\\d{17}(\\d|X|x))$";//身份证

    //对应HTTP的状态码
    public static final int UNAUTHORIZED = 401;
    public static final int FORBIDDEN = 403;
    public static final int NOT_FOUND = 404;
    public static final int REQUEST_TIMEOUT = 408;
    public static final int INTERNAL_SERVER_ERROR = 500;
    public static final int BAD_GATEWAY = 502;
    public static final int SERVICE_UNAVAILABLE = 503;
    public static final int GATEWAY_TIMEOUT = 504;

    public static final int REQUEST_REGISTER_CODE = 108;
    public static final int RESULT_REGISTER_CODE = 109;
    public static final int REQUEST_ADDRESS_CODE = 110;
    public static final int RESULT_ADDRESS_CODE = 111;
    public static final int REQUEST_LOGIN_CODE = 112;
    public static final int RESULT_LOGIN_CODE = 113;
    public static final int REQUEST_EXIST_CODE = 114;
    public static final int RESULT_EXIST_CODE = 115;
    public static final int REQUEST_IDENTITY_APPLICATION_CODE = 116;
    public static final int RESULT_IDENTITY_APPLICATION_CODE = 117;
    public static final int REQUEST_PAY_RESULT_CODE = 118;
    public static final int RESULT_PAY_RESULT_CODE = 119;
    public static final int REQUEST_EDIT_EMAIL_RESULT_CODE = 120;
    public static final int RESULT_EDIT_EMAIL_RESULT_CODE = 121;
    public static final int REQUEST_EDIT_NAME_RESULT_CODE = 122;
    public static final int RESULT_EDIT_NAME_RESULT_CODE = 123;
    public static final int REQUEST_EDIT_PHONE_RESULT_CODE = 128;
    public static final int RESULT_EDIT_PHONE_CODE = 129;
    public static final int RESULT_CHANGE_CODE = 130;
    public static final int RESULT_SCAN_CODE = 120;
    public static final int RESULT_SCAN_ONE_CODE = 121;
    public static final int RESULT_SCAN_TWO_CODE = 122;
    public static final int RESULT_SKIP_CODE = 200;

    // 图文
    public static final int CONSULTATION_TYPE_PICTURE = 1;
    // 视频
    public static final int CONSULTATION_TYPE_VIDEO = 2;

    // 问诊卡片
    public static final String HEALTH_INQUIRYCARD = "health_inquiryCard";
    // 温馨提示
    public static final String HEALTH_INQUIRYTIPS = "health_inquiryTips";
    // 医生接诊文本提示
    public static final String HEALTH_RECEIVETIPS = "health_receiveTips";
    // 问诊小结卡片
    public static final String HEALTH_SUMMARYCARD = "health_summaryCard";
    // 医生开具电子处方提示
    public static final String HEALTH_OPENRXTIPS = "health_openRxTips";
    // 医生开具电子处方审核中
    public static final String HEALTH_RXAPPROVAL = "health_RxApprovalCard";
    public static final String HEALTH_RXAPPROVAL2 = "health_RxApproval";
    // 医生开具电子处方完成提示
    public static final String HEALTH_RXSUCCESSTIPS = "health_RxSuccessTips";
    // 电子处方卡片
    public static final String HEALTH_PRESCRIPTIONCARD = "health_prescriptionCard";
    // 问诊超时自动退诊
    public static final String HEALTH_INQUIRYOVERTIMETIPS = "health_inquiryOverTimeTips";
    // 问诊医生主动退诊
    public static final String HEALTH_RETURNTICKETTIPS = "health_returnTicketTips";
    // 问诊结束
    public static final String HEALTH_INQUIRYOVERTIPS = "health_inquiryOverTips";
    // 视频消息
    public static final String HEALTH_VIDEO = "health_video";
    // 审核不通过
    public static final String HEALTH_OPENRXFAIL = "health_openRxFail";

    // 医生上传资质照片页面 底部的 《工作室服务协议》
    public static final String WEB_001 = "https://health.s-sbio.com/workAgree.html";
    // 医生登录注册页面的 《服务协议及隐私政策》
    public static final String WEB_002 = "https://health.s-sbio.com/doctorAgree.html";
    // 医生CA实名认证 页面的《实名认证服务协议》
    public static final String WEB_003 = "https://health.s-sbio.com/caAgree.html";
    // 首页服务开通指南
    public static final String WEB_004 = "https://health.s-sbio.com/images/app/useGuide.html";
    // 首页服务使用指南
    public static final String WEB_005 = "https://health.s-sbio.com/images/app/openGuide.html";


}
