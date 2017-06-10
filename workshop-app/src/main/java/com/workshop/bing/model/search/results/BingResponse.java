package com.workshop.bing.model.search.results;

import lombok.Data;

@Data
public abstract class BingResponse<T>  {

    String error;
    boolean fromCache;
    protected BingWebResponseData<T> data;
}
