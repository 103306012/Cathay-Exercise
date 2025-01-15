package com.cathay.coindesk.repository;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CoinRepository extends JpaRepository<Coin, Long> {

    public Coin findByChineseName(String chineseName);

    public Coin findByCode(String code);
}
