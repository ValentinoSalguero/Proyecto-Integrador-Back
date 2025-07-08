// gestion-categorias.js

document.addEventListener('DOMContentLoaded', () => {
    const apiURL = 'http://localhost:8080/api/categorias';

    const formAgregarCategoria = document.getElementById('form-agregar-categoria');
    const listaCategorias = document.getElementById('lista-categorias');
    const formActualizarCategoria = document.getElementById('form-actualizar-categoria');
    const btnCancelarActualizarCategoria = document.getElementById('btn-cancelar-actualizar-categoria');

    // Función para listar categorías
    async function listarCategorias() {
        try {
            const res = await fetch(apiURL);
            if (!res.ok) throw new Error('Error al obtener categorías');
            const categorias = await res.json();

            listaCategorias.innerHTML = '';
            if (categorias.length === 0) {
                listaCategorias.innerHTML = '<li class="list-group-item">No hay categorías.</li>';
                return;
            }

            categorias.forEach(categoria => {
                const li = document.createElement('li');
                li.classList.add('list-group-item', 'd-flex', 'justify-content-between', 'align-items-center');
                li.innerHTML = `
                    ${categoria.nombre}
                    <div>
                        <button class="btn btn-sm btn-info btn-editar-categoria me-2" data-id="${categoria.id}" data-nombre="${categoria.nombre}">Editar</button>
                        <button class=\"btn btn-sm btn-danger btn-eliminar-categoria\" data-id=\"${categoria.id}\">Eliminar</button>
                    </div>
                `;
                listaCategorias.appendChild(li);
            });
        } catch (error) {
            alert('No se pudieron listar las categorías');
            console.error(error);
        }
    }

    // Agregar categoría
    formAgregarCategoria.addEventListener('submit', async e => {
        e.preventDefault();
        const nombre = formAgregarCategoria['nombre-categoria'].value.trim();

        if (!nombre) {
            alert('El nombre de la categoría no puede estar vacío');
            return;
        }

        try {
            const res = await fetch(apiURL, {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({ nombre }),
            });
            if (!res.ok) throw new Error('Error al agregar categoría');
            alert('Categoría agregada correctamente');
            formAgregarCategoria.reset();
            listarCategorias();
        } catch (error) {
            alert('No se pudo agregar la categoría. Asegúrese de que no exista ya.');
            console.error(error);
        }
    });

    // Eventos para editar y eliminar categorías
    listaCategorias.addEventListener('click', async e => {
        if (e.target.classList.contains('btn-eliminar-categoria')) {
            const id = e.target.dataset.id;
            if (confirm('¿Seguro que quieres eliminar esta categoría? Esto podría afectar a los productos asociados.')) {
                try {
                    const res = await fetch(`${apiURL}/${id}`, { method: 'DELETE' });
                    if (!res.ok) throw new Error('Error al eliminar categoría');
                    alert('Categoría eliminada correctamente');
                    listarCategorias();
                } catch (error) {
                    alert('No se pudo eliminar la categoría');
                    console.error(error);
                }
            }
        } else if (e.target.classList.contains('btn-editar-categoria')) {
            const id = e.target.dataset.id;
            const nombre = e.target.dataset.nombre;
            formActualizarCategoria['actualizar-categoria-id'].value = id;
            formActualizarCategoria['actualizar-nombre-categoria'].value = nombre;
            formActualizarCategoria.style.display = 'block';
        }
    });

    // Actualizar categoría
    formActualizarCategoria.addEventListener('submit', async e => {
        e.preventDefault();
        const id = formActualizarCategoria['actualizar-categoria-id'].value;
        const newName = formActualizarCategoria['actualizar-nombre-categoria'].value.trim();

        if (!newName) {
            alert('El nombre de la categoría no puede estar vacío');
            return;
        }

        try {
            const res = await fetch(`${apiURL}/${id}`, {
                method: 'PUT',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({ nombre: newName }),
            });
            if (!res.ok) throw new Error('Error al actualizar categoría');
            alert('Categoría actualizada correctamente');
            formActualizarCategoria.style.display = 'none';
            listarCategorias();
        } catch (error) {
            alert('No se pudo actualizar la categoría');
            console.error(error);
        }
    });

    // Cancelar actualización
    btnCancelarActualizarCategoria.onclick = () => {
        formActualizarCategoria.style.display = 'none';
    };

    // Inicializar la lista de categorías
    listarCategorias();
});