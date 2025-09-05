package com.clinicpet.demo.model;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "servicio")
public class Servicio {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer idServicio;

	@ManyToOne
	@JoinColumn(name = "rol_id")
	private Veterinaria veterinaria;

	private String nombre;
	private String descripcion;
	private Double precioBase;

	@OneToMany(mappedBy = "servicio", cascade = CascadeType.ALL)
	private List<Cita> citas;

	@OneToMany(mappedBy = "servicio", cascade = CascadeType.ALL)
	private List<Calificacion> calificaciones;

	public Servicio() {

	}

	public Servicio(Integer idServicio, Veterinaria veterinaria, String nombre, String descripcion, Double precioBase,
			List<Cita> citas, List<Calificacion> calificaciones) {
		super();
		this.idServicio = idServicio;
		this.veterinaria = veterinaria;
		this.nombre = nombre;
		this.descripcion = descripcion;
		this.precioBase = precioBase;
		this.citas = citas;
		this.calificaciones = calificaciones;
	}

	public Integer getIdServicio() {
		return idServicio;
	}

	public void setIdServicio(Integer idServicio) {
		this.idServicio = idServicio;
	}

	public Veterinaria getVeterinaria() {
		return veterinaria;
	}

	public void setVeterinaria(Veterinaria veterinaria) {
		this.veterinaria = veterinaria;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public Double getPrecioBase() {
		return precioBase;
	}

	public void setPrecioBase(Double precioBase) {
		this.precioBase = precioBase;
	}

	public List<Cita> getCitas() {
		return citas;
	}

	public void setCitas(List<Cita> citas) {
		this.citas = citas;
	}

	public List<Calificacion> getCalificaciones() {
		return calificaciones;
	}

	public void setCalificaciones(List<Calificacion> calificaciones) {
		this.calificaciones = calificaciones;
	}

	@Override
	public String toString() {
		return "Servicio [idServicio=" + idServicio + ", nombre=" + nombre + ", descripcion=" + descripcion
				+ ", precioBase=" + precioBase + "]";
	}

}
