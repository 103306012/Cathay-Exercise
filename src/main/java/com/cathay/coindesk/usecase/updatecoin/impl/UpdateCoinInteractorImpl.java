package com.cathay.coindesk.usecase.updatecoin.impl;

import com.cathay.coindesk.repository.Coin;
import com.cathay.coindesk.repository.CoinRepository;
import com.cathay.coindesk.usecase.updatecoin.*;
import com.cathay.coindesk.utility.CurrencyChecker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UpdateCoinInteractorImpl implements UpdateCoinInteractor {

    private final CoinRepository coinRepository;

    @Autowired
    public UpdateCoinInteractorImpl(CoinRepository coinRepository) {
        this.coinRepository = coinRepository;
    }

    @Override
    public void execute(UpdateCoinInput input, UpdateCoinOutput output) throws UpdateCoinException {

        Coin coin = coinRepository.findById(input.getId()).orElseThrow(UpdateCoinNotFoundException::new);

        // check chineseName duplicate
        if (input.getChineseName() != null && coinRepository.findByChineseName(input.getChineseName()) != null && coinRepository.findByChineseName(input.getChineseName()) != coin) {
            throw new UpdateDuplicateCoinException();
        }

        // check code duplicate
        if (input.getCode() != null && coinRepository.findByCode(input.getCode()) != null && coinRepository.findByCode(input.getCode()) != coin) {
            throw new UpdateDuplicateCoinException();
        }

        if (input.getChineseName() != null) {
            coin.setChineseName(input.getChineseName());

        }
        if (input.getCode() != null) {
            if (!CurrencyChecker.isValidCurrencyCode(input.getCode())) {
                throw new UpdateInvalidCoinException();
            }
            coin.setCode(input.getCode());

        }
        Coin newCoin = coinRepository.save(coin);
        output.setId(newCoin.getId());
        output.setChineseName(newCoin.getChineseName());
        output.setCode(newCoin.getCode());
    }


}
