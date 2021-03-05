package com.ssh.shuoshi.api;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.ssh.shuoshi.Constants;
import com.ssh.shuoshi.bean.AreaBean;
import com.ssh.shuoshi.bean.ConsultationBillBean;
import com.ssh.shuoshi.bean.DiagnBean;
import com.ssh.shuoshi.bean.DictListBean;
import com.ssh.shuoshi.bean.DoctorConsultationInfo;
import com.ssh.shuoshi.bean.DoctorWeekScheduleBean;
import com.ssh.shuoshi.bean.DrugBean;
import com.ssh.shuoshi.bean.EmrBean;
import com.ssh.shuoshi.bean.HisDoctorBean;
import com.ssh.shuoshi.bean.HttpResult;
import com.ssh.shuoshi.bean.ImageDiagnoseBean;
import com.ssh.shuoshi.bean.JPushSysMsgRecordBean;
import com.ssh.shuoshi.bean.LoginEntity;
import com.ssh.shuoshi.bean.LoginInfoBean;
import com.ssh.shuoshi.bean.OftenDrugBean;
import com.ssh.shuoshi.bean.SysDeptBean;
import com.ssh.shuoshi.bean.TestBean;
import com.ssh.shuoshi.bean.ca.CAPhoneBean;
import com.ssh.shuoshi.bean.common.SystemTypeBean;
import com.ssh.shuoshi.bean.count.DiagnoseCountBean;
import com.ssh.shuoshi.bean.history.ImGetHistoryBean;
import com.ssh.shuoshi.bean.patient.CommentBean;
import com.ssh.shuoshi.bean.patient.FollowUpListBean;
import com.ssh.shuoshi.bean.patient.MedicalHistoryBean;
import com.ssh.shuoshi.bean.patient.PatientListBean;
import com.ssh.shuoshi.bean.pickview.SysDeptNameBean;
import com.ssh.shuoshi.bean.pickview.SysTitleNameBean;
import com.ssh.shuoshi.bean.prescription.HisPrescriptionDtoBean;
import com.ssh.shuoshi.bean.prescription.PrescriptionStateBean;
import com.ssh.shuoshi.bean.team.DistinctDoctorBean;
import com.ssh.shuoshi.bean.team.DoctorListBean;
import com.ssh.shuoshi.bean.team.DoctorTeamBean;
import com.ssh.shuoshi.bean.team.DoctorTeamDetailBean;
import com.ssh.shuoshi.bean.template.PrescriptionTemplateBean;
import com.ssh.shuoshi.components.JsonUtils;
import com.ssh.shuoshi.components.retrofit.RequestHelper;
import com.ssh.shuoshi.components.storage.UserStorage;
import com.ssh.shuoshi.library.util.PhoneUtil;
import com.ssh.shuoshi.util.SPUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * created by hwt on 2020/12/8
 */
public class CommonApi {

    private RequestHelper mRequestHelper;
    private Context mContext;
    private UserStorage mUserStorage;
    private SPUtil mSpUtil;
    private CommonService mCommonService;


    public CommonApi(Context context, OkHttpClient mOkHttpClient, RequestHelper requestHelper,
                     UserStorage userStorage, SPUtil spUtil) {
        this.mContext = context;
        this.mRequestHelper = requestHelper;
        mUserStorage = userStorage;
        mSpUtil = spUtil;
        Retrofit retrofit = new Retrofit.Builder().addConverterFactory(GsonConverterFactory.create())
                .client(mOkHttpClient)
                .baseUrl(Constants.BASE_URL)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();

        mCommonService = retrofit.create(CommonService.class);
    }

    public CommonApi(final Context context) {
        mSpUtil = new SPUtil(context);
        mUserStorage = new UserStorage(context, mSpUtil);
        this.mRequestHelper = new RequestHelper(context, mUserStorage);

        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .addInterceptor(chain -> {
                    Request request = chain.request();
                    Request.Builder builder1 = request.newBuilder();
                    builder1.addHeader("key", Constants.app_key);
                    builder1.addHeader("app_type", "android");
                    String deviceId = PhoneUtil.getDeviceId(context);
                    builder1.addHeader("device_id", deviceId);
                    builder1.addHeader("app_version", "1.0");
                    Request build = builder1.build();
                    return chain.proceed(build);
                });

        OkHttpClient mOkHttpClient = builder.build();
        mContext = context;
        Retrofit retrofit =
                new Retrofit.Builder().addConverterFactory(GsonConverterFactory.create())
                        .client(mOkHttpClient)
                        .baseUrl(Constants.BASE_URL)
                        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                        .build();
        mCommonService = retrofit.create(CommonService.class);
    }

    /**
     * 对网络接口返回的Response进行分割操作
     *
     * @param response
     * @param <T>
     * @return
     */
    public static <T> Observable<T> flatResponse(final HttpResult<T> response) {
        Log.i("数据", new Gson().toJson(response));
        return Observable.create(e -> {
            if (!e.isDisposed()) {
                if (response.isSuccess()) {
                    Log.i("数据", new Gson().toJson(response));
                    if (response.getResultValue() != null) {
                        e.onNext(response.getResultValue());
                    } else {
                        e.onNext((T) "");
                    }
                    e.onComplete();
                } else {
                    Log.i("错误数据", new Gson().toJson(response));
                    e.onError(new APIException(response.getResultStatus().getCode(),
                            response.getResultStatus().getMessage()));
                }
            }
        });
    }

    public static <T> Observable<T> flatNullResponse(final HttpResult<T> response) {
        return Observable.create(e -> {
            if (!e.isDisposed()) {
                if (response.isSuccess()) {
                    if (response.getResultValue() != null) {
                        e.onNext(response.getResultValue());
                    }
                    e.onComplete();
                } else {
                    e.onError(new APIException(response.getResultStatus().getCode(),
                            response.getResultStatus().getMessage()));
                }
            }
        });
    }


    /**
     * 自定义异常，当接口返回的code不为Constants.OK时，需要跑出此异常
     * eg：登陆时验证码错误；参数为传递等
     */
    public static class APIException extends Exception {
        public int code;
        public String message;

        public APIException(int code, String message) {
            this.code = code;
            this.message = message;
        }

        @Override
        public String getMessage() {
            return message;
        }
    }

    /**
     * 刷新token
     *
     * @param old_token
     * @return
     */
    public Observable<HttpResult<LoginEntity>> userRefreshToken(String old_token) {
        long currentTimeMillis = System.currentTimeMillis();
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("refresh_token", old_token);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestBody body = JsonUtils.toRequestBodyZ(jsonObject);
        Map<String, Object> params = mRequestHelper.getHttpRequestMap(currentTimeMillis);
        params.put("refresh_token", old_token);

        String sign = mRequestHelper.getRequestSign(params, currentTimeMillis);
        return mCommonService.userRefreshToken(currentTimeMillis, sign, body).subscribeOn(Schedulers.io());
    }

    /**
     * 登陆
     */
    public Observable<HttpResult<LoginEntity>> loginNormal(String username, String password) {
        long currentTimeMillis = System.currentTimeMillis();
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("username", username);
            jsonObject.put("password", password);
            jsonObject.put("appType", Constants.APPTYPE);
            jsonObject.put("pushId", mSpUtil.getREGISTRATIONID());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestBody body = JsonUtils.toRequestBody(jsonObject);

        Map<String, Object> map = mRequestHelper.getHttpRequestMap(currentTimeMillis);
        map.put("username", username);
        map.put("password", password);
        map.put("pushId", mSpUtil.getREGISTRATIONID());
        map.put("appType", Constants.APPTYPE);
        String sign = mRequestHelper.getRequestSign(map, currentTimeMillis);
        return mCommonService.loginNormal(currentTimeMillis, sign, body).subscribeOn(Schedulers.io());
    }

    public Observable<HttpResult<TestBean>> test(String token) {
        long currentTimeMillis = System.currentTimeMillis();
        Map<String, Object> params = mRequestHelper.getHttpRequestMap(currentTimeMillis);
        params.put("pageNum", 1);
        params.put("pageSize", 10);
        Log.i("hwt", new Gson().toJson(params));
        String sign = mRequestHelper.getRequestSign(params, currentTimeMillis);
        int pageNum = 1;
        int pageSize = 10;
        return mCommonService.test(currentTimeMillis, token, sign, pageNum, pageSize).subscribeOn(Schedulers.io());
    }

