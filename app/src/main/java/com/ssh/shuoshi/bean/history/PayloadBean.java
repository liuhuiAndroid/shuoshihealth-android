package com.ssh.shuoshi.bean.history;

import java.util.List;

/**
 * created by hwt on 2021/1/15
 */
public class PayloadBean {

    private String text;
    private List<ImageInfoBean> imageInfoArray;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public List<ImageInfoBean> getImageInfoArray() {
        return imageInfoArray;
    }

    public void setImageInfoArray(List<ImageInfoBean> imageInfoArray) {
        this.imageInfoArray = imageInfoArray;
    }

    public class ImageInfoBean {
        public String url;
        public int width;
        public int size;
        public int type;
        public int height;

        public int getWidth() {
            return width;
        }

        public void setWidth(int width) {
            this.width = width;
        }

        public int getSize() {
            return size;
        }

        public void setSize(int size) {
            this.size = size;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public int getHeight() {
            return height;
        }

        public void setHeight(int height) {
            this.height = height;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }
    }
}
