document.addEventListener("DOMContentLoaded", () => {
    try {
        const cardContainer = document.getElementById('card-container');

        let isDarkMode = localStorage.getItem('darkMode') === 'true';

        function crearCardProducto(product) {
            const price = product.precio.toFixed(2);
            let productDiv = document.createElement('div');
            productDiv.className = 'card-product flex';
            productDiv.innerHTML = `
                <div class="container-img"> <img src="${product.imagenUrl || product.images && product.images[0] || 'https://via.placeholder.com/150'}" alt="${product.nombre}" class="product-image">
                </div>
                <div class="card-details"> <h3 class="card-product_title">${product.nombre}</h3>
                    <p class="card-product_description">${product.descripcion || 'Sin descripción'}</p>
                    <p>Stock: ${product.stock}</p>
                    <p class="price">$ ${price}</p>
                    <button class="btn-add"
                        onclick="addToCart('${product.id}', '${product.nombre}', ${price}, '${product.imagenUrl || product.images && product.images[0] || 'https://via.placeholder.com/150'}', ${product.stock});">
                        COMPRAR
                    </button>
                </div>
            `;
            return productDiv;
        }

        function fetchProductos() {
            try {
                fetch(`http://localhost:8080/api/productos`)
                .then(response => {
                    if (!response.ok) throw new Error(`HTTP error! status: ${response.status}`);

                    return response.json();
                })
                .then(data => {
                    try {
                        const productos = data;
                        const cardContainer = document.getElementById('card-container');

                        cardContainer.innerHTML = '';

                        productos.forEach(product => {
                            const productDiv = crearCardProducto(product);
                            cardContainer.appendChild(productDiv);
                        });

                        applyDarkModeToCards();

                        if (pageInfo) pageInfo.textContent = ``;
                        if (prevBtn) prevBtn.style.display = 'none';
                        if (nextBtn) nextBtn.style.display = 'none';

                    } catch (innerError) {
                        console.error('Error al procesar los datos de la API:', innerError);
                    }
                })
                .catch(error => {
                    console.error('Error al cargar los productos desde la API:', error);
                    cardContainer.innerHTML = '<p>Lo sentimos, no pudimos cargar los productos en este momento. Intente de nuevo más tarde.</p>';
                });
            } catch (outerError) {
                console.error('Error en la función fetchProductos:', outerError);
            }
        }

        // Aplicar o quitar el modo oscuro de las tarjetas cargadas
        function applyDarkModeToCards() {
            try {
                const cardProducts = document.querySelectorAll('.card-product');
                cardProducts.forEach(card => {
                    if (isDarkMode) {
                        card.classList.add('dark-mode');
                        let priceElement = card.querySelector('.price');
                        if (priceElement) {
                            priceElement.classList.add('dark-mode');
                        }
                    } else {
                        card.classList.remove('dark-mode');
                        let priceElement = card.querySelector('.price');
                        if (priceElement) {
                            priceElement.classList.remove('dark-mode');
                        }
                    }
                });
            } catch (error) {
                console.error('Error al aplicar el modo oscuro a las tarjetas:', error);
            }
        }

        // Cambiar entre modo oscuro y claro cuando el checkbox es marcado o desmarcado
        document.getElementById('input').addEventListener('change', function () {
            try {
                isDarkMode = this.checked;

                // Guardar la preferencia en el almacenamiento local
                localStorage.setItem('darkMode', isDarkMode);
                applyDarkModeToCards(); // Aplicar el modo oscuro a las tarjetas

                // Cambiar el estilo de los elementos del documento según el modo
                document.body.classList.toggle('dark-mode', isDarkMode);
                document.querySelector('header').classList.toggle('dark-mode', isDarkMode);
                document.querySelector('footer').classList.toggle('dark-mode', isDarkMode);
                document.querySelectorAll('.navbar-nav .nav-link').forEach(link => link.classList.toggle('dark-mode', isDarkMode));
            } catch (error) {
                console.error('Error al cambiar entre modo claro y oscuro:', error);
            }
        });

        fetchProductos(); 
    } catch (error) {
        console.error('Error en la inicialización del DOMContentLoaded:', error);
    }
});