package servlets.flows;

import db.api.UserManager;
import model.User;
import search.NewsFinder;
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
import java.util.Arrays;

@WebServlet(name = "StartSearch", urlPatterns = {"/startSearch"})
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

        User user = userMgr.findUserById(SessionUtils.getUserId(request));

        String[] data = request.getParameter(HaveToAppear).split(",");
        ArrayList<String> haveToAppear = new ArrayList<>(Arrays.asList(data));
        haveToAppear.remove("");

        data = request.getParameter(CantAppear).split(",");
        ArrayList<String> cantAppear = new ArrayList<>(Arrays.asList(data));
        cantAppear.remove("");

        data = request.getParameter(Queries).split(",");
        ArrayList<String> queries = new ArrayList<>(Arrays.asList(data));
        queries.remove("");

        data = request.getParameter(BlackListDomains).split(",");
        ArrayList<String> blacklistDomains = new ArrayList<>(Arrays.asList(data));
        blacklistDomains.remove("");

        newsFinder.Init(haveToAppear,cantAppear,blacklistDomains,queries,user);
        try
        {
            newsFinder.Start();
            //List<RelevantNews> results = newsFinder.GetResults();
            //String json = new Gson().toJson(results);
            response.setContentType("text/plain; charset=ISO-8859-1");
            out.write(newsFinder.PrintResults());
            response.setStatus(HttpServletResponse.SC_OK);
        }
        catch (Exception e){
            response.getWriter().write(e.toString());
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request,response);
    }
}
