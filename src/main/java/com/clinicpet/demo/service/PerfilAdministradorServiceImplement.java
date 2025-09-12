package com.clinicpet.demo.service;

import com.clinicpet.demo.model.PerfilAdministrador;
import com.clinicpet.demo.repository.IPerfilAdministradorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class PerfilAdministradorServiceImplement implements IPerfilAdministradorService {

    @Autowired
    private IPerfilAdministradorRepository perfilAdminRepository;

    @Override
    @Transactional
    public PerfilAdministrador crearPerfilAdministrador(PerfilAdministrador perfilAdmin) {
        return perfilAdminRepository.save(perfilAdmin);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<PerfilAdministrador> obtenerPerfilAdministradorPorId(Integer id) {
        return perfilAdminRepository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public PerfilAdministrador obtenerPerfilAdministradorPorUsuarioId(Integer usuarioId) {
        return perfilAdminRepository.findByUsuarioId(usuarioId);
    }

    @Override
    @Transactional(readOnly = true)
    public PerfilAdministrador obtenerPerfilAdministradorPorUsername(String username) {
        return perfilAdminRepository.findByUsuarioUsername(username);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PerfilAdministrador> obtenerTodosLosPerfilesAdministradores() {
        return perfilAdminRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public List<PerfilAdministrador> obtenerPerfilesPorPermisos(String permiso) {
        return perfilAdminRepository.findByPermisosContaining(permiso);
    }

    @Override
    @Transactional
    public PerfilAdministrador actualizarPerfilAdministrador(Integer id, PerfilAdministrador perfilAdmin) {
        if (perfilAdminRepository.existsById(id)) {
            perfilAdmin.setId(id);
            return perfilAdminRepository.save(perfilAdmin);
        }
        throw new RuntimeException("Perfil Administrador no encontrado con ID: " + id);
    }

    @Override
    @Transactional
    public void eliminarPerfilAdministrador(Integer id) {
        if (perfilAdminRepository.existsById(id)) {
            perfilAdminRepository.deleteById(id);
        } else {
            throw new RuntimeException("Perfil Administrador no encontrado con ID: " + id);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existePerfilAdministradorPorUsuarioId(Integer usuarioId) {
        return perfilAdminRepository.existsByUsuarioId(usuarioId);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existePerfilAdministradorPorId(Integer id) {
        return perfilAdminRepository.existsById(id);
    }
}