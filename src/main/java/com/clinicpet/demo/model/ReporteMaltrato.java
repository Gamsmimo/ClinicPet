package com.clinicpet.demo.model;

import java.util.Date;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

public class ReporteMaltrato {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	private Date fecha;
	private String descripcion;
	private String estado; // pendiente, en revisión, resuelto

	// Relación con Usuario que reporta
	@ManyToOne
	private Usuario usuario;

	// Relación opcional con Mascota
	@ManyToOne
	private Mascota mascota;

	// constructor vacio
	public ReporteMaltrato() {
	}

//constructor con campos 
	public ReporteMaltrato(Integer id, Date fecha, String descripcion, String estado, Usuario usuario,
			Mascota mascota) {
		super();
		this.id = id;
		this.fecha = fecha;
		this.descripcion = descripcion;
		this.estado = estado;
		this.usuario = usuario;
		this.mascota = mascota;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Date getFecha() {
		return fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
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

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public Mascota getMascota() {
		return mascota;
	}

	public void setMascota(Mascota mascota) {
		this.mascota = mascota;
	}

	@Override
	public String toString() {
		return "ReporteMaltrato [id=" + id + ", fecha=" + fecha + ", descripcion=" + descripcion + ", estado=" + estado
				+ "]";
	}

}
