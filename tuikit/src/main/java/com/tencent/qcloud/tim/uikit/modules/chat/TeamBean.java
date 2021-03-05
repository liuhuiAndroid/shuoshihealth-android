package com.tencent.qcloud.tim.uikit.modules.chat;

import java.util.List;

/**
 * created by hwt on 2021/1/15
 */
public class TeamBean {

    private String key;

    private List<Team> info;

    public static class Team {

        private String name;
        private String groupId;
        private String titleName;

        public String getTitleName() {
            return titleName;
        }

        public void setTitleName(String titleName) {
            this.titleName = titleName;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getGroupId() {
            return groupId;
        }

        public void setGroupId(String groupId) {
            this.groupId = groupId;
        }
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public List<Team> getInfo() {
        return info;
    }

    public void setInfo(List<Team> info) {
        this.info = info;
    }
}
