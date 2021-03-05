package com.ssh.shuoshi.bean.team;

import java.util.List;

/**
 * created by hwt on 2021/1/3
 */
public class DistinctDoctorBean {

    private int total;
    private int totalPage;
    private List<String> rows;

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

    public List<String> getRows() {
        return rows;
    }

    public void setRows(List<String> rows) {
        this.rows = rows;
    }
}
