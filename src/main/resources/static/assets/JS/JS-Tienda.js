// ===== FUNCIONALIDAD DE MODO OSCURO/CLARO =====
const themeToggle = document.getElementById("themeToggle");
const htmlElement = document.documentElement;

// Verificar si hay un tema guardado
const savedTheme = localStorage.getItem("theme") || "light";
htmlElement.setAttribute("data-theme", savedTheme);
themeToggle.checked = savedTheme === "dark";

// Event listener para cambiar el tema
themeToggle.addEventListener("change", function() {
	if (this.checked) {
		htmlElement.setAttribute("data-theme", "dark");
		localStorage.setItem("theme", "dark");
	} else {
		htmlElement.setAttribute("data-theme", "light");
		localStorage.setItem("theme", "light");
	}
});

// Funcionalidad del carrusel
let currentSlide = 0;
let slideInterval;

function initCarousel() {
	const slides = document.querySelectorAll(".carousel-slide");
	const indicators = document.querySelectorAll(".indicator");

	slides.forEach((slide, index) => {
		slide.classList.toggle("active", index === currentSlide);
	});

	indicators.forEach((indicator, index) => {
		indicator.classList.toggle("active", index === currentSlide);
	});

	startAutoSlide();
}

function nextSlide() {
	const slides = document.querySelectorAll(".carousel-slide");
	const indicators = document.querySelectorAll(".indicator");

	currentSlide = (currentSlide + 1) % slides.length;

	slides.forEach((slide, index) => {
		slide.classList.toggle("active", index === currentSlide);
	});

	indicators.forEach((indicator, index) => {
		indicator.classList.toggle("active", index === currentSlide);
	});

	resetAutoSlide();
}

function prevSlide() {
	const slides = document.querySelectorAll(".carousel-slide");
	const indicators = document.querySelectorAll(".indicator");

	currentSlide = (currentSlide - 1 + slides.length) % slides.length;

	slides.forEach((slide, index) => {
		slide.classList.toggle("active", index === currentSlide);
	});

	indicators.forEach((indicator, index) => {
		indicator.classList.toggle("active", index === currentSlide);
	});

	resetAutoSlide();
}

function goToSlide(slideIndex) {
	currentSlide = slideIndex;
	initCarousel();
	resetAutoSlide();
}

function startAutoSlide() {
	slideInterval = setInterval(nextSlide, 3000);
}

function resetAutoSlide() {
	clearInterval(slideInterval);
	startAutoSlide();
}

function pauseCarousel() {
	clearInterval(slideInterval);
}

function resumeCarousel() {
	startAutoSlide();
}

