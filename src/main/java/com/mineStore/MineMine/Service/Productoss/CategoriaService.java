package com.mineStore.MineMine.Service.Productoss;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mineStore.MineMine.DTO.Productoss.CategoriaDTO;
import com.mineStore.MineMine.Model.Productoss.Categoria;
import com.mineStore.MineMine.Model.Productoss.Productos;
import com.mineStore.MineMine.Repository.Productoss.CategoriaRepository;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

@Slf4j 
@Service 
@Transactional 
public class CategoriaService {

    @Autowired 
    private CategoriaRepository categoriaRepository;

    private CategoriaDTO convertirDTO (Categoria categoria){
        CategoriaDTO dto = new CategoriaDTO();
        dto.setId_categoria(categoria.getId_categoria());
        dto.setNombreCategoria(categoria.getNombreCategoria());
        dto.setNombreProductos(categoria.getProductos() != null 
            ? categoria.getProductos().stream().map(Productos::getNombreProducto).toList() 
            : List.of());

        return dto;
    }

    public List<CategoriaDTO> obtenerTodos(){
        log.info("Obteniendo Categorias...");
        return categoriaRepository.findAll().stream()
                .map(this::convertirDTO)
                .toList();
    }

    public CategoriaDTO buscarPorId(Integer id){
        log.info("Buscando categoria con ID {}", id);
        Categoria categoria = categoriaRepository.findById(id)
            .orElseThrow(() -> {
                log.warn("No se encontró la categoria con ID {}", id);
                return new RuntimeException("categoria no encontrada");
            });
        log.info("categoria '{}' encontrado correctamente", categoria.getNombreCategoria());
        return convertirDTO(categoria);  
    }
    
    public CategoriaDTO guardarCategoria(Categoria categoria) {
        log.info("Guardando categoria '{}'", categoria.getNombreCategoria());
        Categoria categorias = categoriaRepository.save(categoria);
        log.info("categoria guardado con ID {}", categorias.getId_categoria());
        return convertirDTO(categorias);
    }

    public CategoriaDTO actualizarCategoria(Integer id, Categoria categoria) {
        log.info("Actualizando categoria con ID {}", id);
        Categoria categorias = categoriaRepository.findById(id)
                .orElseThrow(() -> {
                log.warn("No se encontró la categoria con ID {}", id);
                return new RuntimeException("No existe la categoria");
            });
        if(categoria.getNombreCategoria()!= null){
            categorias.setNombreCategoria(categoria.getNombreCategoria());
        }
        Categoria actualizada = categoriaRepository.save(categorias);
        log.info("categoria '{}' actualizada correctamente", actualizada.getNombreCategoria());
        return convertirDTO(actualizada);
    }

    public String eliminarCategoria (Integer id){
        log.info("Intentando eliminar categoria con ID {}", id);
        try {
            Categoria categoria = categoriaRepository.findById(id)
                    .orElseThrow(() -> {
                    log.warn("No se encontró la categoria con ID {}", id);
                    return new RuntimeException("No se pudo eliminar, La ID " + id + " no existe");
                });
            
            if (categoria.getProductos() != null && !categoria.getProductos().isEmpty()) {
                log.warn("No se puede eliminar la categoria '{}' porque tiene productos asociados", categoria.getNombreCategoria());
                return "No se puede eliminar la categoría porque tiene productos asociados";
            }
            
            categoriaRepository.delete(categoria);
            log.info("imagen '{}' eliminada correctamente", categoria.getNombreCategoria());
            return "La categoria con ID " + categoria.getNombreCategoria() + " Ha sido retirado exitosamente";
        } catch (RuntimeException e) {
            return e.getMessage();
        }
    }

}