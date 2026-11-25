package com.mineStore.MineMine.Service.Usuarioss;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.mineStore.MineMine.DTO.Usuarioss.UsuarioDTO;
import com.mineStore.MineMine.Model.Usuarioss.Usuario;
import com.mineStore.MineMine.Repository.Usuarioss.UsuarioRepository;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Transactional
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private UsuarioDTO convertirADTO(Usuario usuario) {
        UsuarioDTO dto = new UsuarioDTO();
        dto.setId_usuario(usuario.getId_usuario());
        dto.setNombre_usuario(usuario.getNombre_usuario());
        dto.setCorreo_electronico(usuario.getCorreo_electronico());
        if (usuario.getRol() != null) {
            dto.setId_rol(usuario.getRol().getId_rol());
            dto.setNombreRol(usuario.getRol().getNombreRol());
        }
        if (usuario.getComuna() != null) {
            dto.setId_comuna(usuario.getComuna().getId_comuna());
            dto.setNombreComuna(usuario.getComuna().getNombreComuna());
        }
        return dto;
    }

    public List<UsuarioDTO> obtenerTodos() {
        log.info("Obteniendo todos los usuarios");
        return usuarioRepository.findAll().stream()
                .map(this::convertirADTO)
                .toList();
    }

    public UsuarioDTO buscarPorId(Integer id) {
        log.info("Buscando usuario con ID {}", id);
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("No se encontro el usuario con ID {}", id);
                    return new RuntimeException("Usuario no encontrado");
                });
        log.info("Usuario '{}' encontrado correctamente", usuario.getNombre_usuario());
        return convertirADTO(usuario);
    }

    public UsuarioDTO guardarUsuario(Usuario usuario) {
        log.info("Guardando usuario '{}'", usuario.getNombre_usuario());
        // AQUÍ: Encriptamos la contraseña antes de guardarla en PostgreSQL
        usuario.setContrasena(passwordEncoder.encode(usuario.getContrasena()));
        Usuario guardado = usuarioRepository.save(usuario);
        log.info("Usuario guardado con ID {}", guardado.getId_usuario());
        return convertirADTO(guardado);
    }

    public UsuarioDTO actualizarUsuario(Integer id, Usuario usuario) {
        log.info("Actualizando usuario con ID {}", id);
        Usuario existente = usuarioRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("No se encontro el usuario con ID {}", id);
                    return new RuntimeException("No existe el usuario");
                });

        if (usuario.getNombre_usuario() != null) {
            existente.setNombre_usuario(usuario.getNombre_usuario());
        }
        if (usuario.getCorreo_electronico() != null) {
            existente.setCorreo_electronico(usuario.getCorreo_electronico());
        }
        if (usuario.getContrasena() != null) {
            existente.setContrasena(passwordEncoder.encode(usuario.getContrasena()));
        }
        if (usuario.getRol() != null) {
            existente.setRol(usuario.getRol());
        }
        if (usuario.getComuna() != null) {
            existente.setComuna(usuario.getComuna());
        }

        Usuario actualizado = usuarioRepository.save(existente);
        log.info("Usuario '{}' actualizado correctamente", actualizado.getNombre_usuario());
        return convertirADTO(actualizado);
    }

    public String eliminarUsuario(Integer id) {
        log.info("Intentando eliminar usuario con ID {}", id);
        try {
            Usuario usuario = usuarioRepository.findById(id)
                    .orElseThrow(() -> {
                        log.warn("No se encontro el usuario con ID {}", id);
                        return new RuntimeException("No se pudo eliminar. La ID " + id + " no existe");
                    });
            usuarioRepository.delete(usuario);
            log.info("Usuario '{}' eliminado correctamente", usuario.getNombre_usuario());
            return "El Usuario " + usuario.getNombre_usuario() + " ha sido retirado exitosamente";
        } catch (RuntimeException e) {
            return e.getMessage();
        }
    }
}