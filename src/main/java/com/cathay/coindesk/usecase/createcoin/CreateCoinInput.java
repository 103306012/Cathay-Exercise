package com.cathay.coindesk.usecase.createcoin;

public class CreateCoinInput {
    private String chineseName;
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
