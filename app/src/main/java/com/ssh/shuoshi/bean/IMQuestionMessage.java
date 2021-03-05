package com.ssh.shuoshi.bean;

/**
 * created by hwt on 2020/12/19
 */
public class IMQuestionMessage extends IMBaseMessage {

    private QuestionExtra content;

    public QuestionExtra getContent() {
        return content;
    }

    public void setContent(QuestionExtra content) {
        this.content = content;
    }
}
