package com.example.demo.application.usecase;

import com.example.demo.domain.exception.RecursoNoEncontradoException;
import com.example.demo.domain.port.PedidoRepositoryPort;

/**
 * Caso de uso: Eliminar un pedido del sistema.
 */
public class EliminarPedidoUseCase {

    private final PedidoRepositoryPort pedidoRepositoryPort;

    public EliminarPedidoUseCase(PedidoRepositoryPort pedidoRepositoryPort) {
        this.pedidoRepositoryPort = pedidoRepositoryPort;
    }

    public void ejecutar(Long id) {
        if (!pedidoRepositoryPort.existePorId(id)) {
            throw new RecursoNoEncontradoException("Pedido no encontrado con ID: " + id);
        }
        pedidoRepositoryPort.eliminarPorId(id);
    }
}
