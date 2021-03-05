package com.ssh.shuoshi.bean;

import java.io.Serializable;

/**
 * created by hwt on 2020/12/16
 */
public class Week implements Serializable {
    /**
     * 星期一
     */
    private String monday;
    /**
     * 星期二
     */
    private String tuesday;
    /**
     * 星期三
     */
    private String wednesday;
    /**
     * 星期四
     */
    private String thursday;
    /**
     * 星期五
     */
    private String friday;
    /**
     * 星期六
     */
    private String saturday;
    /**
     * 星期日
     */
    private String sunday;

    public Week() {
        super();
    }

    public Week(String monday, String tuesday, String wednesday, String thursday, String friday, String saturday,
                String sunday) {
        super();
        this.monday = monday;
        this.tuesday = tuesday;
        this.wednesday = wednesday;
        this.thursday = thursday;
        this.friday = friday;
        this.saturday = saturday;
        this.sunday = sunday;
    }

    public String getMonday() {
        return monday;
    }

    public void setMonday(String monday) {
        this.monday = monday;
    }

    public String getTuesday() {
        return tuesday;
    }

    public void setTuesday(String tuesday) {
        this.tuesday = tuesday;
    }

    public String getWednesday() {
        return wednesday;
    }

    public void setWednesday(String wednesday) {
        this.wednesday = wednesday;
    }

    public String getThursday() {
        return thursday;
    }

    public void setThursday(String thursday) {
        this.thursday = thursday;
    }

    public String getFriday() {
        return friday;
    }

    public void setFriday(String friday) {
        this.friday = friday;
    }

    public String getSaturday() {
        return saturday;
    }

    public void setSaturday(String saturday) {
        this.saturday = saturday;
    }

    public String getSunday() {
        return sunday;
    }

    public void setSunday(String sunday) {
        this.sunday = sunday;
    }

}
