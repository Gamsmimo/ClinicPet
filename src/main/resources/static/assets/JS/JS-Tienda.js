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


// --- Datos de productos ---
const products = {
    food: [
        {
            id: 1,
            name: "Alimento Premium para Perros",
            description: "Alimento balanceado para perros adultos de todas las razas. Con proteínas de alta calidad.",
            price: 24.99,
            image: "IMG/concentrado.png"
        },
        {
            id: 2,
            name: "Alimento para Gatos Sensibles",
            description: "Fórmula especial para gatos con estómagos sensibles. Sin granos ni colorantes artificiales.",
            price: 19.99,
            image: "IMG/comidaparagato.png"
        },
        {
            id: 3,
            name: "Snacks para Perros",
            description: "Deliciosos snacks para perro, bajos en calorías y con vitaminas esenciales.",
            price: 9.99,
            image: "IMG/snackperro.png"
        },
        {
            id: 4,
            name: "Alimento para Aves",
            description: "Mezcla de semillas y granos para aves domésticas. Rico en nutrientes esenciales.",
            price: 8.49,
            image: "IMG/pajaros.png"
        }
    ],
    accessories: [
        {
            id: 5,
            name: "Collar Ajustable",
            description: "Collar de nylon resistente con hebilla de seguridad y ajuste personalizado.",
            price: 12.99,
            image: "IMG/collar.png"
        },
        {
            id: 6,
            name: "Juguete para Gatos",
            description: "Varita con plumas para estimular el instinto de caza de tu gato. Ideal para juego interactivo.",
            price: 7.99,
            image: "IMG/juguetegato.png"
        },
        {
            id: 7,
            name: "Cama para Mascotas",
            description: "Cama suave y cómoda con base antideslizante. Disponible en varios tamaños.",
            price: 29.99,
            image: "IMG/camaperro.png"
        },
        {
            id: 8,
            name: "Arnés Paseo Seguro",
            description: "Arnés ergonómico con correa incluida para paseos cómodos y seguros.",
            price: 18.50,
            image: "IMG/arnes.png"
        }
    ],
    medicine: [
        {
            id: 9,
            name: "Antiparasitario",
            description: "Tabletas antiparasitarias para perros y gatos. Protege contra parásitos internos.",
            price: 14.95,
            image: "IMG/antiparasitario.png"
        },
        {
            id: 10,
            name: "Shampoo Medicado",
            description: "Shampoo para mascotas con problemas dermatológicos. Calma la piel irritada.",
            price: 11.25,
            image: "IMG/shampoo.png"
        },
        {
            id: 11,
            name: "Suplemento Articular",
            description: "Suplemento con glucosamina para la salud articular de perros y gatos.",
            price: 22.75,
            image: "IMG/suplemento.png"
        },
        {
            id: 12,
            name: "Gotas para Oídos",
            description: "Solución limpiadora para prevenir infecciones y mantener los oídos saludables.",
            price: 9.99,
            image: "IMG/gotas.png"
        }
    ]
};

// --- Referencias DOM ---
const foodProductsContainer = document.getElementById("foodProducts");
const accessoryProductsContainer = document.getElementById("accessoryProducts");
const medicineProductsContainer = document.getElementById("medicineProducts");

const cartBtn = document.getElementById("cartBtn");
const shoppingCart = document.getElementById("shoppingCart");
const cartContent = document.getElementById("cartContent");

const subtotalEl = document.getElementById("subtotal");
const discountEl = document.getElementById("discount");
const totalEl = document.getElementById("total");

const cartCount = document.querySelector(".cart-count");

let cart = [];

// --- Renderizar productos ---
function renderProducts() {
    renderCategory(products.food, foodProductsContainer, "Alimentos");
    renderCategory(products.accessories, accessoryProductsContainer, "Accesorios");
    renderCategory(products.medicine, medicineProductsContainer, "Medicamentos");
}

