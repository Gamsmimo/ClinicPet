package com.clinicpet.demo.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "mascota")
public class Mascota {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer idMascota;
	@ManyToOne
	@JoinColumn(name = "idUsuario")
	private Usuario usuario; // dueño actual
	private String nombre;
	private String especie;
	private String raza;
	private Integer edad;
	private String genero; // M/F
	private String tamaño;
	private String descripcion;
	private String estado; // disponible, adoptada, en proceso
	private String foto;

	// constructor vacio
	public Mascota() {
	}

	// constructor con campos
	public Mascota(Integer idMascota, String nombre, String especie, String raza, Integer edad, String genero,
			String tamaño, String descripcion, String estado, String foto, Usuario usuario) {
		super();
		this.idMascota = idMascota;
		this.nombre = nombre;
		this.especie = especie;
		this.raza = raza;
		this.edad = edad;
		this.genero = genero;
		this.tamaño = tamaño;
		this.descripcion = descripcion;
		this.estado = estado;
		this.foto = foto;
		this.usuario = usuario;
	}

	public Integer getIdMascota() {
		return idMascota;
	}

	public void setIdMascota(Integer idMascota) {
		this.idMascota = idMascota;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getEspecie() {
		return especie;
	}

	public void setEspecie(String especie) {
		this.especie = especie;
	}

	public String getRaza() {
		return raza;
	}

	public void setRaza(String raza) {
		this.raza = raza;
	}

	public Integer getEdad() {
		return edad;
	}

	public void setEdad(Integer edad) {
		this.edad = edad;
	}

	public String getGenero() {
		return genero;
	}

	public void setGenero(String genero) {
		this.genero = genero;
	}

	public String getTamaño() {
		return tamaño;
	}

	public void setTamaño(String tamaño) {
		this.tamaño = tamaño;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public String getFoto() {
		return foto;
	}

	public void setFoto(String foto) {
		this.foto = foto;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	@Override
	public String toString() {
		return "Mascota [idMascota=" + idMascota + ", nombre=" + nombre + ", especie=" + especie + ", raza=" + raza
				+ ", edad=" + edad + ", genero=" + genero + ", tamaño=" + tamaño + ", descripcion=" + descripcion
				+ ", estado=" + estado + ", foto=" + foto + "]";
	}

}
