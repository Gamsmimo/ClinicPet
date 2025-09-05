package com.clinicpet.demo.model;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "cita")
public class Cita {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	private String motivo;
	private LocalDateTime fechaHora;

	@ManyToOne
	@JoinColumn(name = "idUsuario", nullable = false)
	private Usuario usuario;

	@ManyToOne
	@JoinColumn(name = "idVeterinario", nullable = false)
	private Usuario veterinario;

	@ManyToOne
	@JoinColumn(name = "idServicio", nullable = false)
	private Servicio servicio;

	// constructor vacio
	public Cita() {
	}

	// constructor con campos
	public Cita(Integer id, String motivo, LocalDateTime fechaHora, Usuario usuario, Usuario veterinario,
			Servicio servicio) {
		super();
		this.id = id;
		this.motivo = motivo;
		this.fechaHora = fechaHora;
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

	public String getMotivo() {
		return motivo;
	}

	public void setMotivo(String motivo) {
		this.motivo = motivo;
	}

	public LocalDateTime getFechaHora() {
		return fechaHora;
	}

	public void setFechaHora(LocalDateTime fechaHora) {
		this.fechaHora = fechaHora;
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
		return "Cita [id=" + id + ", motivo=" + motivo + ", fechaHora=" + fechaHora + "]";
	}

}
