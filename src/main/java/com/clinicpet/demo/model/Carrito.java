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
@Table(name = "carrito")
public class Carrito {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	private String estado;

	@ManyToOne
	@JoinColumn(name = "idUsuario", nullable = false)
	private Usuario usuario;

	@OneToMany(mappedBy = "carrito", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<CarritoProducto> productos;

	// constructor vacio
	public Carrito() {
	}

	// constructor con campos
	public Carrito(Integer id, String estado, Usuario usuario, List<CarritoProducto> productos) {
		super();
		this.id = id;
		this.estado = estado;
		this.usuario = usuario;
		this.productos = productos;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
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

	public List<CarritoProducto> getProductos() {
		return productos;
	}

	public void setProductos(List<CarritoProducto> productos) {
		this.productos = productos;
	}

	@Override
	public String toString() {
		return "Carrito [id=" + id + ", estado=" + estado + "]";
	}

}
