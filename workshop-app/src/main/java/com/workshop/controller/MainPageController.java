package com.workshop.controller;

import com.workshop.model.User;
import com.workshop.model.UserFeedback;
import com.workshop.search.BingNewsFinder;
import com.workshop.search.NewsFinder;
import com.workshop.search.RelevantNews;
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
                if (activity.getActivityType().equals(UserFeedback.ActivityType.BLOCK_DOMAIN.toString())) {
                    blackListDomains.add(activity.getUrl());
                }
            }
        }

        NewsFinder newsFinder = new BingNewsFinder();
        newsFinder.Init(haveToAppear,cantAppear,blackListDomains,queries,user);
        try {
            newsFinder.Start();
        } catch (Exception e) {
            e.printStackTrace();
        }

        List<RelevantNews> results = newsFinder.GetResults();
        modelAndView.setViewName("home");

        return modelAndView;
    }

    @RequestMapping(value = "/home/feedback", method = RequestMethod.POST)
    public ModelAndView feedback(@Valid @ModelAttribute("activityType") String activityType) {
        ModelAndView modelAndView = new ModelAndView();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByEmail(auth.getName());
        //TODO: set the url value from the article.url
        userFeedbackService.sendFeedback(user, UserFeedback.ActivityType.valueOf(activityType), "http://www.yahoo.com");
        modelAndView.setViewName("home");
        return modelAndView;
    }
}