    //短信验证码
    public Observable<HttpResult<String>> sendSmsCode(String phone) {
        long currentTimeMillis = System.currentTimeMillis();
        Map<String, Object> params = mRequestHelper.getHttpRequestMap(currentTimeMillis);
        params.put("phone", phone);
        String sign = mRequestHelper.getRequestSign(params, currentTimeMillis);
        return mCommonService.sendSmsCode(currentTimeMillis, sign, phone).subscribeOn(Schedulers.io());
    }

    //登录
    public Observable<HttpResult<LoginInfoBean>> login(String phone, String smsCode, String uuid) {
        long currentTimeMillis = System.currentTimeMillis();
        Map<String, Object> params = mRequestHelper.getHttpRequestMap(currentTimeMillis);
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("uuid", uuid);
            jsonObject.put("smsCode", smsCode);
            jsonObject.put("phone", phone);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        params.put("uuid", uuid);
        params.put("smsCode", smsCode);
        params.put("phone", phone);
        RequestBody body = JsonUtils.toRequestBody(jsonObject);
        String sign = mRequestHelper.getRequestSign(params, currentTimeMillis);

        return mCommonService.login(currentTimeMillis, sign, body).subscribeOn(Schedulers.io());
    }

    //获取我的医生详细信息
    public Observable<HttpResult<HisDoctorBean>> getDoctorInfo() {
        long currentTimeMillis = System.currentTimeMillis();
        Map<String, Object> params = mRequestHelper.getHttpRequestMap(currentTimeMillis);
        String sign = mRequestHelper.getRequestSign(params, currentTimeMillis);
        return mCommonService.getDoctorInfo(currentTimeMillis, sign, mUserStorage.getToken()).subscribeOn(Schedulers.io());
    }

    // =============================================================================================
    // =============================================================================================
    // ================医生问诊类型

