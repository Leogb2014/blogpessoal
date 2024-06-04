package com.generation.blogpessoal.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.generation.blogpessoal.model.Usuario;
import com.generation.blogpessoal.model.UsuarioLogin;
import com.generation.blogpessoal.repository.UsuarioRepository;
import com.generation.blogpessoal.security.JwtService;

@Service //Spring estamos tratando aqui regras de negocio
public class UsuarioService {
	
	@Autowired
	private UsuarioRepository usuarioRepository;
	
	
	@Autowired
	private JwtService jwtService;
	
	@Autowired
	private AuthenticationManager authenticationManager;
	/*
	 * classe do security que tem gestão de autenticar o usuario
	 * permite acessar metodos que podem entregar ao objeto as suas autoridades concedidas
	 */

	// primeira regra de negócio / vamos definir as regras para permitir o cadastro do usuário
	public Optional<Usuario> cadastrarUsuario(Usuario usuario){
		// nome | usuario(email) | senha | foto
		if(usuarioRepository.findByUsuario(usuario.getUsuario()).isPresent())
			return Optional.empty(); // meu objeto esta vazio
		
		usuario.setSenha(criptografarSenha(usuario.getSenha()));
		
		return Optional.of(usuarioRepository.save(usuario));
		
	}
	/*
	 * método que vai tratar para a senha ser criptografada antes de ser persistida no banco
	 */
	private String criptografarSenha(String senha) {
		//classe que trata a criptografia
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		return encoder.encode(senha);//método encoder sendo aplicado na senha
	}
	
	/*
	 * segundo problema 
	 * objetivo evitar dois usuarios com o mesmo email na hora do update
	 * nome | usuario(email) | senha | foto ti.jacque@gmail.com -> ingrid@gmail.com
	 */
	
	public Optional<Usuario> atualizarUsuario(Usuario usuario){
		//validando se o id passado existe no banco de dados 
		if(usuarioRepository.findById(usuario.getId()).isPresent()){
			
			//objeto optional pq pode existir ou não
			Optional<Usuario> buscaUsuario = usuarioRepository.findByUsuario(usuario.getUsuario());
			
			//pesquisei no banco 2| nome | email@email.com | 123456789 | ""
			
			if(buscaUsuario.isPresent() && (buscaUsuario.get().getId() ) != usuario.getId())
				throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Usuário já existe", null);
			
			usuario.setSenha(criptografarSenha(usuario.getSenha()));
			
			return Optional.ofNullable(usuarioRepository.save(usuario));
		}
		return Optional.empty();
	}
	
	public Optional<UsuarioLogin> autenticarUsuario(Optional<UsuarioLogin> usuarioLogin){
		
		// objeto com os dados do usuario que tenta logar 
		var credenciais = new UsernamePasswordAuthenticationToken(usuarioLogin.get().getUsuario(),
				usuarioLogin.get().getSenha());
		
		//tiver esse usuario e senha
		Authentication authentication = authenticationManager.authenticate(credenciais);
		
		if(authentication.isAuthenticated()){
			Optional<Usuario> usuario = usuarioRepository.findByUsuario(usuarioLogin.get().getUsuario());
			
			if(usuario.isPresent()) {
				
				usuarioLogin.get().setId(usuario.get().getId());
				usuarioLogin.get().setNome(usuario.get().getNome());
				usuarioLogin.get().setFoto(usuario.get().getFoto());
				usuarioLogin.get().setToken(gerarToken(usuarioLogin.get().getUsuario()));
				usuarioLogin.get().setSenha("");
				
				return usuarioLogin;
			}
		}
		return Optional.empty();
		
	}
	
	/*
	 * método que vai la na jwtService e gera o toke de usuario
	 */
	private String gerarToken(String usuario) {
		return "Bearer " +jwtService.generateToken(usuario);
	}


}
