//////// CARRUSEL ////////
const carrusel = document.querySelector('.carrusel');
const botonIzquierdo = document.querySelector('.boton-carrusel.izquierdo');
const botonDerecho = document.querySelector('.boton-carrusel.derecho');

let posicionActual = 0;

if (botonDerecho && botonIzquierdo && carrusel) {
    botonDerecho.addEventListener('click', () => {
        const productos = document.querySelectorAll('.producto');
        if (productos.length === 0) return;
        const anchoProducto = productos[0].offsetWidth + 20; // Incluye el margen
        const maxDesplazamiento = (productos.length - 4) * anchoProducto;
        if (posicionActual < maxDesplazamiento) {
            posicionActual += anchoProducto;
            carrusel.style.transform = `translateX(-${posicionActual}px)`;
        }
    });

    botonIzquierdo.addEventListener('click', () => {
        const productos = document.querySelectorAll('.producto');
        if (productos.length === 0) return;
        const anchoProducto = productos[0].offsetWidth + 20;
        if (posicionActual > 0) {
            posicionActual -= anchoProducto;
            carrusel.style.transform = `translateX(-${posicionActual}px)`;
        }
    });
}

//////// BOTONES (Perfil, Iniciar sesión) ////////
document.addEventListener('DOMContentLoaded', () => {
    const perfilIcono = document.getElementById('perfil-icono');
    const pestanaPerfil = document.getElementById('pestana-perfil');

    if (perfilIcono && pestanaPerfil) {
        perfilIcono.addEventListener('click', () => {
            pestanaPerfil.classList.toggle('mini-pestana-activa');
        });
    }

});

if (document.getElementById("boton-iniciar-sesion") && document.getElementById("modal-iniciar-sesion")) {
    const botonIniciarSesion = document.getElementById("boton-iniciar-sesion");
    const modalIniciarSesion = document.getElementById("modal-iniciar-sesion");
    const cerrarIniciarSesion = modalIniciarSesion.querySelector(".cerrar-boton");

    botonIniciarSesion.addEventListener("click", () => {
        modalIniciarSesion.style.display = "flex";
    });

    if (cerrarIniciarSesion) {
        cerrarIniciarSesion.addEventListener("click", () => {
            modalIniciarSesion.style.display = "none";
        });
    }
}

const params = new URLSearchParams(window.location.search);
if (params.has('error')) {
    const modal = document.getElementById('modal-iniciar-sesion');
    const errorLogin = document.getElementById('error-login');
    if (modal && errorLogin) {
        modal.style.display = 'block';
        errorLogin.style.display = 'block';
    }
}
document.querySelector('.cerrar-boton')?.addEventListener('click', function() {
    const modal = document.getElementById('modal-iniciar-sesion');
    if (modal) {
        modal.style.display = 'none';
    }
    params.delete('error');
    window.history.replaceState({}, document.title, window.location.pathname);
});

//////// REGISTRO /////////
const formRegistro = document.getElementById('form-registrarse');
if (formRegistro) {
    formRegistro.addEventListener('submit', function (event) {
        const nombre = document.getElementById('nombre').value;
        const correo = document.getElementById('correo-registro').value;
        const movil = document.getElementById('movil').value;
        const password = document.getElementById('password-registro').value;
        const confirmarPassword = document.getElementById('confirmar-password').value;
        console.log(`Nombre: ${nombre}, Correo: ${correo}, Móvil: ${movil}, Password: ${password}, Confirmar: ${confirmarPassword}`);
        if (password !== confirmarPassword) {
            event.preventDefault();
            document.getElementById('error-message').textContent = "Las contraseñas no coinciden.";
        } else {
            document.getElementById('error-message').textContent = "";
        }
    });
}

if (document.getElementById("boton-registrarse") && document.getElementById("modal-registrarse")) {
    const botonRegistrarse = document.getElementById("boton-registrarse");
    const modalRegistrarse = document.getElementById("modal-registrarse");
    const cerrarRegistrarse = modalRegistrarse.querySelector(".cerrar-boton-registrarse");

    botonRegistrarse.addEventListener("click", () => {
        modalRegistrarse.style.display = "flex";
    });
    if (cerrarRegistrarse) {
        cerrarRegistrarse.addEventListener("click", () => {
            modalRegistrarse.style.display = "none";
        });
    }
}



function mostrarNombreArchivo() {
    const archivo = document.getElementById('foto').files[0];
    const nombreArchivo = archivo ? archivo.name : 'No se ha seleccionado archivo';
    document.getElementById('nombre-archivo').textContent = nombreArchivo;
}

//////// LAS MÁS VENDIDAS ////////

