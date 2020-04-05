package com.bitbay.mbart.bitbayapp.models.ticker;

import com.fasterxml.jackson.annotation.*;

public class Item {
    private Market market;
    private String time;
    private String highestBid;
    private String lowestAsk;
    private String rate;
    private String previousRate;

    @JsonProperty("market")
    public Market getMarket() { return market; }
    @JsonProperty("market")
    public void setMarket(Market value) { this.market = value; }

    @JsonProperty("time")
    public String getTime() { return time; }
    @JsonProperty("time")
    public void setTime(String value) { this.time = value; }

    @JsonProperty("highestBid")
    public String getHighestBid() { return highestBid; }
    @JsonProperty("highestBid")
    public void setHighestBid(String value) { this.highestBid = value; }

    @JsonProperty("lowestAsk")
    public String getLowestAsk() { return lowestAsk; }
    @JsonProperty("lowestAsk")
    public void setLowestAsk(String value) { this.lowestAsk = value; }

    @JsonProperty("rate")
    public String getRate() { return rate; }
    @JsonProperty("rate")
    public void setRate(String value) { this.rate = value; }

    @JsonProperty("previousRate")
    public String getPreviousRate() { return previousRate; }
    @JsonProperty("previousRate")
    public void setPreviousRate(String value) { this.previousRate = value; }
}