package com.ssh.shuoshi.bean.team;

import java.io.Serializable;
import java.util.List;

/**
 * created by hwt on 2021/1/3
 */
public class DoctorTeamBean implements Serializable {

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
         * createTime : 2021-01-03 22:59
         * updateBy : null
         * updateTime : null
         * remark : null
         * params : {}
         * id : 45
         * expertTeamId : 29
         * doctorId : 61
         * level : 1
         * operator : null
         * doctorExpertTeamName : 12345678
         * doctorName : dtb
         * hospitalName : 女子监狱风云
         * titleName : 主任医师
         * deptName : 中西医结合科
         * doctorNo : IM210102134306007
         * levelName : 领衔专家
         * teamCount : 3
         */

        private Object searchValue;
        private Object createBy;
        private String createTime;
        private Object updateBy;
        private Object updateTime;
        private Object remark;
        private ParamsBean params;
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
        private int teamCount;

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

        public int getTeamCount() {
            return teamCount;
        }

        public void setTeamCount(int teamCount) {
            this.teamCount = teamCount;
        }

        public static class ParamsBean implements Serializable {
        }
    }
}
