package com.cathay.coindesk.controller;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class GetCoinResponseBean {
    private Long id;
    private String chineseName;
    private String code;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getChineseName() {
        return chineseName;
    }

    public void setChineseName(String chineseName) {
        this.chineseName = chineseName;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
