package com.cathay.coindesk.repository;

import javax.persistence.*;

@Entity
@Table(name = "coin")
public class Coin {
    @Id
    @GeneratedValue
    private Long id;

    @Column(name = "chinese_name",nullable = false)
    private String chineseName;

    @Column(name = "code",nullable = false)
    private String code;

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
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