// Datos de productos ampliados
const products = {
	food: [
		{
			id: 1,
			name: "Alimento Premium para Perros",
			description:
				"Alimento balanceado para perros adultos de todas las razas. Con proteínas de alta calidad.",
			price: 24.99,
			image: "concentrado",
			featured: true,
			pet: "dog",
			rating: 5,
		},
		{
			id: 2,
			name: "Alimento para Gatos Sensibles",
			description:
				"Fórmula especial para gatos con estómagos sensibles. Sin granos ni colorantes artificiales.",
			price: 19.99,
			image: "comidaparagato",
			featured: true,
			pet: "cat",
			rating: 4,
		},
		{
			id: 3,
			name: "Snacks para Perros",
			description:
				"Deliciosos snacks para perro, bajos en calorías y con vitaminas esenciales.",
			price: 9.99,
			image: "snackperro",
			featured: false,
			pet: "dog",
			rating: 5,
		},
		{
			id: 4,
			name: "Alimento para Aves",
			description:
				"Mezcla de semillas y granos para aves domésticas. Rico en nutrientes esenciales.",
			price: 8.49,
			image: "pajaros",
			featured: false,
			pet: "bird",
			rating: 4,
		},
		{
			id: 13,
			name: "Comida Orgánica para Gatos",
			description:
				"Ingredientes 100% naturales y orgánicos para gatos exigentes.",
			price: 29.99,
			image: "comidaparagato",
			featured: true,
			pet: "cat",
			rating: 5,
		},
		{
			id: 14,
			name: "Galletas Dentales Perros",
			description: "Snacks que ayudan a mantener los dientes limpios y sanos.",
			price: 12.99,
			image: "snackperro",
			featured: false,
			pet: "dog",
			rating: 4,
		},
	],
	accessories: [
		{
			id: 5,
			name: "Collar Ajustable",
			description:
				"Collar de nylon resistente con hebilla de seguridad y ajuste personalizado.",
			price: 12.99,
			image: "collar",
			featured: false,
			pet: "dog",
			rating: 4,
		},
		{
			id: 6,
			name: "Juguete para Gatos",
			description:
				"Varita con plumas para estimular el instinto de caza de tu gato. Ideal para juego interactivo.",
			price: 7.99,
			image: "juguetegato",
			featured: true,
			pet: "cat",
			rating: 5,
		},
		{
			id: 7,
			name: "Cama para Mascotas",
			description:
				"Cama suave y cómoda con base antideslizante. Disponible en varios tamaños.",
			price: 29.99,
			image: "camaperro",
			featured: true,
			pet: "dog",
			rating: 5,
		},
		{
			id: 8,
			name: "Arnés Paseo Seguro",
			description:
				"Arnés ergonómico con correa incluida para paseos cómodos y seguros.",
			price: 18.5,
			image: "arnes",
			featured: false,
			pet: "dog",
			rating: 4,
		},
		{
			id: 15,
			name: "Rascador para Gatos",
			description:
				"Torre rascadora con múltiples niveles y juguetes colgantes.",
			price: 45.99,
			image: "juguetegato",
			featured: true,
			pet: "cat",
			rating: 5,
		},
		{
			id: 16,
			name: "Transportadora Premium",
			description: "Transportadora espaciosa y ventilada para viajes seguros.",
			price: 39.99,
			image: "collar",
			featured: false,
			pet: "all",
			rating: 4,
		},
	],
	medicine: [
		{
			id: 9,
			name: "Antiparasitario",
			description:
				"Tabletas antiparasitarias para perros y gatos. Protege contra parásitos internos.",
			price: 14.95,
			image: "antiparasitario",
			featured: false,
			pet: "all",
			rating: 5,
		},
		{
			id: 10,
			name: "Shampoo Medicado",
			description:
				"Shampoo para mascotas con problemas dermatológicos. Calma la piel irritada.",
			price: 11.25,
			image: "shampoo",
			featured: true,
			pet: "all",
			rating: 4,
		},
		{
			id: 11,
			name: "Suplemento Articular",
			description:
				"Suplemento con glucosamina para la salud articular de perros y gatos.",
			price: 22.75,
			image: "suplemento",
			featured: true,
			pet: "all",
			rating: 5,
		},
		{
			id: 12,
			name: "Gotas para Oídos",
			description:
				"Solución limpiadora para prevenir infecciones y mantener los oídos saludables.",
			price: 9.99,
			image: "gotas",
			featured: false,
			pet: "all",
			rating: 4,
		},
		{
			id: 17,
			name: "Vitaminas Multifuncionales",
			description: "Complejo vitamínico para fortalecer el sistema inmune.",
			price: 18.99,
			image: "suplemento",
			featured: false,
			pet: "all",
			rating: 5,
		},
		{
			id: 18,
			name: "Spray Antiparasitario",
			description: "Protección externa contra pulgas y garrapatas.",
			price: 16.5,
			image: "antiparasitario",
			featured: false,
			pet: "all",
			rating: 4,
		},
	],
};

let cart = [];
let currentFilter = { category: "all", pet: "all", sort: "featured" };

// Funciones de renderizado
function createProductCard(product) {
	const stars = '<i class="fas fa-star star"></i>'.repeat(product.rating);
	const badge = product.featured
		? '<div class="product-badge">⭐ Destacado</div>'
		: "";

	return `
        <div class="product-card" data-category="${getCategoryByProductId(
		product.id
	)}" data-pet="${product.pet}">
            ${badge}
            <div class="product-image">
                <img src="img/${product.image}.png" alt="${product.name}">
            </div>
            <div class="product-info">
                <div class="product-category">${getCategoryName(
		product.id
	)}</div>
                <h3 class="product-name">${product.name}</h3>
                <div class="product-rating">${stars}</div>
                <p class="product-description">${product.description}</p>
                <div class="product-bottom">
                    <div class="product-price">$${product.price.toFixed(
		2
	)}</div>
                    <button class="add-to-cart" onclick="addToCart(${product.id
		})">
                        <i class="fas fa-cart-plus"></i> Añadir
                    </button>
                </div>
            </div>
        </div>
    `;
}

