package com.bitbay.mbart.bitbayapp.models.transactionHistory;

import java.util.Map;

public class Query {
    private String[][] markets;
    private String[] limit;
    private String[] offset;
    private String[] fromTime;
    private String[] toTime;
    private String[] initializedBy;
    private String[] rateFrom;
    private String[] rateTo;
    private String[] userAction;
    private String[] nextPageCursor;

    public String[][] getMarkets() { return markets; }
    public void setMarkets(String[][] value) { this.markets = value; }

    public String[] getLimit() { return limit; }
    public void setLimit(String[] value) { this.limit = value; }

    public String[] getOffset() { return offset; }
    public void setOffset(String[] value) { this.offset = value; }

    public String[] getFromTime() { return fromTime; }
    public void setFromTime(String[] value) { this.fromTime = value; }

    public String[] getToTime() { return toTime; }
    public void setToTime(String[] value) { this.toTime = value; }

    public String[] getInitializedBy() { return initializedBy; }
    public void setInitializedBy(String[] value) { this.initializedBy = value; }

    public String[] getRateFrom() { return rateFrom; }
    public void setRateFrom(String[] value) { this.rateFrom = value; }

    public String[] getRateTo() { return rateTo; }
    public void setRateTo(String[] value) { this.rateTo = value; }

    public String[] getUserAction() { return userAction; }
    public void setUserAction(String[] value) { this.userAction = value; }

    public String[] getNextPageCursor() { return nextPageCursor; }
    public void setNextPageCursor(String[] value) { this.nextPageCursor = value; }
}