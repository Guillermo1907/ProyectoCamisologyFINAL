package com.Camisology.servicios;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.Camisology.dtos.ProductoDto;
import com.Camisology.repositorios.ProductoInterfaz;

import jakarta.transaction.Transactional;

/**
 * Servicio que proporciona las funcionalidades relacionadas con los productos,
 * como obtener, guardar, modificar y eliminar productos.
 * 
 * @author GPR
 * @date 06/03/2025
 */
@Service
public class ProductoFuncionalidades {

    @Autowired
    ProductoInterfaz productoInterfaz;
    
    /**
     * Obtiene todos los productos disponibles.
     * 
     * @return una lista con todos los productos
     */
    public ArrayList<ProductoDto> getProducto() {
        return (ArrayList<ProductoDto>) productoInterfaz.findAll();
    }
    
    /**
     * Guarda un nuevo producto en la base de datos.
     * 
     * @param producto el producto a guardar
     * @return el producto guardado
     */
    public ProductoDto guardarProducto(ProductoDto producto) {
        return productoInterfaz.save(producto);
    }
    
    /**
     * Modifica un producto existente.
     * 
     * @param producto el producto con los nuevos datos
     * @param id el ID del producto a modificar
     * @return el producto modificado o null si el producto no existe
     */
    public ProductoDto modificarProducto(ProductoDto producto, Long id) {
        ProductoDto prod = productoInterfaz.findById(id).orElse(null);
        if (prod != null) {
            prod.setNombre(producto.getNombre());
            prod.setPrecio(producto.getPrecio());
            prod.setCategoria(producto.getCategoria());
            if (producto.getFoto() != null && producto.getFoto().length > 0) {
                prod.setFoto(producto.getFoto());
            }
            return productoInterfaz.save(prod);
        }
        return null;
    }
    
    /**
     * Elimina un producto de la base de datos.
     * 
     * @param idProducto el ID del producto a eliminar
     * @return true si el producto fue eliminado con éxito, false si no existe
     */
    @Transactional
    public Boolean eliminarProducto(String idProducto) {
        try {
            long id = Long.parseLong(idProducto);
            ProductoDto prod = productoInterfaz.findById(id).orElse(null);
            if (prod == null) {
                System.out.println("El producto indicado no existe");
                return false;
            }
            productoInterfaz.delete(prod);
            System.out.println("El producto ha sido borrado con éxito");
            return true;
        } catch (NumberFormatException e) {
            System.out.println("Error: El ID proporcionado no es válido. " + e.getMessage());
            return false;
        } catch (Exception e) {
            System.out.println("Ocurrió un error inesperado: " + e.getMessage());
            return false;
        }
    }
}
