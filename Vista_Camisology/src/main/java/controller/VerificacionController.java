package controller;

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import servicios.VerificacionServicio;
import servicios.RegistroFuncionalidades;
import dtos.LoginDto;

/**
 * Controlador encargado de gestionar la verificación del código de registro enviado al usuario.
 * 
 * @author GPR
 * @date 06/03/2025
 */
@WebServlet("/verificar")
public class VerificacionController extends HttpServlet {

    private VerificacionServicio verificacionServicio;
    private RegistroFuncionalidades registroFuncionalidad;

    /**
     * Inicializa el controlador y configura los servicios necesarios para la verificación y el registro de usuarios.
     * 
     * @throws ServletException Si ocurre un error durante la inicialización.
     * 
     * @author GPR
     * @date 06/03/2025
     */
    @Override
    public void init() throws ServletException {
        super.init();
        this.verificacionServicio = new VerificacionServicio();
        this.registroFuncionalidad = new RegistroFuncionalidades();
    }

    /**
     * Maneja las solicitudes POST para verificar el código de registro ingresado por el usuario.
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
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String codigoIngresado = request.getParameter("codigo");
        String correo = request.getParameter("correo");

        HttpSession session = request.getSession();
        String correoEnSesion = (String) session.getAttribute("correo");
        
        if (correoEnSesion == null || !correoEnSesion.equals(correo)) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Acceso no autorizado.");
            return;
        }
        
        try {
            boolean esValido = verificacionServicio.verificarCodigo(correo, Integer.parseInt(codigoIngresado));
            if (esValido) {
                LoginDto nuevoUsuario = (LoginDto) session.getAttribute("registroPendiente");
                if (nuevoUsuario != null) {
                    int codigoRespuesta = registroFuncionalidad.crearUsuario(nuevoUsuario);
                    if (codigoRespuesta == 200 || codigoRespuesta == 201) {
                        session.removeAttribute("registroPendiente"); 
                        response.sendRedirect(request.getContextPath() + "/verificacion/verificar.html?verificado=true&correo=" + correo);
                        return;
                    } else {
                        response.sendRedirect(request.getContextPath() + "/verificacion/verificar.html?error=true&correo=" + correo);
                        return;
                    }
                } else {
                    response.sendRedirect(request.getContextPath() + "/verificacion/verificar.html?error=sinRegistro&correo=" + correo);
                    return;
                }
            } else {
                response.sendRedirect(request.getContextPath() + "/verificacion/verificar.html?error=true&correo=" + correo);
            }
        } catch (Exception e) {
            response.sendRedirect(request.getContextPath() + "/verificacion/verificar.html?error=exception&correo=" + correo);
            e.printStackTrace();
        }
    }
}
