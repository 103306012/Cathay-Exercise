package com.cathay.coindesk.usecase.createcoin.impl;

import com.cathay.coindesk.repository.Coin;
import com.cathay.coindesk.repository.CoinRepository;
import com.cathay.coindesk.usecase.createcoin.*;
import com.cathay.coindesk.utility.CurrencyChecker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CreateCoinInteractorImpl implements CreateCoinInteractor {

    private final CoinRepository coinRepository;

    @Autowired
    public CreateCoinInteractorImpl(CoinRepository coinRepository) {
        this.coinRepository = coinRepository;
    }

    @Override
    public void execute(CreateCoinInput input, CreateCoinOutput output) throws CreateCoinException {

        if (coinRepository.findByChineseName(input.getChineseName()) != null || coinRepository.findByCode(input.getCode()) != null) {
            throw new CreateDuplicateCoinException();
        }

        if(!CurrencyChecker.isValidCurrencyCode(input.getCode())) {
            throw new CreateInvalidCoinException();
        }
        Coin coin = new Coin();
        coin.setChineseName(input.getChineseName());
        coin.setCode(input.getCode());
        coinRepository.save(coin);
        output.setId(coin.getId());

    }


}
