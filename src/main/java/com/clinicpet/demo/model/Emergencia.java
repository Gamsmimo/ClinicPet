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
@Table(name = "Emergencia")
public class Emergencia {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	private String tipo;
	private LocalDateTime fechayhora;
	private String descripcion;

	@ManyToOne
	@JoinColumn(name = "idMascota", nullable = false)
	private Mascota mascota;

	@ManyToOne
	@JoinColumn(name = "idVeterinario", nullable = false)
	private Usuario veterinario;

	// constructor sin campos
	public Emergencia() {

	}

	// constructor con campos
	public Emergencia(Integer id, String tipo, LocalDateTime fechayhora, String descripcion, Mascota mascota,
			Usuario veterinario) {
		super();
		this.id = id;
		this.tipo = tipo;
		this.fechayhora = fechayhora;
		this.descripcion = descripcion;
		this.mascota = mascota;
		this.veterinario = veterinario;
	}

	// getters & setters
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public LocalDateTime getFechayhora() {
		return fechayhora;
	}

	public void setFechayhora(LocalDateTime fechayhora) {
		this.fechayhora = fechayhora;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public Mascota getMascota() {
		return mascota;
	}

	public void setMascota(Mascota mascota) {
		this.mascota = mascota;
	}

	public Usuario getVeterinario() {
		return veterinario;
	}

	public void setVeterinario(Usuario veterinario) {
		this.veterinario = veterinario;
	}

	// to string
	@Override
	public String toString() {
		return "Emergencia [id=" + id + ", tipo=" + tipo + ", fechayhora=" + fechayhora + ", descripcion=" + descripcion
				+ ", mascota=" + mascota + ", veterinario=" + veterinario + "]";
	}

}
