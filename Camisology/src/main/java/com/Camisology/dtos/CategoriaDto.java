package com.Camisology.dtos;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * DTO para representar las categorías del sistema.
 * 
 * Contiene los datos básicos de una categoría, como su ID y nombre.
 * 
 * @author GPR
 * @date 06/03/2025
 */
@Entity
@Table(name = "categorias")
public class CategoriaDto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long idCategoria;

    @Column
    private String nombre;

    public Long getIdCategoria() {
        return idCategoria;
    }

    public void setIdCategoria(Long idCategoria) {
        this.idCategoria = idCategoria;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
}
