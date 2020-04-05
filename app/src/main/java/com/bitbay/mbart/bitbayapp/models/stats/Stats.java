package com.bitbay.mbart.bitbayapp.models.stats;

import java.util.Map;
import com.fasterxml.jackson.annotation.*;

public class Stats {
    private String status;
    private Map<String, Item> items;
    private String[] errors;
    private Long estimatedTime;

    @JsonProperty("status")
    public String getStatus() { return status; }
    @JsonProperty("status")
    public void setStatus(String value) { this.status = value; }

    @JsonProperty("items")
    public Map<String, Item> getItems() { return items; }
    @JsonProperty("items")
    public void setItems(Map<String, Item> value) { this.items = value; }

    @JsonProperty("errors")
    public String[] getErrors() { return errors; }
    @JsonProperty("errors")
    public void setErrors(String[] value) { this.errors = value; }

    @JsonProperty("estimatedTime")
    public Long getEstimatedTime() { return estimatedTime; }
    @JsonProperty("estimatedTime")
    public void setEstimatedTime(Long value) { this.estimatedTime = value; }
}