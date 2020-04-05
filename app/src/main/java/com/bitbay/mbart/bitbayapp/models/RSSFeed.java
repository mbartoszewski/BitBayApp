package com.bitbay.mbart.bitbayapp.models;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Path;
import org.simpleframework.xml.Root;
import org.simpleframework.xml.Text;

import java.util.List;

@Root(name = "rss", strict = false)
public class RSSFeed
{
    public RSSFeed() {
    }

    @Element(name = "title", required = false)
    private String title;
    @Element(name = "channel", required = false)
    private String channel;
    @Path("link")
    @Text(required=false)
    private String link;
    @Element(name = "language", required = false)
    private String language;
    @Element(name = "description", required = false)
    private String description;
    @Element(name = "lastBuildDate", required = false)
    private String lastBuildDate;
    @ElementList(inline = true, required = false)
    private List<RSSItem> item;


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLastBuildDate() {
        return lastBuildDate;
    }

    public void setLastBuildDate(String lastBuildDate) {
        this.lastBuildDate = lastBuildDate;
    }

    public List<RSSItem> getItem() {
        return item;
    }

    public void setItem(List<RSSItem> item) {
        this.item = item;
    }
}


