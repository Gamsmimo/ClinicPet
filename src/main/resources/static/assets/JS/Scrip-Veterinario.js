// Toggle del menú lateral en móviles
const btnMenu = document.querySelector('.btn-menu');
const btnCloseMenu = document.querySelector('.btn-close-menu');
const sidebar = document.querySelector('.sidebar');
const mainContent = document.querySelector('.main-content');

btnMenu.addEventListener('click', () => {
	sidebar.classList.add('active');
});


// Navegación entre secciones
const menuItems = document.querySelectorAll('.menu-items a');
const contentSections = document.querySelectorAll('.content-section');

menuItems.forEach(item => {
	item.addEventListener('click', (e) => {
		e.preventDefault();

		// Remover clase active de todos los items del menú
		menuItems.forEach(i => i.classList.remove('active'));

		// Agregar clase active al item clickeado
		item.classList.add('active');

		// Ocultar todas las secciones de contenido
		contentSections.forEach(section => section.classList.remove('active'));

		// Mostrar la sección correspondiente
		const target = item.getAttribute('href');
		document.querySelector(target).classList.add('active');

		// Cerrar menú en móviles
		sidebar.classList.remove('active');
	});
});

// Filtros de la agenda
const filterButtons = document.querySelectorAll('.btn-filter');

filterButtons.forEach(button => {
	button.addEventListener('click', () => {
		filterButtons.forEach(btn => btn.classList.remove('active'));
		button.classList.add('active');

		const filter = button.getAttribute('data-filter');
		filterAppointments(filter);
	});
});

function filterAppointments(filter) {
	const rows = document.querySelectorAll('#agenda tbody tr');

	rows.forEach(row => {
		if (filter === 'all') {
			row.style.display = '';
		} else {
			const status = row.getAttribute('data-status');
			row.style.display = status === filter ? '' : 'none';
		}
	});
}

// Tabs en historias clínicas
const tabButtons = document.querySelectorAll('.tab-btn');
const tabContents = document.querySelectorAll('.tab-content');

tabButtons.forEach(button => {
	button.addEventListener('click', () => {
		const tabId = button.getAttribute('data-tab');

		// Remover clase active de todos los botones y contenidos
		tabButtons.forEach(btn => btn.classList.remove('active'));
		tabContents.forEach(content => content.classList.remove('active'));

		// Agregar clase active al botón y contenido clickeados
		button.classList.add('active');
		document.getElementById(`${tabId}-tab`).classList.add('active');
	});
});

// Selección de mascota en historias clínicas
const petItems = document.querySelectorAll('.pet-item');

petItems.forEach(item => {
	item.addEventListener('click', () => {
		petItems.forEach(i => i.classList.remove('active'));
		item.classList.add('active');

		// Aquí podrías cargar la información de la mascota seleccionada
		const petId = item.getAttribute('data-pet-id');
		loadPetInfo(petId);
	});
});

function loadPetInfo(petId) {
	// Simulación de carga de datos
	console.log(`Cargando información de la mascota ${petId}`);
	// En una aplicación real, harías una petición AJAX aquí
}





// Funciones para los modales
function openModal(modalId) {
	const modal = document.getElementById(`${modalId}-modal`);
	const overlay = document.getElementById('modal-overlay');

	overlay.classList.add('active');
	modal.classList.add('active');
	document.body.style.overflow = 'hidden';
}

function closeModal() {
	const modals = document.querySelectorAll('.modal');
	const overlay = document.getElementById('modal-overlay');

	modals.forEach(modal => modal.classList.remove('active'));
	overlay.classList.remove('active');
	document.body.style.overflow = '';
}

