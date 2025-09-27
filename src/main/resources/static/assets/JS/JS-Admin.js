// admin.js 
let currentTheme = 'light';
document.addEventListener('DOMContentLoaded', function() {
	console.log('✅ Panel administrador inicializado');

	initializeTheme();
	setupNavigation();
	setupTabs();
	setupModals();
	setupProfileSection();
	setupAuthorityAssignment();
	loadInitialData();
});

//tema
function initializeTheme() {
	const savedTheme = localStorage.getItem('clinicpet-theme');
	if (savedTheme) {
		currentTheme = savedTheme;
	} else if (window.matchMedia && window.matchMedia('(prefers-color-scheme: dark)').matches) {
		currentTheme = 'dark';
	}

	applyTheme();
	document.getElementById('themeToggle').addEventListener('click', toggleTheme);
}

function applyTheme() {
	document.documentElement.setAttribute('data-theme', currentTheme);
	localStorage.setItem('clinicpet-theme', currentTheme);

	const themeButton = document.getElementById('themeToggle');
	if (themeButton) {
		themeButton.textContent = currentTheme === 'light' ? 'Modo Oscuro' : 'Modo Claro';
	}
}

function toggleTheme() {
	currentTheme = currentTheme === 'light' ? 'dark' : 'light';
	applyTheme();
}

//NAVEGACIÓN
function setupNavigation() {
	const navLinks = document.querySelectorAll('.nav-link');

	navLinks.forEach(link => {
		link.addEventListener('click', function(e) {
			e.preventDefault();

			// Remover active de todos los enlaces
			navLinks.forEach(l => l.classList.remove('active'));

			// Agregar clase active al enlace clickeado
			this.classList.add('active');

			// Ocultar todas las secciones
			const sections = document.querySelectorAll('.content-section');
			sections.forEach(section => section.classList.remove('active'));

			// Mostrar la sección correspondiente
			const sectionId = this.getAttribute('data-section');
			document.getElementById(sectionId).classList.add('active');
		});
	});
}

// TABS
function setupTabs() {
	const tabButtons = document.querySelectorAll('.tab-btn');

	tabButtons.forEach(button => {
		button.addEventListener('click', function() {
			const tabContainer = this.closest('.tabs-container') || this.closest('.content-section');
			const tabId = this.getAttribute('data-tab');

			if (!tabContainer) return;

			// Remover active de todos los botones de pestaña
			tabContainer.querySelectorAll('.tab-btn').forEach(btn => {
				btn.classList.remove('active');
			});

			// Agregar clase active al botón clickeado
			this.classList.add('active');

			// Ocultar todos los contenidos de pestaña
			tabContainer.querySelectorAll('.tab-content').forEach(content => {
				content.classList.remove('active');
			});

			// Mostrar el contenido de pestaña correspondiente
			const tabContent = document.getElementById(tabId);
			if (tabContent) {
				tabContent.classList.add('active');
			}
		});
	});
}

// MODALES PRINCIPALES
function setupModals() {
	setupModalBasico();
	setupFormularios();
	setupBotonesAccion();
}

function setupModalBasico() {
	// Abrir modales
	document.getElementById('editProfileBtn')?.addEventListener('click', () => abrirModal('profileModal'));
	document.getElementById('registerClinicBtn')?.addEventListener('click', () => abrirModal('clinicModal'));
	document.getElementById('registerVetBtn')?.addEventListener('click', () => abrirModal('vetModal'));

	// Cerrar modales con la X
	document.querySelectorAll('.modal .close').forEach(closeBtn => {
		closeBtn.addEventListener('click', function() {
			cerrarModal(this.closest('.modal').id);
		});
	});

	// Cerrar modales al hacer clic fuera
	window.addEventListener('click', function(e) {
		if (e.target.classList.contains('modal')) {
			cerrarModal(e.target.id);
		}
	});
}

