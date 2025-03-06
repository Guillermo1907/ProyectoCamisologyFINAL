////////USUARIOS////////
function mostrarSeccion(idSeccion) {
    document.querySelectorAll('.seccion').forEach(seccion => seccion.classList.add('oculto'));
    document.getElementById(idSeccion).classList.remove('oculto');

    if (idSeccion === 'usuarios') {
        mostrarPestaña('añadir-usuario');
    }
    if (idSeccion === 'productos') {
        mostrarPestañaProductos('añadir-producto');
    }
}

function mostrarPestaña(idPestaña) {
    document.querySelectorAll('.tab-content').forEach(pestaña => pestaña.classList.add('oculto'));
    document.getElementById(idPestaña).classList.remove('oculto');
    if (idPestaña === 'gestion-usuario') {
        cargarUsuarios();
    }
}

window.onload = function() {
    const params = new URLSearchParams(window.location.search);
    if (params.get('registro') === 'exitoso') {
        alert('Registro con éxito');
        window.location.href = window.location.pathname;
    }
};

function cargarUsuarios() {
    const listaUsuarios = document.getElementById("lista-usuarios");
    listaUsuarios.innerHTML = ""; 

    fetch("/Vista_Camisology/registroPanel")
        .then(response => response.text())
        .then(data => {
            console.log("Respuesta recibida:", data);
            const usuarios = JSON.parse(data);
            if (!usuarios || usuarios.length === 0) {
                listaUsuarios.innerHTML = "<p>No hay usuarios registrados.</p>";
                return;
            }
            usuarios.forEach(usuario => {
                const disableDelete = (usuario.idUsuario == currentUserId) ? "disabled" : "";
                
                const tarjeta = document.createElement("div");
                tarjeta.classList.add("tarjeta");
                tarjeta.innerHTML = `
                    <span>${usuario.nombreCompleto} - ${usuario.correo} - ${usuario.movil} - ${usuario.tipoUsuario}</span>
                    <div>
                        <button class="btn btn-amarillo" onclick="abrirModalEditar(${usuario.idUsuario}, '${usuario.nombreCompleto}', '${usuario.movil}', '${usuario.correo}', '${usuario.tipoUsuario}')">Modificar</button>
                        <button class="btn btn-rojo" onclick="eliminarUsuario(${usuario.idUsuario})" ${disableDelete}>Eliminar</button>
                    </div>
                `;
                listaUsuarios.appendChild(tarjeta);
            });
        })
        .catch(error => {
            console.error("Error al cargar usuarios:", error);
            listaUsuarios.innerHTML = "<p>Error al cargar usuarios.</p>";
        });
}
function abrirModalEditar(id, nombreCompleto, movil, correo, tipoUsuario) {
    mostrarModal();
    document.getElementById('edit-id').value = id;
    document.getElementById('edit-nombre').value = nombreCompleto;
    document.getElementById('edit-movil').value = movil;
    document.getElementById('edit-correo').value = correo;
    document.getElementById('edit-tipoUsuario').value = tipoUsuario;
    document.getElementById('edit-password').value = "";
    document.getElementById('edit-foto').value = "";
}

function mostrarModal() {
    const modal = document.getElementById('modal-editar-usuario');
    modal.style.display = 'flex';
    document.body.classList.add('modal-abierto');
}

function cerrarModal() {
    const modal = document.getElementById('modal-editar-usuario');
    modal.style.display = 'none';
    document.body.classList.remove('modal-abierto');
}