// Cargar datos en modal de edición de producto
function openEditProductModal(productId) {
	// Obtener ID de veterinaria desde el div oculto (si existe)
	const vetDataDiv = document.getElementById('veterinaria-data');
	const veterinariaId = vetDataDiv ? vetDataDiv.getAttribute('data-id') : null;

	fetch(`/perfil-veterinario/producto/datos/${productId}`)
		.then(response => response.json())
		.then(data => {
			if (data.error) {
				console.error(data.error);
				alert(data.error);
				return;
			}

			// Referencias a los campos del formulario
			const nombreInput = document.getElementById('edit-nombre');
			const precioInput = document.getElementById('edit-precio');
			const cantidadInput = document.getElementById('edit-cantidadDisponible');
			const categoriaSelect = document.getElementById('edit-categoria');
			const descripcionInput = document.getElementById('edit-descripcion');
			const imagenPreview = document.getElementById('preview-imagen-actual');
			const formEditar = document.getElementById('form-producto-editar');
			const inputVet = formEditar.querySelector('input[name="idveterinaria"]');

			// Cargar valores en el formulario
			nombreInput.value = data.nombre || '';
			nombreInput.readOnly = false; // Permitir editar el nombre

			precioInput.value = data.precio != null ? data.precio : '';

			cantidadInput.value = data.cantidadDisponible != null ? data.cantidadDisponible : '';

			categoriaSelect.value = data.categoria || '';
			categoriaSelect.disabled = false; // Permitir editar la categoría

			descripcionInput.value = data.descripcion || '';

			// Imagen actual
			if (data.imagen) {
				imagenPreview.src = data.imagen;
				imagenPreview.style.display = 'block';
			} else {
				imagenPreview.src = '';
				imagenPreview.style.display = 'none';
			}

			// Ajustar action del formulario para incluir el ID de producto
			formEditar.action = `/perfil-veterinario/producto/actualizar/${productId}`;

			// Ajustar idveterinaria
			if (inputVet && veterinariaId) {
				inputVet.value = veterinariaId;
			}

			// Abrir modal reutilizando la función existente
			openModal('edit-product');
		})
		.catch(error => {
			console.error('Error al cargar datos del producto:', error);
			alert('Error al cargar datos del producto');
		});
}

// Cerrar modal al hacer clic fuera
document.getElementById('modal-overlay').addEventListener('click', closeModal);

// Inicializar gráficos (usando Chart.js)
function initCharts() {
	// Gráfico de citas por mes
	const appointmentsCtx = document.getElementById('appointments-chart');
	if (appointmentsCtx) {
		new Chart(appointmentsCtx, {
			type: 'bar',
			data: {
				labels: ['Ene', 'Feb', 'Mar', 'Abr', 'May', 'Jun'],
				datasets: [{
					label: 'Citas',
					data: [45, 60, 55, 70, 65, 80],
					backgroundColor: 'rgba(74, 111, 165, 0.7)',
					borderColor: 'rgba(74, 111, 165, 1)',
					borderWidth: 1
				}]
			},
			options: {
				responsive: true,
				maintainAspectRatio: false,
				scales: {
					y: {
						beginAtZero: true
					}
				}
			}
		});
	}

	// Gráfico de tipos de consulta
	const consultationsCtx = document.getElementById('consultations-chart');
	if (consultationsCtx) {
		new Chart(consultationsCtx, {
			type: 'doughnut',
			data: {
				labels: ['Control', 'Vacunación', 'Urgencia', 'Cirugía', 'Otros'],
				datasets: [{
					data: [35, 25, 15, 10, 15],
					backgroundColor: [
						'rgba(74, 111, 165, 0.7)',
						'rgba(92, 184, 92, 0.7)',
						'rgba(240, 173, 78, 0.7)',
						'rgba(217, 83, 79, 0.7)',
						'rgba(91, 192, 222, 0.7)'
					],
					borderWidth: 1
				}]
			},
			options: {
				responsive: true,
				maintainAspectRatio: false
			}
		});
	}

	// Gráfico de ventas por categoría
	const salesCtx = document.getElementById('sales-chart');
	if (salesCtx) {
		new Chart(salesCtx, {
			type: 'pie',
			data: {
				labels: ['Alimentos', 'Medicamentos', 'Juguetes', 'Accesorios'],
				datasets: [{
					data: [45, 30, 15, 10],
					backgroundColor: [
						'rgba(74, 111, 165, 0.7)',
						'rgba(92, 184, 92, 0.7)',
						'rgba(240, 173, 78, 0.7)',
						'rgba(91, 192, 222, 0.7)'
					],
					borderWidth: 1
				}]
			},
			options: {
				responsive: true,
				maintainAspectRatio: false
			}
		});
	}
}