function setupFormularios() {
	// Formulario de perfil
	document.getElementById('profileForm')?.addEventListener('submit', function(e) {
		e.preventDefault();
		actualizarPerfil(this);
	});

	// Formulario de veterinaria
	document.getElementById('clinicForm')?.addEventListener('submit', function(e) {
		e.preventDefault();
		registrarVeterinaria(this);
	});

	// Formulario de veterinario
	document.getElementById('vetForm')?.addEventListener('submit', function(e) {
		e.preventDefault();
		registrarVeterinario(this);
	});
}

//  FUNCIONES DE FORMULARIOS
function actualizarPerfil(form) {
	const formData = new FormData(form);

	fetch('/admin/perfil/editar', {
		method: 'POST',
		body: formData
	})
		.then(response => {
			if (response.ok) {
				mostrarMensaje('Perfil actualizado correctamente', 'success');
				cerrarModal('profileModal');
				// Recargar para ver cambios
				setTimeout(() => location.reload(), 1000);
			} else {
				throw new Error('Error en la respuesta del servidor');
			}
		})
		.catch(error => {
			console.error('Error:', error);
			mostrarMensaje('Error al actualizar el perfil', 'error');
		});
}

function registrarVeterinaria(form) {
	const formData = new FormData(form);

	fetch('/admin/veterinarias/registrar', {
		method: 'POST',
		body: formData
	})
		.then(response => {
			if (response.ok) {
				mostrarMensaje('Veterinaria registrada correctamente', 'success');
				cerrarModal('clinicModal');
				// Recargar la lista
				setTimeout(() => location.reload(), 1000);
			} else {
				throw new Error('Error en la respuesta del servidor');
			}
		})
		.catch(error => {
			console.error('Error:', error);
			mostrarMensaje('Error al registrar veterinaria', 'error');
		});
}

function registrarVeterinario(form) {
	const formData = new FormData(form);

	fetch('/admin/veterinarios/registrar', {
		method: 'POST',
		body: formData
	})
		.then(response => {
			if (response.ok) {
				mostrarMensaje('Veterinario registrado correctamente', 'success');
				cerrarModal('vetModal');
				// Recargar la lista
				setTimeout(() => location.reload(), 1000);
			} else {
				throw new Error('Error en la respuesta del servidor');
			}
		})
		.catch(error => {
			console.error('Error:', error);
			mostrarMensaje('Error al registrar veterinario', 'error');
		});
}

//  PERFIL DEL ADMIN 
function setupProfileSection() {
	setupFotoPerfil();
	setupBotonesPerfil();
}

function setupFotoPerfil() {
	const changePhotoBtn = document.getElementById('changePhotoBtn');
	const photoInput = document.getElementById('photoInput');

	changePhotoBtn?.addEventListener('click', () => photoInput?.click());

	photoInput?.addEventListener('change', function(e) {
		const file = e.target.files[0];
		if (file) {
			// Validar archivo
			if (!file.type.startsWith('image/')) {
				mostrarMensaje('Por favor seleccione un archivo de imagen válido', 'error');
				return;
			}

			if (file.size > 5 * 1024 * 1024) {
				mostrarMensaje('La imagen no debe superar los 5MB', 'error');
				return;
			}

			// Vista previa
			const reader = new FileReader();
			reader.onload = function(e) {
				document.querySelector('.profile-large').src = e.target.result;
				document.querySelector('.profile-pic').src = e.target.result;

				// Subir al servidor
				subirFotoAlServidor(file);
			};
			reader.readAsDataURL(file);
		}
	});
}

function subirFotoAlServidor(file) {
	const formData = new FormData();
	formData.append('foto', file);

	fetch('/admin/perfil/foto', {
		method: 'POST',
		body: formData
	})
		.then(response => {
			if (response.ok) {
				mostrarMensaje('Foto actualizada correctamente', 'success');
			} else {
				throw new Error('Error al subir foto');
			}
		})
		.catch(error => {
			console.error('Error:', error);
			mostrarMensaje('Error al subir la foto', 'error');
		});
}

