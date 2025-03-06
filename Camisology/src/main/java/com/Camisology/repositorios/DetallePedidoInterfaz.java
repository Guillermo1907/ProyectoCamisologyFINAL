package com.Camisology.repositorios;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.Camisology.dtos.DetallePedidoDto;
import com.Camisology.dtos.PedidoDto;
import com.Camisology.dtos.ProductoDto;

/**
 * Interfaz que extiende JpaRepository para realizar operaciones sobre los detalles de los pedidos.
 * 
 * Permite encontrar detalles de pedidos por pedido y producto, así como obtener todos los detalles de un pedido.
 * 
 * @author GPR
 * @date 06/03/2025
 */
public interface DetallePedidoInterfaz extends JpaRepository<DetallePedidoDto, Long> {

    DetallePedidoDto findByPedidoAndProducto(PedidoDto pedido, ProductoDto producto);

    List<DetallePedidoDto> findByPedido(PedidoDto pedido);
}
