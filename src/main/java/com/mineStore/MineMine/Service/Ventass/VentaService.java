package com.mineStore.MineMine.Service.Ventass;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mineStore.MineMine.DTO.Ventass.VentaDTO;
import com.mineStore.MineMine.Model.Detalle;
import com.mineStore.MineMine.Model.Ventass.Venta;
import com.mineStore.MineMine.Repository.Ventass.VentaRepository;
import com.mineStore.MineMine.Service.DetalleService;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Transactional
public class VentaService {

    @Autowired
    private VentaRepository ventaRepository;

    @Autowired
    private DetalleService detalleService;

    private VentaDTO convertirDTO(Venta venta) {
        VentaDTO dto = new VentaDTO();
        dto.setId_venta(venta.getId_venta());
        dto.setFechaVenta(venta.getFechaVenta());
        dto.setTotalVenta(venta.getTotalVenta());

        if (venta.getUsuario() != null) {
            dto.setId_usuario(venta.getUsuario().getId_usuario());
            dto.setNombreUsuario(venta.getUsuario().getNombre_usuario());
        }
        if (venta.getTipo() != null) {
            dto.setId_tipo(venta.getTipo().getId_tipo());
            dto.setNombreTipo(venta.getTipo().getNombreTipo());
        }
        if (venta.getMetodoPago() != null) {
            dto.setId_metodo_pago(venta.getMetodoPago().getId_metodo_pago());
            dto.setNombreMetodoPago(venta.getMetodoPago().getNombreMetodoPago());
        }
        if (venta.getMetodoEnvio() != null) {
            dto.setId_metodo_envio(venta.getMetodoEnvio().getId_metodo_envio());
            dto.setNombreMetodoEnvio(venta.getMetodoEnvio().getNombreMetodoEnvio());
        }

        dto.setDetalles(venta.getDetalles() != null
                ? venta.getDetalles().stream().map(detalleService::convertirDTO).toList()
                : List.of());

        return dto;
    }

    public List<VentaDTO> obtenerTodos() {
        log.info("Obteniendo todas las ventas");
        return ventaRepository.findAll().stream()
                .map(this::convertirDTO)
                .toList();
    }

    public VentaDTO buscarPorId(Integer id) {
        log.info("Buscando venta con ID {}", id);
        Venta venta = ventaRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("No se encontro la venta con ID {}", id);
                    return new RuntimeException("Venta no encontrada");
                });
        log.info("Venta con ID {} encontrada correctamente", id);
        return convertirDTO(venta);
    }

    public VentaDTO guardarVenta(Venta venta) {
        log.info("Guardando nueva venta");
        if (venta.getDetalles() != null && !venta.getDetalles().isEmpty()) {
            double totalCalculado = 0.0;
            for (Detalle d : venta.getDetalles()) {
                d.setVenta(venta);
                if (d.getSubtotal() == null && d.getCantidad() != null && d.getPrecioUnitario() != null) {
                    d.setSubtotal(d.getCantidad() * d.getPrecioUnitario());
                }
                if (d.getSubtotal() != null) {
                    totalCalculado += d.getSubtotal();
                }
            }
            if (venta.getTotalVenta() == null) {
                venta.setTotalVenta(totalCalculado);
            }
        }
        Venta guardada = ventaRepository.save(venta);
        log.info("Venta guardada con ID {}", guardada.getId_venta());
        return convertirDTO(guardada);
    }

    public VentaDTO actualizarVenta(Integer id, Venta venta) {
        log.info("Actualizando venta con ID {}", id);
        Venta existente = ventaRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("No se encontro la venta con ID {}", id);
                    return new RuntimeException("No existe la venta");
                });

        if (venta.getTotalVenta() != null) {
            existente.setTotalVenta(venta.getTotalVenta());
        }
        if (venta.getUsuario() != null) {
            existente.setUsuario(venta.getUsuario());
        }
        if (venta.getTipo() != null) {
            existente.setTipo(venta.getTipo());
        }
        if (venta.getMetodoPago() != null) {
            existente.setMetodoPago(venta.getMetodoPago());
        }
        if (venta.getMetodoEnvio() != null) {
            existente.setMetodoEnvio(venta.getMetodoEnvio());
        }

        Venta actualizada = ventaRepository.save(existente);
        log.info("Venta con ID {} actualizada correctamente", id);
        return convertirDTO(actualizada);
    }

    public String eliminarVenta(Integer id) {
        log.info("Intentando eliminar venta con ID {}", id);
        try {
            Venta venta = ventaRepository.findById(id)
                    .orElseThrow(() -> {
                        log.warn("No se encontro la venta con ID {}", id);
                        return new RuntimeException("No se pudo eliminar. La ID " + id + " no existe");
                    });
            ventaRepository.delete(venta);
            log.info("Venta con ID {} eliminada correctamente", id);
            return "La venta con ID " + id + " ha sido retirada exitosamente";
        } catch (RuntimeException e) {
            return e.getMessage();
        }
    }
}