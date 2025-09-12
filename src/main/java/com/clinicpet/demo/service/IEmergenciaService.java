package com.clinicpet.demo.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import com.clinicpet.demo.model.Emergencia;

public interface IEmergenciaService {

	public List<Emergencia> findAll();

	public Optional<Emergencia> findById(Integer id);

	public Emergencia save(Emergencia emergencia);

	public void deleteById(Integer id);

	public List<Emergencia> findByTipo(String tipo);

	public List<Emergencia> findByMascotaId(Integer mascotaId);

	public List<Emergencia> findByVeterinarioId(Integer veterinarioId);

	public List<Emergencia> findByMascotaIdAndFechaHoraBetween(Integer mascotaId, LocalDateTime inicio,
			LocalDateTime fin);

}
