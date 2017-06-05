package bing.model.search;

import bing.FeignContract;
import bing.model.properties.BingProperties;
import feign.Feign;
import feign.Logger;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import feign.gson.GsonDecoder;
import lombok.Data;

@Data
public class BingSearchAPIService {

    private final BingProperties bingProperties;

    private IBingSearchAPI iBingSearchApi;

    public BingSearchAPIService(BingProperties bingProperties) {
        this.bingProperties = bingProperties;
    }

    public IBingSearchAPI getIBingSearchAPIWrapper() {
        if (iBingSearchApi == null) {
            iBingSearchApi = bingServiceCreator(bingProperties.getSearchKey());
        }
        return iBingSearchApi;
    }

    public IBingSearchAPI bingServiceCreator(final String bingkey) {
        String hostBase = "https://api.cognitive.microsoft.com/bing/v5.0";

        return new BingSearchAPIWrapper(
                Feign.builder().logLevel(Logger.Level.FULL)
                        .contract(new FeignContract())
                        .decoder(new GsonDecoder())
                        .requestInterceptor(new RequestInterceptor() {
                            @Override
                            public void apply(RequestTemplate requestTemplate) {
                                requestTemplate.header("Ocp-Apim-Subscription-Key", bingkey);
                            }
                        })
                        .target(IBingSearchAPI.class, hostBase)
        );
    }
}
