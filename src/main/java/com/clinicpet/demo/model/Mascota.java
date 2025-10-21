package com.clinicpet.demo.model;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "mascotas")
public class Mascota {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	private String nombre;
	private String especie;
	private String raza;
	private Integer edad;
	private String genero;
	private String tamaño;
	private String descripcion;
	private String foto;
<<<<<<< HEAD
	private String unidadEdad;
=======
>>>>>>> bcd859d018ce2d6ff7cc81e0398cf721f3ed8b6f

	// ✅ CAMBIAR A @JsonIgnore
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "idUsuario", nullable = false)
	@JsonIgnore // ← AGREGAR ESTA ANOTACIÓN
	private Usuario usuario;

	// Constructores
	public Mascota() {
	}

<<<<<<< HEAD
	
	public Mascota(Integer id, String nombre, String especie, String raza, Integer edad, String genero, String tamaño,
			String descripcion, String foto, String unidadEdad, Usuario usuario) {
		super();
=======
	public Mascota(Integer id, String nombre, String especie, String raza, Integer edad, String genero, String tamaño,
			String descripcion, String foto, Usuario usuario) {
>>>>>>> bcd859d018ce2d6ff7cc81e0398cf721f3ed8b6f
		this.id = id;
		this.nombre = nombre;
		this.especie = especie;
		this.raza = raza;
		this.edad = edad;
		this.genero = genero;
		this.tamaño = tamaño;
		this.descripcion = descripcion;
		this.foto = foto;
<<<<<<< HEAD
		this.unidadEdad = unidadEdad;
		this.usuario = usuario;
	}
	
	


=======
		this.usuario = usuario;
	}

	// Getters y Setters (todos los que ya tienes)
>>>>>>> bcd859d018ce2d6ff7cc81e0398cf721f3ed8b6f
	public Integer getId() {
		return id;
	}

<<<<<<< HEAD

=======
>>>>>>> bcd859d018ce2d6ff7cc81e0398cf721f3ed8b6f
	public void setId(Integer id) {
		this.id = id;
	}

<<<<<<< HEAD

=======
>>>>>>> bcd859d018ce2d6ff7cc81e0398cf721f3ed8b6f
	public String getNombre() {
		return nombre;
	}

<<<<<<< HEAD

=======
>>>>>>> bcd859d018ce2d6ff7cc81e0398cf721f3ed8b6f
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

<<<<<<< HEAD

=======
>>>>>>> bcd859d018ce2d6ff7cc81e0398cf721f3ed8b6f
	public String getEspecie() {
		return especie;
	}

<<<<<<< HEAD

=======
>>>>>>> bcd859d018ce2d6ff7cc81e0398cf721f3ed8b6f
	public void setEspecie(String especie) {
		this.especie = especie;
	}

<<<<<<< HEAD

=======
>>>>>>> bcd859d018ce2d6ff7cc81e0398cf721f3ed8b6f
	public String getRaza() {
		return raza;
	}

<<<<<<< HEAD

=======
>>>>>>> bcd859d018ce2d6ff7cc81e0398cf721f3ed8b6f
	public void setRaza(String raza) {
		this.raza = raza;
	}

<<<<<<< HEAD

=======
>>>>>>> bcd859d018ce2d6ff7cc81e0398cf721f3ed8b6f
	public Integer getEdad() {
		return edad;
	}

<<<<<<< HEAD

=======
>>>>>>> bcd859d018ce2d6ff7cc81e0398cf721f3ed8b6f
	public void setEdad(Integer edad) {
		this.edad = edad;
	}

<<<<<<< HEAD

=======
>>>>>>> bcd859d018ce2d6ff7cc81e0398cf721f3ed8b6f
	public String getGenero() {
		return genero;
	}

<<<<<<< HEAD

=======
>>>>>>> bcd859d018ce2d6ff7cc81e0398cf721f3ed8b6f
	public void setGenero(String genero) {
		this.genero = genero;
	}

<<<<<<< HEAD

=======
>>>>>>> bcd859d018ce2d6ff7cc81e0398cf721f3ed8b6f
	public String getTamaño() {
		return tamaño;
	}

<<<<<<< HEAD

=======
>>>>>>> bcd859d018ce2d6ff7cc81e0398cf721f3ed8b6f
	public void setTamaño(String tamaño) {
		this.tamaño = tamaño;
	}

<<<<<<< HEAD

=======
>>>>>>> bcd859d018ce2d6ff7cc81e0398cf721f3ed8b6f
	public String getDescripcion() {
		return descripcion;
	}

<<<<<<< HEAD

=======
>>>>>>> bcd859d018ce2d6ff7cc81e0398cf721f3ed8b6f
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

<<<<<<< HEAD

=======
>>>>>>> bcd859d018ce2d6ff7cc81e0398cf721f3ed8b6f
	public String getFoto() {
		return foto;
	}

<<<<<<< HEAD

=======
>>>>>>> bcd859d018ce2d6ff7cc81e0398cf721f3ed8b6f
	public void setFoto(String foto) {
		this.foto = foto;
	}

<<<<<<< HEAD

	public String getUnidadEdad() {
		return unidadEdad;
	}


	public void setUnidadEdad(String unidadEdad) {
		this.unidadEdad = unidadEdad;
	}


=======
>>>>>>> bcd859d018ce2d6ff7cc81e0398cf721f3ed8b6f
	public Usuario getUsuario() {
		return usuario;
	}

<<<<<<< HEAD

=======
>>>>>>> bcd859d018ce2d6ff7cc81e0398cf721f3ed8b6f
	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

<<<<<<< HEAD

=======
>>>>>>> bcd859d018ce2d6ff7cc81e0398cf721f3ed8b6f
	@Override
	public String toString() {
		return "Mascota{" + "id=" + id + ", nombre='" + nombre + '\'' + ", especie='" + especie + '\'' + ", raza='"
				+ raza + '\'' + ", edad=" + edad + ", genero='" + genero + '\'' + ", tamaño='" + tamaño + '\''
				+ ", descripcion='" + descripcion + '\'' + ", foto='" + foto + '\'' + '}';
	}
}
