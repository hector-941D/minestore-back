package com.mineStore.MineMine.Service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.mineStore.MineMine.DTO.DetalleDTO;
import com.mineStore.MineMine.DTO.Ventass.VentaDTO;
import com.mineStore.MineMine.Model.Detalle;
import com.mineStore.MineMine.Model.Ventass.MetodoEnvio;
import com.mineStore.MineMine.Model.Ventass.MetodoPago;
import com.mineStore.MineMine.Model.Ventass.Tipo;
import com.mineStore.MineMine.Model.Usuarioss.Usuario;
import com.mineStore.MineMine.Model.Ventass.Venta;
import com.mineStore.MineMine.Repository.Ventass.VentaRepository;
import com.mineStore.MineMine.Service.Ventass.VentaService;

@ExtendWith(MockitoExtension.class)
class VentaServiceTest {

    @Mock
    private VentaRepository ventaRepository;

    @Mock
    private DetalleService detalleService;

    @InjectMocks
    private VentaService ventaService;

    private Venta ventaEjemplo;
    private Usuario usuarioEjemplo;

    @BeforeEach
    void setUp() {
        usuarioEjemplo = new Usuario();
        usuarioEjemplo.setId_usuario(1);
        usuarioEjemplo.setNombre_usuario("Carlos");

        Tipo tipo = new Tipo();
        tipo.setId_tipo(1);
        tipo.setNombreTipo("Boleta");

        MetodoPago pago = new MetodoPago();
        pago.setId_metodo_pago(1);
        pago.setNombreMetodoPago("Débito");

        MetodoEnvio envio = new MetodoEnvio();
        envio.setId_metodo_envio(1);
        envio.setNombreMetodoEnvio("Despacho a domicilio");

        ventaEjemplo = new Venta();
        ventaEjemplo.setId_venta(1);
        ventaEjemplo.setFechaVenta(LocalDateTime.now());
        ventaEjemplo.setTotalVenta(30000.0);
        ventaEjemplo.setUsuario(usuarioEjemplo);
        ventaEjemplo.setTipo(tipo);
        ventaEjemplo.setMetodoPago(pago);
        ventaEjemplo.setMetodoEnvio(envio);
    }

    @Test
    @DisplayName("Debe retornar todas las ventas convertidas a DTO")
    void obtenerTodos_RetornaListaDTO() {
        when(ventaRepository.findAll()).thenReturn(List.of(ventaEjemplo));

        List<VentaDTO> resultado = ventaService.obtenerTodos();

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals("Carlos", resultado.get(0).getNombreUsuario());
        assertEquals("Boleta", resultado.get(0).getNombreTipo());
        assertEquals("Débito", resultado.get(0).getNombreMetodoPago());
        assertEquals("Despacho a domicilio", resultado.get(0).getNombreMetodoEnvio());
        verify(ventaRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Debe retornar VentaDTO cuando la ID existe")
    void buscarPorId_CuandoExiste_RetornaDTO() {
        when(ventaRepository.findById(1)).thenReturn(Optional.of(ventaEjemplo));

        VentaDTO resultado = ventaService.buscarPorId(1);

        assertNotNull(resultado);
        assertEquals(1, resultado.getId_venta());
        assertEquals(30000.0, resultado.getTotalVenta());
        verify(ventaRepository, times(1)).findById(1);
    }

    @Test
    @DisplayName("Debe lanzar RuntimeException cuando la venta no existe")
    void buscarPorId_CuandoNoExiste_LanzaExcepcion() {
        when(ventaRepository.findById(99)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            ventaService.buscarPorId(99);
        });

        assertEquals("Venta no encontrada", exception.getMessage());
    }

    @Test
    @DisplayName("Debe calcular subtotales y totalVenta automáticamente al guardar venta con detalles")
    void guardarVenta_CalculaSubtotalesYTotal() {
        Detalle d1 = new Detalle();
        d1.setCantidad(2);
        d1.setPrecioUnitario(10000.0);

        Detalle d2 = new Detalle();
        d2.setCantidad(1);
        d2.setPrecioUnitario(5000.0);

        Venta nuevaVenta = new Venta();
        nuevaVenta.setUsuario(usuarioEjemplo);
        nuevaVenta.setDetalles(List.of(d1, d2));

        when(ventaRepository.save(any(Venta.class))).thenAnswer(inv -> {
            Venta v = inv.getArgument(0);
            v.setId_venta(10);
            return v;
        });
        when(detalleService.convertirDTO(any(Detalle.class))).thenReturn(new DetalleDTO());

        VentaDTO resultado = ventaService.guardarVenta(nuevaVenta);

        assertNotNull(resultado);
        assertEquals(10, resultado.getId_venta());
        assertEquals(20000.0, d1.getSubtotal());
        assertEquals(5000.0, d2.getSubtotal());
        assertEquals(25000.0, resultado.getTotalVenta());
        assertEquals(nuevaVenta, d1.getVenta());
        verify(ventaRepository, times(1)).save(nuevaVenta);
    }

    @Test
    @DisplayName("Debe actualizar el total de la venta cuando existe")
    void actualizarVenta_CuandoExiste_ActualizaCampos() {
        Venta datosNuevos = new Venta();
        datosNuevos.setTotalVenta(45000.0);

        when(ventaRepository.findById(1)).thenReturn(Optional.of(ventaEjemplo));
        when(ventaRepository.save(any(Venta.class))).thenAnswer(inv -> inv.getArgument(0));

        VentaDTO resultado = ventaService.actualizarVenta(1, datosNuevos);

        assertEquals(45000.0, resultado.getTotalVenta());
        verify(ventaRepository, times(1)).save(ventaEjemplo);
    }

    @Test
    @DisplayName("Debe eliminar la venta y retornar mensaje de éxito")
    void eliminarVenta_CuandoExiste_EliminaYRetornaMensaje() {
        when(ventaRepository.findById(1)).thenReturn(Optional.of(ventaEjemplo));
        doNothing().when(ventaRepository).delete(ventaEjemplo);

        String respuesta = ventaService.eliminarVenta(1);

        assertEquals("La venta con ID 1 ha sido retirada exitosamente", respuesta);
        verify(ventaRepository, times(1)).delete(ventaEjemplo);
    }

    @Test
    @DisplayName("Debe retornar mensaje de error controlado si la ID de venta a eliminar no existe")
    void eliminarVenta_CuandoNoExiste_RetornaMensajeError() {
        when(ventaRepository.findById(99)).thenReturn(Optional.empty());

        String respuesta = ventaService.eliminarVenta(99);

        assertEquals("No se pudo eliminar. La ID 99 no existe", respuesta);
        verify(ventaRepository, never()).delete(any());
    }
}