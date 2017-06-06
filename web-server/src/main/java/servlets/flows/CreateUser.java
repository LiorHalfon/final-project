package servlets.flows;

import db.api.UserManager;
import model.User;
import servlets.utils.ServletUtils;
import servlets.utils.SessionUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet(name = "CreateUser", urlPatterns = {"/createUser"})
public class CreateUser extends HttpServlet {
    private static final String FIRST_NAME = "firstName";
    private static final String LAST_NAME = "lastName";
    private static final String EMAIL = "email";

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        UserManager userMgr = ServletUtils.GetNewsWorkshopUtils(getServletContext()).userManager;

        String firstName = request.getParameter(FIRST_NAME);
        String lastName = request.getParameter(LAST_NAME);
        String email = request.getParameter(EMAIL);
        User user = new User(firstName,lastName,email);

        userMgr.createUser(user);
        HttpSession session = request.getSession(true);
        session.setAttribute(SessionUtils.USER_ID, user.id);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request,response);
    }
}
