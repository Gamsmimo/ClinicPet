// ===== FUNCIONALIDAD DE MODO OSCURO/CLARO =====
const themeToggle = document.getElementById('themeToggle');
const htmlElement = document.documentElement;

const savedTheme = localStorage.getItem('theme') || 'light';
if (savedTheme === 'dark') {
	document.body.classList.add('dark-mode');
	themeToggle.checked = true;
}

themeToggle.addEventListener('change', function() {
	if (this.checked) {
		htmlElement.setAttribute('data-theme', 'dark');
		localStorage.setItem('theme', 'dark');
	} else {
		htmlElement.setAttribute('data-theme', 'light');
		localStorage.setItem('theme', 'light');
	}
});

// ===== MENÚ HAMBURGUESA =====
document.querySelector('.hamburger-btn').addEventListener('click', function() {
	document.querySelector('.menu-content').classList.toggle('active');
});

document.addEventListener('click', function(event) {
	const menu = document.querySelector('.menu-content');
	const btn = document.querySelector('.hamburger-btn');
	if (!menu.contains(event.target) && !btn.contains(event.target) && menu.classList.contains('active')) {
		menu.classList.remove('active');
	}
});

// ===== DATOS DE MASCOTAS =====
const mascotas = [
	{
		id: 1,
		nombre: "Max",
		tipo: "perro",
		raza: "Labrador",
		edad: 2,
		tamanio: "mediano",
		descripcion: "Max es un perro muy juguetón y cariñoso. Le encanta pasear y jugar con niños.",
		foto: "https://images.unsplash.com/photo-1587300003388-59208cc962cb?w=400",
		contacto: "3222473652"
	},
	{
		id: 2,
		nombre: "Luna",
		tipo: "gato",
		raza: "Siamés",
		edad: 1,
		tamanio: "pequeno",
		descripcion: "Luna es una gata tranquila que disfruta de los mimos y dormir en lugares cálidos.",
		foto: "https://images.unsplash.com/photo-1513360371669-4adf3dd7dff8?w=400",
		contacto: "5557654321"
	},
	{
		id: 3,
		nombre: "Copito",
		tipo: "gato",
		raza: "Persa",
		edad: 2,
		tamanio: "pequeno",
		descripcion: "Copito es un gato tranquilo que le gusta jugar con estambre.",
		foto: "https://images.unsplash.com/photo-1573865526739-10c1d3a1b2e0?w=400",
		contacto: "5557654321"
	},
	{
		id: 4,
		nombre: "Rocky",
		tipo: "perro",
		raza: "Bulldog Francés",
		edad: 2,
		tamanio: "pequeno",
		descripcion: "Rocky es tranquilo y muy leal, perfecto para vivir en apartamento.",
		foto: "https://images.unsplash.com/photo-1583511655857-d19b40a7a54e?w=400",
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
		foto: "https://images.unsplash.com/photo-1574158622682-e40e69881006?w=400",
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
		foto: "https://images.unsplash.com/photo-1514888286974-6c03e2ca1dba?w=400",
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
		foto: "https://images.unsplash.com/photo-1633722715463-d30f4f325e24?w=400",
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
		foto: "https://images.unsplash.com/photo-1595433707802-6b2626ef1c91?w=400",
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
		foto: "https://images.unsplash.com/photo-1505628346881-b72b27e84530?w=400",
		contacto: "5557778888"
	}
];

