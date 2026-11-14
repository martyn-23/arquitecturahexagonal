package com.example.demo.infrastructure.adapter.output;

import com.example.demo.domain.model.Pedido;
import com.example.demo.domain.port.PedidoRepositoryPort;
import com.example.demo.infrastructure.persistence.PedidoJpaRepository;
import com.example.demo.infrastructure.persistence.entity.PedidoEntity;
import com.example.demo.infrastructure.persistence.mapper.PedidoEntityMapper;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Adaptador de salida (Driven Adapter) para la persistencia de Pedidos.
 */
@Component
public class PedidoRepositoryAdapter implements PedidoRepositoryPort {

    private final PedidoJpaRepository pedidoJpaRepository;
    private final PedidoEntityMapper pedidoEntityMapper;

    public PedidoRepositoryAdapter(PedidoJpaRepository pedidoJpaRepository,
                                  PedidoEntityMapper pedidoEntityMapper) {
        this.pedidoJpaRepository = pedidoJpaRepository;
        this.pedidoEntityMapper = pedidoEntityMapper;
    }

    @Override
    @Transactional
    public Pedido guardar(Pedido pedido) {
        PedidoEntity entity = pedidoEntityMapper.toEntity(pedido);
        PedidoEntity guardado = pedidoJpaRepository.save(entity);
        return pedidoEntityMapper.toDomain(guardado);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Pedido> buscarPorId(Long id) {
        return pedidoJpaRepository.findById(id)
                .map(pedidoEntityMapper::toDomain);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Pedido> listarTodos() {
        return pedidoJpaRepository.findAll().stream()
                .map(pedidoEntityMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<Pedido> buscarPorUsuarioId(Long usuarioId) {
        return pedidoJpaRepository.findByUsuarioId(usuarioId).stream()
                .map(pedidoEntityMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void eliminarPorId(Long id) {
        pedidoJpaRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existePorId(Long id) {
        return pedidoJpaRepository.existsById(id);
    }
}
