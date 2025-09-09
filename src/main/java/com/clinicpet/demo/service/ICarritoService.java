package com.clinicpet.demo.service;

import java.util.List;
import java.util.Optional;

import com.clinicpet.demo.model.Carrito;

public interface ICarritoService {

	// CRUD

	Carrito guardarCarrito(Carrito carrito);

	List<Carrito> listarCarritos(Carrito carrito);

	Optional<Carrito> buscarPorId(Integer id);

	void eliminarCarrito(Integer id);

	// METODOS PERSONALIZADOS

	List<Carrito> buscarPorUsuario(Integer usuarioId);

	List<Carrito> buscarPorUsuarioYEstado(Integer usuarioId, String estado);

	List<Carrito> buscarPorEstado(String estado);

}
