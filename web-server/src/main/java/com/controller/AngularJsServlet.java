package com.controller;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import com.google.gson.Gson;
import com.model.PersonData;

public class AngularJsServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    public AngularJsServlet() {
        super();
    }

    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response) throws ServletException, IOException {
        try {
            PersonData personData = new PersonData();
            personData.setFirstName("Mohaideen");
            personData.setLastName("Jamil");

            Gson gson = new Gson();
            String json = gson.toJson(personData);
            response.setContentType("application/json");
            response.getWriter().write(json);
        } catch (Exception e) {
            System.out.println(e.toString());
        }
    }
}