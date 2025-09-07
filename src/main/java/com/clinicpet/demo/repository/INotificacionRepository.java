package com.clinicpet.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.clinicpet.demo.model.Notificacion;

@Repository
public interface INotificacionRepository extends JpaRepository<Notificacion, Integer> {

	// Busca todas las notificaciones de un usuario
	List<Notificacion> findByUsuario_UsuarioId(Integer usuarioId);

	// Busca notificaciones por estado
	List<Notificacion> findByUsuario_UsuarioIdAndEstado(Integer usuarioId, String estado);

	// Busca notificaciones por tipo
	List<Notificacion> findByUsuario_UsuarioIdAndTipo(Integer usuarioId, String tipo);

	// Busca notificaciones recientes de un usuario ordenadas por fecha desc
	List<Notificacion> findByUsuario_UsuarioIdOrderByFechaDesc(Integer idUsuario);

	// Busca todas las notificaciones con un estado especifivo ordenadas de la mas
	// reciente a la mas antigua.
	List<Notificacion> findByUsuario_UsuarioIdAndEstadoOrderByFechaDesc(Integer idUsuario, String estado);

}
