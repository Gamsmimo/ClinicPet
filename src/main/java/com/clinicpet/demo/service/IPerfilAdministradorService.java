package com.clinicpet.demo.service;

import com.clinicpet.demo.model.PerfilAdministrador;
import java.util.List;
import java.util.Optional;

public interface IPerfilAdministradorService {
    PerfilAdministrador crearPerfilAdministrador(PerfilAdministrador perfilAdministrador);
    Optional<PerfilAdministrador> obtenerPerfilAdministradorPorId(Integer id);
    List<PerfilAdministrador> obtenerTodosLosPerfilesAdministrador();
    PerfilAdministrador actualizarPerfilAdministrador(Integer id, PerfilAdministrador perfilAdministrador);
    void eliminarPerfilAdministrador(Integer id);
    Optional<PerfilAdministrador> obtenerPerfilAdministradorPorUsuarioId(Integer usuarioId);
    Optional<PerfilAdministrador> obtenerPerfilAdministradorPorUsername(String username);
    boolean existePerfilAdministradorPorUsuarioId(Integer usuarioId);
    List<PerfilAdministrador> obtenerPerfilesAdministradorPorPermisos(String permiso);
    }