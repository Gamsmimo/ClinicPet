// ===== TEMA CLARO/OSCURO CON ICONOS DE BOOTSTRAP =====
const themeToggleBtn = document.getElementById('themeToggle');
const themeIcon = document.querySelector('.theme-icon');

function getInitialTheme() {
	const saved = localStorage.getItem('clinicpet-theme');
	if (saved) return saved;
	return window.matchMedia && window.matchMedia('(prefers-color-scheme: dark)').matches
		? 'dark'
		: 'light';
}

function applyTheme(theme) {
	document.body.setAttribute('data-theme', theme);
	localStorage.setItem('clinicpet-theme', theme);

	if (theme === 'dark') {
		themeIcon.classList.remove('bi-moon-stars-fill');
		themeIcon.classList.add('bi-sun-fill');
	} else {
		themeIcon.classList.remove('bi-sun-fill');
		themeIcon.classList.add('bi-moon-stars-fill');
	}
}

themeToggleBtn?.addEventListener('click', () => {
	const current = document.body.getAttribute('data-theme');
	const newTheme = current === 'light' ? 'dark' : 'light';
	applyTheme(newTheme);
});

applyTheme(getInitialTheme());

// ===== INICIALIZACIÓN GENERAL =====
document.addEventListener('DOMContentLoaded', () => {
	console.log('✅ Panel administrador inicializado');
	setupNavigation();
	setupModals();
	setupFormValidation();
	setupPhotoUpload();
	setupAuthorityAssignment();
});

// ===== NAVEGACIÓN =====
function setupNavigation() {
	document.querySelectorAll('.nav-link').forEach(link => {
		link.addEventListener('click', function(e) {
			e.preventDefault();
			document.querySelectorAll('.nav-link').forEach(l => l.classList.remove('active'));
			this.classList.add('active');
			document.querySelectorAll('.content-section').forEach(s => s.classList.remove('active'));
			const section = this.getAttribute('data-section');
			document.getElementById(section)?.classList.add('active');
		});
	});
}

// ===== MODALES =====
function abrirModal(idModal) {
	const modal = document.getElementById(idModal);
	if (modal) modal.style.display = 'flex';
}

function cerrarModal(idModal) {
	const modal = document.getElementById(idModal);
	if (modal) modal.style.display = 'none';
}

function setupModals() {
	document.getElementById('editProfileBtn')?.addEventListener('click', () => abrirModal('profileModal'));

	document.querySelectorAll('.modal .close').forEach(closeBtn => {
		closeBtn.addEventListener('click', function() {
			cerrarModal(this.closest('.modal').id);
		});
	});

	window.addEventListener('click', function(e) {
		if (e.target.classList.contains('modal')) {
			e.target.style.display = 'none';
		}
	});
}

// ===== CARGA DE FOTO DE PERFIL =====
function setupPhotoUpload() {
	const input = document.getElementById('photoInput');
	if (input) {
		input.addEventListener('change', cambiarFotoPerfil);
	}
}

function cambiarFotoPerfil(event) {
	const file = event.target.files[0];
	if (!file) return;

	if (!file.type.startsWith('image/')) {
		mostrarMensaje('Por favor seleccione un archivo de imagen válido', 'error');
		return;
	}

	if (file.size > 5 * 1024 * 1024) {
		mostrarMensaje('La imagen debe ser menor a 5MB', 'error');
		return;
	}

	const formData = new FormData();
	formData.append('imagen', file);

	fetch('/admin/perfil/imagen', {
		method: 'POST',
		body: formData
	})
		.then(response => {
			if (response.ok) return response.text();
			throw new Error('Error al subir imagen');
		})
		.then(() => {
			const reader = new FileReader();
			reader.onload = function(e) {
				document.getElementById('adminProfilePic').src = e.target.result;
				document.getElementById('headerProfilePic').src = e.target.result;
			};
			reader.readAsDataURL(file);
			mostrarMensaje('Foto de perfil actualizada correctamente', 'success');
		})
		.catch(error => {
			console.error('Error:', error);
			mostrarMensaje('Error al subir la imagen', 'error');
		});
}

