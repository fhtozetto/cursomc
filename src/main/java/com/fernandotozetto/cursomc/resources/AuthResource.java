package com.fernandotozetto.cursomc.resources;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.fernandotozetto.cursomc.dto.EmailDTO;
import com.fernandotozetto.cursomc.security.JWTUtil;
import com.fernandotozetto.cursomc.security.UserSS;
import com.fernandotozetto.cursomc.services.AuthService;
import com.fernandotozetto.cursomc.services.UserService;

@RestController
@RequestMapping(value="/auth")
public class AuthResource {
	/*
	 * Esse EndPoint é respopnsavel por atualizar o token toda vez que a aplicação percecber que está próximo de sua expiração,
	 * assim o usuário não precisa refazer o login caso o token expire enquanto estiver logado.
	 */
	
	@Autowired
	private JWTUtil jwtUtil;
	
	@Autowired
	private AuthService service;
	
	@RequestMapping(value="/refresh_token", method=RequestMethod.POST) // EndPoint protegido por altenticação só é possivel acessar com o token válido.
	public ResponseEntity<Void> refreshToken(HttpServletResponse response) {
	UserSS user = UserService.authenticated(); // Busca o usuario logado.
	String token = jwtUtil.generateToken(user.getUsername()); //Gera um novo token com o usuario logado. (Atualiza o Token)
	response.addHeader("Authorization", "Bearer " + token); // adiciona o token na resposta da requisição
	return ResponseEntity.noContent().build();
	}
	
	@RequestMapping(value = "/forgot", method = RequestMethod.POST) // esqueci minha senha
	public ResponseEntity<Void> forgot(@Valid @RequestBody EmailDTO objDto) {
		service.sendNewPassword(objDto.getEmail());
		return ResponseEntity.noContent().build();
	}

}
