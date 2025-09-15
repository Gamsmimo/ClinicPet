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

	private String descripcion;

	private String imagen; // Si es null "default.jpg"

	private String contacto;

	// Relaciones
	@ManyToOne
	@JoinColumn(name = "idUsuarioAdoptante")
	private PerfilUsuario usuarioAdoptante;

	@ManyToOne
	@JoinColumn(name = "idVeterinaria", nullable = false)
	private Veterinaria veterinaria;

	@ManyToOne
	@JoinColumn(name = "idMascota", nullable = false)
	private Mascota mascota;

	// Constructores
	public Adopcion() {
	}

	public Adopcion(Integer id, Date fechaSolicitud, String estado, String descripcion, String imagen, String contacto,
			PerfilUsuario usuarioAdoptante, Veterinaria veterinaria, Mascota mascota) {
		this.id = id;
		this.fechaSolicitud = fechaSolicitud;
		this.estado = estado;
		this.descripcion = descripcion;
		this.imagen = imagen;
		this.contacto = contacto;
		this.usuarioAdoptante = usuarioAdoptante;
		this.veterinaria = veterinaria;
		this.mascota = mascota;
	}

	// Getters y Setters
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

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public String getImagen() {
		return imagen;
	}

	public void setImagen(String imagen) {
		this.imagen = imagen;
	}

	public String getContacto() {
		return contacto;
	}

	public void setContacto(String contacto) {
		this.contacto = contacto;
	}

	public PerfilUsuario getUsuarioAdoptante() {
		return usuarioAdoptante;
	}

	public void setUsuarioAdoptante(PerfilUsuario usuarioAdoptante) {
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
		return "Adopcion [id=" + id + ", fechaSolicitud=" + fechaSolicitud + ", estado=" + estado + ", descripcion="
				+ descripcion + ", imagen=" + imagen + ", contacto=" + contacto + ", usuarioAdoptante="
				+ (usuarioAdoptante != null ? usuarioAdoptante.getId() : null) + ", veterinaria="
				+ (veterinaria != null ? veterinaria.getId() : null) + ", mascota="
				+ (mascota != null ? mascota.getId() : null) + "]";
	}
}
