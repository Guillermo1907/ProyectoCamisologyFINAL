package com.Camisology.repositorios;

import org.springframework.data.jpa.repository.JpaRepository;

import com.Camisology.dtos.PedidoDto;
import com.Camisology.dtos.UsuarioDto;

/**
 * Interfaz que extiende JpaRepository para realizar operaciones sobre los pedidos.
 * Permite encontrar un pedido por usuario y estado.
 * 
 * @author GPR
 * @date 06/03/2025
 */
public interface PedidoInterfaz extends JpaRepository<PedidoDto, Long> {

    PedidoDto findByUsuarioAndEstado(UsuarioDto usuario, String estado);
}
