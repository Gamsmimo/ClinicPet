package com.clinicpet.demo.repository;

import com.clinicpet.demo.model.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IProductoRepository extends JpaRepository<Producto, Integer> {
    
    List<Producto> findByNombreContainingIgnoreCase(String nombre);
    List<Producto> findByDescripcionContainingIgnoreCase(String descripcion);
    List<Producto> findByPrecioBetween(Double precioMin, Double precioMax);
    List<Producto> findByStockGreaterThan(Integer stock);
    List<Producto> findByStockLessThan(Integer stock);
    List<Producto> findByStock(Integer stock);
    boolean existsByNombre(String nombre);
    
    // Método adicional si quieres buscar por nombre o descripción
    @Query("SELECT p FROM Producto p WHERE LOWER(p.nombre) LIKE LOWER(CONCAT('%', :texto, '%')) OR LOWER(p.descripcion) LIKE LOWER(CONCAT('%', :texto, '%'))")
    List<Producto> buscarPorNombreODescripcion(@Param("texto") String texto);
}