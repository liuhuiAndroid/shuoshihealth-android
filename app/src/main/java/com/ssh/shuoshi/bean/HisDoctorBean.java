package com.ssh.shuoshi.bean;

/**
 * 医生基本信息
 * created by hwt on 2020/12/12
 */
public class HisDoctorBean {

    private Boolean newFlag;
    private Object searchValue;
    private Object createBy;
    private String createTime;
    private Object updateBy;
    private String updateTime;
    private Object remark;
    private ParamsBean params;
    private int id;
    private String name;
    private String phone;
    private String password;
    private int areaId;
    private String hospitalName;
    private int deptId;
    private int titleId;
    private String goodAt;
    private String briefIntroduction;
    private String headImg;
    private String idCardFront;
    private String idCardBack;
    private String practiceCertificate;
    private String qualificationsCertificate;
    private String titleCertificate;
    private int approvalState;
    private String approvedTime;
    private Object approverName;
    private Object approvedOpinion;
    private Object approverId;
    private int enableFlag;
    private int recommendFlag;
    private String areaCNNmae;
    private String areaENName;
    private String hisSysDeptName;
    private String doctorTitleName;
    private String deptType;
    private Object approveStateName;
    private Object commitApproveTime;
    private Object isRecommendName;
    private Object enableName;
    private String praiseRate;
    private int patientCount;

    private String doctorNo;
    private String idCard;
    private Object hospitalCode;
    private Object practiceCertificateNo;
    private Object practiceCertificateTime;
    private Object qualificationsCertificateNo;
    private Object qualificationsCertificateTime;
    private Object titleCertificateNo;
    private Object titleCertificateTime;
    private Object evaluationScore;
    private String accountId;
    private String authPhone;
    //1、运营商3要素，2、人脸识别
    private Integer authType;
    //认证状态，0未认证，1已认证
    private Integer authState;
    //0非意愿，1意愿
    private Integer desireState;
    private String bizid;
    private String signImg;
    private Integer deptTypeId;
    private Integer provinceId;

    public Boolean getNewFlag() {
        return newFlag;
    }

    public void setNewFlag(Boolean newFlag) {
        this.newFlag = newFlag;
    }

    public String getDoctorNo() {
        return doctorNo;
    }

    public void setDoctorNo(String doctorNo) {
        this.doctorNo = doctorNo;
    }

    public String getIdCard() {
        return idCard;
    }

    public void setIdCard(String idCard) {
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

    public Object getEvaluationScore() {
        return evaluationScore;
    }

    public void setEvaluationScore(Object evaluationScore) {
        this.evaluationScore = evaluationScore;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public String getAuthPhone() {
        return authPhone;
    }

    public void setAuthPhone(String authPhone) {
        this.authPhone = authPhone;
    }

    public Integer getAuthType() {
        return authType;
    }

    public void setAuthType(Integer authType) {
        this.authType = authType;
    }

    public Integer getAuthState() {
        return authState;
    }

    public void setAuthState(Integer authState) {
        this.authState = authState;
    }

    public Integer getDesireState() {
        return desireState;
    }

    public void setDesireState(Integer desireState) {
        this.desireState = desireState;
    }

    public String getBizid() {
        return bizid;
    }

    public void setBizid(String bizid) {
        this.bizid = bizid;
    }

    public String getSignImg() {
        return signImg;
    }

    public void setSignImg(String signImg) {
        this.signImg = signImg;
    }

    public Integer getDeptTypeId() {
        return deptTypeId;
    }

    public void setDeptTypeId(Integer deptTypeId) {
        this.deptTypeId = deptTypeId;
    }

    public Integer getProvinceId() {
        return provinceId;
    }

    public void setProvinceId(Integer provinceId) {
        this.provinceId = provinceId;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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

    public String getHeadImg() {
        return headImg;
    }

    public void setHeadImg(String headImg) {
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

    public String getApprovedTime() {
        return approvedTime;
    }

    public void setApprovedTime(String approvedTime) {
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

    public Object getApproveStateName() {
        return approveStateName;
    }

    public void setApproveStateName(Object approveStateName) {
        this.approveStateName = approveStateName;
    }

    public Object getCommitApproveTime() {
        return commitApproveTime;
    }

    public void setCommitApproveTime(Object commitApproveTime) {
        this.commitApproveTime = commitApproveTime;
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

    public String getPraiseRate() {
        return praiseRate;
    }

    public void setPraiseRate(String praiseRate) {
        this.praiseRate = praiseRate;
    }

    public int getPatientCount() {
        return patientCount;
    }

    public void setPatientCount(int patientCount) {
        this.patientCount = patientCount;
    }

    public static class ParamsBean {
    }
}
