package com.ssh.shuoshi.bean.prescription;

import java.io.Serializable;
import java.util.List;

/**
 * 处方	处方信息   第一层
 * 历史处方，根据ID获取处方、处方订单详细信息 通用合并
 * created by hwt on 2021/1/4
 */
public class HisPrescriptionDtoBean implements Serializable {

    private Object searchValue;
    private Object createBy;
    private String createTime;
    private Object updateBy;
    private Object updateTime;
    private Object remark;
    private ParamsBeanX params;
    private int id;
    private String perscriptionNo;
    private int consultationId;
    private String visitDate;
    private int patientId;
    private int deptId;
    private int doctorId;
    private String doctorName;
    private Object pharmacistId;
    private String pharmacistName;
    private int approvalState;
    private Object approvedTime;
    //审方意见
    private String approvedOpinion;
    private int state;
    private int perscriptionTypeId;
    private int paymentState;
    private Object processMethod;
    private Object orders;
    private String patientName;
    private String sexName;
    //	问诊编号
    private String consulationNo;
    private int patientSex;
    private int patientAge;
    private String diagDesc;
    private String deptName;
    private String approvalStateName;
    private String perscriptionTypeName;
    private String stateName;
    private String paymentStateName;
    private HisPrescriptionOrderBean hisPrescriptionOrder;
    private Object orderStateName;
    private Object orderState;
    private List<HisPrescriptionDetailDtosBean> hisPrescriptionDetailDtos;

    public class HisPrescriptionOrderBean implements Serializable {
        private String createBy;
        private String createTime;
        private String deliverTime;
        private String freightPrice;
        private Integer id;
        private String logisticsNo;
        private String logisticsVendor;
        private String orderNo;
        private Integer orderState;
        private Object params;
        private String payTime;
        private Integer prescriptionId;
        private String price;
        private String receiveAddress;
        private String receiveName;
        private String receivePhone;
        private String remark;
        private String searchValue;
        private String totalPrice;
        private String updateBy;
        private String updateTime;

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

        public String getDeliverTime() {
            return deliverTime;
        }

        public void setDeliverTime(String deliverTime) {
            this.deliverTime = deliverTime;
        }

        public String getFreightPrice() {
            return freightPrice;
        }

        public void setFreightPrice(String freightPrice) {
            this.freightPrice = freightPrice;
        }

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public String getLogisticsNo() {
            return logisticsNo;
        }

        public void setLogisticsNo(String logisticsNo) {
            this.logisticsNo = logisticsNo;
        }

        public String getLogisticsVendor() {
            return logisticsVendor;
        }

        public void setLogisticsVendor(String logisticsVendor) {
            this.logisticsVendor = logisticsVendor;
        }

        public String getOrderNo() {
            return orderNo;
        }

        public void setOrderNo(String orderNo) {
            this.orderNo = orderNo;
        }

        public Integer getOrderState() {
            return orderState;
        }

        public void setOrderState(Integer orderState) {
            this.orderState = orderState;
        }

        public Object getParams() {
            return params;
        }

        public void setParams(Object params) {
            this.params = params;
        }

        public String getPayTime() {
            return payTime;
        }

        public void setPayTime(String payTime) {
            this.payTime = payTime;
        }

        public Integer getPrescriptionId() {
            return prescriptionId;
        }

        public void setPrescriptionId(Integer prescriptionId) {
            this.prescriptionId = prescriptionId;
        }

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public String getReceiveAddress() {
            return receiveAddress;
        }

        public void setReceiveAddress(String receiveAddress) {
            this.receiveAddress = receiveAddress;
        }

        public String getReceiveName() {
            return receiveName;
        }

        public void setReceiveName(String receiveName) {
            this.receiveName = receiveName;
        }

        public String getReceivePhone() {
            return receivePhone;
        }

        public void setReceivePhone(String receivePhone) {
            this.receivePhone = receivePhone;
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

        public String getTotalPrice() {
            return totalPrice;
        }

        public void setTotalPrice(String totalPrice) {
            this.totalPrice = totalPrice;
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
    }

    public Object getOrderStateName() {
        return orderStateName;
    }

    public void setOrderStateName(Object orderStateName) {
        this.orderStateName = orderStateName;
    }

    public Object getOrderState() {
        return orderState;
    }

    public void setOrderState(Object orderState) {
        this.orderState = orderState;
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

    public ParamsBeanX getParams() {
        return params;
    }

    public void setParams(ParamsBeanX params) {
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

    public int getDeptId() {
        return deptId;
    }

    public void setDeptId(int deptId) {
        this.deptId = deptId;
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

    public Object getPharmacistId() {
        return pharmacistId;
    }

    public void setPharmacistId(Object pharmacistId) {
        this.pharmacistId = pharmacistId;
    }

    public String getPharmacistName() {
        return pharmacistName;
    }

    public void setPharmacistName(String pharmacistName) {
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

    public String getApprovedOpinion() {
        return approvedOpinion;
    }

    public void setApprovedOpinion(String approvedOpinion) {
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

    public Object getOrders() {
        return orders;
    }

    public void setOrders(Object orders) {
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

    public HisPrescriptionOrderBean getHisPrescriptionOrder() {
        return hisPrescriptionOrder;
    }

    public void setHisPrescriptionOrder(HisPrescriptionOrderBean hisPrescriptionOrder) {
        this.hisPrescriptionOrder = hisPrescriptionOrder;
    }

    public List<HisPrescriptionDetailDtosBean> getHisPrescriptionDetailDtos() {
        return hisPrescriptionDetailDtos;
    }

    public void setHisPrescriptionDetailDtos(List<HisPrescriptionDetailDtosBean> hisPrescriptionDetailDtos) {
        this.hisPrescriptionDetailDtos = hisPrescriptionDetailDtos;
    }

    public static class ParamsBeanX implements Serializable {
    }
}
