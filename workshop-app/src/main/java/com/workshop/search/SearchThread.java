package com.workshop.search;

import com.aylien.textapi.responses.Article;
import com.aylien.textapi.responses.Sentiment;
import com.aylien.textapi.responses.Summarize;
import com.aylien.textapi.responses.TaxonomyClassifications;
import com.google.gson.Gson;
import com.workshop.service.SearchResultsService;
import com.workshop.service.mail.MailComposer;
import com.workshop.model.User;
import com.workshop.model.UserFeedback;
import com.workshop.service.UserFeedbackService;
import com.workshop.service.UserService;
import com.workshop.service.mail.MailSender;
import de.svenjacobs.loremipsum.LoremIpsum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

@Component
@Scope("prototype")
public class SearchThread extends Thread {

    @Autowired
    private UserService userService;

    @Autowired
    private UserFeedbackService userFeedbackService;

    @Autowired
    private SearchResultsService searchResultsService;

    @Autowired
    private MailSender mailSender;

    @Autowired
    private MailComposer mailComposer;

    private String haveToAppearParam;
    private String cantAppearParam;
    private String queryValueParam;
    private User user;
    private int resultsId;
//
//    public SearchThread(User user, String haveToAppearParam, String cantAppearParam, String queryValueParam) {
//        this.haveToAppearParam = haveToAppearParam;
//        this.cantAppearParam = cantAppearParam;
//        this.queryValueParam = queryValueParam;
//        this.user = user;
//    }

    @Override
    public void run() {

        String[] data = haveToAppearParam.split(",");
        ArrayList<String> haveToAppear = new ArrayList<>(Arrays.asList(data));
        haveToAppear.remove("");

        data = cantAppearParam.split(",");
        ArrayList<String> cantAppear = new ArrayList<>(Arrays.asList(data));
        cantAppear.remove("");

        data = queryValueParam.split(",");
        ArrayList<String> queries = new ArrayList<>(Arrays.asList(data));
        queries.remove("");

        ArrayList<String> blackListDomains = new ArrayList<>();
        List<UserFeedback> userFB = userFeedbackService.getUserFeedbacks(user, UserFeedback.ActivityType.BLOCK_DOMAIN);
        if (userFB != null) {
            for (UserFeedback activity : userFB) {
                if (activity.getActivityType().equals(UserFeedback.ActivityType.BLOCK_DOMAIN)) {
                    blackListDomains.add(activity.getUrl());
                }
            }
        }
        List<RelevantNews> results;
        Boolean useRealWebSearch = false;

        if(useRealWebSearch) {
            NewsFinder newsFinder = new BingNewsFinder();
            newsFinder.Init(haveToAppear, cantAppear, blackListDomains, queries, user);
            try {
                newsFinder.Start();
            } catch (Exception e) {
                e.printStackTrace();
            }

            results = newsFinder.GetResults();
        }
        else{
            results = GetMockResults();
        }

        List<RelevantNewsView> resultView = new ArrayList<>();
        for (RelevantNews news : results) {
            resultView.add(new RelevantNewsView(news, resultsId));
        }

        Gson gson = new Gson();
        String json = gson.toJson(resultView);
        searchResultsService.saveResults(resultsId, json);

        //Digest mail from the results and dump it to a file
        try {
            String text = mailComposer.ComposeMail(resultView, "MyBuzz", resultsId);
            mailSender.sendMail("mybuzzworkshop@gmail.com", user.getEmail(), "subject", text);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setHaveToAppearParam(String haveToAppearParam) {
        this.haveToAppearParam = haveToAppearParam;
    }

    public void setCantAppearParam(String cantAppearParam) {
        this.cantAppearParam = cantAppearParam;
    }

    public void setQueryValueParam(String queryValueParam) {
        this.queryValueParam = queryValueParam;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setResultsId(int resultsId) { this.resultsId = resultsId; }

    //To Test server without really searching:
    private List<RelevantNews> GetMockResults() {
        RelevantNews firstRes;

        Summarize summ = new Summarize();
        summ.setSentences(new String[]{
                "Every time Apple's developer conference rolls around we get a smattering of changes to the App Store Review guidelines.",
                "This corpus of rules can be, in turns, opaque and explicit, and has caused a decent amount of consternation over the years for developers as they try to read into how Apple might interpret one rule or another."
        });
        Sentiment sentiment = new Sentiment();
        sentiment.setPolarity("Positive");
        sentiment.setPolarityConfidence(0.856248);
        URL url = null;
        try {
            url = new URL("https://techcrunch.com/2017/06/21/apple-goes-after-clones-and-spam-on-the-app-store/");
        }
        catch (MalformedURLException e) {
            e.printStackTrace();
        }
        TaxonomyClassifications classifications = new TaxonomyClassifications();
        Article article = new Article();
        article.setTitle("Apple goes after clones and spam on the App Store");
        article.setImages(new String[]{"https://tctechcrunch2011.files.wordpress.com/2017/06/apple-liveblog0706.jpg?w=764&h=400&crop=1"});
        firstRes = new RelevantNews(summ, sentiment, url, classifications,article);

        List<RelevantNews> results = new ArrayList<>();
        results.add(firstRes);
        for (int i=0;i<9;i++){
            results.add(GenerateMockResults());
        }

        return results;
    }

    private RelevantNews GenerateMockResults() {
        LoremIpsum loremIpsum = new LoremIpsum();
        Random rand = new Random();


        Summarize summ = new Summarize();
        summ.setSentences(new String[]{
                loremIpsum.getWords(rand.nextInt(20) + 10),
                loremIpsum.getWords(rand.nextInt(20) + 10)
        });
        Sentiment sentiment = new Sentiment();
        sentiment.setPolarity(rand.nextInt(2) == 1 ?"Positive": "Negative");
        sentiment.setPolarityConfidence(rand.nextFloat());
        URL url = null;
        try {
            url = new URL("https://techcrunch.com/2017/06/21/apple-goes-after-clones-and-spam-on-the-app-store/");
        }
        catch (MalformedURLException e) {
            e.printStackTrace();
        }
        TaxonomyClassifications classifications = new TaxonomyClassifications();
        Article article = new Article();
        article.setTitle(loremIpsum.getWords(8));
        article.setImages(new String[]{"http://loremflickr.com/320/240/business"});
        return new RelevantNews(summ, sentiment, url, classifications,article);

    }
}
