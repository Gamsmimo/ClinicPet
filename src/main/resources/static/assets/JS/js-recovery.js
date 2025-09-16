 // El algoritmo JavaScript permanece EXACTAMENTE igual
        document.getElementById('recoveryForm').addEventListener('submit', function (e) {
            e.preventDefault();

            const email = document.getElementById('email').value.trim();
            const messageEl = document.getElementById('message');

            // Validación simple del email
            if (!isValidEmail(email)) {
                showMessage('Por favor ingresa un correo electrónico válido.', 'error');
                return;
            }

            // Aquí normalmente harías una petición al servidor
            // Simulamos una respuesta exitosa después de 1 segundo
            showMessage('Enviando solicitud...', 'success');

            setTimeout(() => {
                showMessage(`Se ha enviado un enlace de recuperación a ${email}. Por favor revisa tu bandeja de entrada.`, 'success');
                document.getElementById('email').value = '';
            }, 1000);
        });

        function isValidEmail(email) {
            // Expresión regular simple para validar email
            const re = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
            return re.test(email);
        }

        function showMessage(text, type) {
            const messageEl = document.getElementById('message');
            messageEl.textContent = text;
            messageEl.className = 'message ' + type;
            messageEl.style.display = 'block';

            // Ocultar el mensaje después de 5 segundos
            if (type === 'success') {
                setTimeout(() => {
                    messageEl.style.display = 'none';
                }, 5000);
            }
        }