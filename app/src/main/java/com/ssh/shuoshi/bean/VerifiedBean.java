package com.ssh.shuoshi.bean;

import com.ssh.shuoshi.view.wheelview.interfaces.IPickerViewData;

public class VerifiedBean implements IPickerViewData {

    private int code;
    private String name;

    public VerifiedBean(int code, String name) {
        this.code = code;
        this.name = name;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getPickerViewText() {
        return this.name;
    }
}
