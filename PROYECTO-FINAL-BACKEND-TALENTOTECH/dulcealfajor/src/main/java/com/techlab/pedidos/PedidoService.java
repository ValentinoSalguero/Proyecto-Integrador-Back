package com.techlab.pedidos;

import org.springframework.stereotype.Service;

import com.techlab.excepciones.ProductoNoEncontradoException;
import com.techlab.excepciones.StockInsuficienteException;
import com.techlab.productos.Producto;
import com.techlab.productos.ProductoService;

import jakarta.transaction.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class PedidoService {
    private final PedidoRepository pedidoRepository;
    private final ProductoService productoService;

    public PedidoService(PedidoRepository pedidoRepository, ProductoService productoService) {
        this.pedidoRepository = pedidoRepository;
        this.productoService = productoService;
    }

    @Transactional
    public Pedido crearPedido(int usuarioId, List<LineaPedido> lineasSolicitadas) {
        Pedido pedido = new Pedido(usuarioId);
        pedido.setFechaPedido(LocalDateTime.now());
        pedido.setEstado(EstadoPedido.PENDIENTE);

        for (LineaPedido lp : lineasSolicitadas) {
            Producto producto = productoService.buscarPorId(lp.getProducto().getId())
                    .orElseThrow(() -> new ProductoNoEncontradoException("Producto con ID " + lp.getProducto().getId() + " no encontrado"));
            
            if (producto.getStock() < lp.getCantidad()) {
                throw new StockInsuficienteException("No hay suficiente stock de " + producto.getNombre());
            }

            lp.setPedido(pedido);
            pedido.agregarProducto(producto, lp.getCantidad());
        }

        return pedidoRepository.save(pedido);
    }

    @Transactional
    public void confirmarPedido(int pedidoId) {
        Pedido pedido = pedidoRepository.findById(pedidoId)
                .orElseThrow(() -> new ProductoNoEncontradoException("Pedido no encontrado"));

        if (pedido.getEstado() != EstadoPedido.PENDIENTE) {
            throw new IllegalStateException("Solo se pueden confirmar pedidos en estado PENDIENTE");
        }

        for (LineaPedido lp : pedido.getLineas()) {
            productoService.disminuirStock(lp.getProducto().getId(), lp.getCantidad());
        }

        pedido.cambiarEstado(EstadoPedido.CONFIRMADO);
        pedidoRepository.save(pedido);
    }

    public Optional<Pedido> buscarPorId(int id) {
        return pedidoRepository.findById(id);
    }

    public List<Pedido> listarPedidosPorUsuario(int usuarioId) {
        return pedidoRepository.findByUsuarioId(usuarioId);
    }

    @Transactional
    public void cambiarEstadoPedido(int pedidoId, EstadoPedido nuevoEstado) {
        Pedido pedido = pedidoRepository.findById(pedidoId)
                .orElseThrow(() -> new ProductoNoEncontradoException("Pedido no encontrado")); // Podrías crear una PedidoNoEncontradoException
        pedido.cambiarEstado(nuevoEstado);
        pedidoRepository.save(pedido);
    }

    public List<Pedido> listarTodosLosPedidos() {
        return pedidoRepository.findAll();
    }

    @Transactional
    public boolean eliminarPedido(Integer id) {
        if (pedidoRepository.existsById(id)) {
            pedidoRepository.deleteById(id);
            return true;
        }
        return false;
    }
}