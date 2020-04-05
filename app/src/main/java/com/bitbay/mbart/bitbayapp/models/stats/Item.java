package com.bitbay.mbart.bitbayapp.models.stats;

import com.fasterxml.jackson.annotation.*;

public class Item {
    private String m;
    private String h;
    private String l;
    private String v;
    private double r24h;

    @JsonProperty("m")
    public String getM() { return m; }
    @JsonProperty("m")
    public void setM(String value) { this.m = value; }

    @JsonProperty("h")
    public String getH() { return h; }
    @JsonProperty("h")
    public void setH(String value) { this.h = value; }

    @JsonProperty("l")
    public String getL() { return l; }
    @JsonProperty("l")
    public void setL(String value) { this.l = value; }
    @JsonIgnoreProperties
    @JsonProperty("r24h")
    public double getR24h() { return r24h; }
    @JsonProperty("v")
    public String getV() { return v; }
    @JsonProperty("v")
    public void setV(String value) { this.v = value; }
}
