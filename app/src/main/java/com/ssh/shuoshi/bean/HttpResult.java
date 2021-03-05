package com.ssh.shuoshi.bean;

import com.google.gson.annotations.SerializedName;
import com.ssh.shuoshi.Constants;

/**
 * created by hwt on 2020/12/8
 *
 *  {"code":0,"resultStatus":{"code":200,"message":"操作成功"}}
 */
public class HttpResult<T> {

    private ResultStatusBean resultStatus;

    private @SerializedName("resultValue")
    T resultValue;
    /**
     * msg : null
     * code : 200
     * data : 1e1b4bf4a3b24da1b39c7a1eab5c7ad3
     */

    private Object msg;
    private int code;
    private String data;

    public Object getMsg() {
        return msg;
    }

    public void setMsg(Object msg) {
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public static class ResultStatusBean {
        private int code;
        private String message;

        public int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }

    public boolean isSuccess() {
        return resultStatus.getCode() == Constants.OK;
    }

    public ResultStatusBean getResultStatus() {
        return resultStatus;
    }

    public T getResultValue() {
        return resultValue;
    }
}
