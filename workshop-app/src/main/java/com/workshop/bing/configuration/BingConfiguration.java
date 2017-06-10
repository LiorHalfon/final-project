package com.workshop.bing.configuration;

import com.workshop.bing.BingLogic;
import com.workshop.bing.controllers.BingController;
import com.workshop.bing.model.properties.BingApiProperties;
import com.workshop.bing.model.properties.BingParallelProperties;
import com.workshop.bing.model.properties.BingProperties;
import com.workshop.bing.model.search.BingSearchAPIService;
import com.workshop.bing.model.search.results.converters.CognitiveToClassicBingConverter;
import com.workshop.bing.model.search.results.converters.CognitiveTypeResponseToBingNewsResponseConverter;
import com.workshop.bing.model.search.results.converters.CognitiveWebResponseToBingWebResponseConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@ComponentScan
@Configuration
public class BingConfiguration {

    @Bean
    @ConfigurationProperties("bing.keys")
    public BingProperties bingProperties() {
        return new BingProperties();
    }

    @Bean
    @ConfigurationProperties("bing.parallel")
    public BingParallelProperties parallelProperties() {
        return new BingParallelProperties();
    }

    @Bean
    @ConfigurationProperties("bing.api")
    public BingApiProperties bingApiProperties() {
        return new BingApiProperties();
    }

    @Bean
    @Autowired
    public BingSearchAPIService bingSearchAPIService(BingProperties properties) {
        return new BingSearchAPIService(properties);
    }

    @Bean
    public BingLogic bingLogic() {
        return new BingLogic();
    }


    // Converters from new API.
    @Bean
    public CognitiveToClassicBingConverter cognitiveToClassicBingConverter() {
        return new CognitiveToClassicBingConverter(cognitiveWebResponseToBingWebResponseConverter(), cognitiveTypeResponseToBingNewsResponseConverter());
    }

    @Bean
    public CognitiveWebResponseToBingWebResponseConverter cognitiveWebResponseToBingWebResponseConverter() {
        return new CognitiveWebResponseToBingWebResponseConverter();
    }

    @Bean
    public CognitiveTypeResponseToBingNewsResponseConverter cognitiveTypeResponseToBingNewsResponseConverter() {
        return new CognitiveTypeResponseToBingNewsResponseConverter();
    }

    @Bean
    public BingController bingController() {
        return  new BingController();
    }
}
