package com.cathay.coindesk.controller;

import com.fasterxml.jackson.annotation.JsonInclude;

public class CreateCoinResponseBean {
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Long id;

    private String message;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
