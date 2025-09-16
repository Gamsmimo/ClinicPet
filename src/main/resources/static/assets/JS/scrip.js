
// ---------- DEMO seed y persistencia ----------
const demoSeed = {
    admin: { name: 'Admin Principal', email: 'admin@clinicpet.com', phone: '+57 300 000 0000', initials: 'AP' },
    users: [
        { id: 1, name: 'Laura Ríos', email: 'laura@mail.com', role: 'propietario', status: 'activo' },
        { id: 2, name: 'Dr. Felipe Mora', email: 'fmora@vet.com', role: 'veterinario', status: 'activo' },
        { id: 3, name: 'Kevin Ortiz', email: 'kevin@mail.com', role: 'adoptante', status: 'suspendido' },
        { id: 4, name: 'Dra. Ana Paz', email: 'apaz@vet.com', role: 'veterinario', status: 'activo' },
    ],
    pets: [
        { id: 1, name: 'Luna', species: 'Gato', owner: 'Laura Ríos', age: '3' },
        { id: 2, name: 'Max', species: 'Perro', owner: 'Kevin Ortiz', age: '5' },
        { id: 3, name: 'Toby', species: 'Perro', owner: 'Laura Ríos', age: '1' },
    ],
    adoptions: [
        { id: 1, pet: 'Nina', interested: 'María V', status: 'pendiente' },
        { id: 2, pet: 'Rocky', interested: 'José H', status: 'aprobado' },
        { id: 3, pet: 'Michi', interested: 'Carla S', status: 'rechazado' },
    ],
    adoptTracking: [
        { id: 11, pet: 'Rocky', stage: 'Visita domiciliaria', updated: '2025-08-15' },
        { id: 12, pet: 'Nina', stage: 'Evaluación inicial', updated: '2025-08-29' },
    ],
    reports: [
        { id: 101, desc: 'Posible maltrato en parque central', status: 'nuevo', authority: '' },
        { id: 102, desc: 'Perro abandonado en barrio Las Flores', status: 'en revisión', authority: 'Inspección Policía' },
    ],
    vets: [
        { id: 201, name: 'Clínica Huellitas', type: 'clínica', status: 'activo' },
        { id: 202, name: 'Dr. Felipe Mora', type: 'veterinario', status: 'activo' },
    ],
    vetRequests: [
        { id: 301, name: 'Clínica Vida Animal', type: 'clínica' },
        { id: 302, name: 'Dra. Lily Rincón', type: 'veterinario' },
    ],
    ratings: [
        { id: 401, author: 'Laura', target: 'Clínica Huellitas', score: 5, comment: 'Excelente atención', status: 'pendiente' },
        { id: 402, author: 'Kevin', target: 'Dr. Mora', score: 2, comment: 'tarde y descuidado', status: 'pendiente' },
    ],
    comments: [
        { id: 501, author: 'User123', text: 'Visiten mi canal para ganar $$$', status: 'pendiente' },
        { id: 502, author: 'María', text: 'Gracias por la atención con Luna ❤', status: 'pendiente' },
        { id: 503, author: 'Anon', text: 'qué servicio tan ******', status: 'pendiente' },
    ],
    agendaMonthly: [
        { month: 'Abr', cumplidas: 42, canceladas: 7 },
        { month: 'May', cumplidas: 55, canceladas: 9 },
        { month: 'Jun', cumplidas: 60, canceladas: 6 },
        { month: 'Jul', cumplidas: 58, canceladas: 10 },
        { month: 'Ago', cumplidas: 66, canceladas: 8 },
    ]
};

const storageKey = 'clinicpet_admin_data';
const state = loadState();

function loadState() {
    const raw = localStorage.getItem(storageKey);
    if (!raw) { localStorage.setItem(storageKey, JSON.stringify(demoSeed)); return structuredClone(demoSeed); }
    try { return JSON.parse(raw); } catch (e) { console.warn('Estado corrupto, resembrando'); localStorage.setItem(storageKey, JSON.stringify(demoSeed)); return structuredClone(demoSeed); }
}
function save() { localStorage.setItem(storageKey, JSON.stringify(state)); refreshAll(); }

// ---------- Utilidades UI ----------
const $ = sel => document.querySelector(sel);
const $$ = sel => Array.from(document.querySelectorAll(sel));
function setView(id) {
    $$('.nav-btn').forEach(b => b.classList.toggle('active', b.dataset.target === id));
    $$('.view').forEach(v => { v.hidden = v.id !== id; });
}
$('#nav').addEventListener('click', e => {
    if (e.target.closest('.nav-btn')) setView(e.target.closest('.nav-btn').dataset.target)
});

