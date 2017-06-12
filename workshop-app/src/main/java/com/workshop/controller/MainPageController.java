package com.workshop.controller;

import com.aylien.textapi.responses.Article;
import com.aylien.textapi.responses.Sentiment;
import com.aylien.textapi.responses.Summarize;
import com.aylien.textapi.responses.TaxonomyClassifications;
import com.workshop.model.User;
import com.workshop.model.UserFeedback;
import com.workshop.search.BingNewsFinder;
import com.workshop.search.NewsFinder;
import com.workshop.search.RelevantNews;
import com.workshop.search.RelevantNewsView;
import com.workshop.service.UserFeedbackService;
import com.workshop.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by ndayan on 10/06/2017.
 */
@Controller
public class MainPageController {

    @Autowired
    UserFeedbackService userFeedbackService;

    @Autowired
    private UserService userService;

    @RequestMapping(value="/home", method = RequestMethod.GET)
    public ModelAndView home(){
        ModelAndView modelAndView = new ModelAndView();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByEmail(auth.getName());
        modelAndView.addObject("userName", "Welcome " + user.getName() + " " + user.getLastName() + " (" + user.getEmail() + ")");
        modelAndView.addObject("adminMessage","Content Available Only for Users with Admin Role");
        modelAndView.setViewName("home");
        return modelAndView;
    }

    @RequestMapping(value="/home/search", method = RequestMethod.GET)
    public ModelAndView home(@Valid @ModelAttribute("haveToAppear") String haveToAppearParam,
                             @Valid @ModelAttribute("cantAppear") String cantAppearParam,
                             @Valid @ModelAttribute("queryValue") String queryValueParam) {
        ModelAndView modelAndView = new ModelAndView();

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByEmail(auth.getName());

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
        Boolean useRealWebSearch = true;

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
            resultView.add(new RelevantNewsView(news));
        }

        modelAndView.addObject("results", resultView);
        modelAndView.setViewName("results");

        return modelAndView;
    }

    //To Test server without really searching:
    private List<RelevantNews> GetMockResults() {
        RelevantNews tempRes;
        try {
            Summarize summ = new Summarize();
            summ.setSentences(new String[]{"hello", "lior", "this is my test sentence"});
            Sentiment sentiment = new Sentiment();
            sentiment.setPolarity("positive");
            sentiment.setPolarityConfidence(0.856248);
            URL url = new URL("http://google.com");
            TaxonomyClassifications classifi = new TaxonomyClassifications();
            Article article = new Article();
            article.setTitle("Article Title");
            tempRes = new RelevantNews(summ, sentiment, url, classifi,article);
        }
        catch (MalformedURLException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
        List<RelevantNews> results = new ArrayList<>();
        results.add(tempRes);

        return results;
    }
}
