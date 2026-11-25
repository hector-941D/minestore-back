package com.mineStore.MineMine.Service.Ventass;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mineStore.MineMine.DTO.Ventass.TipoDTO;
import com.mineStore.MineMine.Model.Ventass.Tipo;
import com.mineStore.MineMine.Repository.Ventass.TipoRepository;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Transactional
public class TipoService {

    @Autowired
    private TipoRepository tipoRepository;

    private TipoDTO convertirDTO(Tipo tipo) {
        TipoDTO dto = new TipoDTO();
        dto.setId_tipo(tipo.getId_tipo());
        dto.setNombreTipo(tipo.getNombreTipo());
        return dto;
    }

    public List<TipoDTO> obtenerTodos() {
        log.info("Obteniendo todos los tipos");
        return tipoRepository.findAll().stream()
                .map(this::convertirDTO)
                .toList();
    }

    public TipoDTO buscarPorId(Integer id) {
        log.info("Buscando tipo con ID {}", id);
        Tipo tipo = tipoRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("No se encontro el tipo con ID {}", id);
                    return new RuntimeException("Tipo no encontrado");
                });
        log.info("Tipo '{}' encontrado correctamente", tipo.getNombreTipo());
        return convertirDTO(tipo);
    }

    public TipoDTO guardarTipo(Tipo tipo) {
        log.info("Guardando tipo '{}'", tipo.getNombreTipo());
        Tipo guardado = tipoRepository.save(tipo);
        log.info("Tipo guardado con ID {}", guardado.getId_tipo());
        return convertirDTO(guardado);
    }

    public TipoDTO actualizarTipo(Integer id, Tipo tipo) {
        log.info("Actualizando tipo con ID {}", id);
        Tipo existente = tipoRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("No se encontro el tipo con ID {}", id);
                    return new RuntimeException("No existe el tipo");
                });

        if (tipo.getNombreTipo() != null) {
            existente.setNombreTipo(tipo.getNombreTipo());
        }

        Tipo actualizado = tipoRepository.save(existente);
        log.info("Tipo '{}' actualizado correctamente", actualizado.getNombreTipo());
        return convertirDTO(actualizado);
    }

    public String eliminarTipo(Integer id) {
        log.info("Intentando eliminar tipo con ID {}", id);
        try {
            Tipo tipo = tipoRepository.findById(id)
                    .orElseThrow(() -> {
                        log.warn("No se encontro el tipo con ID {}", id);
                        return new RuntimeException("No se pudo eliminar. La ID " + id + " no existe");
                    });

            if (tipo.getVentas() != null && !tipo.getVentas().isEmpty()) {
                log.warn("No se puede eliminar el tipo '{}' porque tiene ventas asociadas", tipo.getNombreTipo());
                return "No se puede eliminar el tipo porque tiene ventas asociadas";
            }

            tipoRepository.delete(tipo);
            log.info("Tipo '{}' eliminado correctamente", tipo.getNombreTipo());
            return "El tipo " + tipo.getNombreTipo() + " ha sido retirado exitosamente";
        } catch (RuntimeException e) {
            return e.getMessage();
        }
    }
}