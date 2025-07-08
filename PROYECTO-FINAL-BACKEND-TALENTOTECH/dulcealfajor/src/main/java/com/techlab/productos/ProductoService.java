package com.techlab.productos;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.techlab.excepciones.NombreNuloOVacioException;
import com.techlab.excepciones.PrecioInvalidoException;
import com.techlab.excepciones.ProductoNoEncontradoException;
import com.techlab.excepciones.StockInsuficienteException;
import com.techlab.excepciones.StockNegativoException;

import java.util.List;
import java.util.Optional;

@Service
public class ProductoService {

    @Autowired
    private ProductoRepository productoRepository;

    public List<Producto> listar() {
        return productoRepository.findAll();
    }

    public Optional<Producto> buscarPorId(int id) {
        return productoRepository.findById(id);
    }

    public Producto agregar(Producto producto) {
        validarProducto(producto);
        return productoRepository.save(producto);
    }

    public boolean eliminar(Integer id) {
        if (productoRepository.existsById(id)) {
            productoRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public Optional<Producto> actualizar(int id, Producto nuevo) {
        validarProducto(nuevo);
        return productoRepository.findById(id).map(p -> {
            p.setNombre(nuevo.getNombre());
            p.setDescripcion(nuevo.getDescripcion());
            p.setPrecio(nuevo.getPrecio());
            p.setCategoria(nuevo.getCategoria());
            p.setImagenUrl(nuevo.getImagenUrl());
            p.setStock(nuevo.getStock());
            return productoRepository.save(p);
        });
    }

    public void disminuirStock(int idProducto, int cantidad) {
        Producto producto = productoRepository.findById(idProducto)
            .orElseThrow(() -> new ProductoNoEncontradoException("Producto con ID " + idProducto + " no encontrado."));
        if (producto.getStock() >= cantidad) {
            producto.setStock(producto.getStock() - cantidad);
            productoRepository.save(producto);
        } else {
            throw new StockInsuficienteException("No hay suficiente stock para el producto " + producto.getNombre());
        }
    }

    private void validarProducto(Producto producto) {
        if (producto.getNombre() == null || producto.getNombre().isBlank()) {
            throw new NombreNuloOVacioException("El nombre no puede ser nulo o vacío.");
        }
        if (producto.getPrecio() < 0) {
            throw new PrecioInvalidoException("El precio no puede ser negativo.");
        }
        if (producto.getStock() < 0) {
            throw new StockNegativoException("El stock no puede ser negativo.");
        }
    }
}