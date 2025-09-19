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
@Table(name = "perfilveterinario")
public class PerfilVeterinario {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	private String especialidad;
	private String tarjetaProfesional;
	private String estado;

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

	// constructor con campos
	public PerfilVeterinario(Integer id, String especialidad, String tarjetaProfesional, String telefono, String estado,
			Usuario usuario, List<Cita> cita, List<Emergencia> emergencia) {
		super();
		this.id = id;
		this.especialidad = especialidad;
		this.tarjetaProfesional = tarjetaProfesional;
		this.estado = estado;
		this.usuario = usuario;
		this.cita = cita;
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

	public String getTarjetaProfesional() {
		return tarjetaProfesional;
	}

	public void setTarjetaProfesional(String tarjetaProfesional) {
		this.tarjetaProfesional = tarjetaProfesional;
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
		return "PerfilVeterinario [id=" + id + ", especialidad=" + especialidad + ", tarjetaProfesional="
				+ tarjetaProfesional + ", estado=" + estado + "]";
	}

}
