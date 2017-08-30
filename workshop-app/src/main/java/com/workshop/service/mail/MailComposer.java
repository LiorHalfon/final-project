package com.workshop.service.mail;

import com.workshop.search.RelevantNewsView;
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

    public String ComposeSearchDoneMail(String mailTitle, int resultsId) throws IOException, TemplateException {
        String mail = "";
        mail += CompileMailHeader("header.html", mailTitle);
        mail += CompileTemplateWithResultId("search-done.html", resultsId);
        mail += CompileTemplateWithResultId("footer.html", resultsId);
        return mail;
    }

    public String ComposeMail(List<RelevantNewsView> news, String mailTitle, int resultsId) throws IOException, TemplateException {
        String mail = "";
        mail += CompileMailHeader("header.html", mailTitle);
        for (int i = 0; i < news.size(); i++) {

            if (i >= 3 && i + 2 < news.size()) {
                mail += Compile3InRow(news.get(i), news.get(i + 1), news.get(i + 2));
                i += 2;
            } else if (i >= 1 && i + 1 < news.size()) {
                mail += Compile2InRow(news.get(i), news.get(i + 1));
                i += 1;
            } else
                mail += CompileArticle("big-article.html", news.get(i));
        }

        mail += CompileTemplateWithResultId("footer.html", resultsId);
        return mail;
    }

    private String Compile2InRow(RelevantNewsView news1,RelevantNewsView news2) throws IOException, TemplateException {
        String res = "";
        res += CompileArticle("2-in-row/first.html",news1);
        res += CompileArticle("2-in-row/second.html",news2);
        return res;
    }

    private String Compile3InRow(RelevantNewsView news1,RelevantNewsView news2, RelevantNewsView news3) throws IOException, TemplateException {
        String res = "";
        res += CompileArticle("3-in-row/first.html",news1);
        res += CompileArticle("3-in-row/second.html",news2);
        res += CompileArticle("3-in-row/third.html",news3);
        return res;
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

    private String CompileTemplateWithResultId(String templateName, int resultsId) throws IOException, TemplateException {
        Template template = freeMarkerConfig.getConfiguration().getTemplate(templateName);
        Map<String, Object> model = new HashMap<>();
        model.put("resultsId", resultsId);
        return processTemplateIntoString(template, model);
    }
}
