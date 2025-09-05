package com.clinicpet.demo.model;

import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class Calificacion {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	private Integer puntuacion;
	private String comentario;
	private LocalDate fecha;

	@ManyToOne
	@JoinColumn(name = "idUsuario", nullable = false)
	private Usuario usuario;

	@ManyToOne
	@JoinColumn(name = "idVeterinario")
	private Usuario veterinario;

	@ManyToOne
	@JoinColumn(name = "idServicio")
	private Servicio servicio;

	// constructor vacio
	public Calificacion() {
	}

	// constructor con campos
	public Calificacion(Integer id, Integer puntuacion, String comentario, LocalDate fecha, Usuario usuario,
			Usuario veterinario, Servicio servicio) {
		super();
		this.id = id;
		this.puntuacion = puntuacion;
		this.comentario = comentario;
		this.fecha = fecha;
		this.usuario = usuario;
		this.veterinario = veterinario;
		this.servicio = servicio;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getPuntuacion() {
		return puntuacion;
	}

	public void setPuntuacion(Integer puntuacion) {
		this.puntuacion = puntuacion;
	}

	public String getComentario() {
		return comentario;
	}

	public void setComentario(String comentario) {
		this.comentario = comentario;
	}

	public LocalDate getFecha() {
		return fecha;
	}

	public void setFecha(LocalDate fecha) {
		this.fecha = fecha;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public Usuario getVeterinario() {
		return veterinario;
	}

	public void setVeterinario(Usuario veterinario) {
		this.veterinario = veterinario;
	}

	public Servicio getServicio() {
		return servicio;
	}

	public void setServicio(Servicio servicio) {
		this.servicio = servicio;
	}

	@Override
	public String toString() {
		return "Calificacion [id=" + id + ", puntuacion=" + puntuacion + ", comentario=" + comentario + ", fecha="
				+ fecha + "]";
	}

}
