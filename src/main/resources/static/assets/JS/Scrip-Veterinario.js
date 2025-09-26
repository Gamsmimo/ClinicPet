// Toggle del menú lateral en móviles
const btnMenu = document.querySelector('.btn-menu');
const btnCloseMenu = document.querySelector('.btn-close-menu');
const sidebar = document.querySelector('.sidebar');
const mainContent = document.querySelector('.main-content');

btnMenu.addEventListener('click', () => {
    sidebar.classList.add('active');
});

btnCloseMenu.addEventListener('click', () => {
    sidebar.classList.remove('active');
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