$('#globalSearch').addEventListener('input', (e) => {
    const q = e.target.value.toLowerCase().trim();
    filterTablesGlobally(q);
});
$('#clearSearch').addEventListener('click', () => { $('#globalSearch').value = ''; filterTablesGlobally(''); });

function filterTablesGlobally(q) {
    ['tblUsers', 'tblPets', 'tblVets', 'tblAdoptions', 'tblModeration', 'tblReports', 'tblVetRequests', 'tblRatings', 'tblAdoptTracking']
        .forEach(id => filterTableRowsByQuery($('#' + id), q));
}
function filterTableRowsByQuery(table, q) { if (!table) return; table.querySelectorAll('tbody tr').forEach(tr => { tr.style.display = tr.innerText.toLowerCase().includes(q) ? '' : 'none'; }); }

function openModal(id) { $('#' + id).showModal(); }
function closeModal(id) { $('#' + id).close(); }

// ---------- Renderizadores ----------
function renderUsers() {
    const tbody = $('#tblUsers tbody');
    const role = $('#roleFilter').value; const status = $('#statusFilter').value; const q = $('#userSearch').value.toLowerCase().trim();
    tbody.innerHTML = '';
    state.users
        .filter(u => !role || u.role === role)
        .filter(u => !status || u.status === status)
        .filter(u => u.name.toLowerCase().includes(q) || u.email.toLowerCase().includes(q))
        .forEach(u => {
            const tr = document.createElement('tr');
            tr.innerHTML = `
        <td>${u.name}</td>
        <td>${u.email}</td>
        <td><span class="pill">${u.role}</span></td>
        <td><span class="pill ${u.status === 'activo' ? 'green' : 'red'}">${u.status}</span></td>
        <td>
          <button class="btn ghost" onclick="editUser(${u.id})">Editar</button>
          <button class="btn warn" onclick="toggleSuspend(${u.id})">${u.status === 'activo' ? 'Suspender' : 'Reactivar'}</button>
          <button class="btn danger" onclick="deleteUser(${u.id})">Eliminar</button>
          <button class="btn" onclick="resetPassword('${u.email}')">Restablecer contraseña</button>
        </td>`;
            tbody.appendChild(tr);
        });
}

function renderPets() {
    const tbody = $('#tblPets tbody');
    const q = $('#petSearch').value.toLowerCase().trim();
    tbody.innerHTML = '';
    state.pets.filter(p => (p.name + p.owner).toLowerCase().includes(q))
        .forEach(p => {
            const tr = document.createElement('tr');
            tr.innerHTML = `<td>${p.name}</td><td>${p.species}</td><td>${p.owner}</td><td>${p.age}</td>`;
            tbody.appendChild(tr);
        });
}

function renderAdoptions() {
    const tbody = $('#tblAdoptions tbody');
    tbody.innerHTML = '';
    state.adoptions.forEach(a => {
        const tr = document.createElement('tr');
        const color = a.status === 'aprobado' ? 'green' : (a.status === 'rechazado' ? 'red' : 'warn');
        tr.innerHTML = `<td>${a.pet}</td><td>${a.interested}</td><td><span class="pill ${color}">${a.status}</span></td>
      <td>
        <button class="btn success" onclick="setAdoptionStatus(${a.id},'aprobado')">Aprobar</button>
        <button class="btn danger" onclick="setAdoptionStatus(${a.id},'rechazado')">Rechazar</button>
      </td>`
        tbody.appendChild(tr);
    });
    const tb2 = $('#tblAdoptTracking tbody');
    tb2.innerHTML = '';
    state.adoptTracking.forEach(t => {
        const tr = document.createElement('tr');
        tr.innerHTML = `<td>${t.pet}</td><td>${t.stage}</td><td>${t.updated}</td>`;
        tb2.appendChild(tr);
    })
}

function renderReports() {
    const tbody = $('#tblReports tbody');
    tbody.innerHTML = '';
    state.reports.forEach(r => {
        const tr = document.createElement('tr');
        const color = r.status === 'nuevo' ? 'warn' : (r.status === 'cerrado' ? 'green' : '');
        tr.innerHTML = `<td>#${r.id}</td><td>${r.desc}</td><td><span class="pill ${color}">${r.status}</span></td>
      <td>${r.authority || '<span class=\'notice\'>Sin asignar</span>'}</td>
      <td>
        <select onchange="assignAuthority(${r.id}, this.value)">
          <option value="">Asignar autoridad…</option>
          <option>Inspección Policía</option>
          <option>Secretaría de Ambiente</option>
          <option>Protección Animal</option>
        </select>
        <button class="btn ghost" onclick="setReportStatus(${r.id}, 'en revisión')">En revisión</button>
        <button class="btn success" onclick="setReportStatus(${r.id}, 'cerrado')">Cerrar</button>
      </td>`
        tbody.appendChild(tr);
    })
}

