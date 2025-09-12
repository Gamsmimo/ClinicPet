package com.clinicpet.demo.service;

import com.clinicpet.demo.model.Producto;
import java.util.List;
import java.util.Optional;

public interface IProductoService {
	Producto crearProducto(Producto producto);

	Optional<Producto> obtenerProductoPorId(Integer id);

	List<Producto> obtenerTodosLosProductos();

	Producto actualizarProducto(Integer id, Producto producto);

	void eliminarProducto(Integer id);

	List<Producto> obtenerProductosPorNombre(String nombre);

	List<Producto> obtenerProductosPorDescripcion(String descripcion);

	List<Producto> obtenerProductosPorRangoPrecio(Double precioMin, Double precioMax);

	List<Producto> obtenerProductosConStockDisponible(Integer stockMinimo);

	List<Producto> obtenerProductosConStockBajo(Integer stockMaximo);

	List<Producto> obtenerProductosPorPrecioMayorIgual(Double precioMin);

	List<Producto> obtenerProductosPorPrecioMenorIgual(Double precioMax);

	List<Producto> obtenerProductosMasVendidos();

	List<Producto> obtenerProductosEnStockOrdenadosPorNombre(Integer stock);

	boolean existeProductoPorNombre(String nombre);

	List<Producto> buscarProductosPorTexto(String texto);
}