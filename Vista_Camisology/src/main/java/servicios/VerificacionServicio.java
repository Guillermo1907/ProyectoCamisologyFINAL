package servicios;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Random;
import javax.mail.*;
import javax.mail.internet.*;

/**
 * Servicio encargado de la generación, verificación y envío de códigos de verificación por correo electrónico.
 * Permite generar un código de verificación único, guardarlo, verificarlo y enviarlo al usuario por correo.
 * 
 * @author GPR
 * @date 06/03/2025
 */
public class VerificacionServicio {

    private static Map<String, Integer> codigosVerificacion = new HashMap<>();

    /**
     * Genera un código de verificación aleatorio de seis dígitos.
     * 
     * @return Un número entero de seis dígitos que representa el código de verificación generado.
     * 
     * @author GPR
     * @date 06/03/2025
     */
    public int generarCodigoVerificacion() {
        Random random = new Random();
        return 100000 + random.nextInt(900000); // Genera un número aleatorio entre 100000 y 999999
    }

    /**
     * Guarda el código de verificación generado para un correo específico.
     * 
     * @param correo El correo electrónico al que se asignará el código de verificación.
     * @param codigo El código de verificación a guardar.
     * 
     * @author GPR
     * @date 06/03/2025
     */
    public void guardarCodigo(String correo, int codigo) {
        codigosVerificacion.put(correo, codigo);
    }

    /**
     * Verifica si el código ingresado por el usuario coincide con el código guardado para el correo especificado.
     * 
     * @param correo El correo electrónico asociado al código de verificación.
     * @param codigoIngresado El código que el usuario ingresó para verificar.
     * @return true si el código ingresado es válido, false en caso contrario.
     * 
     * @author GPR
     * @date 06/03/2025
     */
    public boolean verificarCodigo(String correo, int codigoIngresado) {
        return codigosVerificacion.containsKey(correo) && codigosVerificacion.get(correo) == codigoIngresado;
    }

    /**
     * Envía un correo de verificación con el código generado al correo especificado.
     * 
     * @param correo El correo electrónico al que se enviará el código de verificación.
     * @param codigoVerificacion El código de verificación a enviar.
     * @throws MessagingException Si ocurre un error al intentar enviar el correo electrónico.
     * 
     * @author GPR
     * @date 06/03/2025
     */
    public void enviarCorreoVerificacion(String correo, int codigoVerificacion) throws MessagingException {
        final String usuario = "camisology7@gmail.com"; 
        final String contraseña = "fvax mefm ymrf lowc"; // Contraseña de la cuenta de correo

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        // Configuración de la sesión de correo
        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(usuario, contraseña);
            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(usuario));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(correo));
            message.setSubject("Código de Verificación");
            message.setText("Tu código de verificación es: " + codigoVerificacion);

            Transport.send(message); // Envío del correo
        } catch (MessagingException e) {
            System.err.println("Error al enviar correo: " + e.getMessage());
            throw e;
        }
    }
}
