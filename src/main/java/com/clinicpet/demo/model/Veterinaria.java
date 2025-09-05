package com.clinicpet.demo.model;

import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "Veterinaria")
public class Veterinaria {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer idVeterinaria;
	private String nombre;
	private String direccion;
	private String telefono;
	private String correo;
	private String horario;
	private String descripcion;

	@OneToMany(mappedBy = "veterinaria")
	private List<Adopcion> adopciones;

	@OneToMany(mappedBy = "veterinaria")
	private List<Servicio> servicios;

	@OneToMany(mappedBy = "veterinaria")
	private List<Inventario> inventarios;

	@OneToMany(mappedBy = "veterinaria")
	private List<HistoriaClinica> historiasClinicas;

	@OneToMany(mappedBy = "veterinaria")
	private List<Emergencia> emergencias;

	@OneToMany(mappedBy = "veterinaria")
	private List<VeterinariaVeterinario> veterinarios;

	public Veterinaria() {

	}

	public Veterinaria(Integer idVeterinaria, String nombre, String direccion, String telefono, String correo,
			String horario, String descripcion, List<Adopcion> adopciones, List<Servicio> servicios,
			List<Inventario> inventarios, List<HistoriaClinica> historiasClinicas, List<Emergencia> emergencias,
			List<VeterinariaVeterinario> veterinarios) {
		super();
		this.idVeterinaria = idVeterinaria;
		this.nombre = nombre;
		this.direccion = direccion;
		this.telefono = telefono;
		this.correo = correo;
		this.horario = horario;
		this.descripcion = descripcion;
		this.adopciones = adopciones;
		this.servicios = servicios;
		this.inventarios = inventarios;
		this.historiasClinicas = historiasClinicas;
		this.emergencias = emergencias;
		this.veterinarios = veterinarios;
	}

	public Integer getIdVeterinaria() {
		return idVeterinaria;
	}

	public void setIdVeterinaria(Integer idVeterinaria) {
		this.idVeterinaria = idVeterinaria;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
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

	public String getCorreo() {
		return correo;
	}

	public void setCorreo(String correo) {
		this.correo = correo;
	}

	public String getHorario() {
		return horario;
	}

	public void setHorario(String horario) {
		this.horario = horario;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public List<Adopcion> getAdopciones() {
		return adopciones;
	}

	public void setAdopciones(List<Adopcion> adopciones) {
		this.adopciones = adopciones;
	}

	public List<Servicio> getServicios() {
		return servicios;
	}

	public void setServicios(List<Servicio> servicios) {
		this.servicios = servicios;
	}

	public List<Inventario> getInventarios() {
		return inventarios;
	}

	public void setInventarios(List<Inventario> inventarios) {
		this.inventarios = inventarios;
	}

	public List<HistoriaClinica> getHistoriasClinicas() {
		return historiasClinicas;
	}

	public void setHistoriasClinicas(List<HistoriaClinica> historiasClinicas) {
		this.historiasClinicas = historiasClinicas;
	}

	public List<Emergencia> getEmergencias() {
		return emergencias;
	}

	public void setEmergencias(List<Emergencia> emergencias) {
		this.emergencias = emergencias;
	}

	public List<VeterinariaVeterinario> getVeterinarios() {
		return veterinarios;
	}

	public void setVeterinarios(List<VeterinariaVeterinario> veterinarios) {
		this.veterinarios = veterinarios;
	}

	@Override
	public String toString() {
		return "Veterinaria [idVeterinaria=" + idVeterinaria + ", nombre=" + nombre + ", direccion=" + direccion
				+ ", telefono=" + telefono + ", correo=" + correo + ", horario=" + horario + ", descripcion="
				+ descripcion + "]";
	}

}
