document.addEventListener('DOMContentLoaded', () => {
    const apiPedidosURL = 'http://localhost:8080/api/pedidos';
    const apiProductosURL = 'http://localhost:8080/api/productos';
    
    let historialPedidos = [];
    let productosDisponibles = [];

    const productosLista = document.getElementById('productos-lista');
    const totalPedidoSpan = document.getElementById('total-pedido');
    const btnConfirmar = document.getElementById('btn-confirmar-pedido');
    const listaPedidos = document.getElementById('lista-pedidos');
    const listaAlertas = document.getElementById('lista-alertas');

    // Obtener productos disponibles para el formulario de pedidos
    async function fetchProductosParaPedido() {
        try {
            const res = await fetch(apiProductosURL);
            if (!res.ok) {
                console.error('Error al obtener productos:', res.statusText);
                throw new Error('Error al obtener productos disponibles');
            }
            productosDisponibles = await res.json();
            renderProductos();
        } catch (error) {
            console.error('No se pudieron cargar los pedidos:', error);
            alert('Error al cargar los pedidos. Intenta de nuevo más tarde.');
        }
    }

    // Mostrar el historial de pedidos desde el backend
    async function mostrarHistorial() {
        if (!listaPedidos) return;

        try {
            const res = await fetch(apiPedidosURL);
            if (!res.ok) {
                console.error('Error al obtener el historial de pedidos:', res.statusText);
                throw new Error('Error al obtener el historial de pedidos');
            }
            historialPedidos = await res.json(); // Asigna la respuesta del backend

            listaPedidos.innerHTML = ''; // Limpia la lista actual

            if (historialPedidos.length === 0) {
                listaPedidos.innerHTML = '<p>No hay pedidos realizados.</p>';
                return;
            }

            historialPedidos.forEach(pedido => {
                const pedidoDiv = document.createElement('div');
                pedidoDiv.classList.add('card', 'mb-3');
                pedidoDiv.innerHTML = `
                    <div class="card-header">
                        <strong>Pedido ID: ${pedido.id}</strong> - Fecha: ${new Date(pedido.fechaPedido).toLocaleString()} - Estado: ${pedido.estado}
                    </div>
                    <ul class="list-group list-group-flush">
                        ${pedido.lineas.map(linea => `
                            <li class="list-group-item">
                                ${linea.producto.nombre} (x${linea.cantidad}) - $${linea.subtotal.toFixed(2)}
                            </li>
                        `).join('')}
                    </ul>
                    <div class="card-footer">
                        Total del Pedido: $${pedido.costoTotal.toFixed(2)}
                    </div>
                `;
                listaPedidos.appendChild(pedidoDiv);
            });
        } catch (error) {
            console.error('Error al cargar el historial de pedidos:', error);
            listaPedidos.innerHTML = '<p>No se pudo cargar el historial de pedidos.</p>';
        }
    }

    // Confirmar un nuevo pedido y enviarlo al backend
    async function confirmarPedido() {
        const lineasPedido = [];
        const productosEnFormulario = productosLista.querySelectorAll('.producto-item');

        productosEnFormulario.forEach(item => {
            const id = item.dataset.productId;
            const cantidadInput = item.querySelector('.cantidad-input');
            const cantidad = parseInt(cantidadInput.value);

            if (cantidad > 0) {
                const productoSeleccionado = productosDisponibles.find(p => p.id == id);
                if (productoSeleccionado) {
                    lineasPedido.push({
                        producto: { 
                            id: productoSeleccionado.id,
                            nombre: productoSeleccionado.nombre,
                            precio: productoSeleccionado.precio
                        },
                        cantidad: cantidad
                    });
                }
            }
        });

        if (lineasPedido.length === 0) {
            alert('El pedido está vacío. Agrega al menos un producto.');
            return;
        }

        // Asume un usuarioId fijo por ahora, en un sistema real vendría de la sesión de usuario
        const usuarioId = 1; // ID de usuario de ejemplo
        const nuevoPedidoData = {
            usuarioId: usuarioId,
            lineas: lineasPedido
        };

        try {
            const resCreacion = await fetch(apiPedidosURL, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(nuevoPedidoData)
            });

            if (!resCreacion.ok) {
                const errorData = await resCreacion.json();
                throw new Error(errorData.message || 'Error al confirmar el pedido');
            }

            const pedidoCreado = await resCreacion.json();

            const resConfirmacion = await fetch(`${apiPedidosURL}/${pedidoCreado.id}/confirmar`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                }
            });

            if (!resConfirmacion.ok) {
                const errorData = await resConfirmacion.json();
                throw new Error(errorData.message || 'Error al confirmar el pedido y reducir stock');
            }

            alert('Pedido creado y confirmado correctamente! ID: ' + pedidoCreado.id);

            if (document.getElementById('pedido-form')) {
                productosLista.querySelectorAll('.cantidad-input').forEach(input => input.value = 0);
                calcularTotal();
                // Recargar productos para reflejar el stock actualizado
                fetchProductosParaPedido(); 
            }
            if (document.getElementById('historial-pedidos')) {
                mostrarHistorial();
            }
        } catch (error) {
            console.error('Error en el proceso del pedido:', error);
            alert('No se pudo confirmar el pedido: ' + error.message);
        }
    }

    function renderProductos() {
        if (!productosLista) return;
        productosLista.innerHTML = '';
        if (productosDisponibles.length === 0) {
            productosLista.innerHTML = '<p>No hay productos disponibles.</p>';
            return;
        }
        productosDisponibles.forEach(p => {
            productosLista.innerHTML += `
                <div class="mb-2 producto-item" data-product-id="${p.id}">
                    <img src="${p.imagenUrl || 'https://via.placeholder.com/50'}" alt="${p.nombre}" style="width: 50px; height: 50px; margin-right: 10px;">
                    <span><strong>${p.nombre}</strong> - $${p.precio.toFixed(2)} - Stock: ${p.stock}</span>
                    <input type="number" class="form-control d-inline-block cantidad-input" min="0" max="${p.stock}" value="0" style="width: 80px; margin-left: 10px;">
                </div>
            `;
        });
    }

    function calcularTotal() {
        let total = 0;
        const productosEnLista = productosLista.querySelectorAll('.producto-item');
        productosEnLista.forEach(item => {
            const id = item.dataset.productId;
            const cantidad = parseInt(item.querySelector('.cantidad-input').value) || 0;
            const producto = productosDisponibles.find(p => p.id == id);
            if (producto) {
                total += producto.precio * cantidad;
            }
        });
        totalPedidoSpan.textContent = total.toFixed(2);
    }

    // Mostrar alertas de stock bajo
    function mostrarAlertasStock() {
        if (!listaAlertas) return;
        listaAlertas.innerHTML = '';
        const productosBajoStock = productosDisponibles.filter(p => p.stock > 0 && p.stock <= 5); // Ejemplo: bajo stock si es 5 o menos

        if (productosBajoStock.length === 0) {
            listaAlertas.innerHTML = '<p>No hay alertas de stock bajo.</p>';
            return;
        }

        productosBajoStock.forEach(p => {
            const alertDiv = document.createElement('div');
            alertDiv.classList.add('alert', 'alert-warning');
            alertDiv.textContent = `¡Alerta! El producto "${p.nombre}" tiene stock bajo: ${p.stock} unidades.`;
            listaAlertas.appendChild(alertDiv);
        });
    }

    // Eventos
    if (productosLista) {
        productosLista.addEventListener('input', e => {
            if(e.target.classList.contains('cantidad-input')) {
                const input = e.target;
                const cantidad = parseInt(input.value) || 0;
                const max = parseInt(input.max);
                if(cantidad > max) {
                    alert('Cantidad supera el stock disponible');
                    input.value = max;
                }
                calcularTotal();
            }
        });
        btnConfirmar.addEventListener('click', confirmarPedido);
    }

    if (document.getElementById('pedido-form')) {
        fetchProductosParaPedido(); 
        calcularTotal();
    }
    if (document.getElementById('historial-pedidos')) { 
        mostrarHistorial();
        mostrarAlertasStock();
    }
});
