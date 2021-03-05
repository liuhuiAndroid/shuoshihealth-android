package com.ssh.shuoshi.bean;

import java.util.ArrayList;
import java.util.List;

public class VideoExtra {

    private String call_id;
    private int version;
    private int room_id;

    // 0对方呼叫我 1对方取消 2对方拒绝 3对方超过一分钟不接 4对方接听 5对方挂断 6对方通话中
    private int action;
    private Long duration;
    private ArrayList<String> invited_list;
    private String from = "android";

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public VideoExtra(String call_id, int version, int room_id, int action, Long duration, ArrayList<String> invited_list) {
        this.call_id = call_id;
        this.version = version;
        this.room_id = room_id;
        this.action = action;
        this.duration = duration;
        this.invited_list = invited_list;
    }

    public String getCall_id() {
        return call_id;
    }

    public void setCall_id(String call_id) {
        this.call_id = call_id;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public int getRoom_id() {
        return room_id;
    }

    public void setRoom_id(int room_id) {
        this.room_id = room_id;
    }

    public int getAction() {
        return action;
    }

    public void setAction(int action) {
        this.action = action;
    }

    public Long getDuration() {
        return duration;
    }

    public void setDuration(Long duration) {
        this.duration = duration;
    }

    public List<String> getInvited_list() {
        return invited_list;
    }

    public void setInvited_list(ArrayList<String> invited_list) {
        this.invited_list = invited_list;
    }
}