function setupBotonesPerfil() {
	// Botón cambiar contraseña
	document.getElementById('changePasswordBtn')?.addEventListener('click', function() {
		const newPassword = prompt('Ingrese su nueva contraseña:');
		if (newPassword && newPassword.length >= 6) {
			// Aquí iría la lógica para cambiar la contraseña
			mostrarMensaje('Contraseña cambiada exitosamente', 'success');
		} else if (newPassword) {
			mostrarMensaje('La contraseña debe tener al menos 6 caracteres', 'error');
		}
	});
}

//BOTONES DE ACCIÓN (VER, ACTIVAR, ETC.)
function setupBotonesAccion() {
	// Botones VER
	document.addEventListener('click', function(e) {
		if (e.target.classList.contains('btn-action')) {
			const textoBoton = e.target.textContent.trim();
			const row = e.target.closest('tr');

			if (textoBoton === 'Ver') {
				const id = e.target.getAttribute('data-id');
				const tipo = e.target.getAttribute('data-tipo') || 'usuario';
				verDetalles(tipo, id);
			}
		}
	});

	// Botones ACTIVAR/DESACTIVAR
	document.addEventListener('click', function(e) {
		if (e.target.classList.contains('btn-activate') || e.target.classList.contains('btn-inactivate')) {
			const accion = e.target.classList.contains('btn-activate') ? 'activar' : 'desactivar';
			const id = e.target.getAttribute('data-id');
			const tipo = e.target.getAttribute('data-tipo') || 'usuario';

			gestionarEstado(accion, tipo, id);
		}
	});
}

// FUNCIÓN PARA VER DETALLES
function verDetalles(tipo, id) {
	fetch(`/admin/${tipo}/${id}`)
		.then(response => {
			if (!response.ok) throw new Error('Error en la respuesta');
			return response.json();
		})
		.then(data => {
			mostrarModalDetalles(tipo, data);
		})
		.catch(error => {
			console.error('Error:', error);
			mostrarMensaje('Error al cargar los detalles', 'error');
		});
}

function mostrarModalDetalles(tipo, data) {
	let titulo = '';
	let contenido = '';

	switch (tipo) {
		case 'usuario':
			titulo = 'Detalles del Usuario';
			contenido = `
                <p><strong>Nombre:</strong> ${data.nombres} ${data.apellidos}</p>
                <p><strong>Email:</strong> ${data.correo}</p>
                <p><strong>Teléfono:</strong> ${data.telefono}</p>
                <p><strong>Documento:</strong> ${data.tipoDocumento} ${data.numDocumento}</p>
                <p><strong>Dirección:</strong> ${data.direccion}</p>
                <p><strong>Estado:</strong> ${data.activo ? 'Activo' : 'Inactivo'}</p>
            `;
			break;

		case 'veterinario':
			titulo = 'Detalles del Veterinario';
			contenido = `
                <p><strong>Nombre:</strong> ${data.usuario.nombres} ${data.usuario.apellidos}</p>
                <p><strong>Email:</strong> ${data.usuario.correo}</p>
                <p><strong>Teléfono:</strong> ${data.usuario.telefono}</p>
                <p><strong>Especialidad:</strong> ${data.especialidad}</p>
                <p><strong>Tarjeta Profesional:</strong> ${data.tarjetaProfesional}</p>
                <p><strong>Experiencia:</strong> ${data.experiencia}</p>
                <p><strong>Estado:</strong> ${data.estado ? 'Activo' : 'Inactivo'}</p>
            `;
			break;

		case 'mascota':
			titulo = 'Detalles de la Mascota';
			contenido = `
                <p><strong>Nombre:</strong> ${data.nombre}</p>
                <p><strong>Especie:</strong> ${data.especie}</p>
                <p><strong>Raza:</strong> ${data.raza}</p>
                <p><strong>Edad:</strong> ${data.edad} años</p>
                <p><strong>Género:</strong> ${data.genero}</p>
                <p><strong>Estado:</strong> ${data.estado}</p>
            `;
			break;
	}

	const modalHTML = `
        <div id="detallesModal" class="modal">
            <div class="modal-content">
                <span class="close">&times;</span>
                <h2>${titulo}</h2>
                <div class="detalles-container">
                    ${contenido}
                </div>
            </div>
        </div>
    `;

	// Remover modal anterior si existe
	const modalAnterior = document.getElementById('detallesModal');
	if (modalAnterior) modalAnterior.remove();

	// Agregar nuevo modal
	document.body.insertAdjacentHTML('beforeend', modalHTML);
	abrirModal('detallesModal');

	// Configurar cerrar modal
	document.querySelector('#detallesModal .close').addEventListener('click', () => {
		cerrarModal('detallesModal');
	});
}

