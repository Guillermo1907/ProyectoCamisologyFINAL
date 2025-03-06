package servicios;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import dtos.LoginDto;

/**
 * Servicio encargado de gestionar las funcionalidades relacionadas con el panel de registro de usuarios.
 * Proporciona métodos para enviar datos de registro y obtener la lista de usuarios registrados.
 * 
 * @author GPR
 * @date 06/03/2025
 */
@Service
public class PanelRegistroFuncionalidades {

    /**
     * Envia los datos de un nuevo usuario al panel de administración para su registro.
     * 
     * @param nombre El nombre completo del usuario a registrar.
     * @param correo El correo electrónico del usuario a registrar.
     * @param movil El número de teléfono móvil del usuario a registrar.
     * @param password La contraseña del usuario a registrar.
     * @param fotoBytes Los bytes de la foto del usuario en formato base64 (opcional).
     * @param tipoUsuario El tipo de usuario que se está registrando (Ej. "Administrador", "Usuario").
     * @return El código de respuesta HTTP de la solicitud (200 para éxito, 500 para error).
     * @throws Exception Si ocurre un error al enviar los datos.
     * 
     * @author GPR
     * @date 06/03/2025
     */
    public int enviarDatosPanelRegistro(String nombre, String correo, String movil, String password, byte[] fotoBytes, String tipoUsuario) {
        JSONObject json = new JSONObject();
        json.put("nombreCompleto", nombre);
        json.put("correo", correo);
        json.put("movil", movil);
        json.put("password", password);
        json.put("tipoUsuario", tipoUsuario);
        
        if (fotoBytes != null) {
            String fotoBase64 = java.util.Base64.getEncoder().encodeToString(fotoBytes);
            json.put("foto", fotoBase64);
        }
        
        try {
            URL url = new URL("http://localhost:4925/api/registroPanel");
            HttpURLConnection conexion = (HttpURLConnection) url.openConnection();
            conexion.setRequestMethod("POST");
            conexion.setRequestProperty("Content-Type", "application/json");
            conexion.setDoOutput(true);
            
            try (OutputStream os = conexion.getOutputStream()) {
                byte[] input = json.toString().getBytes(StandardCharsets.UTF_8);
                os.write(input, 0, input.length);
            }
            
            int responseCode = conexion.getResponseCode();
            return responseCode;
        } catch (Exception e) {
            e.printStackTrace();
            return 500;
        }
    }
    
    /**
     * Obtiene la lista de usuarios registrados desde el panel de administración.
     * 
     * @return Una lista de objetos {@link LoginDto} que representan los usuarios registrados.
     * @throws Exception Si ocurre un error al obtener los usuarios.
     * 
     * @author GPR
     * @date 06/03/2025
     */
    public List<LoginDto> obtenerUsuarios() {
        List<LoginDto> usuarios = new ArrayList<>();
        try {
            URL url = new URL("http://localhost:4925/api/mostrarUsuario");
            HttpURLConnection conexion = (HttpURLConnection) url.openConnection();
            conexion.setRequestMethod("GET");
            conexion.setRequestProperty("Accept", "application/json");

            int codigoRespuesta = conexion.getResponseCode();

            if (codigoRespuesta == 200) {
                try (InputStream is = conexion.getInputStream();
                     InputStreamReader isr = new InputStreamReader(is, StandardCharsets.UTF_8);
                     BufferedReader br = new BufferedReader(isr)) {

                    StringBuilder response = new StringBuilder();
                    String linea;
                    while ((linea = br.readLine()) != null) {
                        response.append(linea);
                    }

                    String jsonString = response.toString();
                    jsonString = jsonString.replaceAll(",\\s*\"foto\":\\s*(\"[^\"]*\"|null|\\[[^\\]]*\\])", "");
                    
                    Gson gson = new Gson();
                    Type listType = new TypeToken<ArrayList<LoginDto>>() {}.getType();
                    usuarios = gson.fromJson(jsonString, listType);
                }
            } else {
                System.err.println("Error al obtener usuarios. Código de respuesta: " + codigoRespuesta);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return usuarios;
    }
}