// Inicializar la aplicación
document.addEventListener('DOMContentLoaded', () => {
	// Mostrar la sección de inicio por defecto
	document.querySelector('#inicio').classList.add('active');

	// Inicializar gráficos
	initCharts();

	// Simular carga de datos
	setTimeout(() => {
		document.querySelector('.vet-name').textContent = 'Dr. Pérez';
	}, 500);

	// Asignar idveterinaria al formulario de AGREGAR producto desde el div oculto
	const vetDataDiv = document.getElementById('veterinaria-data');
	const veterinariaId = vetDataDiv ? vetDataDiv.getAttribute('data-id') : null;
	const formAgregar = document.getElementById('form-producto');
	if (formAgregar) {
		const inputVetAdd = formAgregar.querySelector('input[name="idveterinaria"]');
		if (inputVetAdd && veterinariaId) {
			inputVetAdd.value = veterinariaId;
		}
	}

	// Filtros dinámicos de Pet Shop
	const filtroCategoria = document.getElementById('filtro-categoria');
	const filtroEstado = document.getElementById('filtro-estado');
	if (filtroCategoria && filtroEstado) {
		const aplicarFiltros = () => {
			const categoria = filtroCategoria.value;
			const estado = filtroEstado.value;
			const params = new URLSearchParams();
			if (categoria) params.append('categoria', categoria);
			if (estado) params.append('estado', estado);
			
			fetch(`/perfil-veterinario/productos/filtrar?${params.toString()}`)
				.then(res => res.json())
				.then(data => {
					if (data.error) {
						console.error(data.error);
						return;
					}
					const grid = document.querySelector('.product-grid');
					if (!grid) return;
					
					grid.innerHTML = '';
					(data.productos || []).forEach(p => {
						const estadoInv = (p.estado || '').toLowerCase();
						const badgeClass = estadoInv === 'disponible' ? 'product-badge featured' : 'product-badge out-of-stock';
						const estadoTexto = p.estado || 'No disponible';
						const cantidadTexto = (p.cantidadDisponible != null ? p.cantidadDisponible : 0) + ' unidades';
						const precioFormateado = new Intl.NumberFormat('es-CO', { style: 'currency', currency: 'COP', maximumFractionDigits: 0 }).format(p.precio || 0);
						
						const card = document.createElement('div');
						card.className = 'product-card';
						card.innerHTML = `
							<div class="${badgeClass}">${estadoTexto}</div>
							<div class="product-image">
								<img src="${p.imagen || ''}" alt="${p.nombre || ''}">
							</div>
							<div class="product-info">
								<h5>${p.nombre || ''}</h5>
								<p class="product-description">${p.descripcion || ''}</p>
								<div class="product-meta">
									<span class="price">${precioFormateado}</span>
									<span class="stock">
										<i class="fas fa-box"></i>
										<span>${cantidadTexto}</span>
									</span>
								</div>
								<div class="product-actions">
									<button class="btn btn-sm btn-primary" onclick="openEditProductModal(${p.id})"><i class="fas fa-edit"></i></button>
									<form action="/perfil-veterinario/producto/eliminar/${p.id}" method="post" style="display:inline;">
										<button type="submit" class="btn btn-sm btn-danger" onclick="return confirm('¿Seguro que deseas eliminar este producto?');">
											<i class="fas fa-trash"></i>
										</button>
									</form>
								</div>
							</div>
						`;
						grid.appendChild(card);
					});
				})
				.catch(err => console.error('Error al aplicar filtros:', err));
		};
		
		filtroCategoria.addEventListener('change', aplicarFiltros);
		filtroEstado.addEventListener('change', aplicarFiltros);
	}
});

// Funciones para las citas
function startAppointment(appointmentId) {
	console.log(`Iniciando cita ${appointmentId}`);
	// Aquí actualizarías el estado de la cita en la UI y posiblemente en el backend
	alert(`Cita ${appointmentId} iniciada`);
}

function completeAppointment(appointmentId) {
	console.log(`Completando cita ${appointmentId}`);
	// Aquí actualizarías el estado de la cita en la UI y posiblemente en el backend
	alert(`Cita ${appointmentId} completada`);
}

// ===== MODO OSCURO/CLARO =====
(function() {
	// Esperar a que el DOM esté listo
	if (document.readyState === 'loading') {
		document.addEventListener('DOMContentLoaded', initTheme);
	} else {
		initTheme();
	}

	function initTheme() {
		const themeToggle = document.getElementById('themeToggle');
		const htmlElement = document.documentElement;

		if (!themeToggle) {
			console.error('Toggle de tema no encontrado');
			return;
		}

		// Cargar tema guardado o usar tema claro por defecto
		const savedTheme = localStorage.getItem('veterinario-theme') || 'light';
		htmlElement.setAttribute('data-theme', savedTheme);
		themeToggle.checked = savedTheme === 'dark';

		// Escuchar cambios en el toggle
		themeToggle.addEventListener('change', function() {
			if (this.checked) {
				htmlElement.setAttribute('data-theme', 'dark');
				localStorage.setItem('veterinario-theme', 'dark');
				console.log('Tema oscuro activado');
			} else {
				htmlElement.setAttribute('data-theme', 'light');
				localStorage.setItem('veterinario-theme', 'light');
				console.log('Tema claro activado');
			}
		});

		console.log('Sistema de temas inicializado correctamente');
	}
})();


