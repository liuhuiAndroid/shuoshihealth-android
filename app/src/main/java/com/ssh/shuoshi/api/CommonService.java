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

    // ????????????
    @FormUrlEncoded
    @POST("http://appex.we-win.com.cn/UploadErrorFiles.aspx")
    Observable<ResponseBody> uploadErrorFiles(@Field("appId") String appId, @Field("deviceType") String deviceType,
                                              @Field("osVersion") String osVersion,
                                              @Field("deviceModel") String deviceModel, @Field("log") String log);

    // ??????Token
    @POST("user/refreshtoken")
    Observable<HttpResult<LoginEntity>> userRefreshToken(@Header("timestamp") long timestamp, @Header("sign") String sign,
                                                         @Body RequestBody body);

    //??????
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

    //?????????????????????
    @GET("sendSmsCode/{phone}")
    Observable<HttpResult<String>> sendSmsCode(@Header("timestamp") long timestamp,
                                               @Header("sign") String sign,
                                               @Path("phone") String phone);

    //??????
    @POST("login")
    Observable<HttpResult<LoginInfoBean>> login(@Header("timestamp") long timestamp,
                                                @Header("sign") String sign,
                                                @Body RequestBody body);

    //??????????????????????????????
    @GET("app/doctor/info")
    Observable<HttpResult<HisDoctorBean>> getDoctorInfo(@Header("timestamp") long timestamp,
                                                        @Header("sign") String sign,
                                                        @Header("Authorization") String token);

    // =============================================================================================
    // =============================================================================================
    // ================??????????????????

    /**
     * ????????????????????????
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
     * ????????????????????????????????????
     */
    @GET("app/doctorConsultation/info")
    Observable<HttpResult<DoctorConsultationInfo>> getDoctorConsultationInfo(@Header("timestamp") long timestamp,
                                                                             @Header("sign") String sign,
                                                                             @Header("Authorization") String token);

    //????????????????????????????????????
    @GET("doctor/doctorConsultation/{id}")
    Observable<HttpResult<DoctorConsultationInfo>> getDoctorConsultation(@Header("timestamp") long timestamp,
                                                                         @Header("sign") String sign,
                                                                         @Header("Authorization") String token,
                                                                         @Path("id") int id);

    //????????????????????????
    @PUT("app/doctorConsultation")
    Observable<HttpResult<Integer>> editDoctorConsultation(@Header("timestamp") long timestamp,
                                                           @Header("sign") String sign,
                                                           @Header("Authorization") String token,
                                                           @Body RequestBody body);

    // =============================================================================================
    // =============================================================================================
    // ================????????????
    //????????????????????????????????????????????????
    @GET("app/doctorSchedule/getCurrentWeekScheduleListByDate")
    Observable<HttpResult<List<DoctorWeekScheduleBean>>> getDoctorCurrentWeekScheduleListDate(@Header("timestamp") long timestamp,
                                                                                              @Header("sign") String sign,
                                                                                              @Header("Authorization") String token,
                                                                                              @Query("date") String date);

    //????????????????????????????????????????????????
    @GET("app/doctorSchedule/getScheduleListByMonth")
    Observable<HttpResult<List<DoctorWeekScheduleBean>>> getScheduleListByMonth(@Header("timestamp") long timestamp,
                                                                                @Header("sign") String sign,
                                                                                @Header("Authorization") String token,
                                                                                @Query("startDate") String startDate,
                                                                                @Query("endDate") String endDate);

    //??????????????????
    @POST("app/doctorSchedule/copyLastWeekScheduleListByDate")
    Observable<HttpResult<String>> copyDoctorLastWeekScheduleListDate(@Header("timestamp") long timestamp,
                                                                      @Header("sign") String sign,
                                                                      @Header("Authorization") String token,
                                                                      @Query("date") String date);


    //??????????????????
    @POST("app/doctorSchedule/addList")
    Observable<HttpResult<String>> addDoctorSchedule(@Header("timestamp") long timestamp,
                                                     @Header("sign") String sign,
                                                     @Header("Authorization") String token,
                                                     @Body RequestBody body);

    //??????????????????
    @DELETE("app/doctorSchedule/{ids}")
    Observable<HttpResult<String>> deleteDoctorSchedule(@Header("timestamp") long timestamp,
                                                        @Header("sign") String sign,
                                                        @Header("Authorization") String token,
                                                        @Path("ids") String ids);

    // =============================================================================================
    // =============================================================================================

    //????????????
    @POST("app/doctor/exportPhoto")
    Observable<HttpResult<List<String>>> imgUpload(@Header("timestamp") long timestamp,
                                                   @Header("sign") String sign,
                                                   @Header("Authorization") String token,
                                                   @Body RequestBody body);

    //??????????????????
    @GET("common/downloadPhoto")
    Observable<HttpResult<List<String>>> imgDownload(@Header("timestamp") long timestamp,
                                                     @Header("sign") String sign,
                                                     @Header("Authorization") String token,
                                                     @Query("paths") String[] date);


    //??????????????????????????????
    @PUT("app/doctor")
    Observable<HttpResult<String>> putDoctorInfo(@Header("timestamp") long timestamp,
                                                 @Header("sign") String sign,
                                                 @Header("Authorization") String token,
                                                 @Body RequestBody body);

    //????????????????????????
    @PUT("app/doctor/editInfo")
    Observable<HttpResult<String>> putDoctorEditInfo(@Header("timestamp") long timestamp,
                                                     @Header("sign") String sign,
                                                     @Header("Authorization") String token,
                                                     @Body RequestBody body);

    //?????????????????????
    @FormUrlEncoded
    @PUT("app/doctor/editPhone")
    Observable<HttpResult<String>> changeDoctorPhone(@Header("timestamp") long timestamp,
                                                     @Header("sign") String sign,
                                                     @Header("Authorization") String token,
                                                     @Field("id") int id,
                                                     @Field("phone") String phone,
                                                     @Field("smsCode") String smsCode,
                                                     @Field("uuid") String uuid);


    //????????????????????????
    @GET("system/hisSysDept/list")
    Observable<HttpResult<SysDeptNameBean>> getSysDept(@Header("timestamp") long timestamp,
                                                       @Header("sign") String sign,
                                                       @Header("Authorization") String token);

    //????????????????????????
    @GET("app/doctorTitleDict/list")
    Observable<HttpResult<SysTitleNameBean>> getDoctorTitleDict(@Header("timestamp") long timestamp,
                                                                @Header("sign") String sign,
                                                                @Header("Authorization") String token);

    //?????????????????????
    @GET("system/hisSysCityDict/getAreaList")
    Observable<HttpResult<List<AreaBean>>> getProvinceDict(@Header("timestamp") long timestamp,
                                                           @Header("sign") String sign,
                                                           @Header("Authorization") String token);

    // =============================================================================================
    //                             ??????????????????
    // =============================================================================================

    //?????????????????????????????????????????????
    @GET("app/consultation/getMyNewList")
    Observable<HttpResult<ImageDiagnoseBean>> getMyNewList(@Header("timestamp") long timestamp,
                                                           @Header("sign") String sign,
                                                           @Header("Authorization") String token,
                                                           @Query("pageNum") int pageNum,
                                                           @Query("pageSize") int pageSize,
                                                           @Query("isAsc") String isAsc,
                                                           @Query("orderByColumn") String orderByColumn);


    //???????????????????????????????????????
    @GET("app/consultation/getMyWaitList")
    Observable<HttpResult<ImageDiagnoseBean>> getTuWaitList(@Header("timestamp") long timestamp,
                                                            @Header("sign") String sign,
                                                            @Header("Authorization") String token,
                                                            @Query("pageNum") int pageNum,
                                                            @Query("pageSize") int pageSize,
                                                            @Query("isAsc") String isAsc,
                                                            @Query("orderByColumn") String orderByColumn,
                                                            @Query("state") int state);

    //?????????????????????????????????
    @GET("app/consultation/getMyTalkingList")
    Observable<HttpResult<ImageDiagnoseBean>> getTuTalkingList(@Header("timestamp") long timestamp,
                                                               @Header("sign") String sign,
                                                               @Header("Authorization") String token,
                                                               @Query("pageNum") int pageNum,
                                                               @Query("pageSize") int pageSize,
                                                               @Query("isAsc") String isAsc,
                                                               @Query("orderByColumn") String orderByColumn,
                                                               @Query("state") int state);

    //?????????????????????????????????
    @GET("app/consultation/getMyEndList")
    Observable<HttpResult<ImageDiagnoseBean>> getTuEndList(@Header("timestamp") long timestamp,
                                                           @Header("sign") String sign,
                                                           @Header("Authorization") String token,
                                                           @Query("pageNum") int pageNum,
                                                           @Query("pageSize") int pageSize,
                                                           @Query("isAsc") String isAsc,
                                                           @Query("orderByColumn") String orderByColumn,
                                                           @Query("state") int state);

    //???????????????????????????????????????
    @GET("app/consultation/{id}")
    Observable<HttpResult<ImageDiagnoseBean.RowsBean>> getConsultationInfo(@Header("timestamp") long timestamp,
                                                                           @Header("sign") String sign,
                                                                           @Header("Authorization") String token,
                                                                           @Path("id") int id);

    //??????????????????????????????
    @GET("app/emr/getByConsultationId/{consultationId}")
    Observable<HttpResult<EmrBean>> getEmr(@Header("timestamp") long timestamp,
                                           @Header("sign") String sign,
                                           @Header("Authorization") String token,
                                           @Path("consultationId") int id);

    //??????????????????
    @POST("app/emr")
    Observable<HttpResult<Integer>> addEmr(@Header("timestamp") long timestamp,
                                           @Header("sign") String sign,
                                           @Header("Authorization") String token,
                                           @Body RequestBody body);

    //??????????????????
    @PUT("app/emr")
    Observable<HttpResult<Integer>> changeEmr(@Header("timestamp") long timestamp,
                                              @Header("sign") String sign,
                                              @Header("Authorization") String token,
                                              @Body RequestBody body);

    //???????????????????????????
    @GET("system/hisSysDiagnosisDict/listByDiagnName")
    Observable<HttpResult<DiagnBean>> getDeptDiagnosis(@Header("timestamp") long timestamp,
                                                       @Header("sign") String sign,
                                                       @Header("Authorization") String token,
                                                       @Query("pageNum") int pageNum,
                                                       @Query("pageSize") int pageSize,
                                                       @Query("diagnName") String diagnName);


    //????????????
    @PUT("app/consultation/receive")
    Observable<HttpResult<String>> receiveConsultation(@Header("timestamp") long timestamp,
                                                       @Header("sign") String sign,
                                                       @Header("Authorization") String authorization,
                                                       @Query("id") int id);

    //????????????
    @PUT("app/consultation/exit")
    Observable<HttpResult<String>> exitConsultation(@Header("timestamp") long timestamp,
                                                    @Header("sign") String sign,
                                                    @Header("Authorization") String authorization,
                                                    @Query("id") int id,
                                                    @Query("withdrawalReason") String withdrawalReason);

    // ????????????
    @PUT("app/consultation/end")
    Observable<HttpResult<String>> endConsultation(@Header("timestamp") long timestamp,
                                                   @Header("sign") String sign,
                                                   @Header("Authorization") String authorization,
                                                   @Query("id") int id);

    //????????????????????????(??????????????????)
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

    //???????????????????????????
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

    //???????????????
    @FormUrlEncoded
    @POST("app/often")
    Observable<HttpResult<String>> addMyOftenList(@Header("timestamp") long timestamp,
                                                  @Header("sign") String sign,
                                                  @Header("Authorization") String token,
                                                  @Field("phamId") int id);

    //???????????????
    @DELETE("app/often/{ids}")
    Observable<HttpResult<String>> deleteMyOftenList(@Header("timestamp") long timestamp,
                                                     @Header("sign") String sign,
                                                     @Header("Authorization") String token,
                                                     @Path("ids") int ids);

    //????????????????????????
    @GET("app/dict/list")
    Observable<HttpResult<DictListBean>> getDictList(@Header("timestamp") long timestamp,
                                                     @Header("sign") String sign,
                                                     @Header("Authorization") String token);

    //???????????????????????????????????? ??????
    @GET("system/type/{dictType}")
    Observable<HttpResult<SystemTypeBean>> getDictionariesList(@Header("timestamp") long timestamp,
                                                               @Header("sign") String sign,
                                                               @Header("Authorization") String token,
                                                               @Path("dictType") String dictType);

    //???????????????????????????
    @POST("app/prescription")
    Observable<HttpResult<Integer>> addPrescription(@Header("timestamp") long timestamp,
                                                    @Header("sign") String sign,
                                                    @Header("Authorization") String token,
                                                    @Body RequestBody body);

    //????????????
    @PUT("app/prescription")
    Observable<HttpResult<Integer>> putPrescription(@Header("timestamp") long timestamp,
                                                    @Header("sign") String sign,
                                                    @Header("Authorization") String token,
                                                    @Body RequestBody body);


    //????????????????????????????????????
    @GET("app/prescription/getPrescriptionByPatientIdAndPerscriptionTypeId")
    Observable<HttpResult<HisPrescriptionDtoBean>> getPrescriptionByPatientId(@Header("timestamp") long timestamp,
                                                                              @Header("sign") String sign,
                                                                              @Header("Authorization") String token,
                                                                              @Query("patientId") int patientId,
                                                                              @Query("perscriptionTypeId") int perscriptionTypeId);

    //???????????????????????????????????????
    @GET("app/prescription/{id}")
    Observable<HttpResult<HisPrescriptionDtoBean>> getPrescriptionFromId(@Header("timestamp") long timestamp,
                                                                         @Header("sign") String sign,
                                                                         @Header("Authorization") String token,
                                                                         @Path("id") Integer id);


    // =============================================================================================
    //                             ??????????????????
    // =============================================================================================

    // =============================================================================================
    //                             ??????????????????
    // =============================================================================================

    //??????????????????????????????  ???????????? 1??????????????? 2
    @GET("app/prescriptionTemplate/getMyList")
    Observable<HttpResult<PrescriptionTemplateBean>> getPrescriptionTemplate(@Header("timestamp") long timestamp,
                                                                             @Header("sign") String sign,
                                                                             @Header("Authorization") String token,
                                                                             @Query("pageNum") int pageNum,
                                                                             @Query("pageSize") int pageSize,
                                                                             @Query("isAsc") String isAsc,
                                                                             @Query("orderByColumn") String orderByColumn,
                                                                             @Query("perscriptionTypeId") int id);

    //??????????????????
    @POST("app/prescriptionTemplate")
    Observable<HttpResult<Integer>> addPrescriptionTemplate(@Header("timestamp") long timestamp,
                                                            @Header("sign") String sign,
                                                            @Header("Authorization") String token,
                                                            @Body RequestBody body);

    //??????????????????
    @PUT("app/prescriptionTemplate")
    Observable<HttpResult<Integer>> changePrescriptionTemplate(@Header("timestamp") long timestamp,
                                                               @Header("sign") String sign,
                                                               @Header("Authorization") String token,
                                                               @Body RequestBody body);


    //??????????????????
    @DELETE("app/prescriptionTemplate/{ids}")
    Observable<HttpResult<Integer>> deletePrescriptionTemplate(@Header("timestamp") long timestamp,
                                                               @Header("sign") String sign,
                                                               @Header("Authorization") String token,
                                                               @Path("ids") int ids);

    // =============================================================================================
    //                             ??????????????????
    // =============================================================================================


    // =============================================================================================
    //                             CA????????????
    // =============================================================================================

    //CA??????????????????
    @POST("ca/sign/account/createPersonByThirdPartyUserId")
    Observable<HttpResult<CAPhoneBean>> caCreateUserId(@Header("timestamp") long timestamp,
                                                       @Header("sign") String sign,
                                                       @Header("Authorization") String token,
                                                       @Body RequestBody body);

    //CA???????????????????????????
    @POST("ca/oauth/individual/telecom3Factors")
    Observable<HttpResult<CAPhoneBean>> caGetPhoneCode(@Header("timestamp") long timestamp,
                                                       @Header("sign") String sign,
                                                       @Header("Authorization") String token,
                                                       @Body RequestBody body);

    //CA????????????????????????
    @POST("ca/oauth/individual/flowId/telecom3Factors")
    Observable<HttpResult<CAPhoneBean>> caCodeOauth(@Header("timestamp") long timestamp,
                                                    @Header("sign") String sign,
                                                    @Header("Authorization") String token,
                                                    @Body RequestBody body);

    //CA?????????????????????
    @POST("ca/oauth/individual/face")
    Observable<HttpResult<CAPhoneBean>> caFaceFirstOauth(@Header("timestamp") long timestamp,
                                                         @Header("sign") String sign,
                                                         @Header("Authorization") String token,
                                                         @Body RequestBody body);

    //????????????????????????
    @POST("ca/oauth/individual/qryFaceStatus")
    Observable<HttpResult<CAPhoneBean>> queryFaceOauthResult(@Header("timestamp") long timestamp,
                                                             @Header("sign") String sign,
                                                             @Header("Authorization") String token);

    //????????????????????????
    @POST("ca/sign/account/uploadSealData")
    Observable<HttpResult<String>> caImgUpload(@Header("timestamp") long timestamp,
                                               @Header("sign") String sign,
                                               @Header("Authorization") String token,
                                               @Body RequestBody body);

    //??????PDF???????????? ?????????????????????
    @POST("ca/sign/localSignPDF")
    Observable<HttpResult<String>> caSignPDFNone(@Header("timestamp") long timestamp,
                                                 @Header("sign") String sign,
                                                 @Header("Authorization") String token,
                                                 @Body RequestBody body);

    //??????PDF???????????? ???????????????????????????????????????????????????
    @POST("ca/sign/safeSignPDF")
    Observable<HttpResult<CAPhoneBean>> caPhoneSecondCode(@Header("timestamp") long timestamp,
                                                          @Header("sign") String sign,
                                                          @Header("Authorization") String token,
                                                          @Body RequestBody body);

    //??????PDF???????????? ???????????????????????????????????????????????????
    @POST("ca/sign/safeSignPDF3rd")
    Observable<HttpResult<CAPhoneBean>> caPhoneSecondVerify(@Header("timestamp") long timestamp,
                                                            @Header("sign") String sign,
                                                            @Header("Authorization") String token,
                                                            @Body RequestBody body);

    //??????PDF???????????? ?????????????????????????????????????????????????????????
    @POST("ca/sign/createSignAuth")
    Observable<HttpResult<CAPhoneBean>> caFaceSecondCode(@Header("timestamp") long timestamp,
                                                         @Header("sign") String sign,
                                                         @Header("Authorization") String token,
                                                         @Body RequestBody body);

    //??????PDF???????????? ?????????????????????????????????????????????????????????
    @POST("ca/sign/batchSignWithWillness")
    Observable<HttpResult<String>> caFaceThirdCode(@Header("timestamp") long timestamp,
                                                   @Header("sign") String sign,
                                                   @Header("Authorization") String token,
                                                   @Body RequestBody body);


    //???????????? ??????????????????
    @PUT("app/prescription/changeState/{id}")
    Observable<HttpResult<Integer>> changePrescriptionState(@Header("timestamp") long timestamp,
                                                            @Header("sign") String sign,
                                                            @Header("Authorization") String token,
                                                            @Path("id") int id);

    // =============================================================================================
    //                             CA????????????
    // =============================================================================================

    // ??????????????????????????????userSig
    @GET("im/account/getUserSigByUserNo")
    Observable<HttpResult<String>> getUserSigByUserNo(@Header("timestamp") long timestamp,
                                                      @Header("sign") String sign,
                                                      @Header("Authorization") String token,
                                                      @Query("userNo") String userNo);

    // =============================================================================================
    //                             ????????????
    // =============================================================================================
    // ??????????????????????????????
    @GET("app/consultationBill/list")
    Observable<HttpResult<ConsultationBillBean>> getConsultationBillList(@Header("timestamp") long timestamp,
                                                                         @Header("sign") String sign,
                                                                         @Header("Authorization") String token,
                                                                         @Query("pageNum") int pageNum,
                                                                         @Query("pageSize") int pageSize,
                                                                         @Query("isAsc") String isAsc,
                                                                         @Query("orderByColumn") String orderByColumn,
                                                                         @Query("date") String date);

    // ????????????????????????????????????
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
    //                             ????????????
    // =============================================================================================

    // ????????????????????????????????????
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
    //                             ????????????????????????
    // =============================================================================================

    // ??????????????????
    @POST("app/doctorTeam")
    Observable<HttpResult<String>> addDoctorTeam(@Header("timestamp") long timestamp,
                                                 @Header("sign") String sign,
                                                 @Header("Authorization") String token,
                                                 @Body RequestBody body);

    // ??????????????????
    @PUT("app/doctorTeam")
    Observable<HttpResult<Integer>> putDoctorTeam(@Header("timestamp") long timestamp,
                                                  @Header("sign") String sign,
                                                  @Header("Authorization") String token,
                                                  @Body RequestBody body);

    // ??????????????????
    @POST("app/doctorTeam/exit")
    Observable<HttpResult<Integer>> exitDoctorTeam(@Header("timestamp") long timestamp,
                                                   @Header("sign") String sign,
                                                   @Header("Authorization") String token,
                                                   @Query("id") Integer id);

    // ??????????????????????????????
    @GET("app/doctorTeam/list")
    Observable<HttpResult<DoctorTeamBean>> getDoctorListTeam(@Header("timestamp") long timestamp,
                                                             @Header("sign") String sign,
                                                             @Header("Authorization") String token);

    // ??????????????????????????????
    @GET("app/doctorTeam/{id}")
    Observable<HttpResult<DoctorTeamDetailBean>> getDoctorTeamDetail(@Header("timestamp") long timestamp,
                                                                     @Header("sign") String sign,
                                                                     @Header("Authorization") String token,
                                                                     @Path("id") Integer id);

    // ????????????????????????
    @GET("app/getDistinctDoctorName")
    Observable<HttpResult<DistinctDoctorBean>> getDistinctDoctorName(@Header("timestamp") long timestamp,
                                                                     @Header("sign") String sign,
                                                                     @Header("Authorization") String token,
                                                                     @Query("pageNum") int pageNum,
                                                                     @Query("pageSize") int pageSize,
                                                                     @Query("doctorName") String doctorName);


    // ??????????????????
    @GET("app/list")
    Observable<HttpResult<DoctorListBean>> getDoctorList(@Header("timestamp") long timestamp,
                                                         @Header("sign") String sign,
                                                         @Header("Authorization") String token,
                                                         @Query("pageNum") int pageNum,
                                                         @Query("pageSize") int pageSize,
                                                         @Query("doctorName") String doctorName);


    // =============================================================================================
    //                             ????????????????????????
    // =============================================================================================

    // =============================================================================================
    //                             ????????????????????????????????????
    // =============================================================================================

    // ??????????????????????????????
    @GET("app/consultation/getMyPatientListByPatientName")
    Observable<HttpResult<PatientListBean>> getMyPatientListByPatientName(@Header("timestamp") long timestamp,
                                                                          @Header("sign") String sign,
                                                                          @Header("Authorization") String token,
                                                                          @Query("pageNum") int pageNum,
                                                                          @Query("pageSize") int pageSize,
                                                                          @Query("isAsc") String isAsc,
                                                                          @Query("orderByColumn") String orderByColumn,
                                                                          @Query("patientName") String patientName);

    // ??????????????????????????????
    @GET("app/consultation/getMyFollowList")
    Observable<HttpResult<FollowUpListBean>> getMyFollowList(@Header("timestamp") long timestamp,
                                                             @Header("sign") String sign,
                                                             @Header("Authorization") String token,
                                                             @Query("pageNum") int pageNum,
                                                             @Query("pageSize") int pageSize,
                                                             @Query("isAsc") String isAsc,
                                                             @Query("orderByColumn") String orderByColumn);


    // ????????????????????????????????????
    @GET("app/consultation/getMyPatientListByPatientId")
    Observable<HttpResult<MedicalHistoryBean>> getMyPatientListByPatientId(@Header("timestamp") long timestamp,
                                                                           @Header("sign") String sign,
                                                                           @Header("Authorization") String token,
                                                                           @Query("pageNum") int pageNum,
                                                                           @Query("pageSize") int pageSize,
                                                                           @Query("isAsc") String isAsc,
                                                                           @Query("orderByColumn") String orderByColumn,
                                                                           @Query("patientId") Integer patientId);

    // ?????????????????????
    @GET("wx/doctor/getPhotoUrl")
    Observable<HttpResult<String>> getDoctorCodePhoto(@Header("timestamp") long timestamp,
                                                      @Header("sign") String sign,
                                                      @Header("Authorization") String token);


    // ????????????????????????????????????
    @GET("app/consultation/getCountMap")
    Observable<HttpResult<DiagnoseCountBean>> getCountMap(@Header("timestamp") long timestamp,
                                                          @Header("sign") String sign,
                                                          @Header("Authorization") String token,
                                                          @Query("state") int state);

    // ????????????????????????
    @GET("app/patient/comments/list")
    Observable<HttpResult<CommentBean>> getCommentsList(@Header("timestamp") long timestamp,
                                                        @Header("sign") String sign,
                                                        @Header("Authorization") String token,
                                                        @Query("pageNum") int pageNum,
                                                        @Query("pageSize") int pageSize,
                                                        @Query("isAsc") String isAsc,
                                                        @Query("orderByColumn") String orderByColumn);

    // =============================================================================================
    //                             ????????????????????????????????????
    // =============================================================================================

    // ??????IM????????????
    @GET("im/getHistory")
    Observable<HttpResult<ImGetHistoryBean>> imGetHistory(@Header("timestamp") long timestamp,
                                                          @Header("sign") String sign,
                                                          @Header("Authorization") String token,
                                                          @Query("pageNum") int pageNum,
                                                          @Query("pageSize") int pageSize,
                                                          @Query("isAsc") String isAsc,
                                                          @Query("orderByColumn") String orderByColumn,
                                                          @Query("conversationID") String conversationId);

    // ????????????????????????
    @GET("jpush/sysMsgRecord/list")
    Observable<HttpResult<JPushSysMsgRecordBean>> jpushSysMsgRecordList(@Header("timestamp") long timestamp,
                                                                        @Header("sign") String sign,
                                                                        @Header("Authorization") String token,
                                                                        @Query("pageNum") int pageNum,
                                                                        @Query("pageSize") int pageSize,
                                                                        @Query("isAsc") String isAsc,
                                                                        @Query("orderByColumn") String orderByColumn);

    // ?????????????????????????????????
    @POST("jpush/sysMsgRecord/pushNewDoctor")
    Observable<HttpResult<String>> jpushSysMsgRecordPushNewDoctor(@Header("timestamp") long timestamp,
                                                                  @Header("sign") String sign,
                                                                  @Header("Authorization") String token);

    // ????????????????????????
    @DELETE("jpush/sysMsgRecord/{ids}")
    Observable<HttpResult<String>> deleteJpushSysMsgRecord(@Header("timestamp") long timestamp,
                                                           @Header("sign") String sign,
                                                           @Header("Authorization") String token,
                                                           @Path("ids") String ids);

    // ??????????????????
    @PUT("app/consultation/editFollowTime")
    Observable<HttpResult<String>> consultationEditFollowTime(@Header("timestamp") long timestamp,
                                                              @Header("sign") String sign,
                                                              @Header("Authorization") String token,
                                                              @Query("days") int days,
                                                              @Query("id") int id);


    // ??????????????????
    @GET("app/consultation/pushVideoConsulationNotice")
    Observable<HttpResult<String>> pushVideoConsulationNotice(@Header("timestamp") long timestamp,
                                                              @Header("sign") String sign,
                                                              @Header("Authorization") String token,
                                                              @Query("consulationId") int consulationId);
}
