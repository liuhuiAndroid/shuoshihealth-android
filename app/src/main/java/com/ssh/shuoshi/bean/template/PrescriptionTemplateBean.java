package com.ssh.shuoshi.bean.template;

import java.io.Serializable;
import java.util.List;

/**
 * 西药，中药，模版实体类
 * created by hwt on 2020/12/22
 */
public class PrescriptionTemplateBean implements Serializable {

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
         * createTime : 2020-12-24 17:00
         * updateBy : null
         * updateTime : null
         * remark : null
         * params : {}
         * id : 13
         * doctorId : 34
         * perscriptionTypeId : 2
         * templateName : 好的
         * perscriptionTypeName : null
         * hisPrescriptionTemplateDetailDtos : [{"searchValue":null,"createBy":null,"createTime":"2020-12-24 17:00","updateBy":null,"updateTime":null,"remark":null,"params":{},"id":18,"templateId":13,"phamId":null,"phamName":"颗粒剂","phamSpec":null,"units":"副","amount":null,"dosage":null,"dosageUnits":"副","frequencyId":null,"administration":null,"frequencyName":null,"hisPrescriptionTemplateTcmDetails":[{"searchValue":null,"createBy":null,"createTime":"2020-12-24 17:00","updateBy":null,"updateTime":null,"remark":null,"params":{},"id":1,"templateDetailId":18,"phamId":9,"phamName":"999感冒灵","phamSpec":null,"units":"g","amount":1},{"searchValue":null,"createBy":null,"createTime":"2020-12-24 17:00","updateBy":null,"updateTime":null,"remark":null,"params":{},"id":2,"templateDetailId":18,"phamId":5,"phamName":"丹参","phamSpec":null,"units":"g","amount":2},{"searchValue":null,"createBy":null,"createTime":"2020-12-24 17:00","updateBy":null,"updateTime":null,"remark":null,"params":{},"id":3,"templateDetailId":18,"phamId":6,"phamName":"炙甘草","phamSpec":null,"units":"g","amount":3},{"searchValue":null,"createBy":null,"createTime":"2020-12-24 17:00","updateBy":null,"updateTime":null,"remark":null,"params":{},"id":4,"templateDetailId":18,"phamId":3,"phamName":"桂枝","phamSpec":null,"units":"g","amount":4},{"searchValue":null,"createBy":null,"createTime":"2020-12-24 17:03","updateBy":null,"updateTime":null,"remark":null,"params":{},"id":5,"templateDetailId":19,"phamId":9,"phamName":"999感冒灵","phamSpec":null,"units":"g","amount":1},{"searchValue":null,"createBy":null,"createTime":"2020-12-24 17:03","updateBy":null,"updateTime":null,"remark":null,"params":{},"id":6,"templateDetailId":19,"phamId":5,"phamName":"丹参","phamSpec":null,"units":"g","amount":2},{"searchValue":null,"createBy":null,"createTime":"2020-12-24 17:03","updateBy":null,"updateTime":null,"remark":null,"params":{},"id":7,"templateDetailId":19,"phamId":6,"phamName":"炙甘草","phamSpec":null,"units":"g","amount":3},{"searchValue":null,"createBy":null,"createTime":"2020-12-24 17:03","updateBy":null,"updateTime":null,"remark":null,"params":{},"id":8,"templateDetailId":19,"phamId":3,"phamName":"桂枝","phamSpec":null,"units":"g","amount":14},{"searchValue":null,"createBy":null,"createTime":"2020-12-24 17:03","updateBy":null,"updateTime":null,"remark":null,"params":{},"id":9,"templateDetailId":19,"phamId":9,"phamName":"999感冒灵","phamSpec":null,"units":"g","amount":19},{"searchValue":null,"createBy":null,"createTime":"2020-12-24 17:03","updateBy":null,"updateTime":null,"remark":null,"params":{},"id":10,"templateDetailId":19,"phamId":9,"phamName":"999感冒灵","phamSpec":null,"units":"g","amount":19},{"searchValue":null,"createBy":null,"createTime":"2020-12-24 17:03","updateBy":null,"updateTime":null,"remark":null,"params":{},"id":11,"templateDetailId":19,"phamId":9,"phamName":"999感冒灵","phamSpec":null,"units":"g","amount":19}]}]
         */

        private Object searchValue;
        private Object createBy;
        private String createTime;
        private Object updateBy;
        private Object updateTime;
        private Object remark;
        private RowsBean.ParamsBean params;
        private int id;
        private int doctorId;
        private int perscriptionTypeId;
        private String templateName;
        private Object perscriptionTypeName;
        private List<HisPrescriptionTemplateDetailDtosBean> hisPrescriptionTemplateDetailDtos;

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

        public RowsBean.ParamsBean getParams() {
            return params;
        }

        public void setParams(RowsBean.ParamsBean params) {
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

        public int getPerscriptionTypeId() {
            return perscriptionTypeId;
        }

        public void setPerscriptionTypeId(int perscriptionTypeId) {
            this.perscriptionTypeId = perscriptionTypeId;
        }

        public String getTemplateName() {
            return templateName;
        }

        public void setTemplateName(String templateName) {
            this.templateName = templateName;
        }

        public Object getPerscriptionTypeName() {
            return perscriptionTypeName;
        }

        public void setPerscriptionTypeName(Object perscriptionTypeName) {
            this.perscriptionTypeName = perscriptionTypeName;
        }

        public List<HisPrescriptionTemplateDetailDtosBean> getHisPrescriptionTemplateDetailDtos() {
            return hisPrescriptionTemplateDetailDtos;
        }

        public void setHisPrescriptionTemplateDetailDtos(List<HisPrescriptionTemplateDetailDtosBean> hisPrescriptionTemplateDetailDtos) {
            this.hisPrescriptionTemplateDetailDtos = hisPrescriptionTemplateDetailDtos;
        }

        public static class ParamsBean implements Serializable {
        }
    }
}
