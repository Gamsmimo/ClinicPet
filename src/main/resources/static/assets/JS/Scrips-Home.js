document.addEventListener('DOMContentLoaded', function () {
    // --- Menú Hamburguesa ---
    document.querySelector('.hamburger-btn').addEventListener('click', function () {
        document.querySelector('.menu-content').classList.toggle('active');
    });

    // Cerrar menú al hacer clic fuera
    document.addEventListener('click', function (event) {
        const menu = document.querySelector('.menu-content');
        const btn = document.querySelector('.hamburger-btn');
        if (!menu.contains(event.target) && !btn.contains(event.target) && menu.classList.contains('active')) {
            menu.classList.remove('active');
        }
    });

    // --- Carrusel (Owl Carousel) ---
    $(document).ready(function () {
        $('.owl-carousel').owlCarousel({
            loop: true,
            margin: 20,
            nav: true,
            responsive: {
                0: { items: 1 },
                600: { items: 2 },
                1000: { items: 3 }
            }
        });
    });

    // --- Función de Búsqueda ---
    const searchInput = document.getElementById('searchInput');
    const searchResults = document.getElementById('searchResults');
    let currentHighlights = [];

    function getTextExcerpt(text, query, radius = 30) {
        const index = text.toLowerCase().indexOf(query.toLowerCase());
        if (index === -1) return null;

        const start = Math.max(0, index - radius);
        const end = Math.min(text.length, index + query.length + radius);
        let excerpt = text.substring(start, end);

        if (start > 0) excerpt = '...' + excerpt;
        if (end < text.length) excerpt = excerpt + '...';
        return excerpt;
    }

    function highlightText(text, query) {
        const regex = new RegExp(escapeRegExp(query), 'gi');
        return text.replace(regex, match => `<span class="highlight">${match}</span>`);
    }

    function searchContent(query) {
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
        currentHighlights = [];
    }

    function escapeRegExp(string) {
        return string.replace(/[.*+?^${}()|[\]\\]/g, '\\$&');
    }

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

    // --- Calendario ---
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

    calendarButton.addEventListener('click', function (e) {
        e.stopPropagation();
        calendarPopup.classList.toggle('active');
        renderCalendar();
    });

    document.addEventListener('click', function () {
        calendarPopup.classList.remove('active');
    });

    calendarPopup.addEventListener('click', function (e) {
        e.stopPropagation();
    });

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

    agendarCitaBtn.addEventListener('click', function () {
        const formattedDate = selectedDate.toLocaleDateString('es-ES', {
            weekday: 'long',
            year: 'numeric',
            month: 'long',
            day: 'numeric'
        });

        const phoneNumber = "573204767864";
        const message = `¡Hola! Quiero agendar una cita para mi mascota el día ${formattedDate}. Por favor confírmame disponibilidad.`;
        const encodedMessage = encodeURIComponent(message);

        window.open(`https://wa.me/${phoneNumber}?text=${encodedMessage}`, '_blank');
        calendarPopup.classList.remove('active');
    });

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

            if (i === today.getDate() && month === today.getMonth() && year === today.getFullYear()) {
                dayElement.classList.add('current-day');
            }

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


// Función para abrir el modal de emergencia 
function abrirModal() {
    document.getElementById("modalemergencias").style.display = "block";
}

// Función para cerrar el modal
function cerrarModal() {
    document.getElementById("modalemergencias").style.display = "none";
}

// Event listener para el botón de emergencia
document.getElementById("emergencias").addEventListener("click", abrirModal);

// Cerrar el modal al hacer clic fuera de él
window.onclick = function (event) {
    var modal = document.getElementById("modalemergencias");
    if (event.target === modal) {
        cerrarModal();
    }
};

// Cerrar el modal con la tecla Escape
document.addEventListener("keydown", function (event) {
    if (event.key === "Escape") {
        cerrarModal();
    }
});

//WhatsApp
function redirigirWhatsApp() {
    const numero = "573222473652";
    const mensaje = encodeURIComponent("Hola, necesito una cita de emergencia para mi mascota");
    const url = `https://wa.me/${numero}?text=${mensaje}`;
    window.open(url, '_blank');
}

//whatsApp primeros auxilios
function redirigirWhatsApp2() {
    const numero = "573222473652";
    const mensaje = encodeURIComponent("Hola, necesito primeros auxilios para mi mascota");
    const url = `https://wa.me/${numero}?text=${mensaje}`;
    window.open(url, '_blank');
}

function redirigirVideollamada() {
    const enlace = "https://meet.google.com/abc-defg-hij";
    window.open(enlace, "_blank");
}

