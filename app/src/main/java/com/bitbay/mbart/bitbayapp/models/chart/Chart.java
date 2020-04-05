package com.bitbay.mbart.bitbayapp.models.chart;

import java.util.Map;
import com.fasterxml.jackson.annotation.*;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Chart {
    private String status;
    private Map<String, ItemClass> items;

    @SerializedName("errors")
    @Expose
    private String[] error;

    @JsonProperty("status")
    public String getStatus() { return status; }
    @JsonProperty("status")
    public void setStatus(String value) { this.status = value; }
    @JsonProperty("items")
    public Map<String, ItemClass> getItems() { return items; }
    @JsonProperty("items")
    public void setItems(Map<String, ItemClass> value) { this.items = value; }
    public String[] getError() {
        return error;
    }

    public void setError(String[] error) {
        this.error = error;
    }
}