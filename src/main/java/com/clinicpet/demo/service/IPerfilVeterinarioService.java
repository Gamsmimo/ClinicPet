package com.clinicpet.demo.service;

import com.clinicpet.demo.model.PerfilVeterinario;

import java.util.List;
import java.util.Optional;

public interface IPerfilVeterinarioService {
	List<PerfilVeterinario> ListarPorEstado(String estado);

	PerfilVeterinario crearPerfilVeterinario(PerfilVeterinario perfilVeterinario);

	PerfilVeterinario findById(Integer id);

	List<PerfilVeterinario> obtenerTodosLosPerfilesVeterinario();

	PerfilVeterinario obtenerPerfilVeterinarioPorUsuarioId(Integer usuarioId);

	PerfilVeterinario obtenerPerfilVeterinarioPorUsername(String username);

	List<PerfilVeterinario> obtenerTodosLosPerfilesVeterinarios();

	List<PerfilVeterinario> obtenerVeterinariosPorEspecialidad(String especialidad);

	PerfilVeterinario obtenerVeterinarioPorTarjetaProfesional(String tarjetaProfesional);

	List<PerfilVeterinario> obtenerVeterinariosPorTelefono(String telefono);

	List<PerfilVeterinario> obtenerVeterinariosDisponibles();

	
	PerfilVeterinario actualizarPerfilVeterinario(Integer id, PerfilVeterinario perfilVeterinario);

	Optional<PerfilVeterinario> obtenerPerfilVeterinarioPorTargetaProfesional(String tarjetaProfesional);

	void aprobarVeterinario(Integer id);

	PerfilVeterinario desactivarVeterinario(Integer id);

	PerfilVeterinario activarVeterinario(Integer id);

	void rechazarVeterinario(Integer id);

	void editarVeterinario(Integer id);

}