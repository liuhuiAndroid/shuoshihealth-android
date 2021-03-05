package com.ssh.shuoshi.bean.prescription;

import java.io.Serializable;
import java.util.List;

/**
 * created by hwt on 2021/1/4
 */
public class PrescriptionStateBean implements Serializable {

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
        /**
         * searchValue : null
         * createBy : null
         * createTime : 2021-01-03 14:37
         * updateBy : null
         * updateTime : null
         * remark : null
         * params : {}
         * id : 352
         * perscriptionNo : PN210103143728064
         * consultationId : 218
         * visitDate : 2021-01-03 14:37
         * patientId : 51
         * deptId : null
         * doctorId : 61
         * doctorName : null
         * pharmacistId : null
         * pharmacistName : null
         * approvalState : 0
         * approvedTime : null
         * approvedOpinion : null
         * state : 5
         * perscriptionTypeId : 1
         * paymentState : 0
         * processMethod : null
         * orders : null
         * patientName : yanm
         * sexName : 女
         * consulationNo : CN210101170517012
         * patientSex : 2
         * patientAge : 27
         * diagDesc : 等孢子虫病
         * deptName : null
         * approvalStateName : 未审核
         * perscriptionTypeName : 西成药处方
         * stateName : 处方审批未通过作废
         * paymentStateName : 未购买
         * hisPrescriptionDetailDtos : [{"searchValue":null,"createBy":null,"createTime":"2021-01-03 14:37","updateBy":null,"updateTime":null,"remark":null,"params":{},"id":458,"prescriptionId":352,"phamId":867,"phamName":"999 华蟾素口服液 10ml*6支","phamSpec":"10ml*6支","units":"盒","amount":3,"dosage":6,"dosageUnits":"毫克","frequency":"每日2次","administration":"饭前口服","freqDetail":null,"price":209.7,"totalPrice":629.1,"phamTypeId":1,"perscriptionNo":"PN210103143728064","phamOutCode":"5115.0","phamBrand":null,"phamTypeName":"西药","hisPrescriptionTcmDetails":null}]
         * hisPrescriptionOrder : null
         */

        private Object searchValue;
        private Object createBy;
        private String createTime;
        private Object updateBy;
        private Object updateTime;
        private Object remark;
        private ParamsBean params;
        private int id;
        private String perscriptionNo;
        private int consultationId;
        private String visitDate;
        private int patientId;
        private Object deptId;
        private int doctorId;
        private Object doctorName;
        private Object pharmacistId;
        private Object pharmacistName;
        private int approvalState;
        private Object approvedTime;
        private Object approvedOpinion;
        private int state;
        private int perscriptionTypeId;
        private int paymentState;
        private Object processMethod;
        private String orders;
        private String patientName;
        private String sexName;
        private String consulationNo;
        private int patientSex;
        private int patientAge;
        private String diagDesc;
        private String deptName;
        private String approvalStateName;
        private String perscriptionTypeName;
        private String stateName;
        private String paymentStateName;
        private Object hisPrescriptionOrder;
        private List<HisPrescriptionDetailDtosBean> hisPrescriptionDetailDtos;

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

        public String getPerscriptionNo() {
            return perscriptionNo;
        }

        public void setPerscriptionNo(String perscriptionNo) {
            this.perscriptionNo = perscriptionNo;
        }

        public int getConsultationId() {
            return consultationId;
        }

        public void setConsultationId(int consultationId) {
            this.consultationId = consultationId;
        }

        public String getVisitDate() {
            return visitDate;
        }

        public void setVisitDate(String visitDate) {
            this.visitDate = visitDate;
        }

        public int getPatientId() {
            return patientId;
        }

        public void setPatientId(int patientId) {
            this.patientId = patientId;
        }

        public Object getDeptId() {
            return deptId;
        }

        public void setDeptId(Object deptId) {
            this.deptId = deptId;
        }

        public int getDoctorId() {
            return doctorId;
        }

        public void setDoctorId(int doctorId) {
            this.doctorId = doctorId;
        }

        public Object getDoctorName() {
            return doctorName;
        }