function renderVets() {
    const tb = $('#tblVets tbody'); tb.innerHTML = '';
    state.vets.forEach(v => {
        const tr = document.createElement('tr');
        const color = v.status === 'activo' ? 'green' : 'red';
        tr.innerHTML = `<td>${v.name}</td><td>${v.type}</td><td><span class="pill ${color}">${v.status}</span></td>
      <td><button class="btn warn" onclick="toggleVet(${v.id})">${v.status === 'activo' ? 'Suspender' : 'Activar'}</button></td>`;
        tb.appendChild(tr);
    });
    const tbr = $('#tblVetRequests tbody'); tbr.innerHTML = '';
    state.vetRequests.forEach(v => {
        const tr = document.createElement('tr');
        tr.innerHTML = `<td>${v.name}</td><td>${v.type}</td>
      <td>
        <button class="btn success" onclick="approveVet(${v.id})">Aprobar</button>
        <button class="btn danger" onclick="rejectVet(${v.id})">Rechazar</button>
      </td>`;
        tbr.appendChild(tr);
    });
    const tbr2 = $('#tblRatings tbody'); tbr2.innerHTML = '';
    state.ratings.forEach(r => {
        const tr = document.createElement('tr');
        tr.innerHTML = `<td>${r.author}</td><td>${r.target}</td><td>${'★'.repeat(r.score)}</td><td>${r.comment}</td>
      <td>
        <button class="btn success" onclick="setRatingStatus(${r.id},'aprobado')">Validar</button>
        <button class="btn danger" onclick="setRatingStatus(${r.id},'rechazado')">Rechazar</button>
      </td>`;
        tbr2.appendChild(tr);
    })

    // KPIs agenda
    const stats = calcAgendaStats();
    const cont = $('#agendaStats'); cont.innerHTML = '';
    const blocks = [
        { label: 'Citas cumplidas (30 días)', value: stats.cumplidas30 },
        { label: 'Cancelaciones (30 días)', value: stats.canceladas30 },
        { label: 'Tasa de no-show', value: (stats.noshow * 100).toFixed(1) + '%' }
    ];
    blocks.forEach(b => {
        const div = document.createElement('div'); div.className = 'kpi';
        div.innerHTML = `<div class="label">${b.label}</div><div class="value">${b.value}</div>`; cont.appendChild(div);
    })
}

function renderModeration() {
    const tbody = $('#tblModeration tbody');
    tbody.innerHTML = '';
    state.comments.forEach(c => {
        const flagged = isToxic(c.text) || looksSpam(c.text);
        const statusPill = flagged ? '<span class="pill red">posible infracción</span>' : '<span class="pill green">limpio</span>';
        const tr = document.createElement('tr');
        tr.innerHTML = `<td>${c.author}</td><td>${highlightBadWords(c.text)}</td><td>${statusPill}</td>
      <td>
        <button class="btn success" onclick="moderate(${c.id},'aprobado')">Aprobar</button>
        <button class="btn danger" onclick="moderate(${c.id},'rechazado')">Rechazar</button>
      </td>`;
        tbody.appendChild(tr);
    })
}

function refreshAll() {
    renderUsers(); renderPets(); renderAdoptions(); renderReports(); renderVets(); renderModeration();
    updateKPIs(); drawAgendaChart();
    $('#adminName').textContent = state.admin.name;
    $('#adminEmail').textContent = state.admin.email;
    $('#adminPhone').textContent = state.admin.phone;
    $('#avatar').textContent = (state.admin.initials || 'AP').slice(0, 3).toUpperCase();
}

function updateKPIs() {
    $('#kpiUsers').textContent = state.users.filter(u => u.status === 'activo').length;
    $('#kpiPets').textContent = state.pets.length;
    $('#kpiAdoptions').textContent = state.adoptions.filter(a => a.status === 'pendiente').length;
    $('#kpiComments').textContent = state.comments.length;
}

