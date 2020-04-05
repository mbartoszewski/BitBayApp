package com.bitbay.mbart.bitbayapp.models;

import com.bitbay.mbart.bitbayapp.models.ticker.Ticker;
import com.bitbay.mbart.bitbayapp.models.wallet.WalletLists;


public class Dashboard {

    public Dashboard() {

    }

    public Dashboard(WalletLists balances, Ticker ticker) {
        this.balances = balances;

        this.ticker = ticker;
    }

    public WalletLists getBalances() {
        return balances;
    }

    public void setBalances(WalletLists balances) {
        this.balances = balances;
    }

    public Ticker getTicker() {
        return ticker;
    }

    public void setTicker(Ticker ticker) {
        this.ticker = ticker;
    }

    private WalletLists balances = null;
    private Ticker ticker = null;
}
