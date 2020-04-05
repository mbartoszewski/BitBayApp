package com.bitbay.mbart.bitbayapp.models.marketConfiguration;

public class Commissions {
    private double maker;
    private double taker;

    public double getMaker() { return maker; }
    public void setMaker(double value) { this.maker = value; }

    public double getTaker() { return taker; }
    public void setTaker(double value) { this.taker = value; }
}