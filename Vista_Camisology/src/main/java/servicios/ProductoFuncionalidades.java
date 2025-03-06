package servicios;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import org.json.JSONObject;

/**
 * Servicio encargado de gestionar las funcionalidades relacionadas con los productos.
 * Proporciona métodos para obtener, guardar, modificar y eliminar productos en el sistema.
 * 
 * @author GPR
 * @date 06/03/2025
 */
public class ProductoFuncionalidades {

    /**
     * Obtiene la lista de productos disponibles desde el servidor.
     * 
     * @return Una cadena {@link String} que contiene la respuesta del servidor con la lista de productos en formato JSON,
     *         o null si ocurre un error al obtener los productos.
     * @throws Exception Si ocurre un error al realizar la solicitud o procesar la respuesta.
     * 
     * @author GPR
     * @date 06/03/2025
     */
    public String getProductos() {
        try {
            URL url = new URL("http://localhost:4925/api/mostrarProducto");
            HttpURLConnection conexion = (HttpURLConnection) url.openConnection();
            conexion.setRequestMethod("GET");
            conexion.setRequestProperty("Accept", "application/json");

            int responseCode = conexion.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                StringBuilder responseStr = new StringBuilder();
                try (BufferedReader br = new BufferedReader(new InputStreamReader(conexion.getInputStream(), StandardCharsets.UTF_8))) {
                    String linea;
                    while ((linea = br.readLine()) != null) {
                        responseStr.append(linea);
                    }
                }
                return responseStr.toString();
            } else {
                System.err.println("Error en GET productos. Código: " + responseCode);
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Guarda un nuevo producto en el sistema enviando una solicitud HTTP POST al servidor.
     * 
     * @param productJson Un objeto {@link JSONObject} que contiene los datos del producto a guardar.
     * @return Un objeto {@link JSONObject} con la respuesta del servidor, que incluye los datos del producto guardado,
     *         o null si ocurre un error al guardar el producto.
     * @throws Exception Si ocurre un error al realizar la solicitud o procesar la respuesta.
     * 
     * @author GPR
     * @date 06/03/2025
     */
    public JSONObject guardarProducto(JSONObject productJson) {
        try {
            URL url = new URL("http://localhost:4925/api/guardarProducto");
            HttpURLConnection conexion = (HttpURLConnection) url.openConnection();
            conexion.setRequestMethod("POST");
            conexion.setRequestProperty("Content-Type", "application/json");
            conexion.setDoOutput(true);

            try (OutputStream os = conexion.getOutputStream()) {
                byte[] input = productJson.toString().getBytes(StandardCharsets.UTF_8);
                os.write(input, 0, input.length);
            }

            int responseCode = conexion.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK || responseCode == HttpURLConnection.HTTP_CREATED) {
                StringBuilder responseStr = new StringBuilder();
                try (BufferedReader br = new BufferedReader(new InputStreamReader(conexion.getInputStream(), StandardCharsets.UTF_8))) {
                    String linea;
                    while ((linea = br.readLine()) != null) {
                        responseStr.append(linea);
                    }
                }
                return new JSONObject(responseStr.toString());
            } else {
                System.err.println("Error en POST guardar producto. Código: " + responseCode);
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Modifica los datos de un producto existente en el sistema enviando una solicitud HTTP PUT al servidor.
     * 
     * @param idProducto El identificador único del producto que se desea modificar.
     * @param productJson Un objeto {@link JSONObject} que contiene los datos modificados del producto.
     * @return Un objeto {@link JSONObject} con la respuesta del servidor, que incluye los datos del producto modificado,
     *         o null si ocurre un error al modificar el producto.
     * @throws Exception Si ocurre un error al realizar la solicitud o procesar la respuesta.
     * 
     * @author GPR
     * @date 06/03/2025
     */
    public JSONObject modificarProducto(Long idProducto, JSONObject productJson) {
        try {
            URL url = new URL("http://localhost:4925/api/putProducto/" + idProducto);
            HttpURLConnection conexion = (HttpURLConnection) url.openConnection();
            conexion.setRequestMethod("PUT");
            conexion.setRequestProperty("Content-Type", "application/json");
            conexion.setDoOutput(true);

            try (OutputStream os = conexion.getOutputStream()) {
                byte[] input = productJson.toString().getBytes(StandardCharsets.UTF_8);
                os.write(input, 0, input.length);
            }

            int responseCode = conexion.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                StringBuilder responseStr = new StringBuilder();
                try (BufferedReader br = new BufferedReader(new InputStreamReader(conexion.getInputStream(), StandardCharsets.UTF_8))) {
                    String linea;
                    while ((linea = br.readLine()) != null) {
                        responseStr.append(linea);
                    }
                }
                return new JSONObject(responseStr.toString());
            } else {
                System.err.println("Error en PUT modificar producto. Código: " + responseCode);
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Elimina un producto del sistema enviando una solicitud HTTP DELETE al servidor.
     * 
     * @param idProducto El identificador único del producto que se desea eliminar.
     * @return {@code true} si el producto se eliminó correctamente, o {@code false} si ocurrió un error.
     * @throws Exception Si ocurre un error al realizar la solicitud o procesar la respuesta.
     * 
     * @author GPR
     * @date 06/03/2025
     */
    public boolean eliminarProducto(String idProducto) {
        try {
            URL url = new URL("http://localhost:4925/api/deleteProducto/" + idProducto);
            HttpURLConnection conexion = (HttpURLConnection) url.openConnection();
            conexion.setRequestMethod("DELETE");
            int responseCode = conexion.getResponseCode();
            return responseCode == HttpURLConnection.HTTP_OK;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
