package com.example.demo.application.usecase;

import com.example.demo.domain.exception.RecursoNoEncontradoException;
import com.example.demo.domain.model.Pedido;
import com.example.demo.domain.port.PedidoRepositoryPort;

/**
 * Caso de uso: Quitar un ítem del pedido en estado CARRITO.
 */
public class QuitarItemPedidoUseCase {

    private final PedidoRepositoryPort pedidoRepositoryPort;

    public QuitarItemPedidoUseCase(PedidoRepositoryPort pedidoRepositoryPort) {
        this.pedidoRepositoryPort = pedidoRepositoryPort;
    }

    public Pedido ejecutar(Long pedidoId, Long itemId) {
        Pedido pedido = pedidoRepositoryPort.buscarPorId(pedidoId)
                .orElseThrow(() -> new RecursoNoEncontradoException("Pedido no encontrado con ID: " + pedidoId));

        boolean eliminado = pedido.quitarItem(itemId);
        if (!eliminado) {
            throw new RecursoNoEncontradoException("Ítem no encontrado con ID " + itemId + " en el pedido " + pedidoId);
        }

        return pedidoRepositoryPort.guardar(pedido);
    }
}