// ---------- Acciones Usuarios ----------
$('#btnNewUser').addEventListener('click', () => {
    $('#modalUserTitle').textContent = 'Nuevo usuario';
    $('#formUser').reset();
    $('#formUser [name=id]').value = '';
    openModal('modalUser');
});
$('#roleFilter').addEventListener('change', renderUsers);
$('#statusFilter').addEventListener('change', renderUsers);
$('#userSearch').addEventListener('input', renderUsers);

function editUser(id) {
    const u = state.users.find(x => x.id === id); if (!u) return;
    $('#modalUserTitle').textContent = 'Editar usuario';
    const f = $('#formUser');
    f.name.value = u.name; f.email.value = u.email; f.role.value = u.role; f.status.value = u.status; f.id.value = u.id;
    openModal('modalUser');
}
function toggleSuspend(id) { const u = state.users.find(x => x.id === id); if (!u) return; u.status = u.status === 'activo' ? 'suspendido' : 'activo'; save(); }
function deleteUser(id) { if (confirm('¿Eliminar usuario?')) { state.users = state.users.filter(x => x.id !== id); save(); } }
function resetPassword(email) {
    const tmp = Math.random().toString(36).slice(2, 10); alert(`Se generó una contraseña temporal para ${email}:
${tmp}`);
}

$('#formUser').addEventListener('submit', (e) => {
    e.preventDefault();
    const f = e.target; const data = Object.fromEntries(new FormData(f));
    if (data.id) {
        const u = state.users.find(x => x.id == data.id); Object.assign(u, { name: data.name, email: data.email, role: data.role, status: data.status });
    } else {
        const id = Math.max(0, ...state.users.map(u => u.id)) + 1;
        state.users.push({ id, name: data.name, email: data.email, role: data.role, status: data.status });
    }
    closeModal('modalUser'); save();
});

// ---------- Adopciones / Reportes ----------
function setAdoptionStatus(id, status) { const a = state.adoptions.find(x => x.id === id); if (!a) return; a.status = status; save(); }
function assignAuthority(id, auth) { const r = state.reports.find(x => x.id === id); if (!r) return; r.authority = auth; save(); }
function setReportStatus(id, status) { const r = state.reports.find(x => x.id === id); if (!r) return; r.status = status; save(); }

// ---------- Vets ----------
function toggleVet(id) { const v = state.vets.find(x => x.id === id); v.status = v.status === 'activo' ? 'suspendido' : 'activo'; save(); }
function approveVet(id) { const v = state.vetRequests.find(x => x.id === id); if (!v) return; state.vets.push({ id: Date.now(), name: v.name, type: v.type, status: 'activo' }); state.vetRequests = state.vetRequests.filter(x => x.id !== id); save(); }
function rejectVet(id) { state.vetRequests = state.vetRequests.filter(x => x.id !== id); save(); }
function setRatingStatus(id, status) { const r = state.ratings.find(x => x.id === id); if (!r) return; r.status = status; save(); }

// ---------- Moderación ----------
const badWords = ['idiota', 'estúpido', 'malo', '****', '$$$', 'spam'];
function isToxic(text) {
    const t = text.toLowerCase();
    return badWords.some(w => t.includes(w.replace(/[*$]/g, '')));
}
function looksSpam(text) { return /(http|www\.|\$\$\$|canal|gana\$)/i.test(text); }
function highlightBadWords(text) {
    let out = text;
    badWords.forEach(w => {
        const safe = w.replace(/[.*+?^${}()|[\]\\]/g, '\\$&');
        const re = new RegExp(safe, 'ig');
        out = out.replace(re, m =>
            `<mark style="background:rgba(239,68,68,.35);padding:2px 4px;border-radius:6px">${m}</mark>`
        );
    });
    return out;
}

function moderate(id, decision) {
    const c = state.comments.find(x => x.id === id); if (!c) return; c.status = decision; // en producción: mover a colección pública si aprobado
    state.comments = state.comments.filter(x => x.status === 'pendiente'); // mantener bandeja solo con pendientes
    save();
}

// ---------- Perfil ----------
$('#btnEditProfile').addEventListener('click', () => {
    const f = $('#formProfile'); f.name.value = state.admin.name; f.email.value = state.admin.email; f.phone.value = state.admin.phone; f.initials.value = state.admin.initials || ''; openModal('modalProfile');
});
$('#btnChangePhoto').addEventListener('click', () => alert('Para esta demo, el avatar usa iniciales. En producción puedes subir imagen.'))
$('#formProfile').addEventListener('submit', (e) => {
    e.preventDefault(); const data = Object.fromEntries(new FormData(e.target)); Object.assign(state.admin, data); closeModal('modalProfile'); save();
});

