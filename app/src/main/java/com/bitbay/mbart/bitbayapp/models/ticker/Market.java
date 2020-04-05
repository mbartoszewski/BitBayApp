package com.bitbay.mbart.bitbayapp.models.ticker;

import com.fasterxml.jackson.annotation.*;

public class Market {
    private String code;
    private First first;
    private First second;

    @JsonProperty("code")
    public String getCode() { return code; }
    @JsonProperty("code")
    public void setCode(String value) { this.code = value; }

    @JsonProperty("first")
    public First getFirst() { return first; }
    @JsonProperty("first")
    public void setFirst(First value) { this.first = value; }

    @JsonProperty("second")
    public First getSecond() { return second; }
    @JsonProperty("second")
    public void setSecond(First value) { this.second = value; }
}