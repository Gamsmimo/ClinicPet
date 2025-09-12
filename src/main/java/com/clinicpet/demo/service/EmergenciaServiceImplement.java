package com.clinicpet.demo.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.clinicpet.demo.model.Emergencia;
import com.clinicpet.demo.repository.IEmergenciaRepository;

@Service
public class EmergenciaServiceImplement implements IEmergenciaService {

	@Autowired
	private IEmergenciaRepository emergenciaRepository;

	@Override
	public List<Emergencia> findAll() {
		// TODO Auto-generated method stub
		return emergenciaRepository.findAll();
	}

	@Override
	public Optional<Emergencia> findById(Integer id) {
		// TODO Auto-generated method stub
		return emergenciaRepository.findById(id);
	}

	@Override
	public Emergencia save(Emergencia emergencia) {
		// TODO Auto-generated method stub
		return emergenciaRepository.save(emergencia);
	}

	@Override
	public void deleteById(Integer id) {
		// TODO Auto-generated method stub
		emergenciaRepository.deleteById(id);

	}

	@Override
	public List<Emergencia> findByTipo(String tipo) {
		// TODO Auto-generated method stub
		return emergenciaRepository.findByTipo(tipo);
	}

	@Override
	public List<Emergencia> findByMascotaId(Integer mascotaId) {
		// TODO Auto-generated method stub
		return emergenciaRepository.findByMascota_Id(mascotaId);
	}

	@Override
	public List<Emergencia> findByVeterinarioId(Integer veterinarioId) {
		// TODO Auto-generated method stub
		return emergenciaRepository.findByVeterinario_Id(veterinarioId);
	}

	@Override
	public List<Emergencia> findByMascotaIdAndFechaHoraBetween(Integer mascotaId, LocalDateTime inicio,
			LocalDateTime fin) {
		// TODO Auto-generated method stub
		return emergenciaRepository.findByMascota_IdAndFechayhoraBetween(mascotaId, inicio, fin);
	}

}
