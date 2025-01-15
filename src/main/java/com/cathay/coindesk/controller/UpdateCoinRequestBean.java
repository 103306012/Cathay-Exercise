package com.cathay.coindesk.controller;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class UpdateCoinRequestBean {

    private String chineseName;

    @Size(max = 3, min = 3, message = "code 長度必須等於3")
    private String code;

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
