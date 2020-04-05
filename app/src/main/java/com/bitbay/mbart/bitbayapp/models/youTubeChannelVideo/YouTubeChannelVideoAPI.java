package com.bitbay.mbart.bitbayapp.models.youTubeChannelVideo;

import java.util.Map;

public class YouTubeChannelVideoAPI {
    private String kind;
    private String etag;
    private String nextPageToken;
    private String regionCode;
    private PageInfo pageInfo;
    private Item[] items;

    public String getKind() { return kind; }
    public void setKind(String value) { this.kind = value; }

    public String getEtag() { return etag; }
    public void setEtag(String value) { this.etag = value; }

    public String getNextPageToken() { return nextPageToken; }
    public void setNextPageToken(String value) { this.nextPageToken = value; }

    public String getRegionCode() { return regionCode; }
    public void setRegionCode(String value) { this.regionCode = value; }

    public PageInfo getPageInfo() { return pageInfo; }
    public void setPageInfo(PageInfo value) { this.pageInfo = value; }

    public Item[] getItems() { return items; }
    public void setItems(Item[] value) { this.items = value; }
}