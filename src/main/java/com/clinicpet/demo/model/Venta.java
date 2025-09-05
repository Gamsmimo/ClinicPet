package com.clinicpet.demo.model;

import java.sql.Date;
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
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

@Entity
@Table(name = "venta")
public class Venta {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer idVentas;
	@ManyToOne
	@JoinColumn(name = "idUsuario")
	private Usuario usuario;

	@Temporal(TemporalType.TIMESTAMP)
	private Date fecha;

	private Double subtotal;
	private Double total;

	@OneToMany(mappedBy = "venta", cascade = CascadeType.ALL)
	private List<DetalleVenta> detallesVenta;

	@OneToMany(mappedBy = "venta", cascade = CascadeType.ALL)
	private Pago pago;

	public Venta() {

	}

	public Venta(Integer idVentas, Usuario usuario, Date fecha, Double subtotal, Double total,
			List<DetalleVenta> detallesVenta, Pago pago) {
		super();
		this.idVentas = idVentas;
		this.usuario = usuario;
		this.fecha = fecha;
		this.subtotal = subtotal;
		this.total = total;
		this.detallesVenta = detallesVenta;
		this.pago = pago;
	}

	public Integer getIdVentas() {
		return idVentas;
	}

	public void setIdVentas(Integer idVentas) {
		this.idVentas = idVentas;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public Date getFecha() {
		return fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	public Double getSubtotal() {
		return subtotal;
	}

	public void setSubtotal(Double subtotal) {
		this.subtotal = subtotal;
	}

	public Double getTotal() {
		return total;
	}

	public void setTotal(Double total) {
		this.total = total;
	}

	public List<DetalleVenta> getDetallesVenta() {
		return detallesVenta;
	}

	public void setDetallesVenta(List<DetalleVenta> detallesVenta) {
		this.detallesVenta = detallesVenta;
	}

	public Pago getPago() {
		return pago;
	}

	public void setPago(Pago pago) {
		this.pago = pago;
	}

	@Override
	public String toString() {
		return "Venta [idVentas=" + idVentas + ", usuario=" + usuario + ", fecha=" + fecha + ", subtotal=" + subtotal
				+ ", total=" + total + "]";
	}

}
