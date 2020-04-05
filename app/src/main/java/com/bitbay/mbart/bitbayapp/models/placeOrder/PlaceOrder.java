package com.bitbay.mbart.bitbayapp.models.placeOrder;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PlaceOrder {

    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("errors")
    @Expose
    private String[] error;
    @SerializedName("completed")
    @Expose
    private Boolean completed;
    @SerializedName("offerId")
    @Expose
    private String offerId;
    @SerializedName("transactions")
    @Expose
    private List<Transaction> transactions = null;

    /**
     * No args constructor for use in serialization
     *
     */
    public PlaceOrder() {
    }

    /**
     *
     * @param transactions
     * @param status
     * @param offerId
     * @param completed
     */
    public PlaceOrder(String status, Boolean completed, String offerId, List<Transaction> transactions, String[] error) {
        super();
        this.status = status;
        this.completed = completed;
        this.offerId = offerId;
        this.transactions = transactions;
        this.error = error;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Boolean getCompleted() {
        return completed;
    }

    public void setCompleted(Boolean completed) {
        this.completed = completed;
    }

    public String getOfferId() {
        return offerId;
    }

    public void setOfferId(String offerId) {
        this.offerId = offerId;
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }

    public void setTransactions(List<Transaction> transactions) {
        this.transactions = transactions;
    }

    public String[] getError() {
        return error;
    }

    public void setError(String[] error) {
        this.error = error;
    }
}