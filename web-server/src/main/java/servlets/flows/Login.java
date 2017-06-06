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

@WebServlet(name = "Login", urlPatterns = {"/login"})
public class Login extends HttpServlet {
    private static final String EMAIL = "email";

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        UserManager userMgr = ServletUtils.GetNewsWorkshopUtils(getServletContext()).userManager;
        String email = request.getParameter(EMAIL);
        User user = userMgr.findUserByEmail(email);
        HttpSession session = request.getSession(true);

        PrintWriter out = response.getWriter();
        if (user!=null){
            session.setAttribute(SessionUtils.USER_ID, user.id);
            out.write(user.firstName);
            response.setStatus(HttpServletResponse.SC_OK);
        }
        else
        {
            out.write("login failed");
            response.setStatus(HttpServletResponse.SC_OK);
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request,response);
    }
}
