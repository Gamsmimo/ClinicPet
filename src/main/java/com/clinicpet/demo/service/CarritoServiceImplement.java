package com.clinicpet.demo.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.clinicpet.demo.model.Carrito;
import com.clinicpet.demo.repository.ICarritoRepository;

@Service
public class CarritoServiceImplement implements ICarritoService {

	@Autowired
	private ICarritoRepository carritoRepository;

	// CRUD

	@Override
	public Carrito guardarCarrito(Carrito carrito) {
		return carritoRepository.save(carrito);
	}

	@Override
	public List<Carrito> listarCarritos(Carrito carrito) {
		return carritoRepository.findAll();
	}

	@Override
	public Optional<Carrito> buscarPorId(Integer id) {
		return carritoRepository.findById(id);
	}

	@Override
	public void eliminarCarrito(Integer id) {
	}

	// METODOS PERSONALIZADOS

	@Override
	public List<Carrito> buscarPorUsuario(Integer usuarioId) {
		return carritoRepository.findByUsuarioId(usuarioId);
	}

	@Override
	public List<Carrito> buscarPorUsuarioYEstado(Integer usuarioId, String estado) {
		return carritoRepository.findByUsuarioId(usuarioId);
	}

	@Override
	public List<Carrito> buscarPorEstado(String estado) {
		return carritoRepository.findByEstado(estado);
	}

}
