package com.ssh.shuoshi.bean.ca;

/**
 * created by hwt on 2020/12/26
 */
public class CAPhoneBean {

    private int status;
    private String code;
    private String message;
    private String body;
    private DataBean data;
    private int errCode;
    private boolean errShow;
    private String msg;
    private String accountId;
    private Object fingerprint;

    private int prescriptionId;
    private String mobileNo;
    private String authcode;

    public int getPrescriptionId() {
        return prescriptionId;
    }

    public void setPrescriptionId(int prescriptionId) {
        this.prescriptionId = prescriptionId;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    public String getAuthcode() {
        return authcode;
    }

    public void setAuthcode(String authcode) {
        this.authcode = authcode;
    }

    public int getErrCode() {
        return errCode;
    }

    public void setErrCode(int errCode) {
        this.errCode = errCode;
    }

    public boolean isErrShow() {
        return errShow;
    }

    public void setErrShow(boolean errShow) {
        this.errShow = errShow;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public Object getFingerprint() {
        return fingerprint;
    }

    public void setFingerprint(Object fingerprint) {
        this.fingerprint = fingerprint;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * flowId : 1580955480468037218
         */

        private String flowId;

        private String authUrl;
        private String originalUrl;
        private long expire;

        private String url;
        private String shortUrl;
        private String bizId;

        private String status;
        private String message;
        private Object similarity;
        private Object livingScore;

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public Object getSimilarity() {
            return similarity;
        }

        public void setSimilarity(Object similarity) {
            this.similarity = similarity;
        }

        public Object getLivingScore() {
            return livingScore;
        }

        public void setLivingScore(Object livingScore) {
            this.livingScore = livingScore;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getShortUrl() {
            return shortUrl;
        }

        public void setShortUrl(String shortUrl) {
            this.shortUrl = shortUrl;
        }

        public String getBizId() {
            return bizId;
        }

        public void setBizId(String bizId) {
            this.bizId = bizId;
        }

        public String getAuthUrl() {
            return authUrl;
        }

        public void setAuthUrl(String authUrl) {
            this.authUrl = authUrl;
        }

        public String getOriginalUrl() {
            return originalUrl;
        }

        public void setOriginalUrl(String originalUrl) {
            this.originalUrl = originalUrl;
        }

        public long getExpire() {
            return expire;
        }

        public void setExpire(long expire) {
            this.expire = expire;
        }

        public String getFlowId() {
            return flowId;
        }

        public void setFlowId(String flowId) {
            this.flowId = flowId;
        }
    }
}
