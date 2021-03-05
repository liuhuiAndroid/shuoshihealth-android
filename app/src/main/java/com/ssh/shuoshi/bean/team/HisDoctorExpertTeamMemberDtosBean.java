package com.ssh.shuoshi.bean.team;

import java.io.Serializable;

/**
 * 专家团队成员
 * created by hwt on 2021/1/6
 */
public class HisDoctorExpertTeamMemberDtosBean implements Serializable {
    /**
     * searchValue : null
     * createBy : null
     * createTime : 2021-01-04 10:02
     * updateBy : null
     * updateTime : null
     * remark : null
     * params : {}
     * id : 46
     * expertTeamId : 30
     * doctorId : 57
     * level : 2
     * operator : null
     * doctorExpertTeamName : 王者荣耀黄忠
     * doctorName : 刘备
     * hospitalName : 2222
     * titleName : 主任医师
     * deptName : 中医科
     * doctorNo : IM201231211036051
     * levelName : 普通成员
     * teamCount : null
     */

    private Object searchValue;
    private Object createBy;
    private String createTime;
    private Object updateBy;
    private Object updateTime;
    private Object remark;
    private ParamsBeanX params;
    private int id;
    private int expertTeamId;
    private int doctorId;
    private int level;
    private Object operator;
    private String doctorExpertTeamName;
    private String doctorName;
    private String hospitalName;
    private String titleName;
    private String deptName;
    private String doctorNo;
    private String levelName;
    private Object teamCount;
    private String doctorHeadImg;
    private String doctorBaseImg;

    public String getDoctorBaseImg() {
        return doctorBaseImg;
    }

    public void setDoctorBaseImg(String doctorBaseImg) {
        this.doctorBaseImg = doctorBaseImg;
    }

    public String getDoctorHeadImg() {
        return doctorHeadImg;
    }

    public void setDoctorHeadImg(String doctorHeadImg) {
        this.doctorHeadImg = doctorHeadImg;
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

    public int getExpertTeamId() {
        return expertTeamId;
    }

    public void setExpertTeamId(int expertTeamId) {
        this.expertTeamId = expertTeamId;
    }

    public int getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(int doctorId) {
        this.doctorId = doctorId;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public Object getOperator() {
        return operator;
    }

    public void setOperator(Object operator) {
        this.operator = operator;
    }

    public String getDoctorExpertTeamName() {
        return doctorExpertTeamName;
    }

    public void setDoctorExpertTeamName(String doctorExpertTeamName) {
        this.doctorExpertTeamName = doctorExpertTeamName;
    }

    public String getDoctorName() {
        return doctorName;
    }

    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
    }

    public String getHospitalName() {
        return hospitalName;
    }

    public void setHospitalName(String hospitalName) {
        this.hospitalName = hospitalName;
    }

    public String getTitleName() {
        return titleName;
    }

    public void setTitleName(String titleName) {
        this.titleName = titleName;
    }

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    public String getDoctorNo() {
        return doctorNo;
    }

    public void setDoctorNo(String doctorNo) {
        this.doctorNo = doctorNo;
    }

    public String getLevelName() {
        return levelName;
    }

    public void setLevelName(String levelName) {
        this.levelName = levelName;
    }

    public Object getTeamCount() {
        return teamCount;
    }

    public void setTeamCount(Object teamCount) {
        this.teamCount = teamCount;
    }

    public static class ParamsBeanX implements Serializable {
    }
}
