package com.mineStore.MineMine.Service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.mineStore.MineMine.DTO.Productoss.ProductosDTO;
import com.mineStore.MineMine.Model.Productoss.Categoria;
import com.mineStore.MineMine.Model.Productoss.Imagen;
import com.mineStore.MineMine.Model.Productoss.Productos;
import com.mineStore.MineMine.Repository.Productoss.ProductosRepository;
import com.mineStore.MineMine.Service.Productoss.ProductoService;

@ExtendWith(MockitoExtension.class)
class ProductoServiceTest {

    @Mock
    private ProductosRepository productoRepository;

    @InjectMocks
    private ProductoService productoService;

    private Productos productoEjemplo;
    private Categoria categoriaEjemplo;
    private Imagen imagenEjemplo;

    @BeforeEach
    void setUp() {
        categoriaEjemplo = new Categoria();
        categoriaEjemplo.setId_categoria(1);
        categoriaEjemplo.setNombreCategoria("Poleras");

        imagenEjemplo = new Imagen();
        imagenEjemplo.setId_imagen(1);
        imagenEjemplo.setUrlImagen("https://minemine.cl/img/polera.png");

        productoEjemplo = new Productos();
        productoEjemplo.setId_producto(1);
        productoEjemplo.setNombreProducto("Polera Creeper");
        productoEjemplo.setDescripcionProducto("Polera 100% algodón");
        productoEjemplo.setPrecioProducto(15990);
        productoEjemplo.setStockProducto(20);
        productoEjemplo.setCategoria(categoriaEjemplo);
        productoEjemplo.setImagen(imagenEjemplo);
    }

    @Test
    @DisplayName("Debe retornar la lista completa de productos como DTO")
    void obtenerTodos_RetornaListaDTO() {
        when(productoRepository.findAll()).thenReturn(List.of(productoEjemplo));

        List<ProductosDTO> resultado = productoService.obtenerTodos();

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals("Polera Creeper", resultado.get(0).getNombreProducto());
        assertEquals(15990, resultado.get(0).getPrecioProducto());
        assertEquals(1, resultado.get(0).getId_categoria());
        assertEquals(1, resultado.get(0).getId_imagen());
        verify(productoRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Debe retornar ProductosDTO cuando el ID existe")
    void buscarPorId_CuandoExiste_RetornaDTO() {
        when(productoRepository.findById(1)).thenReturn(Optional.of(productoEjemplo));

        ProductosDTO resultado = productoService.buscarPorId(1);

        assertNotNull(resultado);
        assertEquals(1, resultado.getId_producto());
        assertEquals("Polera Creeper", resultado.getNombreProducto());
        verify(productoRepository, times(1)).findById(1);
    }

    @Test
    @DisplayName("Debe lanzar RuntimeException cuando el producto no existe al buscar")
    void buscarPorId_CuandoNoExiste_LanzaExcepcion() {
        when(productoRepository.findById(99)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            productoService.buscarPorId(99);
        });

        assertEquals("Producto no encontrado", exception.getMessage());
        verify(productoRepository, times(1)).findById(99);
    }

    @Test
    @DisplayName("Debe guardar el producto y retornar su DTO")
    void guardarProducto_GuardaYRetornaDTO() {
        when(productoRepository.save(any(Productos.class))).thenReturn(productoEjemplo);

        ProductosDTO resultado = productoService.guardarProducto(productoEjemplo);

        assertNotNull(resultado);
        assertEquals("Polera Creeper", resultado.getNombreProducto());
        assertEquals(20, resultado.getStockProducto());
        verify(productoRepository, times(1)).save(productoEjemplo);
    }

    @Test
    @DisplayName("Debe actualizar parcialmente los campos enviados del producto")
    void actualizarProducto_CuandoExiste_ActualizaCampos() {
        Productos datosNuevos = new Productos();
        datosNuevos.setNombreProducto("Polera Enderman");
        datosNuevos.setPrecioProducto(18990);

        when(productoRepository.findById(1)).thenReturn(Optional.of(productoEjemplo));
        when(productoRepository.save(any(Productos.class))).thenAnswer(inv -> inv.getArgument(0));

        ProductosDTO resultado = productoService.actualizarProducto(1, datosNuevos);

        assertEquals("Polera Enderman", resultado.getNombreProducto());
        assertEquals(18990, resultado.getPrecioProducto());
        assertEquals("Polera 100% algodón", resultado.getDescripcionProducto());
        verify(productoRepository, times(1)).save(productoEjemplo);
    }

    @Test
    @DisplayName("Debe eliminar el producto y retornar mensaje de confirmación cuando existe")
    void eliminarProducto_CuandoExiste_EliminaYRetornaMensaje() {
        when(productoRepository.findById(1)).thenReturn(Optional.of(productoEjemplo));
        doNothing().when(productoRepository).delete(productoEjemplo);

        String respuesta = productoService.eliminarProducto(1);

        assertEquals("El Producto Polera Creeper Ha sido retirado exitosamente", respuesta);
        verify(productoRepository, times(1)).delete(productoEjemplo);
    }

    @Test
    @DisplayName("Debe retornar mensaje controlado cuando el ID a eliminar no existe")
    void eliminarProducto_CuandoNoExiste_RetornaMensajeError() {
        when(productoRepository.findById(99)).thenReturn(Optional.empty());

        String respuesta = productoService.eliminarProducto(99);

        assertEquals("No se pudo eliminar. La ID 99 no existe", respuesta);
        verify(productoRepository, never()).delete(any());
    }
}