document.addEventListener("DOMContentLoaded", () => {
    cargarMasVendidas();
});

function cargarMasVendidas() {
    var carruselProductos = document.getElementById("carrusel-productos");
    if (!carruselProductos) {
        return;
    }
    carruselProductos.innerHTML = "";
    
    fetch("/Vista_Camisology/producto", { credentials: "include" })
    .then(function(response) {
        if (!response.ok) {
            throw new Error("Error en la respuesta, código: " + response.status);
        }
        return response.json();
    })
    .then(function(productos) {
        if (!productos || productos.length === 0) {
            carruselProductos.innerHTML = "<p>No hay productos disponibles.</p>";
            return;
        }
        productos.forEach(function(producto) {
            var imgSrc;
            if (producto.foto && producto.foto !== "No tiene imagen") {
                imgSrc = "data:image/jpeg;base64," + producto.foto;
            } else {
                imgSrc = "";
            }
            var productoDiv = document.createElement("div");
            productoDiv.classList.add("producto");
            productoDiv.innerHTML = "<div class='imagen-producto'>" +
                "<img src='" + imgSrc + "' alt='" + producto.nombre + "'>" +
                "</div>" +
                "<div class='descripcion-producto'>" +
                "<p>" + producto.nombre + "</p>" +
                "<p><strong>" + producto.precio + "€</strong></p>" +
                "</div>";
            var btnAdd = document.createElement("button");
            btnAdd.classList.add("btn-add-cart");
            btnAdd.textContent = "Añadir al carrito";
            btnAdd.onclick = function(e) {
                e.stopPropagation();
                agregarAlCarrito(producto.idProducto, producto.precio);
            };
            productoDiv.appendChild(btnAdd);
            
            carruselProductos.appendChild(productoDiv);
        });
    })
    .catch(function() {
        carruselProductos.innerHTML = "<p>Error al cargar productos.</p>";
    });
}

function agregarAlCarrito(productoId, precio) {
    let formData = new URLSearchParams();
    formData.append("accion", "agregar");
    formData.append("productoId", productoId);
    formData.append("cantidad", "1");

    fetch("carrito", {
        method: "POST",
        body: formData,
        credentials: "include"
    })
    .then(response => {
        if (response.status === 401) {
            alert("Debes iniciar sesión para agregar productos al carrito.");
            throw new Error("Usuario no autenticado");
        }
        return response.json();
    })
    .then(data => {
        alert("Producto añadido al carrito");
    })
    .catch(error => console.error("Error al agregar producto:", error));
}

//////// CARRITO ////////
document.getElementById("carrito-icono").addEventListener("click", function() {
    cargarCarrito();
});

function cargarCarrito() {
    fetch("carrito", { credentials: "include" }) 
        .then(response => {
            if (response.status === 401) { 
                alert("Inicia sesión primero");
                throw new Error("Debes iniciar sesión para ver el carrito.");
            }
            return response.json();
        })
        .then(data => {
            let modalContent = '<div class="contenido-modal-carrito">';
            modalContent += '<span class="cerrar-boton" onclick="cerrarModalCarrito()">&times;</span>';
            modalContent += '<h2>Tu Carrito</h2>';
            
            if (data.error) {
                modalContent += `<p>${data.error}</p>`;
            } else if (data.idPedido) {
                modalContent += `<p>Pedido: ${data.idPedido}</p>`;
                modalContent += `<p>Total: ${data.montoTotal} €</p>`;
                modalContent += '<button onclick="procederCompra()" style="background-color:#005d1e; color:white; border:none; padding:10px 20px; border-radius:5px; cursor:pointer;">Proceder con la compra</button>';
            } else {
                modalContent += `<p>El carrito está vacío.</p>`;
            }
            modalContent += '</div>';
            
            let modal = document.getElementById("modal-carrito");
            modal.innerHTML = modalContent;
            modal.style.display = "block";
        })
        .catch(error => console.error("Error al cargar el carrito:", error));
}

function cerrarModalCarrito() {
    document.getElementById("modal-carrito").style.display = "none";
}

function procederCompra() {
        window.location.href = "pasarelaPago/pasarela.jsp";
}

//////// DIFERENTES LIGAS //////

document.addEventListener("DOMContentLoaded", function() {
    var cuadros = document.querySelectorAll(".cuadro");
    cuadros.forEach(function(cuadro) {
        cuadro.style.cursor = "pointer";
        cuadro.addEventListener("click", function() {
            var urlDestino = cuadro.getAttribute("data-url");
            if (urlDestino) {
                window.location.href = urlDestino;
            }
        });
    });
});






