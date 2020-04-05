package com.bitbay.mbart.bitbayapp.models.openItems;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class OpenItems {

    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("items")
    @Expose
    private List<Item> items = null;
    @SerializedName("errors")
    @Expose
    private String[] errors;

    /**
     * No args constructor for use in serialization
     *
     */
    public OpenItems() {
    }

    /**
     *
     * @param items
     * @param status
     */
    public OpenItems(String status, List<Item> items) {
        super();
        this.status = status;
        this.items = items;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }

    public String[] getErrors() {
        return errors;
    }

    public void setErrors(String[] errors) {
        this.errors = errors;
    }
}