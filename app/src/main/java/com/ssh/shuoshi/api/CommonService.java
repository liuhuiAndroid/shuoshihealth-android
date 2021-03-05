package com.ssh.shuoshi.api;

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
import com.ssh.shuoshi.bean.history.ImGetHistoryBean;
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

import java.util.List;

import io.reactivex.Observable;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * created by hwt on 2020/12/8
 */
public interface CommonService {

    // 日志上传
    @FormUrlEncoded
    @POST("http://appex.we-win.com.cn/UploadErrorFiles.aspx")
    Observable<ResponseBody> uploadErrorFiles(@Field("appId") String appId, @Field("deviceType") String deviceType,
                                              @Field("osVersion") String osVersion,
                                              @Field("deviceModel") String deviceModel, @Field("log") String log);

    // 刷新Token
    @POST("user/refreshtoken")
    Observable<HttpResult<LoginEntity>> userRefreshToken(@Header("timestamp") long timestamp, @Header("sign") String sign,
                                                         @Body RequestBody body);

    //登录
    @POST("user/login")
    Observable<HttpResult<LoginEntity>> loginNormal(@Header("timestamp") long timestamp,
                                                    @Header("sign") String sign,
                                                    @Body RequestBody body);

    @GET("http://libiao.ngrok.24k.fun/doctor/doctor/list")
    Observable<HttpResult<TestBean>> test(@Header("timestamp") long timestamp,
                                          @Header("Authorization") String authorization,
                                          @Header("sign") String sign,
                                          @Query("pageNum") int pageNum,
                                          @Query("pageSize") int pageSize);

    //获取手机验证码
    @GET("sendSmsCode/{phone}")
    Observable<HttpResult<String>> sendSmsCode(@Header("timestamp") long timestamp,
                                               @Header("sign") String sign,
                                               @Path("phone") String phone);

    //登录
    @POST("login")
    Observable<HttpResult<LoginInfoBean>> login(@Header("timestamp") long timestamp,
                                                @Header("sign") String sign,
                                                @Body RequestBody body);

    //获取我的医生详细信息
    @GET("app/doctor/info")
    Observable<HttpResult<HisDoctorBean>> getDoctorInfo(@Header("timestamp") long timestamp,
                                                        @Header("sign") String sign,
                                                        @Header("Authorization") String token);

    // =============================================================================================
    // =============================================================================================
    // ================医生问诊类型

    /**
     * 新增医生问诊类型
     *
     * @param timestamp
     * @param sign
     * @param token
     * @param body
     * @return
     */
    @POST("app/doctorConsultation")
    Observable<HttpResult<Integer>> addDoctorConsultation(@Header("timestamp") long timestamp,
                                                          @Header("sign") String sign,
                                                          @Header("Authorization") String token,
                                                          @Body RequestBody body);

    /**
     * 获取医生问诊类型详细信息
     */
    @GET("app/doctorConsultation/info")
    Observable<HttpResult<DoctorConsultationInfo>> getDoctorConsultationInfo(@Header("timestamp") long timestamp,
                                                                             @Header("sign") String sign,
                                                                             @Header("Authorization") String token);

    //获取医生问诊类型详细信息
    @GET("doctor/doctorConsultation/{id}")
    Observable<HttpResult<DoctorConsultationInfo>> getDoctorConsultation(@Header("timestamp") long timestamp,
                                                                         @Header("sign") String sign,
                                                                         @Header("Authorization") String token,
                                                                         @Path("id") int id);

    //修改医生问诊类型
    @PUT("app/doctorConsultation")
    Observable<HttpResult<Integer>> editDoctorConsultation(@Header("timestamp") long timestamp,
                                                           @Header("sign") String sign,
                                                           @Header("Authorization") String token,
                                                           @Body RequestBody body);

    // =============================================================================================
    // =============================================================================================
    // ================医生排班
    //根据医生所选日期获取当前周的排班
    @GET("app/doctorSchedule/getCurrentWeekScheduleListByDate")
    Observable<HttpResult<List<DoctorWeekScheduleBean>>> getDoctorCurrentWeekScheduleListDate(@Header("timestamp") long timestamp,
                                                                                              @Header("sign") String sign,
                                                                                              @Header("Authorization") String token,
                                                                                              @Query("date") String date);

