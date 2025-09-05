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
@Table(name = "adopcion")
public class Adopcion {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	private LocalDate FechaSolicitud;
	private String estado;

	@ManyToOne
	@JoinColumn(name = "idUsuarioAdoptante", nullable = false)
	private Usuario usuarioAdoptante;

	@ManyToOne
	@JoinColumn(name = "idVeterinaria", nullable = false)
	private Veterinaria veterinaria;

	// constructos vacio
	public Adopcion() {
	}

	// constructor con campos
	public Adopcion(Integer id, LocalDate fechaSolicitud, String estado, Usuario usuarioAdoptante,
			Veterinaria veterinaria) {
		super();
		this.id = id;
		this.FechaSolicitud = fechaSolicitud;
		this.estado = estado;
		this.usuarioAdoptante = usuarioAdoptante;
		this.veterinaria = veterinaria;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public LocalDate getFechaSolicitud() {
		return FechaSolicitud;
	}

	public void setFechaSolicitud(LocalDate fechaSolicitud) {
		FechaSolicitud = fechaSolicitud;
	}

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public Usuario getUsuarioAdoptante() {
		return usuarioAdoptante;
	}

	public void setUsuarioAdoptante(Usuario usuarioAdoptante) {
		this.usuarioAdoptante = usuarioAdoptante;
	}

	public Veterinaria getVeterinaria() {
		return veterinaria;
	}

	public void setVeterinaria(Veterinaria veterinaria) {
		this.veterinaria = veterinaria;
	}

	@Override
	public String toString() {
		return "Adopcion [id=" + id + ", FechaSolicitud=" + FechaSolicitud + ", estado=" + estado + "]";
	}

}
