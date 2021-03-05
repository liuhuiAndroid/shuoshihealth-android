package com.ssh.shuoshi.bean.team;

import java.util.List;

/**
 * created by hwt on 2021/1/4
 */
public class DoctorTeamDetailBean {

    private Object searchValue;
    private Object createBy;
    private String createTime;
    private Object updateBy;
    private Object updateTime;
    private Object remark;
    private ParamsBean params;
    private int id;
    private String name;
    private String introduction;
    private Number price;
    private Integer enableFlag;
    private Object consultationTypeName;
    private List<HisDoctorExpertTeamMemberDtosBean> hisDoctorExpertTeamMemberDtos;

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    public Number getPrice() {
        return price;
    }

    public void setPrice(Number price) {
        this.price = price;
    }

    public Integer getEnableFlag() {
        return enableFlag;
    }

    public void setEnableFlag(Integer enableFlag) {
        this.enableFlag = enableFlag;
    }

    public Object getConsultationTypeName() {
        return consultationTypeName;
    }

    public void setConsultationTypeName(Object consultationTypeName) {
        this.consultationTypeName = consultationTypeName;
    }

    public List<HisDoctorExpertTeamMemberDtosBean> getHisDoctorExpertTeamMemberDtos() {
        return hisDoctorExpertTeamMemberDtos;
    }

    public void setHisDoctorExpertTeamMemberDtos(List<HisDoctorExpertTeamMemberDtosBean> hisDoctorExpertTeamMemberDtos) {
        this.hisDoctorExpertTeamMemberDtos = hisDoctorExpertTeamMemberDtos;
    }

    public static class ParamsBean {
    }

}