document.getElementById("form-editar-usuario").addEventListener("submit", function(event) {
    event.preventDefault();

    const idUsuario = document.getElementById("edit-id").value;
    let usuarioActualizado = {
        nombreCompleto: document.getElementById("edit-nombre").value,
        movil: document.getElementById("edit-movil").value,
        correo: document.getElementById("edit-correo").value,
        tipoUsuario: document.getElementById("edit-tipoUsuario").value,
        password: document.getElementById("edit-password").value.trim()
    };

    if (usuarioActualizado.password === "") {
        delete usuarioActualizado.password;
    }

    let fotoInput = document.getElementById("edit-foto");
    if (fotoInput.files.length > 0) {
        let file = fotoInput.files[0];
        let reader = new FileReader();
        reader.onload = function(e) {
            usuarioActualizado.foto = e.target.result.split(',')[1];
            enviarPut(idUsuario, usuarioActualizado);
        };
        reader.readAsDataURL(file);
    } else {
        enviarPut(idUsuario, usuarioActualizado);
    }
});

function enviarPut(idUsuario, usuarioActualizado) {
    fetch(`/Vista_Camisology/usuario?id=${idUsuario}`, {
        method: "PUT",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify(usuarioActualizado)
    })
    .then(response => {
        if (response.ok) {
            alert("Usuario actualizado con éxito.");
            cerrarModal();
            cargarUsuarios();
        } else {
            alert("Error al actualizar el usuario.");
        }
    })
    .catch(error => {
        console.error("Error:", error);
        alert("No se pudo actualizar el usuario.");
    });
}

function eliminarUsuario(idUsuario) {
    if (idUsuario.toString() === currentUserId.toString()) {
        alert("No te puedes eliminar a ti mismo");
        return;
    }
    
    if (confirm("¿Está seguro de que desea eliminar este usuario?")) {
        fetch(`/Vista_Camisology/usuario?id=${idUsuario}`, {
            method: "DELETE"
        })
        .then(response => {
            if (response.ok) {
                alert("Usuario eliminado exitosamente.");
                cargarUsuarios();
            } else {
                alert("Error al eliminar el usuario.");
            }
        })
        .catch(error => {
            console.error("Error al eliminar el usuario:", error);
        });
    }
}

function validarFormulario() {
    const nombre = document.getElementById("nombre-completo").value.trim();
    const movil = document.getElementById("movil").value.trim();
    const correo = document.getElementById("correo").value.trim();
    const password = document.getElementById("password").value.trim();

    if (!nombre || !movil || !correo || !password) {
        alert("Por favor, complete todos los campos obligatorios.");
        return false;
    }
    return true;
}

////////PRODUCTOS////////

function mostrarPestañaProductos(idPestaña) {
    document.querySelectorAll('#productos .tab-content').forEach(pestaña => pestaña.classList.add('oculto'));
    document.getElementById(idPestaña).classList.remove('oculto');
    if (idPestaña === 'gestion-producto') {
        cargarProductos();
    }
}

function cargarProductos() {
    const listaProductos = document.getElementById("lista-productos");
    listaProductos.innerHTML = "";
    fetch("/Vista_Camisology/producto")
        .then(response => response.text())
        .then(data => {
            const productos = JSON.parse(data);
            if (!productos || productos.length === 0) {
                listaProductos.innerHTML = "<p>No hay productos registrados.</p>";
                return;
            }
            productos.forEach(producto => {
                const tarjeta = document.createElement("div");
                tarjeta.classList.add("tarjeta");
                let categoriaNombre = producto.categoria ? producto.categoria.nombre : "";
                tarjeta.innerHTML = `
                    <span>${producto.nombre} - ${producto.precio} - ${categoriaNombre}</span>
                    <div>
                        <button class="btn btn-amarillo" onclick="abrirModalEditarProducto(${producto.idProducto}, '${producto.nombre}', ${producto.precio}, '${producto.categoria ? producto.categoria.idCategoria : ""}')">Modificar</button>
                        <button class="btn btn-rojo" onclick="eliminarProducto(${producto.idProducto})">Eliminar</button>
                    </div>
                `;
                listaProductos.appendChild(tarjeta);
            });
        })
        .catch(error => {
            console.error("Error al cargar productos:", error);
            listaProductos.innerHTML = "<p>Error al cargar productos.</p>";
        });
}