// ===== GESTOR DE MASCOTAS =====
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
			listaMascotas.innerHTML = '<p style="text-align: center; padding: 40px; color: #666;">No se encontraron mascotas con estos filtros.</p>';
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

		const etiquetasContainer = document.createElement('div');
		etiquetasContainer.className = 'etiquetas-container';

		const tipo = document.createElement('span');
		tipo.className = 'etiqueta etiqueta-tipo';
		tipo.textContent = mascota.tipo === 'perro' ? '🐕 Perro' : mascota.tipo === 'gato' ? '🐈 Gato' : '🐾 Otro';

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

		etiquetasContainer.appendChild(tipo);
		etiquetasContainer.appendChild(tamanio);
		etiquetasContainer.appendChild(edad);

		const raza = document.createElement('p');
		raza.textContent = mascota.raza ? `Raza: ${mascota.raza}` : 'Raza: Mestizo';
		raza.style.fontWeight = '600';
		raza.style.marginBottom = '10px';

		const descripcion = document.createElement('p');
		descripcion.textContent = mascota.descripcion;
		descripcion.style.fontSize = '0.95rem';

		const botonAdoptar = document.createElement('a');
		botonAdoptar.className = 'boton-adoptar';
		botonAdoptar.innerHTML = '¡Ver Detalles!';

		contenido.appendChild(nombre);
		contenido.appendChild(etiquetasContainer);
		contenido.appendChild(raza);
		contenido.appendChild(descripcion);
		contenido.appendChild(botonAdoptar);

		tarjeta.appendChild(imagen);
		tarjeta.appendChild(contenido);

		return tarjeta;
	}

	agregarMascota(nuevaMascota) {
		nuevaMascota.id = this.mascotas.length > 0 ? Math.max(...this.mascotas.map(m => m.id)) + 1 : 1;
		this.mascotas.unshift(nuevaMascota);
		this.cargarMascotas();
		alert(`¡${nuevaMascota.nombre} ha sido publicado para adopción con éxito!`);
	}

	configurarEventos() {
		document.getElementById('filtrar').addEventListener('click', () => {
			const tipo = document.getElementById('tipo-mascota').value;
			const tamanio = document.getElementById('tamanio-mascota').value;
			this.cargarMascotas(tipo, tamanio);
		});

		document.getElementById('submitBtn').addEventListener('click', (e) => {
			e.preventDefault();

			const form = document.getElementById('formulario-mascota');
			if (!form.checkValidity()) {
				form.reportValidity();
				return;
			}

			const fotoInput = document.getElementById('foto');
			let fotoURL = 'https://via.placeholder.com/300x200?text=Sin+imagen';

			if (fotoInput.files && fotoInput.files[0]) {
				fotoURL = URL.createObjectURL(fotoInput.files[0]);
			}

			const nuevaMascota = {
				nombre: document.getElementById('nombre').value,
				tipo: document.getElementById('tipo').value,
				raza: document.getElementById('raza').value || 'Mestizo',
				edad: parseInt(document.getElementById('edad').value) || 0,
				tamanio: document.getElementById('tamanio').value,
				descripcion: document.getElementById('descripcion').value,
				foto: fotoURL,
				contacto: document.getElementById('contacto').value
			};

			this.agregarMascota(nuevaMascota);
			form.reset();
			document.getElementById('fileName').textContent = 'Ningún archivo seleccionado';
			document.getElementById('modalAdopcion').classList.remove('active');
		});
	}
}

// ===== MODAL =====
document.getElementById('btnDarAdopcion').addEventListener('click', () => {
	document.getElementById('modalAdopcion').classList.add('active');
});

document.getElementById('closeModal').addEventListener('click', () => {
	document.getElementById('modalAdopcion').classList.remove('active');
});

document.getElementById('modalAdopcion').addEventListener('click', (e) => {
	if (e.target.id === 'modalAdopcion') {
		document.getElementById('modalAdopcion').classList.remove('active');
	}
});

// ===== TIPO DE MASCOTA =====
document.getElementById('tipo').addEventListener('change', function() {
	const otroTipoGroup = document.getElementById('otroTipoGroup');
	if (this.value === 'otro') {
		otroTipoGroup.style.display = 'block';
		document.getElementById('otroTipo').required = true;
	} else {
		otroTipoGroup.style.display = 'none';
		document.getElementById('otroTipo').required = false;
	}
});

// ===== ARCHIVO =====
document.getElementById('foto').addEventListener('change', function() {
	const fileName = this.files[0] ? this.files[0].name : 'Ningún archivo seleccionado';
	document.getElementById('fileName').textContent = fileName;
});

// ===== INICIALIZAR =====
document.addEventListener('DOMContentLoaded', () => {
	new GestorMascotas();
});

// ===== MOSTRAR BOTÓN DE VOLVER AL INICIO =====
const btnInicio = document.querySelector('.btn-inicio');

window.addEventListener('scroll', function() {
	if (window.scrollY > 300) {
		btnInicio.style.display = 'flex';
	} else {
		btnInicio.style.display = 'none';
	}
});

// Ocultar al cargar la página
btnInicio.style.display = 'none';