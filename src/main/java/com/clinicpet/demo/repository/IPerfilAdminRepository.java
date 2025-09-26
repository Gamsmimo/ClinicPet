package com.clinicpet.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.clinicpet.demo.model.PerfilAdmin;

@Repository
public interface IPerfilAdminRepository extends JpaRepository<PerfilAdmin, Integer> {

	// si se necesita llamar al unico admin ejejej
	PerfilAdmin findByCorreo(String correo);

}
