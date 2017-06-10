package com.workshop.bing.model.search;

import com.workshop.bing.model.search.results.BingCognitiveNewsResponse;
import com.workshop.bing.model.search.results.BingCognitiveSearchResponse;
import feign.RequestLine;
import lombok.extern.slf4j.Slf4j;

import javax.inject.Named;

@Slf4j
public class BingSearchAPIWrapper implements IBingSearchAPI {

    IBingSearchAPI inner;

    public BingSearchAPIWrapper(IBingSearchAPI inner) {
        this.inner = inner;
    }


    @RequestLine("GET /search?" +
            "q={query}&" +
            "mkt=en-us&" +
            "count={count}&" +
            "responseFilter=webpages")
    @Override
    public BingCognitiveSearchResponse searchWeb(@Named("query") String query, @Named("count") int count) {

        BingCognitiveSearchResponse bingWebResponse = null;
        try {
            bingWebResponse = inner.searchWeb(query, count);
            log.debug("Bing OnDemand Request - Web");
        } catch (Exception e) {
            log.info("Exception from Bing web endpoint", e);
            bingWebResponse = new BingCognitiveSearchResponse();
            bingWebResponse.setError("Exception from Bing web endpoint: " + e.getMessage());
        }

        return bingWebResponse;
    }

    @Override
    public BingCognitiveNewsResponse searchNews(@Named("query") String query, @Named("count") int count) {
        BingCognitiveNewsResponse bingNewsResponse = null;
        try {
            bingNewsResponse = inner.searchNews(query, count);
            log.debug("Bing OnDemand Request - News");
        } catch (Exception e) {
            log.info("Exception from Bing news endpoint", e);
            bingNewsResponse = new BingCognitiveNewsResponse();
            bingNewsResponse.setError("Exception from Bing news endpoint: " + e.getMessage());
        }

        return bingNewsResponse;

    }

    @Override
    public BingCognitiveNewsResponse searchNewsCategory(@Named("category") String category, @Named("count") int count) {

        BingCognitiveNewsResponse bingNewsResponse = null;
        try {
            bingNewsResponse = inner.searchNewsCategory(category, count);
            log.debug("Bing OnDemand Request - News Category");
        } catch (Exception e) {
            log.info("Exception from Bing news category endpoint", e);
            bingNewsResponse = new BingCognitiveNewsResponse();
            bingNewsResponse.setError("Exception from Bing news category endpoint: " + e.getMessage());
        }
        return bingNewsResponse;
    }
}
