package bing.model.search;

import bing.model.search.results.BingCognitiveNewsResponse;
import bing.model.search.results.BingCognitiveSearchResponse;
import feign.RequestLine;

import javax.inject.Named;

// see doc here: https://datamarket.azure.com/dataset/bing/search#schema
// Host base url is : https://api.cognitive.microsoft.com/bing/v5.0
public interface IBingSearchAPI {

    @RequestLine("GET /search?" +
            "q={query}&" +
            "mkt=en-us&" +
            "count={count}&" +
            "responseFilter=webpages")
    BingCognitiveSearchResponse searchWeb(@Named("query") String query, @Named("count") int count);

    @RequestLine("GET /news/search?" +
            "q={query}&" +
            "mkt=en-us&" +
            "count={count}")
    BingCognitiveNewsResponse searchNews(@Named("query") String query, @Named("count") int count);

    @RequestLine("GET /news?" +
            //"q=&" +  // Do not include this parameter if you're getting news articles by category.
            "category={category}&" +
            "mkt=en-us&" +
            "count={count}")
    BingCognitiveNewsResponse searchNewsCategory(@Named("category") String category, @Named("count") int count);
}
