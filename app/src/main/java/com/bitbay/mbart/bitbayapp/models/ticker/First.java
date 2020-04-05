package com.bitbay.mbart.bitbayapp.models.ticker;

import com.fasterxml.jackson.annotation.*;

public class First {
    private String currency;
    private String minOffer;
    private Long scale;

    @JsonProperty("currency")
    public String getCurrency() { return currency; }
    @JsonProperty("currency")
    public void setCurrency(String value) { this.currency = value; }

    @JsonProperty("minOffer")
    public String getMinOffer() { return minOffer; }
    @JsonProperty("minOffer")
    public void setMinOffer(String value) { this.minOffer = value; }

    @JsonProperty("scale")
    public Long getScale() { return scale; }
    @JsonProperty("scale")
    public void setScale(Long value) { this.scale = value; }
}
