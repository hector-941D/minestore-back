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

import com.mineStore.MineMine.DTO.Productoss.ImagenDTO;
import com.mineStore.MineMine.Model.Productoss.Imagen;
import com.mineStore.MineMine.Service.Productoss.ImagenService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;


@Tag(name= "Productos", description = "Operaciones CRUD para Imagenes")
@RestController 
@RequestMapping("api/v1/imagenes")
public class ImagenController {

    @Autowired
    private ImagenService imagenService;

    @Operation(summary = "Metodo para obtener todas las imagenes registradas", description = "Devuelve la lista completa de imagenes registradas")
    @GetMapping
    public ResponseEntity<?> obtenerTodasImagenes(){
        List<ImagenDTO> imagenes = imagenService.obtenerTodos();
        if(imagenes.isEmpty()){
            return new ResponseEntity<>("No hay productos en este momento ", HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(imagenes, HttpStatus.OK);
    }

    @Operation(summary = "Metodo para buscar una imagen segun su id", description = "Devuelve una imagen segun su id")
    @GetMapping("/{id}")
    public ResponseEntity<?> buscarPorId(@PathVariable Integer id){
        try{
            ImagenDTO imagen= imagenService.buscarPorId(id);
            return new ResponseEntity<>(imagen, HttpStatus.OK);
        }catch (RuntimeException e){
            return new ResponseEntity<>("No se encontro la imagen ", HttpStatus.NOT_FOUND);
        }
    }

    @Operation(summary = "Metodo para guardar una imagen", description = "Registra una imagen")
    @PostMapping
    public ResponseEntity<?> guardarImagen(@Valid @RequestBody Imagen imagenes){
        try{
            ImagenDTO guardarImagen = imagenService.guardarImagen(imagenes);
            return new ResponseEntity<>(guardarImagen, HttpStatus.CREATED);
        } catch (Exception e){
            return new ResponseEntity<>("No se pudo guardar la imagen, verifique que los datos fueron ingresados correctamente ",HttpStatus.BAD_REQUEST);
        }
    }

    @Operation(summary = "Metodo para actualizar una imagen segun su id", description = "Actualiza buscando por id")
    @PutMapping("/{id}")
    public ResponseEntity<?> actualizarImagen(@PathVariable Integer id,@Valid @RequestBody Imagen imagen){
        try{
            ImagenDTO actualizada = imagenService.actualizarImagen(id, imagen);
            return new ResponseEntity<>(actualizada, HttpStatus.OK);
        } catch (RuntimeException e){
            return new ResponseEntity<>("Error, no se encontro la imagen ",HttpStatus.NOT_FOUND);
        }
    }

    @Operation(summary = "Metodo para eliminar una imagen", description = "Borra una imagen utilizando su id")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminarImagen(@PathVariable Integer id){
        String resultado = imagenService.eliminarImagen(id);
        if(resultado.contains("exitosamente")){
            return new ResponseEntity<>(resultado, HttpStatus.OK);
        }
        return new ResponseEntity<>("Error, No se encontro el producto a borrar ", HttpStatus.NOT_FOUND);
    }
}