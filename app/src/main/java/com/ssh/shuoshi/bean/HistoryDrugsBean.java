package com.ssh.shuoshi.bean;

import java.io.Serializable;
import java.util.List;

/**
 * 历史处方，根据ID获取处方、处方订单详细信息 通用
 * created by hwt on 2020/12/20
 */
public class HistoryDrugsBean implements Serializable {

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
    private String patientName;
    private String sexName;
    private int patientSex;
    private int patientAge;
    private String diagDesc;
    private String deptName;
    private String approvalStateName;
    private String perscriptionTypeName;
    private String stateName;
    private Object orderStateName;
    private Object orderState;
    private Object hisPrescriptionOrder;
    private String consulationNo;//	问诊编号
    private List<HisPrescriptionDetailDtosBean> hisPrescriptionDetailDtos;

    public String getConsulationNo() {
        return consulationNo;
    }

    public void setConsulationNo(String consulationNo) {
        this.consulationNo = consulationNo;
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

    public static class HisPrescriptionDetailDtosBean implements Serializable {
        /**
         * searchValue : null
         * createBy : null
         * createTime : 2020-12-28 19:23
         * updateBy : null
         * updateTime : null
         * remark : null
         * params : {}
         * id : 104
         * prescriptionId : 83
         * phamId : 2
         * phamName : 硝苯地平缓释片(Ⅰ)(扬子江)
         * phamSpec : 10mg*12粒/盒
         * units : null
         * amount : 5.0
         * dosage : 6.0
         * dosageUnits : 克
         * frequency : 每日3次
         * administration : 空腹口服
         * freqDetail : null
         * price : 0.02
         * totalPrice : 0.1
         * phamTypeId : 1
         * perscriptionNo : PN201228192329003
         * phamOutCode : 202012141727002
         * phamBrand : null
         * phamTypeName : 西药
         * hisPrescriptionTcmDetails : null
         */

        private Object searchValue;
        private Object createBy;
        private String createTime;
        private Object updateBy;
        private Object updateTime;
        private Object remark;
        private ParamsBeanX params;
        private int id;
        private int prescriptionId;
        private int phamId;
        private String phamName;
        private String phamSpec;
        private Object units;
        private int amount;
        private int dosage;
        private String dosageUnits;
        private String frequency;
        private String administration;
        private String freqDetail;
        private double price;
        private double totalPrice;
        private int phamTypeId;
        private String perscriptionNo;
        private String phamOutCode;
        private Object phamBrand;
        private String phamTypeName;
        private List<HisPrescriptionTcmDetailsBean> hisPrescriptionTcmDetails;

        public List<HisPrescriptionTcmDetailsBean> getHisPrescriptionTcmDetails() {
            return hisPrescriptionTcmDetails;
        }

        public void setHisPrescriptionTcmDetails(List<HisPrescriptionTcmDetailsBean> hisPrescriptionTcmDetails) {
            this.hisPrescriptionTcmDetails = hisPrescriptionTcmDetails;
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

        public int getPrescriptionId() {
            return prescriptionId;
        }

        public void setPrescriptionId(int prescriptionId) {
            this.prescriptionId = prescriptionId;
        }

        public int getPhamId() {
            return phamId;
        }

        public void setPhamId(int phamId) {
            this.phamId = phamId;
        }

        public String getPhamName() {
            return phamName;
        }

        public void setPhamName(String phamName) {
            this.phamName = phamName;
        }

        public String getPhamSpec() {
            return phamSpec;
        }

        public void setPhamSpec(String phamSpec) {
            this.phamSpec = phamSpec;
        }

        public Object getUnits() {
            return units;
        }

        public void setUnits(Object units) {
            this.units = units;
        }

        public int getAmount() {
            return amount;
        }

        public void setAmount(int amount) {
            this.amount = amount;
        }

        public int getDosage() {
            return dosage;
        }

        public void setDosage(int dosage) {
            this.dosage = dosage;
        }

        public String getDosageUnits() {
            return dosageUnits;
        }

        public void setDosageUnits(String dosageUnits) {
            this.dosageUnits = dosageUnits;
        }

        public String getFrequency() {
            return frequency;
        }

        public void setFrequency(String frequency) {
            this.frequency = frequency;
        }

        public String getAdministration() {
            return administration;
        }

        public void setAdministration(String administration) {
            this.administration = administration;
        }

        public String getFreqDetail() {
            return freqDetail;
        }

        public void setFreqDetail(String freqDetail) {
            this.freqDetail = freqDetail;
        }

        public double getPrice() {
            return price;
        }

        public void setPrice(double price) {
            this.price = price;
        }

        public double getTotalPrice() {
            return totalPrice;
        }

        public void setTotalPrice(double totalPrice) {
            this.totalPrice = totalPrice;
        }

        public int getPhamTypeId() {
            return phamTypeId;
        }

        public void setPhamTypeId(int phamTypeId) {
            this.phamTypeId = phamTypeId;
        }

        public String getPerscriptionNo() {
            return perscriptionNo;
        }

        public void setPerscriptionNo(String perscriptionNo) {
            this.perscriptionNo = perscriptionNo;
        }

        public String getPhamOutCode() {
            return phamOutCode;
        }

        public void setPhamOutCode(String phamOutCode) {
            this.phamOutCode = phamOutCode;
        }

        public Object getPhamBrand() {
            return phamBrand;
        }

        public void setPhamBrand(Object phamBrand) {
            this.phamBrand = phamBrand;
        }

        public String getPhamTypeName() {
            return phamTypeName;
        }

        public void setPhamTypeName(String phamTypeName) {
            this.phamTypeName = phamTypeName;
        }

        public static class ParamsBeanX implements Serializable {
        }

        public static class HisPrescriptionTcmDetailsBean implements Serializable {
            /**
             * searchValue : null
             * createBy : null
             * createTime : 2020-12-29 15:39
             * updateBy : null
             * updateTime : null
             * remark : null
             * params : {}
             * id : 139
             * phamId : 5
             * prescriptionDetailId : 111
             * phamName : 丹参
             * phamSpec : null
             * units : g
             * amount : 22.0
             * price : 0.04
             */

            private Object searchValue;
            private Object createBy;
            private String createTime;
            private Object updateBy;
            private Object updateTime;
            private Object remark;
            private ParamsBeanXX params;
            private int id;
            private int phamId;
            private int prescriptionDetailId;
            private String phamName;
            private String phamSpec;
            private String units;
            private int amount;
            private double price;

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

            public ParamsBeanXX getParams() {
                return params;
            }

            public void setParams(ParamsBeanXX params) {
                this.params = params;
            }

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public int getPhamId() {
                return phamId;
            }

            public void setPhamId(int phamId) {
                this.phamId = phamId;
            }

            public int getPrescriptionDetailId() {
                return prescriptionDetailId;
            }

            public void setPrescriptionDetailId(int prescriptionDetailId) {
                this.prescriptionDetailId = prescriptionDetailId;
            }

            public String getPhamName() {
                return phamName;
            }

            public void setPhamName(String phamName) {
                this.phamName = phamName;
            }

            public String getPhamSpec() {
                return phamSpec;
            }

            public void setPhamSpec(String phamSpec) {
                this.phamSpec = phamSpec;
            }

            public String getUnits() {
                return units;
            }

            public void setUnits(String units) {
                this.units = units;
            }

            public int getAmount() {
                return amount;
            }

            public void setAmount(int amount) {
                this.amount = amount;
            }

            public double getPrice() {
                return price;
            }

            public void setPrice(double price) {
                this.price = price;
            }

            public static class ParamsBeanXX implements Serializable {
            }
        }
    }
}