function getCategoryByProductId(id) {
	if (id <= 4 || (id >= 13 && id <= 14)) return "food";
	if ((id >= 5 && id <= 8) || (id >= 15 && id <= 16)) return "accessories";
	return "medicine";
}

function getCategoryName(id) {
	const cat = getCategoryByProductId(id);
	if (cat === "food") return "Alimento";
	if (cat === "accessories") return "Accesorio";
	return "Medicamento";
}

function getAllProducts() {
	return [...products.food, ...products.accessories, ...products.medicine];
}

function renderProducts() {
	const allProducts = getAllProducts();
	const featured = allProducts.filter((p) => p.featured);

	// Productos destacados (siempre mostrar todos los destacados)
	document.getElementById("featuredProducts").innerHTML = featured
		.map(createProductCard)
		.join("");

	// Todos los productos (filtrados)
	let filtered = filterProducts(allProducts);

	// Si no hay productos filtrados, mostrar mensaje
	if (filtered.length === 0) {
		document.getElementById("allProducts").innerHTML = `
            <div style="grid-column: 1/-1; text-align: center; padding: 60px 20px;">
                <div style="font-size: 4rem; opacity: 0.3; margin-bottom: 20px;">
                    <i class="fas fa-search"></i>
                </div>
                <h3 style="color: #666; margin-bottom: 10px;">No se encontraron productos</h3>
                <p style="color: #999;">Intenta con otros filtros o categorías</p>
            </div>
        `;
	} else {
		document.getElementById("allProducts").innerHTML = filtered
			.map(createProductCard)
			.join("");
	}
}

function filterProducts(products) {
	let filtered = products;

	// Filtro de categoría
	if (currentFilter.category !== "all") {
		filtered = filtered.filter(
			(p) => getCategoryByProductId(p.id) === currentFilter.category
		);
	}

	// Filtro de mascota
	if (currentFilter.pet !== "all") {
		filtered = filtered.filter(
			(p) => p.pet === currentFilter.pet || p.pet === "all"
		);
	}

	// Ordenamiento
	if (currentFilter.sort === "price-low") {
		filtered.sort((a, b) => a.price - b.price);
	} else if (currentFilter.sort === "price-high") {
		filtered.sort((a, b) => b.price - a.price);
	} else if (currentFilter.sort === "name") {
		filtered.sort((a, b) => a.name.localeCompare(b.name));
	}

	return filtered;
}

// Carrito
function addToCart(productId) {
	const product = getAllProducts().find((p) => p.id === productId);
	if (!product) return;

	const existing = cart.find((item) => item.id === productId);
	if (existing) {
		existing.quantity++;
	} else {
		cart.push({ ...product, quantity: 1 });
	}

	updateCart();
	// Sin alerta, solo actualiza el carrito visualmente
}

function removeFromCart(productId) {
	cart = cart.filter((item) => item.id !== productId);
	updateCart();
}

function updateQuantity(productId, change) {
	const item = cart.find((i) => i.id === productId);
	if (!item) return;

	item.quantity += change;
	if (item.quantity <= 0) {
		removeFromCart(productId);
	} else {
		updateCart();
	}
}

