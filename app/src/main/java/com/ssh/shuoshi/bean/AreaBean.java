package com.ssh.shuoshi.bean;

import com.ssh.shuoshi.view.wheelview.interfaces.IPickerViewData;

import java.util.List;

/**
 * created by hwt on 2020/12/15
 */
public class AreaBean implements IPickerViewData {

    private String name;
    private List<CityBean> city;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<CityBean> getCity() {
        return city;
    }

    public void setCity(List<CityBean> city) {
        this.city = city;
    }

    @Override
    public String getPickerViewText() {
        return this.name;
    }

    public static class CityBean {
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
