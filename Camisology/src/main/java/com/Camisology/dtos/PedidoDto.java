package com.Camisology.dtos;

import java.math.BigDecimal;
import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

/**
 * DTO para representar los pedidos realizados por los usuarios.
 * 
 * Incluye información sobre el usuario, método de pago, estado, fecha del pedido y monto total.
 * 
 * @author GPR
 * @date 06/03/2025
 */
@Entity
@Table(name = "pedidos")
public class PedidoDto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long idPedido;

    @ManyToOne
    private UsuarioDto usuario;

    @ManyToOne
    private MetodoPagoDto metodoPago;

    @Column
    private String estado;

    @Column
    private LocalDate fechaPedido;

    @Column
    private BigDecimal montoTotal;

    public Long getIdPedido() {
        return idPedido;
    }

    public void setIdPedido(Long idPedido) {
        this.idPedido = idPedido;
    }

    public UsuarioDto getUsuario() {
        return usuario;
    }

    public void setUsuario(UsuarioDto usuario) {
        this.usuario = usuario;
    }

    public MetodoPagoDto getMetodoPago() {
        return metodoPago;
    }

    public void setMetodoPago(MetodoPagoDto metodoPago) {
        this.metodoPago = metodoPago;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public LocalDate getFechaPedido() {
        return fechaPedido;
    }

    public void setFechaPedido(LocalDate fechaPedido) {
        this.fechaPedido = fechaPedido;
    }

    public BigDecimal getMontoTotal() {
        return montoTotal;
    }

    public void setMontoTotal(BigDecimal montoTotal) {
        this.montoTotal = montoTotal;
    }
}
