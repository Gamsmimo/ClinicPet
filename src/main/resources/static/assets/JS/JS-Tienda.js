/* JS/JS-Tienda.js - versión consolidada y robusta
- Funciones que el HTML puede llamar inline están expuestas en window (redirigir..., abrirModal, cerrarModal)
*/

// ---------- Funciones globales que tu HTML podría tener como onclick ----------
window.abrirModal = function () {
    const m = document.getElementById('modalemergencias');
    if (!m) return;
    m.style.display = 'block';
    m.classList.add('show');
    m.setAttribute('aria-hidden', 'false');
};

window.cerrarModal = function () {
    const m = document.getElementById('modalemergencias');
    if (!m) return;
    m.style.display = 'none';
    m.classList.remove('show');
    m.setAttribute('aria-hidden', 'true');
};

window.redirigirWhatsApp = function () {
    const numero = "573222473652";
    const mensaje = encodeURIComponent("Hola, necesito una cita de emergencia para mi mascota");
    window.open(`https://wa.me/${numero}?text=${mensaje}`, '_blank');
};

window.redirigirWhatsApp2 = function () {
    const numero = "573222473652";
    const mensaje = encodeURIComponent("Hola, necesito primeros auxilios para mi mascota");
    window.open(`https://wa.me/${numero}?text=${mensaje}`, '_blank');
};

window.redirigirVideollamada = function () {
    const enlace = "https://meet.google.com/abc-defg-hij";
    window.open(enlace, '_blank');
};

