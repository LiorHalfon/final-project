package com.workshop.bing.model.search.results.converters;

import lombok.Data;

@Data
public class CognitiveToClassicBingConverter {

    CognitiveWebResponseToBingWebResponseConverter cognitiveWebResponseToBingWebResponseConverter;
    CognitiveTypeResponseToBingNewsResponseConverter cognitiveTypeResponseToBingNewsResponseConverter;

    public CognitiveToClassicBingConverter(CognitiveWebResponseToBingWebResponseConverter cognitiveWebResponseToBingWebResponseConverter,
                                           CognitiveTypeResponseToBingNewsResponseConverter cognitiveTypeResponseToBingNewsResponseConverter) {
        this.cognitiveWebResponseToBingWebResponseConverter = cognitiveWebResponseToBingWebResponseConverter;
        this.cognitiveTypeResponseToBingNewsResponseConverter = cognitiveTypeResponseToBingNewsResponseConverter;
    }
}
