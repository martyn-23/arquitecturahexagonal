package com.example.demo.application.usecase;

import com.example.demo.domain.exception.RecursoNoEncontradoException;
import com.example.demo.domain.model.Pedido;
import com.example.demo.domain.port.PedidoRepositoryPort;

import java.util.List;

/**
 * Caso de uso: Consultar pedidos existentes.
 */
public class ConsultarPedidoUseCase {

    private final PedidoRepositoryPort pedidoRepositoryPort;

    public ConsultarPedidoUseCase(PedidoRepositoryPort pedidoRepositoryPort) {
        this.pedidoRepositoryPort = pedidoRepositoryPort;
    }

    public Pedido buscarPorId(Long id) {
        return pedidoRepositoryPort.buscarPorId(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Pedido no encontrado con ID: " + id));
    }

    public List<Pedido> listarTodos() {
        return pedidoRepositoryPort.listarTodos();
    }

    public List<Pedido> listarPorUsuarioId(Long usuarioId) {
        return pedidoRepositoryPort.buscarPorUsuarioId(usuarioId);
    }
}