document.getElementById("form-guardar-producto").addEventListener("submit", function(event) {
    event.preventDefault();
    let nombre = document.getElementById("producto-nombre").value;
    let precio = document.getElementById("producto-precio").value;
    let categoria = document.getElementById("producto-categoria").value;
    let producto = {
        nombre: nombre,
        precio: precio,
        categoria: { idCategoria: categoria }
    };
    let fotoInput = document.getElementById("producto-foto");
    if (fotoInput.files.length > 0) {
        let file = fotoInput.files[0];
        let reader = new FileReader();
        reader.onload = function(e) {
            producto.foto = e.target.result.split(',')[1];
            guardarProducto(producto);
        };
        reader.readAsDataURL(file);
    } else {
        guardarProducto(producto);
    }
});

function guardarProducto(producto) {
    fetch("/Vista_Camisology/producto", {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify(producto)
    })
    .then(response => {
        if (response.ok) {
            alert("Producto guardado con éxito.");
            document.getElementById("form-guardar-producto").reset();
            cargarProductos();
        } else {
            alert("Error al guardar el producto.");
        }
    })
    .catch(error => {
        console.error("Error al guardar producto:", error);
        alert("Error al guardar el producto.");
    });
}

function abrirModalEditarProducto(id, nombre, precio, categoria) {
    mostrarModalProducto();
    document.getElementById('edit-id-producto').value = id;
    document.getElementById('edit-nombre-producto').value = nombre;
    document.getElementById('edit-precio-producto').value = precio;
    document.getElementById('edit-categoria-producto').value = categoria;
    document.getElementById('edit-foto-producto').value = "";
}

function mostrarModalProducto() {
    const modal = document.getElementById('modal-editar-producto');
    modal.style.display = 'flex';
    document.body.classList.add('modal-abierto');
}

function cerrarModalProducto() {
    const modal = document.getElementById('modal-editar-producto');
    modal.style.display = 'none';
    document.body.classList.remove('modal-abierto');
}

document.getElementById("form-editar-producto").addEventListener("submit", function(event) {
    event.preventDefault();
    const idProducto = document.getElementById("edit-id-producto").value;
    let productoActualizado = {
        nombre: document.getElementById("edit-nombre-producto").value,
        precio: document.getElementById("edit-precio-producto").value,
        categoria: { idCategoria: document.getElementById("edit-categoria-producto").value }
    };
    let fotoInput = document.getElementById("edit-foto-producto");
    if (fotoInput.files.length > 0) {
        let file = fotoInput.files[0];
        let reader = new FileReader();
        reader.onload = function(e) {
            productoActualizado.foto = e.target.result.split(',')[1];
            enviarPutProducto(idProducto, productoActualizado);
        };
        reader.readAsDataURL(file);
    } else {
        enviarPutProducto(idProducto, productoActualizado);
    }
});

function enviarPutProducto(idProducto, productoActualizado) {
    fetch(`/Vista_Camisology/producto?id=${idProducto}`, {
        method: "PUT",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify(productoActualizado)
    })
    .then(response => {
        if (response.ok) {
            alert("Producto actualizado con éxito.");
            cerrarModalProducto();
            cargarProductos();
        } else {
            alert("Error al actualizar el producto.");
        }
    })
    .catch(error => {
        console.error("Error al actualizar el producto:", error);
        alert("No se pudo actualizar el producto.");
    });
}

function eliminarProducto(idProducto) {
    if (confirm("¿Está seguro de que desea eliminar este producto?")) {
        fetch(`/Vista_Camisology/producto?id=${idProducto}`, {
            method: "DELETE"
        })
        .then(response => {
            if (response.ok) {
                alert("Producto eliminado exitosamente.");
                cargarProductos();
            } else {
                alert("Error al eliminar el producto.");
            }
        })
        .catch(error => {
            console.error("Error al eliminar el producto:", error);
        });
    }
}
