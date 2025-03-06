package controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import servicios.UsuarioFuncionalidades;
import org.json.JSONObject;

/**
 * Controlador para gestionar la actualización y eliminación de usuarios.
 * 
 * @author GPR
 * @date 06/03/2025
 */
@WebServlet("/usuario")
public class UsuarioController extends HttpServlet {

    private UsuarioFuncionalidades usuarioFuncionalidade;

    /**
     * Inicializa el controlador y configura el servicio de funcionalidades de usuarios.
     * 
     * @throws ServletException Si ocurre un error durante la inicialización.
     * 
     * @author GPR
     * @date 06/03/2025
     */
    @Override
    public void init() throws ServletException {
        this.usuarioFuncionalidade = new UsuarioFuncionalidades();
    }

    /**
     * Maneja las solicitudes PUT para actualizar los datos de un usuario.
     * Recibe el ID del usuario y los nuevos datos en formato JSON.
     * 
     * @param request  Objeto HttpServletRequest que contiene la solicitud.
     * @param response Objeto HttpServletResponse que contiene la respuesta.
     * @throws ServletException Si ocurre un error durante el procesamiento de la solicitud.
     * @throws IOException Si ocurre un error al leer o escribir datos.
     * 
     * @author GPR
     * @date 06/03/2025
     */
    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String idStr = request.getParameter("id");
        if (idStr == null || idStr.trim().isEmpty()) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "ID del usuario es requerido");
            return;
        }
        Long idUsuario;
        try {
            idUsuario = Long.parseLong(idStr);
        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "ID inválido");
            return;
        }

        StringBuilder sb = new StringBuilder();
        String linea;
        try (BufferedReader reader = request.getReader()) {
            while ((linea = reader.readLine()) != null) {
                sb.append(linea);
            }
        }
        JSONObject usuarioJson = new JSONObject(sb.toString());

        JSONObject usuarioActualizado = usuarioFuncionalidade.modificarUsuario(idUsuario, usuarioJson);
        if (usuarioActualizado != null) {
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            try (PrintWriter out = response.getWriter()) {
                out.print(usuarioActualizado.toString());
            }
        } else {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error al actualizar el usuario");
        }
    }

    /**
     * Maneja las solicitudes DELETE para eliminar un usuario.
     * Recibe el ID del usuario a eliminar y verifica que no se intente eliminar al usuario en sesión.
     * 
     * @param request  Objeto HttpServletRequest que contiene la solicitud.
     * @param response Objeto HttpServletResponse que contiene la respuesta.
     * @throws ServletException Si ocurre un error durante el procesamiento de la solicitud.
     * @throws IOException Si ocurre un error al leer o escribir datos.
     * 
     * @author GPR
     * @date 06/03/2025
     */
    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String idStr = request.getParameter("id");
        if (idStr == null || idStr.trim().isEmpty()) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "ID del usuario es requerido");
            return;
        }
        Long idUsuario;
        try {
            idUsuario = Long.parseLong(idStr);
        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "ID inválido");
            return;
        }
        HttpSession session = request.getSession(false);
        Long idUsuarioSesion = (Long) session.getAttribute("idUsuario");
        if (idUsuarioSesion != null && idUsuarioSesion.equals(idUsuario)) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "No se puede eliminar a sí mismo");
            return;
        }

        boolean resultado = usuarioFuncionalidade.eliminarUsuario(idStr);
        if (resultado) {
            response.setStatus(HttpServletResponse.SC_OK);
        } else {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error al eliminar el usuario");
        }
    }
}
