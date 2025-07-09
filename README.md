# 🛒 Proyecto Integrador Back

Este proyecto es la **pre-entrega de un sistema básico de gestión**, desarrollado en **Java**, utilizando **Spring Boot** y **MySQL**. A través de él, se aplican conocimientos en:

- Programación Orientada a Objetos (POO)
- Manejo de colecciones y excepciones
- Organización modular del código
- Implementación de una API RESTful para un sistema de **E-commerce**

---

## 🎯 Objetivo General

Desarrollar una **API RESTful completa** en Java utilizando **Spring Boot** y **MySQL** para gestionar un sistema de E-commerce integrado con un frontend. Se aplican conceptos avanzados como:

- Arquitectura REST
- Bases de datos relacionales
- Validaciones y excepciones
- Organización modular y reutilización de código

---

## 🧱 Estructura del Proyecto

El proyecto está organizado en las siguientes áreas clave:

### ☕ Java
- Uso de tipos de datos básicos: `int`, `double`, `String`, `boolean`
- Operadores aritméticos, lógicos y relacionales

### 🔷 Programación Orientada a Objetos (POO)
- Clases como `Producto`, `Pedido`, `Categoria`
- Encapsulamiento, herencia y polimorfismo

### 🧺 Colecciones
- Uso de `ArrayList` para manejar productos y líneas de pedido (`ArrayList<LineaPedido>`)

### 🚨 Manejo de Excepciones
- Manejo con bloques `try/catch`
- Excepciones personalizadas como `StockInsuficienteException`

### 🗂️ Organización del Código
- Paquetes por responsabilidad:
  - `com.techlab.productos`
  - `com.techlab.pedidos`
  - `com.techlab.excepciones`
  - `com.techlab.categorias`

### ⚙️ Tecnologías Utilizadas
- **Spring Boot** para construcción de API REST
- **MySQL** como base de datos
- **JPA / Hibernate** para persistencia de datos

---

## ✅ Requisitos Cumplidos

### 🧩 Estructura y POO

- `Producto`: `id`, `nombre`, `descripcion`, `precio`, `categoria`, `imagenUrl`, `stock`, `getters/setters`
- `Pedido`: `id`, `usuarioId`, `fechaPedido`, `estado`, `List<LineaPedido>`, cálculo de total
- Herencia con anotaciones:  
  ```java
  @Inheritance(strategy = InheritanceType.SINGLE_TABLE)
  @DiscriminatorColumn(name = "tipo")
  
📦 Gestión de Productos
| Acción              | Endpoint                     |
| ------------------- | ---------------------------- |
| Listar productos    | `GET /api/productos`         |
| Ver producto por ID | `GET /api/productos/{id}`    |
| Agregar producto    | `POST /api/productos`        |
| Actualizar producto | `PUT /api/productos/{id}`    |
| Eliminar producto   | `DELETE /api/productos/{id}` |

🗃️ Gestión de Categorías
| Acción               | Endpoint                      |
| -------------------- | ----------------------------- |
| Listar categorías    | `GET /api/categorias`         |
| Ver categoría por ID | `GET /api/categorias/{id}`    |
| Agregar categoría    | `POST /api/categorias`        |
| Actualizar categoría | `PUT /api/categorias/{id}`    |
| Eliminar categoría   | `DELETE /api/categorias/{id}` |

📑 Gestión de Pedidos
| Acción                     | Endpoint                               |
| -------------------------- | -------------------------------------- |
| Crear pedido               | `POST /api/pedidos`                    |
| Listar pedidos             | `GET /api/pedidos`                     |
| Listar pedidos por usuario | `GET /api/pedidos/usuario/{usuarioId}` |
| Confirmar pedido           | `POST /api/pedidos/{id}/confirmar`     |
| Cambiar estado             | `PUT /api/pedidos/{id}/estado`         |
| Eliminar pedido            | `DELETE /api/pedidos/{id}`             |

Extras:

Validación de stock (StockInsuficienteException)

Cálculo de costo total

Disminución de stock al confirmar pedido

💻 Interfaz y Usabilidad (Frontend)
Aunque este es el repositorio backend, el sistema cuenta con un menú interactivo (en el frontend) con las siguientes opciones:
================ SISTEMA DE GESTIÓN - TECHLAB ================
1) Gestionar Productos
2) Gestionar Categorías
3) Ver Carrito de Compras
4) Realizar Pedido
5) Consultar Historial de Pedidos
6) Administración (usuarios y stock)
7) Salir
En la sección de Gestión de Productos:
================ —— Gestión de Productos —— ================
a) Agregar Producto
b) Listar Productos
c) Buscar Producto por ID
d) Actualizar Producto
e) Eliminar Producto
f) Volver al menú principal

🧪 Tecnologías y Herramientas
Java 17+

Spring Boot

MySQL

JPA / Hibernate

Maven

📂 Estructura de Paquetes
com.techlab
├── categorias
├── excepciones
├── pedidos
├── productos

🛠️ Autor
Desarrollado por Valentino Salguero – Pre-entrega final de curso en Java Backend.
