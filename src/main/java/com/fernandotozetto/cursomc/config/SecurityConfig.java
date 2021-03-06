package com.fernandotozetto.cursomc.config;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.fernandotozetto.cursomc.security.JWTAuthenticationFilter;
import com.fernandotozetto.cursomc.security.JWTAuthorizationFilter;
import com.fernandotozetto.cursomc.security.JWTUtil;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter{
/*
 * Essa Classe é responsável por definir o que está liberado por padrão 
 * e o que está bloqueado por padrão no Spring Security.
 */
	@Autowired
	private UserDetailsService userDetailsService;
	
	@Autowired
    private Environment env;
	
	@Autowired
	private JWTUtil jwtUtil;
	
	
	private static final String[] PUBLIC_MATCHERS = { // caminhos que estão liberados por padrão
			"/h2-console/**"
	};

	private static final String[] PUBLIC_MATCHERS_GET = { // caminhos que estão liberados apenas para leitura
			"/produtos/**",
			"/categorias/**",
			"/estados/**"
	};
	
	private static final String[] PUBLIC_MATCHERS_POST = { // caminhos que estão liberados apenas para Cadastro
			"/clientes",
			"/auth/forgot/**"
	};

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		
		if (Arrays.asList(env.getActiveProfiles()).contains("test")) { //estou pegando os profiles ativos do projeto e se for o "test"
            http.headers().frameOptions().disable(); //comando que libera acesso ao "h2-console"
        }
		
		http.cors().and().csrf().disable(); //chama o "corsConfigurationSource" caso esteja definido como está // como não armazena seção foi desabilitado o "csrf"
		http.authorizeRequests() 
			.antMatchers(HttpMethod.POST, PUBLIC_MATCHERS_POST).permitAll() // Será permitido cadastrar o cliente, afinal ele fara seu auto cadastro
			.antMatchers(HttpMethod.GET, PUBLIC_MATCHERS_GET).permitAll() // todos os GETs que estiverem no "PUBLIC_MATCHERS_GET" estão permitidos
			.antMatchers(PUBLIC_MATCHERS).permitAll() // todos os caminhos que estiverem no "PUBLIC_MATCHERS" estão permitidos
			.anyRequest().authenticated(); // para todo o resto é exigido altenticação.
		http.addFilter(new JWTAuthenticationFilter(authenticationManager(), jwtUtil));// registra o filtro de autenticação.
		http.addFilter(new JWTAuthorizationFilter(authenticationManager(), jwtUtil, userDetailsService));// Autoria a ação do usuario.
		http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS); // usada para asegurar que nosso backend não irá criar sessão de usuário.
	}
	
	@Override
	public void configure(AuthenticationManagerBuilder auth) throws Exception { // 
		auth.userDetailsService(userDetailsService).passwordEncoder(bCryptPasswordEncoder());
	}
	
	@Bean
	CorsConfigurationSource corsConfigurationSource() {
		/*
		 * Libera acesso básico de multiplas fontes para todos os caminhos com as configurações básicas.
		 */
		CorsConfiguration configuration = new CorsConfiguration().applyPermitDefaultValues();
		configuration.setAllowedMethods(Arrays.asList("POST", "GET", "PUT", "DELETE", "OPTIONS"));
		final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", configuration);
		return source;
	}
	
	@Bean
	public BCryptPasswordEncoder bCryptPasswordEncoder() { //responsavel por criptografar campos do banco de dados
		return new BCryptPasswordEncoder();
	}
}
