package com.ssh.shuoshi.bean.patient;

import java.util.List;

/**
 * created by hwt on 2021/1/4
 */
public class FollowUpListBean {

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
         * createTime : 2021-01-04 14:42
         * updateBy : null
         * updateTime : null
         * remark : null
         * params : {}
         * id : 321
         * consultationNo : CN210104144212013
         * consultationOrderNo : CON210104144212013
         * doctorId : 61
         * patientId : 93
         * userId : 25
         * price : 0.0
         * state : 5
         * orderState : 3
         * illnessDesc : 动荡不定宝贝巴登巴登很烦很好的惠风和畅
         * consultationTypeId : 1
         * applyRefundFlag : 0
         * visitedFlag : 0
         * checkImg : null
         * visitHospitalDiag : null
         * payTime : 2021-01-04
         * faceImg : null
         * furImg : null
         * doctorExperTeamId : null
         * followUpFlag : 1
         * followUpTime : 2021-01-08
         * startTime : null
         * endTime : null
         * actualStartTime : null
         * actualEndTime : null
         * withdrawalReason : null
         * groupId : health_321_1_1
         * patientName : 为了测试数据
         * userImg : null
         * sexName : 女
         * patientAge : 89
         * doctorName : dtb
         * headImg : null
         * doctorDeptName : 中西医结合科
         * doctorTitleName : 主任医师
         * hospitalName : 女子监狱风云
         * doctorExperTeamName : null
         * consultationStateName : 已取消
         * consultationOrderStateName : 已退款
         * consultationTypeName : 图文问诊
         * prescriptionId : null
         * prescriptionState : null
         * prescriptionApprovalState : null
         * perscriptionTypeId : null
         * emrId : null
         * emrIllnessDesc : null
         * emrDiagDesc : null
         * emrOrders : null
         * commentId : null
         * status : null
         * checkImgs : null
         * hisPrescriptionDto : null
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
        private Object checkImg;
        private Object visitHospitalDiag;
        private String payTime;
        private Object faceImg;
        private Object furImg;
        private Object doctorExperTeamId;
        private int followUpFlag;
        private String followUpTime;
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
        private Object prescriptionId;
        private Object prescriptionState;
        private Object prescriptionApprovalState;
        private Object perscriptionTypeId;
        private Object emrId;
        private Object emrIllnessDesc;
        private String emrDiagDesc;
        private Object emrOrders;
        private Object commentId;
        private Object status;
        private Object checkImgs;
        private Object hisPrescriptionDto;

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

        public Object getCheckImg() {
            return checkImg;
        }

        public void setCheckImg(Object checkImg) {
            this.checkImg = checkImg;
        }

        public Object getVisitHospitalDiag() {
            return visitHospitalDiag;
        }

        public void setVisitHospitalDiag(Object visitHospitalDiag) {
            this.visitHospitalDiag = visitHospitalDiag;
        }

        public String getPayTime() {
            return payTime;
        }

        public void setPayTime(String payTime) {
            this.payTime = payTime;
        }

        public Object getFaceImg() {
            return faceImg;
        }

        public void setFaceImg(Object faceImg) {
            this.faceImg = faceImg;
        }

        public Object getFurImg() {
            return furImg;
        }

        public void setFurImg(Object furImg) {
            this.furImg = furImg;
        }

        public Object getDoctorExperTeamId() {
            return doctorExperTeamId;
        }

        public void setDoctorExperTeamId(Object doctorExperTeamId) {
            this.doctorExperTeamId = doctorExperTeamId;
        }

        public int getFollowUpFlag() {
            return followUpFlag;
        }

        public void setFollowUpFlag(int followUpFlag) {
            this.followUpFlag = followUpFlag;
        }

        public String getFollowUpTime() {
            return followUpTime;
        }

        public void setFollowUpTime(String followUpTime) {
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

        public Object getPrescriptionId() {
            return prescriptionId;
        }

        public void setPrescriptionId(Object prescriptionId) {
            this.prescriptionId = prescriptionId;
        }

        public Object getPrescriptionState() {
            return prescriptionState;
        }

        public void setPrescriptionState(Object prescriptionState) {
            this.prescriptionState = prescriptionState;
        }

        public Object getPrescriptionApprovalState() {
            return prescriptionApprovalState;
        }

        public void setPrescriptionApprovalState(Object prescriptionApprovalState) {
            this.prescriptionApprovalState = prescriptionApprovalState;
        }

        public Object getPerscriptionTypeId() {
            return perscriptionTypeId;
        }

        public void setPerscriptionTypeId(Object perscriptionTypeId) {
            this.perscriptionTypeId = perscriptionTypeId;
        }

        public Object getEmrId() {
            return emrId;
        }

        public void setEmrId(Object emrId) {
            this.emrId = emrId;
        }

        public Object getEmrIllnessDesc() {
            return emrIllnessDesc;
        }

        public void setEmrIllnessDesc(Object emrIllnessDesc) {
            this.emrIllnessDesc = emrIllnessDesc;
        }

        public String getEmrDiagDesc() {
            return emrDiagDesc;
        }

        public void setEmrDiagDesc(String emrDiagDesc) {
            this.emrDiagDesc = emrDiagDesc;
        }

        public Object getEmrOrders() {
            return emrOrders;
        }

        public void setEmrOrders(Object emrOrders) {
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

        public Object getCheckImgs() {
            return checkImgs;
        }

        public void setCheckImgs(Object checkImgs) {
            this.checkImgs = checkImgs;
        }

        public Object getHisPrescriptionDto() {
            return hisPrescriptionDto;
        }

        public void setHisPrescriptionDto(Object hisPrescriptionDto) {
            this.hisPrescriptionDto = hisPrescriptionDto;
        }

        public static class ParamsBean {
        }
    }
}
