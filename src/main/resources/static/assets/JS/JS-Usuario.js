const currentUserId = document.getElementById('current-user-id') ? document.getElementById('current-user-id').value : null;
let currentPhotoFile = null;

$(document).ready(function() {
	const API_BASE_URL = 'http://localhost:8080/api';

	// Función para inicializar la aplicación
	initApp();

	function initApp() {
		// Configurar menú hamburguesa
		$('.navbar-toggler').click(function() {
			$('#sidebar').toggleClass('active');
			$('main').toggleClass('active');
		});

		// Configurar navegación entre secciones
		$('.nav-link').click(function(e) {
			e.preventDefault();
			$('.nav-link').removeClass('active');
			$(this).addClass('active');

			const target = $(this).attr('href');
			$('.content-section').hide();
			$(target).show();

			// Cargar contenido específico de cada sección desde el backend
			switch (target) {
				case '#dashboard':
					loadQuickSummary(); // Carga el resumen del dashboard
					loadPetsPreview();  // Carga la vista previa de mascotas
					break;
				case '#mascotas':
					loadPetsSection(); // Carga la lista completa de mascotas
					break;
				case '#historia-clinica':
					loadMedicalHistorySection();
					break;
				case '#tratamientos':
					loadTreatmentsSection();
					break;
				case '#compras':
					loadPurchasesSection();
					break;
				case '#citas':
					loadAppointmentsSection();
					break;
				case '#adopcion':
					loadAdoptionSection(); // Nueva función para cargar la sección de adopción
					break;
				case '#reportes':
					loadReportsSection(); // Nueva función para cargar la sección de reportes
					break;
				case '#opiniones':
					loadReviewsSection(); // Nueva función para cargar la sección de opiniones
					break;
				case '#configuracion':
					loadSettingsSection();
					break;
			}

			// Cerrar menú en móviles
			if ($(window).width() < 768) {
				$('#sidebar').removeClass('active');
				$('main').removeClass('active');
			}
		});

		// Mostrar sección de dashboard por defecto al cargar la página
		// Esto también activará la carga de datos iniciales
		$('.nav-link.active').click();

		// Configurar botones
		$('#add-pet-btn').click(function() {
			$('#addPetModal').modal('show');
		});

		$('#new-appointment-btn').click(function() {
			// Redirigir a WhatsApp para agendar cita
			window.open('https://wa.me/573204767864?text=Hola,%20me%20gustaría%20agendar%20una%20cita', '_blank');
		});

		$('#go-to-shop-btn').click(function() {
			alert('Redirigiendo a la tienda en línea...');
		});

		$('#delete-account-btn').click(function() {
			$('#confirmDeleteModal').modal('show');
		});

		$('#profile-form').off('submit');

		$('#password-form').submit(function(e) {
			e.preventDefault();
			changePassword();
		});

		// Lógica para el rating de opiniones
		$('.rating i').on('click', function() {
			var rating = $(this).data('rating');
			$('#review-rating').val(rating);
			$('.rating i').each(function(index) {
				if (index < rating) {
					$(this).removeClass('far').addClass('fas');
				} else {
					$(this).removeClass('fas').addClass('far');
				}
			});
		});

		// Lógica para el checkbox de confirmación de eliminación de cuenta
		$('#confirm-delete-check').on('change', function() {
			if ($(this).is(':checked')) {
				$('#confirm-delete-btn').prop('disabled', false);
			} else {
				$('#confirm-delete-btn').prop('disabled', true);
			}
		});

		$('#confirm-delete-btn').on('click', function() {
			alert('Funcionalidad de eliminar cuenta no implementada en el backend.');
			var confirmDeleteModal = bootstrap.Modal.getInstance(document.getElementById('confirmDeleteModal'));
			confirmDeleteModal.hide();
		});
	}

	// Función para cargar el resumen rápido del dashboard
	async function loadQuickSummary() {
		// Función deshabilitada - los datos se cargan desde Thymeleaf en el HTML
		console.log('ℹ️ loadQuickSummary deshabilitada - datos cargados desde servidor');
	}

	// Función para cargar vista previa de mascotas
	async function loadPetsPreview() {
		// Función deshabilitada - las mascotas se cargan desde Thymeleaf en el HTML
		console.log('ℹ️ loadPetsPreview deshabilitada - datos cargados desde servidor');
	}

	// Función para cargar sección de mascotas (lista completa)
	async function loadPetsSection() {
		console.log('ℹ️ loadPetsSection deshabilitada - datos cargados desde servidor');
	}

	// Función para cargar sección de historia clínica
	async function loadMedicalHistorySection() {
		console.log('ℹ️ loadMedicalHistorySection deshabilitada - datos cargados desde servidor');
	}

	// Función para mostrar historia clínica de una mascota específica
	window.showPetHistory = async function(petId) {
		if (!currentUserId) return;

		try {
			const response = await fetch(`${API_BASE_URL}/mascotas/${petId}/historial`); // Asume un endpoint para historial de mascota
			if (!response.ok) {
				throw new Error(`HTTP error! status: ${response.status}`);
			}
			const petHistoryData = await response.json(); // Esto debería incluir la info de la mascota y su historial

			$('#no-pet-selected').hide();
			$('#pet-history').show();

			// Actualizar información básica de la mascota
			$('#pet-history-image').attr('src', petHistoryData.foto || 'https://via.placeholder.com/150');
			$('#pet-history-name').text(petHistoryData.nombre);
			$('#pet-history-info').text(`${capitalizeFirstLetter(petHistoryData.especie)} · ${petHistoryData.raza || 'Desconocida'} · ${petHistoryData.edad} años · ${capitalizeFirstLetter(petHistoryData.genero)}`);
			$('#pet-history-status').text(petHistoryData.estado);
			$('#pet-history-status').removeClass('bg-success bg-warning bg-info bg-secondary').addClass(getStatusClass(petHistoryData.estado));

			// Llenar timeline
			$('.timeline').empty();

			if (petHistoryData.medicalHistory && petHistoryData.medicalHistory.length > 0) {
				// Ordenar historial por fecha (más reciente primero)
				const sortedHistory = [...petHistoryData.medicalHistory].sort((a, b) => new Date(b.date) - new Date(a.date));

				sortedHistory.forEach(record => {
					let content = '';

					if (record.type) { // Asume que el historial tiene un campo 'type'
						content = `<p><strong>Tipo:</strong> ${capitalizeFirstLetter(record.type)}</p>`;
					}
					if (record.description) {
						content += `<p>${record.description}</p>`;
					}
					if (record.treatment) {
						content += `<p><strong>Tratamiento:</strong> ${record.treatment}</p>`;
					}
					if (record.medication) {
						content += `<p><strong>Medicación:</strong> ${record.medication}</p>`;
					}
					if (record.vaccines && record.vaccines.length > 0) {
						content += `<p><strong>Vacunas:</strong> ${record.vaccines.join(', ')}</p>`;
					}
					if (record.exams) {
						content += `<p><strong>Exámenes:</strong> ${record.exams}</p>`;
					}
					if (record.notes) {
						content += `<div class="alert alert-light mt-2">
                                       <strong>Notas:</strong> ${record.notes}
                                    </div>`;
					}

					$('.timeline').append(`
                        <div class="timeline-item">
                            <div class="timeline-date">${formatDate(record.date)}</div>
                            <h6 class="timeline-title">${record.title || 'Evento'}</h6>
                            <div class="timeline-content">
                                ${content}
                            </div>
                        </div>
                    `);
				});
			} else {
				$('.timeline').append('<p class="text-center">No hay historial clínico registrado para esta mascota.</p>');
			}

			// Actualizar texto del selector
			$('#pet-selector').html(`<i class="fas fa-paw me-1"></i>${petHistoryData.nombre}`);

		} catch (error) {
			console.error('Error al cargar la historia clínica de la mascota:', error);
			$('#pet-history').hide();
			$('#no-pet-selected').show().html('<p class="text-center text-danger">Error al cargar la historia clínica.</p>');
		}
	}

	// Función para cargar sección de tratamientos
	async function loadTreatmentsSection() {
		if (!currentUserId) return;

		try {
			const response = await fetch(`${API_BASE_URL}/usuarios/${currentUserId}/mascotas`);
			if (response.status === 204) {
				$('#treatment-pet-dropdown').empty().append('<li><a class="dropdown-item" href="#">No hay mascotas</a></li>');
				$('#no-treatment-pet-selected').show();
				$('#pet-treatments').hide();
				return;
			}
			if (!response.ok) {
				throw new Error(`HTTP error! status: ${response.status}`);
			}
			const pets = await response.json();

			$('#treatment-pet-dropdown').empty();
			if (pets.length === 0) {
				$('#treatment-pet-dropdown').append('<li><a class="dropdown-item" href="#">No hay mascotas</a></li>');
				$('#no-treatment-pet-selected').show();
				$('#pet-treatments').hide();
			} else {
				pets.forEach(pet => {
					$('#treatment-pet-dropdown').append(`
                        <li><a class="dropdown-item" href="#" onclick="window.showPetTreatments(${pet.id})">${pet.nombre}</a></li>
                    `);
				});
				// Mostrar tratamientos de la primera mascota por defecto
				window.showPetTreatments(pets[0].id);
			}
		} catch (error) {
			console.error('Error al cargar la sección de tratamientos:', error);
			$('#treatment-pet-dropdown').empty().append('<li><a class="dropdown-item text-danger" href="#">Error al cargar</a></li>');
			$('#no-treatment-pet-selected').show();
			$('#pet-treatments').hide();
		}
	}

	// Función para mostrar tratamientos de una mascota específica
	window.showPetTreatments = async function(petId) {
		if (!currentUserId) return;

		try {
			const response = await fetch(`${API_BASE_URL}/mascotas/${petId}/tratamientos`); // Asume un endpoint para tratamientos
			if (!response.ok) {
				throw new Error(`HTTP error! status: ${response.status}`);
			}
			const treatmentsData = await response.json(); // Esto debería incluir tratamientos y recomendaciones

			$('#no-treatment-pet-selected').hide();
			$('#pet-treatments').show();

			// Llenar tratamientos activos
			$('#active-treatments').empty();
			if (treatmentsData.activeTreatments && treatmentsData.activeTreatments.length > 0) {
				treatmentsData.activeTreatments.forEach(treatment => {
					$('#active-treatments').append(`
                        <div class="treatment-card mb-3">
                            <h6>${treatment.name}</h6>
                            <p>${treatment.description}</p>
                            <div class="treatment-meta">
                                <span class="me-3"><i class="fas fa-calendar-alt me-1"></i>Iniciado: ${formatDate(treatment.startDate)}</span>
                                ${treatment.endDate ? `<span><i class="fas fa-calendar-check me-1"></i>Finaliza: ${formatDate(treatment.endDate)}</span>` : ''}
                            </div>
                        </div>
                    `);
				});
			} else {
				$('#active-treatments').append('<p class="text-center">No hay tratamientos activos.</p>');
			}

			// Llenar recomendaciones
			$('#vet-recommendations').empty();
			if (treatmentsData.recommendations && treatmentsData.recommendations.length > 0) {
				treatmentsData.recommendations.forEach(rec => {
					$('#vet-recommendations').append(`
                        <div class="recommendation-card mb-3">
                            <h6>${rec.type || 'Recomendación'}</h6>
                            <p>${rec.description}</p>
                            <span class="text-muted"><i class="fas fa-calendar-alt me-1"></i>${formatDate(rec.date)}</span>
                        </div>
                    `);
				});
			} else {
				$('#vet-recommendations').append('<p class="text-center">No hay recomendaciones veterinarias.</p>');
			}

			// Actualizar texto del selector (asume que treatmentsData incluye el nombre de la mascota)
			$('#treatment-pet-selector').html(`<i class="fas fa-paw me-1"></i>${treatmentsData.petName || 'Mascota'}`);

		} catch (error) {
			console.error('Error al cargar tratamientos y recomendaciones:', error);
			$('#pet-treatments').hide();
			$('#no-treatment-pet-selected').show().html('<p class="text-center text-danger">Error al cargar tratamientos.</p>');
		}
	}

	// Función para cargar sección de compras
	async function loadPurchasesSection() {
		if (!currentUserId) return;

		try {
			const response = await fetch(`${API_BASE_URL}/usuarios/${currentUserId}/compras`); // Asume un endpoint para compras
			if (response.status === 204) {
				$('#purchase-history').html(`
                    <tr>
                        <td colspan="5" class="text-center py-4">
                            <i class="fas fa-shopping-cart fa-2x mb-3"></i>
                            <h4>No has realizado ninguna compra</h4>
                            <button class="btn btn-primary mt-2" id="go-to-shop-btn"><i class="fas fa-store me-2"></i>Ir a la Tienda</button>
                        </td>
                    </tr>
                `);
				return;
			}
			if (!response.ok) {
				throw new Error(`HTTP error! status: ${response.status}`);
			}
			const purchases = await response.json();

			$('#purchase-history').empty();
			purchases.forEach(purchase => {
				const productsList = purchase.products.map(p => `${p.name} (x${p.quantity})`).join(', ');

				$('#purchase-history').append(`
                    <tr>
                        <td>${formatDate(purchase.date)}</td>
                        <td>${productsList}</td>
                        <td>$${purchase.total.toLocaleString()}</td>
                        <td><span class="badge ${getStatusBadgeClass(purchase.status)}">${capitalizeFirstLetter(purchase.status)}</span></td>
                        <td>
                            <button class="btn btn-sm btn-outline-primary" onclick="window.viewPurchaseDetails(${purchase.id})">Ver Detalles</button>
                        </td>
                    </tr>
                `);
			});
			// Aquí también podrías cargar recomendaciones de productos si tu API las proporciona
			// loadProductRecommendations();
		} catch (error) {
			console.error('Error al cargar la sección de compras:', error);
			$('#purchase-history').html(`
                <tr>
                    <td colspan="5" class="text-center py-4 text-danger">
                        <i class="fas fa-exclamation-triangle fa-2x mb-3"></i>
                        <h4>Error al cargar las compras</h4>
                        <p>Inténtalo de nuevo más tarde.</p>
                    </td>
                </tr>
            `);
		}
	}

	// Función para ver detalles de compra (asume que los datos ya están cargados en el frontend o se hace otra llamada)
	window.viewPurchaseDetails = async function(purchaseId) {
		if (!currentUserId) return;

		try {
			const response = await fetch(`${API_BASE_URL}/usuarios/${currentUserId}/compras/${purchaseId}`); // Asume un endpoint para detalle de compra
			if (!response.ok) {
				throw new Error(`HTTP error! status: ${response.status}`);
			}
			const purchase = await response.json();

			$('#purchase-details-content').empty();

			let productsHTML = '';
			purchase.products.forEach(product => {
				productsHTML += `
                    <div class="purchase-detail-item">
                        <div class="d-flex justify-content-between">
                            <span>${product.name} x${product.quantity}</span>
                            <span>$${(product.price * product.quantity).toLocaleString()}</span>
                        </div>
                    </div>
                `;
			});

			$('#purchase-details-content').html(`
                <div class="mb-3">
                    <span class="appointment-detail-label">Fecha:</span>
                    <span class="appointment-detail-value">${formatDate(purchase.date)}</span>
                </div>
                <div class="mb-3">
                    <span class="appointment-detail-label">Estado:</span>
                    <span class="badge ${getStatusBadgeClass(purchase.status)}">${capitalizeFirstLetter(purchase.status)}</span>
                </div>
                <div class="mb-3">
                    <span class="appointment-detail-label">Productos:</span>
                    <div class="mt-2">
                        ${productsHTML}
                    </div>
                </div>
                <div class="mb-3 pt-2 border-top">
                    <div class="d-flex justify-content-between">
                        <span class="appointment-detail-label">Total:</span>
                        <span class="fw-bold">$${purchase.total.toLocaleString()}</span>
                    </div>
                </div>
            `);

			$('#purchaseDetailsModal').modal('show');
		} catch (error) {
			console.error('Error al cargar detalles de compra:', error);
			alert('Error al cargar los detalles de la compra.');
		}
	}

	// Función para cargar sección de citas
	async function loadAppointmentsSection() {
		if (!currentUserId) return;

		try {
			const response = await fetch(`${API_BASE_URL}/usuarios/${currentUserId}/citas`); // Asume un endpoint para citas
			if (response.status === 204) {
				$('#upcoming-appointments').html('<p class="text-center">No hay citas próximas.</p>');
				$('#past-appointments').html('<p class="text-center">No hay citas pasadas.</p>');
				return;
			}
			if (!response.ok) {
				throw new Error(`HTTP error! status: ${response.status}`);
			}
			const appointments = await response.json();

			$('#upcoming-appointments').empty();
			$('#past-appointments').empty();

			const now = new Date();
			const upcoming = appointments.filter(a => new Date(a.date) >= now);
			const past = appointments.filter(a => new Date(a.date) < now);

			if (upcoming.length === 0) {
				$('#upcoming-appointments').append('<p class="text-center">No hay citas próximas.</p>');
			} else {
				upcoming.forEach(appointment => {
					// Asume que el objeto appointment ya incluye el nombre de la mascota o puedes buscarlo
					const petName = appointment.petName || 'Mascota Desconocida';
					$('#upcoming-appointments').append(`
                        <div class="appointment-card mb-3 p-3 border rounded">
                            <div class="d-flex justify-content-between">
                                <div>
                                    <h6>${petName} - ${getReasonText(appointment.reason)}</h6>
                                    <p class="mb-1"><i class="fas fa-calendar-alt me-1"></i>${formatDate(appointment.date)} a las ${appointment.time}</p>
                                    ${appointment.notes ? `<p class="mb-1"><i class="fas fa-sticky-note me-1"></i>${appointment.notes}</p>` : ''}
                                </div>
                                <div class="text-end">
                                    <span class="badge ${getStatusBadgeClass(appointment.status)} mb-2">${capitalizeFirstLetter(appointment.status)}</span>
                                    <button class="btn btn-sm btn-outline-primary" onclick="window.viewAppointmentDetails(${appointment.id})">
                                        <i class="fas fa-eye me-1"></i>Ver Detalles
                                    </button>
                                </div>
                            </div>
                        </div>
                    `);
				});
			}

			if (past.length === 0) {
				$('#past-appointments').append('<p class="text-center">No hay citas pasadas.</p>');
			} else {
				past.forEach(appointment => {
					const petName = appointment.petName || 'Mascota Desconocida';
					$('#past-appointments').append(`
                        <div class="appointment-card mb-3 p-3 border rounded">
                            <div class="d-flex justify-content-between">
                                <div>
                                    <h6>${petName} - ${getReasonText(appointment.reason)}</h6>
                                    <p class="mb-1"><i class="fas fa-calendar-alt me-1"></i>${formatDate(appointment.date)} a las ${appointment.time}</p>
                                    ${appointment.notes ? `<p class="mb-1"><i class="fas fa-sticky-note me-1"></i>${appointment.notes}</p>` : ''}
                                </div>
                                <div class="text-end">
                                    <span class="badge ${getStatusBadgeClass(appointment.status)} mb-2">${capitalizeFirstLetter(appointment.status)}</span>
                                    <button class="btn btn-sm btn-outline-primary" onclick="window.viewAppointmentDetails(${appointment.id})">
                                        <i class="fas fa-eye me-1"></i>Ver Detalles
                                    </button>
                                </div>
                            </div>
                        </div>
                    `);
				});
			}
		} catch (error) {
			console.error('Error al cargar la sección de citas:', error);
			$('#upcoming-appointments').html('<p class="text-center text-danger">Error al cargar citas.</p>');
			$('#past-appointments').html('<p class="text-center text-danger">Error al cargar citas.</p>');
		}
	}

	// Función para cargar sección de adopción (ejemplo, necesitarás endpoints)
	async function loadAdoptionSection() {
		if (!currentUserId) return;

		try {
			const response = await fetch(`${API_BASE_URL}/usuarios/${currentUserId}/adopciones`); // Asume un endpoint
			if (response.status === 204) {
				$('#current-adoption').html(`
                    <div class="text-center py-3" id="no-current-adoption">
                        <i class="fas fa-paw fa-3x mb-3"></i>
                        <h4>No tienes ningún proceso de adopción en curso</h4>
                        <p>¿Quieres adoptar una mascota?</p>
                        <button class="btn btn-primary" id="new-adoption-btn">Iniciar Proceso</button>
                    </div>
                `);
				$('#adoption-history').html(`
                    <div class="text-center py-3" id="no-adoption-history">
                        <i class="fas fa-paw fa-3x mb-3"></i>
                        <h4>No has adoptado mascotas anteriormente</h4>
                    </div>
                `);
				return;
			}
			if (!response.ok) {
				throw new Error(`HTTP error! status: ${response.status}`);
			}
			const adoptionData = await response.json();

			// Lógica para mostrar proceso actual y historial de adopciones
			// Esto dependerá de la estructura de datos que devuelva tu API
			$('#current-adoption').empty().append('<p>Proceso de adopción actual cargado.</p>'); // Placeholder
			$('#adoption-history').empty().append('<p>Historial de adopciones cargado.</p>'); // Placeholder

		} catch (error) {
			console.error('Error al cargar la sección de adopción:', error);
			$('#current-adoption').html('<p class="text-center text-danger">Error al cargar.</p>');
			$('#adoption-history').html('<p class="text-center text-danger">Error al cargar.</p>');
		}
	}

	// Función para cargar sección de reportes (ejemplo, necesitarás endpoints)
	async function loadReportsSection() {
		if (!currentUserId) return;

		try {
			const response = await fetch(`${API_BASE_URL}/usuarios/${currentUserId}/reportes`); // Asume un endpoint
			if (response.status === 204) {
				$('#reports-list').html(`
                    <tr>
                        <td colspan="5" class="text-center py-4">
                            <i class="fas fa-exclamation-triangle fa-2x mb-3"></i>
                            <h4>No has realizado ningún reporte</h4>
                        </td>
                    </tr>
                `);
				return;
			}
			if (!response.ok) {
				throw new Error(`HTTP error! status: ${response.status}`);
			}
			const reports = await response.json();

			$('#reports-list').empty();
			reports.forEach(report => {
				$('#reports-list').append(`
                    <tr>
                        <td>${formatDate(report.date)}</td>
                        <td>${report.description}</td>
                        <td>${report.location}</td>
                        <td><span class="badge ${getStatusBadgeClass(report.status)}">${capitalizeFirstLetter(report.status)}</span></td>
                        <td>
                            <button class="btn btn-sm btn-outline-info">Ver</button>
                        </td>
                    </tr>
                `);
			});
		} catch (error) {
			console.error('Error al cargar la sección de reportes:', error);
			$('#reports-list').html(`
                <tr>
                    <td colspan="5" class="text-center py-4 text-danger">
                        <i class="fas fa-exclamation-triangle fa-2x mb-3"></i>
                        <h4>Error al cargar los reportes</h4>
                    </td>
                </tr>
            `);
		}
	}

	// Función para cargar sección de opiniones (ejemplo, necesitarás endpoints)
	async function loadReviewsSection() {
		if (!currentUserId) return;

		try {
			const response = await fetch(`${API_BASE_URL}/usuarios/${currentUserId}/opiniones`); // Asume un endpoint
			if (response.status === 204) {
				$('#user-reviews').html(`
                    <div class="text-center py-4">
                        <i class="fas fa-star fa-2x mb-3"></i>
                        <h4>No has dejado ninguna opinión</h4>
                    </div>
                `);
				return;
			}
			if (!response.ok) {
				throw new Error(`HTTP error! status: ${response.status}`);
			}
			const reviews = await response.json();

			$('#user-reviews').empty();
			reviews.forEach(review => {
				$('#user-reviews').append(`
                    <div class="card mb-3">
                        <div class="card-body">
                            <h5 class="card-title">${review.service}</h5>
                            <div class="rating-display mb-2">
                                ${Array(review.rating).fill('<i class="fas fa-star text-warning"></i>').join('')}
                                ${Array(5 - review.rating).fill('<i class="far fa-star text-warning"></i>').join('')}
                            </div>
                            <p class="card-text">${review.comment}</p>
                            <small class="text-muted">${formatDate(review.date)}</small>
                        </div>
                    </div>
                `);
			});
		} catch (error) {
			console.error('Error al cargar la sección de opiniones:', error);
			$('#user-reviews').html('<p class="text-center text-danger">Error al cargar las opiniones.</p>');
		}
	}

	// Función para cargar sección de configuración
	function loadSettingsSection() {
		// Los datos del usuario ya están cargados en el HTML por Thymeleaf
		// No es necesario hacer una llamada AJAX ya que los datos vienen del servidor
		console.log('✅ Sección de configuración cargada (datos del servidor)');

		// Si necesitas cargar datos adicionales, puedes hacerlo aquí
		// pero por ahora los datos del formulario ya están precargados por Thymeleaf
	}

	// --- FUNCIONES PARA ENVIAR DATOS AL BACKEND (AJAX) ---

	// Función para guardar perfil (AJAX)
	// Nota: El formulario de perfil se envía directamente al servidor mediante POST tradicional
	// No es necesario usar AJAX aquí ya que el formulario tiene th:action configurado
	function saveProfile() {
		// El formulario #profile-form se envía automáticamente al servidor
		// mediante el botón submit del formulario
		console.log('✅ Perfil guardado mediante formulario tradicional');
	}

	// Función para cambiar contraseña (AJAX)
	// Nota: Esta función está deshabilitada porque el endpoint no existe
	// Si necesitas cambiar contraseña, implementa el endpoint en el backend
	function changePassword() {
		console.log('⚠️ Función changePassword deshabilitada - endpoint no implementado');
		alert('La función de cambio de contraseña será implementada próximamente.');
	}

	// --- FUNCIONES AUXILIARES ---

	function formatDate(dateString) {
		if (!dateString) return 'N/A';
		const options = { year: 'numeric', month: 'long', day: 'numeric' };
		return new Date(dateString).toLocaleDateString('es-ES', options);
	}

	function capitalizeFirstLetter(string) {
		if (!string) return '';
		return string.charAt(0).toUpperCase() + string.slice(1);
	}

	function getStatusClass(status) {
		if (!status) return 'bg-secondary';
		const lowerStatus = status.toLowerCase();
		if (lowerStatus.includes('saludable') || lowerStatus.includes('disponible')) return 'bg-success';
		if (lowerStatus.includes('tratamiento') || lowerStatus.includes('en proceso')) return 'bg-warning';
		if (lowerStatus.includes('seguimiento')) return 'bg-info';
		return 'bg-secondary';
	}

	function getStatusBadgeClass(status) {
		if (!status) return 'bg-secondary';
		const lowerStatus = status.toLowerCase();
		if (lowerStatus === 'agendada' || lowerStatus === 'pendiente') return 'bg-primary';
		if (lowerStatus === 'finalizada' || lowerStatus === 'entregado' || lowerStatus === 'completado') return 'bg-success';
		if (lowerStatus === 'cancelada' || lowerStatus === 'rechazado') return 'bg-danger';
		if (lowerStatus === 'en proceso') return 'bg-info';
		return 'bg-secondary';
	}

	function getReasonText(reason) {
		const reasons = {
			'consulta': 'Consulta General',
			'vacunacion': 'Vacunación',
			'urgencia': 'Urgencia',
			'cirugia': 'Cirugía',
			'estetica': 'Estética',
			'control': 'Control',
			'otro': 'Otro'
		};
		return reasons[reason] || capitalizeFirstLetter(reason);
	}
});

