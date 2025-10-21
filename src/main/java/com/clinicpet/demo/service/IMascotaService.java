package com.clinicpet.demo.service;

import java.util.List;
import java.util.Optional;

import com.clinicpet.demo.model.Mascota;

public interface IMascotaService {

	Mascota guardarMascota(Mascota mascota);

	List<Mascota> listarMascotas();

	Optional<Mascota> buscarMascotaPorId(Integer id);

<<<<<<< HEAD
=======
	void eliminarMascota(Integer id);

>>>>>>> bcd859d018ce2d6ff7cc81e0398cf721f3ed8b6f
	Mascota actualizarMascota(Mascota mascota); // Este se implementa ahora

	List<Mascota> buscarPorUsuario(Integer usuarioId);

	List<Mascota> buscarPorEspecie(String especie);
<<<<<<< HEAD

	void eliminarMascota(Integer id);

=======
>>>>>>> bcd859d018ce2d6ff7cc81e0398cf721f3ed8b6f
}
