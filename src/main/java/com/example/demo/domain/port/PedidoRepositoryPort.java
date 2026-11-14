package com.example.demo.domain.port;

import com.example.demo.domain.model.Pedido;

import java.util.List;
import java.util.Optional;

/**
 * Puerto de salida (Driven Port) para la persistencia y recuperación de Pedidos y sus ítems.
 */
public interface PedidoRepositoryPort {

    Pedido guardar(Pedido pedido);

    Optional<Pedido> buscarPorId(Long id);

    List<Pedido> listarTodos();

    List<Pedido> buscarPorUsuarioId(Long usuarioId);

    void eliminarPorId(Long id);

    boolean existePorId(Long id);
}
