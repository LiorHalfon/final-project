package com.workshop.controller;

import com.workshop.model.User;
import com.workshop.model.UserFeedback;
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

/**
 * Created by ndayan on 10/06/2017.
 */
@Controller
public class MainPageController {

    @Autowired
    UserFeedbackService userFeedbackService;

    @Autowired
    private UserService userService;

    @RequestMapping(value = "/admin/home/feedback", method = RequestMethod.POST)
    public ModelAndView feedback(@Valid @ModelAttribute("activityType") String activityType) {
        ModelAndView modelAndView = new ModelAndView();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByEmail(auth.getName());
        userFeedbackService.sendFeedback(user, UserFeedback.ActivityType.valueOf(activityType));
        modelAndView.setViewName("admin/home");
        return modelAndView;
    }
}
