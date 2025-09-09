package com.clinicpet.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.clinicpet.demo.model.Producto;

@Repository
public interface IProductoRepository extends JpaRepository<Producto, Long> {
}
