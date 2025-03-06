package com.Camisology.controller;

import java.util.ArrayList;
import java.util.Base64;
import java.util.Map;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.Camisology.dtos.PedidoDto;
import com.Camisology.dtos.ProductoDto;
import com.Camisology.dtos.UsuarioDto;
import com.Camisology.repositorios.UsuarioInterfaz;
import com.Camisology.servicios.CarritoFuncionalidades;
import com.Camisology.servicios.ProductoFuncionalidades;
import com.Camisology.servicios.UsuarioFuncionalidades;
import org.springframework.web.bind.annotation.CrossOrigin;

/**
 * Controlador principal para gestionar las operaciones de la API de Camisology.
 * Proporciona endpoints para la gestión de usuarios, productos y carritos.
 * 
 * @author GPR
 * @date 06/03/2025
 */
@RestController
@RequestMapping("/api")
public class CamisologyController {

	@Autowired
	UsuarioFuncionalidades usuarioFuncionalidade;
	
	@Autowired
	UsuarioInterfaz usuarioInterfaz;
	
	@Autowired
	ProductoFuncionalidades productoFuncionalidades;
	
	@Autowired
	private CarritoFuncionalidades carritoFuncionalidade;
	
	/**
	 * Obtiene la lista de usuarios registrados.
	 * 
	 * @return Un ArrayList de {@link UsuarioDto} que representa la lista de usuarios.
	 * @author GPR
	 * @date 06/03/2025
	 */
	@GetMapping("/mostrarUsuario") 
	public ArrayList<UsuarioDto> getUsuario(){
		return this.usuarioFuncionalidade.getUsuario();
	}
	
	/**
	 * Guarda un nuevo usuario.
	 * 
	 * @param usuario Objeto {@link UsuarioDto} con los datos del usuario a guardar.
	 * @return El usuario guardado.
	 * @author GPR
	 * @date 06/03/2025
	 */
	@PostMapping("/guardarUsuario")
    public UsuarioDto guardarUsuario(@RequestBody UsuarioDto usuario) {
        return this.usuarioFuncionalidade.guardarUsuario(usuario);
    }
	
	/**
	 * Registra un nuevo usuario.
	 * 
	 * @param usuario Objeto {@link UsuarioDto} con los datos del usuario a registrar.
	 * @return Respuesta HTTP que indica el resultado del registro.
	 * @author GPR
	 * @date 06/03/2025
	 */
	@PostMapping("/registro")
	public ResponseEntity<?> registro(@RequestBody UsuarioDto usuario) {
	    if (usuario.getPassword() == null || usuario.getPassword().isEmpty()) {
	        return ResponseEntity.status(400).body("La contraseña no puede ser nula o vacía");
	    }

	    UsuarioDto usuarioExistente = usuarioInterfaz.findByCorreo(usuario.getCorreo());
	    if (usuarioExistente != null) {
	        return ResponseEntity.status(409).body("El correo ya está en uso");
	    }
	    
	    usuario.setTipoUsuario("Usuario");

	    UsuarioDto usuarioGuardado = this.usuarioFuncionalidade.guardarUsuario(usuario);

	    if (usuarioGuardado != null) {
	        return ResponseEntity.status(201).body("Usuario registrado con éxito");
	    } else {
	        return ResponseEntity.status(500).body("Error al registrar el usuario");
	    }
	}
	
	/**
	 * Registra un nuevo usuario desde el panel de administración.
	 * 
	 * @param usuario Objeto {@link UsuarioDto} con los datos del usuario a registrar.
	 * @return Respuesta HTTP que indica el resultado del registro en el panel de administración.
	 * @author GPR
	 * @date 06/03/2025
	 */
	@PostMapping("/registroPanel")
	public ResponseEntity<?> registroPanel(@RequestBody UsuarioDto usuario){
			if (usuario.getPassword() == null || usuario.getPassword().isEmpty()) {
		        return ResponseEntity.status(400).body("La contraseña no puede ser nula o vacía");
		    }
			
		    UsuarioDto usuarioExistente = usuarioInterfaz.findByCorreo(usuario.getCorreo());
		    if (usuarioExistente != null) {
		        return ResponseEntity.status(409).body("El correo ya está en uso");
		    }

		    UsuarioDto usuarioGuardado = this.usuarioFuncionalidade.guardarUsuario(usuario);

		    if (usuarioGuardado != null) {
		        return ResponseEntity.status(201).body("Usuario registrado con éxito");
		    } else {
		        return ResponseEntity.status(500).body("Error al registrar el usuario");
		    }
	}