function renderCategory(items, container, category) {
    container.innerHTML = "";
    items.forEach(item => {
        const productCard = document.createElement("div");
        productCard.classList.add("product-card");

        productCard.innerHTML = `
    <div class="product-image"><img src="${item.image}" alt="${item.name}"></div>
    <div class="product-info">
        <p class="product-category">${category}</p>
        <h3 class="product-name">${item.name}</h3>
        <p class="product-description">${item.description}</p>
        <div class="product-bottom">
        <span class="product-price">$${item.price.toLocaleString()}</span>
        <button class="add-to-cart"><i class="fas fa-cart-plus"></i> Añadir</button>
        </div>
    </div>
    `;

        productCard.querySelector(".add-to-cart").addEventListener("click", () => addToCart(item));
        container.appendChild(productCard);
    });
}

// --- Carrito ---
function addToCart(product) {
    const existing = cart.find(p => p.id === product.id);
    if (existing) {
        existing.quantity++;
    } else {
        cart.push({ ...product, quantity: 1 });
    }
    updateCart();
}

function updateCart() {
    cartContent.innerHTML = "";

    if (cart.length === 0) {
        cartContent.innerHTML = "<p>Tu carrito está vacío</p>";
        subtotalEl.textContent = "0.00";
        discountEl.textContent = "0.00";
        totalEl.textContent = "0.00";
        cartCount.textContent = "0";
        return;
    }

    const itemsContainer = document.createElement("div");
    itemsContainer.classList.add("cart-items");

    let subtotal = 0;

    cart.forEach(item => {
        subtotal += item.price * item.quantity;

        const cartItem = document.createElement("div");
        cartItem.classList.add("cart-item");

        cartItem.innerHTML = `
      <div class="cart-item-image"><img src="${item.image}" alt="${item.name}"></div>
      <div class="cart-item-details">
        <div class="cart-item-name">${item.name}</div>
        <div class="cart-item-price">$${(item.price * item.quantity).toLocaleString()}</div>
        <div class="cart-item-controls">
          <button class="quantity-btn decrease">-</button>
          <span class="quantity-display">${item.quantity}</span>
          <button class="quantity-btn increase">+</button>
          <button class="remove-btn"><i class="fas fa-trash"></i></button>
        </div>
      </div>
    `;

        // Eventos
        cartItem.querySelector(".increase").addEventListener("click", () => {
            item.quantity++;
            updateCart();
        });
        cartItem.querySelector(".decrease").addEventListener("click", () => {
            if (item.quantity > 1) {
                item.quantity--;
            } else {
                cart = cart.filter(p => p.id !== item.id);
            }
            updateCart();
        });
        cartItem.querySelector(".remove-btn").addEventListener("click", () => {
            cart = cart.filter(p => p.id !== item.id);
            updateCart();
        });

        itemsContainer.appendChild(cartItem);
    });

    cartContent.appendChild(itemsContainer);

    const discount = subtotal * 0.1;
    const total = subtotal - discount;

    subtotalEl.textContent = subtotal.toLocaleString();
    discountEl.textContent = discount.toLocaleString();
    totalEl.textContent = total.toLocaleString();

    cartCount.textContent = cart.reduce((acc, p) => acc + p.quantity, 0);
}

// --- Toggle carrito ---
cartBtn.addEventListener("click", () => {
    shoppingCart.classList.toggle("show");
});
function closeCart() {
    shoppingCart.classList.remove("show");
}

// --- Checkout ---
document.querySelector(".checkout").addEventListener("click", () => {
    if (cart.length === 0) {
        alert("Tu carrito está vacío");
        return;
    }
    alert("Gracias por tu compra 🐾");
    cart = [];
    updateCart();
    closeCart();
});

// --- Inicializar ---
renderProducts();


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
$('.search-btn').click(function (e) {
    e.preventDefault();
    var searchTerm = $('.search-input').val().toLowerCase();
    if (searchTerm.length > 0) {
        // Buscar en el contenido de la página
        var found = false;
        $('body').find('*').each(function () {
            var content = $(this).text().toLowerCase();
            if (content.indexOf(searchTerm) > -1) {
                found = true;
                // Scroll to the first match
                $('html, body').animate({
                    scrollTop: $(this).offset().top - 100
                }, 1000);
                return false; // break loop
            }
        });

        if (!found) {
            alert('No se encontraron resultados para: ' + searchTerm);
        }
    }
});

// Permitir buscar con Enter
$('.search-input').keypress(function (e) {
    if (e.which === 13) {
        $('.search-btn').click();
    }
});


