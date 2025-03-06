package com.Camisology.servicios;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.Camisology.dtos.UsuarioDto;
import com.Camisology.repositorios.UsuarioInterfaz;

import jakarta.transaction.Transactional;

/**
 * Servicio que proporciona funcionalidades relacionadas con los usuarios, 
 * como obtener, guardar, registrar, modificar, eliminar y validar credenciales de usuarios.
 * 
 * @autor GPR
 * @date 06/03/2025
 */
@Service
public class UsuarioFuncionalidades {

    @Autowired
    UsuarioInterfaz usuarioInterfaz;

    /**
     * Obtiene todos los usuarios.
     * 
     * @return una lista con todos los usuarios
     */
    public ArrayList<UsuarioDto> getUsuario() {
        return (ArrayList<UsuarioDto>) usuarioInterfaz.findAll();
    }

    /**
     * Guarda un nuevo usuario en la base de datos. La contraseña será encriptada antes de guardarse.
     * 
     * @param usuario el usuario a guardar
     * @return el usuario guardado
     */
    public UsuarioDto guardarUsuario(UsuarioDto usuario) {
        if (usuario.getPassword() == null || usuario.getPassword().isEmpty()) {
            throw new IllegalArgumentException("La contraseña no puede ser nula o vacía");
        }
        usuario.setPassword(encriptarContrasenya(usuario.getPassword()));

        return usuarioInterfaz.save(usuario);
    }

    /**
     * Registra un nuevo usuario con los parámetros proporcionados. La contraseña será encriptada antes de guardarse.
     * 
     * @param nombre el nombre del usuario
     * @param correo el correo del usuario
     * @param movil el móvil del usuario
     * @param password la contraseña del usuario
     * @return el usuario registrado
     */
    public UsuarioDto registrarNuevoUsuario(String nombre, String correo, String movil, String password) {
        UsuarioDto nuevoUsuario = new UsuarioDto();
        nuevoUsuario.setNombreCompleto(nombre);
        nuevoUsuario.setCorreo(correo);
        nuevoUsuario.setMovil(movil);
        nuevoUsuario.setPassword(password);  

        nuevoUsuario.setTipoUsuario("Usuario");

        nuevoUsuario.setPassword(encriptarContrasenya(password));

        return usuarioInterfaz.save(nuevoUsuario);
    }

    /**
     * Modifica un usuario existente. La contraseña y la foto serán actualizadas si se proporcionan nuevos valores.
     * 
     * @param usuario los nuevos datos del usuario
     * @param id el ID del usuario a modificar
     * @return el usuario modificado
     */
    public UsuarioDto modificarUsuario(UsuarioDto usuario, Long id) {
        UsuarioDto user = usuarioInterfaz.findById(id).get();

        user.setNombreCompleto(usuario.getNombreCompleto());
        user.setMovil(usuario.getMovil());
        user.setCorreo(usuario.getCorreo());
        user.setTipoUsuario(usuario.getTipoUsuario());
        if (usuario.getPassword() != null && !usuario.getPassword().trim().isEmpty()) {
            user.setPassword(encriptarContrasenya(usuario.getPassword()));
        }
        if (usuario.getFoto() != null && usuario.getFoto().length > 0) {
            user.setFoto(usuario.getFoto());
        }

        return usuarioInterfaz.save(user); 
    }

    /**
     * Elimina un usuario por su ID.
     * 
     * @param idUsuario el ID del usuario a eliminar
     * @return true si el usuario fue eliminado correctamente, false si no existía
     */
    @Transactional
    public Boolean eliminarUsuario(String idUsuario) {
        try {
            long id = Long.parseLong(idUsuario);

            UsuarioDto usuarioDto = usuarioInterfaz.findByidUsuario(id);

            if (usuarioDto == null) {
                System.out.println("El usuario indicado no existe");
                return false;
            }

            usuarioInterfaz.delete(usuarioDto);
            System.out.println("El usuario ha sido borrado con éxito");
            return true;

        } catch (NumberFormatException e) {
            System.out.println("Error: El ID proporcionado no es válido. " + e.getMessage());
            return false;
        } catch (Exception e) {
            System.out.println("Ocurrió un error inesperado: " + e.getMessage());
            return false;
        }
    }

    /**
     * Valida las credenciales de un usuario comprobando su correo y contraseña.
     * 
     * @param correo el correo del usuario
     * @param password la contraseña del usuario
     * @return true si las credenciales son correctas, false si son incorrectas
     */
    public boolean validarCredenciales(String correo, String password) {
        UsuarioDto usuario = usuarioInterfaz.findByCorreo(correo);

        if (usuario == null) {
            System.out.println("Usuario no encontrado.");
            return false; 
        }

        String passwordEncriptada = encriptarContrasenya(password);

        if (passwordEncriptada.equals(usuario.getPassword())) {
            System.out.println("Credenciales correctas.");
            return true; 
        } else {
            System.out.println("Contraseña incorrecta.");
            return false; 
        }
    }

    /**
     * Encripta una contraseña utilizando el algoritmo SHA-256.
     * 
     * @param contraseña la contraseña a encriptar
     * @return la contraseña encriptada en formato hexadecimal
     */
    public String encriptarContrasenya(String contraseña) {
        if (contraseña == null || contraseña.trim().isEmpty()) {
            throw new IllegalArgumentException("La contraseña no puede ser nula o vacía");
        }

        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(contraseña.getBytes());
            StringBuilder hexString = new StringBuilder();

            for (byte b : hash) {
                String hex = String.format("%02x", b); 
                hexString.append(hex);
            }

            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
}
