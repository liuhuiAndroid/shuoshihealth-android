package com.ssh.shuoshi.bean;

public class JPushMessageExtra {

    private NExtrasBean n_extras;
    private String n_title;
    private String n_content;
    private long msg_id;
    private int show_type;
    private int rom_type;

    public NExtrasBean getN_extras() {
        return n_extras;
    }

    public void setN_extras(NExtrasBean n_extras) {
        this.n_extras = n_extras;
    }

    public String getN_title() {
        return n_title;
    }

    public void setN_title(String n_title) {
        this.n_title = n_title;
    }

    public String getN_content() {
        return n_content;
    }

    public void setN_content(String n_content) {
        this.n_content = n_content;
    }

    public long getMsg_id() {
        return msg_id;
    }

    public void setMsg_id(long msg_id) {
        this.msg_id = msg_id;
    }

    public int getShow_type() {
        return show_type;
    }

    public void setShow_type(int show_type) {
        this.show_type = show_type;
    }

    public int getRom_type() {
        return rom_type;
    }

    public void setRom_type(int rom_type) {
        this.rom_type = rom_type;
    }

    public static class NExtrasBean {
        /**
         * extraBean : {"bussinessId":227,"msgType":4}
         */

        private ExtraBeanBean extraBean;

        public ExtraBeanBean getExtraBean() {
            return extraBean;
        }

        public void setExtraBean(ExtraBeanBean extraBean) {
            this.extraBean = extraBean;
        }

        public static class ExtraBeanBean {
            /**
             * bussinessId : 227
             * msgType : 4
             */

            private Integer bussinessId;
            private Integer msgType;

            public Integer getBussinessId() {
                return bussinessId;
            }

            public void setBussinessId(Integer bussinessId) {
                this.bussinessId = bussinessId;
            }

            public Integer getMsgType() {
                return msgType;
            }

            public void setMsgType(Integer msgType) {
                this.msgType = msgType;
            }
        }
    }

}
