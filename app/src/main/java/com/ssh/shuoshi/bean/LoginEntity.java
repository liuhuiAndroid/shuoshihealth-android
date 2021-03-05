package com.ssh.shuoshi.bean;

/**
 * created by hwt on 2020/12/8
 */
public class LoginEntity {
    private long validTime;
    private String token;
    private HisDoctorBean hisDoctor;

    public HisDoctorBean getDoctorBean() {
        return hisDoctor;
    }

    public void setDoctorBean(HisDoctorBean hisDoctor) {
        this.hisDoctor = hisDoctor;
    }

    public long getValidTime() {
        return validTime;
    }

    public void setValidTime(long validTime) {
        this.validTime = validTime;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
