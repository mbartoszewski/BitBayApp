package com.bitbay.mbart.bitbayapp.models.openItems;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.math.BigDecimal;

public class Item {

    @SerializedName("market")
    @Expose
    private String market;
    @SerializedName("offerType")
    @Expose
    private String offerType;
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("currentAmount")
    @Expose
    private BigDecimal currentAmount;
    @SerializedName("lockedAmount")
    @Expose
    private BigDecimal lockedAmount;
    @SerializedName("rate")
    @Expose
    private BigDecimal rate;
    @SerializedName("startAmount")
    @Expose
    private BigDecimal startAmount;
    @SerializedName("time")
    @Expose
    private String time;
    @SerializedName("postOnly")
    @Expose
    private Boolean postOnly;
    @SerializedName("hidden")
    @Expose
    private Boolean hidden;
    @SerializedName("mode")
    @Expose
    private String mode;
    @SerializedName("receivedAmount")
    @Expose
    private BigDecimal receivedAmount;

    /**
     * No args constructor for use in serialization
     *
     */
    public Item() {
    }

    /**
     *
     * @param id
     * @param time
     * @param startAmount
     * @param offerType
     * @param rate
     * @param market
     * @param hidden
     * @param postOnly
     * @param lockedAmount
     * @param receivedAmount
     * @param currentAmount
     * @param mode
     */
    public Item(String market, String offerType, String id, BigDecimal currentAmount, BigDecimal lockedAmount, BigDecimal rate, BigDecimal startAmount, String time, Boolean postOnly, Boolean hidden, String mode, BigDecimal receivedAmount) {
        super();
        this.market = market;
        this.offerType = offerType;
        this.id = id;
        this.currentAmount = currentAmount;
        this.lockedAmount = lockedAmount;
        this.rate = rate;
        this.startAmount = startAmount;
        this.time = time;
        this.postOnly = postOnly;
        this.hidden = hidden;
        this.mode = mode;
        this.receivedAmount = receivedAmount;
    }

    public String getMarket() {
        return market;
    }

    public void setMarket(String market) {
        this.market = market;
    }

    public String getOfferType() {
        return offerType;
    }

    public void setOfferType(String offerType) {
        this.offerType = offerType;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public BigDecimal getCurrentAmount() {
        return currentAmount;
    }

    public void setCurrentAmount(BigDecimal currentAmount) {
        this.currentAmount = currentAmount;
    }

    public BigDecimal getLockedAmount() {
        return lockedAmount;
    }

    public void setLockedAmount(BigDecimal lockedAmount) {
        this.lockedAmount = lockedAmount;
    }

    public BigDecimal getRate() {
        return rate;
    }

    public void setRate(BigDecimal rate) {
        this.rate = rate;
    }

    public BigDecimal getStartAmount() {
        return startAmount;
    }

    public void setStartAmount(BigDecimal startAmount) {
        this.startAmount = startAmount;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public Boolean getPostOnly() {
        return postOnly;
    }

    public void setPostOnly(Boolean postOnly) {
        this.postOnly = postOnly;
    }

    public Boolean getHidden() {
        return hidden;
    }

    public void setHidden(Boolean hidden) {
        this.hidden = hidden;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public BigDecimal getReceivedAmount() {
        return receivedAmount;
    }

    public void setReceivedAmount(BigDecimal receivedAmount) {
        this.receivedAmount = receivedAmount;
    }

}