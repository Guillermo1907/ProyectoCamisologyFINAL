package controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import servicios.ProductoFuncionalidades;

import org.json.JSONObject;

/**
 * Controlador para gestionar las solicitudes relacionadas con productos.
 * 
 * @author GPR
 * @date 06/03/2025
 */
@WebServlet("/producto")
public class ProductoController extends HttpServlet {

    private ProductoFuncionalidades productoFuncionalidade;

    /**
     * Inicializa el controlador y configura la funcionalidad de productos.
     * 
     * @throws ServletException Si ocurre un error al inicializar el controlador.
     * 
     * @author GPR
     * @date 06/03/2025
     */
    @Override
    public void init() throws ServletException {
        this.productoFuncionalidade = new ProductoFuncionalidades();
    }

    /**
     * Maneja las solicitudes GET para obtener la lista de productos.
     * 
     * @param request  Objeto HttpServletRequest que contiene la solicitud HTTP.
     * @param response Objeto HttpServletResponse que contiene la respuesta HTTP.
     * @throws ServletException Si ocurre un error al procesar la solicitud.
     * @throws IOException Si ocurre un error al escribir la respuesta.
     * 
     * @author GPR
     * @date 06/03/2025
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String productosJson = productoFuncionalidade.getProductos();
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        try (PrintWriter out = response.getWriter()) {
            out.print(productosJson);
        }
    }

    /**
     * Maneja las solicitudes POST para crear un nuevo producto.
     * 
     * @param request  Objeto HttpServletRequest que contiene la solicitud HTTP.
     * @param response Objeto HttpServletResponse que contiene la respuesta HTTP.
     * @throws ServletException Si ocurre un error al procesar la solicitud.
     * @throws IOException Si ocurre un error al leer los datos o escribir la respuesta.
     * 
     * @author GPR
     * @date 06/03/2025
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        StringBuilder sb = new StringBuilder();
        String linea;
        try (BufferedReader reader = request.getReader()) {
            while ((linea = reader.readLine()) != null) {
                sb.append(linea);
            }
        }
        JSONObject productoJson = new JSONObject(sb.toString());
        JSONObject savedProducto = productoFuncionalidade.guardarProducto(productoJson);
        if (savedProducto != null) {
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            try (PrintWriter out = response.getWriter()) {
                out.print(savedProducto.toString());
            }
        } else {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error al guardar producto");
        }
    }

    /**
     * Maneja las solicitudes PUT para modificar un producto existente.
     * 
     * @param request  Objeto HttpServletRequest que contiene la solicitud HTTP.
     * @param response Objeto HttpServletResponse que contiene la respuesta HTTP.
     * @throws ServletException Si ocurre un error al procesar la solicitud.
     * @throws IOException Si ocurre un error al leer los datos o escribir la respuesta.
     * 
     * @author GPR
     * @date 06/03/2025
     */
    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String idStr = request.getParameter("id");
        if (idStr == null || idStr.trim().isEmpty()) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "ID de producto requerido");
            return;
        }
        Long idProducto = Long.parseLong(idStr);

        StringBuilder sb = new StringBuilder();
        String linea;
        try (BufferedReader reader = request.getReader()) {
            while ((linea = reader.readLine()) != null) {
                sb.append(linea);
            }
        }
        JSONObject productJson = new JSONObject(sb.toString());
        JSONObject updatedProduct = productoFuncionalidade.modificarProducto(idProducto, productJson);
        if (updatedProduct != null) {
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            try (PrintWriter out = response.getWriter()) {
                out.print(updatedProduct.toString());
            }
        } else {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error al modificar producto");
        }
    }

    /**
     * Maneja las solicitudes DELETE para eliminar un producto.
     * 
     * @param request  Objeto HttpServletRequest que contiene la solicitud HTTP.
     * @param response Objeto HttpServletResponse que contiene la respuesta HTTP.
     * @throws ServletException Si ocurre un error al procesar la solicitud.
     * @throws IOException Si ocurre un error al escribir la respuesta.
     * 
     * @author GPR
     * @date 06/03/2025
     */
    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String idStr = request.getParameter("id");
        if (idStr == null || idStr.trim().isEmpty()) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "ID de producto requerido");
            return;
        }
        boolean resultado = productoFuncionalidade.eliminarProducto(idStr);
        if (resultado) {
            response.setStatus(HttpServletResponse.SC_OK);
        } else {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error al eliminar producto");
        }
    }
}
