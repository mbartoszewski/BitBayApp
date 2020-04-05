package com.bitbay.mbart.bitbayapp.models.orderbook;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Orderbook {

    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("errors")
    @Expose
    private String[] error;
    @SerializedName("sell")
    @Expose
    private List<OrderbookItems> sell = null;
    @SerializedName("buy")
    @Expose
    private List<OrderbookItems> buy = null;

    /**
     * No args constructor for use in serialization
     *
     */
    public Orderbook() {
    }

    /**
     *
     * @param buy
     * @param sell
     * @param status
     */
    public Orderbook(String status, String[] error, List<OrderbookItems> sell, List<OrderbookItems> buy) {
        super();
        this.status = status;
        this.error = error;
        this.sell = sell;
        this.buy = buy;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String[] getError() {
        return error;
    }

    public void setError(String[] error) {
        this.error = error;
    }

    public List<OrderbookItems> getSell() {
        return sell;
    }

    public void setSell(List<OrderbookItems> sell) {
        this.sell = sell;
    }

    public List<OrderbookItems> getBuy() {
        return buy;
    }

    public void setBuy(List<OrderbookItems> buy) {
        this.buy = buy;
    }

}