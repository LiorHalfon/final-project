package bing.model.search.results.result;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class BingImageResult extends BingSimpleResult{



    private String mediaUrl;
    private String sourceUrl;
    private Long width;
    private Long height;
    private Long fileSize;
    private String contentType;
    private Thumbnail thumbnail;


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


    @JsonProperty("SourceUrl")
    public void setSourceUrl(String sourceUrl) {
        this.sourceUrl = sourceUrl;
    }

    @JsonProperty("Thumbnail")
    public void setThumbnail(Thumbnail thumbnail) {
        this.thumbnail = thumbnail;
    }
}
