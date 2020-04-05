package com.bitbay.mbart.bitbayapp.models;

public class CryptoDeposit {
    private String status;
    private String data;
    private String[] errors;

    public String getStatus() { return status; }
    public void setStatus(String value) { this.status = value; }

    public String getData() { return data; }
    public void setData(String value) { this.data = value; }

    public String[] getErrors() { return errors; }
    public void setErrors(String[] value) { this.errors = value; }
}
