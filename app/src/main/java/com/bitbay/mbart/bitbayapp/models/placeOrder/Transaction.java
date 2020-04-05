package com.bitbay.mbart.bitbayapp.models.placeOrder;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Transaction {

    @SerializedName("amount")
    @Expose
    private String amount;
    @SerializedName("rate")
    @Expose
    private String rate;

    /**
     * No args constructor for use in serialization
     *
     */
    public Transaction() {
    }

    /**
     *
     * @param amount
     * @param rate
     */
    public Transaction(String amount, String rate) {
        super();
        this.amount = amount;
        this.rate = rate;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

}