package com.clinicpet.demo.model;

import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "HistoriaClinica")
public class HistoriaClinica {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	private String diagnostico;
	private String tratamiento;
	private LocalDate fecha;

	@ManyToOne
	@JoinColumn(name = "idMascota", nullable = false)
	private Mascota mascota;

	@ManyToOne
	@JoinColumn(name = "idVterinario", nullable = false)
	private Usuario veterinario;

	@ManyToOne
	@JoinColumn(name = "idVeterinaria", nullable = false)
	private Veterinaria veterinaria;

	@ManyToOne
	@JoinColumn(name = "idUsuario", nullable = false)
	private Usuario usuario;

	// constructor vacio
	public HistoriaClinica() {

	}

	// constructor con campos
	public HistoriaClinica(Integer id, String diagnostico, String tratamiento, LocalDate fecha, Mascota mascota,
			Usuario veterinario, Veterinaria veterinaria, Usuario usuario) {
		super();
		this.id = id;
		this.diagnostico = diagnostico;
		this.tratamiento = tratamiento;
		this.fecha = fecha;
		this.mascota = mascota;
		this.veterinario = veterinario;
		this.veterinaria = veterinaria;
		this.usuario = usuario;
	}

	// getters & setters
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getDiagnostico() {
		return diagnostico;
	}

	public void setDiagnostico(String diagnostico) {
		this.diagnostico = diagnostico;
	}

	public String getTratamiento() {
		return tratamiento;
	}

	public void setTratamiento(String tratamiento) {
		this.tratamiento = tratamiento;
	}

	public LocalDate getFecha() {
		return fecha;
	}

	public void setFecha(LocalDate fecha) {
		this.fecha = fecha;
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

	public Veterinaria getVeterinaria() {
		return veterinaria;
	}

	public void setVeterinaria(Veterinaria veterinaria) {
		this.veterinaria = veterinaria;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	// to string
	@Override
	public String toString() {
		return "HistoriaClinica [id=" + id + ", diagnostico=" + diagnostico + ", tratamiento=" + tratamiento
				+ ", fecha=" + fecha + ", mascota=" + mascota + ", veterinario=" + veterinario + ", veterinaria="
				+ veterinaria + ", usuario=" + usuario + "]";
	}

}