	/**
	 * Actualiza los datos de un usuario.
	 * 
	 * @param idUsuario El ID del usuario a modificar.
	 * @param usuario Objeto {@link UsuarioDto} con los nuevos datos del usuario.
	 * @return El usuario actualizado.
	 * @author GPR
	 * @date 06/03/2025
	 */
	@CrossOrigin(origins = "http://localhost:8080")
	@PutMapping("/putUsuario/{id_usuario}")
	public UsuarioDto putUsuario(@PathVariable("id_usuario") Long idUsuario, @RequestBody UsuarioDto usuario) {    
	    return usuarioFuncionalidade.modificarUsuario(usuario, idUsuario);
	}

	/**
	 * Elimina un usuario por su ID.
	 * 
	 * @param idUsuario El ID del usuario a eliminar.
	 * @return true si la eliminación fue exitosa; false en caso contrario.
	 * @author GPR
	 * @date 06/03/2025
	 */
	@CrossOrigin(origins = "http://localhost:8080")
	@DeleteMapping("deleteUsuario/{idUsuario}")
	public boolean deleteUsuario(@PathVariable("idUsuario")String idUsuario) {
		return this.usuarioFuncionalidade.eliminarUsuario(idUsuario);
	}
	
	/**
	 * Realiza el inicio de sesión del usuario.
	 * 
	 * @param usuario Objeto {@link UsuarioDto} que contiene el correo y la contraseña del usuario.
	 * @return Respuesta HTTP con los datos del usuario en formato JSON si las credenciales son correctas, o un error 401 si son incorrectas.
	 * @author GPR
	 * @date 06/03/2025
	 */
	@PostMapping("/login")
	public ResponseEntity<?> login(@RequestBody UsuarioDto usuario) {
	    if (usuarioFuncionalidade.validarCredenciales(usuario.getCorreo(), usuario.getPassword())) {
	        UsuarioDto usuarioEncontrado = usuarioInterfaz.findByCorreo(usuario.getCorreo());
	        
	        JSONObject json = new JSONObject();

	        json.put("id", usuarioEncontrado.getIdUsuario());
	        json.put("email", usuarioEncontrado.getCorreo());
	        json.put("rol", usuarioEncontrado.getTipoUsuario());

	        byte[] foto = usuarioEncontrado.getFoto();
	        if (foto != null) {
	            String fotoBase64 = Base64.getEncoder().encodeToString(foto);
	            json.put("foto", fotoBase64);
	        } else {
	            json.put("foto", "No tiene imagen");
	        }
	        
	        return ResponseEntity.ok(json.toString());
	    } else {
	        return ResponseEntity.status(401).body("Credenciales incorrectas");
	    }
	}

	// Sección de productos
	
	/**
	 * Obtiene la lista de productos disponibles.
	 * 
	 * @return Un ArrayList de {@link ProductoDto} que representa la lista de productos.
	 * @author GPR
	 * @date 06/03/2025
	 */
	@GetMapping("/mostrarProducto")
	public ArrayList<ProductoDto> getProducto() {
	    return productoFuncionalidades.getProducto();
	}
	
	/**
	 * Guarda un nuevo producto.
	 * 
	 * @param producto Objeto {@link ProductoDto} con los datos del producto a guardar.
	 * @return El producto guardado.
	 * @author GPR
	 * @date 06/03/2025
	 */
	@PostMapping("/guardarProducto")
	public ProductoDto guardarProducto(@RequestBody ProductoDto producto) {
	    return productoFuncionalidades.guardarProducto(producto);
	}
	
