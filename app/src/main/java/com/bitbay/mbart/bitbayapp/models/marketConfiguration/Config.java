package com.bitbay.mbart.bitbayapp.models.marketConfiguration;

public class Config {
    private Buy buy;
    private Buy sell;
    private First first;
    private First second;

    public Buy getBuy() { return buy; }
    public void setBuy(Buy value) { this.buy = value; }

    public Buy getSell() { return sell; }
    public void setSell(Buy value) { this.sell = value; }

    public First getFirst() { return first; }
    public void setFirst(First value) { this.first = value; }

    public First getSecond() { return second; }
    public void setSecond(First value) { this.second = value; }
}