document.addEventListener('DOMContentLoaded', function() {
	var editPetModal = document.getElementById('editPetModal');
	if (editPetModal) {
		editPetModal.addEventListener('show.bs.modal', function(event) {
			var button = event.relatedTarget;
			var mascotaId = button.getAttribute('data-id');
			console.log("🔄 Cargando mascota con ID:", mascotaId);

			// Limpiar formulario antes de cargar
			var form = editPetModal.querySelector('#editPetForm');
			form.reset();

			// Petición AJAX para obtener datos de la mascota
			fetch('/usuarios/perfilusuario/mascota/' + mascotaId)
				.then(response => {
					console.log("📡 Respuesta recibida, status:", response.status);
					if (!response.ok) {
						throw new Error('Error al cargar mascota: ' + response.status);
					}
					return response.json();
				})
				.then(mascota => {
					console.log("✅ Datos de mascota recibidos:", mascota);

					// Rellenar campos del formulario (con validación de existencia)
					const setFieldValue = (id, value) => {
						const element = document.getElementById(id);
						if (element) {
							element.value = value || '';
						} else {
							console.warn(`⚠️ Campo no encontrado: ${id}`);
						}
					};

					setFieldValue('editMascotaId', mascota.id);
					setFieldValue('editNombre', mascota.nombre);
					setFieldValue('editEspecie', mascota.especie);
					setFieldValue('editRaza', mascota.raza);
					setFieldValue('editEdad', mascota.edad);
					setFieldValue('editGenero', mascota.genero);
					setFieldValue('editTamano', mascota.tamaño); // Sin tilde
					setFieldValue('editDescripcion', mascota.descripcion);
					setFieldValue('editMascotaFotoActual', mascota.foto);
					setFieldValue('editUnidadEdad', mascota.unidadEdad || 'años');

					console.log("🎯 Formulario llenado correctamente");
				})
				.catch(error => {
					console.error('❌ Error al cargar datos de la mascota:', error);
					alert('Error al cargar datos de la mascota: ' + error.message);
					// Cerrar modal si falla
					var modal = bootstrap.Modal.getInstance(editPetModal);
					modal.hide();
				});
		});
	}
});

