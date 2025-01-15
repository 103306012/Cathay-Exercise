package com.cathay.coindesk.controller;

import java.util.List;

public class CoindeskResponseBean {

    private String updatedTime;
    private List<CoindeskCoinBean> coinBeanList;

    public String getUpdatedTime() {
        return updatedTime;
    }

    public void setUpdatedTime(String updatedTime) {
        this.updatedTime = updatedTime;
    }

    public List<CoindeskCoinBean> getCoinBeanList() {
        return coinBeanList;
    }

    public void setCoinBeanList(List<CoindeskCoinBean> coinBeanList) {
        this.coinBeanList = coinBeanList;
    }
}
