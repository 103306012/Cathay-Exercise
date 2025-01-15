package com.cathay.coindesk.usecase.updatecoin;

public interface UpdateCoinInteractor {
    public void execute(UpdateCoinInput input, UpdateCoinOutput output) throws UpdateCoinException;
}
