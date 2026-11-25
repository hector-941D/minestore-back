package com.mineStore.MineMine.Service.Ventass;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mineStore.MineMine.DTO.Ventass.MetodoEnvioDTO;
import com.mineStore.MineMine.Model.Ventass.MetodoEnvio;
import com.mineStore.MineMine.Repository.Ventass.MetodoEnvioRepository;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Transactional
public class MetodoEnvioService {

    @Autowired
    private MetodoEnvioRepository metodoEnvioRepository;

    private MetodoEnvioDTO convertirDTO(MetodoEnvio metodoEnvio) {
        MetodoEnvioDTO dto = new MetodoEnvioDTO();
        dto.setId_metodo_envio(metodoEnvio.getId_metodo_envio());
        dto.setNombreMetodoEnvio(metodoEnvio.getNombreMetodoEnvio());
        return dto;
    }

    public List<MetodoEnvioDTO> obtenerTodos() {
        log.info("Obteniendo todos los metodos de envio");
        return metodoEnvioRepository.findAll().stream()
                .map(this::convertirDTO)
                .toList();
    }

    public MetodoEnvioDTO buscarPorId(Integer id) {
        log.info("Buscando metodo de envio con ID {}", id);
        MetodoEnvio metodoEnvio = metodoEnvioRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("No se encontro el metodo de envío con ID {}", id);
                    return new RuntimeException("Metodo de envío no encontrado");
                });
        log.info("Metodo de envio '{}' encontrado correctamente", metodoEnvio.getNombreMetodoEnvio());
        return convertirDTO(metodoEnvio);
    }

    public MetodoEnvioDTO guardarMetodoEnvio(MetodoEnvio metodoEnvio) {
        log.info("Guardando metodo de envío '{}'", metodoEnvio.getNombreMetodoEnvio());
        MetodoEnvio guardado = metodoEnvioRepository.save(metodoEnvio);
        log.info("Metodo de envio guardado con ID {}", guardado.getId_metodo_envio());
        return convertirDTO(guardado);
    }

    public MetodoEnvioDTO actualizarMetodoEnvio(Integer id, MetodoEnvio metodoEnvio) {
        log.info("Actualizando metodo de envío con ID {}", id);
        MetodoEnvio existente = metodoEnvioRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("No se encontro el metodo de envío con ID {}", id);
                    return new RuntimeException("No existe el metodo de envio");
                });

        if (metodoEnvio.getNombreMetodoEnvio() != null) {
            existente.setNombreMetodoEnvio(metodoEnvio.getNombreMetodoEnvio());
        }

        MetodoEnvio actualizado = metodoEnvioRepository.save(existente);
        log.info("Metodo de envio '{}' actualizado correctamente", actualizado.getNombreMetodoEnvio());
        return convertirDTO(actualizado);
    }

    public String eliminarMetodoEnvio(Integer id) {
        log.info("Intentando eliminar metodo de envio con ID {}", id);
        try {
            MetodoEnvio metodoEnvio = metodoEnvioRepository.findById(id)
                    .orElseThrow(() -> {
                        log.warn("No se encontro el metodo de envio con ID {}", id);
                        return new RuntimeException("No se pudo eliminar. La ID " + id + " no existe");
                    });

            if (metodoEnvio.getVentas() != null && !metodoEnvio.getVentas().isEmpty()) {
                log.warn("No se puede eliminar el metodo de envio '{}' porque tiene ventas asociadas", metodoEnvio.getNombreMetodoEnvio());
                return "No se puede eliminar el metodo de envio porque tiene ventas asociadas";
            }

            metodoEnvioRepository.delete(metodoEnvio);
            log.info("Metodo de envio '{}' eliminado correctamente", metodoEnvio.getNombreMetodoEnvio());
            return "El metodo de envio " + metodoEnvio.getNombreMetodoEnvio() + " ha sido retirado exitosamente";
        } catch (RuntimeException e) {
            return e.getMessage();
        }
    }
}