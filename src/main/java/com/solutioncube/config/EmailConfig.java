package com.solutioncube.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.mail.SimpleMailMessage;

@Configuration
public class EmailConfig {

	@Bean
	@Primary
	public SimpleMailMessage simpleMailMessage() {
		
		SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
		simpleMailMessage.setTo("ergunkargun@gmail.com", "ozturkksamett@gmail.com");
		simpleMailMessage.setCc("fatihburakkoca@gmail.com");
		simpleMailMessage.setFrom("solutioncubedev@gmail.com");
		simpleMailMessage.setSubject("About SolutitonCubeJob");
		
		return simpleMailMessage;
	}
}
