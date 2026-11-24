package com.mineStore.MineMine.Service.Productoss;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mineStore.MineMine.DTO.Productoss.ProductosDTO;
import com.mineStore.MineMine.Model.Productoss.Productos;
import com.mineStore.MineMine.Repository.Productoss.ProductosRepository;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Transactional 
public class ProductoService {
    
    @Autowired
    private ProductosRepository productoRepository;

    private ProductosDTO convertirADTO(Productos producto) {
        ProductosDTO dto = new ProductosDTO();
        dto.setId_producto(producto.getId_producto());
        dto.setNombreProducto(producto.getNombreProducto());
        dto.setDescripcionProducto(producto.getDescripcionProducto());
        dto.setPrecioProducto(producto.getPrecioProducto());
        dto.setStockProducto(producto.getStockProducto());
        dto.setId_categoria(producto.getCategoria().getId_categoria());
        dto.setId_imagen(producto.getImagen().getId_imagen());
        return dto;
    }

    public List<ProductosDTO> obtenerTodos(){
        log.info("Obteniendo todos los productos");
        return productoRepository.findAll().stream()
                .map(this::convertirADTO)
                .toList();
    }

    public ProductosDTO buscarPorId(Integer id){
        log.info("Buscando producto con ID {}", id);
        Productos producto = productoRepository.findById(id)
            .orElseThrow(() -> {
                log.warn("No se encontró el producto con ID {}", id);
                return new RuntimeException("Producto no encontrado");
            });
        log.info("Producto '{}' encontrado correctamente", producto.getNombreProducto());
        return convertirADTO(producto);  
    }

    public ProductosDTO guardarProducto(Productos productos) {
        log.info("Guardando producto '{}'", productos.getNombreProducto());
        Productos producto = productoRepository.save(productos);
        log.info("Producto guardado con ID {}", producto.getId_producto());
        return convertirADTO(producto);
    }

    public ProductosDTO actualizarProducto(Integer id, Productos producto) {
        log.info("Actualizando producto con ID {}", id);
        Productos productos = productoRepository.findById(id)
                .orElseThrow(() -> {
                log.warn("No se encontró el producto con ID {}", id);
                return new RuntimeException("No existe el producto");
            });
        if(producto.getNombreProducto()!= null){
            productos.setNombreProducto(producto.getNombreProducto());
        }
        if (producto.getDescripcionProducto() != null){
            productos.setDescripcionProducto(producto.getDescripcionProducto());
        }
        if (producto.getPrecioProducto() != null){
            productos.setPrecioProducto(producto.getPrecioProducto());
        }
        if (producto.getStockProducto() != null){
            productos.setStockProducto(producto.getStockProducto());
        }
        if (producto.getCategoria() != null){
            productos.setCategoria(producto.getCategoria());
        }
        if (producto.getImagen() != null){
            productos.setImagen(producto.getImagen());
        }
        Productos actualizado = productoRepository.save(productos);
        log.info("Producto '{}' actualizado correctamente", actualizado.getNombreProducto());
        return convertirADTO(actualizado);
    }

    public String eliminarProducto (Integer id){
        log.info("Intentando eliminar producto con ID {}", id);
        try {
            Productos producto = productoRepository.findById(id)
                    .orElseThrow(() -> {
                    log.warn("No se encontró el producto con ID {}", id);
                    return new RuntimeException("No se pudo eliminar. La ID " + id + " no existe");
                });
            productoRepository.delete(producto);
            log.info("Producto '{}' eliminado correctamente", producto.getNombreProducto());
            return "El Producto " + producto.getNombreProducto() + " Ha sido retirado exitosamente";
        } catch (RuntimeException e) {
            return e.getMessage();
        }
    }
}