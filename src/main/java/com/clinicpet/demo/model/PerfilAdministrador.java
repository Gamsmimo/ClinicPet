package com.clinicpet.demo.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "perfiladmin")
public class PerfilAdministrador {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	private String rol = "ADMIN";
	private String permisos; // Ej: "GESTION_USUARIOS, REPORTES, PRODUCTOS"

	// Relación con Usuario (el admin es un usuario del sistema)
	@OneToOne
	private Usuario usuario;

	public PerfilAdministrador() {
	}

	public PerfilAdministrador(Integer id, String rol, String permisos, Usuario usuario) {
		super();
		this.id = id;
		this.rol = rol;
		this.permisos = permisos;
		this.usuario = usuario;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getRol() {
		return rol;
	}

	public void setRol(String rol) {
		this.rol = rol;
	}

	public String getPermisos() {
		return permisos;
	}

	public void setPermisos(String permisos) {
		this.permisos = permisos;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	@Override
	public String toString() {
		return "PerfilAdministrador [id=" + id + ", rol=" + rol + ", permisos=" + permisos + "]";
	}

}
