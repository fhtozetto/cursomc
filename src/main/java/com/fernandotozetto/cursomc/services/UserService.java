package com.fernandotozetto.cursomc.services;

import org.springframework.security.core.context.SecurityContextHolder;

import com.fernandotozetto.cursomc.security.UserSS;

public class UserService {
	
	public static UserSS authenticated() { //Retorna o Usuário logado.
		try {
			return (UserSS) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		} catch (Exception e) {
			return null;
		}
	}

}
