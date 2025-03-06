package controller;

import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import servicios.CarritoFuncionalidades;

/**
 * Controlador principal para gestionar las operaciones del carrito de compras.
 * 
 * @author GPR
 * @date 06/03/2025
 */
@WebServlet("/carrito")
public class CarritoController extends HttpServlet {

    private CarritoFuncionalidades carritoServicio;

    /**
     * Inicializa el servlet y establece el servicio de funcionalidades del carrito.
     * 
     * @author GPR
     * @date 06/03/2025
     */
    @Override
    public void init() throws ServletException {
        super.init();
        this.carritoServicio = new CarritoFuncionalidades();
    }

    /**
     * Maneja las solicitudes GET para obtener el carrito activo del usuario.
     * 
     * @param request  Objeto HttpServletRequest que contiene la solicitud HTTP.
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
        HttpSession session = request.getSession();
        Long usuarioId = (Long) session.getAttribute("usuarioId");
        response.setContentType("application/json;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            if (usuarioId == null) {
                out.write("{\"error\":\"Debes iniciar sesión para ver el carrito.\"}");
                return;
            }
            String carritoJson = carritoServicio.obtenerCarrito(usuarioId);
            if (carritoJson == null || carritoJson.isEmpty()) {
                out.write("{\"error\":\"No se encontró carrito activo.\"}");
            } else {
                out.write(carritoJson);
            }
        }
    }

    /**
     * Maneja las solicitudes POST para agregar o eliminar productos del carrito del usuario.
     * 
     * @param request  Objeto HttpServletRequest que contiene la solicitud HTTP.
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
        HttpSession session = request.getSession();
        Long usuarioId = (Long) session.getAttribute("usuarioId");
        response.setContentType("application/json;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            if (usuarioId == null) {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "{\"error\":\"Debes iniciar sesión\"}");
                return;
            }
            String accion = request.getParameter("accion");
            String result = "";
            if ("agregar".equals(accion)) {
                try {
                    Long productoId = Long.parseLong(request.getParameter("productoId"));
                    int cantidad = Integer.parseInt(request.getParameter("cantidad"));
                    result = carritoServicio.agregarProducto(usuarioId, productoId, cantidad);
                } catch (NumberFormatException e) {
                    result = "{\"error\":\"Error en los parámetros para agregar.\"}";
                }
            } else if ("eliminar".equals(accion)) {
                try {
                    Long detalleId = Long.parseLong(request.getParameter("detalleId"));
                    result = carritoServicio.eliminarProducto(usuarioId, detalleId);
                } catch (NumberFormatException e) {
                    result = "{\"error\":\"Error en los parámetros para eliminar.\"}";
                }
            } else {
                result = "{\"error\":\"Acción no válida.\"}";
            }
            out.write(result);
        }
    }
}
