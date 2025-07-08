package com.techlab.pedidos;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import com.techlab.excepciones.ProductoNoEncontradoException;
import com.techlab.excepciones.StockInsuficienteException;

@RestController
@RequestMapping("/api/pedidos")
@CrossOrigin(origins = "*")
public class PedidoController {

    private final PedidoService pedidoService;

    @Autowired
    public PedidoController(PedidoService pedidoService) {
        this.pedidoService = pedidoService;
    }

    @PostMapping
    public ResponseEntity<?> crearPedido(@RequestBody Pedido pedido) {
        try {
            Pedido nuevoPedido = pedidoService.crearPedido(pedido.getUsuarioId(), pedido.getLineas());
            return new ResponseEntity<>(nuevoPedido, HttpStatus.CREATED);
        } catch (ProductoNoEncontradoException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (StockInsuficienteException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al crear el pedido: " + e.getMessage());
        }
    }

    @GetMapping
    public List<Pedido> listarTodosLosPedidos() {
        return pedidoService.listarTodosLosPedidos();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Pedido> obtenerPedidoPorId(@PathVariable Integer id) {
        return pedidoService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/usuario/{usuarioId}")
    public List<Pedido> listarPedidosPorUsuario(@PathVariable Integer usuarioId) {
        return pedidoService.listarPedidosPorUsuario(usuarioId);
    }

    @PostMapping("/{id}/confirmar")
    public ResponseEntity<?> confirmarPedido(@PathVariable Integer id) {
        try {
            pedidoService.confirmarPedido(id);
            return ResponseEntity.ok("Pedido " + id + " confirmado exitosamente.");
        } catch (ProductoNoEncontradoException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (StockInsuficienteException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al confirmar el pedido: " + e.getMessage());
        }
    }

    @PutMapping("/{id}/estado")
    public ResponseEntity<?> cambiarEstadoPedido(@PathVariable Integer id, @RequestBody Map<String, String> payload) {
        try {
            String estadoString = payload.get("nuevoEstado");
            if (estadoString == null) {
                return ResponseEntity.badRequest().body("El cuerpo de la solicitud debe contener 'nuevoEstado'.");
            }
            EstadoPedido nuevoEstado = EstadoPedido.valueOf(estadoString.toUpperCase());
            pedidoService.cambiarEstadoPedido(id, nuevoEstado);
            return ResponseEntity.ok("Estado del pedido " + id + " cambiado a " + nuevoEstado.name());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Estado de pedido inválido: " + e.getMessage());
        } catch (ProductoNoEncontradoException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al cambiar el estado del pedido: " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarPedido(@PathVariable Integer id) {
        if (pedidoService.eliminarPedido(id)) {
            return ResponseEntity.noContent().build(); // 204 No Content
        }
        return ResponseEntity.notFound().build(); // 404 Not Found
    }
}