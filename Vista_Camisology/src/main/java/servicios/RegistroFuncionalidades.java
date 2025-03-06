package servicios;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import org.json.JSONObject;
import org.springframework.stereotype.Service;
import dtos.LoginDto;

/**
 * Servicio encargado de gestionar las funcionalidades relacionadas con el registro de usuarios.
 * Proporciona métodos para encriptar contraseñas y crear nuevos usuarios en el sistema.
 * 
 * @author GPR
 * @date 06/03/2025
 */
@Service
public class RegistroFuncionalidades {

    /**
     * Encripta la contraseña del usuario utilizando el algoritmo SHA-256.
     * 
     * @param contraseña La contraseña en texto plano que se desea encriptar.
     * @return La contraseña encriptada en formato hexadecimal.
     * @throws RuntimeException Si ocurre un error al intentar encriptar la contraseña.
     * 
     * @author GPR
     * @date 06/03/2025
     */
    public String encriptarContrasenya(String contraseña) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(contraseña.getBytes(StandardCharsets.UTF_8));
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = String.format("%02x", b);
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Crea un nuevo usuario en el sistema enviando una solicitud HTTP POST al servidor.
     * 
     * @param usuario Un objeto {@link LoginDto} que contiene los datos del usuario a crear.
     * @return El código de respuesta HTTP del servidor (por ejemplo, 200 si la creación fue exitosa, 500 si hubo un error).
     * @throws Exception Si ocurre un error al realizar la solicitud o procesar la respuesta.
     * 
     * @author GPR
     * @date 06/03/2025
     */
    public int crearUsuario(LoginDto usuario) {
        JSONObject json = new JSONObject();
        json.put("nombreCompleto", usuario.getNombreCompleto());
        json.put("correo", usuario.getCorreo());
        json.put("movil", usuario.getMovil());
        json.put("password", usuario.getPassword());
        json.put("tipoUsuario", usuario.getTipoUsuario());
        if (usuario.getFoto() != null) {
            String fotoBase64 = Base64.getEncoder().encodeToString(usuario.getFoto());
            json.put("foto", fotoBase64);
        }
        try {
            URL url = new URL("http://localhost:4925/api/registro");
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
}
