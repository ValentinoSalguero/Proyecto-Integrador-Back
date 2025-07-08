document.addEventListener('DOMContentLoaded', () => {
    // Esto es una SIMULACIÓN de la gestión de usuarios.
    // Una implementación real requeriría un backend robusto para:
    // - Almacenamiento seguro de usuarios (base de datos).
    // - Autenticación (login, registro).
    // - Autorización (roles y permisos).
    // - Cifrado de contraseñas.
    // - Manejo de sesiones/tokens.

    let usuarios = JSON.parse(localStorage.getItem('usuariosSimulados')) || [];

    const formCrearUsuario = document.getElementById('form-crear-usuario');
    const listaUsuariosDiv = document.getElementById('lista-usuarios');

    function renderUsuarios() {
        listaUsuariosDiv.innerHTML = '';
        if (usuarios.length === 0) {
            listaUsuariosDiv.innerHTML = '<p>No hay usuarios simulados. Crea uno.</p>';
            return;
        }

        const ul = document.createElement('ul');
        ul.classList.add('list-group');
        usuarios.forEach(user => {
            const li = document.createElement('li');
            li.classList.add('list-group-item', 'd-flex', 'justify-content-between', 'align-items-center');
            li.innerHTML = `
                ${user.username} (${user.email}) - Rol: ${user.role}
                <button class="btn btn-sm btn-danger btn-eliminar-usuario" data-id="${user.id}">Eliminar</button>
            `;
            ul.appendChild(li);
        });
        listaUsuariosDiv.appendChild(ul);
    }

    formCrearUsuario.addEventListener('submit', e => {
        e.preventDefault();
        const username = formCrearUsuario['nombre-usuario'].value.trim();
        const email = formCrearUsuario['email-usuario'].value.trim();
        const role = formCrearUsuario['rol-usuario'].value;

        if (!username || !email) {
            alert('Nombre de usuario y email son obligatorios.');
            return;
        }

        const nuevoUsuario = {
            id: 'USER-' + Date.now(),
            username: username,
            email: email,
            role: role
        };

        usuarios.push(nuevoUsuario);
        localStorage.setItem('usuariosSimulados', JSON.stringify(usuarios));
        alert('Usuario simulado creado correctamente!');
        formCrearUsuario.reset();
        renderUsuarios();
    });

    listaUsuariosDiv.addEventListener('click', e => {
        if (e.target.classList.contains('btn-eliminar-usuario')) {
            const id = e.target.dataset.id;
            if (confirm('¿Seguro que quieres eliminar este usuario simulado?')) {
                usuarios = usuarios.filter(user => user.id !== id);
                localStorage.setItem('usuariosSimulados', JSON.stringify(usuarios));
                alert('Usuario simulado eliminado.');
                renderUsuarios();
            }
        }
    });

    // Cargar usuarios al iniciar
    renderUsuarios();
});