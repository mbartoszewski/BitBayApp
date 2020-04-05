package com.bitbay.mbart.bitbayapp.models.marketConfiguration;

public class MarketConfiguration {
    private String status;
    private Config config;
    private String[] errors;

    public String getStatus() { return status; }
    public void setStatus(String value) { this.status = value; }

    public String[] getErrors() { return errors; }
    public void setErrors(String[] value) { this.errors = value; }

    public Config getConfig() { return config; }
    public void setConfig(Config value) { this.config = value; }
}