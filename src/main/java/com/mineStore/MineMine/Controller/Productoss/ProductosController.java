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

import com.mineStore.MineMine.DTO.Productoss.ProductosDTO;
import com.mineStore.MineMine.Model.Productoss.Productos;
import com.mineStore.MineMine.Service.Productoss.ProductoService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@Tag(name = "Productos", description = "Operaciones CRUD para Productos")
@RestController 
@RequestMapping ("/api/v1/productos")
public class ProductosController {

    @Autowired
    private ProductoService productoService;

    @Operation(summary = "Metodo para obtener todos los productos registrados", description = "Devuelve la lista completa de productos registrados")
    @GetMapping
    public ResponseEntity<List<ProductosDTO>> obtenerTodosProductos(){
        return ResponseEntity.ok(productoService.obtenerTodos());
    }

    @Operation(summary = "Metodo para buscar un producto segun su id", description = "Devuelve un producto segun su id")
    @GetMapping("/{id}")
    public ResponseEntity<?> buscarPorId(@PathVariable Integer id){
        try{
            ProductosDTO producto = productoService.buscarPorId(id);
            return new ResponseEntity<>(producto, HttpStatus.OK);
        }catch (RuntimeException e){
            return new ResponseEntity<>("No se encontro el producto ", HttpStatus.NOT_FOUND);
        }
    }

    @Operation(summary = "Metodo para guardar un producto", description = "Registra un producto")
    @PostMapping
    public ResponseEntity<?> guardarProducto(@Valid @RequestBody Productos producto){
        try{
            ProductosDTO guardarProducto = productoService.guardarProducto(producto);
            return new ResponseEntity<>(guardarProducto, HttpStatus.CREATED);
        } catch (Exception e){
            return new ResponseEntity<>("No se pudo guardar el producto, verifique que los datos fueron ingresados correctamente ",HttpStatus.BAD_REQUEST);
        }
    }

    @Operation(summary = "Metodo para actualizar un producto segun su id", description = "Actualiza buscandolo por su id")
    @PutMapping("/{id}")
    public ResponseEntity<?> actualizarProducto(@PathVariable Integer id,@Valid @RequestBody Productos producto){
        try{
            ProductosDTO actualizado = productoService.actualizarProducto(id, producto);
            return new ResponseEntity<>(actualizado, HttpStatus.OK);
        } catch (RuntimeException e){
            return new ResponseEntity<>("Error, no se encontro el producto ",HttpStatus.NOT_FOUND);
        }
    }

    @Operation(summary = "Metodo para eliminar un producto", description = "Borra un producto utilizando su id")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminarProducto(@PathVariable Integer id){
        String resultado = productoService.eliminarProducto(id);
        if(resultado.contains("exitosamente")){
            return new ResponseEntity<>(resultado, HttpStatus.OK);
        }
        return new ResponseEntity<>("Error, No se encontro el producto a borrar ", HttpStatus.NOT_FOUND);
    }
}