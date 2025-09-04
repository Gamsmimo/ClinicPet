// Toggle del menú hamburguesa
document.querySelector('.hamburger-btn').addEventListener('click', function () {
    document.querySelector('.menu-content').classList.toggle('active');
});

// Cerrar menú al hacer clic fuera de él
document.addEventListener('click', function (event) {
    const menu = document.querySelector('.menu-content');
    const btn = document.querySelector('.hamburger-btn');

    if (!menu.contains(event.target) && !btn.contains(event.target) && menu.classList.contains('active')) {
        menu.classList.remove('active');
    }
});

// Función de búsqueda
document.addEventListener('DOMContentLoaded', function () {
    const searchInput = document.getElementById('searchInput');
    const searchResults = document.getElementById('searchResults');
    let currentHighlights = [];

    // Función para extraer texto alrededor de la coincidencia
    function getTextExcerpt(text, query, radius = 30) {
        const index = text.toLowerCase().indexOf(query.toLowerCase());
        if (index === -1) return null;

        const start = Math.max(0, index - radius);
        const end = Math.min(text.length, index + query.length + radius);
        let excerpt = text.substring(start, end);

        // Añadir puntos suspensivos si no estamos al inicio/final
        if (start > 0) excerpt = '...' + excerpt;
        if (end < text.length) excerpt = excerpt + '...';

        return excerpt;
    }

    // Función para resaltar texto en un string (sin modificar DOM)
    function highlightText(text, query) {
        const regex = new RegExp(escapeRegExp(query), 'gi');
        return text.replace(regex, match => `<span class="highlight">${match}</span>`);
    }

    function searchContent(query) {
        // Limpiar resultados anteriores
        searchResults.innerHTML = '';
        clearHighlights();

        if (query.length < 2) {
            searchResults.classList.remove('active');
            return;
        }

        const searchableElements = document.querySelectorAll('[data-searchable]');
        const results = [];
        const regex = new RegExp(escapeRegExp(query), 'gi');

        searchableElements.forEach(element => {
            const text = element.textContent;
            if (regex.test(text)) {
                const excerpt = getTextExcerpt(text, query);
                if (!excerpt) return;

                const titleElement = element.querySelector('h2, h3');
                const title = titleElement ? titleElement.textContent : 'Sección sin título';

                results.push({
                    element,
                    title,
                    excerpt: highlightText(excerpt, query),
                    matches: text.match(regex).length
                });
            }
        });

        displayResults(results);
    }

    function displayResults(results) {
        if (results.length === 0) {
            const noResults = document.createElement('div');
            noResults.className = 'search-result-item';
            noResults.textContent = 'No se encontraron resultados';
            searchResults.appendChild(noResults);
        } else {
            results.forEach(result => {
                const resultItem = document.createElement('div');
                resultItem.className = 'search-result-item';
                resultItem.innerHTML = `
                    <h6>${result.title} <small class="text-muted">(${result.matches} coincidencias)</small></h6>
                    <p>${result.excerpt}</p>
                `;

                resultItem.addEventListener('click', function () {
                    result.element.scrollIntoView({
                        behavior: 'smooth',
                        block: 'center'
                    });

                    // Efecto temporal de resaltado
                    const originalBg = result.element.style.backgroundColor;
                    result.element.style.backgroundColor = 'var(--azul-pastel)';

                    setTimeout(() => {
                        result.element.style.backgroundColor = originalBg;
                    }, 2000);

                    searchResults.classList.remove('active');
                });

                searchResults.appendChild(resultItem);
            });
        }

        searchResults.classList.toggle('active', results.length > 0);
    }

    function clearHighlights() {
        // No necesitamos limpiar nada ya que no modificamos el DOM
        currentHighlights = [];
    }

    function escapeRegExp(string) {
        return string.replace(/[.*+?^${}()|[\]\\]/g, '\\$&');
    }

    // Event listeners
    searchInput.addEventListener('input', function () {
        searchContent(this.value.trim());
    });

    searchInput.addEventListener('focus', function () {
        if (this.value.trim().length >= 2) {
            searchResults.classList.add('active');
        }
    });

    document.addEventListener('click', function (e) {
        if (!searchInput.contains(e.target) && !searchResults.contains(e.target)) {
            searchResults.classList.remove('active');
        }
    });

    searchInput.addEventListener('keypress', function (e) {
        if (e.key === 'Enter') {
            const firstResult = searchResults.querySelector('.search-result-item');
            if (firstResult) {
                firstResult.click();
            }
        }
    });
});


//calendario flotante

