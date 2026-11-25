package com.mineStore.MineMine.Service.Ventass;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mineStore.MineMine.DTO.Ventass.MetodoPagoDTO;
import com.mineStore.MineMine.Model.Ventass.MetodoPago;
import com.mineStore.MineMine.Repository.Ventass.MetodoPagoRepository;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Transactional
public class MetodoPagoService {

    @Autowired
    private MetodoPagoRepository metodoPagoRepository;

    private MetodoPagoDTO convertirDTO(MetodoPago metodoPago) {
        MetodoPagoDTO dto = new MetodoPagoDTO();
        dto.setId_metodo_pago(metodoPago.getId_metodo_pago());
        dto.setNombreMetodoPago(metodoPago.getNombreMetodoPago());
        return dto;
    }

    public List<MetodoPagoDTO> obtenerTodos() {
        log.info("Obteniendo todos los metodos de pago");
        return metodoPagoRepository.findAll().stream()
                .map(this::convertirDTO)
                .toList();
    }

    public MetodoPagoDTO buscarPorId(Integer id) {
        log.info("Buscando metodo de pago con ID {}", id);
        MetodoPago metodoPago = metodoPagoRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("No se encontró el metodo de pago con ID {}", id);
                    return new RuntimeException("Metodo de pago no encontrado");
                });
        log.info("Metodo de pago '{}' encontrado correctamente", metodoPago.getNombreMetodoPago());
        return convertirDTO(metodoPago);
    }

    public MetodoPagoDTO guardarMetodoPago(MetodoPago metodoPago) {
        log.info("Guardando metodo de pago '{}'", metodoPago.getNombreMetodoPago());
        MetodoPago guardado = metodoPagoRepository.save(metodoPago);
        log.info("Metodo de pago guardado con ID {}", guardado.getId_metodo_pago());
        return convertirDTO(guardado);
    }

    public MetodoPagoDTO actualizarMetodoPago(Integer id, MetodoPago metodoPago) {
        log.info("Actualizando método de pago con ID {}", id);
        MetodoPago existente = metodoPagoRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("No se encontró el método de pago con ID {}", id);
                    return new RuntimeException("No existe el método de pago");
                });

        if (metodoPago.getNombreMetodoPago() != null) {
            existente.setNombreMetodoPago(metodoPago.getNombreMetodoPago());
        }

        MetodoPago actualizado = metodoPagoRepository.save(existente);
        log.info("Método de pago '{}' actualizado correctamente", actualizado.getNombreMetodoPago());
        return convertirDTO(actualizado);
    }

    public String eliminarMetodoPago(Integer id) {
        log.info("Intentando eliminar método de pago con ID {}", id);
        try {
            MetodoPago metodoPago = metodoPagoRepository.findById(id)
                    .orElseThrow(() -> {
                        log.warn("No se encontró el método de pago con ID {}", id);
                        return new RuntimeException("No se pudo eliminar. La ID " + id + " no existe");
                    });

            if (metodoPago.getVentas() != null && !metodoPago.getVentas().isEmpty()) {
                log.warn("No se puede eliminar el método de pago '{}' porque tiene ventas asociadas", metodoPago.getNombreMetodoPago());
                return "No se puede eliminar el método de pago porque tiene ventas asociadas";
            }

            metodoPagoRepository.delete(metodoPago);
            log.info("Método de pago '{}' eliminado correctamente", metodoPago.getNombreMetodoPago());
            return "El método de pago " + metodoPago.getNombreMetodoPago() + " ha sido retirado exitosamente";
        } catch (RuntimeException e) {
            return e.getMessage();
        }
    }
}