    //根据医生所选日期获取当前周的排班
    @GET("app/doctorSchedule/getScheduleListByMonth")
    Observable<HttpResult<List<DoctorWeekScheduleBean>>> getScheduleListByMonth(@Header("timestamp") long timestamp,
                                                                                @Header("sign") String sign,
                                                                                @Header("Authorization") String token,
                                                                                @Query("startDate") String startDate,
                                                                                @Query("endDate") String endDate);

    //复制上周排班
    @POST("app/doctorSchedule/copyLastWeekScheduleListByDate")
    Observable<HttpResult<String>> copyDoctorLastWeekScheduleListDate(@Header("timestamp") long timestamp,
                                                                      @Header("sign") String sign,
                                                                      @Header("Authorization") String token,
                                                                      @Query("date") String date);


    //新增医生排班
    @POST("app/doctorSchedule/addList")
    Observable<HttpResult<String>> addDoctorSchedule(@Header("timestamp") long timestamp,
                                                     @Header("sign") String sign,
                                                     @Header("Authorization") String token,
                                                     @Body RequestBody body);

    //删除医生排班
    @DELETE("app/doctorSchedule/{ids}")
    Observable<HttpResult<String>> deleteDoctorSchedule(@Header("timestamp") long timestamp,
                                                        @Header("sign") String sign,
                                                        @Header("Authorization") String token,
                                                        @Path("ids") String ids);

    // =============================================================================================
    // =============================================================================================

    //上传照片
    @POST("app/doctor/exportPhoto")
    Observable<HttpResult<List<String>>> imgUpload(@Header("timestamp") long timestamp,
                                                   @Header("sign") String sign,
                                                   @Header("Authorization") String token,
                                                   @Body RequestBody body);

    //图片通用下载
    @GET("common/downloadPhoto")
    Observable<HttpResult<List<String>>> imgDownload(@Header("timestamp") long timestamp,
                                                     @Header("sign") String sign,
                                                     @Header("Authorization") String token,
                                                     @Query("paths") String[] date);


    //修改医生资质认证信息
    @PUT("app/doctor")
    Observable<HttpResult<String>> putDoctorInfo(@Header("timestamp") long timestamp,
                                                 @Header("sign") String sign,
                                                 @Header("Authorization") String token,
                                                 @Body RequestBody body);

    //修改医生基础信息
    @PUT("app/doctor/editInfo")
    Observable<HttpResult<String>> putDoctorEditInfo(@Header("timestamp") long timestamp,
                                                     @Header("sign") String sign,
                                                     @Header("Authorization") String token,
                                                     @Body RequestBody body);

    //修改医生手机号
    @FormUrlEncoded
    @PUT("app/doctor/editPhone")
    Observable<HttpResult<String>> changeDoctorPhone(@Header("timestamp") long timestamp,
                                                     @Header("sign") String sign,
                                                     @Header("Authorization") String token,
                                                     @Field("id") int id,
                                                     @Field("phone") String phone,
                                                     @Field("smsCode") String smsCode,
                                                     @Field("uuid") String uuid);


    //查询门诊科室列表
    @GET("system/hisSysDept/list")
    Observable<HttpResult<SysDeptNameBean>> getSysDept(@Header("timestamp") long timestamp,
                                                       @Header("sign") String sign,
                                                       @Header("Authorization") String token);

    //查询医生职务列表
    @GET("app/doctorTitleDict/list")
    Observable<HttpResult<SysTitleNameBean>> getDoctorTitleDict(@Header("timestamp") long timestamp,
                                                                @Header("sign") String sign,
                                                                @Header("Authorization") String token);

    //查询省信息列表
    @GET("system/hisSysCityDict/getAreaList")
    Observable<HttpResult<List<AreaBean>>> getProvinceDict(@Header("timestamp") long timestamp,
                                                           @Header("sign") String sign,
                                                           @Header("Authorization") String token);

