package com.clinicpet.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.clinicpet.demo.model.Inventario;

public interface IInventarioRepository extends JpaRepository<Inventario, Integer> {
	
	// Buscar inventario por id de veterinaria
    List<Inventario> findByVeterinaria_Id(Integer veterinariaId);

    // Buscar inventario de un producto en todas las veterinarias
    List<Inventario> findByProducto_Id(Integer productoId);

    // Buscar inventario de un producto en una veterinaria específica
    List<Inventario> findByVeterinaria_IdAndProducto_Id(Integer veterinariaId, Integer productoId);

    // Buscar inventarios con cantidad menor a cierto valor
    List<Inventario> findByCantidadDisponibleLessThan(Integer cantidadDisponible);

    // Buscar inventarios con cantidad mayor o igual a cierto valor
    List<Inventario> findByCantidadDisponibleGreaterThanEqual(Integer cantidadDisponible);

}