//Script para eliminar mascota

function eliminarMascota(id) {
	if (confirm('¿Estás seguro de que quieres eliminar esta mascota?')) {
		fetch('/usuarios/perfilusuario/eliminarmascota/' + id, {
			method: 'DELETE',
			headers: {
				'Content-Type': 'application/json',
			}
		})
			.then(response => {
				console.log('Respuesta del servidor:', response.status);
				if (response.ok) {
					alert('Mascota eliminada exitosamente');
					location.reload();
				} else {
					return response.text().then(errorMessage => {
						alert('Error al eliminar la mascota: ' + errorMessage);
					});
				}
			})
			.catch(error => {
				console.error('Error en la solicitud:', error);
				alert('Error de conexión: ' + error.message);
			});
	}
}

// Función para mostrar alertas (manteniendo tu código original)
function showAlert(message, type) {
	// Crear alerta Bootstrap
	const alertDiv = document.createElement('div');
	alertDiv.className = `alert alert-${type} alert-dismissible fade show mt-3`;
	alertDiv.innerHTML = `
        <i class="fas ${type === 'success' ? 'fa-check-circle' : 'fa-info-circle'} me-2"></i>
        ${message}
        <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
    `;

	// Insertar después del título de la sección
	const section = document.getElementById('configuracion');
	const title = section.querySelector('.border-bottom');
	title.parentNode.insertBefore(alertDiv, title.nextSibling);

	// Auto-eliminar después de 5 segundos
	setTimeout(() => {
		if (alertDiv.parentNode) {
			alertDiv.remove();
		}
	}, 5000);
}

