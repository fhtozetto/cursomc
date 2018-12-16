package com.fernandotozetto.cursomc.services;

import org.springframework.mail.SimpleMailMessage;

import com.fernandotozetto.cursomc.domain.Pedido;

public interface EmailService {
	
	void sendOrderConfirmationEmail(Pedido obj);
	
	void sendEmail(SimpleMailMessage msg);

}
