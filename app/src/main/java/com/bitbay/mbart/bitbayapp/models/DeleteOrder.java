package com.bitbay.mbart.bitbayapp.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DeleteOrder {

    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("errors")
    @Expose
    private String[] errors = null;

    /**
     * No args constructor for use in serialization
     *
     */
    public DeleteOrder() {
    }

    /**
     *
     * @param errors
     * @param status
     */
    public DeleteOrder(String status, String[] errors) {
        super();
        this.status = status;
        this.errors = errors;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String[] getErrors() {
        return errors;
    }

    public void setErrors(String[] errors) {
        this.errors = errors;
    }

}