package com.example.demo.application.usecase;

import com.example.demo.domain.exception.RecursoNoEncontradoException;
import com.example.demo.domain.model.Producto;
import com.example.demo.domain.port.ProductoRepositoryPort;

import java.util.List;

/**
 * Caso de uso: Consultar productos registrados.
 */
public class ConsultarProductoUseCase {

    private final ProductoRepositoryPort productoRepositoryPort;

    public ConsultarProductoUseCase(ProductoRepositoryPort productoRepositoryPort) {
        this.productoRepositoryPort = productoRepositoryPort;
    }

    public Producto buscarPorId(Long id) {
        return productoRepositoryPort.buscarPorId(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Producto no encontrado con ID: " + id));
    }

    public List<Producto> listarTodos() {
        return productoRepositoryPort.listarTodos();
    }
}
