package com.clinicpet.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
<<<<<<< HEAD
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
=======
>>>>>>> bcd859d018ce2d6ff7cc81e0398cf721f3ed8b6f
import org.springframework.stereotype.Repository;

import com.clinicpet.demo.model.Mascota;

<<<<<<< HEAD
import jakarta.transaction.Transactional;

=======
>>>>>>> bcd859d018ce2d6ff7cc81e0398cf721f3ed8b6f
@Repository
public interface IMascotaRepository extends JpaRepository<Mascota, Integer> {

	// Sin cambios: Método derivado (usa si el mapeo JPA es correcto)
	List<Mascota> findByUsuario_Id(Integer id);

	// Sin cambios: Método derivado
	List<Mascota> findByEspecie(String especie);

<<<<<<< HEAD
	@Modifying
	@Transactional
	@Query("DELETE FROM Mascota m WHERE m.id = :id")
	void eliminarMascotaPorId(@Param("id") Integer id);

=======
>>>>>>> bcd859d018ce2d6ff7cc81e0398cf721f3ed8b6f
}