function updateCart() {
	const badge = document.getElementById("cartBadge");
	const itemsContainer = document.getElementById("cartItems");
	const subtotalEl = document.getElementById("subtotal");
	const totalEl = document.getElementById("total");

	// Actualizar badge
	const totalItems = cart.reduce((sum, item) => sum + item.quantity, 0);
	badge.textContent = totalItems;

	// Renderizar items
	if (cart.length === 0) {
		itemsContainer.innerHTML = `
            <div class="empty-cart-message">
                <div class="empty-cart-icon">
                    <i class="fas fa-shopping-cart"></i>
                </div>
                <p>Tu carrito está vacío</p>
                <p style="font-size: 0.9rem; color: #999;">¡Agrega productos para empezar!</p>
            </div>
        `;
	} else {
		itemsContainer.innerHTML = cart
			.map(
				(item) => `
            <div class="cart-item">
                <div class="cart-item-image">
                    <img src="img/${item.image}.png" alt="${item.name}">
                </div>
                <div class="cart-item-details">
                    <div class="cart-item-name">${item.name}</div>
                    <div class="cart-item-price">$${item.price.toFixed(2)}</div>
                    <div class="cart-item-controls">
                        <button class="quantity-btn" onclick="updateQuantity(${item.id
					}, -1)">-</button>
                        <span class="quantity-display">${item.quantity}</span>
                        <button class="quantity-btn" onclick="updateQuantity(${item.id
					}, 1)">+</button>
                        <button class="remove-item" onclick="removeFromCart(${item.id
					})">
                            <i class="fas fa-trash"></i>
                        </button>
                    </div>
                </div>
            </div>
        `
			)
			.join("");
	}

	// Actualizar totales
	const subtotal = cart.reduce(
		(sum, item) => sum + item.price * item.quantity,
		0
	);
	subtotalEl.textContent = `$${subtotal.toFixed(2)}`;
	totalEl.textContent = `$${subtotal.toFixed(2)}`;
}

function showNotification(message) {
	// Función removida - no se usan alertas
	console.log(message);
}

// ===== DATOS DE TIENDAS =====
const stores = [
	{
		id: 1,
		name: "PetShop Centro",
		address: "Calle 19 #14-25, Centro",
		phone: "+57 300 123 4567",
		hours: "Lun-Sáb: 8AM-7PM",
		featured: true
	},
	{
		id: 2,
		name: "PetShop Norte",
		address: "Av. Boyacá #45-12, Norte",
		phone: "+57 300 765 4321",
		hours: "Lun-Dom: 9AM-8PM",
		featured: false
	},
	{
		id: 3,
		name: "PetShop Sur",
		address: "Carrera 15 #8-30, Sur",
		phone: "+57 301 234 5678",
		hours: "Lun-Sáb: 8AM-6PM",
		featured: false
	},
	{
		id: 4,
		name: "PetShop Plaza",
		address: "Centro Comercial Plaza, Local 205",
		phone: "+57 302 987 6543",
		hours: "Lun-Dom: 10AM-9PM",
		featured: true
	}
];

let selectedStore = null;

// ===== FUNCIONES DE TIENDAS =====
function renderStores() {
	const storesGrid = document.getElementById('storesGrid');
	
	storesGrid.innerHTML = stores.map(store => `
		<div class="store-card" onclick="selectStore(${store.id})">
			${store.featured ? '<div class="store-badge">⭐ Destacada</div>' : ''}
			<div class="store-card-image">
				<i class="fas fa-store-alt"></i>
			</div>
			<div class="store-card-content">
				<h3 class="store-card-name">${store.name}</h3>
				<div class="store-card-info">
					<div class="store-info-item">
						<i class="fas fa-map-marker-alt"></i>
						<span>${store.address}</span>
					</div>
					<div class="store-info-item">
						<i class="fas fa-phone"></i>
						<span>${store.phone}</span>
					</div>
					<div class="store-info-item">
						<i class="fas fa-clock"></i>
						<span>${store.hours}</span>
					</div>
				</div>
				<button class="store-card-button">
					<i class="fas fa-shopping-bag"></i>
					Ver Productos
				</button>
			</div>
		</div>
	`).join('');
}

function selectStore(storeId) {
	selectedStore = stores.find(s => s.id === storeId);
	
	// Ocultar selección de tiendas
	document.getElementById('storeSelection').style.display = 'none';
	
	// Mostrar secciones de productos
	document.getElementById('inicio').style.display = 'flex';
	document.getElementById('benefitsSection').style.display = 'block';
	document.getElementById('filtersSection').style.display = 'block';
	document.getElementById('featuredSection').style.display = 'block';
	document.getElementById('allProductsSection').style.display = 'block';
	
	// Inicializar carrusel
	initCarousel();
	
	// Renderizar productos
	renderProducts();
	
	// Scroll suave al inicio
	window.scrollTo({ top: 0, behavior: 'smooth' });
	
	console.log(`Tienda seleccionada: ${selectedStore.name}`);
}

