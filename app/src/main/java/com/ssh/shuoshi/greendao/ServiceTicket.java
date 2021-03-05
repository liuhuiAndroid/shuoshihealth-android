package com.ssh.shuoshi.greendao;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

/**
 * author ：hwt
 * date : 2019/6/25 14:10
 */
@Entity
public class ServiceTicket {

    @Id
    private String orderNo;

    private String photos;

    private int orderType;

    private String remark;

    @Generated(hash = 1805490140)
    public ServiceTicket(String orderNo, String photos, int orderType,
            String remark) {
        this.orderNo = orderNo;
        this.photos = photos;
        this.orderType = orderType;
        this.remark = remark;
    }

    @Generated(hash = 2105530894)
    public ServiceTicket() {
    }

    public String getOrderNo() {
        return this.orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getPhotos() {
        return this.photos;
    }

    public void setPhotos(String photos) {
        this.photos = photos;
    }

    public int getOrderType() {
        return this.orderType;
    }

    public void setOrderType(int orderType) {
        this.orderType = orderType;
    }

    public String getRemark() {
        return this.remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
    
}
