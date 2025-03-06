package controller;

import java.io.IOException;
import java.io.InputStream;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.Part;
import javax.mail.MessagingException;
import dtos.LoginDto;
import servicios.RegistroFuncionalidades;
import servicios.VerificacionServicio;

/**
 * Controlador para gestionar el registro de nuevos usuarios.
 * Permite la creación de cuentas, incluyendo la verificación de correo electrónico.
 * 
 * @author GPR
 * @date 06/03/2025
 */
@WebServlet("/registro")
@MultipartConfig
public class RegistroController extends HttpServlet {

    private RegistroFuncionalidades registroFuncionalidad;
    private VerificacionServicio verificacionServicio;

    /**
     * Inicializa el controlador y configura los servicios necesarios para el registro
     * de usuarios.
     * 
     * @throws ServletException Si ocurre un error al inicializar el controlador.
     */
    @Override
    public void init() throws ServletException {
        super.init();
        this.registroFuncionalidad = new RegistroFuncionalidades();
        this.verificacionServicio = new VerificacionServicio();
    }

    /**
     * Maneja las solicitudes POST para registrar a un nuevo usuario.
     * Recibe los datos del formulario de registro y los procesa.
     * 
     * @param request  Objeto HttpServletRequest que contiene los parámetros de la solicitud.
     * @param response Objeto HttpServletResponse que se utiliza para enviar la respuesta.
     * @throws ServletException Si ocurre un error al procesar la solicitud.
     * @throws IOException Si ocurre un error al leer los datos del formulario o escribir la respuesta.
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Recuperar los datos del formulario
        String nombre = request.getParameter("nombre");
        String correo = request.getParameter("correo-registro");
        String movil = request.getParameter("movil");
        String password = request.getParameter("password");

        // Procesar la imagen de perfil
        Part fotoPart = request.getPart("foto"); // Foto de perfil
        byte[] fotoBytes = null;
        if (fotoPart != null && fotoPart.getSize() > 0) {
            try (InputStream is = fotoPart.getInputStream()) {
                fotoBytes = is.readAllBytes();
            }
        } else {
            fotoBytes = getDefaultImagen(); // Imagen por defecto si no se proporciona una foto
        }

        // Verificar que todos los campos requeridos estén completos
        if (nombre == null || correo == null || movil == null || password == null ||
            nombre.isEmpty() || correo.isEmpty() || movil.isEmpty() || password.isEmpty()) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Datos inválidos");
            return;
        }

        // Generar y enviar código de verificación al correo
        int codigoVerificacion = verificacionServicio.generarCodigoVerificacion();
        try {
            verificacionServicio.enviarCorreoVerificacion(correo, codigoVerificacion);
        } catch (MessagingException e) {
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + "/index.jsp?error=correo");
            return;
        }

        // Guardar el código de verificación en la base de datos
        verificacionServicio.guardarCodigo(correo, codigoVerificacion);

        // Crear el objeto LoginDto para almacenar la información del nuevo usuario
        LoginDto nuevoUsuario = new LoginDto();
        nuevoUsuario.setNombreCompleto(nombre);
        nuevoUsuario.setCorreo(correo);
        nuevoUsuario.setMovil(movil);
        nuevoUsuario.setPassword(password);
        nuevoUsuario.setTipoUsuario("Usuario");
        nuevoUsuario.setFoto(fotoBytes);

        // Guardar la información del usuario en la sesión para continuar el proceso de verificación
        HttpSession session = request.getSession();
        session.setAttribute("registroPendiente", nuevoUsuario);
        session.setAttribute("correo", correo);

        // Redirigir a la página de verificación para completar el registro
        response.sendRedirect(request.getContextPath() + "/verificacion/verificar.html?correo=" + correo);
    }

    /**
     * Método para obtener una imagen por defecto en caso de que el usuario no haya
     * subido una foto de perfil.
     * 
     * @return Un arreglo de bytes con la imagen por defecto.
     * @throws ServletException Si ocurre un error al cargar la imagen.
     */
    private byte[] getDefaultImagen() throws ServletException {
        try (InputStream is = getServletContext().getResourceAsStream("/img/header/iconoperfil.png")) {
            if (is == null) {
                throw new ServletException("Imagen por defecto no encontrada");
            }
            return is.readAllBytes();
        } catch (IOException e) {
            throw new RuntimeException("Error al cargar la imagen por defecto", e);
        }
    }
}
