document.addEventListener('DOMContentLoaded', () => {
    const apiURL = 'http://localhost:8080/api/productos';
    const categoriasApiURL = 'http://localhost:8080/api/categorias';

    // Elementos
    const formAgregar = document.getElementById('form-agregar');
    const formBuscar = document.getElementById('form-buscar');
    const btnListarTodos = document.getElementById('btn-listar-todos');
    const resultadosGestion = document.getElementById('resultados-gestion');
    const formActualizar = document.getElementById('form-actualizar');
    const btnCancelarActualizar = document.getElementById('btn-cancelar-actualizar');
    const selectCategoriaAgregar = document.getElementById('agregar-categoria');
    const selectCategoriaActualizar = document.getElementById('actualizar-categoria');

    // Función para mostrar producto en formato simple para gestión
    function crearCardProducto(producto) {
        return `
            <div style="border: 1px solid #ccc; padding: 1rem; margin-bottom: 0.5rem;">
                <strong>${producto.nombre}</strong><br/>
                Descripción: ${producto.descripcion || 'N/A'}<br/>
                Categoría: ${producto.categoria ? producto.categoria.nombre : 'N/A'}<br/>
                Precio: $${producto.precio.toFixed(2)}<br/>
                Stock: ${producto.stock}<br/>
                ${producto.imagenUrl ? `<img src="${producto.imagenUrl}" alt="${producto.nombre}" style="width: 100px; height: auto;"><br/>` : ''}
                <button class="btn-editar" data-id="${producto.id}">Editar</button>
                <button class="btn-eliminar" data-id="${producto.id}">Eliminar</button>
            </div>
        `;
    }

    // Listar productos con filtro opcional
    async function listarProductos(searchTerm = '') {
        try {
            const res = await fetch(apiURL);
            if (!res.ok) throw new Error('Error al obtener productos');
            const productos = await res.json();

            let productosFiltrados = productos;
            if (searchTerm) {
                const lowerCaseSearchTerm = searchTerm.toLowerCase();
                productosFiltrados = productos.filter(producto => {
                    const nombreMatch = producto.nombre.toLowerCase().includes(lowerCaseSearchTerm);
                    const descripcionMatch = producto.descripcion ? producto.descripcion.toLowerCase().includes(lowerCaseSearchTerm) : false;
                    const categoriaMatch = producto.categoria && producto.categoria.nombre ? producto.categoria.nombre.toLowerCase().includes(lowerCaseSearchTerm) : false;
                    const idMatch = String(producto.id) === lowerCaseSearchTerm;
                    return nombreMatch || descripcionMatch || categoriaMatch || idMatch;
                });
            }

            resultadosGestion.innerHTML = '';

            if (productosFiltrados.length === 0) {
                resultadosGestion.innerHTML = '<p>No se encontraron productos.</p>';
                return;
            }

            productosFiltrados.forEach(producto => {
                resultadosGestion.innerHTML += crearCardProducto(producto);
            });
        } catch (error) {
            alert('No se pudieron listar los productos');
            console.error(error);
        }
    }

    // Agregar producto
    formAgregar.addEventListener('submit', async e => {
        e.preventDefault();
        const nombreProducto = formAgregar['agregar-titulo'].value.trim();
        const descripcionProducto = formAgregar['agregar-descripcion'].value;
        const precioProducto = parseFloat(formAgregar['agregar-precio'].value);
        const stockProducto = parseInt(formAgregar['agregar-stock'].value, 10);
        const urlImagen = formAgregar['agregar-imagen'].value;
        const idCategoriaSeleccionada = parseInt(formAgregar['agregar-categoria'].value);

        if (precioProducto < 0 || stockProducto < 0) {
            alert('Precio y stock deben ser números positivos.');
            return;
        }
        if (!nombreProducto || !descripcionProducto || !urlImagen || isNaN(precioProducto) || isNaN(stockProducto) || isNaN(idCategoriaSeleccionada)) {
            alert('Por favor, complete todos los campos correctamente.');
            return;
        }

        const nuevoProducto = {
            nombre: nombreProducto,
            descripcion: descripcionProducto,
            precio: precioProducto,
            stock: stockProducto,
            imagenUrl: urlImagen,
            categoria: { id: idCategoriaSeleccionada }
        };

        try {
            const res = await fetch(apiURL, {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(nuevoProducto),
            });
            if (!res.ok) {
                const errorData = await res.json();
                throw new Error(errorData.message || 'Error al agregar producto');
            }
            alert('Producto agregado correctamente');
            formAgregar.reset();
            listarProductos();
        } catch (error) {
            alert('No se pudo agregar el producto: ' + error.message);
            console.error(error);
        }
    });

    // Evento de click para botones de editar y eliminar
    resultadosGestion.addEventListener('click', async e => {
        if (e.target.classList.contains('btn-eliminar')) {
            const id = e.target.dataset.id;
            if (confirm('¿Seguro que querés eliminar este producto?')) {
                try {
                    const res = await fetch(`${apiURL}/${id}`, { method: 'DELETE' });
                    if (!res.ok) {
                        const errorBody = await res.text();
                        throw new Error('Error al eliminar producto');
                    }
                    alert('Producto eliminado correctamente');
                    listarProductos();
                } catch (error) {
                    alert('No se pudo eliminar el producto');
                    console.error(error);
                }
            }
        } else if (e.target.classList.contains('btn-editar')) {
            const id = e.target.dataset.id;
            try {
                const res = await fetch(`${apiURL}/${id}`);
                if (!res.ok) throw new Error('Error al obtener producto para editar');
                const producto = await res.json();

                formActualizar['actualizar-id'].value = producto.id;
                formActualizar['actualizar-titulo'].value = producto.nombre;
                formActualizar['actualizar-descripcion'].value = producto.descripcion || '';
                if (producto.categoria && selectCategoriaActualizar) {
                    selectCategoriaActualizar.value = producto.categoria.id;
                }
                formActualizar['actualizar-precio'].value = producto.precio;
                formActualizar['actualizar-stock'].value = producto.stock;
                formActualizar['actualizar-imagen'].value = producto.imagenUrl || '';
                formActualizar.style.display = 'block';
            } catch (error) {
                alert('No se pudo cargar el producto para editar: ' + error.message);
                console.error(error);
            }
        }
    });

    // Buscar producto
    formBuscar.addEventListener('submit', e => {
        e.preventDefault();
        const filtro = formBuscar['buscar-titulo'].value;
        listarProductos(filtro);
    });

    btnListarTodos.onclick = () => {
        formBuscar.reset();
        listarProductos();
    };

    // Actualizar producto
    formActualizar.addEventListener('submit', async e => {
        e.preventDefault();

        const idProductoActualizar = formActualizar['actualizar-id'].value;
        const nombreActualizar = formActualizar['actualizar-titulo'].value.trim();
        const descripcionActualizar = formActualizar['actualizar-descripcion'].value;
        const precioActualizar = parseFloat(formActualizar['actualizar-precio'].value);
        const stockActualizar = parseInt(formActualizar['actualizar-stock'].value, 10);
        const imagenUrlActualizar = formActualizar['actualizar-imagen'].value;
        const idCategoriaActualizar = parseInt(formActualizar['actualizar-categoria'].value);

        if (precioActualizar < 0 || stockActualizar < 0) {
            alert('Precio y stock deben ser números positivos');
            return;
        }
        if (!nombreActualizar || !descripcionActualizar || !imagenUrlActualizar || isNaN(precioActualizar) || isNaN(stockActualizar) || isNaN(idCategoriaActualizar)) {
            alert('Por favor, complete todos los campos para actualizar.');
            return;
        }

        const productoActualizado = {
            id: idProductoActualizar,
            nombre: nombreActualizar,
            descripcion: descripcionActualizar,
            precio: precioActualizar,
            stock: stockActualizar,
            imagenUrl: imagenUrlActualizar,
            categoria: { id: idCategoriaActualizar }
        };

        try {
            const res = await fetch(`${apiURL}/${idProductoActualizar}`, {
                method: 'PUT',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(productoActualizado), // Envía el objeto completo
            });
            if (!res.ok) {
                const errorData = await res.json();
                throw new Error(errorData.message || 'Error al actualizar producto');
            }
            alert('Producto actualizado correctamente');
            formActualizar.style.display = 'none';
            listarProductos();
        } catch (error) {
            alert('No se pudo actualizar el producto: ' + error.message);
            console.error(error);
        }
    });

    async function cargarCategoriasParaProductos() {
        try {
            const res = await fetch(categoriasApiURL);
            if (!res.ok) throw new Error('Error al cargar categorías');
            const categorias = await res.json();

            if (selectCategoriaAgregar) {
                selectCategoriaAgregar.innerHTML = '<option value="">Seleccione una categoría</option>';
                categorias.forEach(cat => {
                    const option = document.createElement('option');
                    option.value = cat.id;
                    option.textContent = cat.nombre;
                    selectCategoriaAgregar.appendChild(option);
                });
            }

            if (selectCategoriaActualizar) {
                selectCategoriaActualizar.innerHTML = '<option value="">Seleccione una categoría</option>';
                categorias.forEach(cat => {
                    const option = document.createElement('option');
                    option.value = cat.id;
                    option.textContent = cat.nombre;
                    selectCategoriaActualizar.appendChild(option);
                });
            }

        } catch (error) {
            console.error('Error al cargar categorías:', error);
            alert('No se pudieron cargar las categorías para los productos.');
        }
    }

    cargarCategoriasParaProductos();

    // Cancelar actualizar
    btnCancelarActualizar.onclick = () => {
        formActualizar.style.display = 'none';
    };

    // Listar todos al cargar
    listarProductos();
});
