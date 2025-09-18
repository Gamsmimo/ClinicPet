package com.clinicpet.demo.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.JoinColumn;

@Entity
@Table(name = "usuario")
public class Usuario {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	private String username;
	private String nombre;

	private String apellidos;
	private String correo;
	private String tipoDocumento;
	private String numDocumento;
	private String telefono;
	private String edad;
	private String contraseña;

	@ManyToOne
	@JoinColumn(name = "rol_id")
	private Rol rol;

	public Usuario() {

	}

	public Usuario(Integer id, String username, String nombre, String apellidos, String correo, String tipoDocumento,
			String numDocumento, String telefono, String edad, String contraseña, Rol rol) {

		super();
		this.id = id;
		this.username = username;
		this.nombre = nombre;

		this.apellidos = apellidos;
		this.correo = correo;
		this.tipoDocumento = tipoDocumento;
		this.numDocumento = numDocumento;
		this.telefono = telefono;
		this.edad = edad;
		this.contraseña = contraseña;
		this.rol = rol;
	}

	// getters y setters

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
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

	public String getTipoDocumento() {
		return tipoDocumento;
	}

	public void setTipoDocumento(String tipoDocumento) {
		this.tipoDocumento = tipoDocumento;
	}

	public String getNumDocumento() {
		return numDocumento;
	}

	public void setNumDocumento(String numDocumento) {
		this.numDocumento = numDocumento;
	}

	public String getTelefono() {
		return telefono;
	}

	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}

	public String getEdad() {
		return edad;
	}

	public void setEdad(String edad) {
		this.edad = edad;
	}

	public String getContraseña() {
		return contraseña;
	}

	public void setContraseña(String contraseña) {
		this.contraseña = contraseña;
	}

	public Rol getRol() {
		return rol;
	}

	public void setRol(Rol rol) {
		this.rol = rol;
	}

	@Override
	public String toString() {

		return "Usuario [id=" + id + ", username=" + username + ", nombre=" + nombre + ", apellidos=" + apellidos
				+ ", correo=" + correo + ", tipoDocumento=" + tipoDocumento + ", numDocumento=" + numDocumento
				+ ", telefono=" + telefono + ", edad=" + edad + ", rol=" + rol + "]";

	}

}