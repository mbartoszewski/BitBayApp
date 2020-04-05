package com.bitbay.mbart.bitbayapp.models.transactionHistory;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Map;

public class TransactionHistory {
    private String status;
    private int totalRows;
    private Item[] items;
    private Query query;
    private String nextPageCursor;
    @SerializedName("errors")
    @Expose
    private String[] error;

    public String getStatus() { return status; }
    public void setStatus(String value) { this.status = value; }

    public int getTotalRows() { return totalRows; }
    public void setTotalRows(int value) { this.totalRows = value; }

    public Item[] getItems() { return items; }
    public void setItems(Item[] value) { this.items = value; }

    public Query getQuery() { return query; }
    public void setQuery(Query value) { this.query = value; }

    public String getNextPageCursor() { return nextPageCursor; }
    public void setNextPageCursor(String value) { this.nextPageCursor = value; }

    public String[] getError() {
        return error;
    }

    public void setError(String[] error) {
        error = error;
    }
}