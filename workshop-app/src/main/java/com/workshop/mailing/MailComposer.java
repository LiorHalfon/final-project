package com.workshop.mailing;

import com.workshop.search.RelevantNewsView;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.springframework.ui.freemarker.FreeMarkerTemplateUtils.processTemplateIntoString;

public class MailComposer {

    private static Configuration freeMarkerConfig;

    //TODO: need help to inject it from springframework
    static {
        freeMarkerConfig = new Configuration(Configuration.VERSION_2_3_25);
        try {
            freeMarkerConfig.setDirectoryForTemplateLoading(new File("./src/main/resources/templates/mail-templates/"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        freeMarkerConfig.setDefaultEncoding("UTF-8");
        freeMarkerConfig.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
        freeMarkerConfig.setLogTemplateExceptions(false);
    }

    public String ComposeMail(List<RelevantNewsView> news, String mailTitle) throws IOException, TemplateException {
        String mail = "";

        mail += CompileMailHeader("header.html", mailTitle);

        for (int i = 0; i < news.size(); i++) {
            if (i % 2 == 0) {
                mail += CompileArticle("lightArticle.html", news.get(i));
            } else {
                mail += CompileArticle("redArticle.html", news.get(i));
            }
        }

        mail += freeMarkerConfig.getTemplate("footer.html").toString();

        return mail;
    }

    public String CompileArticle(String templateName, RelevantNewsView news) throws IOException, TemplateException {
        Template template = freeMarkerConfig.getTemplate(templateName);

        return processTemplateIntoString(template, news);
    }

    public String CompileMailHeader(String templateName, String mailTitle) throws IOException, TemplateException {
        Template template = freeMarkerConfig.getTemplate(templateName);

        Map<String, Object> model = new HashMap<>();
        model.put("title", mailTitle);

        return processTemplateIntoString(template, model);
    }
}
