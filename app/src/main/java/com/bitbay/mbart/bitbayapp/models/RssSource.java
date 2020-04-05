package com.bitbay.mbart.bitbayapp.models;

public class RssSource implements Comparable<RssSource> {
    String rssUrl;
    String rssSource;
    String rssLang;
    int issubscribe;

    public RssSource(String rssUrl, String rssSource, String rssLang, int issubscribe) {
        this.rssUrl = rssUrl;
        this.rssSource = rssSource;
        this.rssLang = rssLang;
        this.issubscribe = issubscribe;
    }

    public RssSource() {

    }

    public int getIssubscribe() {
        return issubscribe;
    }

    public void setIssubscribe(int issubscribe) {
        this.issubscribe = issubscribe;
    }

    public String getRssUrl() {
        return rssUrl;
    }

    public void setRssUrl(String rssUrl) {
        this.rssUrl = rssUrl;
    }

    public String getRssSource() {
        return rssSource;
    }

    public void setRssSource(String rssSource) {
        this.rssSource = rssSource;
    }

    public String getRssLang() {
        return rssLang;
    }

    public void setRssLang(String rssLang) {
        this.rssLang = rssLang;
    }

    @Override
    public int compareTo(RssSource o) {
        return getRssSource().compareTo(o.getRssSource());
    }
}
