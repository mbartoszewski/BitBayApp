package com.bitbay.mbart.bitbayapp.models.youTubeChannelVideo;

import java.util.Map;

public class Snippet {
    private String publishedAt;
    private String channelID;
    private String title;
    private String description;
    private Thumbnails thumbnails;
    private String channelTitle;
    private String liveBroadcastContent;

    public String getPublishedAt() { return publishedAt; }
    public void setPublishedAt(String value) { this.publishedAt = value; }

    public String getChannelID() { return channelID; }
    public void setChannelID(String value) { this.channelID = value; }

    public String getTitle() { return title; }
    public void setTitle(String value) { this.title = value; }

    public String getDescription() { return description; }
    public void setDescription(String value) { this.description = value; }

    public Thumbnails getThumbnails() { return thumbnails; }
    public void setThumbnails(Thumbnails value) { this.thumbnails = value; }

    public String getChannelTitle() { return channelTitle; }
    public void setChannelTitle(String value) { this.channelTitle = value; }

    public String getLiveBroadcastContent() { return liveBroadcastContent; }
    public void setLiveBroadcastContent(String value) { this.liveBroadcastContent = value; }
}