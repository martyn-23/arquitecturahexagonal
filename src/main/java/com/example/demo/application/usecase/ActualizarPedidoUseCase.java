package com.example.demo.application.usecase;

import com.example.demo.domain.exception.RecursoNoEncontradoException;
import com.example.demo.domain.exception.ReglaDeNegocioException;
import com.example.demo.domain.model.DetallePedido;
import com.example.demo.domain.model.Pedido;
import com.example.demo.domain.model.Producto;
import com.example.demo.domain.port.PedidoRepositoryPort;
import com.example.demo.domain.port.ProductoRepositoryPort;

import java.util.ArrayList;
import java.util.List;

/**
 * Caso de uso: Actualizar información general y composición de ítems de un pedido en estado CARRITO.
 */
public class ActualizarPedidoUseCase {

    private final PedidoRepositoryPort pedidoRepositoryPort;
    private final ProductoRepositoryPort productoRepositoryPort;

    public ActualizarPedidoUseCase(PedidoRepositoryPort pedidoRepositoryPort,
                                   ProductoRepositoryPort productoRepositoryPort) {
        this.pedidoRepositoryPort = pedidoRepositoryPort;
        this.productoRepositoryPort = productoRepositoryPort;
    }

    public record ItemEntrada(Long productoId, Integer cantidad) {}

    public Pedido ejecutar(Long id, String cliente, List<ItemEntrada> items) {
        Pedido pedido = pedidoRepositoryPort.buscarPorId(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Pedido no encontrado con ID: " + id));

        pedido.validarEstadoCarrito();

        if (cliente != null && !cliente.trim().isEmpty()) {
            pedido.setCliente(cliente.trim());
        }

        if (items != null) {
            pedido.setItems(new ArrayList<>());
            for (ItemEntrada itemEntrada : items) {
                if (itemEntrada.cantidad() == null || itemEntrada.cantidad() <= 0) {
                    throw new ReglaDeNegocioException("La cantidad debe ser mayor que cero");
                }
                Producto producto = productoRepositoryPort.buscarPorId(itemEntrada.productoId())
                        .orElseThrow(() -> new RecursoNoEncontradoException("Producto no encontrado con ID: " + itemEntrada.productoId()));

                DetallePedido detalle = DetallePedido.builder()
                        .producto(producto)
                        .cantidad(itemEntrada.cantidad())
                        .precioUnitario(producto.getPrecio())
                        .build();

                pedido.agregarItem(detalle);
            }
        }

        pedido.recalcularTotal();
        return pedidoRepositoryPort.guardar(pedido);
    }
}
