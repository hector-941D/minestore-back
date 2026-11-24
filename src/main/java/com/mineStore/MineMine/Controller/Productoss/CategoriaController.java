package com.mineStore.MineMine.Controller.Productoss;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mineStore.MineMine.DTO.Productoss.CategoriaDTO;
import com.mineStore.MineMine.Model.Productoss.Categoria;
import com.mineStore.MineMine.Service.Productoss.CategoriaService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@Tag(name = "Categorias", description = "Operaciones CRUD para Categorias")
@RestController
@RequestMapping("/api/v1/categorias")
public class CategoriaController {

    @Autowired
    private CategoriaService categoriaService;

    @Operation(summary = "Obtener todas las categorias", description = "Devuelve la lista completa de categorias registradas")
    @GetMapping
    public ResponseEntity<List<CategoriaDTO>> obtenerTodasCategorias() {
        return ResponseEntity.ok(categoriaService.obtenerTodos());
    }

    @Operation(summary = "Buscar categoria por ID", description = "Devuelve una categoria según su ID")
    @GetMapping("/{id}")
    public ResponseEntity<?> buscarPorId(@PathVariable Integer id) {
        try {
            return ResponseEntity.ok(categoriaService.buscarPorId(id));
        } catch (RuntimeException e) {
            return new ResponseEntity<>("No se encontró la categoria", HttpStatus.NOT_FOUND);
        }
    }

    @Operation(summary = "Guardar una categoria", description = "Registra una nueva categoria")
    @PostMapping
    public ResponseEntity<?> guardarCategoria(@Valid @RequestBody Categoria categoria) {
        try {
            CategoriaDTO guardada = categoriaService.guardarCategoria(categoria);
            return new ResponseEntity<>(guardada, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>("No se pudo guardar la categoria, verifique los datos ingresados", HttpStatus.BAD_REQUEST);
        }
    }

    @Operation(summary = "Actualizar una categoria", description = "Actualiza una categoria buscandola por su ID")
    @PutMapping("/{id}")
    public ResponseEntity<?> actualizarCategoria(@PathVariable Integer id, @Valid @RequestBody Categoria categoria) {
        try {
            return ResponseEntity.ok(categoriaService.actualizarCategoria(id, categoria));
        } catch (RuntimeException e) {
            return new ResponseEntity<>("Error, no se encontró la categoria", HttpStatus.NOT_FOUND);
        }
    }

    @Operation(summary = "Eliminar una categoria", description = "Borra una categoria utilizando su ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminarCategoria(@PathVariable Integer id) {
        String resultado = categoriaService.eliminarCategoria(id);
        if (resultado.contains("exitosamente")) {
            return ResponseEntity.ok(resultado);
        }
        return new ResponseEntity<>(resultado, HttpStatus.BAD_REQUEST);
    }
}