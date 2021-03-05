package com.ssh.shuoshi.bean.history;

/**
 * created by hwt on 2021/1/15
 */
public class TextBean {

    private ContentBean content;
    private String key;
    private String action;

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public ContentBean getContent() {
        return content;
    }

    public void setContent(ContentBean content) {
        this.content = content;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
