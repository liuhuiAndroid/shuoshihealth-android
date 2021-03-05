package com.ssh.shuoshi.bean;

import com.ssh.shuoshi.Constants;

public class BaseMsg {

    private String msg;
    private int code;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public boolean isSuccess() {
        return code == Constants.OK;
    }

    @Override
    public String toString() {
        return "BaseMsg{" +
                "code=" + code +
                ", msg='" + msg + '\'' +
                '}';
    }
}
