package com.ssh.shuoshi.bean.patient;


import com.ssh.shuoshi.bean.prescription.HisPrescriptionDtoBean;

import java.util.ArrayList;
import java.util.List;

/**
 * 患者病历实体类
 * created by hwt on 2021/1/4
 */
public class MedicalHistoryBean {
    private int total;
    private int totalPage;
    private List<RowsBean> rows;

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }

    public List<RowsBean> getRows() {
        return rows;
    }

    public void setRows(List<RowsBean> rows) {
        this.rows = rows;
    }

    public static class RowsBean {
        /**
         * searchValue : null
         * createBy : null
         * createTime : 2021-01-01 17:11
         * updateBy : null
         * updateTime : null
         * remark : null
         * params : {}
         * id : 220
         * consultationNo : CN210101171146015
         * consultationOrderNo : CON210101171146015
         * doctorId : 61
         * patientId : 71
         * userId : 26
         * price : 0.01
         * state : 3
         * orderState : 5
         * illnessDesc : qweeeeeeeeeeeeeeeeeeee
         * consultationTypeId : 1
         * applyRefundFlag : 0
         * visitedFlag : 1
         * checkImg : null
         * visitHospitalDiag : 1111
         * payTime : 2021-01-01
         * faceImg : test/2021/01/01/usertest/26/2e523289-2283-4446-80a3-3014280df46b.jpg
         * furImg : test/2021/01/01/usertest/26/1c6675a4-6c4d-4f2f-83d8-d6a2e054a73e.jpg
         * doctorExperTeamId : null
         * followUpFlag : null
         * followUpTime : null
         * startTime : null
         * endTime : null
         * actualStartTime : 2021-01-01 17:12:42
         * actualEndTime : null
         * withdrawalReason : null
         * groupId : health_26_58_1
         * patientName : 朱森林患者
         * userImg : null
         * sexName : 男
         * patientAge : 28
         * doctorName : dtb
         * headImg : null
         * doctorDeptName : 中西医结合科
         * doctorTitleName : 主任医师
         * hospitalName : 女子监狱风云
         * doctorExperTeamName : null
         * consultationStateName : 已完成
         * consultationOrderStateName : 已取消
         * consultationTypeName : 图文问诊
         * prescriptionId : 224
         * prescriptionState : 1
         * prescriptionApprovalState : 0
         * perscriptionTypeId : 1
         * emrId : 136
         * emrIllnessDesc : 你回家吃点感冒药
         * emrDiagDesc : 急性鼻咽炎［普通感冒］
         * emrOrders : 回家休息
         * commentId : null
         * status : null
         * checkImgs : null
         */

        private Object searchValue;
        private Object createBy;
        private String createTime;
        private Object updateBy;
        private Object updateTime;
        private Object remark;
        private ParamsBean params;
        private int id;
        private String consultationNo;
        private String consultationOrderNo;
        private int doctorId;
        private int patientId;
        private int userId;
        private double price;
        private int state;
        private int orderState;
        private String illnessDesc;
        private int consultationTypeId;
        private int applyRefundFlag;
        private int visitedFlag;
        private String checkImg;
        private String visitHospitalDiag;
        private String payTime;
        private String faceImg;
        private String furImg;
        private Object doctorExperTeamId;
        private Object followUpFlag;
        private Object followUpTime;
        private Object startTime;
        private Object endTime;
        private String actualStartTime;
        private Object actualEndTime;
        private Object withdrawalReason;
        private String groupId;
        private String patientName;
        private Object userImg;
        private String sexName;
        private int patientAge;
        private String doctorName;
        private Object headImg;
        private String doctorDeptName;
        private String doctorTitleName;
        private String hospitalName;
        private Object doctorExperTeamName;
        private String consultationStateName;
        private String consultationOrderStateName;
        private String consultationTypeName;
        private int prescriptionId;
        private int prescriptionState;
        private int prescriptionApprovalState;
        private int perscriptionTypeId;
        private Integer emrId;
        private String emrIllnessDesc;
        private String emrDiagDesc;
        private String emrOrders;
        private Object commentId;
        private Object status;
        private HisPrescriptionDtoBean hisPrescriptionDto;

        //下载地址
        private List<String> checkImgs;
        //展示地址
        private List<String> photoImages;

        private boolean isDownloadPhoto;

        public boolean isDownloadPhoto() {
            return isDownloadPhoto;
        }

        public void setDownloadPhoto(boolean downloadPhoto) {
            isDownloadPhoto = downloadPhoto;
        }

        public List<String> getPhotoImages() {
            return photoImages;
        }

        public void setPhotoImages(List<String> photoImages) {
            this.photoImages = photoImages;
        }

        public Object getSearchValue() {
            return searchValue;
        }

        public void setSearchValue(Object searchValue) {
            this.searchValue = searchValue;
        }

        public Object getCreateBy() {
            return createBy;
        }

        public void setCreateBy(Object createBy) {
            this.createBy = createBy;
        }

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }

        public Object getUpdateBy() {
            return updateBy;
        }

        public void setUpdateBy(Object updateBy) {
            this.updateBy = updateBy;
        }

        public Object getUpdateTime() {
            return updateTime;
        }

        public void setUpdateTime(Object updateTime) {
            this.updateTime = updateTime;
        }

        public Object getRemark() {
            return remark;
        }

        public void setRemark(Object remark) {
            this.remark = remark;
        }

        public ParamsBean getParams() {
            return params;
        }

        public void setParams(ParamsBean params) {
            this.params = params;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getConsultationNo() {
            return consultationNo;
        }

        public void setConsultationNo(String consultationNo) {
            this.consultationNo = consultationNo;
        }

        public String getConsultationOrderNo() {
            return consultationOrderNo;
        }

        public void setConsultationOrderNo(String consultationOrderNo) {
            this.consultationOrderNo = consultationOrderNo;
        }

        public int getDoctorId() {
            return doctorId;
        }

        public void setDoctorId(int doctorId) {
            this.doctorId = doctorId;
        }

        public int getPatientId() {
            return patientId;
        }

        public void setPatientId(int patientId) {
            this.patientId = patientId;
        }

        public int getUserId() {
            return userId;
        }

        public void setUserId(int userId) {
            this.userId = userId;
        }

        public double getPrice() {
            return price;
        }

        public void setPrice(double price) {
            this.price = price;
        }

        public int getState() {
            return state;
        }

        public void setState(int state) {
            this.state = state;
        }

        public int getOrderState() {
            return orderState;
        }

        public void setOrderState(int orderState) {
            this.orderState = orderState;
        }

        public String getIllnessDesc() {
            return illnessDesc;
        }

        public void setIllnessDesc(String illnessDesc) {
            this.illnessDesc = illnessDesc;
        }

        public int getConsultationTypeId() {
            return consultationTypeId;
        }

        public void setConsultationTypeId(int consultationTypeId) {
            this.consultationTypeId = consultationTypeId;
        }

        public int getApplyRefundFlag() {
            return applyRefundFlag;
        }

        public void setApplyRefundFlag(int applyRefundFlag) {
            this.applyRefundFlag = applyRefundFlag;
        }

        public int getVisitedFlag() {
            return visitedFlag;
        }

        public void setVisitedFlag(int visitedFlag) {
            this.visitedFlag = visitedFlag;
        }

        public String getCheckImg() {
            return checkImg;
        }

        public void setCheckImg(String checkImg) {
            this.checkImg = checkImg;
        }

        public String getVisitHospitalDiag() {
            return visitHospitalDiag;
        }

        public void setVisitHospitalDiag(String visitHospitalDiag) {
            this.visitHospitalDiag = visitHospitalDiag;
        }

        public String getPayTime() {
            return payTime;
        }

        public void setPayTime(String payTime) {
            this.payTime = payTime;
        }

        public String getFaceImg() {
            return faceImg;
        }

        public void setFaceImg(String faceImg) {
            this.faceImg = faceImg;
        }

        public String getFurImg() {
            return furImg;
        }

        public void setFurImg(String furImg) {
            this.furImg = furImg;
        }

        public Object getDoctorExperTeamId() {
            return doctorExperTeamId;
        }

        public void setDoctorExperTeamId(Object doctorExperTeamId) {
            this.doctorExperTeamId = doctorExperTeamId;
        }

        public Object getFollowUpFlag() {
            return followUpFlag;
        }

        public void setFollowUpFlag(Object followUpFlag) {
            this.followUpFlag = followUpFlag;
        }

        public Object getFollowUpTime() {
            return followUpTime;
        }

        public void setFollowUpTime(Object followUpTime) {
            this.followUpTime = followUpTime;
        }

        public Object getStartTime() {
            return startTime;
        }

        public void setStartTime(Object startTime) {
            this.startTime = startTime;
        }

        public Object getEndTime() {
            return endTime;
        }

        public void setEndTime(Object endTime) {
            this.endTime = endTime;
        }

        public String getActualStartTime() {
            return actualStartTime;
        }

        public void setActualStartTime(String actualStartTime) {
            this.actualStartTime = actualStartTime;
        }

        public Object getActualEndTime() {
            return actualEndTime;
        }

        public void setActualEndTime(Object actualEndTime) {
            this.actualEndTime = actualEndTime;
        }

        public Object getWithdrawalReason() {
            return withdrawalReason;
        }

        public void setWithdrawalReason(Object withdrawalReason) {
            this.withdrawalReason = withdrawalReason;
        }

        public String getGroupId() {
            return groupId;
        }

        public void setGroupId(String groupId) {
            this.groupId = groupId;
        }

        public String getPatientName() {
            return patientName;
        }

        public void setPatientName(String patientName) {
            this.patientName = patientName;
        }

        public Object getUserImg() {
            return userImg;
        }

        public void setUserImg(Object userImg) {
            this.userImg = userImg;
        }

        public String getSexName() {
            return sexName;
        }

        public void setSexName(String sexName) {
            this.sexName = sexName;
        }

        public int getPatientAge() {
            return patientAge;
        }

        public void setPatientAge(int patientAge) {
            this.patientAge = patientAge;
        }

        public String getDoctorName() {
            return doctorName;
        }

        public void setDoctorName(String doctorName) {
            this.doctorName = doctorName;
        }

        public Object getHeadImg() {
            return headImg;
        }

        public void setHeadImg(Object headImg) {
            this.headImg = headImg;
        }

        public String getDoctorDeptName() {
            return doctorDeptName;
        }

        public void setDoctorDeptName(String doctorDeptName) {
            this.doctorDeptName = doctorDeptName;
        }

        public String getDoctorTitleName() {
            return doctorTitleName;
        }

        public void setDoctorTitleName(String doctorTitleName) {
            this.doctorTitleName = doctorTitleName;
        }

        public String getHospitalName() {
            return hospitalName;
        }

        public void setHospitalName(String hospitalName) {
            this.hospitalName = hospitalName;
        }

        public Object getDoctorExperTeamName() {
            return doctorExperTeamName;
        }

        public void setDoctorExperTeamName(Object doctorExperTeamName) {
            this.doctorExperTeamName = doctorExperTeamName;
        }

        public String getConsultationStateName() {
            return consultationStateName;
        }

        public void setConsultationStateName(String consultationStateName) {
            this.consultationStateName = consultationStateName;
        }

        public String getConsultationOrderStateName() {
            return consultationOrderStateName;
        }

        public void setConsultationOrderStateName(String consultationOrderStateName) {
            this.consultationOrderStateName = consultationOrderStateName;
        }

        public String getConsultationTypeName() {
            return consultationTypeName;
        }

        public void setConsultationTypeName(String consultationTypeName) {
            this.consultationTypeName = consultationTypeName;
        }

        public int getPrescriptionId() {
            return prescriptionId;
        }

        public void setPrescriptionId(int prescriptionId) {
            this.prescriptionId = prescriptionId;
        }

        public int getPrescriptionState() {
            return prescriptionState;
        }

        public void setPrescriptionState(int prescriptionState) {
            this.prescriptionState = prescriptionState;
        }

        public int getPrescriptionApprovalState() {
            return prescriptionApprovalState;
        }

        public void setPrescriptionApprovalState(int prescriptionApprovalState) {
            this.prescriptionApprovalState = prescriptionApprovalState;
        }

        public int getPerscriptionTypeId() {
            return perscriptionTypeId;
        }

        public void setPerscriptionTypeId(int perscriptionTypeId) {
            this.perscriptionTypeId = perscriptionTypeId;
        }

        public Integer getEmrId() {
            return emrId;
        }

        public void setEmrId(Integer emrId) {
            this.emrId = emrId;
        }

        public String getEmrIllnessDesc() {
            return emrIllnessDesc;
        }

        public void setEmrIllnessDesc(String emrIllnessDesc) {
            this.emrIllnessDesc = emrIllnessDesc;
        }

        public String getEmrDiagDesc() {
            return emrDiagDesc;
        }

        public void setEmrDiagDesc(String emrDiagDesc) {
            this.emrDiagDesc = emrDiagDesc;
        }

        public String getEmrOrders() {
            return emrOrders;
        }

        public void setEmrOrders(String emrOrders) {
            this.emrOrders = emrOrders;
        }

        public Object getCommentId() {
            return commentId;
        }

        public void setCommentId(Object commentId) {
            this.commentId = commentId;
        }

        public Object getStatus() {
            return status;
        }

        public void setStatus(Object status) {
            this.status = status;
        }

        public List<String> getCheckImgs() {
            return checkImgs;
        }

        public void setCheckImgs(List<String> checkImgs) {
            this.checkImgs = checkImgs;
        }

        public HisPrescriptionDtoBean getHisPrescriptionDto() {
            return hisPrescriptionDto;
        }

        public void setHisPrescriptionDto(HisPrescriptionDtoBean hisPrescriptionDto) {
            this.hisPrescriptionDto = hisPrescriptionDto;
        }

        public static class ParamsBean {
        }

    }
}
