package com.workshop.bing.controllers;

import com.workshop.bing.BingLogic;
import com.workshop.bing.model.search.results.result.BingNewsResponse;
import com.workshop.bing.model.search.results.result.BingNewsResult;
import com.workshop.bing.model.search.results.result.BingWebResponse;
import groovy.util.logging.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@Slf4j
public class BingController {

    @Autowired
    private BingLogic bingLogic;

    public BingNewsResponse searchNews(String query) {

       return bingLogic.searchNews(query);
    }

    public BingWebResponse searchWeb(String query) {

        return bingLogic.searchWeb(query);
    }

    public BingNewsResponse searchBusinessNews() {

        return bingLogic.searchBusinessNews();
    }

    public BingNewsResponse searchBusinessNewsOnlyUrls() {
        BingNewsResponse bingNewsResponse = bingLogic.searchBusinessNews();

        List<BingNewsResult> results = bingNewsResponse.getData() == null ? null : bingNewsResponse.getData().getResults();

        if (!results.isEmpty()) {
            List<BingNewsResult> validResults = results.stream()
                    .filter(r -> r!= null)
                    .map(r -> bingLogic.emptyNewsResultExceptForUrl((BingNewsResult) r))
                    .collect(Collectors.toList());
            bingNewsResponse.getData().setResults(validResults);
        }

        return bingNewsResponse;
    }
}

