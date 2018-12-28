package com.fernandotozetto.cursomc.security;

import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component // marca essa classe para que possa ser injetada em outras classe "Autowierd".
public class JWTUtil {
	@Value("${jwt.secret}")// pega a informação na chave {jwt.secret} dentro de "Application.properties"
	private String secret;

	@Value("${jwt.expiration}")// pega a informação na chave {jwt.expiration} dentro de "Application.properties"
	private Long expiration;
	
	public String generateToken(String username) { //gera o Token da aplicação
		return Jwts.builder() //gera o Token da aplicação 
				.setSubject(username) // Usuário
				.setExpiration(new Date(System.currentTimeMillis() + expiration)) // tempo para explirar o Token
				.signWith(SignatureAlgorithm.HS512, secret.getBytes()) // Algoritimo usado para criptografia / palavra secreta
				.compact(); // compacta a informação.
	}
}
