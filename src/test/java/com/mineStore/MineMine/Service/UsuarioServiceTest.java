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
import org.springframework.security.crypto.password.PasswordEncoder;

import com.mineStore.MineMine.DTO.Usuarioss.UsuarioDTO;
import com.mineStore.MineMine.Model.Usuarioss.Comuna;
import com.mineStore.MineMine.Model.Usuarioss.Rol;
import com.mineStore.MineMine.Model.Usuarioss.Usuario;
import com.mineStore.MineMine.Repository.Usuarioss.UsuarioRepository;
import com.mineStore.MineMine.Service.Usuarioss.UsuarioService;

@ExtendWith(MockitoExtension.class)
class UsuarioServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UsuarioService usuarioService;

    private Usuario usuarioEjemplo;
    private Rol rolEjemplo;
    private Comuna comunaEjemplo;

    @BeforeEach
    void setUp() {
        rolEjemplo = new Rol();
        rolEjemplo.setId_rol(1);
        rolEjemplo.setNombreRol("ADMIN");

        comunaEjemplo = new Comuna();
        comunaEjemplo.setId_comuna(1);
        comunaEjemplo.setNombreComuna("Santiago");

        usuarioEjemplo = new Usuario();
        usuarioEjemplo.setId_usuario(1);
        usuarioEjemplo.setNombre_usuario("Carlos");
        usuarioEjemplo.setCorreo_electronico("carlos@minemine.cl");
        usuarioEjemplo.setContrasena("clavePlana123");
        usuarioEjemplo.setRol(rolEjemplo);
        usuarioEjemplo.setComuna(comunaEjemplo);
    }

    @Test
    @DisplayName("Debe retornar la lista de usuarios convertidos a DTO")
    void obtenerTodos_RetornaListaDTO() {
        when(usuarioRepository.findAll()).thenReturn(List.of(usuarioEjemplo));

        List<UsuarioDTO> resultado = usuarioService.obtenerTodos();

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals("Carlos", resultado.get(0).getNombre_usuario());
        assertEquals("carlos@minemine.cl", resultado.get(0).getCorreo_electronico());
        assertEquals("ADMIN", resultado.get(0).getNombreRol());
        assertEquals("Santiago", resultado.get(0).getNombreComuna());
        verify(usuarioRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Debe retornar un UsuarioDTO cuando el ID existe")
    void buscarPorId_CuandoExiste_RetornaDTO() {
        when(usuarioRepository.findById(1)).thenReturn(Optional.of(usuarioEjemplo));

        UsuarioDTO resultado = usuarioService.buscarPorId(1);

        assertNotNull(resultado);
        assertEquals(1, resultado.getId_usuario());
        assertEquals("Carlos", resultado.getNombre_usuario());
        verify(usuarioRepository, times(1)).findById(1);
    }

    @Test
    @DisplayName("Debe lanzar RuntimeException cuando el ID no existe al buscar")
    void buscarPorId_CuandoNoExiste_LanzaExcepcion() {
        when(usuarioRepository.findById(99)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            usuarioService.buscarPorId(99);
        });

        assertEquals("Usuario no encontrado", exception.getMessage());
        verify(usuarioRepository, times(1)).findById(99);
    }

    @Test
    @DisplayName("Debe encriptar la contraseña con PasswordEncoder antes de guardar el usuario")
    void guardarUsuario_EncriptaContrasenaYGuarda() {
        when(passwordEncoder.encode("clavePlana123")).thenReturn("$2a$10$hashEncriptadoBCrypt");
        when(usuarioRepository.save(any(Usuario.class))).thenAnswer(invocation -> invocation.getArgument(0));

        UsuarioDTO resultado = usuarioService.guardarUsuario(usuarioEjemplo);

        assertNotNull(resultado);
        assertEquals("$2a$10$hashEncriptadoBCrypt", usuarioEjemplo.getContrasena());
        assertEquals("Carlos", resultado.getNombre_usuario());
        verify(passwordEncoder, times(1)).encode("clavePlana123");
        verify(usuarioRepository, times(1)).save(usuarioEjemplo);
    }

    @Test
    @DisplayName("Debe actualizar campos y re-encriptar contraseña si viene en la petición")
    void actualizarUsuario_CuandoExiste_ActualizaYEncripta() {
        Usuario datosNuevos = new Usuario();
        datosNuevos.setNombre_usuario("Carlos Actualizado");
        datosNuevos.setContrasena("nuevaClave456");

        when(usuarioRepository.findById(1)).thenReturn(Optional.of(usuarioEjemplo));
        when(passwordEncoder.encode("nuevaClave456")).thenReturn("$2a$10$nuevoHashBCrypt");
        when(usuarioRepository.save(any(Usuario.class))).thenAnswer(invocation -> invocation.getArgument(0));

        UsuarioDTO resultado = usuarioService.actualizarUsuario(1, datosNuevos);

        assertEquals("Carlos Actualizado", resultado.getNombre_usuario());
        assertEquals("$2a$10$nuevoHashBCrypt", usuarioEjemplo.getContrasena());
        // Verifica que campos no enviados mantengan su valor original
        assertEquals("carlos@minemine.cl", resultado.getCorreo_electronico());
        verify(passwordEncoder, times(1)).encode("nuevaClave456");
        verify(usuarioRepository, times(1)).save(usuarioEjemplo);
    }

    @Test
    @DisplayName("Debe eliminar el usuario y retornar mensaje de éxito cuando el ID existe")
    void eliminarUsuario_CuandoExiste_EliminaYRetornaMensaje() {
        when(usuarioRepository.findById(1)).thenReturn(Optional.of(usuarioEjemplo));
        doNothing().when(usuarioRepository).delete(usuarioEjemplo);

        String respuesta = usuarioService.eliminarUsuario(1);

        assertEquals("El Usuario Carlos ha sido retirado exitosamente", respuesta);
        verify(usuarioRepository, times(1)).delete(usuarioEjemplo);
    }

    @Test
    @DisplayName("Debe retornar mensaje de error controlado cuando el ID a eliminar no existe")
    void eliminarUsuario_CuandoNoExiste_RetornaMensajeError() {
        when(usuarioRepository.findById(99)).thenReturn(Optional.empty());

        String respuesta = usuarioService.eliminarUsuario(99);

        assertEquals("No se pudo eliminar. La ID 99 no existe", respuesta);
        verify(usuarioRepository, never()).delete(any());
    }
}