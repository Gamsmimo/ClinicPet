// ===== CONFIGURACIÓN INICIAL =====
document.addEventListener('DOMContentLoaded', function() {
	console.log('✅ Panel administrador inicializado');
	initializeTheme();
	setupNavigation();
	setupModals();
	setupFormValidation();
	setupPhotoUpload();
});

// ===== TEMA =====
function initializeTheme() {
	const savedTheme = localStorage.getItem('clinicpet-theme') || 'light';
	document.documentElement.setAttribute('data-theme', savedTheme);
	const btn = document.getElementById('themeToggle');
	if (btn) {
		btn.textContent = savedTheme === 'light' ? 'Modo Oscuro' : 'Modo Claro';
		btn.addEventListener('click', toggleTheme);
	}
}

function toggleTheme() {
	const currentTheme = document.documentElement.getAttribute('data-theme');
	const newTheme = currentTheme === 'light' ? 'dark' : 'light';
	document.documentElement.setAttribute('data-theme', newTheme);
	localStorage.setItem('clinicpet-theme', newTheme);
	document.getElementById('themeToggle').textContent = newTheme === 'light' ? 'Modo Oscuro' : 'Modo Claro';
}

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
function setupModals() {
	// Botón editar perfil
	document.getElementById('editProfileBtn')?.addEventListener('click', () => abrirModal('profileModal'));

	// Cerrar modales
	document.querySelectorAll('.modal .close').forEach(closeBtn => {
		closeBtn.addEventListener('click', function() {
			const modal = this.closest('.modal');
			if (modal) modal.style.display = 'none';
		});
	});

	// Cerrar al hacer click fuera
	window.addEventListener('click', function(e) {
		if (e.target.classList.contains('modal')) {
			e.target.style.display = 'none';
		}
	});
}

// ===== FUNCIONES GLOBALES =====
function abrirModal(modalId) {
	const modal = document.getElementById(modalId);
	if (modal) modal.style.display = 'block';
}

function cerrarModal(modalId) {
	const modal = document.getElementById(modalId);
	if (modal) modal.style.display = 'none';
}

// ===== CAMBIAR FOTO DE PERFIL =====
function setupPhotoUpload() {
	const input = document.getElementById('photoInput');
	if (input) {
		input.addEventListener('change', cambiarFotoPerfil);
	}
}

