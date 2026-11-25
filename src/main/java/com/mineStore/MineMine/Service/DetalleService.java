package com.mineStore.MineMine.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mineStore.MineMine.DTO.DetalleDTO;
import com.mineStore.MineMine.Model.Detalle;
import com.mineStore.MineMine.Repository.DetalleRepository;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Transactional
public class DetalleService {

    @Autowired
    private DetalleRepository detalleRepository;

    public DetalleDTO convertirDTO(Detalle detalle) {
        DetalleDTO dto = new DetalleDTO();
        dto.setId_detalle(detalle.getId_detalle());
        dto.setCantidad(detalle.getCantidad());
        dto.setPrecioUnitario(detalle.getPrecioUnitario());
        dto.setSubtotal(detalle.getSubtotal());
        if (detalle.getVenta() != null) {
            dto.setId_venta(detalle.getVenta().getId_venta());
        }
        if (detalle.getProducto() != null) {
            dto.setId_producto(detalle.getProducto().getId_producto());
            dto.setNombreProducto(detalle.getProducto().getNombreProducto());
        }
        return dto;
    }

    public List<DetalleDTO> obtenerTodos() {
        log.info("Obteniendo todos los detalles");
        return detalleRepository.findAll().stream()
                .map(this::convertirDTO)
                .toList();
    }

    public DetalleDTO buscarPorId(Integer id) {
        log.info("Buscando detalle con ID {}", id);
        Detalle detalle = detalleRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("No se encontro el detalle con ID {}", id);
                    return new RuntimeException("Detalle no encontrado");
                });
        log.info("Detalle con ID {} encontrado correctamente", id);
        return convertirDTO(detalle);
    }

    public DetalleDTO guardarDetalle(Detalle detalle) {
        log.info("Guardando detalle");
        if (detalle.getSubtotal() == null && detalle.getCantidad() != null && detalle.getPrecioUnitario() != null) {
            detalle.setSubtotal(detalle.getCantidad() * detalle.getPrecioUnitario());
        }
        Detalle guardado = detalleRepository.save(detalle);
        log.info("Detalle guardado con ID {}", guardado.getId_detalle());
        return convertirDTO(guardado);
    }

    public DetalleDTO actualizarDetalle(Integer id, Detalle detalle) {
        log.info("Actualizando detalle con ID {}", id);
        Detalle existente = detalleRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("No se encontró el detalle con ID {}", id);
                    return new RuntimeException("No existe el detalle");
                });

        if (detalle.getCantidad() != null) {
            existente.setCantidad(detalle.getCantidad());
        }
        if (detalle.getPrecioUnitario() != null) {
            existente.setPrecioUnitario(detalle.getPrecioUnitario());
        }
        if (detalle.getSubtotal() != null) {
            existente.setSubtotal(detalle.getSubtotal());
        }
        if (detalle.getVenta() != null) {
            existente.setVenta(detalle.getVenta());
        }
        if (detalle.getProducto() != null) {
            existente.setProducto(detalle.getProducto());
        }

        Detalle actualizado = detalleRepository.save(existente);
        log.info("Detalle con ID {} actualizado correctamente", id);
        return convertirDTO(actualizado);
    }

    public String eliminarDetalle(Integer id) {
        log.info("Intentando eliminar detalle con ID {}", id);
        try {
            Detalle detalle = detalleRepository.findById(id)
                    .orElseThrow(() -> {
                        log.warn("No se encontró el detalle con ID {}", id);
                        return new RuntimeException("No se pudo eliminar. La ID " + id + " no existe");
                    });
            detalleRepository.delete(detalle);
            log.info("Detalle con ID {} eliminado correctamente", id);
            return "El detalle con ID " + id + " ha sido retirado exitosamente";
        } catch (RuntimeException e) {
            return e.getMessage();
        }
    }
}