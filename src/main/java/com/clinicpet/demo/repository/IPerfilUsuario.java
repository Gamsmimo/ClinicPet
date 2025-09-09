package com.clinicpet.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.clinicpet.demo.model.PerfilUsuario;

@Repository
public interface IPerfilUsuario extends JpaRepository<PerfilUsuario, Long> {
}
