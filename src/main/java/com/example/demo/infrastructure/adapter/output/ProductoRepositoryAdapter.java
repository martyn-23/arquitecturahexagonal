package com.example.demo.infrastructure.adapter.output;

import com.example.demo.domain.model.Producto;
import com.example.demo.domain.port.ProductoRepositoryPort;
import com.example.demo.infrastructure.persistence.ProductoJpaRepository;
import com.example.demo.infrastructure.persistence.entity.ProductoEntity;
import com.example.demo.infrastructure.persistence.mapper.ProductoEntityMapper;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Adaptador de salida (Driven Adapter) para la persistencia de Productos.
 */
@Component
public class ProductoRepositoryAdapter implements ProductoRepositoryPort {

    private final ProductoJpaRepository productoJpaRepository;
    private final ProductoEntityMapper productoEntityMapper;

    public ProductoRepositoryAdapter(ProductoJpaRepository productoJpaRepository,
                                   ProductoEntityMapper productoEntityMapper) {
        this.productoJpaRepository = productoJpaRepository;
        this.productoEntityMapper = productoEntityMapper;
    }

    @Override
    @Transactional
    public Producto guardar(Producto producto) {
        ProductoEntity entity = productoEntityMapper.toEntity(producto);
        ProductoEntity guardada = productoJpaRepository.save(entity);
        return productoEntityMapper.toDomain(guardada);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Producto> buscarPorId(Long id) {
        return productoJpaRepository.findById(id)
                .map(productoEntityMapper::toDomain);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Producto> listarTodos() {
        return productoJpaRepository.findAll().stream()
                .map(productoEntityMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void eliminarPorId(Long id) {
        productoJpaRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existePorId(Long id) {
        return productoJpaRepository.existsById(id);
    }
}
