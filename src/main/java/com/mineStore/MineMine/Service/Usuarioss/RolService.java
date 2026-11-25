package com.mineStore.MineMine.Service.Usuarioss;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mineStore.MineMine.DTO.Usuarioss.RolDTO;
import com.mineStore.MineMine.Model.Usuarioss.Rol;
import com.mineStore.MineMine.Model.Usuarioss.Usuario;
import com.mineStore.MineMine.Repository.Usuarioss.RolRepository;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Transactional
public class RolService {

    @Autowired
    private RolRepository rolRepository;

    private RolDTO convertirDTO(Rol rol) {
        RolDTO dto = new RolDTO();
        dto.setId_rol(rol.getId_rol());
        dto.setNombreRol(rol.getNombreRol());
        dto.setNombreUsuarios(rol.getUsuarios() != null
                ? rol.getUsuarios().stream().map(Usuario::getNombre_usuario).toList()
                : List.of());
        return dto;
    }

    public List<RolDTO> obtenerTodos() {
        log.info("Obteniendo todos los roles");
        return rolRepository.findAll().stream()
                .map(this::convertirDTO)
                .toList();
    }

    public RolDTO buscarPorId(Integer id) {
        log.info("Buscando rol con ID {}", id);
        Rol rol = rolRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("No se encontro el rol con ID {}", id);
                    return new RuntimeException("Rol no encontrado");
                });
        log.info("Rol '{}' encontrado correctamente", rol.getNombreRol());
        return convertirDTO(rol);
    }

    public RolDTO guardarRol(Rol rol) {
        log.info("Guardando rol '{}'", rol.getNombreRol());
        Rol guardado = rolRepository.save(rol);
        log.info("Rol guardado con ID {}", guardado.getId_rol());
        return convertirDTO(guardado);
    }

    public RolDTO actualizarRol(Integer id, Rol rol) {
        log.info("Actualizando rol con ID {}", id);
        Rol existente = rolRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("No se encontro el rol con ID {}", id);
                    return new RuntimeException("No existe el rol");
                });

        if (rol.getNombreRol() != null) {
            existente.setNombreRol(rol.getNombreRol());
        }

        Rol actualizado = rolRepository.save(existente);
        log.info("Rol '{}' actualizado correctamente", actualizado.getNombreRol());
        return convertirDTO(actualizado);
    }

    public String eliminarRol(Integer id) {
        log.info("Intentando eliminar rol con ID {}", id);
        try {
            Rol rol = rolRepository.findById(id)
                    .orElseThrow(() -> {
                        log.warn("No se encontro el rol con ID {}", id);
                        return new RuntimeException("No se pudo eliminar. La ID " + id + " no existe");
                    });

            if (rol.getUsuarios() != null && !rol.getUsuarios().isEmpty()) {
                log.warn("No se puede eliminar el rol '{}' porque tiene usuarios asociados", rol.getNombreRol());
                return "No se puede eliminar el rol porque tiene usuarios asociados";
            }

            rolRepository.delete(rol);
            log.info("Rol '{}' eliminado correctamente", rol.getNombreRol());
            return "El rol " + rol.getNombreRol() + " ha sido retirado exitosamente";
        } catch (RuntimeException e) {
            return e.getMessage();
        }
    }
}