function cambiarFotoPerfil(event) {
	const file = event.target.files[0];
	if (!file) return;

	// Validar tipo de archivo
	if (!file.type.startsWith('image/')) {
		mostrarMensaje('Por favor seleccione un archivo de imagen válido', 'error');
		return;
	}

	// Validar tamaño (5MB máximo)
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
			if (response.ok) {
				return response.text();
			}
			throw new Error('Error al subir imagen');
		})
		.then(() => {
			// Actualizar la vista previa
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
	// Validar formulario de veterinario
	const vetForm = document.getElementById('vetForm');
	if (vetForm) {
		vetForm.addEventListener('submit', function(e) {
			const password = document.getElementById('vetPassword')?.value;
			if (password && password.length < 6) {
				e.preventDefault();
				mostrarMensaje('La contraseña debe tener al menos 6 caracteres', 'error');
				return;
			}
			mostrarMensaje('Registrando veterinario...', 'info');
		});
	}

	// Validar formulario de perfil
	const profileForm = document.getElementById('profileForm');
	if (profileForm) {
		profileForm.addEventListener('submit', function() {
			mostrarMensaje('Actualizando perfil...', 'info');
		});
	}

	// Validar formulario de veterinaria
	const clinicForm = document.getElementById('clinicForm');
	if (clinicForm) {
		clinicForm.addEventListener('submit', function() {
			mostrarMensaje('Registrando veterinaria...', 'info');
		});
	}
}

// ===== FUNCIONES PARA GESTIÓN DE USUARIOS =====
function verDetallesUsuario(id) {
	fetch(`/admin/usuario/${id}`)
		.then(response => response.json())
		.then(usuario => {
			mostrarMensaje(`Usuario: ${usuario.nombres} ${usuario.apellidos}\nEmail: ${usuario.correo}\nRol: ${usuario.rol?.nombre || 'Sin rol'}`, 'info');
		})
		.catch(error => {
			console.error('Error:', error);
			mostrarMensaje('Error al cargar detalles del usuario', 'error');
		});
}

function cambiarEstadoUsuario(id, activar) {
	const accion = activar ? 'activar' : 'desactivar';

	fetch(`/admin/usuarios/${accion}/${id}`, {
		method: 'POST'
	})
		.then(response => {
			if (response.ok) {
				location.reload();
			} else {
				throw new Error('Error al cambiar estado');
			}
		})
		.catch(error => {
			console.error('Error:', error);
			mostrarMensaje('Error al cambiar estado del usuario', 'error');
		});
}

function verDetallesVeterinario(id) {
	mostrarMensaje(`Cargando detalles del veterinario ID: ${id}`, 'info');
}

function verDetallesVeterinaria(id) {
	mostrarMensaje(`Cargando detalles de la veterinaria ID: ${id}`, 'info');
}

// ===== FUNCIONES AUXILIARES =====
function mostrarMensaje(mensaje, tipo = 'info') {
	if (typeof Swal !== 'undefined') {
		const config = {
			text: mensaje,
			toast: true,
			position: 'top-end',
			showConfirmButton: false,
			timer: 3000,
			timerProgressBar: true
		};

		switch (tipo) {
			case 'success':
				Swal.fire({ ...config, icon: 'success', title: 'Éxito' });
				break;
			case 'error':
				Swal.fire({ ...config, icon: 'error', title: 'Error' });
				break;
			case 'warning':
				Swal.fire({ ...config, icon: 'warning', title: 'Advertencia' });
				break;
			default:
				Swal.fire({ ...config, icon: 'info', title: 'Información' });
		}
	} else {
		alert(mensaje);
	}
}

/* ---------- Abrir / cerrar modal ---------- */
function abrirModal(idModal) { document.getElementById(idModal).style.display = 'flex'; }
function cerrarModal(idModal) { document.getElementById(idModal).style.display = 'none'; }

/* ---------- Ver detalles USUARIO ---------- */
function verDetallesUsuario(id) {
	fetch('/admin/usuario/' + id)   // necesitas crear este endpoint si no existe
		.then(r => r.json())
		.then(u => {
			mostrarModal('Usuario',
				`<p><b>Nombre:</b> ${u.nombres} ${u.apellidos}</p>
             <p><b>Correo:</b> ${u.correo}</p>
             <p><b>Teléfono:</b> ${u.telefono}</p>
             <p><b>Estado:</b> ${u.activo ? 'Activo' : 'Desactivado'}</p>`);
		});
}

/* ---------- Ver detalles MASCOTA ---------- */
function verMascota(id) {
	fetch('/admin/mascota/' + id)
		.then(r => r.json())
		.then(m => {
			document.getElementById('modalTitulo').innerText = 'Datos de la mascota';
			document.getElementById('modalCuerpo').innerHTML = `
        <p><b>Nombre:</b> ${m.nombre}</p>
        <p><b>Especie:</b> ${m.especie}</p>
        <p><b>Edad:</b> ${m.edad} años</p>
        <p><b>Género:</b> ${m.genero}</p>
        <p><b>Tamaño:</b> ${m.tamano}</p>
        <p><b>Descripción:</b> ${m.descripcion || 'Sin descripción'}</p>
        <p><b>Dueño:</b> ${m.usuario.nombres} ${m.usuario.apellidos}</p>
        <img src="${m.foto || 'https://via.placeholder.com/150'}" alt="Foto" style="width:150px;border-radius:8px;">
      `;
			abrirModal('detalleModal');
		})
		.catch(error => {
			console.error('Error al cargar mascota:', error);
			alert('No se pudo cargar la información de la mascota');
		});
}

/* ---------- función auxiliar para llenar y abrir ---------- */
function mostrarModal(titulo, htmlBody) {
	document.getElementById('modalTitle').innerText = titulo;
	document.getElementById('modalBody').innerHTML = htmlBody;
	abrirModal('detalleModal');
}


/* ---------- Abrir / cerrar modal ---------- */
function abrirModal(idModal) { document.getElementById(idModal).style.display = 'flex'; }
function cerrarModal(idModal) { document.getElementById(idModal).style.display = 'none'; }

/* ---------- Ver detalles MASCOTA ---------- */
function verMascota(id) {
	fetch('/admin/mascota/' + id)
		.then(r => r.json())
		.then(m => {
			document.getElementById('modalTitulo').innerText = 'Datos de la mascota';
			document.getElementById('modalCuerpo').innerHTML = `
        <p><b>Nombre:</b> ${m.nombre}</p>
        <p><b>Especie:</b> ${m.especie}</p>
        <p><b>Edad:</b> ${m.edad} años</p>
        <p><b>Género:</b> ${m.genero}</p>
        <p><b>Tamaño:</b> ${m.tamano}</p>
        <p><b>Descripción:</b> ${m.descripcion || 'Sin descripción'}</p>
        <p><b>Dueño:</b> ${m.usuario.nombres} ${m.usuario.apellidos}</p>
        <img src="${m.foto || 'https://via.placeholder.com/150'}" alt="Foto" style="width:150px;border-radius:8px;">
      `;
			abrirModal('detalleModal');
		});
}

/* ---------- Ver detalles USUARIO ---------- */
function verUsuario(id) {
	fetch('/admin/usuario/' + id)
		.then(r => r.json())
		.then(usuario => {
			document.getElementById('modalTitulo').innerText = 'Datos del usuario';
			document.getElementById('modalCuerpo').innerHTML = `
        <p><b>Nombre:</b> ${usuario.nombres} ${usuario.apellidos}</p>
        <p><b>Correo:</b> ${usuario.correo}</p>
        <p><b>Teléfono:</b> ${usuario.telefono}</p>
        <p><b>Dirección:</b> ${usuario.direccion || 'No registrada'}</p>
        <p><b>Edad:</b> ${usuario.edad || 'No indicada'}</p>
      `;
			abrirModal('detalleModal');
		});
}

/* ---------- Ver detalles VETERINARIO ---------- */
function verVeterinario(id) {
	fetch('/admin/veterinario/' + id)
		.then(r => r.json())
		.then(v => {
			document.getElementById('modalTitulo').innerText = 'Datos del veterinario';
			document.getElementById('modalCuerpo').innerHTML = `
        <p><b>Nombre:</b> ${v.usuario.nombres} ${v.usuario.apellidos}</p>
        <p><b>Edad:</b> ${v.usuario.edad || 'No indicada'}</p>
        <p><b>Documento:</b> ${v.usuario.tipoDocumento} ${v.usuario.numDocumento}</p>
        <p><b>Dirección:</b> ${v.usuario.direccion || 'No registrada'}</p>
        <p><b>Teléfono:</b> ${v.usuario.telefono}</p>
        <p><b>Correo:</b> ${v.usuario.correo}</p>
        <p><b>Especialidad:</b> ${v.especialidad}</p>
        <p><b>Tarjeta profesional:</b> ${v.tarjetaProfesional}</p>
        <p><b>Experiencia:</b> ${v.experiencia} años</p>
        <p><b>Veterinaria:</b> ${v.veterinaria ? v.veterinaria.nombre : 'No asignada'}</p>
      `;
			abrirModal('detalleModal');
		});
}

/* ---------- Ver detalles VETERINARIA ---------- */
function verVeterinaria(id) {
	fetch('/admin/veterinaria/' + id)
		.then(r => r.json())
		.then(vet => {
			document.getElementById('modalTitulo').innerText = 'Datos de la veterinaria';
			document.getElementById('modalCuerpo').innerHTML = `
        <p><b>Nombre:</b> ${vet.nombre}</p>
        <p><b>RUT:</b> ${vet.rut}</p>
        <p><b>Dirección:</b> ${vet.direccion}</p>
        <p><b>Correo:</b> ${vet.correo}</p>
        <p><b>Horario:</b> ${vet.horario}</p>
        <p><b>Descripción:</b> ${vet.descripcion || 'Sin descripción'}</p>
        <p><b>Estado:</b> ${vet.estado}</p>
      `;
			abrirModal('detalleModal');
		});
}


// ===== HACER FUNCIONES GLOBALES =====
window.abrirModal = abrirModal;
window.cerrarModal = cerrarModal;
window.verDetallesUsuario = verDetallesUsuario;
window.verMascota = verMascota;
window.cambiarEstadoUsuario = cambiarEstadoUsuario;
window.verDetallesVeterinario = verDetallesVeterinario;
window.verDetallesVeterinaria = verDetallesVeterinaria;
window.cambiarFotoPerfil = cambiarFotoPerfil;
