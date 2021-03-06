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
import com.workshop.service.mail.MailComposer;
import com.workshop.service.mail.MailSender;
import freemarker.template.TemplateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.mail.MessagingException;
import javax.validation.Valid;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

@Controller
public class ResultsController {

    public static final String EMAIL_SUBJECT = "MyBuzz";
    @Autowired
    UserFeedbackService userFeedbackService;

    @Autowired
    SearchResultsService searchResultsService;

    @Autowired
    private UserService userService;

    @Autowired
    private MailComposer mailComposer;

    @Autowired
    private MailSender mailSender;

    @RequestMapping(value = "/results/view", method = RequestMethod.GET)
    public ModelAndView viewResultsTable(@Valid @ModelAttribute("resultsId") int resultsId,
                                         @Valid @ModelAttribute("userEmail") String userEmail) {
        ModelAndView modelAndView = new ModelAndView();

        List<RelevantNewsView> resultView = getRelevantNewsViewsById(resultsId);
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        boolean isAdmin = userService.isAdminByEmail(auth.getName());
        String resultsJson = searchResultsService.getResultsByResultsId(resultsId).getResults();

        modelAndView.addObject("results", resultView);
        modelAndView.addObject("isAdmin", isAdmin);
        modelAndView.addObject("userEmail", userEmail);
        modelAndView.addObject("resultsJson", resultsJson);
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
        return new ResponseEntity("Got " + activityType, HttpStatus.OK);
    }

    @RequestMapping(value = "/results/view/article", method = RequestMethod.GET)
    public ModelAndView viewArticle(@Valid @ModelAttribute("resultsId") int resultsId,
                                    @Valid @ModelAttribute("index") int index,
                                    @Valid @ModelAttribute("userEmail") String userEmail) {
        ModelAndView modelAndView = new ModelAndView();

        List<RelevantNewsView> resultView = getRelevantNewsViewsById(resultsId);

        modelAndView.addObject("article", resultView.get(index));
        modelAndView.addObject("userEmail", userEmail);
        modelAndView.setViewName("article");
        return modelAndView;
    }

    @RequestMapping(value = "/results/view/mail", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String previewMail(@Valid @ModelAttribute("resultsId") int resultsId,
                              @Valid @ModelAttribute("resultsJson") String resultsJson) throws IOException, TemplateException {
        List<RelevantNewsView> searchResults = getRelevantNewsViewsByJson(resultsJson);
        String mailHtml = mailComposer.ComposeMail(searchResults, EMAIL_SUBJECT, resultsId);
        return mailHtml;
    }

    @RequestMapping(value = "/results/sendemail", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<?> sendMail(@Valid @ModelAttribute("resultsId") int resultsId,
                                      @Valid @ModelAttribute("userEmail") String userEmail,
                                      @Valid @ModelAttribute("resultsJson") String resultsJson) throws IOException, TemplateException {
        List<RelevantNewsView> searchResults = getRelevantNewsViewsByJson(resultsJson);
        String mailHtml = mailComposer.ComposeMail(searchResults, "MyBuzz", resultsId);
        try {
            searchResultsService.saveResults(resultsId, resultsJson);
            mailSender.sendMail("mybuzzworkshop@gmail.com", userEmail, EMAIL_SUBJECT, mailHtml);
        } catch (MessagingException e) {
            e.printStackTrace();
            return new ResponseEntity<>(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>("Email was sent successfully!",HttpStatus.OK);
    }

    private List<RelevantNewsView> getRelevantNewsViewsById(@Valid @ModelAttribute("resultsId") int resultsId) {
        SearchResults searchResults = searchResultsService.getResultsByResultsId(resultsId);
        return getRelevantNewsViewsByJson(searchResults.getResults());
    }

    private List<RelevantNewsView> getRelevantNewsViewsByJson(String resultsJson) {
        Type listType = new TypeToken<ArrayList<RelevantNewsView>>() {
        }.getType();
        Gson gson = new Gson();
        return gson.fromJson(resultsJson, listType);
    }
}
