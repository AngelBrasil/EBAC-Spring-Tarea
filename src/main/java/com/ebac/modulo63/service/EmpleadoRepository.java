package com.ebac.modulo63.service;

import com.ebac.modulo63.dto.Empleado;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmpleadoRepository extends JpaRepository<Empleado , Long> {
}
