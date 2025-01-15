package com.cathay.coindesk.controller;

import com.fasterxml.jackson.annotation.JsonInclude;

public class DeleteCoinResponseBean {

    private String message;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
