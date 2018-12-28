package com.fernandotozetto.cursomc.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.fernandotozetto.cursomc.domain.Cliente;
import com.fernandotozetto.cursomc.repositories.ClienteRepository;
import com.fernandotozetto.cursomc.security.UserSS;


@Service
public class UserDetailsServiceImpl implements UserDetailsService {
	/*
	 * Essa Classe permite uma pusca pelo nome do usuário.
	 */
	
	@Autowired
	private ClienteRepository repo;
	
	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException { // Busca um cliente por email
		Cliente cli = repo.findByEmail(email);
		if (cli == null) { //caso não encontrado.
			throw new UsernameNotFoundException(email); //retorna o erro do Spring Security.
		}
		return new UserSS(cli.getId(), cli.getEmail(), cli.getSenha(), cli.getPerfis());
	}
}