    // =============================================================================================
    //                             图文问诊开始
    // =============================================================================================

    //首页获取我的最新的患者消息列表
    @GET("app/consultation/getMyNewList")
    Observable<HttpResult<ImageDiagnoseBean>> getMyNewList(@Header("timestamp") long timestamp,
                                                           @Header("sign") String sign,
                                                           @Header("Authorization") String token,
                                                           @Query("pageNum") int pageNum,
                                                           @Query("pageSize") int pageSize,
                                                           @Query("isAsc") String isAsc,
                                                           @Query("orderByColumn") String orderByColumn);


    //获取我的待接诊问诊订单列表
    @GET("app/consultation/getMyWaitList")
    Observable<HttpResult<ImageDiagnoseBean>> getTuWaitList(@Header("timestamp") long timestamp,
                                                            @Header("sign") String sign,
                                                            @Header("Authorization") String token,
                                                            @Query("pageNum") int pageNum,
                                                            @Query("pageSize") int pageSize,
                                                            @Query("isAsc") String isAsc,
                                                            @Query("orderByColumn") String orderByColumn,
                                                            @Query("state") int state);

    //获取我的咨询中订单列表
    @GET("app/consultation/getMyTalkingList")
    Observable<HttpResult<ImageDiagnoseBean>> getTuTalkingList(@Header("timestamp") long timestamp,
                                                               @Header("sign") String sign,
                                                               @Header("Authorization") String token,
                                                               @Query("pageNum") int pageNum,
                                                               @Query("pageSize") int pageSize,
                                                               @Query("isAsc") String isAsc,
                                                               @Query("orderByColumn") String orderByColumn,
                                                               @Query("state") int state);

    //获取我的已结束订单列表
    @GET("app/consultation/getMyEndList")
    Observable<HttpResult<ImageDiagnoseBean>> getTuEndList(@Header("timestamp") long timestamp,
                                                           @Header("sign") String sign,
                                                           @Header("Authorization") String token,
                                                           @Query("pageNum") int pageNum,
                                                           @Query("pageSize") int pageSize,
                                                           @Query("isAsc") String isAsc,
                                                           @Query("orderByColumn") String orderByColumn,
                                                           @Query("state") int state);

    //获取问诊、问诊订单详细信息
    @GET("app/consultation/{id}")
    Observable<HttpResult<ImageDiagnoseBean.RowsBean>> getConsultationInfo(@Header("timestamp") long timestamp,
                                                                           @Header("sign") String sign,
                                                                           @Header("Authorization") String token,
                                                                           @Path("id") int id);

    //获取问诊小结详细信息
    @GET("app/emr/getByConsultationId/{consultationId}")
    Observable<HttpResult<EmrBean>> getEmr(@Header("timestamp") long timestamp,
                                           @Header("sign") String sign,
                                           @Header("Authorization") String token,
                                           @Path("consultationId") int id);

    //新增问诊小结
    @POST("app/emr")
    Observable<HttpResult<Integer>> addEmr(@Header("timestamp") long timestamp,
                                           @Header("sign") String sign,
                                           @Header("Authorization") String token,
                                           @Body RequestBody body);

    //修改问诊小结
    @PUT("app/emr")
    Observable<HttpResult<Integer>> changeEmr(@Header("timestamp") long timestamp,
                                              @Header("sign") String sign,
                                              @Header("Authorization") String token,
                                              @Body RequestBody body);

    //问诊小结的诊断搜索
    @GET("system/hisSysDiagnosisDict/listByDiagnName")
    Observable<HttpResult<DiagnBean>> getDeptDiagnosis(@Header("timestamp") long timestamp,
                                                       @Header("sign") String sign,
                                                       @Header("Authorization") String token,
                                                       @Query("pageNum") int pageNum,
                                                       @Query("pageSize") int pageSize,
                                                       @Query("diagnName") String diagnName);


    //图文接诊
    @PUT("app/consultation/receive")
    Observable<HttpResult<String>> receiveConsultation(@Header("timestamp") long timestamp,
                                                       @Header("sign") String sign,
                                                       @Header("Authorization") String authorization,
                                                       @Query("id") int id);

