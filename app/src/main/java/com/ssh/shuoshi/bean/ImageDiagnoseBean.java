package com.ssh.shuoshi.bean;

import com.ssh.shuoshi.bean.team.HisDoctorExpertTeamMemberDtosBean;

import java.io.Serializable;
import java.util.List;

/**
 * created by hwt on 2020/12/17
 */
public class ImageDiagnoseBean implements Serializable {

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

    public static class RowsBean implements Serializable {

        private Object searchValue;
        private Object createBy;
        private String createTime;
        private Object updateBy;
        private Object updateTime;
        private Object remark;
        private ParamsBean params;
        private int id;
        private String consultationNo;
        private int doctorId;
        private int patientId;
        private int userId;
        private double price;
        private int state;
        private int orderState;
        private String illnessDesc;
        private int consultationTypeId;
        private Integer applyRefundFlag;
        private Integer visitedFlag;
        private String checkImg;
        private String visitHospitalDiag;
        private String payTime;
        private String faceImg;
        private String furImg;
        private Integer doctorExperTeamId;
        private Object followUpFlag;
        private Object followUpTime;
        private String startTime;
        private String endTime;
        //实际问诊开始时间
        private String actualStartTime;
        private String actualEndTime;
        private String patientName;
        private String userImg;
        private String sexName;
        private int patientAge;
        private String doctorName;
        private String headImg;
        private String doctorDeptName;
        private String doctorTitleName;
        private String hospitalName;
        //专家团队名称
        private String doctorExperTeamName;
        //问诊状态
        private String consultationStateName;
        private String consultationOrderStateName;
        private String consultationTypeName;
        //处方ID
        private Integer prescriptionId;
        //处方医生Id
        private Integer prescriptionDoctorId;
        //问诊小结Id
        private Integer emrId;
        //问诊小结医生Id
        private Integer emrDoctorId;
        private int commentId;
        private String diagDesc;

        //历史处方状态0：无 1：西医，2：中医，3：全部
        private int status;
        //问诊订单编号
        private String consultationOrderNo;
        //处方状态，0待签名，1待审方，2有效，3过期作废，4处方审批未通过作废
        private Integer prescriptionState;
        //审批状态：0 未审方，1同意，2拒绝
        private Integer prescriptionApprovalState;
        private String emrIllnessDesc;
        private String emrDiagDesc;
        private String emrOrders;
        private Object hisPrescriptionDto;
        private List<String> checkImgs;
        private String groupId;
        //处方类别ID,1:西医，2中医
        private Integer perscriptionTypeId;
        private List<HisDoctorExpertTeamMemberDtosBean> hisDoctorExpertTeamMemberDtos;


        public Integer getPrescriptionDoctorId() {
            return prescriptionDoctorId;
        }

        public void setPrescriptionDoctorId(Integer prescriptionDoctorId) {
            this.prescriptionDoctorId = prescriptionDoctorId;
        }

        public Integer getEmrDoctorId() {
            return emrDoctorId;
        }

        public void setEmrDoctorId(Integer emrDoctorId) {
            this.emrDoctorId = emrDoctorId;
        }

        public List<HisDoctorExpertTeamMemberDtosBean> getHisDoctorExpertTeamMemberDtos() {
            return hisDoctorExpertTeamMemberDtos;
        }

        public void setHisDoctorExpertTeamMemberDtos(List<HisDoctorExpertTeamMemberDtosBean> hisDoctorExpertTeamMemberDtos) {
            this.hisDoctorExpertTeamMemberDtos = hisDoctorExpertTeamMemberDtos;
        }

        public String getGroupId() {
            return groupId;
        }

        public void setGroupId(String groupId) {
            this.groupId = groupId;
        }

        public Integer getPerscriptionTypeId() {
            return perscriptionTypeId;
        }

        public void setPerscriptionTypeId(Integer perscriptionTypeId) {
            this.perscriptionTypeId = perscriptionTypeId;
        }

        public String getConsultationOrderNo() {
            return consultationOrderNo;
        }

        public void setConsultationOrderNo(String consultationOrderNo) {
            this.consultationOrderNo = consultationOrderNo;
        }

        public Integer getPrescriptionState() {
            return prescriptionState;
        }

        public void setPrescriptionState(Integer prescriptionState) {
            this.prescriptionState = prescriptionState;
        }

        public Integer getPrescriptionApprovalState() {
            return prescriptionApprovalState;
        }

        public void setPrescriptionApprovalState(Integer prescriptionApprovalState) {
            this.prescriptionApprovalState = prescriptionApprovalState;
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

        public Object getHisPrescriptionDto() {
            return hisPrescriptionDto;
        }

        public void setHisPrescriptionDto(Object hisPrescriptionDto) {
            this.hisPrescriptionDto = hisPrescriptionDto;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public String getDiagDesc() {
            return diagDesc;
        }

        public void setDiagDesc(String diagDesc) {
            this.diagDesc = diagDesc;
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

        public Integer getApplyRefundFlag() {
            return applyRefundFlag;
        }

        public void setApplyRefundFlag(Integer applyRefundFlag) {
            this.applyRefundFlag = applyRefundFlag;
        }

        public Integer getVisitedFlag() {
            return visitedFlag;
        }

        public void setVisitedFlag(Integer visitedFlag) {
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

        public Integer getDoctorExperTeamId() {
            return doctorExperTeamId;
        }

        public void setDoctorExperTeamId(Integer doctorExperTeamId) {
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

        public String getStartTime() {
            return startTime;
        }

        public void setStartTime(String startTime) {
            this.startTime = startTime;
        }

        public String getEndTime() {
            return endTime;
        }

        public void setEndTime(String endTime) {
            this.endTime = endTime;
        }

        public String getActualStartTime() {
            return actualStartTime;
        }

        public void setActualStartTime(String actualStartTime) {
            this.actualStartTime = actualStartTime;
        }

        public String getActualEndTime() {
            return actualEndTime;
        }

        public void setActualEndTime(String actualEndTime) {
            this.actualEndTime = actualEndTime;
        }

        public String getPatientName() {
            return patientName;
        }

        public void setPatientName(String patientName) {
            this.patientName = patientName;
        }

        public String getUserImg() {
            return userImg;
        }

        public void setUserImg(String userImg) {
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

        public String getHeadImg() {
            return headImg;
        }

        public void setHeadImg(String headImg) {
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

        public String getDoctorExperTeamName() {
            return doctorExperTeamName;
        }

        public void setDoctorExperTeamName(String doctorExperTeamName) {
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

        public Integer getPrescriptionId() {
            return prescriptionId;
        }

        public void setPrescriptionId(Integer prescriptionId) {
            this.prescriptionId = prescriptionId;
        }

        public Integer getEmrId() {
            return emrId;
        }

        public void setEmrId(Integer emrId) {
            this.emrId = emrId;
        }

        public int getCommentId() {
            return commentId;
        }

        public void setCommentId(int commentId) {
            this.commentId = commentId;
        }

        public List<String> getCheckImgs() {
            return checkImgs;
        }

        public void setCheckImgs(List<String> checkImgs) {
            this.checkImgs = checkImgs;
        }

        public static class ParamsBean implements Serializable {
        }
    }
}
