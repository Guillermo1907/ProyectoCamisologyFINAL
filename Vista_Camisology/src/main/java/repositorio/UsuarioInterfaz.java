package repositorio;

import org.springframework.data.jpa.repository.JpaRepository;
import dtos.LoginDto;

/**
 * Interfaz que extiende {@link JpaRepository} para realizar operaciones CRUD sobre la entidad {@link LoginDto}.
 * Permite la gestión de los usuarios y la búsqueda de un usuario por su correo electrónico.
 * 
 * @author GPR
 * @date 06/03/2025
 */
public interface UsuarioInterfaz extends JpaRepository<LoginDto, Long> {
    LoginDto findByCorreo(String correo);
}
