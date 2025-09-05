package com.clinicpet.demo.model;

import java.util.List;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;

public class PerfilVeterinario {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	private String especialidad;
	private String rut; // registro único profesional
	private String telefono;

	// Relación con Usuario
	@OneToOne
	private Usuario usuario;

	// Relación con citas
	@OneToMany(mappedBy = "veterinario")
	private List<Cita> cita;

	// Relación con emergencia
	@OneToMany(mappedBy = "veterinario")
	private List<Emergencia> emergencia;

	// constructor vacio
	public PerfilVeterinario() {
	}

	public PerfilVeterinario(Integer id, String especialidad, String rut, String telefono, Usuario usuario,
			List<Cita> citas, List<Emergencia> emergencia) {
		super();
		this.id = id;
		this.especialidad = especialidad;
		this.rut = rut;
		this.telefono = telefono;
		this.usuario = usuario;
		this.cita = citas;
		this.emergencia = emergencia;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getEspecialidad() {
		return especialidad;
	}

	public void setEspecialidad(String especialidad) {
		this.especialidad = especialidad;
	}

	public String getRut() {
		return rut;
	}

	public void setRut(String rut) {
		this.rut = rut;
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

	public List<Cita> getCita() {
		return cita;
	}

	public void setCita(List<Cita> cita) {
		this.cita = cita;
	}

	public List<Emergencia> getEmergencia() {
		return emergencia;
	}

	public void setEmergencia(List<Emergencia> emergencia) {
		this.emergencia = emergencia;
	}

	@Override
	public String toString() {
		return "PerfilVeterinario [id=" + id + ", especialidad=" + especialidad + ", rut=" + rut + ", telefono="
				+ telefono + "]";
	}

}
