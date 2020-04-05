package com.bitbay.mbart.bitbayapp.models;

import com.bitbay.mbart.bitbayapp.models.stats.Stats;
import com.bitbay.mbart.bitbayapp.models.ticker.Ticker;

public class TickerStats {

    public TickerStats() {

    }

    public Ticker getTicker() {
        return ticker;
    }

    public void setTicker(Ticker ticker) {
        this.ticker = ticker;
    }

    public Stats getStats() {
        return stats;
    }

    public void setStats(Stats stats) {
        this.stats = stats;
    }

    private Ticker ticker = null;
    private Stats stats = null;

    public TickerStats(Ticker ticker, Stats stats) {
        this.ticker = ticker;
        this.stats = stats;
    }

}
