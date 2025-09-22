package com.clinicpet.demo.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "PerfilAdministrador")
public class PerfilAdmin {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	private String nombres;
	private String apellidos;
	private String correo;
	private String telefono;
	private String cedula;
	private String foto; // guardarás la URL o nombre de archivo

	// relaciones
	@ManyToOne
	@JoinColumn(name = "idRol")
	private Rol rol;

	// Constructor vacio
	public PerfilAdmin() {
	}

	// constructor con campos
	public PerfilAdmin(Integer id, String nombres, String apellidos, String correo, String telefono, String cedula,
			String foto, Rol rol) {
		super();
		this.id = id;
		this.nombres = nombres;
		this.apellidos = apellidos;
		this.correo = correo;
		this.telefono = telefono;
		this.cedula = cedula;
		this.foto = foto;
		this.rol = rol;
	}

	// getters y setters
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getNombres() {
		return nombres;
	}

	public void setNombres(String nombres) {
		this.nombres = nombres;
	}

	public String getApellidos() {
		return apellidos;
	}

	public void setApellidos(String apellidos) {
		this.apellidos = apellidos;
	}

	public String getCorreo() {
		return correo;
	}

	public void setCorreo(String correo) {
		this.correo = correo;
	}

	public String getTelefono() {
		return telefono;
	}

	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}

	public String getCedula() {
		return cedula;
	}

	public void setCedula(String cedula) {
		this.cedula = cedula;
	}

	public String getFoto() {
		return foto;
	}

	public void setFoto(String foto) {
		this.foto = foto;
	}

	public Rol getRol() {
		return rol;
	}

	public void setRol(Rol rol) {
		this.rol = rol;
	}

	@Override
	public String toString() {
		return "PerfilAdmin [id=" + id + ", nombres=" + nombres + ", apellidos=" + apellidos + ", correo=" + correo
				+ ", telefono=" + telefono + ", cedula=" + cedula + ", foto=" + foto + "]";
	}

}
