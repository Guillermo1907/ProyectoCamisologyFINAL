
////////BOTONES////////

document.addEventListener('DOMContentLoaded', () => {
    const perfilIcono = document.getElementById('perfil-icono');
    const pestanaPerfil = document.getElementById('pestana-perfil');

    perfilIcono.addEventListener('click', () => {
        pestanaPerfil.classList.toggle('mini-pestana-activa');
    });

    // Cierra la mini pestaña si haces clic fuera
    document.addEventListener('click', (e) => {
        if (!e.target.closest('#perfil-icono') && !e.target.closest('#pestana-perfil')) {
            pestanaPerfil.classList.remove('mini-pestana-activa');
        }
    });
});


//////// INICIAR SESION ////////

const botonIniciarSesion = document.getElementById("boton-iniciar-sesion");
const modalIniciarSesion = document.getElementById("modal-iniciar-sesion");
const cerrarIniciarSesion = modalIniciarSesion.querySelector(".cerrar-boton");

botonIniciarSesion.addEventListener("click", () => {
    modalIniciarSesion.style.display = "flex";
});

cerrarIniciarSesion.addEventListener("click", () => {
    modalIniciarSesion.style.display = "none";
});

const params = new URLSearchParams(window.location.search);
 if (params.has('error')) {
     const modal = document.getElementById('modal-iniciar-sesion');
     const errorLogin = document.getElementById('error-login');

     // Mostrar el modal y el mensaje de error
     modal.style.display = 'block';
     errorLogin.style.display = 'block';
 }

 // Manejar el cierre del modal
 document.querySelector('.cerrar-boton').addEventListener('click', function() {
     const modal = document.getElementById('modal-iniciar-sesion');
     modal.style.display = 'none';

     // Limpiar el parámetro de error de la URL
     const params = new URLSearchParams(window.location.search);
     params.delete('error');
     window.history.replaceState({}, document.title, window.location.pathname);
 });



////////REGISTRO////////


// Validacion de contraseña
document.getElementById('form-registrarse').addEventListener('submit', function (event) {
    const nombre = document.getElementById('nombre').value;
    const correo = document.getElementById('correo-registro').value;  // Asegúrate de que se use "correo-registro"
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


const botonRegistrarse = document.getElementById("boton-registrarse");
const modalRegistrarse = document.getElementById("modal-registrarse");
const cerrarRegistrarse = modalRegistrarse.querySelector(".cerrar-boton-registrarse");


botonRegistrarse.addEventListener("click", () => {
    modalRegistrarse.style.display = "flex";
});


cerrarRegistrarse.addEventListener("click", () => {
    modalRegistrarse.style.display = "none";
});

function mostrarNombreArchivo() {
       const archivo = document.getElementById('foto').files[0];
       const nombreArchivo = archivo ? archivo.name : 'No se ha seleccionado archivo';
       document.getElementById('nombre-archivo').textContent = nombreArchivo;
   }


//CARRITO 

////////CARRUSEL////////
const carrusel = document.querySelector('.carrusel');
const botonIzquierdo = document.querySelector('.boton-carrusel.izquierdo');
const botonDerecho = document.querySelector('.boton-carrusel.derecho');

let posicionActual = 0;

botonDerecho.addEventListener('click', () => {
    const productos = document.querySelectorAll('.producto');
    const anchoProducto = productos[0].offsetWidth + 20; // Incluye el margen
    const maxDesplazamiento = (productos.length - 4) * anchoProducto;

    if (posicionActual < maxDesplazamiento) {
        posicionActual += anchoProducto;
        carrusel.style.transform = `translateX(-${posicionActual}px)`;
    }
});

botonIzquierdo.addEventListener('click', () => {
    const productos = document.querySelectorAll('.producto');
    const anchoProducto = productos[0].offsetWidth + 20;

    if (posicionActual > 0) {
        posicionActual -= anchoProducto;
        carrusel.style.transform = `translateX(-${posicionActual}px)`;
    }
});


////////BOTONES////////

document.addEventListener('DOMContentLoaded', () => {
    const perfilIcono = document.getElementById('perfil-icono');
    const pestanaPerfil = document.getElementById('pestana-perfil');

    perfilIcono.addEventListener('click', () => {
        pestanaPerfil.classList.toggle('mini-pestana-activa');
    });

    // Cierra la mini pestaña si haces clic fuera
    document.addEventListener('click', (e) => {
        if (!e.target.closest('#perfil-icono') && !e.target.closest('#pestana-perfil')) {
            pestanaPerfil.classList.remove('mini-pestana-activa');
        }
    });
});


//////// INICIAR SESION ////////

const botonIniciarSesion = document.getElementById("boton-iniciar-sesion");
const modalIniciarSesion = document.getElementById("modal-iniciar-sesion");
const cerrarIniciarSesion = modalIniciarSesion.querySelector(".cerrar-boton");

botonIniciarSesion.addEventListener("click", () => {
    modalIniciarSesion.style.display = "flex";
});

cerrarIniciarSesion.addEventListener("click", () => {
    modalIniciarSesion.style.display = "none";
});

const params = new URLSearchParams(window.location.search);
 if (params.has('error')) {
     const modal = document.getElementById('modal-iniciar-sesion');
     const errorLogin = document.getElementById('error-login');

     // Mostrar el modal y el mensaje de error
     modal.style.display = 'block';
     errorLogin.style.display = 'block';
 }

 // Manejar el cierre del modal
 document.querySelector('.cerrar-boton').addEventListener('click', function() {
     const modal = document.getElementById('modal-iniciar-sesion');
     modal.style.display = 'none';

     // Limpiar el parámetro de error de la URL
     const params = new URLSearchParams(window.location.search);
     params.delete('error');
     window.history.replaceState({}, document.title, window.location.pathname);
 });



////////REGISTRO////////


// Validacion de contraseña
document.getElementById('form-registrarse').addEventListener('submit', function (event) {
    const nombre = document.getElementById('nombre').value;
    const correo = document.getElementById('correo-registro').value;  // Asegúrate de que se use "correo-registro"
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


const botonRegistrarse = document.getElementById("boton-registrarse");
const modalRegistrarse = document.getElementById("modal-registrarse");
const cerrarRegistrarse = modalRegistrarse.querySelector(".cerrar-boton-registrarse");


botonRegistrarse.addEventListener("click", () => {
    modalRegistrarse.style.display = "flex";
});


cerrarRegistrarse.addEventListener("click", () => {
    modalRegistrarse.style.display = "none";
});

function mostrarNombreArchivo() {
       const archivo = document.getElementById('foto').files[0];
       const nombreArchivo = archivo ? archivo.name : 'No se ha seleccionado archivo';
       document.getElementById('nombre-archivo').textContent = nombreArchivo;
   }


//CARRITO 