/* ========== HISTORIA CLÍNICA - JAVASCRIPT ========== */

// Variables globales
let hcRecords = [createEmptyHCRecord()];
let hcCurrentIdx = 0;
let hcIsEditing = false;

// Elementos del DOM
const hcEditBtn = document.getElementById('hcEditBtn');
const hcSaveBtn = document.getElementById('hcSaveBtn');
const hcNewBtn = document.getElementById('hcNewBtn');
const hcPrintBtn = document.getElementById('hcPrintBtn');
const hcPrevBtn = document.getElementById('hcPrevBtn');
const hcNextBtn = document.getElementById('hcNextBtn');
const hcPhotoContainer = document.getElementById('hcPetPhotoContainer');
const hcDropZone = document.getElementById('hcDropZone');
const hcFileInput = document.getElementById('hcFileInput');
const hcAttachmentPreview = document.getElementById('hcAttachmentPreview');

// Crear registro vacío
function createEmptyHCRecord() {
    return {
        fields: Array(16).fill(''),
        photo: null,
        attachments: [],
        date: new Date().toISOString().split('T')[0]
    };
}

// Guardar registro actual
function saveCurrentHCRecord() {
    const inputs = document.querySelectorAll('.hc-editable');
    const fieldsArray = Array.from(inputs).slice(0, -1).map(input => input.value);
    hcRecords[hcCurrentIdx].fields = fieldsArray;
    hcRecords[hcCurrentIdx].date = document.getElementById('hcRegistrationDate').value;
}

// Cargar registro
function loadHCRecord(idx) {
    const record = hcRecords[idx];
    const inputs = document.querySelectorAll('.hc-editable');
    
    record.fields.forEach((value, i) => {
        if (inputs[i]) {
            inputs[i].value = value;
        }
    });
    
    document.getElementById('hcRegistrationDate').value = record.date;
    document.getElementById('hcNumber').textContent = String(idx + 1).padStart(3, '0');
    
    // Foto de mascota
    if (record.photo) {
        hcPhotoContainer.innerHTML = `<img src="${record.photo}">`;
    } else {
        hcPhotoContainer.innerHTML = '<i class="fa-solid fa-camera"></i>';
    }
    hcPhotoContainer.innerHTML += '<input type="file" id="hcPhotoInput" accept="image/*" style="display:none">';
    
    // Anexos
    hcAttachmentPreview.innerHTML = '';
    record.attachments.forEach((src, i) => {
        const div = document.createElement('div');
        div.className = 'hc-attachment-item';
        div.innerHTML = `
            <img src="${src}">
            <button class="hc-remove" data-idx="${i}"><i class="fa-solid fa-xmark"></i></button>
        `;
        hcAttachmentPreview.appendChild(div);
    });
    
    updateHCPagination();
}

// Actualizar paginación
function updateHCPagination() {
    document.getElementById('hcCurrentPage').textContent = hcCurrentIdx + 1;
    document.getElementById('hcTotalPages').textContent = hcRecords.length;
    
    hcPrevBtn.disabled = hcCurrentIdx === 0;
    hcNextBtn.disabled = hcCurrentIdx === hcRecords.length - 1;
    
    // Dots de paginación
    const dotsContainer = document.getElementById('hcPageDots');
    dotsContainer.innerHTML = '';
    
    hcRecords.forEach((_, i) => {
        const dot = document.createElement('div');
        dot.className = 'hc-dot' + (i === hcCurrentIdx ? ' active' : '');
        dot.onclick = () => goToHCPage(i);
        dotsContainer.appendChild(dot);
    });
}

// Ir a página específica
function goToHCPage(idx) {
    if (hcIsEditing) {
        swal("Atención", "Guarde los cambios antes de cambiar de página", "warning");
        return;
    }
    saveCurrentHCRecord();
    hcCurrentIdx = idx;
    loadHCRecord(idx);
}

