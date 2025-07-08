# Proyecto-Integrador-Back

---

## Descripción de Mi Proyecto

Este proyecto es mi **pre-entrega de un sistema básico de gestión**, desarrollado en **Java**. A través de él, pude aplicar y consolidar mis conocimientos en Programación Orientada a Objetos (POO), el manejo de colecciones, excepciones y la organización del código.

## Estructura de Mi Proyecto

Organicé mi proyecto en las siguientes áreas clave:

* **Java**: Utilicé conceptos fundamentales de Java, como tipos de datos, variables, operadores aritméticos, lógicos y relacionales.
* **Programación Orientada a Objetos (POO)**: Implementé **clases** (`Producto`, `Pedido`), **objetos**, **encapsulamiento**, así como **herencia** y **polimorfismo** para extender funcionalidades.
* **Colecciones**: Usé `ArrayList` para gestionar colecciones de productos y pedidos.
* **Manejo de Excepciones**: Implementé bloques `try/catch` para manejar errores comunes y también creé excepciones personalizadas.
* **Organización de Código**: Estructuré mi código en **paquetes lógicos** para mejorar la modularidad y facilitar el mantenimiento.

## Requisitos Cumplidos

### Estructura y POO
* Desarrollé clases clave como `Producto` (con `id`, `nombre`, `precio`, `stock`, `getters` y `setters`) y `Pedido` (con `id`, una lista de productos/líneas y métodos para calcular el total).
* Mi clase `Main` se encarga de orquestar el menú interactivo y delega la lógica de negocio a servicios específicos.

### Gestión de Productos
* **Ingreso de Productos**: Mi sistema permite agregar productos solicitando su nombre, precio y cantidad en stock.
* **Visualización de Productos**: Muestra una lista detallada de todos los productos registrados, incluyendo su ID, nombre, precio y stock.
* **Búsqueda y Actualización**: Puedo buscar productos por nombre o ID, mostrar su información y actualizar su precio o stock con las validaciones necesarias.
* **Eliminación de Productos**: Mi sistema posibilita la eliminación de productos por ID o posición, con la opción de pedir confirmación.

### Gestión de Pedidos
* **Creación de Pedidos**: Puedo crear pedidos nuevos, seleccionando productos y sus cantidades.
    * **Validación de Stock**: Verifico la disponibilidad de stock antes de confirmar un pedido.
    * **Cálculo de Costo Total**: Calculo el valor total de cada pedido.
    * **Actualización de Stock**: Descuento el stock de los productos una vez que el pedido es confirmado.
* **Listado de Pedidos**: Tengo una funcionalidad para visualizar los pedidos que he realizado, su costo y los productos asociados.

### Interfaz y Usabilidad
* **Menú Principal Interactivo**: Mi programa presenta un menú de opciones claro y se repite hasta que decido salir.

    ```
    =================================== SISTEMA DE GESTIÓN - TECHLAB ==================================
    1) Agregar producto
    2) Listar productos
    3) Buscar/Actualizar producto
    4) Eliminar producto
    5) Crear un pedido
    6) Listar pedidos
    7) Salir

    Elija una opción:
    ```

## Ejemplo de Flujo de Uso (Mi Escenario)

1.  Mi programa inicia y muestra el **menú principal**.
2.  Yo selecciono la opción "1" para **Agregar Producto**, e ingreso el nombre, precio y stock. Se crea un objeto `Producto` y se añade a la lista.
3.  Luego, selecciono la opción "2" para **Listar Productos**, y el sistema me muestra todos los productos con su ID, nombre, precio y stock.
4.  Después, selecciono la opción "5" para **Crear Pedido**. El sistema me pregunta cuántos productos deseo agregar, y me pide el ID del producto y la cantidad.
    * Si no hay suficiente stock, el sistema lanza una `StockInsuficienteException` o me muestra un mensaje adecuado.
    * Si hay stock, se descuenta del inventario y se crea el pedido en la colección de pedidos.
5.  Finalmente, selecciono la opción "7" para **Salir**, y el programa finaliza.

---
