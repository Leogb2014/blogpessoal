package com.generation.blogpessoal.security;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.generation.blogpessoal.model.Usuario;

/*
 * objetivo é informar para o security os dados de acesso da api
 */

public class UserDetailsImpl implements UserDetails{
	
	//versão da classe que estamos trabalhando
	private static final long serialVersionUID = 1L;
	
	private String userName;
	private String password;
	
	//clase segurança que tras todas autorizações de acesso que o usuario tem
	private List<GrantedAuthority> authorities;
	
	
	public UserDetailsImpl(Usuario user) {
		this.userName = user.getUsuario();
		this.password = user.getSenha();
	}
	
	public UserDetailsImpl() {
	
	}


	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		// autorizações de acesso do usuário
		return authorities;
	}

	@Override
	public String getPassword() {
		// retorna a senha do usuario
		return password;
	}

	@Override
	public String getUsername() {
		// retorna o usuario que esta tentando logar
		return userName;
	}

	@Override
	public boolean isAccountNonExpired() {
		// se a conta do usuario não expirou ele acessa - true
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		// se a conta do usuario não esta bloqueada - true
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		// se a credencial não estiver expirada - true
		return true;
	}

	@Override
	public boolean isEnabled() {
		// se o usuario esta habilitado - true
		return true;
	}

}
