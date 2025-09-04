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
                    <tr>
                        <td>${formatDate(purchase.date)}</td>
                        <td>${productsList}</td>
                        <td>$${purchase.total.toLocaleString()}</td>
                        <td><span class="badge ${getStatusBadgeClass(purchase.status)}">${capitalizeFirstLetter(purchase.status)}</span></td>
                        <td>
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