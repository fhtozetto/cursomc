package com.fernandotozetto.cursomc.security;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fernandotozetto.cursomc.domain.enums.Perfil;

public class UserSS implements UserDetails {
private static final long serialVersionUID = 1L;
	
	private Integer id;
	private String email;
	private String senha;
	private Collection<? extends GrantedAuthority> authorities;
	
	public UserSS() {
	}
	
	public UserSS(Integer id, String email, String senha, Set<Perfil> perfis) {
		super();
		this.id = id;
		this.email = email;
		this.senha = senha;
		this.authorities = perfis.stream().map(x -> new SimpleGrantedAuthority(x.getDescricao())).collect(Collectors.toList());
	}
	

	public Integer getId() {
		return id;
	}
	
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() { // retorna as autorizações 
		return authorities;
	}

	@Override
	public String getPassword() { // retorna a senha
		return senha;
	}

	@Override
	public String getUsername() { // retorna o usuário
		return email;
	}

	@Override
	public boolean isAccountNonExpired() { //a conta não está expirada? 
		return true; // por enquanto sempre verdadeiro!
	}

	@Override
	public boolean isAccountNonLocked() { // a conta não está bloqueada?
		return true; // por enquanto sempre verdadeiro!
	}

	@Override
	public boolean isCredentialsNonExpired() { // as credenciais não estão espiradas?
		return true; // por enquanto sempre verdadeiro!
	}

	@Override
	public boolean isEnabled() { // o usuario está habilitado?
		return true; // por enquanto sempre verdadeiro!
	}
	
	public boolean hasRole(Perfil perfil) {
		return getAuthorities().contains(new SimpleGrantedAuthority(perfil.getDescricao()));
	}
}
