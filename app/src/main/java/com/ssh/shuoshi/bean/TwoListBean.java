package com.ssh.shuoshi.bean;

import java.util.List;

/**
 * created by hwt on 2020/12/11
 */
public class TwoListBean {

    private String date;
    private String week;

    private List<Detail> list;

    public static class Detail {
        private String time;

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getWeek() {
        return week;
    }

    public void setWeek(String week) {
        this.week = week;
    }

    public List<Detail> getList() {
        return list;
    }

    public void setList(List<Detail> list) {
        this.list = list;
    }
}
