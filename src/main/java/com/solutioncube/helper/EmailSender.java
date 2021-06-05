package com.solutioncube.helper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Component
public class EmailSender {

	@Autowired
	JavaMailSender mailSender;

	@Autowired
	SimpleMailMessage simpleMailMessage;

	public void sendMail(String text) {

		simpleMailMessage.setText(text);
		mailSender.send(simpleMailMessage);
	}

	public void sendDailyJobReportMail() {

		sendMail(ApiErrorLogger.getApiErrors().size() == 0 ? "Job başarılı bir şekilde çalıştı." : "Job çalışma esnasında hata aldı! Lütfen logları inceleyeniz.");
	}
}