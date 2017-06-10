package com.workshop.bing.model.search.results.result;

import com.workshop.bing.model.BingMetadata;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class Thumbnail {


    private BingMetadata metadata;
    private String mediaUrl;
    private String contentType;
    private Long width;
    private Long height;
    private Long fileSize;


    @JsonProperty("__metadata")
    public void setMetadata(BingMetadata metadata) {
        this.metadata = metadata;
    }

    @JsonProperty("MediaUrl")
    public void setMediaUrl(String mediaUrl) {
        this.mediaUrl = mediaUrl;
    }

    @JsonProperty("ContentType")
    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    @JsonProperty("Width")
    public void setWidth(Long width) {
        this.width = width;
    }

    @JsonProperty("Height")
    public void setHeight(Long height) {
        this.height = height;
    }

    @JsonProperty("FileSize")
    public void setFileSize(Long fileSize) {
        this.fileSize = fileSize;
    }


}
