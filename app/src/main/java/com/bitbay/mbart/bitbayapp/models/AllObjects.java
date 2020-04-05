package com.bitbay.mbart.bitbayapp.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AllObjects {

    @SerializedName("max")
    @Expose
    private String max;
    @SerializedName("min")
    @Expose
    private String min;
    @SerializedName("last")
    @Expose
    private String last;
    @SerializedName("previousRate")
    @Expose
    private String previousRate;
    @SerializedName("bid")
    @Expose
    private String bid;
    @SerializedName("ask")
    @Expose
    private String ask;
    @SerializedName("volume")
    @Expose
    private String volume;

    private String cryptoCurrency;

    private String fiatCurrency;

    private long timestamp;

    private double currencyAmount;

    /**
     * No args constructor for use in serialization
     *
     */
    public AllObjects() {
    }

    /**
     *
     * @param timestamp
     * @param max
     * @param min
     * @param last
     * @param bid
     * @param ask
     * @param volume
     */
    public AllObjects(String cryptoCurrency, String fiatCurrency, double currencyAmount, long timestamp, String max, String min, String last,String previousRate, String bid, String ask, String volume) {
        super();
        this.cryptoCurrency = cryptoCurrency;
        this.fiatCurrency = fiatCurrency;
        this.currencyAmount = currencyAmount;
        this.timestamp = timestamp;
        this.max = max;
        this.min = min;
        this.last = last;
        this.previousRate = previousRate;
        this.bid = bid;
        this.ask = ask;
        this.volume = volume;
    }


    public String getMax() {
        return max;
    }

    public void setMax(String max) {
        this.max = max;
    }

    public String getMin() {
        return min;
    }

    public void setMin(String min) {
        this.min = min;
    }

    public String getLast() {
        return last;
    }

    public void setLast(String last) {
        this.last = last;
    }

    public String getPreviousRate() {
        return previousRate;
    }

    public void setPreviousRate(String previousRate) {
        this.previousRate = previousRate;
    }

    public String getBid() {
        return bid;
    }

    public void setBid(String bid) {
        this.bid = bid;
    }

    public String getAsk() {
        return ask;
    }

    public void setAsk(String ask) {
        this.ask = ask;
    }

    public String getVolume() {
        return volume;
    }

    public void setVolume(String volume) {
        this.volume = volume;
    }

    public String getCryptoCurrency() {
        return cryptoCurrency;
    }

    public void setCryptoCurrency(String cryptoCurrency) {
        this.cryptoCurrency = cryptoCurrency;
    }

    public String getFiatCurrency() {
        return fiatCurrency;
    }

    public void setFiatCurrency(String fiatCurrency) {
        this.fiatCurrency = fiatCurrency;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public double getCurrencyAmount() {
        return currencyAmount;
    }

    public void setCurrencyAmount(double currencyAmount) {
        this.currencyAmount = currencyAmount;
    }
}