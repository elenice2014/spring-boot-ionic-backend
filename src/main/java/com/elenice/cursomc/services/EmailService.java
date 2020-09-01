package com.elenice.cursomc.services;

import javax.mail.internet.MimeMessage;

import org.springframework.mail.SimpleMailMessage;

import com.elenice.cursomc.domain.Pedido;

public interface EmailService {

	void sendOrderConfirmationEmail(Pedido obj);
	void sendEmail(SimpleMailMessage msg);
	
	//enviar email HTML
	void sendOrderConfirmationHtmlEmail(Pedido obj); 
	void sendHtmlEmail(MimeMessage msg); 
}