document.addEventListener('DOMContentLoaded', function () {
    const calendarButton = document.getElementById('calendarButton');
    const calendarPopup = document.getElementById('calendarPopup');
    const prevMonthBtn = document.getElementById('prevMonth');
    const nextMonthBtn = document.getElementById('nextMonth');
    const todayBtn = document.getElementById('todayBtn');
    const agendarCitaBtn = document.getElementById('agendarCitaBtn');
    const currentMonthYear = document.getElementById('currentMonthYear');
    const calendarDays = document.getElementById('calendarDays');

    let currentDate = new Date();
    let selectedDate = new Date();

    // Mostrar/ocultar calendario
    calendarButton.addEventListener('click', function (e) {
        e.stopPropagation();
        calendarPopup.classList.toggle('active');
        renderCalendar();
    });

    // Cerrar calendario al hacer clic fuera
    document.addEventListener('click', function () {
        calendarPopup.classList.remove('active');
    });

    calendarPopup.addEventListener('click', function (e) {
        e.stopPropagation();
    });

    // Navegación del calendario
    prevMonthBtn.addEventListener('click', function () {
        currentDate.setMonth(currentDate.getMonth() - 1);
        renderCalendar();
    });

    nextMonthBtn.addEventListener('click', function () {
        currentDate.setMonth(currentDate.getMonth() + 1);
        renderCalendar();
    });

    todayBtn.addEventListener('click', function () {
        currentDate = new Date();
        selectedDate = new Date();
        renderCalendar();
    });

    // Agendar cita por WhatsApp
    agendarCitaBtn.addEventListener('click', function () {
        const formattedDate = selectedDate.toLocaleDateString('es-ES', {
            weekday: 'long',
            year: 'numeric',
            month: 'long',
            day: 'numeric'
        });

        // Número de WhatsApp (reemplaza con el número real)
        const phoneNumber = "573204767864"; // Ejemplo: número de Colombia

        // Mensaje predefinido
        const message = `¡Hola! Quiero agendar una cita para mi mascota el día ${formattedDate}. Por favor confírmame disponibilidad.`;

        // Codificar el mensaje para URL
        const encodedMessage = encodeURIComponent(message);

        // Redirigir a WhatsApp
        window.open(`https://wa.me/${phoneNumber}?text=${encodedMessage}`, '_blank');

        calendarPopup.classList.remove('active');
    });

    // Renderizar calendario
    function renderCalendar() {
        const year = currentDate.getFullYear();
        const month = currentDate.getMonth();

        currentMonthYear.textContent = currentDate.toLocaleDateString('es-ES', {
            month: 'long',
            year: 'numeric'
        });

        const firstDay = new Date(year, month, 1);
        const lastDay = new Date(year, month + 1, 0);
        const daysInMonth = lastDay.getDate();
        const startingDay = firstDay.getDay();

        calendarDays.innerHTML = '';

        // Días del mes anterior
        const prevMonthLastDay = new Date(year, month, 0).getDate();
        for (let i = 0; i < startingDay; i++) {
            const dayElement = document.createElement('div');
            dayElement.className = 'calendar-day other-month';
            dayElement.textContent = prevMonthLastDay - startingDay + i + 1;
            calendarDays.appendChild(dayElement);
        }

        // Días del mes actual
        const today = new Date();
        for (let i = 1; i <= daysInMonth; i++) {
            const dayElement = document.createElement('div');
            dayElement.className = 'calendar-day';
            dayElement.textContent = i;

            // Marcar día actual
            if (i === today.getDate() && month === today.getMonth() && year === today.getFullYear()) {
                dayElement.classList.add('current-day');
            }

            // Marcar día seleccionado
            if (i === selectedDate.getDate() && month === selectedDate.getMonth() && year === selectedDate.getFullYear()) {
                dayElement.style.backgroundColor = 'var(--verde-suave)';
                dayElement.style.fontWeight = 'bold';
            }

            dayElement.addEventListener('click', function () {
                selectedDate = new Date(year, month, i);
                renderCalendar();
            });

            calendarDays.appendChild(dayElement);
        }

        // Días del siguiente mes
        const daysToShow = 42 - (startingDay + daysInMonth);
        for (let i = 1; i <= daysToShow; i++) {
            const dayElement = document.createElement('div');
            dayElement.className = 'calendar-day other-month';
            dayElement.textContent = i;
            calendarDays.appendChild(dayElement);
        }
    }

    // Inicializar calendario
    renderCalendar();
});


