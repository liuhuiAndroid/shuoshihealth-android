package com.ssh.shuoshi.bean;

/**
 * created by hwt on 2020/12/11
 */
public class LoginInfoBean {

    private HisDoctorBean hisDoctor;
    private String token;

    public HisDoctorBean getHisDoctor() {
        return hisDoctor;
    }

    public void setHisDoctor(HisDoctorBean hisDoctor) {
        this.hisDoctor = hisDoctor;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

}
