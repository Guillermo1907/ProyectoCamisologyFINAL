<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Panel de Administración</title>
    <link href="https://fonts.googleapis.com/css2?family=Bebas+Neue&family=Roboto:wght@400;700&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="panel-ad.css">
</head>
<body>
  <div class="sidebar">
    <a href="panel-ad.jsp"><h2>Panel de Administración</h2></a>
    <a href="#" onclick="mostrarSeccion('usuarios')">Usuarios</a>
    <a href="#" onclick="mostrarSeccion('productos')">Productos</a>
  </div>
  <div class="content">
  
    <!-- Sección Usuarios -->
    <div id="usuarios" class="seccion oculto">
        <h2>Gestión de Usuarios</h2>
        <div class="tabs">
            <button class="tab-btn" onclick="mostrarPestaña('añadir-usuario')">Añadir Usuario</button>
            <button class="tab-btn" onclick="mostrarPestaña('gestion-usuario')">Gestión Usuario</button>
        </div>
        <div id="añadir-usuario" class="tab-content">
        
            <p><b>AÑADIR USUARIO</b></p>
            <form id="form-registrarse" action="/Vista_Camisology/registroPanel" method="POST" enctype="multipart/form-data">
             <div class="contenedor-formulario">
                <input type="text" id="nombre-completo" name="nombre-completo" placeholder="Nombre Completo">
                <input type="text" id="movil" name="movil" placeholder="Móvil">
                <input type="email" id="correo" name="correo" placeholder="Correo">
                <input type="password" id="password" name="password" placeholder="Contraseña">
                <input type="file" id="foto" name="foto" accept="image/*">
                <label>
                    <input type="checkbox" id="admin" name="admin"> Administrador
                </label>
                <button class="btn btn-amarillo" type="submit">Enviar</button>
            </div>
            </form>
           
        </div>

      <div id="gestion-usuario" class="tab-content oculto">
        <h3>Lista de Usuarios</h3>
        <div id="lista-usuarios">
        </div>
      </div>
    </div>
        
        
<div id="modal-editar-usuario" class="modal">
    <div class="modal-contenido">
        <span class="cerrar-modal" onclick="cerrarModal()">&times;</span>
        <h2>Editar Usuario</h2>
        <form id="form-editar-usuario" enctype="multipart/form-data">
            <input type="hidden" id="edit-id" name="id">
            <label>Nombre Completo:</label>
            <input type="text" id="edit-nombre" name="nombreCompleto" required>
            
            <label>Móvil:</label>
            <input type="text" id="edit-movil" name="movil" required>
            
            <label>Correo:</label>
            <input type="email" id="edit-correo" name="correo" required>
            
            <label>Contraseña:</label>
            <input type="password" id="edit-password" name="password">
            
             <label>Tipo de Usuario:</label>
      			<select id="edit-tipoUsuario" name="tipoUsuario" required>
          		<option value="Usuario">Usuario</option>
          		<option value="Administrador">Administrador</option>
      		</select>
            
            <label>Foto:</label>
            <input type="file" id="edit-foto" name="foto" accept="image/*">
            
            <button type="submit" class="btn btn-amarillo">Guardar Cambios</button>
        </form>
    </div>
</div>

        
		    <!-- Sección Productos -->
<div id="productos" class="seccion oculto">
  <h2>Gestión de Productos</h2>
  <div class="tabs">
    <button class="tab-btn" onclick="mostrarPestañaProductos('añadir-producto')">Añadir Producto</button>
    <button class="tab-btn" onclick="mostrarPestañaProductos('gestion-producto')">Gestión Producto</button>
  </div>
  
  <div id="añadir-producto" class="tab-content">
    <p><b>AÑADIR PRODUCTO</b></p>
    <form id="form-guardar-producto" enctype="multipart/form-data">
      <div class="contenedor-formulario">
        <input type="text" id="producto-nombre" name="nombre" placeholder="Nombre" required>
        <input type="number" id="producto-precio" name="precio" placeholder="Precio" required step="0.01">
        <select id="producto-categoria" name="categoria" required>
         <option value="">Seleccione Liga</option>
        <option value="1">Selecciones</option>
        <option value="2">La Liga</option>
        <option value="3">Premier League</option>
        <option value="4">Serie A</option>
        <option value="5">Bundesliga</option>
        </select>
        <input type="file" id="producto-foto" name="foto" accept="image/*">
        <button class="btn btn-amarillo" type="submit">Guardar Producto</button>
      </div>
    </form>
  </div>
  
  <div id="gestion-producto" class="tab-content oculto">
    <h3>Lista de Productos</h3>
    <div id="lista-productos">
    </div>
  </div>
</div>

<div id="modal-editar-producto" class="modal">
  <div class="modal-contenido">
    <span class="cerrar-modal" onclick="cerrarModalProducto()">&times;</span>
    <h2>Editar Producto</h2>
    <form id="form-editar-producto" enctype="multipart/form-data">
      <input type="hidden" id="edit-id-producto" name="id">
      <label>Nombre:</label>
      <input type="text" id="edit-nombre-producto" name="nombre" required>
      <label>Precio:</label>
      <input type="number" id="edit-precio-producto" name="precio" required step="0.01">
      <label>Categoría:</label>
      <select id="edit-categoria-producto" name="categoria" required>
        <option value="">Seleccione Liga</option>
        <option value="1">Selecciones</option>
        <option value="2">La Liga</option>
        <option value="3">Premier League</option>
        <option value="4">Serie A</option>
        <option value="5">Bundesliga</option>
      </select>
      <label>Foto:</label>
      <input type="file" id="edit-foto-producto" name="foto" accept="image/*">
      <button type="submit" class="btn btn-amarillo">Guardar Cambios</button>
    </form>
  </div>
</div>
</div>
    <script>
    //esto lo utilizo para conseguir el id del admin para que se no se pueda eliminar a si mismo
    var currentUserId = "<%= session.getAttribute("usuarioId") %>";
</script>


    
    
  <script src="panelad.js"></script>
</body>
</html>
