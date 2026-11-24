package com.mineStore.MineMine.Service.Productoss;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mineStore.MineMine.DTO.Productoss.ImagenDTO;
import com.mineStore.MineMine.Model.Productoss.Imagen;
import com.mineStore.MineMine.Model.Productoss.Productos;
import com.mineStore.MineMine.Repository.Productoss.ImagenRepository;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

@Slf4j 
@Service 
@Transactional 
public class ImagenService {

    @Autowired 
    private ImagenRepository imagenRepository;

    private ImagenDTO convertirDTO(Imagen imagen){
        ImagenDTO dto =  new ImagenDTO();
        dto.setId_imagen(imagen.getId_imagen());
        dto.setUrlImagen(imagen.getUrlImagen());
        dto.setNombreProductos(imagen.getProductos() != null 
            ? imagen.getProductos().stream().map(Productos::getNombreProducto).toList() 
            : List.of());

        return dto;
    }

    public List<ImagenDTO> obtenerTodos(){
        log.info("Obteniendo todas las imagenes");
        return imagenRepository.findAll().stream()
                .map(this::convertirDTO)
                .toList();
    }
    
    public ImagenDTO buscarPorId(Integer id){
        log.info("Buscando imagen con ID {}", id);
        Imagen imagen = imagenRepository.findById(id)
            .orElseThrow(() -> {
                log.warn("No se encontró la imagen con ID {}", id);
                return new RuntimeException("imagen no encontrada");
            });
        log.info("imagen '{}' encontrado correctamente", imagen.getUrlImagen());
        return convertirDTO(imagen);  
    }

    public ImagenDTO guardarImagen(Imagen imagenes) {
        log.info("Guardando producto '{}'", imagenes.getUrlImagen());
        Imagen imagen = imagenRepository.save(imagenes);
        log.info("imagen guardado con ID {}", imagen.getId_imagen());
        return convertirDTO(imagen);
    }

    public ImagenDTO actualizarImagen(Integer id, Imagen imagen) {
        log.info("Actualizando imagen con ID {}", id);
        Imagen imagenes = imagenRepository.findById(id)
                .orElseThrow(() -> {
                log.warn("No se encontró la imagen con ID {}", id);
                return new RuntimeException("No existe la imagen");
            });
        if(imagen.getUrlImagen()!= null){
            imagenes.setUrlImagen(imagen.getUrlImagen());;
        }
        Imagen actualizada = imagenRepository.save(imagenes);
        log.info("Imagen '{}' actualizada correctamente", actualizada.getUrlImagen());
        return convertirDTO(actualizada);
    }

    public String eliminarImagen (Integer id){
        log.info("Intentando eliminar imagen con ID {}", id);
        try {
            Imagen imagen = imagenRepository.findById(id)
                    .orElseThrow(() -> {
                    log.warn("No se encontró la imagen con ID {}", id);
                    return new RuntimeException("No se pudo eliminar, La ID " + id + " no existe");
                });

            if (imagen.getProductos() != null && !imagen.getProductos().isEmpty()) {
                log.warn("No se puede eliminar la imagen con ID {} porque tiene productos asociados", id);
                return "No se puede eliminar la imagen porque tiene productos asociados";
            }
            
            imagenRepository.delete(imagen);
            log.info("imagen '{}' eliminada correctamente", imagen.getUrlImagen());
            return "La imagen con ID " + imagen.getId_imagen() + " Ha sido retirado exitosamente";
        } catch (RuntimeException e) {
            return e.getMessage();
        }
    }

    
}