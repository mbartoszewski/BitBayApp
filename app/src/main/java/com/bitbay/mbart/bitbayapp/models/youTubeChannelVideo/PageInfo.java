package com.bitbay.mbart.bitbayapp.models.youTubeChannelVideo;

import java.util.Map;

public class PageInfo {
    private long totalResults;
    private long resultsPerPage;

    public long getTotalResults() { return totalResults; }
    public void setTotalResults(long value) { this.totalResults = value; }

    public long getResultsPerPage() { return resultsPerPage; }
    public void setResultsPerPage(long value) { this.resultsPerPage = value; }
}