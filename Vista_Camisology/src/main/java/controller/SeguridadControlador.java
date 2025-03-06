package controller;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Filtro de seguridad para gestionar el acceso a las rutas de la aplicación.
 * Permite el acceso a rutas públicas y restringe el acceso a rutas protegidas,
 * redirigiendo a los usuarios sin los permisos adecuados.
 * 
 * @author GPR
 * @date 06/03/2025
 */
@WebFilter("/*")
public class SeguridadControlador implements Filter {

    /**
     * Inicializa el filtro.
     * 
     * @param filterConfig Configuración del filtro.
     * @throws ServletException Si ocurre un error durante la inicialización.
     * 
     * @author GPR
     * @date 06/03/2025
     */
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    /**
     * Filtra cada solicitud entrante para verificar los permisos de acceso según la URI solicitada.
     * 
     * @param request Objeto ServletRequest que contiene la solicitud.
     * @param response Objeto ServletResponse que contiene la respuesta.
     * @param chain Cadena de filtros que permite continuar con la ejecución si se cumplen las condiciones.
     * @throws IOException Si ocurre un error de entrada/salida.
     * @throws ServletException Si ocurre un error durante el procesamiento de la solicitud.
     * 
     * @author GPR
     * @date 06/03/2025
     */
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
      throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;
        String uri = req.getRequestURI();

        // Rutas públicas que no requieren autenticación
        boolean rutasPublicas = uri.endsWith("index.jsp")
                || uri.endsWith("bundesliga.jsp")
                || uri.endsWith("laliga.jsp")
                || uri.endsWith("premierleague.jsp")
                || uri.endsWith("selecciones.jsp")
                || uri.endsWith("serie.jsp")
                || uri.endsWith("pasarela.jsp")
                || uri.endsWith("verificar.html")
                || uri.endsWith(".js")
                || uri.endsWith(".css")
                || uri.endsWith(".jpg")
                || uri.endsWith(".png");

        if (rutasPublicas) {
            chain.doFilter(request, response);
            return;
        }

        // Verifica si la solicitud es para el panel de administración
        boolean rutaAdmin = uri.contains("/panel-administracion/panel-ad.jsp");

        if (rutaAdmin) {
            HttpSession session = req.getSession(false);
            if (session == null || session.getAttribute("rol") == null ||
                    !session.getAttribute("rol").equals("Administrador")) {
                res.sendRedirect(req.getContextPath() + "/index.jsp");
                return;
            }
        }

        chain.doFilter(request, response);
    }
}
