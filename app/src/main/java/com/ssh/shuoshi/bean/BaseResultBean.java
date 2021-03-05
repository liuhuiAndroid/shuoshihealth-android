package com.ssh.shuoshi.bean;

import com.google.gson.annotations.SerializedName;
import com.ssh.shuoshi.Constants;

/**
 * created by hwt on 2020/12/8
 */
public class BaseResultBean<T> {
    private ResultStatusBean resultStatus;

    private @SerializedName("resultValue")
    T resultValue;

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
