package servicios;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

/**
 * Servicio que proporciona funcionalidades para el proceso de inicio de sesión de un usuario.
 * Permite realizar el login mediante una solicitud HTTP POST.
 * 
 * @author GPR
 * @date 06/03/2025
 */
@Service
public class LoginFuncionalidades {

    /**
     * Realiza el proceso de inicio de sesión enviando una solicitud HTTP POST con el correo y la contraseña del usuario.
     * 
     * @param correo El correo electrónico del usuario que intenta iniciar sesión.
     * @param password La contraseña del usuario que intenta iniciar sesión.
     * @return Un objeto {@link JSONObject} con la respuesta del servidor, que contiene la información de sesión si es exitosa, o null en caso de error.
     * @throws Exception Si ocurre un error al realizar la solicitud o procesar la respuesta.
     */
    public JSONObject login(String correo, String password) {
        JSONObject requestJson = new JSONObject();
        requestJson.put("correo", correo);
        requestJson.put("password", password);

        try {
            URL url = new URL("http://localhost:4925/api/login");
            HttpURLConnection conexion = (HttpURLConnection) url.openConnection();
            conexion.setRequestMethod("POST");
            conexion.setRequestProperty("Content-Type", "application/json");
            conexion.setDoOutput(true);

            try (OutputStream os = conexion.getOutputStream()) {
                byte[] input = requestJson.toString().getBytes(StandardCharsets.UTF_8);
                os.write(input, 0, input.length);
            }

            int responseCode = conexion.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader in = new BufferedReader(new InputStreamReader(conexion.getInputStream(), StandardCharsets.UTF_8));
                StringBuilder responseStr = new StringBuilder();
                String line;
                while ((line = in.readLine()) != null) {
                    responseStr.append(line);
                }
                in.close();
                return new JSONObject(responseStr.toString());
            } else {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