// ---------- Código principal (se ejecuta cuando DOM está listo) ----------
document.addEventListener('DOMContentLoaded', () => {
    /* ---------- Modal Emergencias ---------- */
    const btnEmergencias = document.getElementById('emergencias');
    const modalEmerg = document.getElementById('modalemergencias');
    const closeBtn = modalEmerg ? (modalEmerg.querySelector('.close') || document.getElementById('cerrarEmergencias')) : null;

    if (btnEmergencias) btnEmergencias.addEventListener('click', window.abrirModal);
    if (closeBtn) closeBtn.addEventListener('click', window.cerrarModal);

    // Cerrar si clickeas fuera
    window.addEventListener('click', (e) => {
        if (modalEmerg && e.target === modalEmerg) window.cerrarModal();
    });
    // Cerrar con Escape
    document.addEventListener('keydown', (e) => {
        if (e.key === 'Escape') window.cerrarModal();
    });

    /* ---------- Calendario flotante ---------- */
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

    function renderCalendar() {
        if (!calendarDays || !currentMonthYear) return;
        const year = currentDate.getFullYear();
        const month = currentDate.getMonth();

        currentMonthYear.textContent = currentDate.toLocaleDateString('es-ES', { month: 'long', year: 'numeric' });

        const firstDay = new Date(year, month, 1);
        const lastDay = new Date(year, month + 1, 0);
        const daysInMonth = lastDay.getDate();
        const startingDay = firstDay.getDay();

        calendarDays.innerHTML = '';

        // prev month days
        const prevMonthLastDay = new Date(year, month, 0).getDate();
        for (let i = 0; i < startingDay; i++) {
            const d = document.createElement('div');
            d.className = 'calendar-day other-month';
            d.textContent = prevMonthLastDay - startingDay + i + 1;
            calendarDays.appendChild(d);
        }

        // current month
        const today = new Date();
        for (let i = 1; i <= daysInMonth; i++) {
            const d = document.createElement('div');
            d.className = 'calendar-day';
            d.textContent = i;

            if (i === today.getDate() && month === today.getMonth() && year === today.getFullYear()) {
                d.classList.add('current-day');
            }

            if (i === selectedDate.getDate() && month === selectedDate.getMonth() && year === selectedDate.getFullYear()) {
                d.style.backgroundColor = 'var(--verde-suave)';
                d.style.fontWeight = '700';
            }

            d.addEventListener('click', () => {
                selectedDate = new Date(year, month, i);
                renderCalendar();
            });

            calendarDays.appendChild(d);
        }

        // next month filler
        const cells = startingDay + daysInMonth;
        const daysToShow = cells % 7 === 0 ? 0 : 7 - (cells % 7);
        for (let i = 1; i <= daysToShow; i++) {
            const d = document.createElement('div');
            d.className = 'calendar-day other-month';
            d.textContent = i;
            calendarDays.appendChild(d);
        }
    }

    if (calendarButton && calendarPopup) {
        calendarButton.addEventListener('click', (e) => {
            e.stopPropagation();
            calendarPopup.classList.toggle('active');
            renderCalendar();
        });

        calendarPopup.addEventListener('click', (e) => e.stopPropagation());
        document.addEventListener('click', () => calendarPopup.classList.remove('active'));

        if (prevMonthBtn) prevMonthBtn.addEventListener('click', () => { currentDate.setMonth(currentDate.getMonth() - 1); renderCalendar(); });
        if (nextMonthBtn) nextMonthBtn.addEventListener('click', () => { currentDate.setMonth(currentDate.getMonth() + 1); renderCalendar(); });
        if (todayBtn) todayBtn.addEventListener('click', () => { currentDate = new Date(); selectedDate = new Date(); renderCalendar(); });

        if (agendarCitaBtn) {
            agendarCitaBtn.addEventListener('click', () => {
                const formattedDate = selectedDate.toLocaleDateString('es-ES', { weekday: 'long', year: 'numeric', month: 'long', day: 'numeric' });
                const phoneNumber = "573204767864";
                const message = `¡Hola! Quiero agendar una cita para mi mascota el día ${formattedDate}. Por favor confírmame disponibilidad.`;
                window.open(`https://wa.me/${phoneNumber}?text=${encodeURIComponent(message)}`, '_blank');
                calendarPopup.classList.remove('active');
            });
        }

        renderCalendar();
    }

    /* ---------- Productos y Carrito ---------- */
    const products = {
        food: [
            { id: 1, name: "Alimento Premium para Perros", description: "Alimento balanceado para perros adultos de todas las razas. Con proteínas de alta calidad.", price: 24.99, image: "concentrado" },
            { id: 2, name: "Alimento para Gatos Sensibles", description: "Fórmula especial para gatos con estómagos sensibles. Sin granos ni colorantes artificiales.", price: 19.99, image: "comidaparagato" },
            { id: 3, name: "Snacks para Perros", description: "Deliciosos snacks para perro, bajos en calorías y con vitaminas esenciales.", price: 9.99, image: "snackperro" },
            { id: 4, name: "Alimento para Aves", description: "Mezcla de semillas y granos para aves domésticas. Rico en nutrientes esenciales.", price: 8.49, image: "pajaros" }
        ],
        accessories: [
            { id: 5, name: "Collar Ajustable", description: "Collar de nylon resistente con hebilla de seguridad y ajuste personalizado.", price: 12.99, image: "collar" },
            { id: 6, name: "Juguete para Gatos", description: "Varita con plumas para estimular el instinto de caza de tu gato. Ideal para juego interactivo.", price: 7.99, image: "juguetegato" },
            { id: 7, name: "Cama para Mascotas", description: "Cama suave y cómoda con base antideslizante. Disponible en varios tamaños.", price: 29.99, image: "camaperro" },
            { id: 8, name: "Arnés Paseo Seguro", description: "Arnés ergonómico con correa incluida para paseos cómodos y seguros.", price: 18.50, image: "arnes" }
        ],
        medicine: [
            { id: 9, name: "Antiparasitario", description: "Tabletas antiparasitarias para perros y gatos. Protege contra parásitos internos.", price: 14.95, image: "antiparasitario" },
            { id: 10, name: "Shampoo Medicado", description: "Shampoo para mascotas con problemas dermatológicos. Calma la piel irritada.", price: 11.25, image: "shampoo" },
            { id: 11, name: "Suplemento Articular", description: "Suplemento con glucosamina para la salud articular de perros y gatos.", price: 22.75, image: "suplemento" },
            { id: 12, name: "Gotas para Oídos", description: "Solución limpiadora para prevenir infecciones y mantener los oídos saludables.", price: 9.99, image: "gotas" }
        ]
    };

    let cart = [];

    // DOM refs carrito/productos
    const foodProductsContainer = document.getElementById('foodProducts');
    const accessoryProductsContainer = document.getElementById('accessoryProducts');
    const medicineProductsContainer = document.getElementById('medicineProducts');
    const cartItemsContainer = document.getElementById('cartItems');
    const cartCountElement = document.querySelector('.cart-count');
    const subtotalElement = document.getElementById('subtotal');
    const totalElement = document.getElementById('total');
    const checkoutBtn = document.querySelector('.checkout-btn');

    const shoppingCart = document.getElementById('shoppingCart');
    const cartBtn = document.getElementById('cartBtn');

    if (cartBtn && shoppingCart) {
        cartBtn.addEventListener('click', () => shoppingCart.classList.toggle('show'));
    }

    function createProductCard(product, category) {
        const categoryName = category === 'food' ? 'Alimento' : (category === 'accessories' ? 'Accesorio' : 'Medicamento');
        return `
      <div class="product-card">
        <div class="product-image">
          <img src="img/${product.image}.png" alt="${product.name}" class="product-img">
        </div>
        <div class="product-info">
          <div class="product-category">${categoryName}</div>
          <h3 class="product-name">${product.name}</h3>
          <p class="product-description">${product.description}</p>
          <div class="product-bottom">
            <div class="product-price">$${product.price.toFixed(2)}</div>
            <button class="add-to-cart" data-id="${product.id}" data-category="${category}">
              <i class="fas fa-cart-plus"></i> Añadir
            </button>
          </div>
        </div>
      </div>
    `;
    }

    function renderProducts() {
        if (foodProductsContainer) {
            foodProductsContainer.innerHTML = '';
            products.food.forEach(p => foodProductsContainer.insertAdjacentHTML('beforeend', createProductCard(p, 'food')));
        }
        if (accessoryProductsContainer) {
            accessoryProductsContainer.innerHTML = '';
            products.accessories.forEach(p => accessoryProductsContainer.insertAdjacentHTML('beforeend', createProductCard(p, 'accessories')));
        }
        if (medicineProductsContainer) {
            medicineProductsContainer.innerHTML = '';
            products.medicine.forEach(p => medicineProductsContainer.insertAdjacentHTML('beforeend', createProductCard(p, 'medicine')));
        }

        // bind add-to-cart safely (use event.currentTarget)
        document.querySelectorAll('.add-to-cart').forEach(btn => {
            btn.addEventListener('click', addToCart);
        });
    }

    function findProductById(id) {
        return products.food.concat(products.accessories, products.medicine).find(p => p.id === id);
    }

    function addToCart(event) {
        const btn = event.currentTarget || event.target.closest('.add-to-cart');
        if (!btn) return;
        const productId = parseInt(btn.dataset.id, 10);
        const product = findProductById(productId);
        if (!product) return;

        const existing = cart.find(i => i.id === product.id);
        if (existing) existing.quantity += 1;
        else cart.push({ id: product.id, name: product.name, price: product.price, image: product.image, quantity: 1 });

        updateCart();

        // feedback
        const original = btn.innerHTML;
        btn.innerHTML = '<i class="fas fa-check"></i> Añadido';
        btn.disabled = true;
        setTimeout(() => { btn.innerHTML = original; btn.disabled = false; }, 1200);
    }

    function createCartItem(item) {
        return `
      <div class="cart-item" data-id="${item.id}">
        <div class="cart-item-image"><img src="img/${item.image}.png" alt="${item.name}"></div>
        <div class="cart-item-details">
          <div class="cart-item-name">${item.name}</div>
          <div class="cart-item-price">$${item.price.toFixed(2)}</div>
          <div class="cart-item-controls">
            <button class="quantity-btn decrease-quantity" data-id="${item.id}">-</button>
            <span class="quantity-display">${item.quantity}</span>
            <button class="quantity-btn increase-quantity" data-id="${item.id}">+</button>
            <button class="remove-item" data-id="${item.id }"> <i class="bi bi-trash-fill" style="font-size: 20px;"></i></button>
          </div>
        </div>
      </div>`;
    }

    function updateCart() {
        // contador
        const totalItems = cart.reduce((t, it) => t + it.quantity, 0);
        if (cartCountElement) cartCountElement.textContent = totalItems;

        if (!cartItemsContainer) return;
        if (cart.length === 0) {
            cartItemsContainer.innerHTML = '<div class="empty-cart-message">Tu carrito está vacío</div>';
        } else {
            cartItemsContainer.innerHTML = '';
            cart.forEach(item => cartItemsContainer.insertAdjacentHTML('beforeend', createCartItem(item)));

            // bind controls
            cartItemsContainer.querySelectorAll('.decrease-quantity').forEach(b => b.addEventListener('click', decreaseQuantity));
            cartItemsContainer.querySelectorAll('.increase-quantity').forEach(b => b.addEventListener('click', increaseQuantity));
            cartItemsContainer.querySelectorAll('.remove-item').forEach(b => b.addEventListener('click', removeItem));
        }

        updateSummary();
    }

    function updateSummary() {
        const subtotal = cart.reduce((s, it) => s + it.price * it.quantity, 0);
        const discount = 0;
        const total = subtotal - discount;
        if (subtotalElement) subtotalElement.textContent = `$${subtotal.toFixed(2)}`;
        if (totalElement) totalElement.textContent = `$${total.toFixed(2)}`;
    }

    function decreaseQuantity(e) {
        const id = parseInt((e.currentTarget || e.target).dataset.id, 10);
        const item = cart.find(i => i.id === id);
        if (!item) return;
        item.quantity -= 1;
        if (item.quantity <= 0) cart = cart.filter(i => i.id !== id);
        updateCart();
    }

    function increaseQuantity(e) {
        const id = parseInt((e.currentTarget || e.target).dataset.id, 10);
        const item = cart.find(i => i.id === id);
        if (!item) return;
        item.quantity += 1;
        updateCart();
    }

    function removeItem(e) {
        const el = e.currentTarget || e.target.closest('.remove-item');
        const id = parseInt(el.dataset.id, 10);
        cart = cart.filter(i => i.id !== id);
        updateCart();
    }

    function checkout() {
        if (!cart.length) { alert('Tu carrito está vacío. Añade productos antes de pagar.'); return; }
        const total = cart.reduce((s, it) => s + it.price * it.quantity, 0);
        const discount = total * 0.1;
        const finalTotal = total - discount;
        alert(`¡Gracias por tu compra!\nTotal: $${finalTotal.toFixed(2)}\nTu pedido ha sido procesado.`);
        cart = [];
        updateCart();
    }

    if (checkoutBtn) checkoutBtn.addEventListener('click', checkout);

    // inicia
    renderProducts();
    updateCart();

    /* ---------- Menú hamburguesa ---------- */
    const hamburgerBtn = document.querySelector('.hamburger-btn');
    const menuContent = document.querySelector('.menu-content');
    if (hamburgerBtn && menuContent) {
        hamburgerBtn.addEventListener('click', () => menuContent.classList.toggle('active'));
        document.addEventListener('click', (e) => {
            if (!menuContent.contains(e.target) && !hamburgerBtn.contains(e.target) && menuContent.classList.contains('active')) {
                menuContent.classList.remove('active');
            }
        });
    }

    /* ---------- Búsqueda (vanilla) ---------- */
    const searchInput = document.getElementById('searchInput');
    if (searchInput) {
        searchInput.addEventListener('keydown', (e) => {
            if (e.key === 'Enter') {
                const term = searchInput.value.trim().toLowerCase();
                if (!term) return;
                // buscar en texto visible de la página
                const walker = document.createTreeWalker(document.body, NodeFilter.SHOW_TEXT, null, false);
                let node;
                let found = null;
                while ((node = walker.nextNode())) {
                    if (node.nodeValue.toLowerCase().includes(term)) { found = node; break; }
                }
                if (found) {
                    const el = found.parentElement;
                    el.scrollIntoView({ behavior: 'smooth', block: 'center' });
                    el.style.transition = 'background 0.6s';
                    const prevBg = el.style.backgroundColor;
                    el.style.backgroundColor = 'rgba(255,255,0,0.35)';
                    setTimeout(() => el.style.backgroundColor = prevBg, 1600);
                } else {
                    alert('No se encontraron resultados para: ' + term);
                }
            }
        });
    }

}); // end DOMContentLoaded
