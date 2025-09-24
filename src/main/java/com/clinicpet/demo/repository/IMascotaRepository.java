package com.clinicpet.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.clinicpet.demo.model.Mascota;

@Repository
public interface IMascotaRepository extends JpaRepository<Mascota, Integer> {

	List<Mascota> findByUsuario_Id(Integer id);

	List<Mascota> findByEstado(String estado);

	List<Mascota> findByEspecie(String especie);

}
