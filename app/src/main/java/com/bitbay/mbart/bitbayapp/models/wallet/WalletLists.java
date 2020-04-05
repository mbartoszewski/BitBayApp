package com.bitbay.mbart.bitbayapp.models.wallet;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class WalletLists {

    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("balances")
    @Expose
    private List<Balance> balances = null;
    @SerializedName("errors")
    @Expose
    private String[] errors;

    /**
     * No args constructor for use in serialization
     *
     */
    public WalletLists() {
    }

    /**
     *
     * @param errors
     * @param status
     * @param balances
     */
    public WalletLists(String status, List<Balance> balances, String[] errors) {
        super();
        this.status = status;
        this.balances = balances;
        this.errors = errors;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<Balance> getBalances() {
        return balances;
    }

    public void setBalances(List<Balance> balances) {
        this.balances = balances;
    }

    public String[] getErrors() {
        return errors;
    }

    public void setErrors(String[] errors) {
        this.errors = errors;
    }

}