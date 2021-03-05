package com.ssh.shuoshi.bean;

import com.ssh.shuoshi.view.wheelview.interfaces.IPickerViewData;

import java.util.List;

public class TimeBean implements IPickerViewData {

    private String year;

    public TimeBean(String year) {
        this.year = year;
    }

    private List<MonthBean> months;

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public List<MonthBean> getMonths() {
        return months;
    }

    public void setMonths(List<MonthBean> months) {
        this.months = months;
    }

    @Override
    public String getPickerViewText() {
        return year;
    }

    public static class MonthBean {

        private String month;

        public MonthBean(String month) {
            this.month = month;
        }

        public String getMonth() {
            return month;
        }

        public void setMonth(String month) {
            this.month = month;
        }
    }
}
