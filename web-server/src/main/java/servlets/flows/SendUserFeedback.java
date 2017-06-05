package servlets.flows;

import db.api.UserFeedbackManager;
import db.api.UserManager;
import model.User;
import model.UserFeedback;
import servlets.utils.NewsWorkshopUtils;
import servlets.utils.ServletUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "SendUserFeedback", urlPatterns = {"/sendUserFeedback"})
public class SendUserFeedback extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        NewsWorkshopUtils newsWorkshopUtils = ServletUtils.GetNewsWorkshopUtils(getServletContext());
        UserFeedbackManager userfeedbackMgr = newsWorkshopUtils.userFeedbackManager;
        UserManager userMgr = newsWorkshopUtils.userManager;

        //TODO: extract user id/email from request
        User user = userMgr.findUserById("1");

        userfeedbackMgr.sendUserFeedback(user, UserFeedback.ActivityType.BLOCK_DOMAIN);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request,response);
    }
}
