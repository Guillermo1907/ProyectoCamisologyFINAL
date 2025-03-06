package servicios;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

/**
 * Clase que proporciona funcionalidades para gestionar el carrito de compras de un usuario.
 * Permite obtener el carrito, agregar productos y eliminar productos del carrito.
 * 
 * @author GPR
 * @date 06/03/2025
 */
public class CarritoFuncionalidades {

    /**
     * Obtiene el contenido del carrito de compras para un usuario específico.
     * 
     * @param usuarioId El ID del usuario cuyo carrito se desea obtener.
     * @return El contenido del carrito en formato JSON como String.
     * @throws Exception Si ocurre un error al realizar la solicitud o procesar la respuesta.
     * 
     * @author GPR
     * @date 06/03/2025
     */
    public String obtenerCarrito(Long usuarioId) {
        try {
            URL url = new URL("http://localhost:4925/api/carrito/" + usuarioId);
            HttpURLConnection conexion = (HttpURLConnection) url.openConnection();
            conexion.setRequestMethod("GET");
            int responseCode = conexion.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader in = new BufferedReader(new InputStreamReader(conexion.getInputStream(), StandardCharsets.UTF_8));
                StringBuilder response = new StringBuilder();
                String linea;
                while ((linea = in.readLine()) != null) {
                    response.append(linea);
                }
                in.close();
                return response.toString();
            } else {
                System.err.println("Error al obtener el carrito, código: " + responseCode);
                return "";
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }
    
    /**
     * Agrega un producto al carrito de compras de un usuario.
     * 
     * @param usuarioId El ID del usuario al que se agregará el producto.
     * @param productoId El ID del producto que se desea agregar.
     * @param cantidad La cantidad del producto que se desea agregar.
     * @return La respuesta del servidor, generalmente un JSON indicando el éxito o el error.
     * @throws Exception Si ocurre un error al realizar la solicitud o procesar la respuesta.
     * 
     * @author GPR
     * @date 06/03/2025
     */
    public String agregarProducto(Long usuarioId, Long productoId, int cantidad) {
        try {
            URL url = new URL("http://localhost:4925/api/carrito/" + usuarioId + "/agregar?productoId=" 
                              + productoId + "&cantidad=" + cantidad);
            HttpURLConnection conexion = (HttpURLConnection) url.openConnection();
            conexion.setRequestMethod("POST");
            conexion.setDoOutput(true);
            conexion.setRequestProperty("Content-Type", "application/json");
            
            int responseCode = conexion.getResponseCode();
            BufferedReader in = new BufferedReader(new InputStreamReader(conexion.getInputStream(), StandardCharsets.UTF_8));
            StringBuilder response = new StringBuilder();
            String linea;
            while ((linea = in.readLine()) != null) {
                response.append(linea);
            }
            in.close();
            return response.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return "{\"error\":\"Error al agregar producto.\"}";
        }
    }
    
    /**
     * Elimina un producto del carrito de compras de un usuario.
     * 
     * @param usuarioId El ID del usuario del que se eliminará el producto.
     * @param detalleId El ID del detalle del carrito que se desea eliminar.
     * @return La respuesta del servidor, generalmente un JSON indicando el éxito o el error.
     * @throws Exception Si ocurre un error al realizar la solicitud o procesar la respuesta.
     * 
     * @author GPR
     * @date 06/03/2025
     */
    public String eliminarProducto(Long usuarioId, Long detalleId) {
        try {
            URL url = new URL("http://localhost:4925/api/carrito/" + usuarioId + "/eliminar?detalleId=" + detalleId);
            HttpURLConnection conexion = (HttpURLConnection) url.openConnection();
            conexion.setRequestMethod("DELETE");
            int responseCode = conexion.getResponseCode();
            BufferedReader in = new BufferedReader(new InputStreamReader(conexion.getInputStream(), StandardCharsets.UTF_8));
            StringBuilder response = new StringBuilder();
            String linea;
            while ((linea = in.readLine()) != null) {
                response.append(linea);
            }
            in.close();
            return response.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return "{\"error\":\"Error al eliminar producto.\"}";
        }
    }
}
