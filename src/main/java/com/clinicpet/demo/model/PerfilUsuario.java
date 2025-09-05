package com.clinicpet.demo.model;

import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "perfilrusuario")
public class PerfilUsuario {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	private String direccion;
	private String telefono;

	// Relación con Usuario
	@OneToOne
	private Usuario usuario;

	// Relación con Mascotas
	@OneToMany(mappedBy = "usuario")
	private List<Mascota> mascota;

	// constructor vacio
	public PerfilUsuario() {
	}

	// constructor con campos
	public PerfilUsuario(Integer id, String direccion, String telefono, Usuario usuario, List<Mascota> mascota) {
		super();
		this.id = id;
		this.direccion = direccion;
		this.telefono = telefono;
		this.usuario = usuario;
		this.mascota = mascota;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getDireccion() {
		return direccion;
	}

	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}

	public String getTelefono() {
		return telefono;
	}

	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public List<Mascota> getMascota() {
		return mascota;
	}

	public void setMascota(List<Mascota> mascota) {
		this.mascota = mascota;
	}

	@Override
	public String toString() {
		return "PerfilUsuario [id=" + id + ", direccion=" + direccion + ", telefono=" + telefono + "]";
	}

}