// ===== VALIDACIÓN DE FORMULARIOS =====
function setupFormValidation() {
	const vetForm = document.getElementById('vetForm');
	if (vetForm) {
		vetForm.addEventListener('submit', function(e) {
			const password = document.getElementById('vetPassword')?.value;
			if (password && password.length < 6) {
				e.preventDefault();
				mostrarMensaje('La contraseña debe tener al menos 6 caracteres', 'error');
			} else {
				mostrarMensaje('Registrando veterinario...', 'info');
			}
		});
	}

	const profileForm = document.getElementById('profileForm');
	if (profileForm) {
		profileForm.addEventListener('submit', () => mostrarMensaje('Actualizando perfil...', 'info'));
	}

	const clinicForm = document.getElementById('clinicForm');
	if (clinicForm) {
		clinicForm.addEventListener('submit', () => mostrarMensaje('Registrando veterinaria...', 'info'));
	}
}

// ===== MENSAJES AL USUARIO =====
function mostrarMensaje(mensaje, tipo = 'info') {
	if (typeof Swal !== 'undefined') {
		Swal.fire({
			text: mensaje,
			icon: tipo,
			toast: true,
			position: 'top-end',
			showConfirmButton: false,
			timer: 3000,
			timerProgressBar: true
		});
	} else {
		alert(mensaje);
	}
}

// ===== FUNCIONES DE CARGA DE DETALLES =====
function verMascota(id) {
	fetch(`/admin/mascota/${id}`)
		.then(res => res.json())
		.then(data => {
			document.getElementById('modalTitulo').innerText = 'Detalles de Mascota';
			document.getElementById('modalCuerpo').innerHTML = `
        <p><strong>Nombre:</strong> ${data.nombre}</p>
        <p><strong>Especie:</strong> ${data.especie}</p>
        <p><strong>Edad:</strong> ${data.edad}</p>
        <p><strong>Género:</strong> ${data.genero}</p>
        <p><strong>Tamaño:</strong> ${data.tamano}</p>
        <p><strong>Descripción:</strong> ${data.descripcion}</p>
        <p><strong>Dueño:</strong> ${data.usuario.nombres} ${data.usuario.apellidos}</p>
        <img src="${data.foto || 'https://via.placeholder.com/150'}" width="150" />
      `;
			abrirModal('detalleModal');
		})
		.catch(error => {
			console.error('Error al cargar mascota:', error);
			mostrarMensaje('No se pudo cargar la información de la mascota', 'error');
		});
}

function verUsuario(id) {
	fetch(`/admin/usuario/${id}`)
		.then(res => res.json())
		.then(data => {
			document.getElementById('modalTitulo').innerText = 'Detalles de Usuario';
			document.getElementById('modalCuerpo').innerHTML = `
        <p><strong>Nombre:</strong> ${data.nombres} ${data.apellidos}</p>
        <p><strong>Correo:</strong> ${data.correo}</p>
        <p><strong>Teléfono:</strong> ${data.telefono}</p>
        <p><strong>Dirección:</strong> ${data.direccion}</p>
        <p><strong>Edad:</strong> ${data.edad}</p>
      `;
			abrirModal('detalleModal');
		});
}

function verVeterinario(id) {
	fetch(`/admin/veterinario/${id}`)
		.then(res => res.json())
		.then(data => {
			document.getElementById('modalTitulo').innerText = 'Detalles de Veterinario';
			document.getElementById('modalCuerpo').innerHTML = `
        <p><strong>Nombre:</strong> ${data.usuario.nombres} ${data.usuario.apellidos}</p>
        <p><strong>Edad:</strong> ${data.usuario.edad}</p>
        <p><strong>Documento:</strong> ${data.usuario.tipoDocumento} ${data.usuario.numDocumento}</p>
        <p><strong>Dirección:</strong> ${data.usuario.direccion}</p>
        <p><strong>Teléfono:</strong> ${data.usuario.telefono}</p>
        <p><strong>Correo:</strong> ${data.usuario.correo}</p>
        <p><strong>Especialidad:</strong> ${data.especialidad}</p>
        <p><strong>Tarjeta Profesional:</strong> ${data.tarjetaProfesional}</p>
        <p><strong>Experiencia:</strong> ${data.experiencia} años</p>
      `;
			abrirModal('detalleModal');
		});
}

