package com.fernandotozetto.cursomc.security;

import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
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
	
	public boolean tokenValido(String token) {
		Claims claims = getClaims(token); // claims armazena as reinvidicações do tokem (no nosso caso é o usuário e o tempo de expiração)
		if (claims != null) {
			String username = claims.getSubject();// pega o usuário
			Date expirationDate = claims.getExpiration();// pega a data de expiração
			Date now = new Date(System.currentTimeMillis());// pega a data atual
			if (username != null && expirationDate != null && now.before(expirationDate)) {
				return true;
			}
		}
		return false;
	}

	public String getUsername(String token) {
		Claims claims = getClaims(token);
		if (claims != null) {
			return claims.getSubject();
		}
		return null;
	}
	
	private Claims getClaims(String token) {
		try {
			return Jwts.parser().setSigningKey(secret.getBytes()).parseClaimsJws(token).getBody();// função que recupera os claims a partir de um token
		}
		catch (Exception e) {
			return null;
		}
	}
}
