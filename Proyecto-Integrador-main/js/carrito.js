let cart = JSON.parse(localStorage.getItem('cart')) || [];

// Función para agregar un producto al carrito
function addToCart(id, title, price, image, stockDisponible) {
    try {
        let cartItem = cart.find(item => item.id === id); // Busca por ID

        if (cartItem) {
            // Si el producto ya está en el carrito, verifica el stock antes de aumentar la cantidad
            if (cartItem.quantity < stockDisponible) {
                cartItem.quantity += 1;
                alert(`"${title}" añadido al carrito. Cantidad actual: ${cartItem.quantity}`);
            } else {
                alert(`No se puede añadir más de "${title}". Stock máximo alcanzado en el carrito (${stockDisponible}).`);
                return false;
            }
        } else {
            // Si el producto no está en el carrito y hay stock
            if (stockDisponible > 0) {
                cart.push({ id, title, price, image, quantity: 1 }); 
                alert(`"${title}" añadido al carrito.`);
            } else {
                alert(`No hay stock disponible para "${title}".`);
                return false;
            }
        }
        localStorage.setItem('cart', JSON.stringify(cart));
        updateCartDisplay();
        document.getElementById("cart-sidebar").classList.add("active");
        return true;
    } catch (error) {
        console.error("Error al agregar al carrito:", error);
        alert("Ocurrió un error al agregar el producto al carrito.");
        return false;
    }
}

// Función para actualizar la visualización del carrito
function updateCartDisplay() {
    try {
        const cartContainer = document.getElementById('cart-container'); 
        cartContainer.innerHTML = ''; 
        let total = 0; 

        if (cart.length === 0) {
            cartContainer.innerHTML = '<p>El carrito está vacío.</p>';
            document.getElementById('checkout-button').disabled = true; 
            return;
        } else {
            document.getElementById('checkout-button').disabled = false;
        }

        cart.forEach((item, index) => { 
            const itemTotal = (item.price * item.quantity).toFixed(2); 
            total += item.price * item.quantity; 

            const itemDiv = document.createElement('div'); 
            itemDiv.classList.add('cart-item'); 
            itemDiv.innerHTML = `
                <img src="${item.image}" alt="${item.title}" class="img">
                <div class="item-details">
                    <h4>${item.title}</h4>
                    <p>Precio: $${item.price.toFixed(2)}</p>
                </div>
                <div class="quantity-controls">
                    <button class="btn btn-danger btn-sm" onclick="updateCartQuantity(${index}, -1)">
                        ${item.quantity > 1 ? '-' : 'x'}
                    </button>
                    <span class="quantity">${item.quantity}</span>
                    <button class="btn btn-info btn-sm" onclick="updateCartQuantity(${index}, 1)">+</button>
                </div>
                <div class="cart-summary">
                    <p>$${itemTotal}</p>
                </div>
            `; 
            cartContainer.appendChild(itemDiv); 
        });

        const totalDiv = document.createElement('div'); 
        totalDiv.classList.add('cart-total'); 
        totalDiv.innerHTML = `<h3>Total: $${total.toFixed(2)}</h3>`;
        cartContainer.appendChild(totalDiv); 
    } catch (error) {
        console.error("Error al cargar el carrito:", error); 
        alert("Ocurrió un error al cargar el carrito."); 
    }
}

function updateCartQuantity(index, change) {
    const item = cart[index];
    if (!item) return;

    
    if (change === 1) {
        item.quantity += 1;
    } else { 
        item.quantity += change;
        if (item.quantity <= 0) {
            cart.splice(index, 1);
        }
    }
    localStorage.setItem('cart', JSON.stringify(cart));
    updateCartDisplay();
}

// Función para vaciar el carrito
function emptyCart() {
    cart = [];
    localStorage.setItem('cart', JSON.stringify(cart));
    updateCartDisplay();
    alert('Carrito vaciado.');
}

async function checkout() {
    if (cart.length === 0) {
        alert('El carrito está vacío. Agrega productos antes de finalizar la compra.');
        return;
    }

    
    const lineasPedido = cart.map(item => ({
        producto: { id: item.id },
        cantidad: item.quantity
    }));

    const usuarioId = 1;
    const nuevoPedidoData = {
        usuarioId: usuarioId,
        lineas: lineasPedido
    };

    try {
        const resCreacion = await fetch('http://localhost:8080/api/pedidos', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(nuevoPedidoData)
        });

        if (!resCreacion.ok) {
            const errorData = await resCreacion.json();
            throw new Error(errorData.message || 'Error al crear el pedido.');
        }

        const pedidoCreado = await resCreacion.json();
        
        const resConfirmacion = await fetch(`http://localhost:8080/api/pedidos/${pedidoCreado.id}/confirmar`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            }
        });

        if (!resConfirmacion.ok) {
            const errorData = await resConfirmacion.json();
            throw new Error(errorData.message || 'Error al confirmar el pedido y reducir stock.');
        }

        emptyCart(); 
        // Mostrar modal de éxito
        document.getElementById('success-modal').style.display = 'block';
        document.getElementById('cart-sidebar').classList.remove('active'); // Cerrar carrito
        
        if (typeof fetchProductos === 'function') {
            fetchProductos(1);
        }

    } catch (error) {
        console.error("Error al finalizar la compra:", error);
        alert("Ocurrió un error al finalizar la compra: " + error.message);
    }
}

document.addEventListener('DOMContentLoaded', () => {
    const openCartBtn = document.getElementById('open-cart');
    const closeCartBtn = document.getElementById('close-cart');
    const cartSidebar = document.getElementById('cart-sidebar');
    const emptyCartBtn = document.getElementById('empty-button');
    const checkoutBtn = document.getElementById('checkout-button');
    const successModal = document.getElementById('success-modal');
    const closeSuccessModalBtn = document.getElementById('close-success-modal');


    if (openCartBtn) openCartBtn.addEventListener('click', () => {
        cartSidebar.classList.add('active');
        updateCartDisplay();
    });
    if (closeCartBtn) closeCartBtn.addEventListener('click', () => {
        cartSidebar.classList.remove('active');
    });
    if (emptyCartBtn) emptyCartBtn.addEventListener('click', emptyCart);
    if (checkoutBtn) checkoutBtn.addEventListener('click', checkout);
    if (closeSuccessModalBtn) closeSuccessModalBtn.addEventListener('click', () => {
        successModal.style.display = 'none';
    });

    const urlParams = new URLSearchParams(window.location.search);
    if (urlParams.get('openCart') === 'true') {
        if (cartSidebar) {
            cartSidebar.classList.add('active');
            updateCartDisplay();
        }
    }
    // Cargar el carrito al inicio (independientemente de la URL)
    updateCartDisplay();
});