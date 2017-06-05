package bing;

import bing.model.BingConsts;
import bing.model.properties.BingApiProperties;
import bing.model.properties.BingProperties;
import bing.model.search.BingSearchAPIService;
import bing.model.search.IBingSearchAPI;
import bing.model.search.results.*;
import bing.model.search.results.converters.CognitiveToClassicBingConverter;
import bing.model.search.results.result.BingArticleResult;
import bing.model.search.results.result.BingNewsResponse;
import bing.model.search.results.result.BingNewsResult;
import bing.model.search.results.result.BingWebResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

@Slf4j
public class BingLogic {

    @Autowired
    private BingSearchAPIService searchService;

    @Autowired
    private CognitiveToClassicBingConverter cognitiveToClassicBingConverter;

    @Autowired
    BingProperties bingProperties;

    @Autowired
    BingApiProperties bingApiProperties;

    public BingWebResponse searchWeb(String query) {
        BingWebResponse response = new BingWebResponse();

        return (BingWebResponse)searchBing(query, BingConsts.BING_QUERY_TYPES.WEB, response);
    }

    public BingNewsResponse searchNews(String query) {
        BingNewsResponse bingNewsResponse = new BingNewsResponse();

        return (BingNewsResponse)searchBing(query, BingConsts.BING_QUERY_TYPES.NEWS, bingNewsResponse);
    }

    public BingNewsResult emptyNewsResultExceptForUrl(BingNewsResult bingNewsResult) {
        if (bingNewsResult == null) return bingNewsResult;

        bingNewsResult.setTitle(null);
        bingNewsResult.setDescription(null);

        return bingNewsResult;
    }

    public BingNewsResponse searchBusinessNews() {

        BingCognitiveNewsResponse bingCognitiveNewsResultBingCognitiveResponse = searchService.getIBingSearchAPIWrapper().searchNewsCategory(BingNewsCategory.Business.name(), bingApiProperties.getDefaultBusinessNewsCount());

        BingNewsResponse bingNewsResponse = cognitiveToClassicBingConverter.getCognitiveTypeResponseToBingNewsResponseConverter().convert(null, bingCognitiveNewsResultBingCognitiveResponse);

        if (bingNewsResponse != null) {
            if (bingNewsResponse.getData() != null) {
                fillDisplayUrl(bingNewsResponse);
            } else {
                bingNewsResponse.setError("Exception was thrown from Bing Service");
                log.debug("got empty D in response from BusinessNews: [{]]", bingNewsResponse);
            }
        } else {
            log.debug("got null response from bing searching BusinessNews");
        }

        return bingNewsResponse;
    }

    private BingResponse searchBing(String query, BingConsts.BING_QUERY_TYPES queryType, BingResponse bingResponse) {

        BingWebResponseData responseData = null;
        //Perform ondemand to bing service
        bingResponse = getWebResponseData(queryType, query);

        boolean saveBingResultsToRepo = false;
        //Bing service threw an exception
        if (!responseIsValid(bingResponse) || bingResponse.getData() == null) {
            log.error(String.format("Ondemand with regular key failed for %s.", query));
            bingResponse.setError("Exception was thrown from Bing Service" + (bingResponse == null ? "" : bingResponse.getError()));
        } else {
            responseData = bingResponse.getData();
            if(CollectionUtils.isEmpty(responseData.getResults())) {
                responseData = new BingWebResponseData();
            }
            log.debug("BING REGULAR KEY USED - " + query);
            saveBingResultsToRepo = true;
        }

        bingResponse.setData(responseData);

        return bingResponse;
    }

    public BingResponse<? extends BingArticleResult> getWebResponseData(BingConsts.BING_QUERY_TYPES type, String query) {


        BingResponse<? extends BingArticleResult> bingResponse = sendWebRequestToBing(query, type);


        fillDisplayUrl(bingResponse);


        return bingResponse;
    }

    BingResponse<? extends BingArticleResult> sendWebRequestToBing(String query, BingConsts.BING_QUERY_TYPES type) {

        IBingSearchAPI iBingSearchAPIWrapper = searchService.getIBingSearchAPIWrapper();

        BingResponse<? extends BingArticleResult> bingResponse = callBing(query, type, iBingSearchAPIWrapper);

        return bingResponse;
    }


    BingResponse<? extends BingArticleResult> callBing(String query, BingConsts.BING_QUERY_TYPES type, IBingSearchAPI iBingSearchAPIWrapper) {

        BingResponse<? extends BingArticleResult> bingResponse = null;

        if (type.equals(BingConsts.BING_QUERY_TYPES.NEWS)) {
            BingCognitiveNewsResponse bingCognitiveNewsResponse = iBingSearchAPIWrapper.searchNews(query, bingApiProperties.getDefaultNewsCount());
            bingResponse = cognitiveToClassicBingConverter.getCognitiveTypeResponseToBingNewsResponseConverter().convert(query, bingCognitiveNewsResponse);
        } else if (type.equals(BingConsts.BING_QUERY_TYPES.WEB)) {
            BingCognitiveSearchResponse bingCognitiveSearchResponse = iBingSearchAPIWrapper.searchWeb(query, bingApiProperties.getDefaultWebCount());
            bingResponse = cognitiveToClassicBingConverter.getCognitiveWebResponseToBingWebResponseConverter().convert(bingCognitiveSearchResponse);
        }

        return bingResponse;
    }
    boolean responseIsValid(BingResponse<? extends BingArticleResult> bingResponse) {
        return bingResponse != null &&
                StringUtils.isBlank(bingResponse.getError());
    }

    private void fillDisplayUrl(BingResponse<? extends BingArticleResult> bingResponse) {

        if (responseIsValid(bingResponse)) {
            fillDisplayUrl(bingResponse.getData());
        }
    }

    private <T extends BingArticleResult> void fillDisplayUrl(BingWebResponseData<T> bingWebResponseData) {
        if (bingWebResponseData != null && CollectionUtils.isNotEmpty(bingWebResponseData.getResults())) {
            bingWebResponseData.getResults().forEach(r -> {
                if (StringUtils.isBlank(r.getDisplayUrl()) && StringUtils.isNotBlank(r.getUrl())) {
                    r.setDisplayUrl(BingUtils.extractDisplaylUrl(r.getUrl()));
                }
            });
        }
    }
}