    //图文退诊
    @PUT("app/consultation/exit")
    Observable<HttpResult<String>> exitConsultation(@Header("timestamp") long timestamp,
                                                    @Header("sign") String sign,
                                                    @Header("Authorization") String authorization,
                                                    @Query("id") int id,
                                                    @Query("withdrawalReason") String withdrawalReason);

    // 结束问诊
    @PUT("app/consultation/end")
    Observable<HttpResult<String>> endConsultation(@Header("timestamp") long timestamp,
                                                   @Header("sign") String sign,
                                                   @Header("Authorization") String authorization,
                                                   @Query("id") int id);

    //查询医生药品列表(新增药品页面)
    @GET("app/pharmacy/getMyList")
    Observable<HttpResult<DrugBean>> getMyYaoList(@Header("timestamp") long timestamp,
                                                  @Header("sign") String sign,
                                                  @Header("Authorization") String token,
                                                  @Query("pageNum") int pageNum,
                                                  @Query("pageSize") int pageSize,
                                                  @Query("isAsc") String isAsc,
                                                  @Query("orderByColumn") String orderByColumn,
                                                  @Query("searchValue") String searchValue,
                                                  @Query("prescriptionTypeId") int phamCategoryId,
                                                  @Query("phamVendorId") Integer phamVendorId);

    //获取我的常用药列表
    @GET("app/often/getMyList")
    Observable<HttpResult<OftenDrugBean>> getMyOftenList(@Header("timestamp") long timestamp,
                                                         @Header("sign") String sign,
                                                         @Header("Authorization") String token,
                                                         @Query("pageNum") int pageNum,
                                                         @Query("pageSize") int pageSize,
                                                         @Query("isAsc") String isAsc,
                                                         @Query("orderByColumn") String orderByColumn,
                                                         @Query("prescriptionTypeId") int phamCategoryId,
                                                         @Query("phamVendorId") Integer phamVendorId);

    //新增常用药
    @FormUrlEncoded
    @POST("app/often")
    Observable<HttpResult<String>> addMyOftenList(@Header("timestamp") long timestamp,
                                                  @Header("sign") String sign,
                                                  @Header("Authorization") String token,
                                                  @Field("phamId") int id);

    //删除常用药
    @DELETE("app/often/{ids}")
    Observable<HttpResult<String>> deleteMyOftenList(@Header("timestamp") long timestamp,
                                                     @Header("sign") String sign,
                                                     @Header("Authorization") String token,
                                                     @Path("ids") int ids);

    //查询用药频次列表
    @GET("app/dict/list")
    Observable<HttpResult<DictListBean>> getDictList(@Header("timestamp") long timestamp,
                                                     @Header("sign") String sign,
                                                     @Header("Authorization") String token);

    //根据字典类型获取字典列表 通用
    @GET("system/type/{dictType}")
    Observable<HttpResult<SystemTypeBean>> getDictionariesList(@Header("timestamp") long timestamp,
                                                               @Header("sign") String sign,
                                                               @Header("Authorization") String token,
                                                               @Path("dictType") String dictType);

    //新增处方、处方订单
    @POST("app/prescription")
    Observable<HttpResult<Integer>> addPrescription(@Header("timestamp") long timestamp,
                                                    @Header("sign") String sign,
                                                    @Header("Authorization") String token,
                                                    @Body RequestBody body);

    //修改处方
    @PUT("app/prescription")
    Observable<HttpResult<Integer>> putPrescription(@Header("timestamp") long timestamp,
                                                    @Header("sign") String sign,
                                                    @Header("Authorization") String token,
                                                    @Body RequestBody body);


    //获取患者最新一次历史处方
    @GET("app/prescription/getPrescriptionByPatientIdAndPerscriptionTypeId")
    Observable<HttpResult<HisPrescriptionDtoBean>> getPrescriptionByPatientId(@Header("timestamp") long timestamp,
                                                                              @Header("sign") String sign,
                                                                              @Header("Authorization") String token,
                                                                              @Query("patientId") int patientId,
                                                                              @Query("perscriptionTypeId") int perscriptionTypeId);

