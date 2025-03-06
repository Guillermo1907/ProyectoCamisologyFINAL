package com.Camisology.servicios;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.Camisology.dtos.DetallePedidoDto;
import com.Camisology.dtos.PedidoDto;
import com.Camisology.dtos.ProductoDto;
import com.Camisology.dtos.UsuarioDto;
import com.Camisology.repositorios.DetallePedidoInterfaz;
import com.Camisology.repositorios.PedidoInterfaz;
import com.Camisology.repositorios.ProductoInterfaz;
import com.Camisology.repositorios.UsuarioInterfaz;

/**
 * Servicio para manejar las funcionalidades del carrito de compras de un usuario,
 * incluyendo la obtención de un carrito activo, agregar y eliminar productos del carrito.
 * 
 * @author GPR
 * @date 06/03/2025
 */
@Service
public class CarritoFuncionalidades {

    @Autowired
    private PedidoInterfaz pedidoRepositorio;

    @Autowired
    private DetallePedidoInterfaz detallePedidoRepositorio;

    @Autowired
    private ProductoInterfaz productoRepositorio;

    @Autowired
    private UsuarioInterfaz usuarioRepositorio;
    
    /**
     * Obtiene el carrito activo de un usuario.
     * 
     * @param usuarioId el ID del usuario
     * @return el carrito activo del usuario
     * @throws Exception si el usuario no existe o no tiene un carrito activo
     */
    public PedidoDto obtenerCarritoActivo(Long usuarioId) throws Exception {
        Optional<UsuarioDto> usuarioOpt = usuarioRepositorio.findById(usuarioId);
        if (!usuarioOpt.isPresent()) {
            throw new Exception("Usuario no encontrado");
        }
        UsuarioDto usuario = usuarioOpt.get();
        PedidoDto carrito = pedidoRepositorio.findByUsuarioAndEstado(usuario, "Pendiente");
        return carrito;
    }

    /**
     * Agrega un producto al carrito de compras de un usuario.
     * Si el producto ya está en el carrito, se incrementa su cantidad.
     * 
     * @param usuarioId el ID del usuario
     * @param productoId el ID del producto
     * @param cantidad la cantidad de producto a agregar
     * @return el carrito actualizado
     * @throws Exception si el usuario o el producto no existen
     */
    public PedidoDto agregarProducto(Long usuarioId, Long productoId, int cantidad) throws Exception {
        Optional<UsuarioDto> usuarioOpt = usuarioRepositorio.findById(usuarioId);
        if (!usuarioOpt.isPresent()) {
            throw new Exception("Usuario no encontrado");
        }
        UsuarioDto usuario = usuarioOpt.get();

        PedidoDto carrito = pedidoRepositorio.findByUsuarioAndEstado(usuario, "Pendiente");
        if (carrito == null) {
            carrito = new PedidoDto();
            carrito.setUsuario(usuario);
            carrito.setEstado("Pendiente");
            carrito.setFechaPedido(LocalDate.now());
            carrito.setMontoTotal(BigDecimal.ZERO);
            carrito = pedidoRepositorio.save(carrito);
        }

        Optional<ProductoDto> productoOpt = productoRepositorio.findById(productoId);
        if (!productoOpt.isPresent()) {
            throw new Exception("Producto no encontrado");
        }
        ProductoDto producto = productoOpt.get();

        DetallePedidoDto detalle = detallePedidoRepositorio.findByPedidoAndProducto(carrito, producto);
        if (detalle != null) {
            detalle.setCantidad(detalle.getCantidad() + cantidad);
            detallePedidoRepositorio.save(detalle);
        } else {
            detalle = new DetallePedidoDto();
            detalle.setPedido(carrito);
            detalle.setProducto(producto);
            detalle.setCantidad(cantidad);
            detalle.setPrecioUnitario(producto.getPrecio());
            detallePedidoRepositorio.save(detalle);
        }

        BigDecimal total = BigDecimal.ZERO;
        List<DetallePedidoDto> detalles = detallePedidoRepositorio.findByPedido(carrito);
        for (DetallePedidoDto d : detalles) {
            total = total.add(d.getPrecioUnitario().multiply(new BigDecimal(d.getCantidad())));
        }
        carrito.setMontoTotal(total);
        carrito = pedidoRepositorio.save(carrito);

        return carrito;
    }
    
    /**
     * Elimina un producto del carrito de compras de un usuario.
     * 
     * @param usuarioId el ID del usuario
     * @param detalleId el ID del detalle del producto a eliminar
     * @return el carrito actualizado
     * @throws Exception si el usuario o el detalle no existen o no pertenecen al carrito
     */
    public PedidoDto eliminarProducto(Long usuarioId, Long detalleId) throws Exception {
        Optional<UsuarioDto> usuarioOpt = usuarioRepositorio.findById(usuarioId);
        if (!usuarioOpt.isPresent()) {
            throw new Exception("Usuario no encontrado");
        }
        UsuarioDto usuario = usuarioOpt.get();

        PedidoDto carrito = pedidoRepositorio.findByUsuarioAndEstado(usuario, "Pendiente");
        if (carrito == null) {
            throw new Exception("No hay carrito activo para el usuario");
        }

        Optional<DetallePedidoDto> detalleOpt = detallePedidoRepositorio.findById(detalleId);
        if (!detalleOpt.isPresent()) {
            throw new Exception("Detalle no encontrado");
        }
        DetallePedidoDto detalle = detalleOpt.get();

        if (!detalle.getPedido().getIdPedido().equals(carrito.getIdPedido())) {
            throw new Exception("El detalle no pertenece al carrito activo");
        }

        detallePedidoRepositorio.delete(detalle);

        BigDecimal total = BigDecimal.ZERO;
        List<DetallePedidoDto> detalles = detallePedidoRepositorio.findByPedido(carrito);
        for (DetallePedidoDto d : detalles) {
            total = total.add(d.getPrecioUnitario().multiply(new BigDecimal(d.getCantidad())));
        }
        carrito.setMontoTotal(total);
        carrito = pedidoRepositorio.save(carrito);

        return carrito;
    }
}