	/**
	 * Modifica un producto existente.
	 * 
	 * @param idProducto El ID del producto a modificar.
	 * @param producto Objeto {@link ProductoDto} con los nuevos datos del producto.
	 * @return El producto modificado.
	 * @author GPR
	 * @date 06/03/2025
	 */
	//@CrossOrigin(origins = "http://localhost:8080")
	@PutMapping("/putProducto/{id_producto}")
	public ProductoDto putProducto(@PathVariable("id_producto") Long idProducto, @RequestBody ProductoDto producto) {
	    return productoFuncionalidades.modificarProducto(producto, idProducto);
	}
	
	/**
	 * Elimina un producto.
	 * 
	 * @param idProducto El ID del producto a eliminar.
	 * @return true si la eliminación fue exitosa; false en caso contrario.
	 * @author GPR
	 * @date 06/03/2025
	 */
	//@CrossOrigin(origins = "http://localhost:8080")
	@DeleteMapping("/deleteProducto/{idProducto}")
	public boolean deleteProducto(@PathVariable("idProducto") String idProducto) {
	    return productoFuncionalidades.eliminarProducto(idProducto);
	}	
	
	// Sección del Carrito
	
	/**
	 * Obtiene el carrito activo de un usuario.
	 * 
	 * @param usuarioId El ID del usuario.
	 * @return Respuesta HTTP con el carrito activo en formato JSON, o un error si no se encuentra o ocurre algún problema.
	 * @author GPR
	 * @date 06/03/2025
	 */
	@GetMapping("/carrito/{usuarioId}")
	public ResponseEntity<?> obtenerCarrito(@PathVariable("usuarioId") Long usuarioId) {
	    try {
	        PedidoDto carrito = carritoFuncionalidade.obtenerCarritoActivo(usuarioId);
	        if (carrito == null) {
	            return ResponseEntity.status(HttpStatus.NOT_FOUND)
	                    .body("No se encontró un carrito activo para el usuario");
	        }
	        return ResponseEntity.ok(carrito);
	    } catch (Exception e) {
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
	                .body("Error al obtener el carrito: " + e.getMessage());
	    }
	}

	/**
	 * Agrega un producto al carrito de un usuario.
	 * 
	 * @param usuarioId El ID del usuario.
	 * @param productoId El ID del producto a agregar.
	 * @param cantidad La cantidad del producto a agregar.
	 * @return Respuesta HTTP con el carrito actualizado en formato JSON, o un error si ocurre algún problema.
	 * @author GPR
	 * @date 06/03/2025
	 */
	@PostMapping("/carrito/{usuarioId}/agregar")
	public ResponseEntity<?> agregarProducto(@PathVariable("usuarioId") Long usuarioId, @RequestParam("productoId") Long productoId, @RequestParam("cantidad") Integer cantidad) {
	    try {
	        PedidoDto carrito = carritoFuncionalidade.agregarProducto(usuarioId, productoId, cantidad);
	        return ResponseEntity.ok(carrito);
	    } catch (Exception e) {
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
	                .body("Error al agregar el producto: " + e.getMessage());
	    }
	}  
	
	/**
	 * Elimina un producto del carrito de un usuario.
	 * 
	 * @param usuarioId El ID del usuario.
	 * @param detalleId El ID del detalle del producto a eliminar.
	 * @return Respuesta HTTP con el carrito actualizado en formato JSON, o un error si ocurre algún problema.
	 * @author GPR
	 * @date 06/03/2025
	 */
	@DeleteMapping("/carrito/{usuarioId}/eliminar")
	public ResponseEntity<?> eliminarProducto(
	        @PathVariable("usuarioId") Long usuarioId, @RequestParam("detalleId") Long detalleId) {
	    try {
	        PedidoDto carritoActualizado = carritoFuncionalidade.eliminarProducto(usuarioId, detalleId);
	        return ResponseEntity.ok(carritoActualizado);
	    } catch (Exception e) {
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
	                                 .body("Error al eliminar el producto: " + e.getMessage());
	    }
	}
}
