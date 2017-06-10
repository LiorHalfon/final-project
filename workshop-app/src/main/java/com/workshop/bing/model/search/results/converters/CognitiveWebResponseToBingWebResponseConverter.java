package com.workshop.bing.model.search.results.converters;

import com.workshop.bing.BingUtils;
import com.workshop.bing.model.search.results.BingCognitiveSearchResponse;
import com.workshop.bing.model.search.results.BingCognitiveWebPageResponse;
import com.workshop.bing.model.search.results.BingCognitiveWebResult;
import com.workshop.bing.model.search.results.BingWebResponseData;
import com.workshop.bing.model.search.results.result.BingWebResponse;
import com.workshop.bing.model.search.results.result.BingWebResult;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class CognitiveWebResponseToBingWebResponseConverter {
    public BingWebResponse convert(BingCognitiveSearchResponse bingCognitiveSearchResponse) {
        BingWebResponse bingWebResponse = new BingWebResponse();

        bingWebResponse.setFromCache(false);
        BingWebResponseData<BingWebResult> d = new BingWebResponseData<>();
        bingWebResponse.setData(d);

        d.setNext(null);
        List<BingWebResult> webResults = new ArrayList<>();
        d.setResults(webResults);

        String error = null;
        if(bingCognitiveSearchResponse == null) {
            error = "Bing object returned null: bingCognitiveSearchResponse";
        } else if(StringUtils.isNotBlank(bingCognitiveSearchResponse.getError())) {
            error = bingCognitiveSearchResponse.getError();
        }

        if(StringUtils.isNotEmpty(error)) {
            bingWebResponse.setError(error);
            return bingWebResponse;
        }

        BingCognitiveWebPageResponse response = null;
        try {
            response = bingCognitiveSearchResponse.getWebPages();
        } catch (Exception e) {
            log.debug("Could not get web pages from response", e);
        }

        if(response == null || response.getValue() == null) {
            log.debug("Bing object returned null: bingWebPages");
            return bingWebResponse;
        }

        for (BingCognitiveWebResult cognitiveWebResult : bingCognitiveSearchResponse.getWebPages().getValue()) {
            BingWebResult webResult = new BingWebResult();
            webResult.setId(cognitiveWebResult.getId());

             /*
             url handling - for backward compatible
             1. url & display url will hold the actual url
             2. bing wrapped url will haled url that wrapped with bing - for example :

            https://www.bing.com/cr?IG=F6856E7ECA244C9F94998C76F812B817&CID=0D3AB5D8025F6CE01893BC3B03B86DF4&rd=1&h=nb0blFgV78Rc7uGR2TaQMBJ4V6s6HzxM7rpeax_XNHA&v=1&r=https%3a%2f%2fwww.google.com%2fabout%2fcompany%2ffacts%2f&p=DevEx,5105.1
              */
            webResult.setBingWrappedUrl(cognitiveWebResult.getUrl());
            String actualUrl = BingUtils.extractDisplaylUrl(cognitiveWebResult.getUrl());
            webResult.setUrl(actualUrl);
            webResult.setDisplayUrl(cognitiveWebResult.getDisplayUrl());


            webResult.setTitle(cognitiveWebResult.getName());
            webResult.setDescription(cognitiveWebResult.getSnippet());
            // webResult.setMetadata();

            webResults.add(webResult);
        }

        return bingWebResponse;
    }
}