// Función para manejar el cierre de sesión
function handleLogout() {
	if (confirm('¿Estás seguro de que deseas cerrar sesión?')) {
		// Aquí puedes agregar lógica adicional antes de redirigir
		// Por ejemplo, limpiar localStorage, hacer logout en API, etc.

		// Limpiar datos de sesión si es necesario
		localStorage.removeItem('userToken');
		sessionStorage.clear();

		// Redirigir al index
		window.location.href = '/usuarios/index';
	}
}

document.addEventListener('DOMContentLoaded', function() {
	const logoutLink = document.querySelector('.sidebar .nav-link.text-danger');
	if (logoutLink) {
		logoutLink.addEventListener('click', function(e) {
			e.preventDefault();
			handleLogout();
		});
	}

	const headerLogoutLink = document.querySelector('.custom-logout-link');
	if (headerLogoutLink) {
		headerLogoutLink.addEventListener('click', function(e) {
			e.preventDefault();
			handleLogout();
		});
	}
});

window.previewProfilePicture = function(input) {
	const file = input.files && input.files[0];
	if (!file) return;

	const maxSizeMB = 10; // 10 MB
	const maxSizeBytes = maxSizeMB * 1024 * 1024; // Convertir a bytes
	const maxSizeInput = input.getAttribute('data-max-size');
	const maxSize = maxSizeInput ? parseInt(maxSizeInput, 10) : maxSizeBytes;
	
	if (file.size > maxSize) {
		const fileSizeMB = (file.size / (1024 * 1024)).toFixed(2);
		alert(`La imagen (${fileSizeMB}MB) excede el tamaño máximo permitido de ${maxSizeMB}MB.`);
		input.value = '';
		return;
	}

	// Validar tipo de archivo
	const validTypes = ['image/jpeg', 'image/png', 'image/gif', 'image/webp'];
	if (!validTypes.includes(file.type)) {
		alert('Formato de archivo no válido. Solo se permiten JPG, PNG, GIF y WebP.');
		input.value = '';
		return;
	}

	// Guardar el archivo en variable global
	currentPhotoFile = file;

	const reader = new FileReader();
	reader.onload = function(e) {
		const previewContainer = document.getElementById('new-picture-preview');
		const previewImg = document.getElementById('picture-preview');
		if (previewImg) {
			previewImg.src = e.target.result;
		}
		if (previewContainer) {
			previewContainer.style.display = 'block';
		}
	};
	reader.readAsDataURL(file);
};

