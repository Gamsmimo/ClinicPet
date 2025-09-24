package com.clinicpet.demo.service;

import com.clinicpet.demo.model.Usuario;
import com.clinicpet.demo.model.Rol;
import com.clinicpet.demo.repository.IUsuarioRepository;
import com.clinicpet.demo.repository.IRolRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UsuarioServiceImplement implements IUsuarioService {

	@Autowired
	private IUsuarioRepository usuarioRepository;

	@Autowired
	private IRolRepository rolRepository; // Asegura que esté inyectado

	@Override
	public Usuario crearUsuario(Usuario usuario) {
		// Validación básica de correo (opcional, para evitar duplicados)
		if (usuarioRepository.existsByCorreo(usuario.getCorreo())) {
			throw new RuntimeException("Correo ya existe");
		}

		// ASIGNACIÓN DE ROL POR DEFECTO: Si no tiene rol, asigna "USUARIO" (ID 1)
		if (usuario.getRol() == null) {
			Optional<Rol> rolDefaultOpt = rolRepository.findById(1); // ID 1 = USUARIO
			if (rolDefaultOpt.isPresent()) {
				usuario.setRol(rolDefaultOpt.get()); // Asigna el rol persistido
			} else {
				throw new RuntimeException("Rol por defecto no encontrado. Verifica la tabla 'rol'");
			}
		} else {
			// Si tiene rol proporcionado, verifica que exista en BD
			Integer rolId = usuario.getRol().getId();
			if (rolId == null || !rolRepository.existsById(rolId)) {
				throw new RuntimeException("Rol proporcionado no existe");
			}
			// Re-asigna el rol completo desde BD (evita transient)
			usuario.setRol(rolRepository.findById(rolId).get());
		}

		// Ahora guarda: rol es válido y no null
		return usuarioRepository.save(usuario);
	}

	// Si tienes un método save() separado, corrígelo igual
	@Override
	public void save(Usuario usuario) {
		crearUsuario(usuario); // Reutiliza la lógica de rol
	}

	@Override
	public Usuario actualizarUsuario(Integer id, Usuario usuarioActualizado) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Usuario> listarTodosUsuarios() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Optional<Usuario> buscarUsuarioPorId(Integer id) {
		// TODO Auto-generated method stub
		return Optional.empty();
	}

	@Override
	public Optional<Usuario> buscarUsuarioPorNombres(String nombres) {
		// TODO Auto-generated method stub
		return Optional.empty();
	}

	@Override
	public Optional<Usuario> buscarUsuarioPorCorreo(String correo) {
		// TODO Auto-generated method stub
		return usuarioRepository.findByCorreo(correo);
	}

	@Override
	public Optional<Usuario> buscarUsuarioPorDocumento(String numDocumento) {
		// TODO Auto-generated method stub
		return Optional.empty();
	}

	@Override
	public List<Usuario> buscarUsuariosPorRol(Integer rolId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Usuario> buscarUsuariosPorNombresOApellidos(String nombres, String apellidos) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean existeCorreo(String correo) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean existeDocumento(String numDocumento) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean existeNombres(String nombres) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Usuario activarUsuario(Integer id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Usuario desactivarUsuario(Integer id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Usuario asignarRol(Integer id, Integer rolId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void eliminarUsuario(Integer id) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean validarCredencialesPorCorreo(String correo, String password) {
		// TODO Auto-generated method stub
		return false;
	}

	// ... resto de métodos (listarTodosUsuarios, actualizarUsuario, etc.) sin
	// cambios
}
