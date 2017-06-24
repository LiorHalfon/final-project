package com.workshop.service.mail;

import com.workshop.search.RelevantNewsView;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.springframework.ui.freemarker.FreeMarkerTemplateUtils.processTemplateIntoString;

@Service("mailComposer")
public class MailComposer {

    @Autowired
    private FreeMarkerConfigurer freeMarkerConfig;

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

        mail += freeMarkerConfig.getConfiguration().getTemplate("footer.html").toString();

        return mail;
    }

    private String CompileArticle(String templateName, RelevantNewsView news) throws IOException, TemplateException {
        Template template = freeMarkerConfig.getConfiguration().getTemplate(templateName);

        return processTemplateIntoString(template, news);
    }

    private String CompileMailHeader(String templateName, String mailTitle) throws IOException, TemplateException {
        Template template = freeMarkerConfig.getConfiguration().getTemplate(templateName);

        Map<String, Object> model = new HashMap<>();
        model.put("title", mailTitle);

        return processTemplateIntoString(template, model);
    }
}
