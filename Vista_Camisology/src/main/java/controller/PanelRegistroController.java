package controller;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import com.google.gson.Gson;

import dtos.LoginDto;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;
import servicios.PanelRegistroFuncionalidades;

/**
 * Controlador para gestionar el registro de usuarios desde el panel de administración.
 * 
 * @author GPR
 * @date 06/03/2025
 */
@WebServlet("/registroPanel")
@MultipartConfig
public class PanelRegistroController extends HttpServlet {

    private PanelRegistroFuncionalidades panelRegistroFuncionalidad;

    /**
     * Inicializa el servlet y establece la funcionalidad para el registro en el panel.
     * 
     * @author GPR
     * @date 06/03/2025
     */
    @Override
    public void init() throws ServletException {
        super.init();
        this.panelRegistroFuncionalidad = new PanelRegistroFuncionalidades();
    }

    /**
     * Maneja las solicitudes POST para registrar un nuevo usuario en el panel de administración.
     * 
     * @param request Objeto HttpServletRequest que contiene la solicitud HTTP.
     * @param response Objeto HttpServletResponse que contiene la respuesta HTTP.
     * @throws ServletException Si ocurre un error durante el procesamiento de la solicitud.
     * @throws IOException Si ocurre un error al escribir la respuesta.
     * 
     * @author GPR
     * @date 06/03/2025
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String nombre = request.getParameter("nombre-completo");
        String movil = request.getParameter("movil");
        String correo = request.getParameter("correo");
        String password = request.getParameter("password");
        String admin = request.getParameter("admin");
        String tipoUsuario = (admin != null && admin.equals("on")) ? "Administrador" : "Usuario";

        Part fotoPart = request.getPart("foto");
        byte[] fotoBytes = null;
        if (fotoPart != null && fotoPart.getSize() > 0) {
            try (InputStream is = fotoPart.getInputStream()) {
                fotoBytes = is.readAllBytes();
            }
        } else {
            fotoBytes = getDefaultImagen();
        }

        if (nombre == null || movil == null || correo == null || password == null ||
            nombre.trim().isEmpty() || movil.trim().isEmpty() ||
            correo.trim().isEmpty() || password.trim().isEmpty()) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Datos inválidos");
            return;
        }

        try {
            int codigoRespuesta = panelRegistroFuncionalidad.enviarDatosPanelRegistro(
                    nombre, correo, movil, password, fotoBytes, tipoUsuario);

            if (codigoRespuesta == 200 || codigoRespuesta == 201) {
                response.sendRedirect(request.getContextPath() + "/panel-administracion/panel-ad.jsp?registro=exitoso");
            } else {
                response.sendRedirect(request.getContextPath() + "/panel-administracion/panel-ad.jsp?error=true");
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + "/panel-administracion/panel-ad.jsp?error=exception");
        }
    }
    
    /**
     * Carga una imagen predeterminada si no se proporciona una imagen de perfil.
     * 
     * @return Un arreglo de bytes con la imagen por defecto.
     * @throws ServletException Si ocurre un error al cargar la imagen por defecto.
     * 
     * @author GPR
     * @date 06/03/2025
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
    
    /**
     * Maneja las solicitudes GET para obtener los usuarios registrados.
     * 
     * @param request Objeto HttpServletRequest que contiene la solicitud HTTP.
     * @param response Objeto HttpServletResponse que contiene la respuesta HTTP.
     * @throws ServletException Si ocurre un error durante el procesamiento de la solicitud.
     * @throws IOException Si ocurre un error al escribir la respuesta.
     * 
     * @author GPR
     * @date 06/03/2025
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        try {
            List<LoginDto> usuarios = panelRegistroFuncionalidad.obtenerUsuarios();

            Gson gson = new Gson();
            String jsonUsuarios = gson.toJson(usuarios);

            response.getWriter().write(jsonUsuarios);
        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error al cargar usuarios");
        }
    }
}
