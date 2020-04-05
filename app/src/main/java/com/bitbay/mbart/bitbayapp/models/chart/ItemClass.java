package com.bitbay.mbart.bitbayapp.models.chart;

import com.fasterxml.jackson.annotation.*;

public class ItemClass{
    private String o;
    private String c;
    private String h;
    private String l;
    private String v;
    private String timestamp;

    public ItemClass(String timestamp, String o, String c, String h, String l, String v)
    {
        this.timestamp = timestamp;
        this.o = o;
        this.c = c;
        this.h = h;
        this.l = l;
        this.v = v;
    }

    @JsonProperty("o")
    public String getO() { return o; }
    @JsonProperty("o")
    public void setO(String value) { this.o = value; }

    @JsonProperty("c")
    public String getC() { return c; }
    @JsonProperty("c")
    public void setC(String value) { this.c = value; }

    @JsonProperty("h")
    public String getH() { return h; }
    @JsonProperty("h")
    public void setH(String value) { this.h = value; }

    @JsonProperty("l")
    public String getL() { return l; }
    @JsonProperty("l")
    public void setL(String value) { this.l = value; }

    @JsonProperty("v")
    public String getV() { return v; }
    @JsonProperty("v")
    public void setV(String value) { this.v = value; }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}