// Event Listeners
hcEditBtn.addEventListener('click', function() {
    hcIsEditing = true;
    document.querySelectorAll('.hc-editable').forEach(el => el.disabled = false);
    hcEditBtn.style.display = 'none';
    hcSaveBtn.style.display = 'flex';
    swal("Modo Edición", "Puede modificar la historia clínica", "info");
});

hcSaveBtn.addEventListener('click', function() {
    hcIsEditing = false;
    document.querySelectorAll('.hc-editable').forEach(el => el.disabled = true);
    hcSaveBtn.style.display = 'none';
    hcEditBtn.style.display = 'flex';
    saveCurrentHCRecord();
    swal("¡Guardado!", "Historia clínica guardada correctamente", "success");
});

hcNewBtn.addEventListener('click', function() {
    if (hcIsEditing) {
        swal("Atención", "Guarde los cambios primero", "warning");
        return;
    }
    saveCurrentHCRecord();
    hcRecords.push(createEmptyHCRecord());
    hcCurrentIdx = hcRecords.length - 1;
    loadHCRecord(hcCurrentIdx);
    swal("Nueva Historia", "Se ha creado una nueva historia clínica", "success");
});

hcPrintBtn.addEventListener('click', function() {
    window.print();
});

hcPrevBtn.addEventListener('click', function() {
    goToHCPage(hcCurrentIdx - 1);
});

hcNextBtn.addEventListener('click', function() {
    goToHCPage(hcCurrentIdx + 1);
});

// Foto de mascota
hcPhotoContainer.addEventListener('click', function() {
    const photoInput = document.getElementById('hcPhotoInput');
    if (photoInput && hcIsEditing) {
        photoInput.click();
    }
});

document.addEventListener('change', function(e) {
    if (e.target.id === 'hcPhotoInput' && e.target.files[0]) {
        const reader = new FileReader();
        reader.onload = function(ev) {
            hcRecords[hcCurrentIdx].photo = ev.target.result;
            hcPhotoContainer.innerHTML = `
                <img src="${ev.target.result}">
                <input type="file" id="hcPhotoInput" accept="image/*" style="display:none">
            `;
        };
        reader.readAsDataURL(e.target.files[0]);
    }
});

// Drag and drop para anexos
hcDropZone.addEventListener('click', function() {
    hcFileInput.click();
});

hcDropZone.addEventListener('dragover', function(e) {
    e.preventDefault();
    hcDropZone.style.borderColor = 'var(--hc-teal)';
    hcDropZone.style.background = 'var(--hc-mint)';
});

hcDropZone.addEventListener('dragleave', function() {
    hcDropZone.style.borderColor = 'var(--hc-mint-light)';
    hcDropZone.style.background = '#f8fcfb';
});

hcDropZone.addEventListener('drop', function(e) {
    e.preventDefault();
    hcDropZone.style.borderColor = 'var(--hc-mint-light)';
    hcDropZone.style.background = '#f8fcfb';
    handleHCFiles(e.dataTransfer.files);
});

hcFileInput.addEventListener('change', function(e) {
    handleHCFiles(e.target.files);
});

// Manejar archivos
function handleHCFiles(files) {
    Array.from(files).forEach(file => {
        if (file.type.startsWith('image/')) {
            const reader = new FileReader();
            reader.onload = function(ev) {
                hcRecords[hcCurrentIdx].attachments.push(ev.target.result);
                loadHCRecord(hcCurrentIdx);
            };
            reader.readAsDataURL(file);
        }
    });
    
    if (files.length > 0) {
        swal("Archivos Adjuntados", `Se han agregado ${files.length} archivo(s)`, "success");
    }
}

// Eliminar anexos
hcAttachmentPreview.addEventListener('click', function(e) {
    const removeBtn = e.target.closest('.hc-remove');
    if (removeBtn) {
        const idx = parseInt(removeBtn.dataset.idx);
        hcRecords[hcCurrentIdx].attachments.splice(idx, 1);
        loadHCRecord(hcCurrentIdx);
        swal("Eliminado", "Archivo eliminado correctamente", "info");
    }
});

// Inicializar
document.addEventListener('DOMContentLoaded', function() {
    loadHCRecord(0);
});

// Si el DOM ya está cargado
if (document.readyState === 'complete' || document.readyState === 'interactive') {
    loadHCRecord(0);
}