// Datos iniciales de mascotas (simulando una base de datos)
const mascotas = [
    {
        id: 1,
        nombre: "Max",
        tipo: "perro",
        raza: "Labrador",
        edad: 2,
        tamanio: "mediano",
        descripcion: "Max es un perro muy juguetón y cariñoso. Le encanta pasear y jugar con niños.",
        foto: "img/max.jpg",
        contacto: "3222473652" // Ahora usamos número de teléfono
    },
    {
        id: 2,
        nombre: "Luna",
        tipo: "gato",
        raza: "Siamés",
        edad: 1,
        tamanio: "pequeno",
        descripcion: "Luna es una gata tranquila que disfruta de los mimos y dormir en lugares cálidos.",
        foto: "img/Luna.webp",
        contacto: "5557654321"
    },
    {
        id: 3,
        nombre: "Copito",
        tipo: "gato",
        raza: "Siamés",
        edad: 2,
        tamanio: "pequeno",
        descripcion: "Copito es un gato tranquilo que le gusta jugar con estambre.",
        foto: "img/Copito.png",
        contacto: "5557654321"
    },
    {
        id: 4,
        nombre: "Rocky",
        tipo: "perro",
        raza: "Bulldog Francés",
        edad: 2,
        tamanio: "pequeño",
        descripcion: "Rocky es tranquilo y muy leal, perfecto para vivir en apartamento.",
        foto: "img/Rocky.jpg",
        contacto: "5552468101"
    },
    {
        id: 5,
        nombre: "Tor",
        tipo: "gato",
        raza: "Maine Coon",
        edad: 2,
        tamanio: "grande",
        descripcion: "Tor es un gatito cariñoso y muy curioso, le encanta explorar la casa.",
        foto: "img/Tor.jpg",
        contacto: "5557654321"
    },
    {
        id: 6,
        nombre: "Mila",
        tipo: "gato",
        raza: "Bengala",
        edad: 1,
        tamanio: "mediano",
        descripcion: "Mila es muy activa y le encanta trepar y jugar todo el día.",
        foto: "img/Mila.jpg",
        contacto: "5551357913"
    },
    {
        id: 7,
        nombre: "Simba",
        tipo: "perro",
        raza: "Golden Retriever",
        edad: 5,
        tamanio: "grande",
        descripcion: "Simba es un amoroso compañero que disfruta los paseos largos.",
        foto: "img/Simba.jpg",
        contacto: "5553214567"
    },
    {
        id: 8,
        nombre: "Nina",
        tipo: "gato",
        raza: "Persa",
        edad: 6,
        tamanio: "mediano",
        descripcion: "Nina es calmada y elegante, ideal para hogares tranquilos.",
        foto: "img/Nina.webp",
        contacto: "5556547890"
    },
    {
        id: 9,
        nombre: "Toby",
        tipo: "perro",
        raza: "Beagle",
        edad: 1,
        tamanio: "mediano",
        descripcion: "Toby es curioso, alegre y se lleva bien con otros animales.",
        foto: "img/Tobi.jpeg",
        contacto: "5557778888"
    },
];

// Clase para manejar las mascotas
class GestorMascotas {
    constructor() {
        this.mascotas = mascotas;
        this.cargarMascotas();
        this.configurarEventos();
    }

    cargarMascotas(filtroTipo = 'todos', filtroTamanio = 'todos') {
        const listaMascotas = document.getElementById('lista-mascotas');
        listaMascotas.innerHTML = '';

        const mascotasFiltradas = this.mascotas.filter(mascota => {
            const cumpleTipo = filtroTipo === 'todos' || mascota.tipo === filtroTipo;
            const cumpleTamanio = filtroTamanio === 'todos' || mascota.tamanio === filtroTamanio;
            return cumpleTipo && cumpleTamanio;
        });

        if (mascotasFiltradas.length === 0) {
            listaMascotas.innerHTML = '<p class="sin-resultados">No se encontraron mascotas con estos filtros.</p>';
            return;
        }

        mascotasFiltradas.forEach(mascota => {
            const tarjeta = this.crearTarjetaMascota(mascota);
            listaMascotas.appendChild(tarjeta);
        });
    }

