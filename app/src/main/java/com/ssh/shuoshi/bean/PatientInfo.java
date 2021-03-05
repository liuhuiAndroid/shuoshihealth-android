package com.ssh.shuoshi.bean;

public class PatientInfo {

    private String name;
    private int age;
    private String sex;
    private int status;
    private String desc;
    private String imageUrl;
    private String time;

    public PatientInfo(String name, int age, String sex, int status, String desc, String imageUrl, String time) {
        this.name = name;
        this.age = age;
        this.sex = sex;
        this.status = status;
        this.desc = desc;
        this.imageUrl = imageUrl;
        this.time = time;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