function verVeterinaria(id) {
	fetch(`/admin/veterinaria/${id}`)
		.then(res => res.json())
		.then(data => {
			document.getElementById('modalTitulo').innerText = 'Detalles de Veterinaria';
			document.getElementById('modalCuerpo').innerHTML = `
        <p><strong>Nombre:</strong> ${data.nombre}</p>
        <p><strong>RUT:</strong> ${data.rut}</p>
        <p><strong>Dirección:</strong> ${data.direccion}</p>
        <p><strong>Correo:</strong> ${data.correo}</p>
        <p><strong>Horario:</strong> ${data.horario}</p>
        <p><strong>Descripción:</strong> ${data.descripcion}</p>
        <p><strong>Estado:</strong> ${data.estado}</p>
      `;
			abrirModal('detalleModal');
		});
}

// ===== ASIGNACIÓN DE AUTORIDADES (REPORTES) =====
function setupAuthorityAssignment() {
	document.addEventListener('click', e => {
		if (e.target.classList.contains('btn-assign')) {
			const reportId = e.target.getAttribute('data-report-id');
			const select = document.querySelector(`.authority-select[data-report-id="${reportId}"]`);
			const selectedValue = select.value;
			const selectedText = select.options[select.selectedIndex].text;

			if (!selectedValue) return mostrarMensaje('Por favor selecciona una autoridad', 'warning');

			const row = e.target.closest('tr');
			row.querySelector('.status').textContent = 'Asignado';
			row.querySelector('.status').className = 'status assigned';
			row.querySelector('.authority-assigned').textContent = selectedText;
			row.querySelector('.authority-assigned').className = `authority-assigned ${selectedValue}`;
			row.querySelector('.authority-dropdown').innerHTML = `
                <button class="btn-action btn-reassign" data-report-id="${reportId}">Reasignar</button>
                <button class="btn-action btn-complete" data-report-id="${reportId}">Completado</button>
            `;
		}

		if (e.target.classList.contains('btn-reassign')) {
			const reportId = e.target.getAttribute('data-report-id');
			const row = e.target.closest('tr');
			row.querySelector('.status').textContent = 'Pendiente';
			row.querySelector('.status').className = 'status pending';
			row.querySelector('.authority-assigned').textContent = '-';
			row.querySelector('.authority-assigned').className = 'authority-assigned';
			row.querySelector('.authority-dropdown').innerHTML = `
                <select class="authority-select" data-report-id="${reportId}">
                    <option value="">Seleccionar autoridad...</option>
                    <option value="policia">Policía Nacional</option>
                    <option value="seguridad-animal">Seguridad Animal</option>
                    <option value="defensa-animal">Defensa Animal Municipal</option>
                    <option value="ambiental">Policía Ambiental</option>
                    <option value="fiscalia">Fiscalía</option>
                </select>
                <button class="btn-action btn-assign" data-report-id="${reportId}">Asignar</button>
            `;
		}

		if (e.target.classList.contains('btn-complete')) {
			const reportId = e.target.getAttribute('data-report-id');
			const row = e.target.closest('tr');
			row.querySelector('.status').textContent = 'Completado';
			row.querySelector('.status').className = 'status active';
			row.querySelector('.authority-dropdown').innerHTML = '<span class="status completed">Finalizado</span>';
			mostrarMensaje('Reporte marcado como completado', 'success');
		}
	});
}

// ===== EXPONER FUNCIONES AL HTML =====
window.abrirModal = abrirModal;
window.cerrarModal = cerrarModal;
window.verMascota = verMascota;
window.verUsuario = verUsuario;
window.verVeterinario = verVeterinario;
window.verVeterinaria = verVeterinaria;
window.cambiarFotoPerfil = cambiarFotoPerfil;
window.cambiarEstadoUsuario = cambiarEstadoUsuario;