    crearTarjetaMascota(mascota) {
        const tarjeta = document.createElement('div');
        tarjeta.className = 'tarjeta-mascota';

        const imagen = document.createElement('img');
        imagen.className = 'imagen-mascota';
        imagen.src = mascota.foto || 'https://via.placeholder.com/300x200?text=Sin+imagen';
        imagen.alt = `Foto de ${mascota.nombre}`;

        const contenido = document.createElement('div');
        contenido.className = 'contenido-mascota';

        const nombre = document.createElement('h3');
        nombre.textContent = mascota.nombre;

        const tipo = document.createElement('span');
        tipo.className = 'etiqueta etiqueta-tipo';
        tipo.textContent = mascota.tipo === 'perro' ? 'Perro' : mascota.tipo === 'gato' ? 'Gato' : 'Otro';

        const tamanio = document.createElement('span');
        tamanio.className = 'etiqueta etiqueta-tamanio';
        let tamanioTexto = '';
        if (mascota.tamanio === 'pequeno') tamanioTexto = 'Pequeño';
        else if (mascota.tamanio === 'mediano') tamanioTexto = 'Mediano';
        else tamanioTexto = 'Grande';
        tamanio.textContent = tamanioTexto;

        const edad = document.createElement('span');
        edad.className = 'etiqueta etiqueta-edad';
        edad.textContent = `${mascota.edad} ${mascota.edad === 1 ? 'año' : 'años'}`;

        const raza = document.createElement('p');
        raza.textContent = mascota.raza ? `Raza: ${mascota.raza}` : 'Raza: Mestizo';

        const descripcion = document.createElement('p');
        descripcion.textContent = mascota.descripcion;

        const botonAdoptar = document.createElement('a');
        botonAdoptar.className = 'boton-adoptar';
        botonAdoptar.href = `https://wa.me/${this.obtenerNumeroWhatsApp(mascota.contacto)}?text=${encodeURIComponent(`Hola, estoy interesado/a en adoptar a ${mascota.nombre}. Por favor, ¿podrías darme más información?`)}`;
        botonAdoptar.target = '_blank';
        botonAdoptar.textContent = '¡Quiero adoptar!';

        contenido.appendChild(nombre);
        contenido.appendChild(tipo);
        contenido.appendChild(tamanio);
        contenido.appendChild(edad);
        contenido.appendChild(raza);
        contenido.appendChild(descripcion);
        contenido.appendChild(botonAdoptar);

        tarjeta.appendChild(imagen);
        tarjeta.appendChild(contenido);

        return tarjeta;
    }

    obtenerNumeroWhatsApp(contacto) {
        // Extraer solo los números del contacto
        const soloNumeros = contacto.replace(/\D/g, '');

        // Si ya tiene código de país, devolverlo
        if (soloNumeros.length > 10) return soloNumeros;

        // Código deL país (+57)
        return '57' + soloNumeros;
    }

    agregarMascota(nuevaMascota) {
        // Generar un ID único
        nuevaMascota.id = this.mascotas.length > 0 ?
            Math.max(...this.mascotas.map(m => m.id)) + 1 : 1;

        this.mascotas.unshift(nuevaMascota);
        this.cargarMascotas();

        // Mostrar mensaje de éxito
        alert(`¡${nuevaMascota.nombre} ha sido publicado para adopción con éxito!`);
    }

    configurarEventos() {
        // Filtrar mascotas
        document.getElementById('filtrar').addEventListener('click', () => {
            const tipo = document.getElementById('tipo-mascota').value;
            const tamanio = document.getElementById('tamanio-mascota').value;
            this.cargarMascotas(tipo, tamanio);
        });

        // Formulario para dar en adopción
        document.getElementById('formulario-mascota').addEventListener('submit', (e) => {
            e.preventDefault();

            const nuevaMascota = {
                nombre: document.getElementById('nombre').value,
                tipo: document.getElementById('tipo').value,
                raza: document.getElementById('raza').value,
                edad: parseInt(document.getElementById('edad').value) || 0,
                tamanio: document.getElementById('tamanio').value,
                descripcion: document.getElementById('descripcion').value,
                foto: document.getElementById('foto').value || 'https://via.placeholder.com/300x200?text=Sin+imagen',
                contacto: document.getElementById('contacto').value
            };

            this.agregarMascota(nuevaMascota);

            // Limpiar formulario
            e.target.reset();

            // Desplazarse a la sección de mascotas
            document.getElementById('adoptar').scrollIntoView({ behavior: 'smooth' });
        });

        // Smooth scrolling para los enlaces del menú
        document.querySelectorAll('a[href^="#"]').forEach(anchor => {
            anchor.addEventListener('click', function (e) {
                e.preventDefault();
                document.querySelector(this.getAttribute('href')).scrollIntoView({
                    behavior: 'smooth'
                });
            });
        });
    }
}

// Inicializar la aplicación cuando el DOM esté listo
document.addEventListener('DOMContentLoaded', () => {
    new GestorMascotas();
});

// Función para abrir el modal
function abrirModal() {
    document.getElementById("modalConsejos").style.display = "block";
}

// Función para cerrar el modal
function cerrarModal() {
    document.getElementById("modalConsejos").style.display = "none";
}

// Event listener para el botón de consejos
document.getElementById("Consejos").addEventListener("click", abrirModal);

// Cerrar el modal al hacer clic fuera de él
window.onclick = function (event) {
    var modal = document.getElementById("modalConsejos");
    if (event.target === modal) {
        cerrarModal();
    }
};

// Cerrar el modal con la tecla Escape
document.addEventListener('keydown', function (event) {
    if (event.key === 'Escape') {
        cerrarModal();
    }
});

