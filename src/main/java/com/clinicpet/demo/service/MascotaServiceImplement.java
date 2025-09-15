package com.clinicpet.demo.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.clinicpet.demo.model.Mascota;
import com.clinicpet.demo.repository.IMascotaRepository;

@Service
public class MascotaServiceImplement implements IMascotaService {
	@Autowired
	private IMascotaRepository mascotaRepository;

	@Override
	public Mascota guardarMascota(Mascota mascota) {
		return mascotaRepository.save(mascota);
	}

	@Override
	public List<Mascota> listarMascotas() {
		return mascotaRepository.findAll();
	}

	@Override
	public Optional<Mascota> buscarMascotaPorId(Integer id) {
		return mascotaRepository.findById(id);
	}

	@Override
	public void eliminarMascota(Integer id) {
		mascotaRepository.deleteById(id);
	}

	@Override
	public List<Mascota> buscarPorUsuario(Integer usuarioId) {
		return mascotaRepository.findByPerfilusuarioId(usuarioId);
	}

	@Override
	public List<Mascota> buscarPorEstado(String estado) {
		return mascotaRepository.findByEstado(estado);
	}

	@Override
	public List<Mascota> buscarPorEspecie(String especie) {
		return mascotaRepository.findByEspecie(especie);
	}

}