//GESTIÓN DE ESTADO (ACTIVAR/DESACTIVAR)
function gestionarEstado(accion, tipo, id) {
	if (!confirm(`¿Está seguro de que desea ${accion} este ${tipo}?`)) return;

	fetch(`/admin/${tipo}s/${accion}/${id}`, {
		method: 'POST'
	})
		.then(response => {
			if (response.ok) {
				mostrarMensaje(`${tipo.charAt(0).toUpperCase() + tipo.slice(1)} ${accion}do correctamente`, 'success');
				location.reload();
			} else {
				throw new Error('Error en la operación');
			}
		})
		.catch(error => {
			console.error('Error:', error);
			mostrarMensaje(`Error al ${accion} ${tipo}`, 'error');
		});
}

// ASIGNACIÓN DE REPORTES
function setupAuthorityAssignment() {
	document.addEventListener('click', function(e) {
		if (e.target.classList.contains('btn-assign')) {
			const reporteId = e.target.getAttribute('data-report-id');
			asignarReporte(reporteId);
		}
	});
}

function asignarReporte(reporteId) {
	const select = document.querySelector(`.authority-select[data-report-id="${reporteId}"]`);
	const autoridad = select?.value;

	if (!autoridad) {
		mostrarMensaje('Por favor seleccione una autoridad', 'error');
		return;
	}

	fetch(`/admin/reportes/asignar/${reporteId}?autoridad=${encodeURIComponent(autoridad)}`, {
		method: 'POST'
	})
		.then(response => {
			if (response.ok) {
				mostrarMensaje('Reporte asignado correctamente', 'success');
				location.reload();
			} else {
				throw new Error('Error al asignar reporte');
			}
		})
		.catch(error => {
			console.error('Error:', error);
			mostrarMensaje('Error al asignar reporte', 'error');
		});
}

function abrirModal(modalId) {
	const modal = document.getElementById(modalId);
	if (modal) {
		modal.style.display = 'block';
	}
}

function cerrarModal(modalId) {
	const modal = document.getElementById(modalId);
	if (modal) {
		modal.style.display = 'none';
	}
}

function mostrarMensaje(mensaje, tipo) {
	if (typeof Swal !== 'undefined') {
		Swal.fire({
			title: tipo === 'success' ? 'Éxito' : 'Error',
			text: mensaje,
			icon: tipo,
			confirmButtonText: 'Aceptar'
		});
	} else {
		alert((tipo === 'success' ? '✅ ' : '❌ ') + mensaje);
	}
}

function loadInitialData() {
	console.log("Cargando datos iniciales...");

}

// Agregar esto al JS para manejar modales largos
function ajustarAlturaModal() {
	document.querySelectorAll('.modal-content').forEach(modal => {
		const alturaVentana = window.innerHeight;
		const alturaModal = modal.scrollHeight;

		if (alturaModal > alturaVentana * 0.2) {
			modal.style.maxHeight = '80vh';
			modal.style.overflowY = 'auto';
		}
	});
}

// Ejecutar cuando se abra un modal
function abrirModal(modalId) {
	const modal = document.getElementById(modalId);
	if (modal) {
		modal.style.display = 'block';
		setTimeout(ajustarAlturaModal, 100); // Pequeño delay para que se renderice
	}
}

// También al redimensionar la ventana
window.addEventListener('resize', ajustarAlturaModal);

// Hacer funciones globales para HTML
window.abrirModal = abrirModal;
window.cerrarModal = cerrarModal;
window.verDetalles = verDetalles;