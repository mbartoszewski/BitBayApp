package com.bitbay.mbart.bitbayapp.models.youTubeChannelVideo;

import java.util.Map;

public class Item {
    private String kind;
    private String etag;
    private ID id;
    private Snippet snippet;

    public String getKind() { return kind; }
    public void setKind(String value) { this.kind = value; }

    public String getEtag() { return etag; }
    public void setEtag(String value) { this.etag = value; }

    public ID getID() { return id; }
    public void setID(ID value) { this.id = value; }

    public Snippet getSnippet() { return snippet; }
    public void setSnippet(Snippet value) { this.snippet = value; }
}