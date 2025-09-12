package com.clinicpet.demo.service;

import com.clinicpet.demo.model.PerfilVeterinario;
import java.util.List;
import java.util.Optional;

public interface IPerfilVeterinarioService {
	PerfilVeterinario crearPerfilVeterinario(PerfilVeterinario perfilVeterinario);

	Optional<PerfilVeterinario> obtenerPerfilVeterinarioPorId(Integer id);

	List<PerfilVeterinario> obtenerTodosLosPerfilesVeterinario();

	PerfilVeterinario actualizarPerfilVeterinario(Integer id, PerfilVeterinario perfilVeterinario);

	void eliminarPerfilVeterinario(Integer id);

	Optional<PerfilVeterinario> obtenerPerfilVeterinarioPorUsuarioId(Integer usuarioId);

	Optional<PerfilVeterinario> obtenerPerfilVeterinarioPorUsername(String username);

	List<PerfilVeterinario> obtenerPerfilesVeterinarioPorEspecialidad(String especialidad);

	Optional<PerfilVeterinario> obtenerPerfilVeterinarioPorRut(String rut);

	List<PerfilVeterinario> obtenerPerfilesVeterinarioPorTelefono(String telefono);

	List<PerfilVeterinario> obtenerVeterinariosDisponibles();

	boolean existePerfilVeterinarioPorUsuarioId(Integer usuarioId);

	boolean existePerfilVeterinarioPorRut(String rut);
}