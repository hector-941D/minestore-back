package com.mineStore.MineMine.Repository.Usuarioss;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.mineStore.MineMine.Model.Usuarioss.Usuario;

@Repository 
public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {
    
    @Query("SELECT u FROM Usuario u WHERE u.correo_electronico = :correo")
    Optional<Usuario> buscarPorCorreo(@Param("correo") String correo);
}
