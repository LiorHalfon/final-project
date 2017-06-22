package com.workshop.mailing;

import com.workshop.search.RelevantNewsView;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;

import java.io.File;
import java.io.IOException;

import static org.springframework.ui.freemarker.FreeMarkerTemplateUtils.processTemplateIntoString;

public class MailComposer {

    private static Configuration freeMarkerConfig;

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

    public String ComposeHtml(RelevantNewsView news) throws IOException, TemplateException {
        Template template = freeMarkerConfig.getTemplate("redArticle.html");

        return processTemplateIntoString(template,news);
    }
}
