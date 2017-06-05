package bing.controllers;

import bing.BingLogic;
import bing.model.search.results.result.BingNewsResponse;
import bing.model.search.results.result.BingNewsResult;
import bing.model.search.results.result.BingWebResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
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

        if (CollectionUtils.isNotEmpty(results)) {
            List<BingNewsResult> validResults = results.stream()
                    .filter(r -> r!= null)
                    .map(r -> bingLogic.emptyNewsResultExceptForUrl((BingNewsResult) r))
                    .collect(Collectors.toList());
            bingNewsResponse.getData().setResults(validResults);
        }

        return bingNewsResponse;
    }
}

