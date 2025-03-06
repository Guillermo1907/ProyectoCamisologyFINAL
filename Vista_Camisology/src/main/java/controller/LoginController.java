package controller;

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import servicios.LoginFuncionalidades;
import org.json.JSONObject;

/**
 * Controlador principal para gestionar las operaciones de login y logout de la aplicación.
 * 
 * @author GPR
 * @date 06/03/2025
 */
@WebServlet("/login")
public class LoginController extends HttpServlet {

    private LoginFuncionalidades loginFuncionalidade;

    /**
     * Inicializa el servlet y establece el servicio de funcionalidades de login.
     * 
     * @author GPR
     * @date 06/03/2025
     */
    @Override
    public void init() throws ServletException {
        this.loginFuncionalidade = new LoginFuncionalidades();
    }

    /**
     * Maneja las solicitudes POST para login y logout.
     * 
     * @param request Objeto HttpServletRequest que contiene la solicitud HTTP.
     * @param response Objeto HttpServletResponse que contiene la respuesta HTTP.
     * @throws ServletException Si ocurre un error durante el procesamiento de la solicitud.
     * @throws IOException Si ocurre un error al escribir en la respuesta.
     * 
     * @author GPR
     * @date 06/03/2025
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");

        if ("logout".equalsIgnoreCase(action)) {
            logout(request, response);
        } else {
            login(request, response);
        }
    }

    /**
     * Procesa la solicitud de login.
     * 
     * @param request Objeto HttpServletRequest que contiene la solicitud HTTP.
     * @param response Objeto HttpServletResponse que contiene la respuesta HTTP.
     * @throws IOException Si ocurre un error al escribir la respuesta.
     * 
     * @author GPR
     * @date 06/03/2025
     */
    private void login(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String correo = request.getParameter("correo");
        String password = request.getParameter("password");

        JSONObject userResponse = loginFuncionalidade.login(correo, password);

        if (userResponse != null) {
            JSONObject usuarioObject;
            Long usuarioId = 0L;
            if (userResponse.has("usuario")) {
                usuarioObject = userResponse.getJSONObject("usuario");
                usuarioId = usuarioObject.optLong("idUsuario", 0);
            } else {
                usuarioObject = userResponse;
                usuarioId = userResponse.optLong("id", 0);
            }

            if (usuarioId == 0) {
                System.out.println("errorID");
                response.sendRedirect(request.getContextPath() + "/index.jsp?error=ID_INVALIDO");
                return;
            }

            String rol = usuarioObject.optString("rol", "Usuario");
            String correoResp = usuarioObject.optString("email", correo);
            String fotoBase64 = usuarioObject.optString("foto", null);

            HttpSession session = request.getSession();
            session.setAttribute("usuario", correoResp);
            session.setAttribute("rol", rol);
            session.setAttribute("foto", fotoBase64);
            session.setAttribute("usuarioId", usuarioId);

            if ("Administrador".equalsIgnoreCase(rol)) {
                response.sendRedirect(request.getContextPath() + "/panel-administracion/panel-ad.jsp");
            } else {
                response.sendRedirect(request.getContextPath() + "/index.jsp");
            }
        } else {
            response.sendRedirect(request.getContextPath() + "/index.jsp?error=true");
        }
    }

    /**
     * Procesa la solicitud de logout invalidando la sesión actual.
     * 
     * @param request Objeto HttpServletRequest que contiene la solicitud HTTP.
     * @param response Objeto HttpServletResponse que contiene la respuesta HTTP.
     * @throws IOException Si ocurre un error al redirigir la respuesta.
     * 
     * @author GPR
     * @date 06/03/2025
     */
    private void logout(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        response.sendRedirect(request.getContextPath() + "/index.jsp");
    }
}
