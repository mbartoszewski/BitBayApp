package com.bitbay.mbart.bitbayapp.models;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

import java.util.Objects;

@Root(name = "item", strict = false)
public class RSSItem implements Comparable<RSSItem> {

    // All <item> node name
    @Element(name = "title", required = false)
    private String title;
    @Element(name = "link")
    private String link;
    @Element(name = "description")
    private String description;
    @Element(name = "pubDate")
    private String pubDate;
    @Element(name = "guid")
    private String guid;

    private String rssSourceUrl;
    private String source;

    public RSSItem() {
    }

    public String getRssSourceUrl() {
        return rssSourceUrl;
    }

    public void setRssSourceUrl(String rssSourceUrl) {
        this.rssSourceUrl = rssSourceUrl;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPubDate() {
        return pubDate;
    }

    public void setPubDate(String pubDate) {
        this.pubDate = pubDate;
    }

    public String getGuid() {
        return guid;
    }

    public void setGuid(String guid) {
        this.guid = guid;
    }

    @Override
    public int compareTo(RSSItem o) {
        return o.getPubDate().compareTo(getPubDate());
    }

    @Override
    public boolean equals(Object o) {
        if (this.link.equals(((RSSItem) o).link)) return true;
        if (!this.link.equals(((RSSItem) o).link)) return false;
        if (o == null || getClass() != o.getClass()) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hash(link);
    }
}

