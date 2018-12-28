package com.fernandotozetto.cursomc.security;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

public class JWTAuthorizationFilter extends BasicAuthenticationFilter{ //extrai o usuario e senha do token para fazer a validação

	private JWTUtil jwtUtil;
	
	private UserDetailsService userDetailsService;
	
	public JWTAuthorizationFilter(AuthenticationManager authenticationManager, 
								  JWTUtil jwtUtil, 
								  UserDetailsService userDetailsService) {
		super(authenticationManager);
		this.jwtUtil = jwtUtil;
		this.userDetailsService = userDetailsService;
	}
	
	@Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain) throws IOException, ServletException { // esse metodo intersepta a requisição e verifica se o usuario está autotizado
		
		String header = request.getHeader("Authorization");// pega o cabeçalho da requisição.
		if (header != null && header.startsWith("Bearer ")) { // verifica se a chave existe
			UsernamePasswordAuthenticationToken auth = getAuthentication(header.substring(7));//valida o token se for inválido retorna null
			if (auth != null) {
				SecurityContextHolder.getContext().setAuthentication(auth);// autoriza a requisição
			}
		}
		chain.doFilter(request, response);
	}

	private UsernamePasswordAuthenticationToken getAuthentication(String token) {
		if (jwtUtil.tokenValido(token)) {
			String username = jwtUtil.getUsername(token);// pega o usuario no dentro do token
			UserDetails user = userDetailsService.loadUserByUsername(username);// busca o usuario no banco de dados
			return new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());//retorna o usuario e suas autorizações (perfil)
		}
		return null;
	}

}
