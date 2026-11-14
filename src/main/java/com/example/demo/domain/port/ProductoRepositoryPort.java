package com.example.demo.domain.port;

import com.example.demo.domain.model.Producto;

import java.util.List;
import java.util.Optional;

/**
 * Puerto de salida (Driven Port) para la persistencia y recuperación de Productos.
 */
public interface ProductoRepositoryPort {

    Producto guardar(Producto producto);

    Optional<Producto> buscarPorId(Long id);

    List<Producto> listarTodos();

    void eliminarPorId(Long id);

    boolean existePorId(Long id);
}
