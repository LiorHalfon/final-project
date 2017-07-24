package com.workshop.controller;

import com.workshop.model.User;
import com.workshop.search.*;
import com.workshop.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;

/**
 * Created by ndayan on 10/06/2017.
 */
@Controller
public class MainPageController {

    @Autowired
    private UserService userService;

    @Autowired
    private ThreadPoolTaskExecutor taskExecutor;

    @Autowired
    private SearchThread searchThread;

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

        searchThread.setQueryValueParam(queryValueParam);
        searchThread.setHaveToAppearParam(haveToAppearParam);
        searchThread.setCantAppearParam(cantAppearParam);
        searchThread.setUser(user);

        taskExecutor.execute(searchThread);

        modelAndView.setViewName("search-started");

        return modelAndView;
    }
}
