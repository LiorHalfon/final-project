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
import java.io.PrintWriter;

@WebServlet(name = "CreateUser", urlPatterns = {"/createUser"})
public class CreateUser extends HttpServlet {
    private static final String FIRST_NAME = "firstname";
    private static final String LAST_NAME = "lastname";
    private static final String EMAIL = "email";
    private static final String USER_ID = "userid";

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        PrintWriter out = response.getWriter();
        UserManager userMgr = ServletUtils.GetNewsWorkshopUtils(getServletContext()).userManager;

        String firstName = request.getParameter(FIRST_NAME);
        String lastName = request.getParameter(LAST_NAME);
        String email = request.getParameter(EMAIL);
        String id = request.getParameter(USER_ID);
        User user = new User(firstName,lastName,email);
        user.id = id;

        try {
            userMgr.createUser(user);
            HttpSession session = request.getSession(true);
            session.setAttribute(SessionUtils.USER_ID, user.id);

            out.write(user.firstName);
            response.setStatus(HttpServletResponse.SC_OK);
        }catch (Exception e){
            out.write("failed");
            response.setStatus(HttpServletResponse.SC_OK);
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request,response);
    }
}