window.saveProfilePicture = async function() {
	if (!currentPhotoFile) {
		alert('No hay ninguna imagen para guardar.');
		return;
	}

	const userId = document.getElementById('current-user-id').value;
	if (!userId) {
		alert('Error: No se pudo identificar el usuario.');
		return;
	}

	try {
		// Mostrar indicador de carga
		const saveButton = document.querySelector('#new-picture-preview .btn-success');
		if (saveButton) {
			saveButton.innerHTML = '<i class="fas fa-spinner fa-spin me-1"></i>Guardando...';
			saveButton.disabled = true;
		}

		// Convertir archivo a Base64
		const reader = new FileReader();
		reader.onload = async function(e) {
			try {
				const base64String = e.target.result; // Enviar completo con prefijo data:image/...;base64,

				console.log('📤 Enviando foto al servidor...');

				// Enviar al servidor con JSON
				const response = await fetch('/usuarios/perfilusuario/actualizarFotoPerfil', {
					method: 'POST',
					headers: {
						'Content-Type': 'application/json'
					},
					body: JSON.stringify({
						fotoPerfil: base64String,
						usuarioId: userId
					})
				});

				console.log('📡 Respuesta del servidor:', response.status);

				if (response.ok) {
					const result = await response.json();
					console.log('✅ Respuesta:', result);

					// Actualizar la imagen en la interfaz
					const dataUrl = URL.createObjectURL(currentPhotoFile);
					document.getElementById('current-profile-picture').src = dataUrl;
					document.getElementById('sidebar-profile-picture').src = dataUrl;

					// Limpiar
					document.getElementById('new-picture-preview').style.display = 'none';
					document.getElementById('profile-picture-input').value = '';
					currentPhotoFile = null;

					alert('✅ Foto de perfil actualizada correctamente.');
				} else {
					const errorText = await response.text();
					console.error('❌ Error del servidor:', errorText);
					throw new Error(errorText || 'Error del servidor');
				}
			} catch (error) {
				console.error('❌ Error al guardar la foto:', error);
				alert('❌ Error al guardar la foto: ' + error.message);
			} finally {
				// Restaurar botón
				const saveButton = document.querySelector('#new-picture-preview .btn-success');
				if (saveButton) {
					saveButton.innerHTML = '<i class="fas fa-check me-1"></i>Guardar';
					saveButton.disabled = false;
				}
			}
		};
		reader.readAsDataURL(currentPhotoFile);

	} catch (error) {
		console.error('Error al procesar la foto:', error);
		alert('Error al procesar la foto: ' + error.message);
		// Restaurar botón
		const saveButton = document.querySelector('#new-picture-preview .btn-success');
		if (saveButton) {
			saveButton.innerHTML = '<i class="fas fa-check me-1"></i>Guardar';
			saveButton.disabled = false;
		}
	}
};