    /**
     * 新增医生问诊类型
     *
     * @param consultationTypeId 问诊类别
     * @return
     */
    public Observable<HttpResult<Integer>> addDoctorConsultation(int consultationTypeId) {
        long currentTimeMillis = System.currentTimeMillis();
        Map<String, Object> params = mRequestHelper.getHttpRequestMap(currentTimeMillis);
        JSONObject jsonObject = new JSONObject();
        try {
            // 问诊类别
            jsonObject.put("consultationTypeId", consultationTypeId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        params.put("consultationTypeId", consultationTypeId);
        RequestBody body = JsonUtils.toRequestBody(jsonObject);
        String sign = mRequestHelper.getRequestSign(params, currentTimeMillis);
        return mCommonService.addDoctorConsultation(currentTimeMillis, sign, mUserStorage.getToken(), body).subscribeOn(Schedulers.io());
    }

    /**
     * 修改医生问诊类型
     *
     * @param consultationTypeId 问诊类别
     * @param price              问诊价格
     * @param enableFlag         0未开启，1开启
     * @return
     */
    public Observable<HttpResult<Integer>> editDoctorConsultation(
            int id,
            Integer consultationTypeId,
            Double price,
            Integer enableFlag
    ) {
        long currentTimeMillis = System.currentTimeMillis();
        Map<String, Object> params = mRequestHelper.getHttpRequestMap(currentTimeMillis);
        JSONObject jsonObject = new JSONObject();
        try {
            // 问诊类别
            jsonObject.put("id", id);
            jsonObject.put("consultationTypeId", consultationTypeId);
            jsonObject.put("price", price);
            jsonObject.put("enableFlag", enableFlag);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        params.put("id", id);
        params.put("consultationTypeId", consultationTypeId);
        params.put("price", price);
        params.put("enableFlag", enableFlag);
        RequestBody body = JsonUtils.toRequestBody(jsonObject);
        String sign = mRequestHelper.getRequestSign(params, currentTimeMillis);
        return mCommonService.editDoctorConsultation(currentTimeMillis, sign, mUserStorage.getToken(), body).subscribeOn(Schedulers.io());
    }

    /**
     * 获取医生问诊类型详细信息
     */
    public Observable<HttpResult<DoctorConsultationInfo>> getDoctorConsultationInfo() {
        long currentTimeMillis = System.currentTimeMillis();
        Map<String, Object> params = mRequestHelper.getHttpRequestMap(currentTimeMillis);
        String sign = mRequestHelper.getRequestSign(params, currentTimeMillis);
        return mCommonService.getDoctorConsultationInfo(currentTimeMillis, sign, mUserStorage.getToken()).subscribeOn(Schedulers.io());
    }

    //获取医生问诊类型详细信息
    public Observable<HttpResult<DoctorConsultationInfo>> getDoctorConsultation(int id) {
        long currentTimeMillis = System.currentTimeMillis();
        Map<String, Object> params = mRequestHelper.getHttpRequestMap(currentTimeMillis);
        params.put("id", id + "");
        String sign = mRequestHelper.getRequestSign(params, currentTimeMillis);
        return mCommonService.getDoctorConsultation(currentTimeMillis, sign, mUserStorage.getToken(), id).subscribeOn(Schedulers.io());
    }

    // =============================================================================================
    // =============================================================================================
    // ================医生排班

    /**
     * 新增医生排班
     */
    public Observable<HttpResult<String>> addDoctorSchedule(List<Map<String, Object>> list) {
        long currentTimeMillis = System.currentTimeMillis();
        Map<String, Object> params = mRequestHelper.getHttpRequestMap(currentTimeMillis);
        for (int i = 0; i < list.size(); i++) {
            params.putAll(list.get(i));
        }
        RequestBody body = JsonUtils.toRequestBody(list);
        String sign = mRequestHelper.getRequestSign(params, currentTimeMillis);
        return mCommonService.addDoctorSchedule(currentTimeMillis, sign, mUserStorage.getToken(), body).subscribeOn(Schedulers.io());
    }

    /**
     * 根据医生所选日期获取当前周的排班
     */
    public Observable<HttpResult<List<DoctorWeekScheduleBean>>> getDoctorCurrentWeekScheduleListDate(String date) {
        long currentTimeMillis = System.currentTimeMillis();
        Map<String, Object> params = mRequestHelper.getHttpRequestMap(currentTimeMillis);
        String sign = mRequestHelper.getRequestSign(params, currentTimeMillis);
        return mCommonService.getDoctorCurrentWeekScheduleListDate(currentTimeMillis, sign, mUserStorage.getToken(), date).subscribeOn(Schedulers.io());
    }

    /**
     * 根据医生所选日期获取当前周的排班
     */
    public Observable<HttpResult<List<DoctorWeekScheduleBean>>> getScheduleListByMonth(String startDate, String endDate) {
        long currentTimeMillis = System.currentTimeMillis();
        Map<String, Object> params = mRequestHelper.getHttpRequestMap(currentTimeMillis);
        String sign = mRequestHelper.getRequestSign(params, currentTimeMillis);
        return mCommonService.getScheduleListByMonth(currentTimeMillis, sign, mUserStorage.getToken(), startDate, endDate)
                .subscribeOn(Schedulers.io());
    }

    /**
     * 复制上周排班
     */
    public Observable<HttpResult<String>> copyDoctorLastWeekScheduleListDate(String date) {
        long currentTimeMillis = System.currentTimeMillis();
        Map<String, Object> params = mRequestHelper.getHttpRequestMap(currentTimeMillis);
        String sign = mRequestHelper.getRequestSign(params, currentTimeMillis);
        return mCommonService.copyDoctorLastWeekScheduleListDate(currentTimeMillis, sign, mUserStorage.getToken(), date).subscribeOn(Schedulers.io());
    }

    /**
     * 删除医生排班
     */
    public Observable<HttpResult<String>> deleteDoctorSchedule(int ids) {
        long currentTimeMillis = System.currentTimeMillis();
        Map<String, Object> params = mRequestHelper.getHttpRequestMap(currentTimeMillis);
        String sign = mRequestHelper.getRequestSign(params, currentTimeMillis);
        return mCommonService.deleteDoctorSchedule(currentTimeMillis, sign, mUserStorage.getToken(), ids + "").subscribeOn(Schedulers.io());
    }

    // ================医生排班结束
    // =============================================================================================
    // =============================================================================================

    public static final String MULTIPART_FORM_DATA = "multipart/form-data";

    /**
     * 资质图片上传
     */
    public Observable<HttpResult<List<String>>> imgNewUpload(String photoList) {
        long currentTimeMillis = System.currentTimeMillis();
        MultipartBody.Builder multyBuilder = new MultipartBody.Builder().setType(MultipartBody.FORM);
        File file = new File(photoList);
        RequestBody requestFile = RequestBody.create(okhttp3.MediaType.parse("image/*"), file);
        multyBuilder.addFormDataPart("files", file.getName(), requestFile)
                .setType(MediaType.parse(MULTIPART_FORM_DATA));
        Map<String, Object> params = mRequestHelper.getHttpRequestMap(currentTimeMillis);
        String sign = mRequestHelper.getRequestSign(params, currentTimeMillis);
        return mCommonService.imgUpload(currentTimeMillis, sign, mUserStorage.getToken(), multyBuilder.build()).subscribeOn(Schedulers.io());
    }

    /**
     * 通用图片下载
     */
    public Observable<HttpResult<List<String>>> imgDownload(String[] photoList) {
        long currentTimeMillis = System.currentTimeMillis();
        Map<String, Object> params = mRequestHelper.getHttpRequestMap(currentTimeMillis);
        params.put("paths", photoList);
        String sign = mRequestHelper.getRequestSign(params, currentTimeMillis);
        return mCommonService.imgDownload(currentTimeMillis, sign, mUserStorage.getToken(), photoList).subscribeOn(Schedulers.io());
    }

    /**
     * 修改医生信息
     */
    public Observable<HttpResult<String>> putDoctorInfo(Map<String, Object> mData) {
        int id = mUserStorage.getDoctorInfo().getId();
        mData.put("id", id);
        RequestBody body = JsonUtils.toRequestBody(mData);
        long currentTimeMillis = System.currentTimeMillis();
        Map<String, Object> params = mRequestHelper.getHttpRequestMap(currentTimeMillis);
        params.putAll(mData);
        String sign = mRequestHelper.getRequestSign(params, currentTimeMillis);
        return mCommonService.putDoctorInfo(currentTimeMillis, sign, mUserStorage.getToken(), body).subscribeOn(Schedulers.io());
    }

    /**
     * 修改医生基础信息
     */
    public Observable<HttpResult<String>> putDoctorEditInfo(Map<String, Object> mData) {
        int id = mUserStorage.getDoctorInfo().getId();
        mData.put("id", id);
        RequestBody body = JsonUtils.toRequestBody(mData);
        long currentTimeMillis = System.currentTimeMillis();
        Map<String, Object> params = mRequestHelper.getHttpRequestMap(currentTimeMillis);
        params.putAll(mData);
        String sign = mRequestHelper.getRequestSign(params, currentTimeMillis);
        return mCommonService.putDoctorEditInfo(currentTimeMillis, sign, mUserStorage.getToken(), body).subscribeOn(Schedulers.io());
    }

    /**
     * 修改医生手机号
     */
    public Observable<HttpResult<String>> changeDoctorPhone(String phone, String code, String mUuid) {
        int id = mUserStorage.getDoctorInfo().getId();
        long currentTimeMillis = System.currentTimeMillis();
        Map<String, Object> params = mRequestHelper.getHttpRequestMap(currentTimeMillis);
        params.put("smsCode", code);
        params.put("phone", phone);
        params.put("id", id);
        params.put("uuid", mUuid);
        String sign = mRequestHelper.getRequestSign(params, currentTimeMillis);
        return mCommonService.changeDoctorPhone(currentTimeMillis, sign, mUserStorage.getToken(), id, phone, code, mUuid).subscribeOn(Schedulers.io());
    }


    /**
     * 查询门诊科室列表
     */
    public Observable<HttpResult<SysDeptNameBean>> getSysDept() {
        long currentTimeMillis = System.currentTimeMillis();
        Map<String, Object> params = mRequestHelper.getHttpRequestMap(currentTimeMillis);
        String sign = mRequestHelper.getRequestSign(params, currentTimeMillis);
        return mCommonService.getSysDept(currentTimeMillis, sign, mUserStorage.getToken()).subscribeOn(Schedulers.io());
    }

    /**
     * 查询医生职务列表
     */
    public Observable<HttpResult<SysTitleNameBean>> getDoctorTitleDict() {
        long currentTimeMillis = System.currentTimeMillis();
        Map<String, Object> params = mRequestHelper.getHttpRequestMap(currentTimeMillis);
        String sign = mRequestHelper.getRequestSign(params, currentTimeMillis);
        return mCommonService.getDoctorTitleDict(currentTimeMillis, sign, mUserStorage.getToken()).subscribeOn(Schedulers.io());
    }

    /**
     * 查询城市信息列表
     */
    public Observable<HttpResult<List<AreaBean>>> getProvinceDict() {
        long currentTimeMillis = System.currentTimeMillis();
        Map<String, Object> params = mRequestHelper.getHttpRequestMap(currentTimeMillis);
        String sign = mRequestHelper.getRequestSign(params, currentTimeMillis);
        return mCommonService.getProvinceDict(currentTimeMillis, sign, mUserStorage.getToken()).subscribeOn(Schedulers.io());
    }

    //-----------------------------------------------------图文问诊开始-----------------------------------------------

    /**
     * 首页获取我的最新的患者消息列表
     */
    public Observable<HttpResult<ImageDiagnoseBean>> getMyNewList(int page) {
        int pageSize = 10;
        String isAsc = "desc";
        String orderByColumn = "msgtime";
        long currentTimeMillis = System.currentTimeMillis();
        Map<String, Object> params = mRequestHelper.getHttpRequestMap(currentTimeMillis);
        Map<String, Object> mData = new HashMap<>();
        mData.put("pageNum", page);
        mData.put("pageSize", pageSize);
        mData.put("isAsc", isAsc);
        mData.put("orderByColumn", orderByColumn);
        params.putAll(mData);
        String sign = mRequestHelper.getRequestSign(params, currentTimeMillis);
        return mCommonService.getMyNewList(currentTimeMillis, sign, mUserStorage.getToken(),
                page, pageSize, isAsc, orderByColumn).subscribeOn(Schedulers.io());
    }

    /**
     * 获取我的待接诊问诊订单列表
     * 接口不分页了，暂时分页参数改大点。state 参数暂时没用了，先放着吧。
     */
    public Observable<HttpResult<ImageDiagnoseBean>> getTuWaitList(int page, int state) {
        int pageSize = 100;
        String isAsc = "asc";
        String orderByColumn = "payTime";
        long currentTimeMillis = System.currentTimeMillis();
        Map<String, Object> params = mRequestHelper.getHttpRequestMap(currentTimeMillis);
        Map<String, Object> mData = new HashMap<>();
        mData.put("pageNum", page);
        mData.put("pageSize", pageSize);
        mData.put("isAsc", isAsc);
        mData.put("orderByColumn", orderByColumn);
        params.putAll(mData);
        String sign = mRequestHelper.getRequestSign(params, currentTimeMillis);
        return mCommonService.getTuWaitList(currentTimeMillis, sign, mUserStorage.getToken(), page, pageSize, isAsc, orderByColumn, state)
                .subscribeOn(Schedulers.io());
    }

    /**
     * 获取我的咨询中订单列表
     */
    public Observable<HttpResult<ImageDiagnoseBean>> getTuTalkingList(int page, int state) {
        int pageSize = 10;
        String isAsc = "desc";
        String orderByColumn = "msgtime";
        long currentTimeMillis = System.currentTimeMillis();
        Map<String, Object> params = mRequestHelper.getHttpRequestMap(currentTimeMillis);
        Map<String, Object> mData = new HashMap<>();
        mData.put("pageNum", page);
        mData.put("pageSize", pageSize);
        mData.put("isAsc", isAsc);
        mData.put("orderByColumn", orderByColumn);
        params.putAll(mData);
        String sign = mRequestHelper.getRequestSign(params, currentTimeMillis);
        return mCommonService.getTuTalkingList(currentTimeMillis, sign, mUserStorage.getToken(), page, pageSize, isAsc, orderByColumn, state)
                .subscribeOn(Schedulers.io());
    }

    /**
     * 获取我的已结束订单列表
     */
    public Observable<HttpResult<ImageDiagnoseBean>> getTuEndList(int page, int state) {
        int pageSize = 10;
        String isAsc = "desc";
        String orderByColumn = "actualEndTime";
        long currentTimeMillis = System.currentTimeMillis();
        Map<String, Object> params = mRequestHelper.getHttpRequestMap(currentTimeMillis);
        Map<String, Object> mData = new HashMap<>();
        mData.put("pageNum", page);
        mData.put("pageSize", pageSize);
        mData.put("isAsc", isAsc);
        mData.put("orderByColumn", orderByColumn);
        params.putAll(mData);
        String sign = mRequestHelper.getRequestSign(params, currentTimeMillis);
        return mCommonService.getTuEndList(currentTimeMillis, sign, mUserStorage.getToken(), page, pageSize, isAsc, orderByColumn, state)
                .subscribeOn(Schedulers.io());
    }

    /**
     * 获取问诊、问诊订单详细信息
     */
    public Observable<HttpResult<ImageDiagnoseBean.RowsBean>> getConsultationInfo(int id) {
        long currentTimeMillis = System.currentTimeMillis();
        Map<String, Object> params = mRequestHelper.getHttpRequestMap(currentTimeMillis);
        String sign = mRequestHelper.getRequestSign(params, currentTimeMillis);
        return mCommonService.getConsultationInfo(currentTimeMillis, sign, mUserStorage.getToken(), id).subscribeOn(Schedulers.io());
    }

    /**
     * 获取问诊小结详细信息
     */
    public Observable<HttpResult<EmrBean>> getEmr(int ids) {
        long currentTimeMillis = System.currentTimeMillis();
        Map<String, Object> params = mRequestHelper.getHttpRequestMap(currentTimeMillis);
        String sign = mRequestHelper.getRequestSign(params, currentTimeMillis);
        return mCommonService.getEmr(currentTimeMillis, sign, mUserStorage.getToken(), ids).subscribeOn(Schedulers.io());
    }

    /**
     * 新增问诊小结
     */
    public Observable<HttpResult<Integer>> addEmr(Map<String, Object> list) {
        long currentTimeMillis = System.currentTimeMillis();
        Map<String, Object> params = mRequestHelper.getHttpRequestMap(currentTimeMillis);
        params.putAll(list);
        RequestBody body = JsonUtils.toRequestBody(list);
        String sign = mRequestHelper.getRequestSign(params, currentTimeMillis);
        return mCommonService.addEmr(currentTimeMillis, sign, mUserStorage.getToken(), body).subscribeOn(Schedulers.io());
    }

    /**
     * 修改问诊小结
     */
    public Observable<HttpResult<Integer>> changeEmr(Map<String, Object> list) {
        long currentTimeMillis = System.currentTimeMillis();
        Map<String, Object> params = mRequestHelper.getHttpRequestMap(currentTimeMillis);
        params.putAll(list);
        RequestBody body = JsonUtils.toRequestBody(list);
        String sign = mRequestHelper.getRequestSign(params, currentTimeMillis);
        return mCommonService.changeEmr(currentTimeMillis, sign, mUserStorage.getToken(), body).subscribeOn(Schedulers.io());
    }

    /**
     * 问诊小结的诊断搜索
     */
    public Observable<HttpResult<DiagnBean>> getDeptDiagnosis(String diagnName, int page) {
        int pageSize = 20;
        long currentTimeMillis = System.currentTimeMillis();
        Map<String, Object> params = mRequestHelper.getHttpRequestMap(currentTimeMillis);
        params.put("pageNum", page);
        params.put("pageSize", pageSize);
        params.put("diagnName", diagnName);
        String sign = mRequestHelper.getRequestSign(params, currentTimeMillis);
        return mCommonService.getDeptDiagnosis(currentTimeMillis, sign, mUserStorage.getToken(), page, pageSize, diagnName).subscribeOn(Schedulers.io());
    }

    /**
     * 接诊
     */
    public Observable<HttpResult<String>> receiveConsultation(int id) {
        long currentTimeMillis = System.currentTimeMillis();
        Map<String, Object> params = mRequestHelper.getHttpRequestMap(currentTimeMillis);
        params.put("id", id);
        String sign = mRequestHelper.getRequestSign(params, currentTimeMillis);
        return mCommonService.receiveConsultation(currentTimeMillis, sign, mUserStorage.getToken(), id).subscribeOn(Schedulers.io());
    }

    /**
     * 退诊
     */
    public Observable<HttpResult<String>> exitConsultation(int id, String reason) {
        long currentTimeMillis = System.currentTimeMillis();
        Map<String, Object> params = mRequestHelper.getHttpRequestMap(currentTimeMillis);
        params.put("id", id);
        params.put("reason", reason);
        String sign = mRequestHelper.getRequestSign(params, currentTimeMillis);
        return mCommonService.exitConsultation(currentTimeMillis, sign, mUserStorage.getToken(), id, reason).subscribeOn(Schedulers.io());
    }

    /**
     * 结束问诊
     */
    public Observable<HttpResult<String>> endConsultation(int id) {
        long currentTimeMillis = System.currentTimeMillis();
        Map<String, Object> params = mRequestHelper.getHttpRequestMap(currentTimeMillis);
        params.put("id", id);
        String sign = mRequestHelper.getRequestSign(params, currentTimeMillis);
        return mCommonService.endConsultation(currentTimeMillis, sign, mUserStorage.getToken(), id).subscribeOn(Schedulers.io());
    }

    /**
     * 查询医生药品列表(新增药品页面)
     */
    public Observable<HttpResult<DrugBean>> getMyYaoList(String name, int page, int type, Integer phamVendorId) {
        int pageSize = 20;
        String isAsc = "asc";
        String orderByColumn = "phamAliasName";
        long currentTimeMillis = System.currentTimeMillis();
        Map<String, Object> params = mRequestHelper.getHttpRequestMap(currentTimeMillis);
        params.put("pageNum", page);
        params.put("pageSize", pageSize);
        params.put("isAsc", isAsc);
        params.put("orderByColumn", orderByColumn);
        params.put("searchValue", name);
        params.put("prescriptionTypeId", type);
        String sign = mRequestHelper.getRequestSign(params, currentTimeMillis);
        return mCommonService.getMyYaoList(currentTimeMillis, sign, mUserStorage.getToken(), page, pageSize, isAsc, orderByColumn, name, type, phamVendorId)
                .subscribeOn(Schedulers.io());
    }

    /**
     * 查询医生中药药品列表(新增药品页面)
     */
    public Observable<HttpResult<DrugBean>> getChineseDrugList(String name, int page, int type, Integer phamVendorId) {
        int pageSize = 20;
        String isAsc = "asc";
        String orderByColumn = "phamAliasName";
        long currentTimeMillis = System.currentTimeMillis();
        Map<String, Object> params = mRequestHelper.getHttpRequestMap(currentTimeMillis);
        params.put("pageNum", page);
        params.put("pageSize", pageSize);
        params.put("isAsc", isAsc);
        params.put("orderByColumn", orderByColumn);
        params.put("searchValue", name);
        params.put("prescriptionTypeId", type);
        String sign = mRequestHelper.getRequestSign(params, currentTimeMillis);
        return mCommonService.getMyYaoList(currentTimeMillis, sign, mUserStorage.getToken(), page, pageSize, isAsc, orderByColumn, name, type, phamVendorId)
                .subscribeOn(Schedulers.io());
    }

    /**
     * 获取我的常用药列表
     */
    public Observable<HttpResult<OftenDrugBean>> getMyOftenList(int type, int page, Integer phamVendorId) {

        int pageSize = 10;
        String isAsc = "asc";
        String orderByColumn = "pharmacyaliasname";
        long currentTimeMillis = System.currentTimeMillis();
        Map<String, Object> params = mRequestHelper.getHttpRequestMap(currentTimeMillis);
        params.put("pageNum", page);
        params.put("pageSize", pageSize);
        params.put("isAsc", isAsc);
        params.put("orderByColumn", orderByColumn);
        params.put("prescriptionTypeId", type);
        String sign = mRequestHelper.getRequestSign(params, currentTimeMillis);
        return mCommonService.getMyOftenList(currentTimeMillis, sign, mUserStorage.getToken(),
                page, pageSize, isAsc, orderByColumn, type, phamVendorId)
                .subscribeOn(Schedulers.io());
    }

    /**
     * 新增常用药
     */
    public Observable<HttpResult<String>> addMyOftenList(int id) {
        long currentTimeMillis = System.currentTimeMillis();
        Map<String, Object> params = mRequestHelper.getHttpRequestMap(currentTimeMillis);
        params.put("phamId", id);
        String sign = mRequestHelper.getRequestSign(params, currentTimeMillis);
        return mCommonService.addMyOftenList(currentTimeMillis, sign, mUserStorage.getToken(), id)
                .subscribeOn(Schedulers.io());
    }

    /**
     * 删除常用药
     */
    public Observable<HttpResult<String>> deleteMyOftenList(int ids) {
        long currentTimeMillis = System.currentTimeMillis();
        Map<String, Object> params = mRequestHelper.getHttpRequestMap(currentTimeMillis);
        params.put("ids", ids);
        String sign = mRequestHelper.getRequestSign(params, currentTimeMillis);
        return mCommonService.deleteMyOftenList(currentTimeMillis, sign, mUserStorage.getToken(), ids)
                .subscribeOn(Schedulers.io());
    }

    /**
     * 获取我的常用药列表
     */
    public Observable<HttpResult<DictListBean>> getDictList() {
        long currentTimeMillis = System.currentTimeMillis();
        Map<String, Object> params = mRequestHelper.getHttpRequestMap(currentTimeMillis);
        String sign = mRequestHelper.getRequestSign(params, currentTimeMillis);
        return mCommonService.getDictList(currentTimeMillis, sign, mUserStorage.getToken())
                .subscribeOn(Schedulers.io());
    }

    /**
     * 根据字典类型获取字典列表 通用,用于替换 用药频次
     */
    public Observable<HttpResult<SystemTypeBean>> getDictionariesList(String dictType) {
        long currentTimeMillis = System.currentTimeMillis();
        Map<String, Object> params = mRequestHelper.getHttpRequestMap(currentTimeMillis);
        String sign = mRequestHelper.getRequestSign(params, currentTimeMillis);
        return mCommonService.getDictionariesList(currentTimeMillis, sign, mUserStorage.getToken(), dictType)
                .subscribeOn(Schedulers.io());
    }


    /**
     * 获取处方、处方订单详细信息
     */
    public Observable<HttpResult<HisPrescriptionDtoBean>> getPrescriptionFromId(Integer id) {
        long currentTimeMillis = System.currentTimeMillis();
        Map<String, Object> params = mRequestHelper.getHttpRequestMap(currentTimeMillis);
        String sign = mRequestHelper.getRequestSign(params, currentTimeMillis);
        return mCommonService.getPrescriptionFromId(currentTimeMillis, sign, mUserStorage.getToken(), id)
                .subscribeOn(Schedulers.io());
    }


    /**
     * 新增处方、处方订单
     */
    public Observable<HttpResult<Integer>> addPrescription(Map<String, Object> map) {
        long currentTimeMillis = System.currentTimeMillis();
        Map<String, Object> params = mRequestHelper.getHttpRequestMap(currentTimeMillis);
        params.putAll(map);
        RequestBody body = JsonUtils.toRequestBody(map);
        String sign = mRequestHelper.getRequestSign(params, currentTimeMillis);
        return mCommonService.addPrescription(currentTimeMillis, sign, mUserStorage.getToken(), body).subscribeOn(Schedulers.io());
    }

    /**
     * 修改处方
     */
    public Observable<HttpResult<Integer>> putPrescription(Map<String, Object> map) {
        long currentTimeMillis = System.currentTimeMillis();
        Map<String, Object> params = mRequestHelper.getHttpRequestMap(currentTimeMillis);
        params.putAll(map);
        RequestBody body = JsonUtils.toRequestBody(map);
        String sign = mRequestHelper.getRequestSign(params, currentTimeMillis);
        return mCommonService.putPrescription(currentTimeMillis, sign, mUserStorage.getToken(), body).subscribeOn(Schedulers.io());
    }

    /**
     * 新增处方、处方订单
     */
    public Observable<HttpResult<HisPrescriptionDtoBean>> getPrescriptionByPatientId(int patientId, int perscriptionTypeId) {
        long currentTimeMillis = System.currentTimeMillis();
        Map<String, Object> params = mRequestHelper.getHttpRequestMap(currentTimeMillis);
        params.put("patientId", patientId);
        params.put("perscriptionTypeId", perscriptionTypeId);
        String sign = mRequestHelper.getRequestSign(params, currentTimeMillis);
        return mCommonService.getPrescriptionByPatientId(currentTimeMillis, sign, mUserStorage.getToken(), patientId, perscriptionTypeId).subscribeOn(Schedulers.io());
    }

    //-----------------------------------------------------图文问诊结束-----------------------------------------------

    //-----------------------------------------------------处方模版开始-----------------------------------------------

    /**
     * 获取我的处方模版列表  开西药传 1，开中药传 2
     */
    public Observable<HttpResult<PrescriptionTemplateBean>> getPrescriptionTemplate(int page, int perscriptionTypeId) {
        int pageSize = 10;
        String isAsc = "desc";
        String orderByColumn = "createTime";
        long currentTimeMillis = System.currentTimeMillis();
        Map<String, Object> params = mRequestHelper.getHttpRequestMap(currentTimeMillis);
        params.put("pageNum", page);
        params.put("pageSize", pageSize);
        params.put("isAsc", isAsc);
        params.put("orderByColumn", orderByColumn);
        params.put("perscriptionTypeId", perscriptionTypeId);
        String sign = mRequestHelper.getRequestSign(params, currentTimeMillis);
        return mCommonService.getPrescriptionTemplate(currentTimeMillis, sign, mUserStorage.getToken(),
                page, pageSize, isAsc, orderByColumn, perscriptionTypeId).subscribeOn(Schedulers.io());
    }

    /**
     * 新增处方模版
     */
    public Observable<HttpResult<Integer>> addPrescriptionTemplate(Map<String, Object> map) {
        long currentTimeMillis = System.currentTimeMillis();
        Map<String, Object> params = mRequestHelper.getHttpRequestMap(currentTimeMillis);
        params.putAll(map);
        RequestBody body = JsonUtils.toRequestBody(map);
        String sign = mRequestHelper.getRequestSign(params, currentTimeMillis);
        return mCommonService.addPrescriptionTemplate(currentTimeMillis, sign, mUserStorage.getToken(), body).subscribeOn(Schedulers.io());
    }

    /**
     * 修改处方模版
     */
    public Observable<HttpResult<Integer>> changePrescriptionTemplate(Map<String, Object> map) {
        long currentTimeMillis = System.currentTimeMillis();
        Map<String, Object> params = mRequestHelper.getHttpRequestMap(currentTimeMillis);
        params.putAll(map);
        RequestBody body = JsonUtils.toRequestBody(map);
        String sign = mRequestHelper.getRequestSign(params, currentTimeMillis);
        return mCommonService.changePrescriptionTemplate(currentTimeMillis, sign, mUserStorage.getToken(), body).subscribeOn(Schedulers.io());
    }

    /**
     * 删除处方模版
     */
    public Observable<HttpResult<Integer>> deletePrescriptionTemplate(int ids) {
        long currentTimeMillis = System.currentTimeMillis();
        Map<String, Object> params = mRequestHelper.getHttpRequestMap(currentTimeMillis);
        params.put("ids", ids);
        String sign = mRequestHelper.getRequestSign(params, currentTimeMillis);
        return mCommonService.deletePrescriptionTemplate(currentTimeMillis, sign, mUserStorage.getToken(), ids)
                .subscribeOn(Schedulers.io());
    }

    //-----------------------------------------------------处方模版结束-----------------------------------------------

    //-----------------------------------------------------CA模版开始-----------------------------------------------

    /**
     * CA创建个人账号
     */
    public Observable<HttpResult<CAPhoneBean>> caCreateUserId(Map<String, Object> map) {
        long currentTimeMillis = System.currentTimeMillis();
        Map<String, Object> params = mRequestHelper.getHttpRequestMap(currentTimeMillis);
        params.putAll(map);
        RequestBody body = JsonUtils.toRequestBody(map);
        String sign = mRequestHelper.getRequestSign(params, currentTimeMillis);
        return mCommonService.caCreateUserId(currentTimeMillis, sign, mUserStorage.getToken(), body)
                .subscribeOn(Schedulers.io());
    }

    /**
     * CA手机认证获取验证码
     */
    public Observable<HttpResult<CAPhoneBean>> caGetPhoneCode(Map<String, Object> map) {
        long currentTimeMillis = System.currentTimeMillis();
        Map<String, Object> params = mRequestHelper.getHttpRequestMap(currentTimeMillis);
        params.putAll(map);
        RequestBody body = JsonUtils.toRequestBody(map);
        String sign = mRequestHelper.getRequestSign(params, currentTimeMillis);
        return mCommonService.caGetPhoneCode(currentTimeMillis, sign, mUserStorage.getToken(), body)
                .subscribeOn(Schedulers.io());
    }

    /**
     * CA手机认证第一次短信验证
     */
    public Observable<HttpResult<CAPhoneBean>> caCodeOauth(Map<String, Object> map) {
        long currentTimeMillis = System.currentTimeMillis();
        Map<String, Object> params = mRequestHelper.getHttpRequestMap(currentTimeMillis);
        params.putAll(map);
        RequestBody body = JsonUtils.toRequestBody(map);
        String sign = mRequestHelper.getRequestSign(params, currentTimeMillis);
        return mCommonService.caCodeOauth(currentTimeMillis, sign, mUserStorage.getToken(), body)
                .subscribeOn(Schedulers.io());
    }

    /**
     * CA人脸认证第一步
     */
    public Observable<HttpResult<CAPhoneBean>> caFaceFirstOauth(Map<String, Object> map) {
        long currentTimeMillis = System.currentTimeMillis();
        Map<String, Object> params = mRequestHelper.getHttpRequestMap(currentTimeMillis);
        params.putAll(map);
        RequestBody body = JsonUtils.toRequestBody(map);
        String sign = mRequestHelper.getRequestSign(params, currentTimeMillis);
        return mCommonService.caFaceFirstOauth(currentTimeMillis, sign, mUserStorage.getToken(), body)
                .subscribeOn(Schedulers.io());
    }

    /**
     * 人脸识别结果查询
     */
    public Observable<HttpResult<CAPhoneBean>> queryFaceOauthResult() {
        long currentTimeMillis = System.currentTimeMillis();
        Map<String, Object> params = mRequestHelper.getHttpRequestMap(currentTimeMillis);
        String sign = mRequestHelper.getRequestSign(params, currentTimeMillis);
        return mCommonService.queryFaceOauthResult(currentTimeMillis, sign, mUserStorage.getToken())
                .subscribeOn(Schedulers.io());
    }

    /**
     * 医生上传印章图片
     */
    public Observable<HttpResult<String>> caImgUpload(String photoList) {
        long currentTimeMillis = System.currentTimeMillis();
        MultipartBody.Builder multyBuilder = new MultipartBody.Builder().setType(MultipartBody.FORM);
        File file = new File(photoList);
        RequestBody requestFile = RequestBody.create(okhttp3.MediaType.parse("image/*"), file);
        multyBuilder.addFormDataPart("busPath", "90");
        multyBuilder.addFormDataPart("file", file.getName(), requestFile)
                .setType(MediaType.parse(MULTIPART_FORM_DATA));
        Map<String, Object> params = mRequestHelper.getHttpRequestMap(currentTimeMillis);
        String sign = mRequestHelper.getRequestSign(params, currentTimeMillis);
        return mCommonService.caImgUpload(currentTimeMillis, sign, mUserStorage.getToken(), multyBuilder.build()).subscribeOn(Schedulers.io());
    }

    /**
     * 用户PDF文件签署 （无意愿签署）
     */
    public Observable<HttpResult<String>> caSignPDFNone(Map<String, Object> map) {
        long currentTimeMillis = System.currentTimeMillis();
        Map<String, Object> params = mRequestHelper.getHttpRequestMap(currentTimeMillis);
        params.putAll(map);
        RequestBody body = JsonUtils.toRequestBody(map);
        String sign = mRequestHelper.getRequestSign(params, currentTimeMillis);
        return mCommonService.caSignPDFNone(currentTimeMillis, sign, mUserStorage.getToken(), body)
                .subscribeOn(Schedulers.io());
    }

    /**
     * 用户PDF文件签署 （有意愿签署，手机验证码二次发送）
     */
    public Observable<HttpResult<CAPhoneBean>> caPhoneSecondCode(Map<String, Object> map) {
        long currentTimeMillis = System.currentTimeMillis();
        Map<String, Object> params = mRequestHelper.getHttpRequestMap(currentTimeMillis);
        params.putAll(map);
        RequestBody body = JsonUtils.toRequestBody(map);
        String sign = mRequestHelper.getRequestSign(params, currentTimeMillis);
        return mCommonService.caPhoneSecondCode(currentTimeMillis, sign, mUserStorage.getToken(), body)
                .subscribeOn(Schedulers.io());
    }

    /**
     * 用户PDF文件签署 （有意愿签署，手机验证码二次验证）
     */
    public Observable<HttpResult<CAPhoneBean>> caPhoneSecondVerify(Map<String, Object> map) {
        long currentTimeMillis = System.currentTimeMillis();
        Map<String, Object> params = mRequestHelper.getHttpRequestMap(currentTimeMillis);
        params.putAll(map);
        RequestBody body = JsonUtils.toRequestBody(map);
        String sign = mRequestHelper.getRequestSign(params, currentTimeMillis);
        return mCommonService.caPhoneSecondVerify(currentTimeMillis, sign, mUserStorage.getToken(), body)
                .subscribeOn(Schedulers.io());
    }

    /**
     * 用户PDF文件签署 （有意愿签署，人脸识别二次认证第一步）
     */
    public Observable<HttpResult<CAPhoneBean>> caFaceSecondCode(Map<String, Object> map) {
        long currentTimeMillis = System.currentTimeMillis();
        Map<String, Object> params = mRequestHelper.getHttpRequestMap(currentTimeMillis);
        params.putAll(map);
        RequestBody body = JsonUtils.toRequestBody(map);
        String sign = mRequestHelper.getRequestSign(params, currentTimeMillis);
        return mCommonService.caFaceSecondCode(currentTimeMillis, sign, mUserStorage.getToken(), body)
                .subscribeOn(Schedulers.io());
    }

    /**
     * 用户PDF文件签署 （有意愿签署，人脸识别二次认证第二步）
     */
    public Observable<HttpResult<String>> caFaceThirdCode(Map<String, Object> map) {
        long currentTimeMillis = System.currentTimeMillis();
        Map<String, Object> params = mRequestHelper.getHttpRequestMap(currentTimeMillis);
        params.putAll(map);
        RequestBody body = JsonUtils.toRequestBody(map);
        String sign = mRequestHelper.getRequestSign(params, currentTimeMillis);
        return mCommonService.caFaceThirdCode(currentTimeMillis, sign, mUserStorage.getToken(), body)
                .subscribeOn(Schedulers.io());
    }

    /**
     * 提交审核 修改处方状态
     */
    public Observable<HttpResult<Integer>> changePrescriptionState(int id) {
        long currentTimeMillis = System.currentTimeMillis();
        Map<String, Object> params = mRequestHelper.getHttpRequestMap(currentTimeMillis);
        params.put("id", id);
        String sign = mRequestHelper.getRequestSign(params, currentTimeMillis);
        return mCommonService.changePrescriptionState(currentTimeMillis, sign, mUserStorage.getToken(), id)
                .subscribeOn(Schedulers.io());
    }

    //-----------------------------------------------------CA模版结束-----------------------------------------------

    /**
     * 根据用户唯一标识生成userSig
     */
    public Observable<HttpResult<String>> getUserSigByUserNo(String userNo) {
        long currentTimeMillis = System.currentTimeMillis();
        Map<String, Object> params = mRequestHelper.getHttpRequestMap(currentTimeMillis);
        params.put("userNo", userNo);
        String sign = mRequestHelper.getRequestSign(params, currentTimeMillis);
        return mCommonService.getUserSigByUserNo(currentTimeMillis, sign, mUserStorage.getToken(), userNo)
                .subscribeOn(Schedulers.io());
    }

    //-----------------------------------------------------专家团队模版开始-----------------------------------------------

    /**
     * 新增专家团队
     */
    public Observable<HttpResult<String>> addDoctorTeam(Map<String, Object> map) {
        long currentTimeMillis = System.currentTimeMillis();
        Map<String, Object> params = mRequestHelper.getHttpRequestMap(currentTimeMillis);
        params.putAll(map);
        RequestBody body = JsonUtils.toRequestBody(map);
        String sign = mRequestHelper.getRequestSign(params, currentTimeMillis);
        return mCommonService.addDoctorTeam(currentTimeMillis, sign, mUserStorage.getToken(), body)
                .subscribeOn(Schedulers.io());
    }

    /**
     * 修改专家团队
     */
    public Observable<HttpResult<Integer>> putDoctorTeam(Map<String, Object> map) {
        long currentTimeMillis = System.currentTimeMillis();
        Map<String, Object> params = mRequestHelper.getHttpRequestMap(currentTimeMillis);
        params.putAll(map);
        RequestBody body = JsonUtils.toRequestBody(map);
        String sign = mRequestHelper.getRequestSign(params, currentTimeMillis);
        return mCommonService.putDoctorTeam(currentTimeMillis, sign, mUserStorage.getToken(), body)
                .subscribeOn(Schedulers.io());
    }

    /**
     * 退出专家团队
     */
    public Observable<HttpResult<Integer>> exitDoctorTeam(Integer id) {
        long currentTimeMillis = System.currentTimeMillis();
        Map<String, Object> params = mRequestHelper.getHttpRequestMap(currentTimeMillis);
        params.put("id", id);
        String sign = mRequestHelper.getRequestSign(params, currentTimeMillis);
        return mCommonService.exitDoctorTeam(currentTimeMillis, sign, mUserStorage.getToken(), id)
                .subscribeOn(Schedulers.io());
    }

    /**
     * 查询我的专家团队列表
     */
    public Observable<HttpResult<DoctorTeamBean>> getDoctorListTeam() {
        long currentTimeMillis = System.currentTimeMillis();
        Map<String, Object> params = mRequestHelper.getHttpRequestMap(currentTimeMillis);
        String sign = mRequestHelper.getRequestSign(params, currentTimeMillis);
        return mCommonService.getDoctorListTeam(currentTimeMillis, sign, mUserStorage.getToken())
                .subscribeOn(Schedulers.io());
    }

    /**
     * 获取专家团队详细信息
     */
    public Observable<HttpResult<DoctorTeamDetailBean>> getDoctorTeamDetail(Integer id) {
        long currentTimeMillis = System.currentTimeMillis();
        Map<String, Object> params = mRequestHelper.getHttpRequestMap(currentTimeMillis);
        params.put("id", id);
        String sign = mRequestHelper.getRequestSign(params, currentTimeMillis);
        return mCommonService.getDoctorTeamDetail(currentTimeMillis, sign, mUserStorage.getToken(), id)
                .subscribeOn(Schedulers.io());
    }

    /**
     * 模糊搜索医生字段
     */
    public Observable<HttpResult<DistinctDoctorBean>> getDistinctDoctorName(int page, String doctorName) {
        int pageSize = 10;
        long currentTimeMillis = System.currentTimeMillis();
        Map<String, Object> params = mRequestHelper.getHttpRequestMap(currentTimeMillis);
        params.put("pageNum", page);
        params.put("pageSize", pageSize);
        params.put("doctorName", doctorName);
        String sign = mRequestHelper.getRequestSign(params, currentTimeMillis);
        return mCommonService.getDistinctDoctorName(currentTimeMillis, sign, mUserStorage.getToken(),
                page, pageSize, doctorName).subscribeOn(Schedulers.io());
    }

    /**
     * 查询医生列表
     */
    public Observable<HttpResult<DoctorListBean>> getDoctorList(int page, String doctorName) {
        int pageSize = 10;
        long currentTimeMillis = System.currentTimeMillis();
        Map<String, Object> params = mRequestHelper.getHttpRequestMap(currentTimeMillis);
        params.put("pageNum", page);
        params.put("pageSize", pageSize);
        params.put("doctorName", doctorName);
        String sign = mRequestHelper.getRequestSign(params, currentTimeMillis);
        return mCommonService.getDoctorList(currentTimeMillis, sign, mUserStorage.getToken(),
                page, pageSize, doctorName).subscribeOn(Schedulers.io());
    }


    //-----------------------------------------------------专家团队模版结束-----------------------------------------------

    //-----------------------------------------------------我的订单-----------------------------------------------

    /**
     * 获取我的待接诊问诊订单列表
     */
    public Observable<HttpResult<ConsultationBillBean>> getConsultationBillList(int page, String date) {
        int pageSize = 10;
        String isAsc = "desc";
        String orderByColumn = "createTime";
        long currentTimeMillis = System.currentTimeMillis();
        Map<String, Object> params = mRequestHelper.getHttpRequestMap(currentTimeMillis);
        Map<String, Object> mData = new HashMap<>();
        mData.put("pageNum", page);
        mData.put("pageSize", pageSize);
        mData.put("isAsc", isAsc);
        mData.put("orderByColumn", orderByColumn);
        mData.put("date", date);
        params.putAll(mData);
        String sign = mRequestHelper.getRequestSign(params, currentTimeMillis);
        return mCommonService.getConsultationBillList(currentTimeMillis, sign, mUserStorage.getToken(),
                page, pageSize, isAsc, orderByColumn, date)
                .subscribeOn(Schedulers.io());
    }


    //-----------------------------------------------------我的处方-----------------------------------------------

    /**
     * 根据状态获取我的处方列表
     */
    public Observable<HttpResult<PrescriptionStateBean>> getByApprovalStatePrescription(int page, int state) {
        int pageSize = 10;
        String isAsc = "desc";
        String orderByColumn = "createTime";
        long currentTimeMillis = System.currentTimeMillis();
        Map<String, Object> params = mRequestHelper.getHttpRequestMap(currentTimeMillis);
        Map<String, Object> mData = new HashMap<>();
        mData.put("pageNum", page);
        mData.put("pageSize", pageSize);
        mData.put("isAsc", isAsc);
        mData.put("orderByColumn", orderByColumn);
        mData.put("state", state);
        params.putAll(mData);
        String sign = mRequestHelper.getRequestSign(params, currentTimeMillis);
        return mCommonService.getByApprovalStatePrescription(currentTimeMillis, sign, mUserStorage.getToken(),
                page, pageSize, isAsc, orderByColumn, state)
                .subscribeOn(Schedulers.io());
    }


    //----------------------------------------------随访，患者，病历模版开始-----------------------------------------------

    /**
     * 获取我的问诊患者列表
     */
    public Observable<HttpResult<PatientListBean>> getMyPatientListByPatientName(int page, String patientName) {
        int pageSize = 10;
        String isAsc = "desc";
        String orderByColumn = "payTime";
        long currentTimeMillis = System.currentTimeMillis();
        Map<String, Object> params = mRequestHelper.getHttpRequestMap(currentTimeMillis);
        Map<String, Object> mData = new HashMap<>();
        mData.put("pageNum", page);
        mData.put("pageSize", pageSize);
        mData.put("isAsc", isAsc);
        mData.put("orderByColumn", orderByColumn);
        mData.put("patientName", patientName);
        params.putAll(mData);
        String sign = mRequestHelper.getRequestSign(params, currentTimeMillis);
        return mCommonService.getMyPatientListByPatientName(currentTimeMillis, sign, mUserStorage.getToken(),
                page, pageSize, isAsc, orderByColumn, patientName)
                .subscribeOn(Schedulers.io());
    }

    /**
     * 获取我的随访患者列表
     */
    public Observable<HttpResult<FollowUpListBean>> getMyFollowList(int page) {
        int pageSize = 10;
        String isAsc = "asc";
        String orderByColumn = "followUpTime";
        long currentTimeMillis = System.currentTimeMillis();
        Map<String, Object> params = mRequestHelper.getHttpRequestMap(currentTimeMillis);
        Map<String, Object> mData = new HashMap<>();
        mData.put("pageNum", page);
        mData.put("pageSize", pageSize);
        mData.put("isAsc", isAsc);
        mData.put("orderByColumn", orderByColumn);
        params.putAll(mData);
        String sign = mRequestHelper.getRequestSign(params, currentTimeMillis);
        return mCommonService.getMyFollowList(currentTimeMillis, sign, mUserStorage.getToken(),
                page, pageSize, isAsc, orderByColumn)
                .subscribeOn(Schedulers.io());
    }

    /**
     * 根据患者获取问诊详情列表
     */
    public Observable<HttpResult<MedicalHistoryBean>> getMyPatientListByPatientId(int page, Integer patientId) {
        int pageSize = 10;
        String isAsc = "desc";
        String orderByColumn = "payTime";
        long currentTimeMillis = System.currentTimeMillis();
        Map<String, Object> params = mRequestHelper.getHttpRequestMap(currentTimeMillis);
        Map<String, Object> mData = new HashMap<>();
        mData.put("pageNum", page);
        mData.put("pageSize", pageSize);
        mData.put("isAsc", isAsc);
        mData.put("orderByColumn", orderByColumn);
        mData.put("patientId", patientId);
        params.putAll(mData);
        String sign = mRequestHelper.getRequestSign(params, currentTimeMillis);
        return mCommonService.getMyPatientListByPatientId(currentTimeMillis, sign, mUserStorage.getToken(),
                page, pageSize, isAsc, orderByColumn, patientId)
                .subscribeOn(Schedulers.io());
    }

    /**
     * 获取医生二维码
     */
    public Observable<HttpResult<String>> getDoctorCodePhoto() {
        long currentTimeMillis = System.currentTimeMillis();
        Map<String, Object> params = mRequestHelper.getHttpRequestMap(currentTimeMillis);
        String sign = mRequestHelper.getRequestSign(params, currentTimeMillis);
        return mCommonService.getDoctorCodePhoto(currentTimeMillis, sign, mUserStorage.getToken())
                .subscribeOn(Schedulers.io());
    }

    /**
     * 获取历史记录
     */
    public Observable<HttpResult<ImGetHistoryBean>> imGetHistory(int page, String conversationId) {
        int pageSize = 13;
        String isAsc = "asc";
        String orderByColumn = "msgSeq";
        long currentTimeMillis = System.currentTimeMillis();
        Map<String, Object> params = mRequestHelper.getHttpRequestMap(currentTimeMillis);
        Map<String, Object> mData = new HashMap<>();
        mData.put("pageNum", page);
        mData.put("pageSize", pageSize);
        mData.put("isAsc", isAsc);
        mData.put("orderByColumn", orderByColumn);
        mData.put("conversationID", conversationId);
        params.putAll(mData);
        String sign = mRequestHelper.getRequestSign(params, currentTimeMillis);
        return mCommonService.imGetHistory(currentTimeMillis, sign, mUserStorage.getToken(),
                page, pageSize, isAsc, orderByColumn, conversationId)
                .subscribeOn(Schedulers.io());
    }

    /**
     * 获取我的问诊订单对应数量
     */
    public Observable<HttpResult<DiagnoseCountBean>> getCountMap(int state) {
        long currentTimeMillis = System.currentTimeMillis();
        Map<String, Object> params = mRequestHelper.getHttpRequestMap(currentTimeMillis);
        Map<String, Object> mData = new HashMap<>();
        mData.put("state", state);
        params.putAll(mData);
        String sign = mRequestHelper.getRequestSign(params, currentTimeMillis);
        return mCommonService.getCountMap(currentTimeMillis, sign, mUserStorage.getToken(),
                state).subscribeOn(Schedulers.io());
    }

    /**
     * 查询患者评论列表
     */
    public Observable<HttpResult<CommentBean>> getCommentsList(int page) {
        int pageSize = 10;
        String isAsc = "desc";
        String orderByColumn = "createTime";
        long currentTimeMillis = System.currentTimeMillis();
        Map<String, Object> params = mRequestHelper.getHttpRequestMap(currentTimeMillis);
        Map<String, Object> mData = new HashMap<>();
        mData.put("pageNum", page);
        mData.put("pageSize", pageSize);
        mData.put("isAsc", isAsc);
        mData.put("orderByColumn", orderByColumn);
        params.putAll(mData);
        String sign = mRequestHelper.getRequestSign(params, currentTimeMillis);
        return mCommonService.getCommentsList(currentTimeMillis, sign, mUserStorage.getToken(),
                page, pageSize, isAsc, orderByColumn).subscribeOn(Schedulers.io());
    }

    /**
     * 获取极光消息历史记录
     */
    public Observable<HttpResult<JPushSysMsgRecordBean>> jpushSysMsgRecordList(int page) {
        int pageSize = 10;
        String isAsc = "desc";
        String orderByColumn = "createTime";
        long currentTimeMillis = System.currentTimeMillis();
        Map<String, Object> params = mRequestHelper.getHttpRequestMap(currentTimeMillis);
        Map<String, Object> mData = new HashMap<>();
        mData.put("pageNum", page);
        mData.put("pageSize", pageSize);
        mData.put("isAsc", isAsc);
        mData.put("orderByColumn", orderByColumn);
        params.putAll(mData);
        String sign = mRequestHelper.getRequestSign(params, currentTimeMillis);
        return mCommonService.jpushSysMsgRecordList(currentTimeMillis, sign, mUserStorage.getToken(),
                page, pageSize, isAsc, orderByColumn)
                .subscribeOn(Schedulers.io());
    }

    /**
     * 推送新用户资质认证通知
     */
    public Observable<HttpResult<String>> jpushSysMsgRecordPushNewDoctor() {
        long currentTimeMillis = System.currentTimeMillis();
        Map<String, Object> params = mRequestHelper.getHttpRequestMap(currentTimeMillis);
        Map<String, Object> mData = new HashMap<>();
        params.putAll(mData);
        String sign = mRequestHelper.getRequestSign(params, currentTimeMillis);
        return mCommonService.jpushSysMsgRecordPushNewDoctor(currentTimeMillis, sign, mUserStorage.getToken())
                .subscribeOn(Schedulers.io());
    }

    /**
     * 删除极光推送记录
     */
    public Observable<HttpResult<String>> deleteJpushSysMsgRecord(String ids) {
        long currentTimeMillis = System.currentTimeMillis();
        Map<String, Object> params = mRequestHelper.getHttpRequestMap(currentTimeMillis);
        Map<String, Object> mData = new HashMap<>();
        params.putAll(mData);
        String sign = mRequestHelper.getRequestSign(params, currentTimeMillis);
        return mCommonService.deleteJpushSysMsgRecord(currentTimeMillis, sign, mUserStorage.getToken(), ids)
                .subscribeOn(Schedulers.io());
    }


    /**
     * 删除极光推送记录
     */
    public Observable<HttpResult<String>> consultationEditFollowTime(int days, int id) {
        long currentTimeMillis = System.currentTimeMillis();
        Map<String, Object> params = mRequestHelper.getHttpRequestMap(currentTimeMillis);
        Map<String, Object> mData = new HashMap<>();
        params.putAll(mData);
        String sign = mRequestHelper.getRequestSign(params, currentTimeMillis);
        return mCommonService.consultationEditFollowTime(currentTimeMillis, sign, mUserStorage.getToken(), days, id)
                .subscribeOn(Schedulers.io());
    }


    /**
     * 视频通话提醒
     */
    public Observable<HttpResult<String>> pushVideoConsulationNotice(int id) {
        long currentTimeMillis = System.currentTimeMillis();
        Map<String, Object> params = mRequestHelper.getHttpRequestMap(currentTimeMillis);
        Map<String, Object> mData = new HashMap<>();
        params.putAll(mData);
        String sign = mRequestHelper.getRequestSign(params, currentTimeMillis);
        return mCommonService.pushVideoConsulationNotice(currentTimeMillis, sign, mUserStorage.getToken(), id)
                .subscribeOn(Schedulers.io());
    }

}
