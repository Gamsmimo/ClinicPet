package com.clinicpet.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.clinicpet.demo.model.PerfilVeterinario;

@Repository
public interface IPerfilVeterinarioRepository extends JpaRepository<PerfilVeterinario, Long> {
}
