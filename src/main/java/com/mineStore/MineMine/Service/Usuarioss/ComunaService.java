package com.mineStore.MineMine.Service.Usuarioss;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mineStore.MineMine.DTO.Usuarioss.ComunaDTO;
import com.mineStore.MineMine.Model.Usuarioss.Comuna;
import com.mineStore.MineMine.Model.Usuarioss.Usuario;
import com.mineStore.MineMine.Repository.Usuarioss.ComunaRepository;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Transactional
public class ComunaService {

    @Autowired
    private ComunaRepository comunaRepository;

    private ComunaDTO convertirDTO(Comuna comuna) {
        ComunaDTO dto = new ComunaDTO();
        dto.setId_comuna(comuna.getId_comuna());
        dto.setNombreComuna(comuna.getNombreComuna());
        if (comuna.getRegion() != null) {
            dto.setId_region(comuna.getRegion().getId_region());
            dto.setNombreRegion(comuna.getRegion().getNombreRegion());
        }
        dto.setNombreUsuarios(comuna.getUsuarios() != null
                ? comuna.getUsuarios().stream().map(Usuario::getNombre_usuario).toList()
                : List.of());
        return dto;
    }

    public List<ComunaDTO> obtenerTodos() {
        log.info("Obteniendo todas las comunas");
        return comunaRepository.findAll().stream()
                .map(this::convertirDTO)
                .toList();
    }

    public ComunaDTO buscarPorId(Integer id) {
        log.info("Buscando comuna con ID {}", id);
        Comuna comuna = comunaRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("No se encontro la comuna con ID {}", id);
                    return new RuntimeException("Comuna no encontrada");
                });
        log.info("Comuna '{}' encontrada correctamente", comuna.getNombreComuna());
        return convertirDTO(comuna);
    }

    public ComunaDTO guardarComuna(Comuna comuna) {
        log.info("Guardando comuna '{}'", comuna.getNombreComuna());
        Comuna guardada = comunaRepository.save(comuna);
        log.info("Comuna guardada con ID {}", guardada.getId_comuna());
        return convertirDTO(guardada);
    }

    public ComunaDTO actualizarComuna(Integer id, Comuna comuna) {
        log.info("Actualizando comuna con ID {}", id);
        Comuna existente = comunaRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("No se encontro la comuna con ID {}", id);
                    return new RuntimeException("No existe la comuna");
                });

        if (comuna.getNombreComuna() != null) {
            existente.setNombreComuna(comuna.getNombreComuna());
        }
        if (comuna.getRegion() != null) {
            existente.setRegion(comuna.getRegion());
        }

        Comuna actualizada = comunaRepository.save(existente);
        log.info("Comuna '{}' actualizada correctamente", actualizada.getNombreComuna());
        return convertirDTO(actualizada);
    }

    public String eliminarComuna(Integer id) {
        log.info("Intentando eliminar comuna con ID {}", id);
        try {
            Comuna comuna = comunaRepository.findById(id)
                    .orElseThrow(() -> {
                        log.warn("No se encontro la comuna con ID {}", id);
                        return new RuntimeException("No se pudo eliminar. La ID " + id + " no existe");
                    });

            if (comuna.getUsuarios() != null && !comuna.getUsuarios().isEmpty()) {
                log.warn("No se puede eliminar la comuna '{}' porque tiene usuarios asociados", comuna.getNombreComuna());
                return "No se puede eliminar la comuna porque tiene usuarios asociados";
            }

            comunaRepository.delete(comuna);
            log.info("Comuna '{}' eliminada correctamente", comuna.getNombreComuna());
            return "La comuna " + comuna.getNombreComuna() + " ha sido retirada exitosamente";
        } catch (RuntimeException e) {
            return e.getMessage();
        }
    }
}