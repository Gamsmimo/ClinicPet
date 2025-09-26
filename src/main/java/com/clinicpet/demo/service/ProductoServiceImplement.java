package com.clinicpet.demo.service;

import com.clinicpet.demo.model.Producto;
import com.clinicpet.demo.repository.IProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class ProductoServiceImplement implements IProductoService {

	@Autowired
	private IProductoRepository productoRepository;

	@Override
	@Transactional
	public Producto crearProducto(Producto producto) {
		return productoRepository.save(producto);
	}

	@Override
	@Transactional(readOnly = true)
	public Optional<Producto> obtenerProductoPorId(Integer id) {
		return productoRepository.findById(id);
	}

	@Override
	@Transactional(readOnly = true)
	public List<Producto> obtenerTodosLosProductos() {
		return productoRepository.findAll();
	}

	@Override
	@Transactional(readOnly = true)
	public List<Producto> buscarProductosPorNombre(String nombre) {
		return productoRepository.findByNombreContainingIgnoreCase(nombre);
	}

	@Override
	@Transactional(readOnly = true)
	public List<Producto> buscarProductosPorDescripcion(String descripcion) {
		return productoRepository.findByDescripcionContainingIgnoreCase(descripcion);
	}

	@Override
	@Transactional(readOnly = true)
	public List<Producto> buscarProductosPorRangoPrecio(Double precioMin, Double precioMax) {
		return productoRepository.findByPrecioBetween(precioMin, precioMax);
	}

	@Override
	@Transactional(readOnly = true)
	public List<Producto> obtenerProductosConStockDisponible() {
		return productoRepository.findByStockGreaterThan(0);
	}

	@Override
	@Transactional(readOnly = true)
	public List<Producto> obtenerProductosConStockBajo(Integer stockMaximo) {
		// Cambiado a stock menor que el máximo especificado
		return productoRepository.findByStockLessThan(stockMaximo);
	}

	@Override
	@Transactional(readOnly = true)
	public List<Producto> obtenerProductosMasVendidos() {
		// Implementación alternativa - ordenar por stock descendente
		return productoRepository.findAll(Sort.by(Sort.Direction.DESC, "stock"));
	}

	@Override
	@Transactional(readOnly = true)
	public List<Producto> buscarProductosPorTexto(String texto) {
		// Implementación alternativa - buscar por nombre
		return productoRepository.findByNombreContainingIgnoreCase(texto);
	}

	@Override
	@Transactional
	public Producto actualizarProducto(Integer id, Producto producto) {
		if (productoRepository.existsById(id)) {
			producto.setId(id);
			return productoRepository.save(producto);
		}
		throw new RuntimeException("Producto no encontrado con ID: " + id);
	}

	@Override
	@Transactional
	public Producto actualizarStockProducto(Integer id, Integer nuevoStock) {
		Optional<Producto> productoOpt = productoRepository.findById(id);
		if (productoOpt.isPresent()) {
			Producto producto = productoOpt.get();
			producto.setStock(nuevoStock);
			return productoRepository.save(producto);
		}
		throw new RuntimeException("Producto no encontrado con ID: " + id);
	}

	@Override
	@Transactional
	public void eliminarProducto(Integer id) {
		if (productoRepository.existsById(id)) {
			productoRepository.deleteById(id);
		} else {
			throw new RuntimeException("Producto no encontrado con ID: " + id);
		}
	}

	@Override
	@Transactional(readOnly = true)
	public boolean existeProductoPorId(Integer id) {
		return productoRepository.existsById(id);
	}

	@Override
	@Transactional(readOnly = true)
	public boolean existeProductoPorNombre(String nombre) {
		return productoRepository.existsByNombre(nombre);
	}

	@Override
	@Transactional(readOnly = true)
	public long contarTotalProductos() {
		return productoRepository.count();
	}

	@Override
	@Transactional(readOnly = true)
	public long contarProductosSinStock() {
		// Contar productos con stock = 0
		return productoRepository.findByStock(0).size();
	}
}