    //获取处方、处方订单详细信息
    @GET("app/prescription/{id}")
    Observable<HttpResult<HisPrescriptionDtoBean>> getPrescriptionFromId(@Header("timestamp") long timestamp,
                                                                         @Header("sign") String sign,
                                                                         @Header("Authorization") String token,
                                                                         @Path("id") Integer id);


    // =============================================================================================
    //                             图文问诊结束
    // =============================================================================================

    // =============================================================================================
    //                             处方模版开始
    // =============================================================================================

    //获取我的处方模版列表  开西药传 1，开中药传 2
    @GET("app/prescriptionTemplate/getMyList")
    Observable<HttpResult<PrescriptionTemplateBean>> getPrescriptionTemplate(@Header("timestamp") long timestamp,
                                                                             @Header("sign") String sign,
                                                                             @Header("Authorization") String token,
                                                                             @Query("pageNum") int pageNum,
                                                                             @Query("pageSize") int pageSize,
                                                                             @Query("isAsc") String isAsc,
                                                                             @Query("orderByColumn") String orderByColumn,
                                                                             @Query("perscriptionTypeId") int id);

    //新增处方模版
    @POST("app/prescriptionTemplate")
    Observable<HttpResult<Integer>> addPrescriptionTemplate(@Header("timestamp") long timestamp,
                                                            @Header("sign") String sign,
                                                            @Header("Authorization") String token,
                                                            @Body RequestBody body);

    //修改处方模版
    @PUT("app/prescriptionTemplate")
    Observable<HttpResult<Integer>> changePrescriptionTemplate(@Header("timestamp") long timestamp,
                                                               @Header("sign") String sign,
                                                               @Header("Authorization") String token,
                                                               @Body RequestBody body);


    //删除处方模版
    @DELETE("app/prescriptionTemplate/{ids}")
    Observable<HttpResult<Integer>> deletePrescriptionTemplate(@Header("timestamp") long timestamp,
                                                               @Header("sign") String sign,
                                                               @Header("Authorization") String token,
                                                               @Path("ids") int ids);

    // =============================================================================================
    //                             处方模版结束
    // =============================================================================================


    // =============================================================================================
    //                             CA模版开始
    // =============================================================================================

    //CA创建个人账号
    @POST("ca/sign/account/createPersonByThirdPartyUserId")
    Observable<HttpResult<CAPhoneBean>> caCreateUserId(@Header("timestamp") long timestamp,
                                                       @Header("sign") String sign,
                                                       @Header("Authorization") String token,
                                                       @Body RequestBody body);

    //CA手机认证获取验证码
    @POST("ca/oauth/individual/telecom3Factors")
    Observable<HttpResult<CAPhoneBean>> caGetPhoneCode(@Header("timestamp") long timestamp,
                                                       @Header("sign") String sign,
                                                       @Header("Authorization") String token,
                                                       @Body RequestBody body);

    //CA手机认证短信验证
    @POST("ca/oauth/individual/flowId/telecom3Factors")
    Observable<HttpResult<CAPhoneBean>> caCodeOauth(@Header("timestamp") long timestamp,
                                                    @Header("sign") String sign,
                                                    @Header("Authorization") String token,
                                                    @Body RequestBody body);

    //CA人脸认证第一步
    @POST("ca/oauth/individual/face")
    Observable<HttpResult<CAPhoneBean>> caFaceFirstOauth(@Header("timestamp") long timestamp,
                                                         @Header("sign") String sign,
                                                         @Header("Authorization") String token,
                                                         @Body RequestBody body);

    //人脸识别结果查询
    @POST("ca/oauth/individual/qryFaceStatus")
    Observable<HttpResult<CAPhoneBean>> queryFaceOauthResult(@Header("timestamp") long timestamp,
                                                             @Header("sign") String sign,
                                                             @Header("Authorization") String token);

    //医生上传印章图片
    @POST("ca/sign/account/uploadSealData")
    Observable<HttpResult<String>> caImgUpload(@Header("timestamp") long timestamp,
                                               @Header("sign") String sign,
                                               @Header("Authorization") String token,
                                               @Body RequestBody body);

