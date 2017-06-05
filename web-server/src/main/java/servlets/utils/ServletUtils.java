package servlets.utils;

import javax.servlet.ServletContext;

public class ServletUtils {
    private static final String NEWS_WORKSHOP_ATTRIBUTE_NAME = "userManager";


    public static NewsWorkshopUtils GetNewsWorkshopUtils(ServletContext servletContext) {
        if (servletContext.getAttribute(NEWS_WORKSHOP_ATTRIBUTE_NAME) == null) {
            servletContext.setAttribute(NEWS_WORKSHOP_ATTRIBUTE_NAME, new NewsWorkshopUtils());
        }
        return (NewsWorkshopUtils) servletContext.getAttribute(NEWS_WORKSHOP_ATTRIBUTE_NAME);
    }
}
