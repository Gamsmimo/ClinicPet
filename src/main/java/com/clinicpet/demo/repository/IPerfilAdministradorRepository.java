package com.clinicpet.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.clinicpet.demo.model.PerfilAdministrador;

@Repository
public interface IPerfilAdministradorRepository extends JpaRepository<PerfilAdministrador, Integer> {

}