    //用户PDF文件签署 （无意愿签署）
    @POST("ca/sign/localSignPDF")
    Observable<HttpResult<String>> caSignPDFNone(@Header("timestamp") long timestamp,
                                                 @Header("sign") String sign,
                                                 @Header("Authorization") String token,
                                                 @Body RequestBody body);

    //用户PDF文件签署 （有意愿签署，手机验证码二次发送）
    @POST("ca/sign/safeSignPDF")
    Observable<HttpResult<CAPhoneBean>> caPhoneSecondCode(@Header("timestamp") long timestamp,
                                                          @Header("sign") String sign,
                                                          @Header("Authorization") String token,
                                                          @Body RequestBody body);

    //用户PDF文件签署 （有意愿签署，手机验证码二次验证）
    @POST("ca/sign/safeSignPDF3rd")
    Observable<HttpResult<CAPhoneBean>> caPhoneSecondVerify(@Header("timestamp") long timestamp,
                                                            @Header("sign") String sign,
                                                            @Header("Authorization") String token,
                                                            @Body RequestBody body);

    //用户PDF文件签署 （有意愿签署，人脸识别二次认证第一步）
    @POST("ca/sign/createSignAuth")
    Observable<HttpResult<CAPhoneBean>> caFaceSecondCode(@Header("timestamp") long timestamp,
                                                         @Header("sign") String sign,
                                                         @Header("Authorization") String token,
                                                         @Body RequestBody body);

    //用户PDF文件签署 （有意愿签署，人脸识别二次认证第二步）
    @POST("ca/sign/batchSignWithWillness")
    Observable<HttpResult<String>> caFaceThirdCode(@Header("timestamp") long timestamp,
                                                   @Header("sign") String sign,
                                                   @Header("Authorization") String token,
                                                   @Body RequestBody body);


    //提交审核 修改处方状态
    @PUT("app/prescription/changeState/{id}")
    Observable<HttpResult<Integer>> changePrescriptionState(@Header("timestamp") long timestamp,
                                                            @Header("sign") String sign,
                                                            @Header("Authorization") String token,
                                                            @Path("id") int id);

    // =============================================================================================
    //                             CA模版结束
    // =============================================================================================

    // 根据用户唯一标识生成userSig
    @GET("im/account/getUserSigByUserNo")
    Observable<HttpResult<String>> getUserSigByUserNo(@Header("timestamp") long timestamp,
                                                      @Header("sign") String sign,
                                                      @Header("Authorization") String token,
                                                      @Query("userNo") String userNo);

    // =============================================================================================
    //                             我的订单
    // =============================================================================================
    // 查询问诊订单账单列表
    @GET("app/consultationBill/list")
    Observable<HttpResult<ConsultationBillBean>> getConsultationBillList(@Header("timestamp") long timestamp,
                                                                         @Header("sign") String sign,
                                                                         @Header("Authorization") String token,
                                                                         @Query("pageNum") int pageNum,
                                                                         @Query("pageSize") int pageSize,
                                                                         @Query("isAsc") String isAsc,
                                                                         @Query("orderByColumn") String orderByColumn,
                                                                         @Query("date") String date);

    // 根据状态获取我的处方列表
    @GET("app/getByApprovalState")
    Observable<HttpResult<ConsultationBillBean>> getByApprovalStateList(@Header("timestamp") long timestamp,
                                                                        @Header("sign") String sign,
                                                                        @Header("Authorization") String token,
                                                                        @Query("pageNum") int pageNum,
                                                                        @Query("pageSize") int pageSize,
                                                                        @Query("isAsc") String isAsc,
                                                                        @Query("orderByColumn") String orderByColumn,
                                                                        @Query("date") String date);

    // =============================================================================================
    //                             我的处方
    // =============================================================================================

