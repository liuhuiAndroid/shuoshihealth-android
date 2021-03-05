package com.ssh.shuoshi.bean;

import java.io.Serializable;
import java.util.List;

public class ConsultationBillBean {

    private double totalPrice;

    private HisConsultationBillDtos hisConsultationBillDtos;

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public HisConsultationBillDtos getHisConsultationBillDtos() {
        return hisConsultationBillDtos;
    }

    public void setHisConsultationBillDtos(HisConsultationBillDtos hisConsultationBillDtos) {
        this.hisConsultationBillDtos = hisConsultationBillDtos;
    }

    public class HisConsultationBillDtos {

        private int total;
        private List<ConsultationBean> rows;
        private int totalPage;

        public int getTotal() {
            return total;
        }

        public void setTotal(int total) {
            this.total = total;
        }

        public List<ConsultationBean> getRows() {
            return rows;
        }

        public void setRows(List<ConsultationBean> rows) {
            this.rows = rows;
        }

        public int getTotalPage() {
            return totalPage;
        }

        public void setTotalPage(int totalPage) {
            this.totalPage = totalPage;
        }
    }

    public class ConsultationBean implements Serializable {

        int acctingIndicator;
        double amount;
        String consultationCreateTime;
        int consultationId;
        String consultationNo;
        //问诊订单OrderState
        int consultationOrderState;
        String consultationOrderStateName;
        String consultationPayTime;
        int consultationState;
        String consultationStateName;
        int consultationTypeId;
        String consultationTypeName;
        String createBy;
        String createTime;
        int doctorDeptId;
        String doctorDeptName;
        String doctorHospitalName;
        int doctorId;
        String doctorName;
        String doctorTitleName;
        int id;
        String illnessDesc;
        String memo;
        int patientAge;
        int patientId;
        String patientName;
        int payMethodId;
        String remark;
        String searchValue;
        String sexName;
        String tradeNo;
        String updateBy;
        String updateTime;
        int userId;
        String visitHospitalDiag;
        String consultationOrderNo;
        int visitedFlag;

        public String getConsultationOrderNo() {
            return consultationOrderNo;
        }

        public void setConsultationOrderNo(String consultationOrderNo) {
            this.consultationOrderNo = consultationOrderNo;
        }

        public int getAcctingIndicator() {
            return acctingIndicator;
        }

        public void setAcctingIndicator(int acctingIndicator) {
            this.acctingIndicator = acctingIndicator;
        }

        public double getAmount() {
            return amount;
        }

        public void setAmount(double amount) {
            this.amount = amount;
        }

        public String getConsultationCreateTime() {
            return consultationCreateTime;
        }

        public void setConsultationCreateTime(String consultationCreateTime) {
            this.consultationCreateTime = consultationCreateTime;
        }

        public int getConsultationId() {
            return consultationId;
        }

        public void setConsultationId(int consultationId) {
            this.consultationId = consultationId;
        }

        public String getConsultationNo() {
            return consultationNo;
        }

        public void setConsultationNo(String consultationNo) {
            this.consultationNo = consultationNo;
        }

        public int getConsultationOrderState() {
            return consultationOrderState;
        }

        public void setConsultationOrderState(int consultationOrderState) {
            this.consultationOrderState = consultationOrderState;
        }

        public String getConsultationOrderStateName() {
            return consultationOrderStateName;
        }

        public void setConsultationOrderStateName(String consultationOrderStateName) {
            this.consultationOrderStateName = consultationOrderStateName;
        }

        public String getConsultationPayTime() {
            return consultationPayTime;
        }

        public void setConsultationPayTime(String consultationPayTime) {
            this.consultationPayTime = consultationPayTime;
        }

        public int getConsultationState() {
            return consultationState;
        }

        public void setConsultationState(int consultationState) {
            this.consultationState = consultationState;
        }

        public String getConsultationStateName() {
            return consultationStateName;
        }