// Event Listeners
document.addEventListener("DOMContentLoaded", () => {
	// Renderizar tiendas al cargar
	renderStores();

	// Carrito
	document.getElementById("openCart").addEventListener("click", () => {
		document.getElementById("cartSidebar").classList.add("active");
		document.getElementById("cartOverlay").classList.add("active");
	});

	document
		.getElementById("closeCart")
		.addEventListener("click", closeCartSidebar);
	document
		.getElementById("cartOverlay")
		.addEventListener("click", closeCartSidebar);

	function closeCartSidebar() {
		document.getElementById("cartSidebar").classList.remove("active");
		document.getElementById("cartOverlay").classList.remove("active");
	}

	// Checkout
	document.getElementById("checkoutBtn").addEventListener("click", () => {
		if (cart.length === 0) {
			alert("Tu carrito está vacío");
			return;
		}
		const total = cart.reduce(
			(sum, item) => sum + item.price * item.quantity,
			0
		);
		alert(`¡Gracias por tu compra!\nTotal: $${total.toFixed(2)}`);
		cart = [];
		updateCart();
		closeCartSidebar();
	});

	// ===== FUNCIONALIDAD DEL BOTÓN DE FILTRAR (SIN ALERTAS) =====
	document.getElementById("filtrar").addEventListener("click", () => {
		// Obtener valores de los filtros
		const categoryValue = document.getElementById("categoryFilter").value;
		const sortValue = document.getElementById("sortFilter").value;

		// Actualizar el objeto de filtros
		currentFilter.category = categoryValue;
		currentFilter.sort = sortValue;

		// Renderizar productos con los nuevos filtros
		renderProducts();

		// Scroll suave a la sección de productos
		document.getElementById("allProducts").scrollIntoView({
			behavior: "smooth",
			block: "start",
		});
	});

	// Filtros en tiempo real (opcional - ya los tenías pero mejoro la integración)
	document.getElementById("categoryFilter").addEventListener("change", (e) => {
		currentFilter.category = e.target.value;
		// No renderizamos aquí, esperamos al botón
	});

	document.getElementById("sortFilter").addEventListener("change", (e) => {
		currentFilter.sort = e.target.value;
		// No renderizamos aquí, esperamos al botón
	});

	// Filtros de mascota (si los tienes en el HTML)
	document.querySelectorAll(".filter-pill").forEach((pill) => {
		pill.addEventListener("click", function() {
			document
				.querySelectorAll(".filter-pill")
				.forEach((p) => p.classList.remove("active"));
			this.classList.add("active");
			currentFilter.pet = this.dataset.pet;
			renderProducts();
		});
	});

	// Menú hamburguesa
	const hamburgerBtn = document.querySelector(".hamburger-btn");
	const menuContent = document.querySelector(".menu-content");

	hamburgerBtn.addEventListener("click", () => {
		menuContent.classList.toggle("active");
	});

	document.addEventListener("click", (e) => {
		if (!menuContent.contains(e.target) && !hamburgerBtn.contains(e.target)) {
			menuContent.classList.remove("active");
		}
	});

	// Funcionalidad del calendario
	const calendarButton = document.getElementById("calendarButton");
	const calendarPopup = document.getElementById("calendarPopup");

	if (calendarButton && calendarPopup) {
		calendarButton.addEventListener("click", (e) => {
			e.stopPropagation();
			calendarPopup.classList.toggle("active");
		});

		document.addEventListener("click", (e) => {
			if (
				!calendarPopup.contains(e.target) &&
				!calendarButton.contains(e.target)
			) {
				calendarPopup.classList.remove("active");
			}
		});
	}

	// Modal emergencias
	const btnEmergencias = document.getElementById("emergencias");
	const modalEmergencias = document.getElementById("modalemergencias");

	if (btnEmergencias && modalEmergencias) {
		btnEmergencias.addEventListener("click", () => {
			modalEmergencias.style.display = "block";
		});
	}
});

// Función para cerrar modal de emergencias
function cerrarModal() {
	document.getElementById("modalemergencias").style.display = "none";
}

// Funciones de redirección para emergencias
function redirigirVideollamada() {
	alert("Redirigiendo a videollamada...");
	// window.location.href = 'tu-url-videollamada';
}

function redirigirWhatsApp() {
	window.open("https://wa.me/1234567890", "_blank");
}

function redirigirWhatsApp2() {
	window.open(
		"https://wa.me/1234567890?text=Necesito%20primeros%20auxilios",
		"_blank"
	);
}
