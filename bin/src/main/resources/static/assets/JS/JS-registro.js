const modal = document.getElementById('termsModal');
        const openBtn = document.getElementById('open-terms');
        const closeBtn = document.querySelector('.close');

        openBtn.addEventListener('click', (e) => {
            e.preventDefault();
            modal.style.display = 'flex';
        });

        closeBtn.addEventListener('click', () => {
            modal.style.display = 'none';
        });

        window.addEventListener('click', (e) => {
            if (e.target === modal) {
                modal.style.display = 'none';
            }
        });

        const btnRegistrar = document.getElementById('btn-registrar');
        const checkTerminos = document.getElementById('acepto');

        btnRegistrar.addEventListener('click', () => {
            if (!checkTerminos.checked) {
                alert('Debes aceptar los Términos y Condiciones para continuar.');
                return;
            }
            window.location.href = 'login deep.html';
        });

        document.querySelector('.btn-google').addEventListener('click', (e) => {
            e.preventDefault();
            alert('Aquí se conectaría con Google');
        });

        document.querySelector('.btn-facebook').addEventListener('click', (e) => {
            e.preventDefault();
            alert('Aquí se conectaría con Facebook');
        });