        public void setConsultationStateName(String consultationStateName) {
            this.consultationStateName = consultationStateName;
        }

        public int getConsultationTypeId() {
            return consultationTypeId;
        }

        public void setConsultationTypeId(int consultationTypeId) {
            this.consultationTypeId = consultationTypeId;
        }

        public String getConsultationTypeName() {
            return consultationTypeName;
        }

        public void setConsultationTypeName(String consultationTypeName) {
            this.consultationTypeName = consultationTypeName;
        }

        public String getCreateBy() {
            return createBy;
        }

        public void setCreateBy(String createBy) {
            this.createBy = createBy;
        }

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }

        public int getDoctorDeptId() {
            return doctorDeptId;
        }

        public void setDoctorDeptId(int doctorDeptId) {
            this.doctorDeptId = doctorDeptId;
        }

        public String getDoctorDeptName() {
            return doctorDeptName;
        }

        public void setDoctorDeptName(String doctorDeptName) {
            this.doctorDeptName = doctorDeptName;
        }

        public String getDoctorHospitalName() {
            return doctorHospitalName;
        }

        public void setDoctorHospitalName(String doctorHospitalName) {
            this.doctorHospitalName = doctorHospitalName;
        }

        public int getDoctorId() {
            return doctorId;
        }

        public void setDoctorId(int doctorId) {
            this.doctorId = doctorId;
        }

        public String getDoctorName() {
            return doctorName;
        }

        public void setDoctorName(String doctorName) {
            this.doctorName = doctorName;
        }

        public String getDoctorTitleName() {
            return doctorTitleName;
        }

        public void setDoctorTitleName(String doctorTitleName) {
            this.doctorTitleName = doctorTitleName;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getIllnessDesc() {
            return illnessDesc;
        }

        public void setIllnessDesc(String illnessDesc) {
            this.illnessDesc = illnessDesc;
        }

        public String getMemo() {
            return memo;
        }

        public void setMemo(String memo) {
            this.memo = memo;
        }

        public int getPatientAge() {
            return patientAge;
        }

        public void setPatientAge(int patientAge) {
            this.patientAge = patientAge;
        }

        public int getPatientId() {
            return patientId;
        }

        public void setPatientId(int patientId) {
            this.patientId = patientId;
        }

        public String getPatientName() {
            return patientName;
        }

        public void setPatientName(String patientName) {
            this.patientName = patientName;
        }

        public int getPayMethodId() {
            return payMethodId;
        }

        public void setPayMethodId(int payMethodId) {
            this.payMethodId = payMethodId;
        }

        public String getRemark() {
            return remark;
        }

        public void setRemark(String remark) {
            this.remark = remark;
        }

        public String getSearchValue() {
            return searchValue;
        }

        public void setSearchValue(String searchValue) {
            this.searchValue = searchValue;
        }

        public String getSexName() {
            return sexName;
        }

        public void setSexName(String sexName) {
            this.sexName = sexName;
        }

        public String getTradeNo() {
            return tradeNo;
        }

        public void setTradeNo(String tradeNo) {
            this.tradeNo = tradeNo;
        }

        public String getUpdateBy() {
            return updateBy;
        }

        public void setUpdateBy(String updateBy) {
            this.updateBy = updateBy;
        }

        public String getUpdateTime() {
            return updateTime;
        }

        public void setUpdateTime(String updateTime) {
            this.updateTime = updateTime;
        }

        public int getUserId() {
            return userId;
        }

        public void setUserId(int userId) {
            this.userId = userId;
        }

        public String getVisitHospitalDiag() {
            return visitHospitalDiag;
        }

        public void setVisitHospitalDiag(String visitHospitalDiag) {
            this.visitHospitalDiag = visitHospitalDiag;
        }

        public int getVisitedFlag() {
            return visitedFlag;
        }

        public void setVisitedFlag(int visitedFlag) {
            this.visitedFlag = visitedFlag;
        }
    }
}