window.removeProfilePicture = async function() {
	if (!confirm('¿Estás seguro de que deseas eliminar tu foto de perfil?')) {
		return;
	}

	const userId = document.getElementById('current-user-id').value;
	if (!userId) {
		alert('Error: No se pudo identificar el usuario.');
		return;
	}

	try {
		const response = await fetch('/usuarios/perfilusuario/eliminarFotoPerfil', {
			method: 'POST',
			headers: {
				'Content-Type': 'application/json'
			},
			body: JSON.stringify({
				usuarioId: userId
			})
		});

		if (response.ok) {
			const defaultSrc = '/assets/IMG/humano.jpg';
			document.getElementById('current-profile-picture').src = defaultSrc;
			document.getElementById('sidebar-profile-picture').src = defaultSrc;

			document.getElementById('new-picture-preview').style.display = 'none';
			document.getElementById('profile-picture-input').value = '';
			currentPhotoFile = null;

			showAlert('Foto de perfil eliminada correctamente.', 'info');
		} else {
			throw new Error('Error del servidor');
		}
	} catch (error) {
		console.error('Error al eliminar la foto:', error);
		showAlert('Error al eliminar la foto: ' + error.message, 'danger');
	}
};

window.cancelProfilePicture = function() {
	const input = document.getElementById('profile-picture-input');
	const previewContainer = document.getElementById('new-picture-preview');
	const previewImg = document.getElementById('picture-preview');
	if (input) input.value = '';
	if (previewImg) previewImg.src = '';
	if (previewContainer) previewContainer.style.display = 'none';
	currentPhotoFile = null;
};

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

		// Establecer tema por defecto (claro)
		htmlElement.setAttribute('data-theme', 'light');
		themeToggle.checked = false;

		// Escuchar cambios en el toggle
		themeToggle.addEventListener('change', function() {
			if (this.checked) {
				htmlElement.setAttribute('data-theme', 'dark');
				console.log('Tema oscuro activado');
			} else {
				htmlElement.setAttribute('data-theme', 'light');
				console.log('Tema claro activado');
			}
		});

		console.log('Sistema de temas inicializado correctamente');
	}
})();

function cargarSidebar() {
	if (!currentUserId) {
		console.warn('No se pudo cargar el sidebar: currentUserId no está definido');
		return;
	}

	fetch(`/usuario/buscar/${currentUserId}`)
		.then(r => {
			if (!r.ok) {
				throw new Error(`Error HTTP: ${r.status}`);
			}
			return r.json();
		})
		.then(u => {
			const sidebarNombre = document.getElementById("sidebarNombre");
			const sidebarFoto = document.getElementById("sidebarFoto");

			if (sidebarNombre) sidebarNombre.innerText = u.nombres || 'Usuario';
			if (sidebarFoto) sidebarFoto.src = u.imagen ? `/${u.imagen}` : "/img/default.png";
		})
		.catch(error => console.error('Error al cargar el sidebar:', error));
}

cargarSidebar();
