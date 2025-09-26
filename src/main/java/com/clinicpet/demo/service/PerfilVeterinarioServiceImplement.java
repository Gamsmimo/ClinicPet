package com.clinicpet.demo.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.clinicpet.demo.model.PerfilVeterinario;
import com.clinicpet.demo.repository.IPerfilVeterinarioRepository;

@Service
public class PerfilVeterinarioServiceImplement implements IPerfilVeterinarioService {

	@Autowired
	private IPerfilVeterinarioRepository perfilVeterinarioRepository;

	@Override
	public PerfilVeterinario guardarPerfil(PerfilVeterinario perfil) {
		// TODO Auto-generated method stub
		return perfilVeterinarioRepository.save(perfil);
	}

	@Override
	public Optional<PerfilVeterinario> buscarPorId(Integer id) {
		// TODO Auto-generated method stub
		return perfilVeterinarioRepository.findById(id);
	}

	@Override
	public Optional<PerfilVeterinario> buscarPorUsuarioId(Integer usuarioId) {
		// TODO Auto-generated method stub
		return perfilVeterinarioRepository.findByUsuarioId(usuarioId);
	}

	@Override
	public Optional<PerfilVeterinario> buscarPorUsuarioCorreo(String correo) {
		// TODO Auto-generated method stub
		return perfilVeterinarioRepository.findByUsuarioCorreo(correo);
	}

	@Override
	public Optional<PerfilVeterinario> buscarPorUsuarioNumDocumento(String numDocumento) {
		// TODO Auto-generated method stub
		return perfilVeterinarioRepository.findByUsuarioNumDocumento(numDocumento);
	}

	@Override
	public PerfilVeterinario actualizarPerfil(Integer id, PerfilVeterinario perfilActualizado) {
		// TODO Auto-generated method stub
		Optional<PerfilVeterinario> perfilExistente = perfilVeterinarioRepository.findById(id);

		if (perfilExistente.isPresent()) {
			PerfilVeterinario perfil = perfilExistente.get();

			// ACTUALIZAR CAMPOS DEL PERFIL VETERINARIO
			perfil.setEspecialidad(perfilActualizado.getEspecialidad());
			perfil.setExperiencia(perfilActualizado.getExperiencia());
			perfil.setVeterinaria(perfilActualizado.getVeterinaria());

			// ACTUALIZAR CAMPOS DEL USUARIO ASOCIADO
			if (perfilActualizado.getUsuario() != null) {
				// Actualizar solo campos permitidos
				perfil.getUsuario().setNombres(perfilActualizado.getUsuario().getNombres());
				perfil.getUsuario().setCorreo(perfilActualizado.getUsuario().getCorreo());
				perfil.getUsuario().setTelefono(perfilActualizado.getUsuario().getTelefono());
			}

			return perfilVeterinarioRepository.save(perfil);
		}
		throw new RuntimeException("Perfil no encontrado");
	}

	@Override
	public boolean existePorUsuarioCorreo(String correo) {
		// TODO Auto-generated method stub
		return perfilVeterinarioRepository.existsByUsuarioCorreo(correo);
	}

	@Override
	public boolean existePorUsuarioNumDocumento(String numDocumento) {
		// TODO Auto-generated method stub
		return perfilVeterinarioRepository.existsByUsuarioNumDocumento(numDocumento);
	}

}