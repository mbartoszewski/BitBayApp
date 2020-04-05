package com.bitbay.mbart.bitbayapp.models.fiatDeposit;

public class Data {
    private String bankAccountNumber;
    private String address;
    private String name;
    private String title;
    private String swift;

    public String getBankAccountNumber() { return bankAccountNumber; }
    public void setBankAccountNumber(String value) { this.bankAccountNumber = value; }

    public String getAddress() { return address; }
    public void setAddress(String value) { this.address = value; }

    public String getName() { return name; }
    public void setName(String value) { this.name = value; }

    public String getTitle() { return title; }
    public void setTitle(String value) { this.title = value; }

    public String getSwift() { return swift; }
    public void setSwift(String value) { this.swift = value; }
}