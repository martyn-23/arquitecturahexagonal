package com.example.demo.application.usecase;

import com.example.demo.domain.exception.RecursoNoEncontradoException;
import com.example.demo.domain.exception.ReglaDeNegocioException;
import com.example.demo.domain.model.DetallePedido;
import com.example.demo.domain.model.Pedido;
import com.example.demo.domain.model.Producto;
import com.example.demo.domain.port.PedidoRepositoryPort;
import com.example.demo.domain.port.ProductoRepositoryPort;

/**
 * Caso de uso: Agregar un ítem o producto al carrito de un pedido.
 */
public class AgregarItemPedidoUseCase {

    private final PedidoRepositoryPort pedidoRepositoryPort;
    private final ProductoRepositoryPort productoRepositoryPort;

    public AgregarItemPedidoUseCase(PedidoRepositoryPort pedidoRepositoryPort,
                                    ProductoRepositoryPort productoRepositoryPort) {
        this.pedidoRepositoryPort = pedidoRepositoryPort;
        this.productoRepositoryPort = productoRepositoryPort;
    }

    public Pedido ejecutar(Long pedidoId, Long productoId, Integer cantidad) {
        if (cantidad == null || cantidad <= 0) {
            throw new ReglaDeNegocioException("La cantidad debe ser mayor que cero");
        }

        Pedido pedido = pedidoRepositoryPort.buscarPorId(pedidoId)
                .orElseThrow(() -> new RecursoNoEncontradoException("Pedido no encontrado con ID: " + pedidoId));

        Producto producto = productoRepositoryPort.buscarPorId(productoId)
                .orElseThrow(() -> new RecursoNoEncontradoException("Producto no encontrado con ID: " + productoId));

        DetallePedido detalle = DetallePedido.builder()
                .producto(producto)
                .cantidad(cantidad)
                .precioUnitario(producto.getPrecio())
                .build();

        pedido.agregarItem(detalle);
        return pedidoRepositoryPort.guardar(pedido);
    }
}
