package com.cathay.coindesk.usecase.createcoin;

public interface CreateCoinInteractor {
    public void execute(CreateCoinInput input, CreateCoinOutput output) throws  CreateCoinException;
}
