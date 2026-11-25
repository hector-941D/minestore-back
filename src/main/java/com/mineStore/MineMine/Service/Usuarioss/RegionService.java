package com.mineStore.MineMine.Service.Usuarioss;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mineStore.MineMine.DTO.Usuarioss.RegionDTO;
import com.mineStore.MineMine.Model.Usuarioss.Comuna;
import com.mineStore.MineMine.Model.Usuarioss.Region;
import com.mineStore.MineMine.Repository.Usuarioss.RegionRepository;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Transactional
public class RegionService {

    @Autowired
    private RegionRepository regionRepository;

    private RegionDTO convertirDTO(Region region) {
        RegionDTO dto = new RegionDTO();
        dto.setId_region(region.getId_region());
        dto.setNombreRegion(region.getNombreRegion());
        dto.setNombreComunas(region.getComunas() != null
                ? region.getComunas().stream().map(Comuna::getNombreComuna).toList()
                : List.of());
        return dto;
    }

    public List<RegionDTO> obtenerTodos() {
        log.info("Obteniendo todas las regiones");
        return regionRepository.findAll().stream()
                .map(this::convertirDTO)
                .toList();
    }

    public RegionDTO buscarPorId(Integer id) {
        log.info("Buscando región con ID {}", id);
        Region region = regionRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("No se encontró la región con ID {}", id);
                    return new RuntimeException("Región no encontrada");
                });
        log.info("Región '{}' encontrada correctamente", region.getNombreRegion());
        return convertirDTO(region);
    }

    public RegionDTO guardarRegion(Region region) {
        log.info("Guardando región '{}'", region.getNombreRegion());
        Region guardada = regionRepository.save(region);
        log.info("Región guardada con ID {}", guardada.getId_region());
        return convertirDTO(guardada);
    }

    public RegionDTO actualizarRegion(Integer id, Region region) {
        log.info("Actualizando región con ID {}", id);
        Region existente = regionRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("No se encontró la región con ID {}", id);
                    return new RuntimeException("No existe la región");
                });

        if (region.getNombreRegion() != null) {
            existente.setNombreRegion(region.getNombreRegion());
        }

        Region actualizada = regionRepository.save(existente);
        log.info("Región '{}' actualizada correctamente", actualizada.getNombreRegion());
        return convertirDTO(actualizada);
    }

    public String eliminarRegion(Integer id) {
        log.info("Intentando eliminar región con ID {}", id);
        try {
            Region region = regionRepository.findById(id)
                    .orElseThrow(() -> {
                        log.warn("No se encontró la región con ID {}", id);
                        return new RuntimeException("No se pudo eliminar. La ID " + id + " no existe");
                    });

            if (region.getComunas() != null && !region.getComunas().isEmpty()) {
                log.warn("No se puede eliminar la región '{}' porque tiene comunas asociadas", region.getNombreRegion());
                return "No se puede eliminar la región porque tiene comunas asociadas";
            }

            regionRepository.delete(region);
            log.info("Región '{}' eliminada correctamente", region.getNombreRegion());
            return "La región " + region.getNombreRegion() + " ha sido retirada exitosamente";
        } catch (RuntimeException e) {
            return e.getMessage();
        }
    }
}