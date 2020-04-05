package com.bitbay.mbart.bitbayapp.models.youTubeChannelVideo;

import java.util.Map;

public class Thumbnails {
    private Default thumbnailsDefault;
    private Default medium;
    private Default high;

    public Default getThumbnailsDefault() { return thumbnailsDefault; }
    public void setThumbnailsDefault(Default value) { this.thumbnailsDefault = value; }

    public Default getMedium() { return medium; }
    public void setMedium(Default value) { this.medium = value; }

    public Default getHigh() { return high; }
    public void setHigh(Default value) { this.high = value; }
}