package com.ssh.shuoshi.bean;

/**
 * 视频消息
 */
public class IMVideoMessage extends IMBaseMessage {

    private VideoExtra content;

    public VideoExtra getContent() {
        return content;
    }

    public void setContent(VideoExtra content) {
        this.content = content;
    }
}
