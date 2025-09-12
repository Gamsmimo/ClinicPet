package com.clinicpet.demo.service;

import com.clinicpet.demo.model.Producto;
import com.clinicpet.demo.repository.IProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductoService implements IProductoService {

	@Autowired
	private IProductoRepository productoRepository;

	@Override
	public Producto crearProducto(Producto producto) {
		return productoRepository.save(producto);
	}

	@Override
	public Optional<Producto> obtenerProductoPorId(Integer id) {
		return productoRepository.findById(id);
	}

	@Override
	public List<Producto> obtenerTodosLosProductos() {
		return productoRepository.findAll();
	}

	@Override
	public Producto actualizarProducto(Integer id, Producto producto) {
		if (productoRepository.existsById(id)) {
			producto.setId(id);
			return productoRepository.save(producto);
		}
		return null;
	}

	@Override
	public void eliminarProducto(Integer id) {
		productoRepository.deleteById(id);
	}

	@Override
	public List<Producto> obtenerProductosPorNombre(String nombre) {
		return productoRepository.findByNombreContainingIgnoreCase(nombre);
	}

	@Override
	public List<Producto> obtenerProductosPorDescripcion(String descripcion) {
		return productoRepository.findByDescripcionContainingIgnoreCase(descripcion);
	}

	@Override
	public List<Producto> obtenerProductosPorRangoPrecio(Double precioMin, Double precioMax) {
		return productoRepository.findByPrecioBetween(precioMin, precioMax);
	}

	@Override
	public List<Producto> obtenerProductosConStockDisponible(Integer stockMinimo) {
		return productoRepository.findByStockGreaterThan(stockMinimo);
	}

	@Override
	public List<Producto> obtenerProductosConStockBajo(Integer stockMaximo) {
		return productoRepository.findByStockLessThanEqual(stockMaximo);
	}

	@Override
	public List<Producto> obtenerProductosPorPrecioMayorIgual(Double precioMin) {
		return productoRepository.findByPrecioGreaterThanEqual(precioMin);
	}

	@Override
	public List<Producto> obtenerProductosPorPrecioMenorIgual(Double precioMax) {
		return productoRepository.findByPrecioLessThanEqual(precioMax);
	}

	@Override
	public List<Producto> obtenerProductosMasVendidos() {
		return productoRepository.findProductosMasVendidos();
	}

	@Override
	public List<Producto> obtenerProductosEnStockOrdenadosPorNombre(Integer stock) {
		return productoRepository.findByStockGreaterThanOrderByNombreAsc(stock);
	}

	@Override
	public boolean existeProductoPorNombre(String nombre) {
		return productoRepository.existsByNombre(nombre);
	}

	@Override
	public List<Producto> buscarProductosPorTexto(String texto) {
		return productoRepository.buscarPorNombreODescripcion(texto);
	}
}
