<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page session="true" %>
<!DOCTYPE html>
<html lang="es">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Camisology | Pasarela</title>
  <link rel="stylesheet" href="pasarela.css">
  <link href="https://fonts.googleapis.com/css2?family=Bebas+Neue&family=Roboto:wght@400;700&display=swap" rel="stylesheet">
  <link rel="icon" href="/Vista_Camisology/img/iconos/favicon.ico" type="image/x-icon">
</head>
<body>
  <header>
      <a href="/Vista_Camisology/index.jsp" style="text-decoration: none;">
          <div class="logo">CAMISOLOGY</div>
      </a>
      <% 
          String usuario = (String) session.getAttribute("usuario");
          String foto = (String) session.getAttribute("foto");
          String fotoSrc = (foto != null) ? "data:image/png;base64," + foto : "/Vista_Camisology/img/header/iconoperfil.png";
      %>
      <div class="iconos-header">
          <img id="perfil-icono" src="<%= fotoSrc %>" alt="Icono de Perfil">
      </div>
  </header>

  <main class="contenido-pasarela">
      <div class="form-container">
          <h2>Datos de la Tarjeta de Crédito</h2>
          <form id="form-pasarela" action="/Vista_Camisology/pasarelaPago/pasarela/validar" method="POST">
              <div class="grupo-formulario">
                  <label for="cardNumber">Número de Tarjeta:</label>
                  <input type="text" id="cardNumber" name="cardNumber" placeholder="16 dígitos" required>
              </div>
              <div class="grupo-formulario">
                  <label for="cardHolder">Titular de la Tarjeta:</label>
                  <input type="text" id="cardHolder" name="cardHolder" placeholder="Nombre completo" required>
              </div>
              <div class="grupo-formulario">
                  <label for="expiration">Fecha de Expiración (MM/AA):</label>
                  <input type="text" id="expiration" name="expiration" placeholder="MM/AA" required>
              </div>
              <div class="grupo-formulario">
                  <label for="cvv">CVV:</label>
                  <input type="text" id="cvv" name="cvv" placeholder="3 dígitos" required>
              </div>
              <button type="submit" class="btn-enviar">Ingresar</button>
              <input type="hidden" id="idPedido" name="idPedido" value="<%= request.getParameter("idPedido") %>">
          </form>
      </div>
  </main>

  <div id="modal-confirmacion">
      <div class="modal-content">
          <span class="cerrar-modal" onclick="cerrarModalConfirmacion()">×</span>
          <h2>Pago aceptado</h2>
          <img class="imagen-confirmacion" src="confirmacion.png" alt="Confirmación">
          <div id="mensaje-redirigiendo">Redirigiendo a la página principal...</div>
      </div>
  </div>

  <footer class="pie-pagina">
      <div class="contenedor-footer">
          <div class="logo">CAMISOLOGY</div>
          <p class="copyright">© 2025 Camisology. Todos los derechos reservados.</p>
      </div>
  </footer>

  <script src="pasarela.js"></script>
</body>
</html>
