package com.shopme;

import java.util.Properties;

import javax.servlet.http.HttpServletRequest;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import com.shopme.setting.EmailSettingBag;

public class Utility {

	public static String getSiteUrl(HttpServletRequest request) {
		String siteURL = request.getRequestURL().toString();
		return siteURL.replace(request.getServletPath(), "");
	}
	
	public static JavaMailSenderImpl prepareMailSender(EmailSettingBag settings) {
		
		JavaMailSenderImpl mailSender = new  JavaMailSenderImpl();
		mailSender.setHost(settings.getHost());
		mailSender.setPort(settings.getPort());
		mailSender.setUsername(settings.getUsername());
		mailSender.setPassword(settings.getPassword());
		
		Properties mailProperties = new Properties();
		
		mailProperties.setProperty("mail.smtp.auth", settings.getSmptAuth());
		mailProperties.setProperty("mail.smtp.starttls.enable", settings.getSmptSecured());
		
		mailSender.setJavaMailProperties(mailProperties);
		
		return mailSender;
	}
}
