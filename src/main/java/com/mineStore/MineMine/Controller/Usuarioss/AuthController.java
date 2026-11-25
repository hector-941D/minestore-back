package com.mineStore.MineMine.Controller.Usuarioss;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mineStore.MineMine.Model.Usuarioss.Usuario;
import com.mineStore.MineMine.Repository.Usuarioss.UsuarioRepository;
import com.mineStore.MineMine.Security.JwtService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Data;

@Tag(name = "Autenticación", description = "Login y generación de Token JWT")
@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtService jwtService;

    @PostMapping("/login")
    @Operation(summary = "Iniciar sesión", description = "Valida credenciales y devuelve un Token JWT junto con el rol del usuario")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        Usuario usuario = usuarioRepository.buscarPorCorreo(request.getCorreo_electronico())
                .orElse(null);

        if (usuario == null || !passwordEncoder.matches(request.getContrasena(), usuario.getContrasena())) {
            return new ResponseEntity<>(Map.of("error", "Correo o contraseña incorrectos"), HttpStatus.UNAUTHORIZED);
        }

        String rol = usuario.getRol().getNombreRol();
        String token = jwtService.generarToken(usuario.getCorreo_electronico(), rol);

        return ResponseEntity.ok(Map.of(
                "token", token,
                "tipo", "Bearer",
                "id_usuario", usuario.getId_usuario(),
                "nombre_usuario", usuario.getNombre_usuario(),
                "rol", rol
        ));
    }

    @Data
    public static class LoginRequest {
        private String correo_electronico;
        private String contrasena;
    }
}