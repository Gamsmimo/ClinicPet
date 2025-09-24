package com.clinicpet.demo.model;

import jakarta.persistence.*;

@Entity
@Table(name = "mascotas") // Plural básico (cambia a "mascota" si prefieres singular)
public class Mascota {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(nullable = false, length = 100)
	private String nombre;

	@Column(nullable = false, length = 50)
	private String especie;

	@Column(length = 100)
	private String raza;

	@Column(nullable = false)
	private Integer edad;

	@Column(length = 10)
	private String genero;

	@Column(length = 50)
	private String tamaño;

	@Column(length = 500)
	private String descripcion;

	@Column(nullable = false, length = 50)
	private String estado;

	@Column(length = 255)
	private String foto;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "idUsuario", nullable = false)
	private Usuario usuario;

	public Mascota() {
	}

	public Mascota(Integer id, String nombre, String especie, String raza, Integer edad, String genero, String tamaño,
			String descripcion, String estado, String foto, Usuario usuario) {
		super();
		this.id = id;
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

	// Getters y Setters (tus originales, sin cambios)
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
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
		return "Mascota [id=" + id + ", nombre=" + nombre + ", especie=" + especie + ", raza=" + raza + ", edad=" + edad
				+ ", genero=" + genero + ", tamaño=" + tamaño + ", descripcion=" + descripcion + ", estado=" + estado
				+ ", foto=" + foto + "]";
	}
}
