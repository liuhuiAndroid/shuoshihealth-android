package com.ssh.shuoshi.bean.team;

import java.io.Serializable;
import java.util.List;

/**
 * created by hwt on 2021/1/3
 */
public class DoctorListBean implements Serializable {

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
         * createTime : 2020-12-21 10:34
         * updateBy : null
         * updateTime : 2020-12-21 16:56:06
         * remark : null
         * params : {}
         * id : 31
         * doctorNo : null
         * name : 韩文通
         * phone : 17521362373
         * idCard : null
         * hospitalCode : null
         * practiceCertificateNo : null
         * practiceCertificateTime : null
         * qualificationsCertificateNo : null
         * qualificationsCertificateTime : null
         * titleCertificateNo : null
         * titleCertificateTime : null
         * areaId : 283
         * hospitalName : 医院
         * deptId : 3
         * titleId : 1
         * goodAt : 擅长领域
         * briefIntroduction : 个人简介
         * headImg : null
         * idCardFront : test/2020/12/21/doctor/e767418b-3234-4580-acec-894fd65662c0.png
         * idCardBack : test/2020/12/21/doctor/366faa6a-0176-4a96-a1ff-711865ffd427.png
         * practiceCertificate : test/2020/12/21/doctor/33906042-2cca-44d9-805e-1c95d57a3223.png
         * qualificationsCertificate : test/2020/12/21/doctor/c7f674e5-84e9-470e-9aa6-222331e9ffdd.png
         * titleCertificate : test/2020/12/21/doctor/3b5aa32d-7825-4b54-b623-6ecb5f7f9c1e.png
         * approvalState : 2
         * approvedTime : null
         * approverName : null
         * approvedOpinion : null
         * approverId : null
         * enableFlag : 1
         * recommendFlag : 0
         * evaluationScore : null
         * accountId : null
         * authPhone : null
         * authType : null
         * authState : null
         * desireState : null
         * bizid : null
         * signImg : null
         * areaCNNmae : 长治市
         * areaENName :
         * hisSysDeptName : 中医科
         * doctorTitleName : 医师
         * deptType : 中医
         * deptTypeId : 2
         * approveStateName : null
         * isRecommendName : null
         * enableName : null
         * provinceId : null
         */

        private Object searchValue;
        private Object createBy;
        private String createTime;
        private Object updateBy;
        private String updateTime;
        private Object remark;
        private ParamsBean params;
        private int id;
        private Object doctorNo;
        private String name;
        private String phone;
        private Object idCard;
        private Object hospitalCode;
        private Object practiceCertificateNo;
        private Object practiceCertificateTime;
        private Object qualificationsCertificateNo;
        private Object qualificationsCertificateTime;
        private Object titleCertificateNo;
        private Object titleCertificateTime;
        private int areaId;
        private String hospitalName;
        private int deptId;
        private int titleId;
        private String goodAt;
        private String briefIntroduction;
        private Object headImg;
        private String idCardFront;
        private String idCardBack;
        private String practiceCertificate;
        private String qualificationsCertificate;
        private String titleCertificate;
        private int approvalState;
        private Object approvedTime;
        private Object approverName;
        private Object approvedOpinion;
        private Object approverId;
        private int enableFlag;
        private int recommendFlag;
        private Object evaluationScore;
        private Object accountId;
        private Object authPhone;
        private Object authType;
        private Object authState;
        private Object desireState;
        private Object bizid;
        private Object signImg;
        private String areaCNNmae;
        private String areaENName;
        private String hisSysDeptName;
        private String doctorTitleName;
        private String deptType;
        private int deptTypeId;
        private Object approveStateName;
        private Object isRecommendName;
        private Object enableName;
        private Object provinceId;

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

        public String getUpdateTime() {
            return updateTime;
        }

