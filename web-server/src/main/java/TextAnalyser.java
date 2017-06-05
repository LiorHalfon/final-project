import com.aylien.textapi.TextAPIClient;
import com.aylien.textapi.TextAPIException;
import com.aylien.textapi.parameters.ClassifyByTaxonomyParams;
import com.aylien.textapi.parameters.ClassifyParams;
import com.aylien.textapi.parameters.SentimentParams;
import com.aylien.textapi.responses.Classifications;
import com.aylien.textapi.responses.Sentiment;
import com.aylien.textapi.responses.TaxonomyClassifications;

import java.net.URL;

public class TextAnalyser {
    private final String _appId = "b5c4e688";
    private final String _applicationKey = "5d5fb206e2803c3a7e117f596c0fbe79";
    private final int _tweetThreshold = 140;
    private TextAPIClient _client;

    public TextAnalyser() {
        _client = new TextAPIClient(_appId, _applicationKey);
    }

    public Classifications ClassifyUrl(URL url) throws TextAPIException {
        ClassifyParams.Builder builder = ClassifyParams.newBuilder();
        builder.setUrl(url);
        return _client.classify(builder.build());
    }

    public TaxonomyClassifications ClassifyUrlByTaxonomy(URL url) throws TextAPIException {
        ClassifyByTaxonomyParams.Builder builder = ClassifyByTaxonomyParams.newBuilder();
        builder.setUrl(url);
        builder.setTaxonomy(ClassifyByTaxonomyParams.StandardTaxonomy.IAB_QAG);
        return _client.classifyByTaxonomy(builder.build());
    }

    public Sentiment GetSentiment(String text) throws TextAPIException {
        SentimentParams.Builder builder = SentimentParams.newBuilder();
        builder.setText(text);

        if (text.length() <= _tweetThreshold)
            builder.setMode("tweet");
        else
            builder.setMode("document");

        return _client.sentiment(builder.build());
    }
}
