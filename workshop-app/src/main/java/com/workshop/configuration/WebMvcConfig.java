package com.workshop.configuration;

import com.workshop.search.SearchThread;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;
import org.springframework.web.servlet.view.freemarker.FreeMarkerViewResolver;

import java.net.URL;
import java.util.Properties;

@Configuration
public class WebMvcConfig extends WebMvcConfigurerAdapter {

	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
		return bCryptPasswordEncoder;
	}

	@Bean
	public ThreadPoolTaskExecutor taskExecutor() {
		ThreadPoolTaskExecutor pool = new ThreadPoolTaskExecutor();
		pool.setCorePoolSize(5);
		pool.setMaxPoolSize(10);
		pool.setWaitForTasksToCompleteOnShutdown(true);
		return pool;
	}

	@Bean
	public JavaMailSenderImpl mailSenderSpring() {
		JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
		mailSender.setHost("smtp.gmail.com");
		mailSender.setPort(587);

		mailSender.setUsername("mybuzzworkshop@gmail.com");
		mailSender.setPassword("Initial1");

		Properties props = mailSender.getJavaMailProperties();
		props.put("mail.transport.protocol", "smtp");
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.debug", "true");

		return mailSender;
	}

	@Bean
	public com.workshop.service.mail.MailSender mailSender() {
		return new com.workshop.service.mail.MailSender();
	}

	@Bean
	public SearchThread searchThread() {
		return new SearchThread();
	}

	@Bean
	public FreeMarkerConfigurer freeMarkerConfigurer() {
		FreeMarkerConfigurer freeMarkerConfig = new FreeMarkerConfigurer();
//		ClassLoader classLoader = getClass().getClassLoader();
//		URL resource = classLoader.getResource("templates/mail-templates/");
//		freeMarkerConfig.setTemplateLoaderPath("/templates/mail-templates/");
		freeMarkerConfig.setTemplateLoaderPath("classpath:/templates/mail-templates/");
		freeMarkerConfig.setDefaultEncoding("UTF-8");
		freeMarkerConfig.setPreferFileSystemAccess(false);

		return freeMarkerConfig;
	}

	@Bean
	public FreeMarkerViewResolver freemarkerViewResolver() {
		FreeMarkerViewResolver resolver = new FreeMarkerViewResolver();
		resolver.setCache(true);
		resolver.setPrefix("");
		resolver.setSuffix(".ftl");
		return resolver;
	}
}