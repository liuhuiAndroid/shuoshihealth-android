package com.tencent.qcloud.tim.uikit;

/**
 * created by hwt on 2021/1/15
 */
public class NameEvent {
    private int type;
    private String name;

    public NameEvent(int type, String name) {
        this.type = type;
        this.name = name;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
