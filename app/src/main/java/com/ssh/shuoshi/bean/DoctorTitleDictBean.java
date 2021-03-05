package com.ssh.shuoshi.bean;

import java.util.List;

/**
 * created by hwt on 2020/12/14
 */
public class DoctorTitleDictBean {


    public static class ResultStatusBean {
        /**
         * code : 200
         * message : 查询成功
         */

        private int code;
        private String message;

        public int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }

    public static class ResultValueBean {
        /**
         * rows : [{"createTime":"2020-12-09 20:20:08","deptTypeId":0,"enableFlag":1,"hospitalId":0,"id":1,"operator":"admin","params":{},"parentId":0,"serialNo":1,"titleName":"主任医师"},{"createTime":"2020-12-09 20:20:26","deptTypeId":0,"enableFlag":1,"hospitalId":0,"id":2,"operator":"admin","params":{},"parentId":0,"serialNo":2,"titleName":"副主任医师"},{"createTime":"2020-12-09 20:20:44","deptTypeId":0,"enableFlag":1,"hospitalId":0,"id":3,"operator":"admin","params":{},"parentId":0,"serialNo":3,"titleName":"主治医师"},{"createTime":"2020-12-09 20:21:00","deptTypeId":0,"enableFlag":1,"hospitalId":0,"id":4,"operator":"admin","params":{},"parentId":0,"serialNo":4,"titleName":"住院医师"},{"createTime":"2020-12-09 20:21:19","deptTypeId":0,"enableFlag":1,"hospitalId":0,"id":5,"operator":"admin","params":{},"parentId":0,"serialNo":5,"titleName":"医师"},{"createTime":"2020-12-09 20:21:46","deptTypeId":0,"enableFlag":1,"hospitalId":0,"id":6,"operator":"admin","params":{},"parentId":0,"serialNo":6,"titleName":"医士"}]
         * total : 6
         * totalPage : 1
         */

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

        public static class RowsBean {
            /**
             * createTime : 2020-12-09 20:20:08
             * deptTypeId : 0
             * enableFlag : 1
             * hospitalId : 0
             * id : 1
             * operator : admin
             * params : {}
             * parentId : 0
             * serialNo : 1
             * titleName : 主任医师
             */

            private String createTime;
            private int deptTypeId;
            private int enableFlag;
            private int hospitalId;
            private int id;
            private String operator;
            private ParamsBean params;
            private int parentId;
            private int serialNo;
            private String titleName;

            public String getCreateTime() {
                return createTime;
            }

            public void setCreateTime(String createTime) {
                this.createTime = createTime;
            }

            public int getDeptTypeId() {
                return deptTypeId;
            }

            public void setDeptTypeId(int deptTypeId) {
                this.deptTypeId = deptTypeId;
            }

            public int getEnableFlag() {
                return enableFlag;
            }

            public void setEnableFlag(int enableFlag) {
                this.enableFlag = enableFlag;
            }

            public int getHospitalId() {
                return hospitalId;
            }

            public void setHospitalId(int hospitalId) {
                this.hospitalId = hospitalId;
            }

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getOperator() {
                return operator;
            }

            public void setOperator(String operator) {
                this.operator = operator;
            }

            public ParamsBean getParams() {
                return params;
            }

            public void setParams(ParamsBean params) {
                this.params = params;
            }

            public int getParentId() {
                return parentId;
            }

            public void setParentId(int parentId) {
                this.parentId = parentId;
            }

            public int getSerialNo() {
                return serialNo;
            }

            public void setSerialNo(int serialNo) {
                this.serialNo = serialNo;
            }

            public String getTitleName() {
                return titleName;
            }

            public void setTitleName(String titleName) {
                this.titleName = titleName;
            }

            public static class ParamsBean {
            }
        }
    }
}