    // 根据状态获取我的处方列表
    @GET("app/getByApprovalState")
    Observable<HttpResult<PrescriptionStateBean>> getByApprovalStatePrescription(@Header("timestamp") long timestamp,
                                                                                 @Header("sign") String sign,
                                                                                 @Header("Authorization") String token,
                                                                                 @Query("pageNum") int pageNum,
                                                                                 @Query("pageSize") int pageSize,
                                                                                 @Query("isAsc") String isAsc,
                                                                                 @Query("orderByColumn") String orderByColumn,
                                                                                 @Query("state") int state);

    // =============================================================================================
    //                             专家团队模版开始
    // =============================================================================================

    // 新增专家团队
    @POST("app/doctorTeam")
    Observable<HttpResult<String>> addDoctorTeam(@Header("timestamp") long timestamp,
                                                 @Header("sign") String sign,
                                                 @Header("Authorization") String token,
                                                 @Body RequestBody body);

    // 修改专家团队
    @PUT("app/doctorTeam")
    Observable<HttpResult<Integer>> putDoctorTeam(@Header("timestamp") long timestamp,
                                                  @Header("sign") String sign,
                                                  @Header("Authorization") String token,
                                                  @Body RequestBody body);

    // 退出专家团队
    @POST("app/doctorTeam/exit")
    Observable<HttpResult<Integer>> exitDoctorTeam(@Header("timestamp") long timestamp,
                                                   @Header("sign") String sign,
                                                   @Header("Authorization") String token,
                                                   @Query("id") Integer id);

    // 查询我的专家团队列表
    @GET("app/doctorTeam/list")
    Observable<HttpResult<DoctorTeamBean>> getDoctorListTeam(@Header("timestamp") long timestamp,
                                                             @Header("sign") String sign,
                                                             @Header("Authorization") String token);

    // 获取专家团队详细信息
    @GET("app/doctorTeam/{id}")
    Observable<HttpResult<DoctorTeamDetailBean>> getDoctorTeamDetail(@Header("timestamp") long timestamp,
                                                                     @Header("sign") String sign,
                                                                     @Header("Authorization") String token,
                                                                     @Path("id") Integer id);

    // 模糊搜索医生字段
    @GET("app/getDistinctDoctorName")
    Observable<HttpResult<DistinctDoctorBean>> getDistinctDoctorName(@Header("timestamp") long timestamp,
                                                                     @Header("sign") String sign,
                                                                     @Header("Authorization") String token,
                                                                     @Query("pageNum") int pageNum,
                                                                     @Query("pageSize") int pageSize,
                                                                     @Query("doctorName") String doctorName);


    // 查询医生列表
    @GET("app/list")
    Observable<HttpResult<DoctorListBean>> getDoctorList(@Header("timestamp") long timestamp,
                                                         @Header("sign") String sign,
                                                         @Header("Authorization") String token,
                                                         @Query("pageNum") int pageNum,
                                                         @Query("pageSize") int pageSize,
                                                         @Query("doctorName") String doctorName);


    // =============================================================================================
    //                             专家团队模版结束
    // =============================================================================================

    // =============================================================================================
    //                             随访，患者，病历模版开始
    // =============================================================================================

    // 获取我的问诊患者列表
    @GET("app/consultation/getMyPatientListByPatientName")
    Observable<HttpResult<PatientListBean>> getMyPatientListByPatientName(@Header("timestamp") long timestamp,
                                                                          @Header("sign") String sign,
                                                                          @Header("Authorization") String token,
                                                                          @Query("pageNum") int pageNum,
                                                                          @Query("pageSize") int pageSize,
                                                                          @Query("isAsc") String isAsc,
                                                                          @Query("orderByColumn") String orderByColumn,
                                                                          @Query("patientName") String patientName);

    // 获取我的随访患者列表
    @GET("app/consultation/getMyFollowList")
    Observable<HttpResult<FollowUpListBean>> getMyFollowList(@Header("timestamp") long timestamp,
                                                             @Header("sign") String sign,
                                                             @Header("Authorization") String token,
                                                             @Query("pageNum") int pageNum,
                                                             @Query("pageSize") int pageSize,
                                                             @Query("isAsc") String isAsc,
                                                             @Query("orderByColumn") String orderByColumn);


