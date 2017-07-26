package com.workshop.controller;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.workshop.model.SearchResults;
import com.workshop.model.User;
import com.workshop.model.UserFeedback;
import com.workshop.search.RelevantNewsView;
import com.workshop.service.SearchResultsService;
import com.workshop.service.UserFeedbackService;
import com.workshop.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

@Controller
public class ResultsController {

    @Autowired
    UserFeedbackService userFeedbackService;

    @Autowired
    SearchResultsService searchResultsService;

    @Autowired
    private UserService userService;

    @RequestMapping(value = "/results/view", method = RequestMethod.GET)
    public ModelAndView  viewResultsTable(@Valid @ModelAttribute("resultsId") int resultsId) {
        ModelAndView modelAndView = new ModelAndView();

        SearchResults searchResults = searchResultsService.getResultsByResultsId(resultsId);

        Type listType = new TypeToken<ArrayList<RelevantNewsView>>(){}.getType();
        Gson gson = new Gson();
        List<RelevantNewsView> resultView = gson.fromJson(searchResults.getResults(), listType);

        modelAndView.addObject("results", resultView);
        modelAndView.setViewName("results");
        return modelAndView;
    }

    @RequestMapping(value = "/results/feedback", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity feedback(@Valid @ModelAttribute("activityType") String activityType,
                                   @Valid @ModelAttribute("url") String url) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByEmail(auth.getName());
        userFeedbackService.sendFeedback(user, UserFeedback.ActivityType.valueOf(activityType), url);
        return new ResponseEntity("Got " +activityType ,HttpStatus.OK);
    }
}
