package servlets.utils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public class SessionUtils {
    public static final String USER_ID = "userId";

    public static String getUserId (HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        Object attribute = session != null ? session.getAttribute(USER_ID) : null;
        return attribute != null ? (String)attribute : "-1";
    }
}
