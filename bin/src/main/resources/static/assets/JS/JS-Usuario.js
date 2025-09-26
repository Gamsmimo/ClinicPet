<<<<<<< HEAD

$(document).ready(function() {
	// --- CONFIGURACIÓN INICIAL Y DATOS DEL USUARIO ---
	// En una aplicación real, el ID del usuario logueado se obtendría de la sesión
	// o de un token JWT. Para este ejemplo, lo obtenemos de un campo oculto en el HTML.
	// Asegúrate de que tu HTML tenga: <input type="hidden" id="current-user-id" th:value="${idUsuarioActual}">
	const currentUserId = $('#current-user-id').val();

	// URL base de tu API REST (ajusta el puerto si es necesario)
	const API_BASE_URL = 'http://localhost:8080/api';

	// Función para inicializar la aplicación
	initApp();

	function initApp() {
		// Cargar datos del usuario (nombre) - Esto debería venir del backend
		// Por ahora, si no tienes un endpoint para obtener el usuario, puedes dejarlo estático
		// o implementarlo. Asumimos que el nombre del usuario se cargará dinámicamente.
		// $('#username').text("Cargando...");
		// $('#username-header').text("Cargando...");

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

		// El botón de guardar mascota ahora es un submit de formulario tradicional,
		// por lo que no necesita un manejador de click en JS para enviar los datos.
		// La función addNewPet() ya no es necesaria para el envío.

		$('#new-appointment-btn').click(function() {
			// Redirigir a WhatsApp para agendar cita
			window.open('https://wa.me/573204767864?text=Hola,%20me%20gustaría%20agendar%20una%20cita', '_blank');
		});

		$('#go-to-shop-btn').click(function() {
			alert('Redirigiendo a la tienda en línea...');
			// Aquí podrías redirigir a una URL real de tu tienda
			// window.location.href = '/tienda';
		});

		$('#delete-account-btn').click(function() {
			$('#confirmDeleteModal').modal('show');
		});

		// Enviar formulario de perfil (AJAX o tradicional, dependiendo de tu implementación)
		$('#profile-form').submit(function(e) {
			e.preventDefault(); // Prevenir el envío tradicional
			saveProfile();
		});

		// Enviar formulario de contraseña (AJAX o tradicional)
		$('#password-form').submit(function(e) {
			e.preventDefault(); // Prevenir el envío tradicional
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

	// --- FUNCIONES PARA CARGAR DATOS DESDE EL BACKEND ---

	// Función para cargar el resumen rápido del dashboard
	async function loadQuickSummary() {
		if (!currentUserId) return;

		try {
			const response = await fetch(`${API_BASE_URL}/usuarios/${currentUserId}/resumen`); // Asume un endpoint de resumen
			if (!response.ok) {
				if (response.status === 404) { // Usuario no encontrado
					console.warn("Usuario no encontrado para el resumen.");
					return;
				}
				throw new Error(`HTTP error! status: ${response.status}`);
			}
			const summaryData = await response.json();

			// Actualizar nombre del usuario
			$('#username').text(summaryData.userName || "Usuario");
			$('#username-header').text(summaryData.userName ? summaryData.userName.split(' ')[0] : "Usuario");

			// Actualizar próxima cita
			if (summaryData.nextAppointment) {
				$('#next-appointment').text(`${formatDate(summaryData.nextAppointment.date)}, ${summaryData.nextAppointment.time} - ${summaryData.nextAppointment.petName}: ${getReasonText(summaryData.nextAppointment.reason)}`);
			} else {
				$('#next-appointment').text('No hay citas próximas.');
			}

			// Actualizar estado de salud (asume que el resumen trae la mascota principal)
			if (summaryData.mainPetHealth) {
				$('#pet-health').text(`${summaryData.mainPetHealth.petName} - ${summaryData.mainPetHealth.status}`);
			} else {
				$('#pet-health').text('No hay mascotas registradas.');
			}

			// Actualizar última compra
			if (summaryData.lastPurchase) {
				$('#last-purchase').text(`${formatDate(summaryData.lastPurchase.date)} - ${summaryData.lastPurchase.productName} (${summaryData.lastPurchase.quantity} unidades)`);
			} else {
				$('#last-purchase').text('No hay compras recientes.');
			}

			// Actualizar recomendación
			if (summaryData.vetRecommendation) {
				$('#vet-recommendation').text(`Recordatorio: ${summaryData.vetRecommendation.description}`);
			} else {
				$('#vet-recommendation').text('No hay recomendaciones pendientes.');
			}

		} catch (error) {
			console.error('Error al cargar el resumen rápido:', error);
			// Fallback a datos por defecto o mensaje de error
			$('#next-appointment').text('Error al cargar.');
			$('#pet-health').text('Error al cargar.');
			$('#last-purchase').text('Error al cargar.');
			$('#vet-recommendation').text('Error al cargar.');
		}
	}

	// Función para cargar vista previa de mascotas
	async function loadPetsPreview() {
		if (!currentUserId) return;

		try {
			const response = await fetch(`${API_BASE_URL}/usuarios/${currentUserId}/mascotas`);
			if (response.status === 204) { // No Content
				$('#pets-preview').html('<p class="text-center">No tienes mascotas registradas aún.</p>');
				return;
			}
			if (!response.ok) {
				throw new Error(`HTTP error! status: ${response.status}`);
			}
			const pets = await response.json();

			$('#pets-preview').empty();
			if (pets.length === 0) {
				$('#pets-preview').append('<p class="text-center">No tienes mascotas registradas aún.</p>');
			} else {
				pets.slice(0, 3).forEach(pet => {
					$('#pets-preview').append(`
                        <div class="col-md-4">
                            <div class="pet-card">
                                <img src="${pet.foto || 'https://via.placeholder.com/100'}" alt="${pet.nombre}" class="pet-img">
                                <h5>${pet.nombre}</h5>
                                <p>${capitalizeFirstLetter(pet.especie)} · ${pet.raza || 'Desconocida'} · ${pet.edad} años</p>
                                <span class="pet-status ${getStatusClass(pet.estado)}">${pet.estado}</span>
                            </div>
                        </div>
                    `);
				});

				if (pets.length > 3) {
					$('#pets-preview').append(`
                        <div class="col-md-4">
                            <div class="pet-card text-center d-flex align-items-center justify-content-center" style="height: 100%;">
                                <div>
                                    <i class="fas fa-paw fa-3x mb-3"></i>
                                    <h5>+${pets.length - 3} mascotas</h5>
                                    <a href="#mascotas" class="btn btn-sm btn-outline-primary">Ver todas</a>
                                </div>
                            </div>
                        </div>
                    `);
				}
			}
		} catch (error) {
			console.error('Error al cargar vista previa de mascotas:', error);
			$('#pets-preview').html('<p class="text-center text-danger">Error al cargar las mascotas.</p>');
		}
	}

	// Función para cargar sección de mascotas (lista completa)
	async function loadPetsSection() {
		if (!currentUserId) return;

		try {
			const response = await fetch(`${API_BASE_URL}/usuarios/${currentUserId}/mascotas`);
			if (response.status === 204) {
				$('#pets-list').html('<p class="text-center">No tienes mascotas registradas aún. ¡Agrega una!</p>');
				return;
			}
			if (!response.ok) {
				throw new Error(`HTTP error! status: ${response.status}`);
			}
			const pets = await response.json();

			$('#pets-list').empty();
			if (pets.length === 0) {
				$('#pets-list').append('<p class="text-center">No tienes mascotas registradas aún. ¡Agrega una!</p>');
			} else {
				pets.forEach(pet => {
					$('#pets-list').append(`
                        <div class="col-md-6 col-lg-4">
                            <div class="card pet-card">
                                <img src="${pet.foto || 'https://via.placeholder.com/150'}" class="card-img-top" alt="${pet.nombre}">
                                <div class="card-body">
                                    <h5 class="card-title">${pet.nombre}</h5>
                                    <p class="card-text">
                                        <strong>Especie:</strong> ${capitalizeFirstLetter(pet.especie)}<br>
                                        <strong>Raza:</strong> ${pet.raza || 'Desconocida'}<br>
                                        <strong>Edad:</strong> ${pet.edad} años<br>
                                        <strong>Sexo:</strong> ${capitalizeFirstLetter(pet.genero)}
                                    </p>
                                    <span class="badge ${getStatusClass(pet.estado)} mb-3">${pet.estado}</span>
                                </div>
                            </div>
                        </div>
                    `);
				});
			}
		} catch (error) {
			console.error('Error al cargar la sección de mascotas:', error);
			$('#pets-list').html('<p class="text-center text-danger">Error al cargar las mascotas.</p>');
		}
	}

	// Función para cargar sección de historia clínica
	async function loadMedicalHistorySection() {
		if (!currentUserId) return;

		try {
			const response = await fetch(`${API_BASE_URL}/usuarios/${currentUserId}/mascotas`);
			if (response.status === 204) {
				$('#pet-dropdown').empty().append('<li><a class="dropdown-item" href="#">No hay mascotas</a></li>');
				$('#no-pet-selected').show();
				$('#pet-history').hide();
				return;
			}
			if (!response.ok) {
				throw new Error(`HTTP error! status: ${response.status}`);
			}
			const pets = await response.json();

			$('#pet-dropdown').empty();
			if (pets.length === 0) {
				$('#pet-dropdown').append('<li><a class="dropdown-item" href="#">No hay mascotas</a></li>');
				$('#no-pet-selected').show();
				$('#pet-history').hide();
			} else {
				pets.forEach(pet => {
					$('#pet-dropdown').append(`
                        <li><a class="dropdown-item" href="#" onclick="window.showPetHistory(${pet.id})">${pet.nombre}</a></li>
                    `);
				});
				// Mostrar la historia clínica de la primera mascota por defecto
				window.showPetHistory(pets[0].id);
			}
		} catch (error) {
			console.error('Error al cargar la sección de historia clínica:', error);
			$('#pet-dropdown').empty().append('<li><a class="dropdown-item text-danger" href="#">Error al cargar</a></li>');
			$('#no-pet-selected').show();
			$('#pet-history').hide();
		}
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
=======
$(document).ready(function() {
    // Datos de ejemplo para simular la aplicación
    const userData = {
        name: "Juan Pérez",
        email: "juan.perez@example.com",
        phone: "+57 300 123 4567",
        address: "Calle 123 #45-67, Bogotá",
        pets: [
            {
                id: 1,
                name: "Max",
                species: "perro",
                breed: "Golden Retriever",
                age: 3,
                sex: "macho",
                image: "https://images.unsplash.com/photo-1561037404-61cd46aa615b?ixlib=rb-1.2.1&auto=format&fit=crop&w=500&q=60",
                status: "Saludable",
                medicalHistory: [
                    {
                        date: "2023-06-15",
                        type: "consulta",
                        title: "Consulta de rutina",
                        description: "Revisión general, todo en orden. Peso: 28kg. Temperatura normal.",
                        treatment: null,
                        medication: null,
                        vaccines: ["Rabia"],
                        exams: null,
                        notes: "Próxima cita en 6 meses para vacunación anual."
                    },
                    {
                        date: "2023-03-10",
                        type: "tratamiento",
                        title: "Infección de oído",
                        description: "Presentaba enrojecimiento y mal olor en oído derecho.",
                        treatment: "Limpieza de oídos y antibióticos",
                        medication: "Otomax - 2 gotas cada 12 horas por 7 días",
                        vaccines: null,
                        exams: "Cultivo de oído - positivo para bacterias",
                        notes: "Evitar que entre agua en los oídos durante el tratamiento."
                    }
                ],
                treatments: [
                    {
                        id: 1,
                        name: "Control de peso",
                        description: "Dieta especial para mantener peso ideal",
                        startDate: "2023-06-15",
                        endDate: null,
                        status: "en curso",
                        completed: false
                    }
                ],
                recommendations: [
                    {
                        id: 1,
                        date: "2023-06-15",
                        type: "dieta",
                        description: "Reducir croquetas a 1 taza 2 veces al día y aumentar ejercicio",
                        completed: false
                    },
                    {
                        id: 2,
                        date: "2023-06-15",
                        type: "cuidado",
                        description: "Cepillado diario para control de pelo",
                        completed: true
                    }
                ]
            },
            {
                id: 2,
                name: "Luna",
                species: "gato",
                breed: "Siamés",
                age: 2,
                sex: "hembra",
                image: "https://images.unsplash.com/photo-1514888286974-6c03e2ca1dba?ixlib=rb-1.2.1&auto=format&fit=crop&w=500&q=60",
                status: "En tratamiento",
                medicalHistory: [
                    {
                        date: "2023-05-20",
                        type: "consulta",
                        title: "Esterilización",
                        description: "Procedimiento realizado sin complicaciones.",
                        treatment: "Post-operatorio",
                        medication: "Analgésico - 1/2 tableta cada 12 horas por 3 días",
                        vaccines: null,
                        exams: "Pre-quirúrgicos - normales",
                        notes: "Evitar que lama la herida. Usar collar isabelino."
                    }
                ],
                treatments: [
                    {
                        id: 2,
                        name: "Post-operatorio esterilización",
                        description: "Cuidados después de cirugía",
                        startDate: "2023-05-20",
                        endDate: "2023-06-05",
                        status: "finalizado",
                        completed: true
                    }
                ],
                recommendations: [
                    {
                        id: 3,
                        date: "2023-05-20",
                        type: "cuidado",
                        description: "Mantener en reposo por 10 días",
                        completed: true
                    },
                    {
                        id: 4,
                        date: "2023-05-20",
                        type: "dieta",
                        description: "Alimento especial para gatos esterilizados",
                        completed: false
                    }
                ]
            }
        ],
        purchases: [
            {
                id: 1,
                date: "2023-07-10",
                products: [
                    { name: "Alimento Premium para Perro", quantity: 1, price: 45000 },
                    { name: "Shampoo Antipulgas", quantity: 1, price: 32000 }
                ],
                status: "entregado",
                total: 77000
            },
            {
                id: 2,
                date: "2023-06-25",
                products: [
                    { name: "Juguete para Gato", quantity: 2, price: 15000 },
                    { name: "Arena Sanitaria", quantity: 1, price: 28000 }
                ],
                status: "entregado",
                total: 58000
            }
        ],
        appointments: [
            {
                id: 1,
                petId: 1,
                date: "2023-07-15",
                time: "10:00",
                reason: "vacunacion",
                notes: "Vacunación anual",
                diagnosis: "Mascota en perfecto estado de salud. Se aplicó vacuna contra la rabia.",
                status: "finalizada"
            },
            {
                id: 2,
                petId: 2,
                date: "2023-05-20",
                time: "14:30",
                reason: "cirugia",
                notes: "Esterilización",
                diagnosis: "Procedimiento exitoso. Recuperación sin complicaciones.",
                status: "finalizada"
            }
        ],
        reviews: [],
        reports: []
    };

    // Inicializar la aplicación
    initApp();

    // Función para inicializar la aplicación
    function initApp() {
        // Cargar datos del usuario
        $('#username').text(userData.name);
        $('#username-header').text(userData.name.split(' ')[0]);
        
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
            
            // Cargar contenido específico de cada sección
            switch(target) {
                case '#mascotas':
                    loadPetsSection();
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
        
        // Mostrar sección de dashboard por defecto
        $('.nav-link.active').click();
        
        // Cargar resumen rápido
        loadQuickSummary();
        
        // Cargar vista previa de mascotas
        loadPetsPreview();
        
        // Configurar botones
        $('#add-pet-btn').click(function() {
            $('#addPetModal').modal('show');
        });
        
        $('#save-pet-btn').click(function() {
            addNewPet();
        });
        
        $('#new-appointment-btn').click(function() {
            // Redirigir a WhatsApp
            window.open('https://wa.me/573204767864?text=Hola,%20me%20gustaría%20agendar%20una%20cita', '_blank');
        });
        
        $('#go-to-shop-btn').click(function() {
            alert('Redirigiendo a la tienda en línea...');
        });
        
        $('#delete-account-btn').click(function() {
            $('#confirmDeleteModal').modal('show');
        });
        
        // Enviar formulario de perfil
        $('#profile-form').submit(function(e) {
            e.preventDefault();
            saveProfile();
        });
        
        // Enviar formulario de contraseña
        $('#password-form').submit(function(e) {
            e.preventDefault();
            changePassword();
        });
    }
    
    // Función para cargar el resumen rápido
    function loadQuickSummary() {
        if (userData.appointments.length > 0) {
            const nextAppointment = userData.appointments.find(a => a.status === 'agendada');
            if (nextAppointment) {
                const pet = userData.pets.find(p => p.id === nextAppointment.petId);
                $('#next-appointment').text(`${formatDate(nextAppointment.date)}, ${nextAppointment.time} - ${pet.name}: ${getReasonText(nextAppointment.reason)}`);
            }
        }
        
        if (userData.pets.length > 0) {
            const pet = userData.pets[0];
            $('#pet-health').text(`${pet.name} - ${pet.status}`);
        }
        
        if (userData.purchases.length > 0) {
            const lastPurchase = userData.purchases[0];
            $('#last-purchase').text(`${formatDate(lastPurchase.date)} - ${lastPurchase.products[0].name} (${lastPurchase.products[0].quantity}${lastPurchase.products[0].quantity > 1 ? ' unidades' : ' unidad'})`);
        }
        
        if (userData.pets.length > 0 && userData.pets[0].recommendations.length > 0) {
            const recommendation = userData.pets[0].recommendations[0];
            $('#vet-recommendation').text(`Recordatorio: ${recommendation.description}`);
        }
    }
    
    // Función para cargar vista previa de mascotas
    function loadPetsPreview() {
        $('#pets-preview').empty();
        
        userData.pets.slice(0, 3).forEach(pet => {
            $('#pets-preview').append(`
                <div class="col-md-4">
                    <div class="pet-card">
                        <img src="${pet.image}" alt="${pet.name}" class="pet-img">
                        <h5>${pet.name}</h5>
                        <p>${capitalizeFirstLetter(pet.species)} · ${pet.breed} · ${pet.age} años</p>
                        <span class="pet-status ${getStatusClass(pet.status)}">${pet.status}</span>
                    </div>
                </div>
            `);
        });
        
        if (userData.pets.length > 3) {
            $('#pets-preview').append(`
                <div class="col-md-4">
                    <div class="pet-card text-center d-flex align-items-center justify-content-center" style="height: 100%;">
                        <div>
                            <i class="fas fa-paw fa-3x mb-3"></i>
                            <h5>+${userData.pets.length - 3} mascotas</h5>
                            <a href="#mascotas" class="btn btn-sm btn-outline-primary">Ver todas</a>
                        </div>
                    </div>
                </div>
            `);
        }
    }
    
    // Función para cargar sección de mascotas
    function loadPetsSection() {
        $('#pets-list').empty();
        
        userData.pets.forEach(pet => {
            $('#pets-list').append(`
                <div class="col-md-6 col-lg-4">
                    <div class="card pet-card">
                        <img src="${pet.image}" class="card-img-top" alt="${pet.name}">
                        <div class="card-body">
                            <h5 class="card-title">${pet.name}</h5>
                            <p class="card-text">
                                <strong>Especie:</strong> ${capitalizeFirstLetter(pet.species)}<br>
                                <strong>Raza:</strong> ${pet.breed}<br>
                                <strong>Edad:</strong> ${pet.age} años<br>
                                <strong>Sexo:</strong> ${capitalizeFirstLetter(pet.sex)}
                            </p>
                            <span class="badge ${getStatusClass(pet.status)} mb-3">${pet.status}</span>
                        </div>
                    </div>
                </div>
            `);
        });
    }
    
    // Función para cargar sección de historia clínica
    function loadMedicalHistorySection() {
        // Llenar dropdown de mascotas
        $('#pet-dropdown').empty();
        userData.pets.forEach(pet => {
            $('#pet-dropdown').append(`
                <li><a class="dropdown-item" href="#" onclick="showPetHistory(${pet.id})">${pet.name}</a></li>
            `);
        });
        
        // Mostrar primera mascota por defecto si existe
        if (userData.pets.length > 0) {
            showPetHistory(userData.pets[0].id);
        }
    }
    
    // Función para mostrar historia clínica de una mascota
    window.showPetHistory = function(petId) {
        const pet = userData.pets.find(p => p.id === petId);
        if (!pet) return;
        
        $('#no-pet-selected').hide();
        $('#pet-history').show();
        
        // Actualizar información básica
        $('#pet-history-image').attr('src', pet.image);
        $('#pet-history-name').text(pet.name);
        $('#pet-history-info').text(`${capitalizeFirstLetter(pet.species)} · ${pet.breed} · ${pet.age} años · ${capitalizeFirstLetter(pet.sex)}`);
        $('#pet-history-status').text(pet.status);
        $('#pet-history-status').removeClass('bg-success bg-warning bg-info').addClass(getStatusClass(pet.status));
        
        // Llenar timeline
        $('.timeline').empty();
        
        // Ordenar historial por fecha (más reciente primero)
        const sortedHistory = [...pet.medicalHistory].sort((a, b) => new Date(b.date) - new Date(a.date));
        
        sortedHistory.forEach(record => {
            let content = '';
            
            if (record.type === 'consulta') {
                content = `<p><strong>Motivo:</strong> ${record.title}</p>
                           <p>${record.description}</p>`;
            } else if (record.type === 'tratamiento') {
                content = `<p><strong>Tratamiento:</strong> ${record.treatment}</p>
                           <p><strong>Descripción:</strong> ${record.description}</p>`;
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
                    <h6 class="timeline-title">${record.title}</h6>
                    <div class="timeline-content">
                        ${content}
                    </div>
                </div>
            `);
        });
        
        // Actualizar texto del selector
        $('#pet-selector').html(`<i class="fas fa-paw me-1"></i>${pet.name}`);
    }
    
    // Función para cargar sección de tratamientos
    function loadTreatmentsSection() {
        // Llenar dropdown de mascotas
        $('#treatment-pet-dropdown').empty();
        userData.pets.forEach(pet => {
            $('#treatment-pet-dropdown').append(`
                <li><a class="dropdown-item" href="#" onclick="showPetTreatments(${pet.id})">${pet.name}</a></li>
            `);
        });
        
        // Mostrar primera mascota por defecto si existe
        if (userData.pets.length > 0) {
            showPetTreatments(userData.pets[0].id);
        }
    }
    
    // Función para mostrar tratamientos de una mascota
    window.showPetTreatments = function(petId) {
        const pet = userData.pets.find(p => p.id === petId);
        if (!pet) return;
        
        $('#no-treatment-pet-selected').hide();
        $('#pet-treatments').show();
        
        // Llenar tratamientos activos
        $('#active-treatments').empty();
        
        const activeTreatments = pet.treatments.filter(t => t.status !== 'finalizado');
        const completedTreatments = pet.treatments.filter(t => t.status === 'finalizado');
        
        if (activeTreatments.length === 0 && completedTreatments.length === 0) {
            $('#active-treatments').append(`
                <div class="text-center py-3">
                    <i class="fas fa-check-circle fa-2x text-muted mb-3"></i>
                    <h5>No hay tratamientos</h5>
                    <p class="text-muted">${pet.name} no tiene tratamientos registrados</p>
                </div>
            `);
        } else {
            // Mostrar tratamientos activos
            activeTreatments.forEach(treatment => {
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
            
            // Mostrar tratamientos completados
            completedTreatments.forEach(treatment => {
                $('#active-treatments').append(`
                    <div class="treatment-card completed mb-3">
                        <h6>${treatment.name}</h6>
                        <p>${treatment.description}</p>
                        <div class="treatment-meta">
                            <span class="me-3"><i class="fas fa-calendar-alt me-1"></i>Iniciado: ${formatDate(treatment.startDate)}</span>
                            <span><i class="fas fa-calendar-check me-1"></i>Finalizado: ${formatDate(treatment.endDate)}</span>
                        </div>
                    </div>
                `);
            });
        }
        
        // Actualizar texto del selector
        $('#treatment-pet-selector').html(`<i class="fas fa-paw me-1"></i>${pet.name}`);
    }
    
    // Función para cargar sección de compras
    function loadPurchasesSection() {
        $('#purchase-history').empty();
        
        if (userData.purchases.length === 0) {
            $('#purchase-history').append(`
                <tr>
                    <td colspan="5" class="text-center py-4">
                        <i class="fas fa-shopping-cart fa-2x mb-3"></i>
                        <h4>No has realizado ninguna compra</h4>
                        <button class="btn btn-primary mt-2" id="go-to-shop-btn"><i class="fas fa-store me-2"></i>Ir a la Tienda</button>
                    </td>
                </tr>
            `);
        } else {
            userData.purchases.forEach(purchase => {
                const productsList = purchase.products.map(p => `${p.name} (x${p.quantity})`).join(', ');
                
                $('#purchase-history').append(`
>>>>>>> 21ec2e8465c2f8d5e6595cb364bf21309a264dc8
                    <tr>
                        <td>${formatDate(purchase.date)}</td>
                        <td>${productsList}</td>
                        <td>$${purchase.total.toLocaleString()}</td>
                        <td><span class="badge ${getStatusBadgeClass(purchase.status)}">${capitalizeFirstLetter(purchase.status)}</span></td>
                        <td>
<<<<<<< HEAD
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
	async function loadSettingsSection() {
		if (!currentUserId) return;

		try {
			const response = await fetch(`${API_BASE_URL}/usuarios/${currentUserId}`); // Asume un endpoint para obtener el usuario
			if (!response.ok) {
				throw new Error(`HTTP error! status: ${response.status}`);
			}
			const userData = await response.json();

			// Precargar formularios con los datos del usuario
			$('#profile-name').val(userData.nombre);
			$('#profile-email').val(userData.correo);
			$('#profile-phone').val(userData.telefono || '');
			$('#profile-address').val(userData.direccion || '');

			// Actualizar nombre en el sidebar y header
			$('#username').text(userData.nombre);
			$('#username-header').text(userData.nombre.split(' ')[0]);

			// Configurar switches de notificaciones (asume que userData incluye preferencias)
			$('#notif-appointments').prop('checked', userData.notifAppointments || true);
			$('#notif-treatments').prop('checked', userData.notifTreatments || true);
			$('#notif-products').prop('checked', userData.notifProducts || true);
			$('#notif-news').prop('checked', userData.notifNews || true);

		} catch (error) {
			console.error('Error al cargar la sección de configuración:', error);
			alert('Error al cargar la configuración del perfil.');
		}
	}

	// --- FUNCIONES PARA ENVIAR DATOS AL BACKEND (AJAX) ---

	// Función para guardar perfil (AJAX)
	async function saveProfile() {
		if (!currentUserId) return;

		const name = $('#profile-name').val();
		const email = $('#profile-email').val();
		const phone = $('#profile-phone').val();
		const address = $('#profile-address').val();

		if (!name || !email) {
			alert('Por favor completa los campos obligatorios: Nombre y Correo electrónico');
			return;
		}

		try {
			const response = await fetch(`${API_BASE_URL}/usuarios/${currentUserId}`, {
				method: 'PUT', // O PATCH, dependiendo de tu API
				headers: {
					'Content-Type': 'application/json'
				},
				body: JSON.stringify({
					nombre: name,
					correo: email,
					telefono: phone,
					direccion: address
				})
			});

			if (!response.ok) {
				throw new Error(`HTTP error! status: ${response.status}`);
			}

			const updatedUser = await response.json();
			$('#username').text(updatedUser.nombre);
			$('#username-header').text(updatedUser.nombre.split(' ')[0]);
			alert('Perfil actualizado exitosamente');

		} catch (error) {
			console.error('Error al guardar el perfil:', error);
			alert('Hubo un error al actualizar el perfil.');
		}
	}

	// Función para cambiar contraseña (AJAX)
	async function changePassword() {
		if (!currentUserId) return;

		const currentPassword = $('#current-password').val();
		const newPassword = $('#new-password').val();
		const confirmPassword = $('#confirm-password').val();

		if (!currentPassword || !newPassword || !confirmPassword) {
			alert('Por favor completa todos los campos');
			return;
		}

		if (newPassword !== confirmPassword) {
			alert('Las contraseñas nuevas no coinciden');
			return;
		}

		try {
			const response = await fetch(`${API_BASE_URL}/usuarios/${currentUserId}/cambiar-password`, { // Asume un endpoint
				method: 'POST',
				headers: {
					'Content-Type': 'application/json'
				},
				body: JSON.stringify({
					currentPassword: currentPassword,
					newPassword: newPassword
				})
			});

			if (!response.ok) {
				const errorData = await response.json();
				throw new Error(errorData.message || `HTTP error! status: ${response.status}`);
			}

			$('#password-form')[0].reset();
			alert('Contraseña cambiada exitosamente');

		} catch (error) {
			console.error('Error al cambiar la contraseña:', error);
			alert('Hubo un error al cambiar la contraseña: ' + error.message);
		}
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
// Función para redirigir a formulario de edición pasando el id de la mascota
function editarMascota(mascotaId) {
	// Aquí puedes redirigir a un formulario dedicado a la edición
	// Ejemplo, si tienes una ruta para editar:
	window.location.href = `/usuarios/perfilusuario/editarMascota?id=${mascotaId}`;
}


// Función para eliminar mascota con confirmación
function eliminarMascota(mascotaId) {
	if (confirm('¿Estás seguro que deseas eliminar esta mascota? Esta acción es irreversible.')) {
		$.ajax({
			url: `/usuarios/perfilusuario/eliminarMascota`,  // Endpoint en backend que procesa la eliminación
			type: 'POST',
			data: { id: mascotaId },
			success: function(response) {
				if (response.success) {
					alert('Mascota eliminada correctamente.');
					location.reload();  // Recarga la página para actualizar la lista
				} else {
					alert('Error al eliminar la mascota: ' + response.message);
				}
			},
			error: function() {
				alert('Error en el servidor al intentar eliminar la mascota.');
			}
		});
	}
}

=======
                            <button class="btn btn-sm btn-outline-primary" onclick="viewPurchaseDetails(${purchase.id})">Ver Detalles</button>
                        </td>
                    </tr>
                `);
            });
        }
    }
    
    // Función para ver detalles de compra
    window.viewPurchaseDetails = function(purchaseId) {
        const purchase = userData.purchases.find(p => p.id === purchaseId);
        if (!purchase) return;
        
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
    }
    
    // Función para cargar sección de citas
    function loadAppointmentsSection() {
        $('#past-appointments').empty();
        
        const now = new Date();
        const past = userData.appointments.filter(a => new Date(a.date) < now && a.status !== 'agendada');
        
        if (past.length === 0) {
            $('#past-appointments').append(`
                <div class="text-center py-3">
                    <i class="fas fa-history fa-2x text-muted mb-3"></i>
                    <h5>No hay citas pasadas</h5>
                    <p class="text-muted">No tienes historial de citas veterinarias</p>
                </div>
            `);
        } else {
            past.forEach(appointment => {
                const pet = userData.pets.find(p => p.id === appointment.petId);
                $('#past-appointments').append(`
                    <div class="appointment-card mb-3 p-3 border rounded">
                        <div class="d-flex justify-content-between">
                            <div>
                                <h6>${pet.name} - ${getReasonText(appointment.reason)}</h6>
                                <p class="mb-1"><i class="fas fa-calendar-alt me-1"></i>${formatDate(appointment.date)} a las ${appointment.time}</p>
                                ${appointment.notes ? `<p class="mb-1"><i class="fas fa-sticky-note me-1"></i>${appointment.notes}</p>` : ''}
                            </div>
                            <div class="text-end">
                                <span class="badge ${getStatusBadgeClass(appointment.status)} mb-2">${capitalizeFirstLetter(appointment.status)}</span>
                                <button class="btn btn-sm btn-outline-primary" onclick="viewAppointmentDetails(${appointment.id})">
                                    <i class="fas fa-eye me-1"></i>Ver Detalles
                                </button>
                            </div>
                        </div>
                    </div>
                `);
            });
        }
    }
    
    // Función para ver detalles de cita
    window.viewAppointmentDetails = function(appointmentId) {
        const appointment = userData.appointments.find(a => a.id === appointmentId);
        if (!appointment) return;
        
        const pet = userData.pets.find(p => p.id === appointment.petId);
        
        $('#appointment-details-content').empty();
        
        $('#appointment-details-content').html(`
            <div class="appointment-detail">
                <span class="appointment-detail-label">Mascota:</span>
                <span class="appointment-detail-value">${pet.name}</span>
            </div>
            <div class="appointment-detail">
                <span class="appointment-detail-label">Fecha:</span>
                <span class="appointment-detail-value">${formatDate(appointment.date)} a las ${appointment.time}</span>
            </div>
            <div class="appointment-detail">
                <span class="appointment-detail-label">Motivo:</span>
                <span class="appointment-detail-value">${getReasonText(appointment.reason)}</span>
            </div>
            <div class="appointment-detail">
                <span class="appointment-detail-label">Notas:</span>
                <span class="appointment-detail-value">${appointment.notes || 'No hay notas adicionales'}</span>
            </div>
            <div class="appointment-detail">
                <span class="appointment-detail-label">Diagnóstico:</span>
                <span class="appointment-detail-value">${appointment.diagnosis || 'No hay diagnóstico registrado'}</span>
            </div>
        `);
        
        $('#appointmentDetailsModal').modal('show');
    }
    
    // Función para cargar sección de configuración
    function loadSettingsSection() {
        // Los formularios ya están precargados con los datos del usuario
    }
    
    // Función para agregar nueva mascota
    function addNewPet() {
        const name = $('#pet-name').val();
        const species = $('#pet-species').val();
        const breed = $('#pet-breed').val();
        const age = $('#pet-age').val();
        const sex = $('#pet-sex').val();
        
        if (!name || !species) {
            alert('Por favor completa los campos obligatorios: Nombre y Especie');
            return;
        }
        
        const newPet = {
            id: userData.pets.length + 1,
            name,
            species,
            breed: breed || 'Desconocida',
            age: age ? parseInt(age) : 0,
            sex: sex || 'desconocido',
            image: 'https://via.placeholder.com/150?text=' + name.charAt(0),
            status: 'Saludable',
            medicalHistory: [],
            treatments: [],
            recommendations: []
        };
        
        userData.pets.push(newPet);
        $('#addPetModal').modal('hide');
        $('#add-pet-form')[0].reset();
        
        // Actualizar las secciones relevantes
        loadPetsPreview();
        loadPetsSection();
        loadMedicalHistorySection();
        loadTreatmentsSection();
        
        alert(`${name} ha sido agregado a tus mascotas exitosamente!`);
    }
    
    // Función para guardar perfil
    function saveProfile() {
        const name = $('#profile-name').val();
        const email = $('#profile-email').val();
        const phone = $('#profile-phone').val();
        const address = $('#profile-address').val();
        
        if (!name || !email) {
            alert('Por favor completa los campos obligatorios: Nombre y Correo electrónico');
            return;
        }
        
        userData.name = name;
        userData.email = email;
        userData.phone = phone;
        userData.address = address;
        
        $('#username').text(name);
        $('#username-header').text(name.split(' ')[0]);
        
        alert('Perfil actualizado exitosamente');
    }
    
    // Función para cambiar contraseña
    function changePassword() {
        const currentPassword = $('#current-password').val();
        const newPassword = $('#new-password').val();
        const confirmPassword = $('#confirm-password').val();
        
        if (!currentPassword || !newPassword || !confirmPassword) {
            alert('Por favor completa todos los campos');
            return;
        }
        
        if (newPassword !== confirmPassword) {
            alert('Las contraseñas nuevas no coinciden');
            return;
        }
        
        // En una aplicación real, aquí se haría una verificación con el servidor
        $('#password-form')[0].reset();
        alert('Contraseña cambiada exitosamente');
    }
    
    // Funciones auxiliares
    function formatDate(dateString) {
        const options = { year: 'numeric', month: 'long', day: 'numeric' };
        return new Date(dateString).toLocaleDateString('es-ES', options);
    }
    
    function capitalizeFirstLetter(string) {
        return string.charAt(0).toUpperCase() + string.slice(1);
    }
    
    function getStatusClass(status) {
        if (status.toLowerCase().includes('saludable')) return 'bg-success';
        if (status.toLowerCase().includes('tratamiento')) return 'bg-warning';
        if (status.toLowerCase().includes('seguimiento')) return 'bg-info';
        return 'bg-secondary';
    }
    
    function getStatusBadgeClass(status) {
        if (status === 'agendada') return 'bg-primary';
        if (status === 'finalizada') return 'bg-success';
        if (status === 'cancelada') return 'bg-danger';
        if (status === 'entregado') return 'bg-success';
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
        return reasons[reason] || reason;
    }
});
>>>>>>> 21ec2e8465c2f8d5e6595cb364bf21309a264dc8
