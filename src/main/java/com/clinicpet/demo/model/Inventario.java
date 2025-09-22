package com.clinicpet.demo.model;

import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity
@Table(name = "Inventario", uniqueConstraints = { @UniqueConstraint(columnNames = { "idVeterinaria", "idProducto" }) })
public class Inventario {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	private Integer cantidadDisponible;
	private LocalDate fechaActualizacion;

	@ManyToOne
	@JoinColumn(name = "idVeterinaria", nullable = false)
	private Veterinaria veterinaria;

	@ManyToOne
	@JoinColumn(name = "idProducto", nullable = false)
	private Producto producto;

	// constructor vacio
	public Inventario() {

	}

	// constructor con campos
	public Inventario(Integer id, Integer cantidadDisponible, LocalDate fechaActualizacion, Veterinaria veterinaria,
			Producto producto) {
		super();
		this.id = id;
		this.cantidadDisponible = cantidadDisponible;
		this.fechaActualizacion = fechaActualizacion;
		this.veterinaria = veterinaria;
		this.producto = producto;
	}

	// getters & setters
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getCantidadDisponible() {
		return cantidadDisponible;
	}

	public void setCantidadDisponible(Integer cantidadDisponible) {
		this.cantidadDisponible = cantidadDisponible;
	}

	public LocalDate getFechaActualizacion() {
		return fechaActualizacion;
	}

	public void setFechaActualizacion(LocalDate fechaActualizacion) {
		this.fechaActualizacion = fechaActualizacion;
	}

	public Veterinaria getVeterinaria() {
		return veterinaria;
	}

	public void setVeterinaria(Veterinaria veterinaria) {
		this.veterinaria = veterinaria;
	}

	public Producto getProducto() {
		return producto;
	}

	public void setProducto(Producto producto) {
		this.producto = producto;
	}

	// to string
	@Override
	public String toString() {
		return "Inventario [id=" + id + ", cantidadDisponible=" + cantidadDisponible + ", fechaActualizacion="
				+ fechaActualizacion + ", veterinaria=" + veterinaria + ", producto=" + producto + "]";
	}

}
