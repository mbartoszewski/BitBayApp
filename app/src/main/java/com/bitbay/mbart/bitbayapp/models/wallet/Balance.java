package com.bitbay.mbart.bitbayapp.models.wallet;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Balance {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("userId")
    @Expose
    private String userId;
    @SerializedName("availableFunds")
    @Expose
    private Double availableFunds;
    @SerializedName("totalFunds")
    @Expose
    private Double totalFunds;
    @SerializedName("lockedFunds")
    @Expose
    private Double lockedFunds;
    @SerializedName("currency")
    @Expose
    private String currency;
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("balanceEngine")
    @Expose
    private String balanceEngine;

    private double rate;
    /**
     * No args constructor for use in serialization
     *
     */
    public Balance() {
    }

    /**
     *
     * @param id
     * @param availableFunds
     * @param lockedFunds
     * @param name
     * @param userId
     * @param balanceEngine
     * @param totalFunds
     * @param type
     * @param currency
     */
    public Balance(String id, String userId, Double availableFunds, Double totalFunds, Double lockedFunds, String currency, String type, String name, String balanceEngine, double rate) {
        super();
        this.id = id;
        this.userId = userId;
        this.availableFunds = availableFunds;
        this.totalFunds = totalFunds;
        this.lockedFunds = lockedFunds;
        this.currency = currency;
        this.type = type;
        this.name = name;
        this.balanceEngine = balanceEngine;
        this.rate = rate;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Double getAvailableFunds() {
        return availableFunds;
    }

    public void setAvailableFunds(Double availableFunds) {
        this.availableFunds = availableFunds;
    }

    public Double getTotalFunds() {
        return totalFunds;
    }

    public void setTotalFunds(Double totalFunds) {
        this.totalFunds = totalFunds;
    }

    public Double getLockedFunds() {
        return lockedFunds;
    }

    public void setLockedFunds(Double lockedFunds) {
        this.lockedFunds = lockedFunds;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBalanceEngine() {
        return balanceEngine;
    }

    public void setBalanceEngine(String balanceEngine) {
        this.balanceEngine = balanceEngine;
    }

    public double getRate() {
        return rate;
    }

    public void setRate(double rate) {
        this.rate = rate;
    }
}