        public void setDoctorName(Object doctorName) {
            this.doctorName = doctorName;
        }

        public Object getPharmacistId() {
            return pharmacistId;
        }

        public void setPharmacistId(Object pharmacistId) {
            this.pharmacistId = pharmacistId;
        }

        public Object getPharmacistName() {
            return pharmacistName;
        }

        public void setPharmacistName(Object pharmacistName) {
            this.pharmacistName = pharmacistName;
        }

        public int getApprovalState() {
            return approvalState;
        }

        public void setApprovalState(int approvalState) {
            this.approvalState = approvalState;
        }

        public Object getApprovedTime() {
            return approvedTime;
        }

        public void setApprovedTime(Object approvedTime) {
            this.approvedTime = approvedTime;
        }

        public Object getApprovedOpinion() {
            return approvedOpinion;
        }

        public void setApprovedOpinion(Object approvedOpinion) {
            this.approvedOpinion = approvedOpinion;
        }

        public int getState() {
            return state;
        }

        public void setState(int state) {
            this.state = state;
        }

        public int getPerscriptionTypeId() {
            return perscriptionTypeId;
        }

        public void setPerscriptionTypeId(int perscriptionTypeId) {
            this.perscriptionTypeId = perscriptionTypeId;
        }

        public int getPaymentState() {
            return paymentState;
        }

        public void setPaymentState(int paymentState) {
            this.paymentState = paymentState;
        }

        public Object getProcessMethod() {
            return processMethod;
        }

        public void setProcessMethod(Object processMethod) {
            this.processMethod = processMethod;
        }

        public String getOrders() {
            return orders;
        }

        public void setOrders(String orders) {
            this.orders = orders;
        }

        public String getPatientName() {
            return patientName;
        }

        public void setPatientName(String patientName) {
            this.patientName = patientName;
        }

        public String getSexName() {
            return sexName;
        }

        public void setSexName(String sexName) {
            this.sexName = sexName;
        }

        public String getConsulationNo() {
            return consulationNo;
        }

        public void setConsulationNo(String consulationNo) {
            this.consulationNo = consulationNo;
        }

        public int getPatientSex() {
            return patientSex;
        }

        public void setPatientSex(int patientSex) {
            this.patientSex = patientSex;
        }

        public int getPatientAge() {
            return patientAge;
        }

        public void setPatientAge(int patientAge) {
            this.patientAge = patientAge;
        }

        public String getDiagDesc() {
            return diagDesc;
        }

        public void setDiagDesc(String diagDesc) {
            this.diagDesc = diagDesc;
        }

        public String getDeptName() {
            return deptName;
        }

        public void setDeptName(String deptName) {
            this.deptName = deptName;
        }

        public String getApprovalStateName() {
            return approvalStateName;
        }

        public void setApprovalStateName(String approvalStateName) {
            this.approvalStateName = approvalStateName;
        }

        public String getPerscriptionTypeName() {
            return perscriptionTypeName;
        }

        public void setPerscriptionTypeName(String perscriptionTypeName) {
            this.perscriptionTypeName = perscriptionTypeName;
        }

        public String getStateName() {
            return stateName;
        }

        public void setStateName(String stateName) {
            this.stateName = stateName;
        }

        public String getPaymentStateName() {
            return paymentStateName;
        }

        public void setPaymentStateName(String paymentStateName) {
            this.paymentStateName = paymentStateName;
        }

        public Object getHisPrescriptionOrder() {
            return hisPrescriptionOrder;
        }

        public void setHisPrescriptionOrder(Object hisPrescriptionOrder) {
            this.hisPrescriptionOrder = hisPrescriptionOrder;
        }

        public List<HisPrescriptionDetailDtosBean> getHisPrescriptionDetailDtos() {
            return hisPrescriptionDetailDtos;
        }

        public void setHisPrescriptionDetailDtos(List<HisPrescriptionDetailDtosBean> hisPrescriptionDetailDtos) {
            this.hisPrescriptionDetailDtos = hisPrescriptionDetailDtos;
        }

        public static class ParamsBean implements Serializable {
        }
    }
}
