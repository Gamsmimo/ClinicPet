package com.clinicpet.demo.service;

<<<<<<< HEAD
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.clinicpet.demo.model.PerfilVeterinario;
import com.clinicpet.demo.repository.IPerfilVeterinarioRepository;
=======
import com.clinicpet.demo.model.PerfilVeterinario;
import com.clinicpet.demo.repository.IPerfilVeterinarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
>>>>>>> 21ec2e8465c2f8d5e6595cb364bf21309a264dc8

@Service
public class PerfilVeterinarioServiceImplement implements IPerfilVeterinarioService {

	@Autowired
	private IPerfilVeterinarioRepository perfilVeterinarioRepository;

	@Override
<<<<<<< HEAD
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
=======
	@Transactional
	public PerfilVeterinario crearPerfilVeterinario(PerfilVeterinario perfilVeterinario) {
		return perfilVeterinarioRepository.save(perfilVeterinario);
	}

	@Override
	@Transactional
	public PerfilVeterinario findById(Integer id) {
		return perfilVeterinarioRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("Veterinario no encontrado"));
	}

	@Transactional(readOnly = true)
	public PerfilVeterinario obtenerPerfilVeterinarioPorUsuarioId(Integer usuarioId) {
>>>>>>> 21ec2e8465c2f8d5e6595cb364bf21309a264dc8
		return perfilVeterinarioRepository.findByUsuarioId(usuarioId);
	}

	@Override
<<<<<<< HEAD
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
=======
	@Transactional(readOnly = true)
	public PerfilVeterinario obtenerPerfilVeterinarioPorNombres(String nombres) {
		return perfilVeterinarioRepository.findByUsuarioNombres(nombres);
	}

	@Override
	@Transactional(readOnly = true)
	public List<PerfilVeterinario> obtenerTodosLosPerfilesVeterinarios() {

		return perfilVeterinarioRepository.findAll();
	}

	@Override
	public PerfilVeterinario actualizarPerfilVeterinario(Integer id, PerfilVeterinario perfilVeterinario) {
		if (perfilVeterinarioRepository.existsById(id)) {
			perfilVeterinario.setId(id);
			return perfilVeterinarioRepository.save(perfilVeterinario);
		}
		return null;
	}

	@Transactional(readOnly = true)
	public List<PerfilVeterinario> obtenerVeterinariosPorEspecialidad(String especialidad) {
		return perfilVeterinarioRepository.findByEspecialidad(especialidad);
	}

	@Override
	@Transactional(readOnly = true)
	public List<PerfilVeterinario> obtenerVeterinariosDisponibles() {
		return perfilVeterinarioRepository.findVeterinariosDisponibles();
	}

	@Transactional(readOnly = true)
	public boolean existePerfilVeterinarioPorId(Integer id) {
		return perfilVeterinarioRepository.existsById(id);
	}

	@Override
	public List<PerfilVeterinario> obtenerTodosLosPerfilesVeterinario() {
		return perfilVeterinarioRepository.findAll();
	}

	@Override
	public PerfilVeterinario obtenerVeterinarioPorTarjetaProfesional(String tarjetaProfesional) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Optional<PerfilVeterinario> obtenerPerfilVeterinarioPorTargetaProfesional(String tarjetaProfesional) {
		return Optional.empty();
	}

	@Override
	public List<PerfilVeterinario> ListarPorEstado(String estado) {
		return perfilVeterinarioRepository.findByEstado(estado);
	}

	@Override
	@Transactional
	public PerfilVeterinario desactivarVeterinario(Integer id) {
		PerfilVeterinario veterinario = perfilVeterinarioRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("Veterinario no encontrado"));
		veterinario.setEstado("Inactiva"); // cambiar estado a inactiva
		return perfilVeterinarioRepository.save(veterinario);
	}

	@Override
	public PerfilVeterinario activarVeterinario(Integer id) {
		PerfilVeterinario veterinario = findById(id);
		veterinario.setEstado("Aprobada");
		return perfilVeterinarioRepository.save(veterinario);
	}

	@Override
	@Transactional
	public void aprobarVeterinario(Integer id) {
		PerfilVeterinario veterinario = perfilVeterinarioRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("Veterinario no encontrado"));
		veterinario.setEstado("Aprobada"); // cambiar estado
		perfilVeterinarioRepository.save(veterinario); // guardar en la BD
	}

	@Override
	@Transactional
	public void rechazarVeterinario(Integer id) {
		PerfilVeterinario veterinario = perfilVeterinarioRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("Veterinario no encontrado"));
		veterinario.setEstado("Rechazada"); // cambiar estado
		perfilVeterinarioRepository.save(veterinario); // guardar en la BD
	}

	@Override
	public void editarVeterinario(Integer id) {
	}

}
>>>>>>> 21ec2e8465c2f8d5e6595cb364bf21309a264dc8
