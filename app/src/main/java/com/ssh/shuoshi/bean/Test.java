package com.ssh.shuoshi.bean;

import java.util.List;

/**
 * created by hwt on 2020/12/19
 */
public class Test {


    /**
     * resultStatus : {"code":200,"message":"查询成功"}
     * resultValue : {"total":181,"rows":[{"virtualDom":{"name":"custom","text":{"content":{"room_id":561,"version":3,"call_id":"","duration":20,"action":0,"from":"android","invited_list":[]},"key":"health_video"}},"conversationID":"GROUPhealth_561","isRead":true,"type":"TIMCustomElem","conversationType":"GROUP","nick":"","payload":{"text":""},"from":"IM210109193540038","ID":14,"time":"2021-01-18 15:39:23","to":"health_561","flow":"out","isResend":true}],"totalPage":13}
     */

    private ResultStatusBean resultStatus;
    private ResultValueBean resultValue;

    public ResultStatusBean getResultStatus() {
        return resultStatus;
    }

    public void setResultStatus(ResultStatusBean resultStatus) {
        this.resultStatus = resultStatus;
    }

    public ResultValueBean getResultValue() {
        return resultValue;
    }

    public void setResultValue(ResultValueBean resultValue) {
        this.resultValue = resultValue;
    }

    public static class ResultStatusBean {
        /**
         * code : 200
         * message : 查询成功
         */

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

    public static class ResultValueBean {
        /**
         * total : 181
         * rows : [{"virtualDom":{"name":"custom","text":{"content":{"room_id":561,"version":3,"call_id":"","duration":20,"action":0,"from":"android","invited_list":[]},"key":"health_video"}},"conversationID":"GROUPhealth_561","isRead":true,"type":"TIMCustomElem","conversationType":"GROUP","nick":"","payload":{"text":""},"from":"IM210109193540038","ID":14,"time":"2021-01-18 15:39:23","to":"health_561","flow":"out","isResend":true}]
         * totalPage : 13
         */

        private int total;
        private int totalPage;
        private List<RowsBean> rows;

        public int getTotal() {
            return total;
        }

        public void setTotal(int total) {
            this.total = total;
        }

        public int getTotalPage() {
            return totalPage;
        }

        public void setTotalPage(int totalPage) {
            this.totalPage = totalPage;
        }

        public List<RowsBean> getRows() {
            return rows;
        }

        public void setRows(List<RowsBean> rows) {
            this.rows = rows;
        }

        public static class RowsBean {
            /**
             * virtualDom : {"name":"custom","text":{"content":{"room_id":561,"version":3,"call_id":"","duration":20,"action":0,"from":"android","invited_list":[]},"key":"health_video"}}
             * conversationID : GROUPhealth_561
             * isRead : true
             * type : TIMCustomElem
             * conversationType : GROUP
             * nick :
             * payload : {"text":""}
             * from : IM210109193540038
             * ID : 14
             * time : 2021-01-18 15:39:23
             * to : health_561
             * flow : out
             * isResend : true
             */

            private VirtualDomBean virtualDom;
            private String conversationID;
            private boolean isRead;
            private String type;
            private String conversationType;
            private String nick;
            private PayloadBean payload;
            private String from;
            private int ID;
            private String time;
            private String to;
            private String flow;
            private boolean isResend;

            public VirtualDomBean getVirtualDom() {
                return virtualDom;
            }

            public void setVirtualDom(VirtualDomBean virtualDom) {
                this.virtualDom = virtualDom;
            }

            public String getConversationID() {
                return conversationID;
            }

            public void setConversationID(String conversationID) {
                this.conversationID = conversationID;
            }

            public boolean isIsRead() {
                return isRead;
            }

            public void setIsRead(boolean isRead) {
                this.isRead = isRead;
            }

            public String getType() {
                return type;
            }

            public void setType(String type) {
                this.type = type;
            }

            public String getConversationType() {
                return conversationType;
            }

            public void setConversationType(String conversationType) {
                this.conversationType = conversationType;
            }

            public String getNick() {
                return nick;
            }

            public void setNick(String nick) {
                this.nick = nick;
            }

            public PayloadBean getPayload() {
                return payload;
            }

            public void setPayload(PayloadBean payload) {
                this.payload = payload;
            }

            public String getFrom() {
                return from;
            }

            public void setFrom(String from) {
                this.from = from;
            }

            public int getID() {
                return ID;
            }

            public void setID(int ID) {
                this.ID = ID;
            }

            public String getTime() {
                return time;
            }

            public void setTime(String time) {
                this.time = time;
            }

            public String getTo() {
                return to;
            }

            public void setTo(String to) {
                this.to = to;
            }

            public String getFlow() {
                return flow;
            }

            public void setFlow(String flow) {
                this.flow = flow;
            }

            public boolean isIsResend() {
                return isResend;
            }

            public void setIsResend(boolean isResend) {
                this.isResend = isResend;
            }

            public static class VirtualDomBean {
                /**
                 * name : custom
                 * text : {"content":{"room_id":561,"version":3,"call_id":"","duration":20,"action":0,"from":"android","invited_list":[]},"key":"health_video"}
                 */

                private String name;
                private TextBean text;

                public String getName() {
                    return name;
                }

                public void setName(String name) {
                    this.name = name;
                }

                public TextBean getText() {
                    return text;
                }

                public void setText(TextBean text) {
                    this.text = text;
                }

                public static class TextBean {
                    /**
                     * content : {"room_id":561,"version":3,"call_id":"","duration":20,"action":0,"from":"android","invited_list":[]}
                     * key : health_video
                     */

                    private ContentBean content;
                    private String key;

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

                    public static class ContentBean {
                        /**
                         * room_id : 561
                         * version : 3
                         * call_id :
                         * duration : 20
                         * action : 0
                         * from : android
                         * invited_list : []
                         */

                        private int room_id;
                        private int version;
                        private String call_id;
                        private int duration;
                        private int action;
                        private String from;
                        private List<?> invited_list;

                        public int getRoom_id() {
                            return room_id;
                        }

                        public void setRoom_id(int room_id) {
                            this.room_id = room_id;
                        }

                        public int getVersion() {
                            return version;
                        }

                        public void setVersion(int version) {
                            this.version = version;
                        }

                        public String getCall_id() {
                            return call_id;
                        }

                        public void setCall_id(String call_id) {
                            this.call_id = call_id;
                        }

                        public int getDuration() {
                            return duration;
                        }

                        public void setDuration(int duration) {
                            this.duration = duration;
                        }

                        public int getAction() {
                            return action;
                        }

                        public void setAction(int action) {
                            this.action = action;
                        }

                        public String getFrom() {
                            return from;
                        }

                        public void setFrom(String from) {
                            this.from = from;
                        }

                        public List<?> getInvited_list() {
                            return invited_list;
                        }

                        public void setInvited_list(List<?> invited_list) {
                            this.invited_list = invited_list;
                        }
                    }
                }
            }

            public static class PayloadBean {
                /**
                 * text :
                 */

                private String text;

                public String getText() {
                    return text;
                }

                public void setText(String text) {
                    this.text = text;
                }
            }
        }
    }
}
