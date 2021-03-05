package com.ssh.shuoshi.bean;

import java.util.List;

/**
 * created by hwt on 2020/12/15
 */
public class DoctorWeekScheduleBean {

    private String date;
    private String dayOfWeek;
    private List<HisDoctorSchedulesBean> hisDoctorSchedules;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDayOfWeek() {
        return dayOfWeek;
    }

    public void setDayOfWeek(String dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    public List<HisDoctorSchedulesBean> getHisDoctorSchedules() {
        return hisDoctorSchedules;
    }

    public void setHisDoctorSchedules(List<HisDoctorSchedulesBean> hisDoctorSchedules) {
        this.hisDoctorSchedules = hisDoctorSchedules;
    }

    public static class HisDoctorSchedulesBean {
        /**
         * searchValue : null
         * createBy : null
         * createTime : 2020-12-16 16:02:17
         * updateBy : null
         * updateTime : null
         * remark : null
         * params : {}
         * id : 5
         * doctorId : 19
         * deptId : 1
         * dayOfWeek : 3
         * workDate : 2020-12-15
         * startTime : 01:03
         * endTime : 04:00
         * doctorName : null
         * deptName : null
         */

        private Object searchValue;
        private Object createBy;
        private String createTime;
        private Object updateBy;
        private Object updateTime;
        private Object remark;
        private ParamsBean params;
        private int id;
        private int doctorId;
        private int deptId;
        private int dayOfWeek;
        private String workDate;
        private String startTime;
        private String endTime;
        private int appointmentNum;
        private String doctorName;
        private String deptName;

        public int getAppointmentNum() {
            return appointmentNum;
        }

        public void setAppointmentNum(int appointmentNum) {
            this.appointmentNum = appointmentNum;
        }

        public String getDoctorName() {
            return doctorName;
        }

        public void setDoctorName(String doctorName) {
            this.doctorName = doctorName;
        }

        public String getDeptName() {
            return deptName;
        }

        public void setDeptName(String deptName) {
            this.deptName = deptName;
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

        public int getDoctorId() {
            return doctorId;
        }

        public void setDoctorId(int doctorId) {
            this.doctorId = doctorId;
        }

        public int getDeptId() {
            return deptId;
        }

        public void setDeptId(int deptId) {
            this.deptId = deptId;
        }

        public int getDayOfWeek() {
            return dayOfWeek;
        }

        public void setDayOfWeek(int dayOfWeek) {
            this.dayOfWeek = dayOfWeek;
        }

        public String getWorkDate() {
            return workDate;
        }

        public void setWorkDate(String workDate) {
            this.workDate = workDate;
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

        public static class ParamsBean {
        }
    }
}
