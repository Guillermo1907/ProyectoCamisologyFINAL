package servicios;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import org.json.JSONObject;

/**
 * Servicio encargado de gestionar las funcionalidades relacionadas con la pasarela de pedidos.
 * Proporciona un método para actualizar el estado de un pedido.
 * 
 * @author GPR
 * @date 06/03/2025
 */
public class PasarelaFuncionalidades {

    /**
     * Actualiza el estado de un pedido en el sistema enviando una solicitud HTTP PUT al servidor.
     * 
     * @param idPedido El identificador único del pedido que se desea actualizar.
     * @param nuevoEstado El nuevo estado que se asignará al pedido.
     * @return Un objeto {@link JSONObject} que contiene la respuesta del servidor, que incluye el mensaje de éxito
     *         o un mensaje de error si ocurre alguna falla durante la actualización.
     * @throws Exception Si ocurre un error al realizar la solicitud o procesar la respuesta.
     * 
     * @author GPR
     * @date 06/03/2025
     */
    public JSONObject actualizarEstado(int idPedido, String nuevoEstado) {
        JSONObject jsonRequest = new JSONObject();
        jsonRequest.put("idPedido", idPedido);
        jsonRequest.put("nuevoEstado", nuevoEstado);

        try {
            URL url = new URL("http://localhost:4925/api/actualizarEstadoPedido");
            HttpURLConnection conexion = (HttpURLConnection) url.openConnection();
            conexion.setRequestMethod("PUT");
            conexion.setRequestProperty("Content-Type", "application/json");
            conexion.setDoOutput(true);

            try (OutputStream os = conexion.getOutputStream()) {
                byte[] input = jsonRequest.toString().getBytes(StandardCharsets.UTF_8);
                os.write(input, 0, input.length);
            }

            int responseCode = conexion.getResponseCode();
            StringBuilder responseStr = new StringBuilder();
            try (BufferedReader in = new BufferedReader(
                    new InputStreamReader(conexion.getInputStream(), StandardCharsets.UTF_8))) {
                String line;
                while ((line = in.readLine()) != null) {
                    responseStr.append(line.trim());
                }
            }
            return new JSONObject(responseStr.toString());
        } catch (Exception e) {
            e.printStackTrace();
            return new JSONObject().put("mensaje", "Error al actualizar estado");
        }
    }
}
