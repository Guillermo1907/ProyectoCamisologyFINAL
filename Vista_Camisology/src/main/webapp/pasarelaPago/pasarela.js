document.addEventListener("DOMContentLoaded", function() {
    const formularioPasarela = document.getElementById("form-pasarela");
    formularioPasarela.addEventListener("submit", function(evento) {
        evento.preventDefault(); 


        const numeroTarjeta = document.getElementById("cardNumber").value.trim();
        const titularTarjeta = document.getElementById("cardHolder").value.trim();
        const expiracion = document.getElementById("expiration").value.trim();
        const cvv = document.getElementById("cvv").value.trim();


        const regexNumero = /^\d{16}$/;
        const regexCVV = /^\d{3}$/;
        const regexExpiracion = /^(0[1-9]|1[0-2])\/\d{2}$/; 

        if (!regexNumero.test(numeroTarjeta) || !regexCVV.test(cvv) || !regexExpiracion.test(expiracion) || titularTarjeta === "") {
            alert("Tarjeta de crédito inválida");
            return;
        }

        const datosFormulario = new URLSearchParams();
        datosFormulario.append("cardNumber", numeroTarjeta);
        datosFormulario.append("cardHolder", titularTarjeta);
        datosFormulario.append("expiration", expiracion);
        datosFormulario.append("cvv", cvv);

        fetch("/Vista_Camisology/pasarelaPago/pasarela/validar", {
            method: "POST",
            headers: { "Content-Type": "application/x-www-form-urlencoded" },
            body: datosFormulario.toString()
        })
        .then(response => response.text())
        .then(texto => {
            try {
                const resultado = JSON.parse(texto);
                if (resultado.aceptado) {
                    mostrarModalConfirmacion();
                } else {
                    alert("Tarjeta de crédito inválida");
                }
            } catch (e) {
                console.error("Error al parsear la respuesta:", e);
                alert("Error al validar la tarjeta");
            }
        })
        .catch(error => {
            console.error("Error al validar la tarjeta:", error);
            alert("Error al validar la tarjeta");
        });
    });
});

function mostrarModalConfirmacion() {
    const modal = document.getElementById("modal-confirmacion");
    modal.style.display = "flex"; 

    setTimeout(() => {
        window.location.href = "/Vista_Camisology/index.jsp";
    }, 4000);
}

function cerrarModalConfirmacion() {
    document.getElementById("modal-confirmacion").style.display = "none";
}