    // 根据患者获取问诊详情列表
    @GET("app/consultation/getMyPatientListByPatientId")
    Observable<HttpResult<MedicalHistoryBean>> getMyPatientListByPatientId(@Header("timestamp") long timestamp,
                                                                           @Header("sign") String sign,
                                                                           @Header("Authorization") String token,
                                                                           @Query("pageNum") int pageNum,
                                                                           @Query("pageSize") int pageSize,
                                                                           @Query("isAsc") String isAsc,
                                                                           @Query("orderByColumn") String orderByColumn,
                                                                           @Query("patientId") Integer patientId);

    // 获取医生二维码
    @GET("wx/doctor/getPhotoUrl")
    Observable<HttpResult<String>> getDoctorCodePhoto(@Header("timestamp") long timestamp,
                                                      @Header("sign") String sign,
                                                      @Header("Authorization") String token);


    // 获取我的问诊订单对应数量
    @GET("app/consultation/getCountMap")
    Observable<HttpResult<DiagnoseCountBean>> getCountMap(@Header("timestamp") long timestamp,
                                                          @Header("sign") String sign,
                                                          @Header("Authorization") String token,
                                                          @Query("state") int state);

    // 查询患者评论列表
    @GET("app/patient/comments/list")
    Observable<HttpResult<CommentBean>> getCommentsList(@Header("timestamp") long timestamp,
                                                        @Header("sign") String sign,
                                                        @Header("Authorization") String token,
                                                        @Query("pageNum") int pageNum,
                                                        @Query("pageSize") int pageSize,
                                                        @Query("isAsc") String isAsc,
                                                        @Query("orderByColumn") String orderByColumn);

    // =============================================================================================
    //                             随访，患者，病历模版结束
    // =============================================================================================

    // 获取IM历史记录
    @GET("im/getHistory")
    Observable<HttpResult<ImGetHistoryBean>> imGetHistory(@Header("timestamp") long timestamp,
                                                          @Header("sign") String sign,
                                                          @Header("Authorization") String token,
                                                          @Query("pageNum") int pageNum,
                                                          @Query("pageSize") int pageSize,
                                                          @Query("isAsc") String isAsc,
                                                          @Query("orderByColumn") String orderByColumn,
                                                          @Query("conversationID") String conversationId);

    // 获取消息历史记录
    @GET("jpush/sysMsgRecord/list")
    Observable<HttpResult<JPushSysMsgRecordBean>> jpushSysMsgRecordList(@Header("timestamp") long timestamp,
                                                                        @Header("sign") String sign,
                                                                        @Header("Authorization") String token,
                                                                        @Query("pageNum") int pageNum,
                                                                        @Query("pageSize") int pageSize,
                                                                        @Query("isAsc") String isAsc,
                                                                        @Query("orderByColumn") String orderByColumn);

    // 推送新用户资质认证通知
    @POST("jpush/sysMsgRecord/pushNewDoctor")
    Observable<HttpResult<String>> jpushSysMsgRecordPushNewDoctor(@Header("timestamp") long timestamp,
                                                                  @Header("sign") String sign,
                                                                  @Header("Authorization") String token);

    // 删除极光推送记录
    @DELETE("jpush/sysMsgRecord/{ids}")
    Observable<HttpResult<String>> deleteJpushSysMsgRecord(@Header("timestamp") long timestamp,
                                                           @Header("sign") String sign,
                                                           @Header("Authorization") String token,
                                                           @Path("ids") String ids);

    // 设置随访时间
    @PUT("app/consultation/editFollowTime")
    Observable<HttpResult<String>> consultationEditFollowTime(@Header("timestamp") long timestamp,
                                                              @Header("sign") String sign,
                                                              @Header("Authorization") String token,
                                                              @Query("days") int days,
                                                              @Query("id") int id);


    // 视频通话提醒
    @GET("app/consultation/pushVideoConsulationNotice")
    Observable<HttpResult<String>> pushVideoConsulationNotice(@Header("timestamp") long timestamp,
                                                              @Header("sign") String sign,
                                                              @Header("Authorization") String token,
                                                              @Query("consulationId") int consulationId);
}
