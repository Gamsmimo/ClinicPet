package com.clinicpet.demo.service;

import com.clinicpet.demo.model.Producto;
import java.util.List;
import java.util.Optional;

public interface IProductoService {

	Producto crearProducto(Producto producto);

	Optional<Producto> obtenerProductoPorId(Integer id);

	List<Producto> obtenerTodosLosProductos();

	List<Producto> buscarProductosPorNombre(String nombre);

	List<Producto> buscarProductosPorDescripcion(String descripcion);

	List<Producto> buscarProductosPorRangoPrecio(Double precioMin, Double precioMax);

	List<Producto> obtenerProductosConStockDisponible();

	List<Producto> obtenerProductosConStockBajo(Integer stockMaximo);

	List<Producto> obtenerProductosMasVendidos();

	List<Producto> buscarProductosPorTexto(String texto);

	Producto actualizarProducto(Integer id, Producto producto);

	Producto actualizarStockProducto(Integer id, Integer nuevoStock);

	void eliminarProducto(Integer id);

	boolean existeProductoPorId(Integer id);

	boolean existeProductoPorNombre(String nombre);

	long contarTotalProductos();

	long contarProductosSinStock();
}