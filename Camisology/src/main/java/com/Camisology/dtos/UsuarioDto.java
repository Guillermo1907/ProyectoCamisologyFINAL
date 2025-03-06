package com.Camisology.dtos;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

/**
 * DTO para representar a los usuarios registrados en el sistema.
 * 
 * Incluye información personal como nombre completo, móvil, correo, tipo de usuario, contraseña, y una foto opcional.
 * También se relaciona con pedidos realizados por el usuario.
 * 
 * @author GPR
 * @date 06/03/2025
 */
@Entity
@Table(name = "usuarios")
public class UsuarioDto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long idUsuario;
    
    @Column
    private String nombreCompleto;
    
    @Column
    private String movil;
    
    @Column
    private String correo;
    
    @Column
    private String tipoUsuario;
    
    @Column
    private String password;
    
    @Column 
    private byte[] foto;

    @ManyToOne
    private PedidoDto pedido;

    public Long getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Long idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getNombreCompleto() {
        return nombreCompleto;
    }

    public void setNombreCompleto(String nombreCompleto) {
        this.nombreCompleto = nombreCompleto;
    }

    public String getMovil() {
        return movil;
    }

    public void setMovil(String movil) {
        this.movil = movil;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getTipoUsuario() {
        return tipoUsuario;
    }

    public void setTipoUsuario(String tipoUsuario) {
        this.tipoUsuario = tipoUsuario;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public byte[] getFoto() {
        return foto;
    }

    public void setFoto(byte[] foto) {
        this.foto = foto;
    }

    public PedidoDto getPedido() {
        return pedido;
    }

    public void setPedido(PedidoDto pedido) {
        this.pedido = pedido;
    }
}
