<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page session="true" %>
<!DOCTYPE html>
    <html lang="es">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Camisology | Premier League</title>
        <link rel="stylesheet" href="premier.css">
        <link href="https://fonts.googleapis.com/css2?family=Bebas+Neue&family=Roboto:wght@400;700&display=swap" rel="stylesheet">
        <link rel="icon" href="/Vista_Camisology/img/iconos/favicon.ico" type="image/x-icon">
    </head>
    <body>
            <header>
            
		<a href="/Vista_Camisology/index.jsp" style="text-decoration: none;"><div class="logo">CAMISOLOGY</div></a>
            
        <div class="enlaces-categorias">
    		<a href="/Vista_Camisology/categorias/Selecciones/selecciones.jsp">SELECCIONES</a>
    		<a href="/Vista_Camisology/categorias/LaLiga/laliga.jsp">LA LIGA</a>
    		<a href="/Vista_Camisology/categorias/PremierLeague/premierleague.jsp">PREMIER LEAGUE</a>
    		<a href="/Vista_Camisology/categorias/SerieA/seriea.jsp">SERIE A</a>
    		<a href="/Vista_Camisology/categorias/Bundesliga/bundesliga.jsp">BUNDESLIGA</a>
		</div>
                
        <% 
        	String usuario = (String) session.getAttribute("usuario");
        	String foto = (String) session.getAttribute("foto");
        	String fotoSrc = (foto != null) ? "data:image/png;base64," + foto : "/Vista_Camisology/img/header/iconoperfil.png";
        %>

        <div class="iconos-header">
            <img id="perfil-icono" src="<%= fotoSrc %>" alt="Icono de Perfil">
            <img id="carrito-icono" src="/Vista_Camisology/img/header/carrito.png" alt="Carrito de la compra">
        </div>

             

            </header>
            

         <div id="pestana-perfil" class="mini-pestana">
    		<% if (usuario != null) { %>
    		<form action="<%= request.getContextPath() %>/login" method="post">
        		<input type="hidden" name="action" value="logout">
        		<button class="boton boton-rojo">Cerrar Sesión</button>
    		</form>
    		<% } else { %>
        		<button id="boton-iniciar-sesion" class="boton">Iniciar Sesión</button>
        		<button id="boton-registrarse" class="boton boton-secundario">Registrarse</button>
    		<% } %>
		</div>


<!-- Modal de Iniciar Sesión -->
<div id="modal-iniciar-sesion" class="modal" style="display: none;">
    <div class="contenido-modal">
        <span class="cerrar-boton">&times;</span>
        <h2>Iniciar Sesión</h2>
        <form class="needs-validation" novalidate action="login" method="POST">
            <label for="correo">Correo Electrónico:</label>
            <input type="email" id="correo" name="correo" required>
            
            <label for="password">Contraseña:</label>
            <input type="password" id="password" name="password" required>
            
            <button type="submit" class="boton-enviar">Iniciar Sesión</button>
        </form>
        <p id="error-login" style="color: red; display: none;">Correo o contraseña incorrectos.</p> 
    </div>
</div>

<!-- Modal de Registrarse -->
<div id="modal-registrarse" class="modal-registrarse">
    <div class="contenido-modal-registrarse">
        <span class="cerrar-boton-registrarse">&times;</span>
        <h2>Crear una Cuenta</h2>
        
	      <form id="form-registrarse" action="registro" method="POST" enctype="multipart/form-data">
	    <input type="text" id="nombre" name="nombre" placeholder="Nombre completo" required>
	    <input type="email" id="correo-registro" name="correo-registro" placeholder="Correo electrónico" required>
	    <input type="tel" id="movil" name="movil" placeholder="Número de teléfono" required>
	    <input type="password" id="password-registro" name="password" placeholder="Contraseña" required>
	    <input type="password" id="confirmar-password" name="confirmar-password" placeholder="Confirmar contraseña" required>
	     <div class="subida-imagen">
        	<label for="foto">Subir foto</label>
        	<span id="nombre-archivo">No se ha seleccionado archivo</span>
        	<input type="file" id="foto" name="foto" accept="image/*" onchange="mostrarNombreArchivo()">
    	</div>
	    
	    <div class="checkbox">
	        <input type="checkbox" id="aceptar-terminos" required>
	        <label for="aceptar-terminos">Acepto los <a href="#" style="color: #ff5555;">términos y condiciones</a></label>
	    </div>
	    <button type="submit" class="boton-enviar-registrarse">Registrarse</button>
	    <div class="error-message" id="error-message" style="color: red;"></div>
	</form>

    </div>
</div>



  

        <footer class="pie-pagina">
            <div class="contenedor-footer">
                <div class="logo">CAMISOLOGY</div>
                <p class="frasefinal">ENCUENTRA CAMISETAS ORIGINALES DE TUS EQUIPOS Y SELECCIONES FAVORITAS. 
                    ¡Viste tu pasión!</p>
                <div class="enlaces-footer">
                    <a href="#">Contacta con nosotros</a>
                    <a href="#">Política de privacidad</a>
                    <a href="#">Términos y condiciones</a>
                    <a href="#">Política de cookies</a>
                </div>
                <p class="copyright">© 2025 Camisology. Todos los derechos reservados.</p>
            </div>
        </footer>

        <script src="premier.js"></script>
    </body>
    </html>
