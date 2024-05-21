package com.generation.blogpessoal.model;

import java.time.LocalDateTime;

import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Entity 
@Table(name="tb_postagens")
public class Postagem {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@NotBlank(message = "O atributo TITULO é obrigatório!")// validation - validar atributo para que ele não deixe de ser passado e também não seja vazio
	@Size(min = 5, max = 100, message="O atributo TITULO deve ter bo mínimo 5 caracteres e no máximo 100 caracteres.")
	private String titulo;
	
	@NotBlank(message="o atributo TEXTO é obrigatório!")
	@Size(min = 10, max = 1000, message = "O atributo TEXTO deve ter no mínimo 10 caracteres e no máximo 1000 caracteres.")
	private String texto;
	
	@UpdateTimestamp // pega a data e hora do sistema e preenche no banco de dados 
	private LocalDateTime data;

	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTitulo() {
		return titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	public String getTexto() {
		return texto;
	}

	public void setTexto(String texto) {
		this.texto = texto;
	}

	public LocalDateTime getData() {
		return data;
	}

	public void setData(LocalDateTime data) {
		this.data = data;
	}
	

}