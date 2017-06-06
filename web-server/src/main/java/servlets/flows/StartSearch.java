package servlets.flows;

import com.google.gson.Gson;
import db.api.UserManager;
import model.User;
import search.NewsFinder;
import search.RelevantNews;
import servlets.utils.NewsWorkshopUtils;
import servlets.utils.ServletUtils;
import servlets.utils.SessionUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

@WebServlet(name = "StartSearch")
public class StartSearch extends HttpServlet {
    private static final String HaveToAppear = "haveToAppear";
    private static final String CantAppear = "cantAppear";
    private static final String Queries = "queries";
    private static final String BlackListDomains = "blackListDomains";

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        PrintWriter out = response.getWriter();
        NewsWorkshopUtils newsWorkshop = ServletUtils.GetNewsWorkshopUtils(getServletContext());
        NewsFinder newsFinder = newsWorkshop.newsFinder;
        UserManager userMgr = newsWorkshop.userManager;

        String haveToAppearString = request.getParameter(HaveToAppear);
        //TODO: how to parse the array parameters?
        User user = userMgr.findUserById(SessionUtils.getUserId(request));
        ArrayList<String> haveToAppear = new ArrayList<>();
        ArrayList<String> cantAppear = new ArrayList<>();
        ArrayList<String> blacklistDomains = new ArrayList<>();
        ArrayList<String> queries = new ArrayList<>();

        newsFinder.Init(haveToAppear,cantAppear,blacklistDomains,queries,user);
        try
        {
            newsFinder.Start();
            List<RelevantNews> results = newsFinder.GetResults();
            String json = new Gson().toJson(results);
            response.setContentType("application/json");
            out.write(json);
            response.setStatus(HttpServletResponse.SC_OK);
        }
        catch (Exception e){
            response.getWriter().write(e.toString());
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
