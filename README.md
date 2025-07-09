Proyecto-Integrador-Back
Descripción del Proyecto
Este proyecto es la pre-entrega de un sistema básico de gestión, desarrollado en Java utilizando Spring Boot y MySQL. A través de él, pude aplicar y consolidar mis conocimientos en Programación Orientada a Objetos (POO), el manejo de colecciones, excepciones y la organización del código, así como la implementación de una API RESTful para un sistema de E-commerce.

Objetivo General
Desarrollar una API RESTful completa en Java utilizando Spring Boot y MySQL para gestionar un sistema de E-commerce, integrándose con una aplicación frontend. La aplicación deberá aplicar correctamente conceptos avanzados de programación en Java, arquitectura REST, bases de datos relacionales, validaciones, excepciones y organización modular.

Estructura del Proyecto
El proyecto está organizado en las siguientes áreas clave:

Java: Se utilizaron conceptos fundamentales de Java, como tipos de datos, variables, y operadores aritméticos, lógicos y relacionales en las funciones de cálculo y validación. Se emplean variables de tipo int (para cantidades e IDs), double (para precios), String (para nombres/descripciones), y boolean.

Programación Orientada a Objetos (POO): Se implementaron clases (Producto, Pedido, Categoria), objetos, encapsulamiento, así como herencia y polimorfismo para extender funcionalidades.

Colecciones: Se utilizaron ArrayList para manejar productos y ArrayList<LineaPedido> para los productos dentro de un pedido.

Manejo de Excepciones: Se implementaron bloques try/catch para manejar errores comunes (por ejemplo, NumberFormatException al convertir datos) y se crearon excepciones personalizadas como StockInsuficienteException.

Organización de Código: El código está estructurado en paquetes lógicos para mejorar la modularidad y facilitar el mantenimiento, incluyendo com.techlab.productos, com.techlab.pedidos, com.techlab.excepciones, y com.techlab.categorias.

Spring Boot: Utilizado para construir la API RESTful.

MySQL: Base de datos relacional para persistir los datos.

JPA/Hibernate: Para la interacción con la base de datos a través de entidades.

Requisitos Cumplidos
Estructura y POO
Se desarrollaron clases clave como Producto (con id, nombre, descripcion, precio, categoria, imagenUrl, stock, getters y setters) y Pedido (con id, usuarioId, fechaPedido, estado, una lista de LineaPedido y métodos para calcular el total).

La clase Main (implícita en Spring Boot como aplicación principal) orquesta la ejecución, delegando la lógica de negocio a los servicios (ProductoService, PedidoService, CategoriaService).

Se implementó herencia en Producto (@Inheritance(strategy = InheritanceType.SINGLE_TABLE) y @DiscriminatorColumn) permitiendo la extensión a subclases si se deseara.

Gestión de Productos
La API ofrece los siguientes endpoints para la gestión de productos:

Listar productos disponibles: GET /api/productos.

Obtener detalles individuales de un producto: GET /api/productos/{id}.

Agregar nuevos productos al catálogo: POST /api/productos. Los productos tienen atributos como ID, Nombre, Descripción, Precio, Categoría, Imagen (URL) y Stock.

Actualizar información de productos existentes: PUT /api/productos/{id}. Se puede actualizar precio o stock, validando que los valores sean coherentes (ej. stock no negativo).

Eliminar productos: DELETE /api/productos/{id}. El sistema permite eliminar un producto por su ID.

Gestión de Categorías
La API ofrece los siguientes endpoints para la gestión de categorías:

Listar categorías disponibles: GET /api/categorias.

Obtener detalles individuales de una categoría: GET /api/categorias/{id}.

Agregar nuevas categorías: POST /api/categorias.

Actualizar información de categorías existentes: PUT /api/categorias/{id}.

Eliminar categorías: DELETE /api/categorias/{id}.

Gestión de Pedidos
La API ofrece los siguientes endpoints para la gestión de pedidos:

Creación de Pedidos: POST /api/pedidos.

Permite crear un pedido nuevo, solicitando productos y sus cantidades.

Validación de Stock: Se verifica la disponibilidad de stock antes de confirmar un pedido, lanzando StockInsuficienteException si no hay suficiente.

Cálculo de Costo Total: Calcula el valor total de cada pedido.

Actualización de Stock: Disminuye el stock de los productos una vez que el pedido es confirmado.

Listado de Pedidos: GET /api/pedidos para todos los pedidos, y GET /api/pedidos/usuario/{usuarioId} para pedidos por usuario.

Confirmar Pedido: POST /api/pedidos/{id}/confirmar.

Cambiar Estado del Pedido: PUT /api/pedidos/{id}/estado (permite gestionar estados como pendiente, confirmado, enviado, entregado, cancelado).

Eliminar Pedido: DELETE /api/pedidos/{id}.

Interfaz y Usabilidad (HTML - Requisitos para el Frontend)
Aunque este es el repositorio del backend, el frontend posee un menú principal interactivo con las siguientes opciones:

=================================== SISTEMA DE GESTIÓN - TECHLAB ==================================
1) Gestionar Productos
2) Gestionar Categorías
3) Ver Carrito de Compras
4) Realizar Pedido
5) Consultar Historial de Pedidos
6) Administración (usuarios y stock)
7) Salir
Además de una sección de "Gestión de Productos" con las siguientes opciones:

=================================== ———————Gestion de Productos——————-==================================
a) Agregar Producto
b) Listar Productos
c) Buscar Producto por ID
d) Actualizar Producto
e) Eliminar Producto
f) Volver al menú principal
