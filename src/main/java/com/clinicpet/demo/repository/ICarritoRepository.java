package com.clinicpet.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.clinicpet.demo.model.Carrito;

@Repository
public interface ICarritoRepository extends JpaRepository<Carrito, Integer> {

	// buscar todos los carritos de un usuario
	List<Carrito> findByUsuarioId(Integer usuaruioId);

	// buscar carritos de un usuario por su estado
	List<Carrito> finfByUsuarioIdAndEstado(Integer usuarioId, String estado);

	// buscar todos los carritos por estado
	List<Carrito> findByEstado(String estado);
}