        public void setUpdateTime(String updateTime) {
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

        public Object getDoctorNo() {
            return doctorNo;
        }

        public void setDoctorNo(Object doctorNo) {
            this.doctorNo = doctorNo;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public Object getIdCard() {
            return idCard;
        }

        public void setIdCard(Object idCard) {
            this.idCard = idCard;
        }

        public Object getHospitalCode() {
            return hospitalCode;
        }

        public void setHospitalCode(Object hospitalCode) {
            this.hospitalCode = hospitalCode;
        }

        public Object getPracticeCertificateNo() {
            return practiceCertificateNo;
        }

        public void setPracticeCertificateNo(Object practiceCertificateNo) {
            this.practiceCertificateNo = practiceCertificateNo;
        }

        public Object getPracticeCertificateTime() {
            return practiceCertificateTime;
        }

        public void setPracticeCertificateTime(Object practiceCertificateTime) {
            this.practiceCertificateTime = practiceCertificateTime;
        }

        public Object getQualificationsCertificateNo() {
            return qualificationsCertificateNo;
        }

        public void setQualificationsCertificateNo(Object qualificationsCertificateNo) {
            this.qualificationsCertificateNo = qualificationsCertificateNo;
        }

        public Object getQualificationsCertificateTime() {
            return qualificationsCertificateTime;
        }

        public void setQualificationsCertificateTime(Object qualificationsCertificateTime) {
            this.qualificationsCertificateTime = qualificationsCertificateTime;
        }

        public Object getTitleCertificateNo() {
            return titleCertificateNo;
        }

        public void setTitleCertificateNo(Object titleCertificateNo) {
            this.titleCertificateNo = titleCertificateNo;
        }

        public Object getTitleCertificateTime() {
            return titleCertificateTime;
        }

        public void setTitleCertificateTime(Object titleCertificateTime) {
            this.titleCertificateTime = titleCertificateTime;
        }

        public int getAreaId() {
            return areaId;
        }

        public void setAreaId(int areaId) {
            this.areaId = areaId;
        }

        public String getHospitalName() {
            return hospitalName;
        }

        public void setHospitalName(String hospitalName) {
            this.hospitalName = hospitalName;
        }

        public int getDeptId() {
            return deptId;
        }

        public void setDeptId(int deptId) {
            this.deptId = deptId;
        }

        public int getTitleId() {
            return titleId;
        }

        public void setTitleId(int titleId) {
            this.titleId = titleId;
        }

        public String getGoodAt() {
            return goodAt;
        }

        public void setGoodAt(String goodAt) {
            this.goodAt = goodAt;
        }

        public String getBriefIntroduction() {
            return briefIntroduction;
        }

        public void setBriefIntroduction(String briefIntroduction) {
            this.briefIntroduction = briefIntroduction;
        }

        public Object getHeadImg() {
            return headImg;
        }

        public void setHeadImg(Object headImg) {
            this.headImg = headImg;
        }

        public String getIdCardFront() {
            return idCardFront;
        }

        public void setIdCardFront(String idCardFront) {
            this.idCardFront = idCardFront;
        }

        public String getIdCardBack() {
            return idCardBack;
        }

        public void setIdCardBack(String idCardBack) {
            this.idCardBack = idCardBack;
        }

        public String getPracticeCertificate() {
            return practiceCertificate;
        }

        public void setPracticeCertificate(String practiceCertificate) {
            this.practiceCertificate = practiceCertificate;
        }

        public String getQualificationsCertificate() {
            return qualificationsCertificate;
        }

        public void setQualificationsCertificate(String qualificationsCertificate) {
            this.qualificationsCertificate = qualificationsCertificate;
        }

        public String getTitleCertificate() {
            return titleCertificate;
        }

        public void setTitleCertificate(String titleCertificate) {
            this.titleCertificate = titleCertificate;
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

        public Object getApproverName() {
            return approverName;
        }

        public void setApproverName(Object approverName) {
            this.approverName = approverName;
        }

        public Object getApprovedOpinion() {
            return approvedOpinion;
        }

        public void setApprovedOpinion(Object approvedOpinion) {
            this.approvedOpinion = approvedOpinion;
        }

        public Object getApproverId() {
            return approverId;
        }

        public void setApproverId(Object approverId) {
            this.approverId = approverId;
        }

        public int getEnableFlag() {
            return enableFlag;
        }

        public void setEnableFlag(int enableFlag) {
            this.enableFlag = enableFlag;
        }

        public int getRecommendFlag() {
            return recommendFlag;
        }

        public void setRecommendFlag(int recommendFlag) {
            this.recommendFlag = recommendFlag;
        }

        public Object getEvaluationScore() {
            return evaluationScore;
        }

        public void setEvaluationScore(Object evaluationScore) {
            this.evaluationScore = evaluationScore;
        }

        public Object getAccountId() {
            return accountId;
        }

        public void setAccountId(Object accountId) {
            this.accountId = accountId;
        }

        public Object getAuthPhone() {
            return authPhone;
        }

        public void setAuthPhone(Object authPhone) {
            this.authPhone = authPhone;
        }

        public Object getAuthType() {
            return authType;
        }

        public void setAuthType(Object authType) {
            this.authType = authType;
        }

        public Object getAuthState() {
            return authState;
        }

        public void setAuthState(Object authState) {
            this.authState = authState;
        }

        public Object getDesireState() {
            return desireState;
        }

        public void setDesireState(Object desireState) {
            this.desireState = desireState;
        }

        public Object getBizid() {
            return bizid;
        }

        public void setBizid(Object bizid) {
            this.bizid = bizid;
        }

        public Object getSignImg() {
            return signImg;
        }

        public void setSignImg(Object signImg) {
            this.signImg = signImg;
        }

        public String getAreaCNNmae() {
            return areaCNNmae;
        }

        public void setAreaCNNmae(String areaCNNmae) {
            this.areaCNNmae = areaCNNmae;
        }

        public String getAreaENName() {
            return areaENName;
        }

        public void setAreaENName(String areaENName) {
            this.areaENName = areaENName;
        }

        public String getHisSysDeptName() {
            return hisSysDeptName;
        }

        public void setHisSysDeptName(String hisSysDeptName) {
            this.hisSysDeptName = hisSysDeptName;
        }

        public String getDoctorTitleName() {
            return doctorTitleName;
        }

        public void setDoctorTitleName(String doctorTitleName) {
            this.doctorTitleName = doctorTitleName;
        }

        public String getDeptType() {
            return deptType;
        }

        public void setDeptType(String deptType) {
            this.deptType = deptType;
        }

        public int getDeptTypeId() {
            return deptTypeId;
        }

        public void setDeptTypeId(int deptTypeId) {
            this.deptTypeId = deptTypeId;
        }

        public Object getApproveStateName() {
            return approveStateName;
        }

        public void setApproveStateName(Object approveStateName) {
            this.approveStateName = approveStateName;
        }

        public Object getIsRecommendName() {
            return isRecommendName;
        }

        public void setIsRecommendName(Object isRecommendName) {
            this.isRecommendName = isRecommendName;
        }

        public Object getEnableName() {
            return enableName;
        }

        public void setEnableName(Object enableName) {
            this.enableName = enableName;
        }

        public Object getProvinceId() {
            return provinceId;
        }

        public void setProvinceId(Object provinceId) {
            this.provinceId = provinceId;
        }

        public static class ParamsBean implements Serializable {
        }
    }
}
