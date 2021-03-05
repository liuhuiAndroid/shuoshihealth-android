package com.ssh.shuoshi.bean;

public class CityBean {

    private AdditionalProperties1Bean additionalProperties1;

    public AdditionalProperties1Bean getAdditionalProperties1() {
        return additionalProperties1;
    }

    public void setAdditionalProperties1(AdditionalProperties1Bean additionalProperties1) {
        this.additionalProperties1 = additionalProperties1;
    }

    public static class AdditionalProperties1Bean {
        /**
         * cityCode :
         * cityLevel : 0
         * cityNameEn :
         * cityNameZh :
         * createBy :
         * createTime :
         * id : 0
         * params : {}
         * parentId : 0
         * remark :
         * searchValue :
         * updateBy :
         * updateTime :
         */

        private String cityCode;
        private int cityLevel;
        private String cityNameEn;
        private String cityNameZh;
        private String createBy;
        private String createTime;
        private int id;
        private ParamsBean params;
        private int parentId;
        private String remark;
        private String searchValue;
        private String updateBy;
        private String updateTime;

        public String getCityCode() {
            return cityCode;
        }

        public void setCityCode(String cityCode) {
            this.cityCode = cityCode;
        }

        public int getCityLevel() {
            return cityLevel;
        }

        public void setCityLevel(int cityLevel) {
            this.cityLevel = cityLevel;
        }

        public String getCityNameEn() {
            return cityNameEn;
        }

        public void setCityNameEn(String cityNameEn) {
            this.cityNameEn = cityNameEn;
        }

        public String getCityNameZh() {
            return cityNameZh;
        }

        public void setCityNameZh(String cityNameZh) {
            this.cityNameZh = cityNameZh;
        }

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

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
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

        public static class ParamsBean {
        }
    }
}
