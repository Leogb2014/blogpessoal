package com.generation.blogpessoal.security;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.generation.blogpessoal.model.Usuario;
import com.generation.blogpessoal.repository.UsuarioRepository;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
	
	/*
	 * objetivo da classe
	 * verificar se o usuario existe no banco de dados
	 * 
	 */
	
	@Autowired
	private UsuarioRepository usuarioRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		// esse metodo vai validar se o usuario ja existe
		Optional<Usuario> usuario = usuarioRepository.findByUsuario(username);
		
		//estamos verificando se o usuario existe pelo findByUsuario
		if(usuario.isPresent()) {
			return new UserDetailsImpl(usuario.get());
		}else {
			throw new ResponseStatusException(HttpStatus.FORBIDDEN);
		}
		
	}
	
	

}
