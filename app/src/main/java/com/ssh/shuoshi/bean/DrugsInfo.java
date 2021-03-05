package com.ssh.shuoshi.bean;

/**
 * 药品
 */
public class DrugsInfo {

    private String imageUrl;
    private String name;
    // 规格
    private String specification;

    private String price;

    public DrugsInfo(String imageUrl, String name, String specification, String price) {
        this.imageUrl = imageUrl;
        this.name = name;
        this.specification = specification;
        this.price = price;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSpecification() {
        return specification;
    }

    public void setSpecification(String specification) {
        this.specification = specification;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}
