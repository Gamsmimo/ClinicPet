package com.clinicpet.demo.repository;

import com.clinicpet.demo.model.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

public interface IProductoRepository extends JpaRepository<Producto, Integer> {

	List<Producto> findByNombreContainingIgnoreCase(String nombre);

	List<Producto> findByDescripcionContainingIgnoreCase(String descripcion);

	List<Producto> findByPrecioBetween(Double precioMin, Double precioMax);

	List<Producto> findByStockGreaterThan(Integer stockMinimo);

	List<Producto> findByStockLessThanEqual(Integer stockMaximo);

	List<Producto> findByPrecioGreaterThanEqual(Double precioMin);

	List<Producto> findByPrecioLessThanEqual(Double precioMax);

	List<Producto> findByStockGreaterThanOrderByNombreAsc(Integer stock);

	boolean existsByNombre(String nombre);

	@Query("SELECT p FROM Producto p ORDER BY p.stock DESC")
	List<Producto> findProductosMasVendidos();

	@Query("SELECT p FROM Producto p WHERE LOWER(p.nombre) LIKE LOWER(CONCAT('%', :texto, '%')) "
			+ "OR LOWER(p.descripcion) LIKE LOWER(CONCAT('%', :texto, '%'))")
	List<Producto> buscarPorNombreODescripcion(String texto);
}
