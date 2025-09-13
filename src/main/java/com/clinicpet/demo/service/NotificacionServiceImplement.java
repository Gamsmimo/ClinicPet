package com.clinicpet.demo.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.clinicpet.demo.model.Notificacion;
import com.clinicpet.demo.model.Usuario;
import com.clinicpet.demo.repository.INotificacionRepository;
import com.clinicpet.demo.repository.IUsuarioRepository;

@Service
public class NotificacionServiceImplement implements INotificacionService {

	@Autowired
	private INotificacionRepository notificacionRepository;

	@Autowired
	private IUsuarioRepository usuarioRepository;

	@Override
	public Notificacion guardarNotificacion(Notificacion notificacion) {
		// TODO Auto-generated method stub
		validarNotificacion(notificacion);

		// si no tiene fecha estabelcer la fecha actual
		if (notificacion.getFecha() == null) {
			notificacion.setFecha(LocalDateTime.now());
		}
		// si no tiene estado establecer como no leido.
		if (notificacion.getEstado() == null || notificacion.getEstado().trim().isEmpty()) {
			notificacion.setEstado("No leída.");
		}
		return notificacionRepository.save(notificacion);
	}

	@Override
	public List<Notificacion> obtenerTodasLasNotificaciones() {
		// TODO Auto-generated method stub
		return notificacionRepository.findAll();
	}

	@Override
	public Optional<Notificacion> obtenerNotificacionPorId(Integer id) {
		// TODO Auto-generated method stub
		return notificacionRepository.findById(id);
	}

	@Override
	public void eliminarNotificacion(Integer id) {
		// TODO Auto-generated method stub
		if (!notificacionRepository.existsById(id)) {
			throw new IllegalArgumentException("No existe una notificación con el ID: " + id);
		}
		notificacionRepository.deleteById(id);
	}

	@Override
	public List<Notificacion> obtenerNotificacionesPorUsuario(Integer usuarioId) {
		// TODO Auto-generated method stub
		return notificacionRepository.findByUsuario_Id(usuarioId);
	}

	@Override
	public List<Notificacion> obtenerNotificacionesPorUsuarioYEstado(Integer usuarioId, String estado) {
		// TODO Auto-generated method stub
		return notificacionRepository.findByUsuario_IdAndEstado(usuarioId, estado);
	}

	@Override
	public List<Notificacion> obtenerNotificacionesPorUsuarioYTipo(Integer usuarioId, String tipo) {
		// TODO Auto-generated method stub
		return notificacionRepository.findByUsuario_IdAndTipo(usuarioId, tipo);
	}

	@Override
	public List<Notificacion> obtenerNotificacionesRecientesPorUsuario(Integer usuarioId) {
		// TODO Auto-generated method stub
		return notificacionRepository.findByUsuario_IdOrderByFechaDesc(usuarioId);
	}

	@Override
	public List<Notificacion> obtenerNotificacionesPorUsuarioYEstadoOrdenadas(Integer usuarioId, String estado) {
		// TODO Auto-generated method stub
		return notificacionRepository.findByUsuario_IdAndEstadoOrderByFechaDesc(usuarioId, estado);
	}

	@Override
	public Notificacion marcarComoLeida(Integer notificacionId) {
		// TODO Auto-generated method stub
		Notificacion notificacion = notificacionRepository.findById(notificacionId)
				.orElseThrow(() -> new IllegalArgumentException("No existe la notificación con ID: " + notificacionId));

		notificacion.setEstado("Leída");
		return notificacionRepository.save(notificacion);
	}

	@Override
	public Notificacion marcarComoNoLeida(Integer notificacionId) {
		// TODO Auto-generated method stub
		Notificacion notificacion = notificacionRepository.findById(notificacionId)
				.orElseThrow(() -> new IllegalArgumentException("No existe la notificación con ID: " + notificacionId));

		notificacion.setEstado("No leída");
		return notificacionRepository.save(notificacion);
	}

	@Override
	public void marcarTodasComoLeidas(Integer usuarioId) {
		// TODO Auto-generated method stub
		List<Notificacion> notificacionesNoLeidas = obtenerNotificacionesPorUsuarioYEstado(usuarioId, "No leída");

		for (Notificacion notificacion : notificacionesNoLeidas) {
			notificacion.setEstado("Leída");
			notificacionRepository.save(notificacion);
		}

	}

	@Override
	public int contarNotificacionesNoLeidas(Integer usuarioId) {
		// TODO Auto-generated method stub
		List<Notificacion> notificacionesNoLeidas = obtenerNotificacionesPorUsuarioYEstado(usuarioId, "No leída");
		return notificacionesNoLeidas.size();
	}

	@Override
	public Notificacion crearNotificacion(Integer usuarioId, String mensaje, String tipo) {
		// TODO Auto-generated method stub
		Usuario usuario = usuarioRepository.findById(usuarioId) 
	            .orElseThrow(() -> new IllegalArgumentException("No existe el usuario con ID: " + usuarioId));

		Notificacion notificacion = new Notificacion();
		notificacion.setMensaje(mensaje);
		notificacion.setTipo(tipo);
		notificacion.setEstado("no leída");
		notificacion.setFecha(LocalDateTime.now());
		notificacion.setUsuario(usuario);

		return guardarNotificacion(notificacion);
	}

	// Método para validaciones
	private void validarNotificacion(Notificacion notificacion) {
		if (notificacion.getMensaje() == null || notificacion.getMensaje().trim().isEmpty()) {
			throw new IllegalArgumentException("El mensaje de la notificación es obligatorio");
		}

		if (notificacion.getTipo() == null || notificacion.getTipo().trim().isEmpty()) {
			throw new IllegalArgumentException("El tipo de notificación es obligatorio");
		}

		if (notificacion.getUsuario() == null) {
			throw new IllegalArgumentException("El usuario es obligatorio");
		}

		// Validar tipos de notificación permitidos.
		List<String> tiposPermitidos = List.of("sistema", "recordatorio", "alerta", "promocion", "general");
		if (!tiposPermitidos.contains(notificacion.getTipo().toLowerCase())) {
			throw new IllegalArgumentException("Tipo de notificación no válido: " + notificacion.getTipo());
		}

		// Validar estados permitidos
		List<String> estadosPermitidos = List.of("Leída", "No leída", "Archivada");
		if (notificacion.getEstado() != null && !estadosPermitidos.contains(notificacion.getEstado().toLowerCase())) {
			throw new IllegalArgumentException("Estado de notificación no válido: " + notificacion.getEstado());
		}
	}

	//Notificaciones pendientes por leer de un usuario (mostrar contador de notificaciones no leidas)
	public List<Notificacion> obtenerNotificacionesNoLeidasPorUsuario(Integer usuarioId) {
		return obtenerNotificacionesPorUsuarioYEstado(usuarioId, "no leída");
	}

	//obtiene las "n" notificaciones mas recientes de un usuario evitando la sobrecarga al usuario con demasiadas notificaciones viejas.
	public List<Notificacion> obtenerUltimasNotificaciones(Integer usuarioId, int limite) {
		List<Notificacion> todas = obtenerNotificacionesRecientesPorUsuario(usuarioId);
		return todas.stream().limit(limite).toList();
	}

}