// ---------- Respaldo ----------
$('#btnExport').addEventListener('click', () => {
    const blob = new Blob([JSON.stringify(state, null, 2)], { type: 'application/json' });
    const url = URL.createObjectURL(blob);
    const a = Object.assign(document.createElement('a'), { href: url, download: 'clinicpet_admin_demo.json' }); a.click(); URL.revokeObjectURL(url);
});
$('#fileImport').addEventListener('change', (e) => {
    const file = e.target.files[0]; if (!file) return; const r = new FileReader(); r.onload = () => { try { const data = JSON.parse(r.result); Object.assign(state, data); save(); alert('Datos importados.'); } catch { alert('Archivo inválido'); } }; r.readAsText(file);
});
$('#btnResetDemo').addEventListener('click', () => { if (confirm('¿Restablecer datos de demostración?')) { localStorage.removeItem(storageKey); location.reload(); } });

// =====================
//  Tema
// =====================
const themeToggle = document.getElementById('themeToggle');
const themeIcon = document.getElementById('themeIcon');

// Al cargar la página revisa si hay un tema guardado
if (localStorage.getItem('theme') === 'light') {
    document.body.classList.add('light');
    themeIcon.textContent = '☀️';
} else {
    themeIcon.textContent = '🌙';
}

// Evento de click en el botón
themeToggle.addEventListener('click', () => {
    document.body.classList.toggle('light');

    if (document.body.classList.contains('light')) {
        themeIcon.textContent = '☀️';     // Modo claro
        localStorage.setItem('theme', 'light');
    } else {
        themeIcon.textContent = '🌙';     // Modo oscuro
        localStorage.setItem('theme', 'dark');
    }
});

// ---------- Gráfico agenda (sin librerías) ----------
function drawAgendaChart() {
    const canvas = document.getElementById('chartAgenda'); const ctx = canvas.getContext('2d');
    const data = state.agendaMonthly; const W = canvas.width, H = canvas.height;
    ctx.clearRect(0, 0, W, H);
    ctx.fillStyle = 'rgba(255,255,255,0.03)'; ctx.fillRect(0, 0, W, H);
    const pad = 40; ctx.strokeStyle = 'rgba(255,255,255,.25)'; ctx.lineWidth = 1; ctx.beginPath(); ctx.moveTo(pad, 10); ctx.lineTo(pad, H - pad); ctx.lineTo(W - 10, H - pad); ctx.stroke();
    const maxY = Math.max(...data.map(d => Math.max(d.cumplidas, d.canceladas))) + 10;
    function y(v) { return H - pad - (v / maxY) * (H - pad - 20); }
    function x(i) { return pad + (i + 1) * ((W - pad - 20) / (data.length + 1)); }
    function drawLine(key) { ctx.beginPath(); data.forEach((d, i) => { const X = x(i), Y = y(d[key]); if (i === 0) ctx.moveTo(X, Y); else ctx.lineTo(X, Y); }); ctx.strokeStyle = key === 'cumplidas' ? '#22d3ee' : '#ef4444'; ctx.lineWidth = 2; ctx.stroke(); }
    drawLine('cumplidas'); drawLine('canceladas');
    ctx.fillStyle = '#e5e7eb'; ctx.font = '12px system-ui';
    data.forEach((d, i) => { ctx.beginPath(); ctx.arc(x(i), y(d.cumplidas), 3, 0, Math.PI * 2); ctx.fill(); ctx.beginPath(); ctx.arc(x(i), y(d.canceladas), 3, 0, Math.PI * 2); ctx.fill(); ctx.fillText(d.month, x(i) - 8, H - 16); });
    ctx.fillText('Cumplidas', W - 140, 20); ctx.fillStyle = '#22d3ee'; ctx.fillRect(W - 170, 13, 10, 3); ctx.fillStyle = '#e5e7eb';
    ctx.fillText('Canceladas', W - 140, 36); ctx.fillStyle = '#ef4444'; ctx.fillRect(W - 170, 29, 10, 3);
}
function calcAgendaStats() {
    const last = state.agendaMonthly.slice(-2);
    const cumplidas30 = last.reduce((a, b) => a + b.cumplidas, 0);
    const canceladas30 = last.reduce((a, b) => a + b.canceladas, 0);
    const noshow = canceladas30 / Math.max(1, (cumplidas30 + canceladas30));
    return { cumplidas30, canceladas30, noshow };
}

window.addEventListener('DOMContentLoaded', () => {
    refreshAll();
});
