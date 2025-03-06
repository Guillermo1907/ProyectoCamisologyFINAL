package com.Camisology.repositorios;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.Camisology.dtos.UsuarioDto;

/**
 * Interfaz que extiende JpaRepository para realizar operaciones sobre los usuarios.
 * Permite encontrar un usuario por su ID o por su correo.
 * 
 * @author GPR
 * @date 06/03/2025
 */
@Repository
public interface UsuarioInterfaz extends JpaRepository<UsuarioDto, Long> {

    UsuarioDto findByidUsuario(long idUsuario);

    UsuarioDto findByCorreo(String correo);
}
