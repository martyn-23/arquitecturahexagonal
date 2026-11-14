package com.example.demo.application.usecase;

import com.example.demo.domain.exception.RecursoNoEncontradoException;
import com.example.demo.domain.port.ProductoRepositoryPort;

/**
 * Caso de uso: Eliminar un producto del catálogo.
 */
public class EliminarProductoUseCase {

    private final ProductoRepositoryPort productoRepositoryPort;

    public EliminarProductoUseCase(ProductoRepositoryPort productoRepositoryPort) {
        this.productoRepositoryPort = productoRepositoryPort;
    }

    public void ejecutar(Long id) {
        if (!productoRepositoryPort.existePorId(id)) {
            throw new RecursoNoEncontradoException("Producto no encontrado con ID: " + id);
        }
        productoRepositoryPort.eliminarPorId(id);
    }
}
