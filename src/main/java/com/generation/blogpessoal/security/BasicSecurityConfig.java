package com.generation.blogpessoal.security;

import static org.springframework.security.config.Customizer.withDefaults;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/*
 * objetivo da classe
 * informar as configurações de sergurança 
 * liberar os links que não necessitam de login
 */

@Configuration //informar que essa classe é uma classe de configuração
@EnableWebSecurity //informar que essas configurações se aplicam a todo o projeto
public class BasicSecurityConfig {
	
	    //injeççao de dependencia que trazem o jwtAuthFilter
	    @Autowired
	    private JwtAuthFilter authFilter;

	    //ajustes de usuario e senha
	    @Bean
	    UserDetailsService userDetailsService() {

	        return new UserDetailsServiceImpl();
	    }

	    //criptografia da senha
	    @Bean
	    PasswordEncoder passwordEncoder() {
	        return new BCryptPasswordEncoder();
	    }

	    //conseguiu validar usuario e senha
	    @Bean
	    AuthenticationProvider authenticationProvider() {
	        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
	        authenticationProvider.setUserDetailsService(userDetailsService());
	        authenticationProvider.setPasswordEncoder(passwordEncoder());
	        return authenticationProvider;
	    }

	    //implementar gerenciamento de autenticação
	    @Bean
	    AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration)
	            throws Exception {
	        return authenticationConfiguration.getAuthenticationManager();
	    }

	    
	    @Bean
	    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

	    	http
		        .sessionManagement(management -> management
		                .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
		        		.csrf(csrf -> csrf.disable())
		        		.cors(withDefaults());

	    	http
		        .authorizeHttpRequests((auth) -> auth
		                .requestMatchers("/usuarios/logar").permitAll()
		                .requestMatchers("/usuarios/cadastrar").permitAll()
		                .requestMatchers("/error/**").permitAll()
		                .requestMatchers(HttpMethod.OPTIONS).permitAll()
		                .anyRequest().authenticated())
		        .authenticationProvider(authenticationProvider())
		        .addFilterBefore(authFilter, UsernamePasswordAuthenticationFilter.class)
		        .httpBasic(withDefaults());

			return http.build();

	    }

}
