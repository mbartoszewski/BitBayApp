package com.bitbay.mbart.bitbayapp.models.orderbook;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class OrderbookItems {

    @SerializedName("ra")
    @Expose
    private String ra;
    @SerializedName("ca")
    @Expose
    private String ca;
    @SerializedName("sa")
    @Expose
    private String sa;
    @SerializedName("pa")
    @Expose
    private String pa;
    @SerializedName("co")
    @Expose
    private Integer co;

    /**
     * No args constructor for use in serialization
     *
     */
    public OrderbookItems() {
    }

    /**
     *
     * @param ra
     * @param co
     * @param ca
     * @param pa
     * @param sa
     */
    public OrderbookItems(String ra, String ca, String sa, String pa, Integer co) {
        super();
        this.ra = ra;
        this.ca = ca;
        this.sa = sa;
        this.pa = pa;
        this.co = co;
    }

    public String getRa() {
        return ra;
    }

    public void setRa(String ra) {
        this.ra = ra;
    }

    public String getCa() {
        return ca;
    }

    public void setCa(String ca) {
        this.ca = ca;
    }

    public String getSa() {
        return sa;
    }

    public void setSa(String sa) {
        this.sa = sa;
    }

    public String getPa() {
        return pa;
    }

    public void setPa(String pa) {
        this.pa = pa;
    }

    public Integer getCo() {
        return co;
    }

    public void setCo(Integer co) {
        this.co = co;
    }

}