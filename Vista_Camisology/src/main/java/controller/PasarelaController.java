package controller;

import java.io.IOException;
import java.io.PrintWriter;
import org.json.JSONObject;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Controlador para manejar las solicitudes de validación de pagos en la pasarela de pagos.
 * 
 * @author GPR
 * @date 06/03/2025
 */
@WebServlet("/pasarelaPago/pasarela/validar")
public class PasarelaController extends HttpServlet {

    /**
     * Maneja las solicitudes POST para validar los datos de pago de una tarjeta.
     * 
     * @param solicitud Objeto HttpServletRequest que contiene la solicitud HTTP.
     * @param respuesta Objeto HttpServletResponse que contiene la respuesta HTTP.
     * @throws ServletException Si ocurre un error durante el procesamiento de la solicitud.
     * @throws IOException Si ocurre un error al escribir la respuesta.
     * 
     * @author GPR
     * @date 06/03/2025
     */
    @Override
    protected void doPost(HttpServletRequest solicitud, HttpServletResponse respuesta)
            throws ServletException, IOException {
        
        // Obtención de los parámetros enviados en la solicitud
        String numeroTarjeta = solicitud.getParameter("cardNumber");
        String titularTarjeta = solicitud.getParameter("cardHolder");
        String expiracion = solicitud.getParameter("expiration");
        String cvv = solicitud.getParameter("cvv");

        // Validación de los datos ingresados
        boolean formatoNumero = numeroTarjeta != null && numeroTarjeta.matches("^\\d{16}$");
        boolean formatoCVV = cvv != null && cvv.matches("^\\d{3}$");
        boolean formatoExpiracion = expiracion != null && expiracion.matches("^(0[1-9]|1[0-2])/\\d{2}$");
        boolean formatoTitular = titularTarjeta != null && !titularTarjeta.trim().isEmpty();

        // Validación con el algoritmo de Luhn y la fecha de expiración
        boolean luhnValido = formatoNumero && validarLuhn(numeroTarjeta);
        boolean expiracionValida = formatoExpiracion && validarExpiracion(expiracion);

        // Determina si los datos son válidos
        boolean aceptado = formatoNumero && formatoCVV && formatoExpiracion && formatoTitular
                && luhnValido && expiracionValida;

        // Construcción de la respuesta en formato JSON
        JSONObject jsonRespuesta = new JSONObject();
        jsonRespuesta.put("aceptado", aceptado);

        // Establecer el tipo de contenido como JSON y escribir la respuesta
        respuesta.setContentType("application/json;charset=UTF-8");
        PrintWriter salida = respuesta.getWriter();
        salida.print(jsonRespuesta.toString());
        salida.flush();
    }

    /**
     * Valida el número de tarjeta utilizando el algoritmo de Luhn.
     * 
     * @param numeroTarjeta El número de la tarjeta de crédito.
     * @return true si el número de tarjeta es válido según el algoritmo de Luhn, false en caso contrario.
     * 
     * @author GPR
     * @date 06/03/2025
     */
    private boolean validarLuhn(String numeroTarjeta) {
        int suma = 0;
        boolean alternar = false;
        for (int i = numeroTarjeta.length() - 1; i >= 0; i--) {
            int digito = Integer.parseInt(numeroTarjeta.substring(i, i + 1));
            if (alternar) {
                digito *= 2;
                if (digito > 9) {
                    digito = (digito % 10) + 1;
                }
            }
            suma += digito;
            alternar = !alternar;
        }
        return (suma % 10 == 0);
    }

    /**
     * Valida la fecha de expiración de la tarjeta.
     * 
     * @param expiracion La fecha de expiración en formato MM/YY.
     * @return true si la fecha de expiración es válida (posterior al año actual), false en caso contrario.
     * 
     * @author GPR
     * @date 06/03/2025
     */
    private boolean validarExpiracion(String expiracion) {
        String[] partes = expiracion.split("/");
        int mes = Integer.parseInt(partes[0]);
        int anio = Integer.parseInt(partes[1]) + 2000; // Se ajusta el año para los últimos dos dígitos.
        return anio >= 2025;
    }
}
