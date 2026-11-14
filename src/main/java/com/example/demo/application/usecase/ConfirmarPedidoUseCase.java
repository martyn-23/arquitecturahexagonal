package com.example.demo.application.usecase;

import com.example.demo.domain.exception.RecursoNoEncontradoException;
import com.example.demo.domain.model.DetallePedido;
import com.example.demo.domain.model.Pedido;
import com.example.demo.domain.port.PedidoRepositoryPort;
import com.example.demo.domain.port.ProductoRepositoryPort;

/**
 * Caso de uso: Confirmar un pedido en estado CARRITO.
 * Aplica las reglas del dominio de validación de existencias, descuento de inventario
 * y cambio de estado a CONFIRMADO.
 */
public class ConfirmarPedidoUseCase {

    private final PedidoRepositoryPort pedidoRepositoryPort;
    private final ProductoRepositoryPort productoRepositoryPort;

    public ConfirmarPedidoUseCase(PedidoRepositoryPort pedidoRepositoryPort,
                                  ProductoRepositoryPort productoRepositoryPort) {
        this.pedidoRepositoryPort = pedidoRepositoryPort;
        this.productoRepositoryPort = productoRepositoryPort;
    }

    public Pedido ejecutar(Long id) {
        Pedido pedido = pedidoRepositoryPort.buscarPorId(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Pedido no encontrado con ID: " + id));

        // El modelo de dominio valida el estado, ítems y descuenta el stock de los productos asociados
        pedido.confirmar();

        // Persistir el nuevo stock de cada producto involucrado
        for (DetallePedido item : pedido.getItems()) {
            productoRepositoryPort.guardar(item.getProducto());
        }

        // Persistir el pedido con su nuevo estado CONFIRMADO
        return pedidoRepositoryPort.guardar(pedido);
    }
}
