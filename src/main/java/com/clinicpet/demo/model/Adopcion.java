package com.clinicpet.demo.model;

import java.util.Date;

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
	private Date fechaSolicitud;
	private String estado;

	@ManyToOne
	@JoinColumn(name = "idUsuarioAdoptante", nullable = false)
	private Usuario usuarioAdoptante;

	@ManyToOne
	@JoinColumn(name = "idVeterinaria", nullable = false)
	private Veterinaria veterinaria;

	@ManyToOne
	@JoinColumn(name = "idMascota")
	private Mascota mascota;

	// constructos vacio
	public Adopcion() {
	}

	// constructor con campos
	public Adopcion(Integer id, Date fechaSolicitud, String estado, Usuario usuarioAdoptante, Veterinaria veterinaria,
			Mascota mascota) {
		super();
		this.id = id;
		this.fechaSolicitud = fechaSolicitud;
		this.estado = estado;
		this.usuarioAdoptante = usuarioAdoptante;
		this.veterinaria = veterinaria;
		this.mascota = mascota;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Date getFechaSolicitud() {
		return fechaSolicitud;
	}

	public void setFechaSolicitud(Date fechaSolicitud) {
		this.fechaSolicitud = fechaSolicitud;
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

	public Mascota getMascota() {
		return mascota;
	}

	public void setMascota(Mascota mascota) {
		this.mascota = mascota;
	}

	@Override
	public String toString() {
		return "Adopcion [id=" + id + ", fechaSolicitud=" + fechaSolicitud + ", estado=" + estado + "]";
	}

}