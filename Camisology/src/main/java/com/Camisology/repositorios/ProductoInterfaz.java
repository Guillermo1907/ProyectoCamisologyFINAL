package com.Camisology.repositorios;

import org.springframework.data.jpa.repository.JpaRepository;
import com.Camisology.dtos.ProductoDto;

/**
 * Interfaz que extiende JpaRepository para realizar operaciones sobre los productos.
 * Permite encontrar un producto por su nombre.
 * 
 * @author GPR
 * @date 06/03/2025
 */
public interface ProductoInterfaz extends JpaRepository<ProductoDto, Long> {

    ProductoDto findByNombre(String nombre);
}
