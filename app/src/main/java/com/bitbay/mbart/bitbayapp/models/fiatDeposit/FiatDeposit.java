package com.bitbay.mbart.bitbayapp.models.fiatDeposit;

public class FiatDeposit {
    private String status;
    private Data data;
    private String[] errors;

    public String getStatus() { return status; }
    public void setStatus(String value) { this.status = value; }

    public Data getData() { return data; }
    public void setData(Data value) { this.data = value; }

    public String[] getErrors() { return errors; }
    public void setErrors(String[] value) { this.errors = value; }
}