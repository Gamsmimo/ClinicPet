package com.clinicpet.demo.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.clinicpet.demo.model.Inventario;

@Repository
public interface IInventarioRepository extends JpaRepository<Inventario, Integer> {

	List<Inventario> findByVeterinaria_VeterinariaId(Integer veterinariaId);

	// Buscar inventario de un producto en todas las veterinarias.
	List<Inventario> findByProducto_ProductoId(Integer productoId);

	// Buscar inventario de un producto en una vatyerinaria especifica (unico por
	// resriccion)
	Optional<Inventario> findByVeterinaria_VterinariaIdAndProducto_ProductoId(Integer veterinariaId);

	// Buscar inventarios con una cantidad menor que un valor.
	List<Inventario> findByCantidadDisponibleLessThan(Integer cantidadDisponible);

	// Buscar inventarios con una cantidad mayor o igual a cierto valor.
	List<Inventario> findByCantidadDisponibleGreaterThanEqual(Integer cantidadDisponible);

}
