package com.workshop.bing.model.search.results.converters;

import com.workshop.bing.BingUtils;
import com.workshop.bing.model.search.results.BingCognitiveNewsResponse;
import com.workshop.bing.model.search.results.BingCognitiveNewsResult;
import com.workshop.bing.model.search.results.BingWebResponseData;
import com.workshop.bing.model.search.results.result.BingNewsResponse;
import com.workshop.bing.model.search.results.result.BingNewsResult;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Slf4j
public class CognitiveTypeResponseToBingNewsResponseConverter {
    public BingNewsResponse convert(String query, BingCognitiveNewsResponse bingCognitiveNewsResponse) {
        BingNewsResponse newsResponse = new BingNewsResponse();
        newsResponse.setFromCache(false);

        BingWebResponseData<BingNewsResult> d = new BingWebResponseData<>();
        newsResponse.setData(d);

        d.setNext(null);
        List<BingNewsResult> results = new ArrayList<>();
        d.setResults(results);

        String error = null;
        if(bingCognitiveNewsResponse == null) {
            error = "Bing object returned null: bingCognitiveNewsResponse";
        } else if(StringUtils.isNotBlank(bingCognitiveNewsResponse.getError())) {
            error = bingCognitiveNewsResponse.getError();
        }

        if(StringUtils.isNotEmpty(error)) {
            newsResponse.setError(error);
            log.debug(error);
            return newsResponse;
        }

        List<BingCognitiveNewsResult> responseValues = null;
        try {
            responseValues = bingCognitiveNewsResponse.getValue();
        } catch (Exception e) {
            log.debug("Could not get values from bing news response", e);
        }

        if(responseValues == null) {
            log.debug("Bing object returned null: bingCognitiveNewsResponse --> Values");
            return newsResponse;
        }

        for(BingCognitiveNewsResult cognitiveNewsResult : responseValues) {
            BingNewsResult newsResult = new BingNewsResult();

            newsResult.setTitle(cognitiveNewsResult.getName());
            newsResult.setDescription(cognitiveNewsResult.getDescription());

            /*
             url handling - for backward compatiable
             1. url & display url will hold the actual url
             2. bing wrapped url will haled url that wrapped with bing - for example :

            https://www.bing.com/cr?IG=F6856E7ECA244C9F94998C76F812B817&CID=0D3AB5D8025F6CE01893BC3B03B86DF4&rd=1&h=nb0blFgV78Rc7uGR2TaQMBJ4V6s6HzxM7rpeax_XNHA&v=1&r=https%3a%2f%2fwww.google.com%2fabout%2fcompany%2ffacts%2f&p=DevEx,5105.1
              */

            String actualUrl = BingUtils.extractDisplaylUrl(cognitiveNewsResult.getUrl());
            newsResult.setUrl(actualUrl);
            newsResult.setDisplayUrl(actualUrl);
            newsResult.setBingWrappedUrl(cognitiveNewsResult.getUrl());

            if(CollectionUtils.isNotEmpty(cognitiveNewsResult.getProvider())) {
                newsResult.setSource(cognitiveNewsResult.getProvider().get(0).getName());
            }

            trySetDate(query, cognitiveNewsResult, newsResult);

            results.add(newsResult);
        }


        return newsResponse;
    }

    private void trySetDate(String query, BingCognitiveNewsResult cognitiveNewsResult, BingNewsResult newsResult) {
        Date parsedDate = null;
        String[] formats = {"yyyy-MM-dd'T'HH:mm:ss", "yyyy-MM-dd"};

        for (String format : formats) {
            try {
                String dateInput = cognitiveNewsResult.getDatePublished();
                SimpleDateFormat parser = new SimpleDateFormat(format);
                parsedDate = parser.parse(dateInput);
            } catch(Exception e) {
                String message = String.format("Could not parse date with format %s for query %s", format, query);
                log.debug(message, e);
            }
            if(parsedDate != null) {
                newsResult.setDate(parsedDate);
                return;
            }
        }
        String message = String.format("Error parsing date %s from bing news for query: %s", cognitiveNewsResult.getDatePublished(), query);
        log.info(message);
    }
}
