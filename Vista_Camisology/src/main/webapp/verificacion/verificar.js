document.addEventListener("DOMContentLoaded", function() {
    function getParameterByName(name) {
        const urlParams = new URLSearchParams(window.location.search);
        return urlParams.get(name);
    }

    const verificado = getParameterByName('verificado');
    const error = getParameterByName('error');
    const correo = getParameterByName('correo');

    if (correo) {
        const correoInput = document.getElementById('correo');
        if (correoInput) {
            correoInput.value = correo;
        }
    }

    const messageElement = document.getElementById('message');

    if (verificado) {
        messageElement.innerHTML = "<p style='color: green;'>¡Código verificado con éxito! Redirigiendo...</p>";
        setTimeout(function() {
            window.location.href = '../index.jsp';
        }, 2000);
    } else if (error) {
        messageElement.innerHTML = "<p style='color: red;'>Error al verificar el código. Intenta nuevamente.</p>";
    }
});
