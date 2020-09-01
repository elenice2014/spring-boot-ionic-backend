package com.elenice.cursomc.services;

import org.springframework.mail.SimpleMailMessage;

import com.elenice.cursomc.domain.Pedido;

public interface EmailService {

	void sendOrderConfirmationEmail(Pedido obj);

	void sendEmail(SimpleMailMessage msg);
}
