package servicios;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

import org.json.JSONObject;

/**
 * Servicio encargado de gestionar las funcionalidades relacionadas con la modificación y eliminación de usuarios.
 * Proporciona métodos para modificar y eliminar usuarios en el sistema.
 * 
 * @author GPR
 * @date 06/03/2025
 */
public class UsuarioFuncionalidades {

    /**
     * Modifica los datos de un usuario existente en el sistema enviando una solicitud HTTP PUT.
     * 
     * @param idUsuario El ID del usuario que se desea modificar.
     * @param usuarioJson Un objeto {@link JSONObject} que contiene los nuevos datos del usuario.
     * @return Un objeto {@link JSONObject} con la respuesta del servidor, que contiene la información del usuario modificado si la operación fue exitosa, o null en caso de error.
     * @throws Exception Si ocurre un error al realizar la solicitud o procesar la respuesta.
     * 
     * @author GPR
     * @date 06/03/2025
     */
    public JSONObject modificarUsuario(Long idUsuario, JSONObject usuarioJson) {
        try {
            URL url = new URL("http://localhost:4925/api/putUsuario/" + idUsuario);
            HttpURLConnection conexion = (HttpURLConnection) url.openConnection();
            conexion.setRequestMethod("PUT");
            conexion.setRequestProperty("Content-Type", "application/json");
            conexion.setDoOutput(true);

            String jsonStr = usuarioJson.toString();
            try (OutputStream os = conexion.getOutputStream()) {
                byte[] input = jsonStr.getBytes(StandardCharsets.UTF_8);
                os.write(input, 0, input.length);
            }

            int responseCode = conexion.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                StringBuilder responseStr = new StringBuilder();
                try (BufferedReader br = new BufferedReader(
                        new InputStreamReader(conexion.getInputStream(), StandardCharsets.UTF_8))) {
                    String line;
                    while ((line = br.readLine()) != null) {
                        responseStr.append(line);
                    }
                }
                return new JSONObject(responseStr.toString());
            } else {
                System.err.println("Error al modificar usuario. Código: " + responseCode);
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Elimina un usuario del sistema enviando una solicitud HTTP DELETE.
     * 
     * @param idUsuario El ID del usuario que se desea eliminar.
     * @return true si la eliminación fue exitosa (código de respuesta 200), false en caso contrario.
     * @throws Exception Si ocurre un error al realizar la solicitud o procesar la respuesta.
     * 
     * @author GPR
     * @date 06/03/2025
     */
    public boolean eliminarUsuario(String idUsuario) {
        try {
            URL url = new URL("http://localhost:4925/api/deleteUsuario/" + idUsuario);
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
