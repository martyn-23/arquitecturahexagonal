package com.example.demo.application.usecase;

import com.example.demo.domain.exception.RecursoNoEncontradoException;
import com.example.demo.domain.model.Producto;
import com.example.demo.domain.port.ProductoRepositoryPort;

/**
 * Caso de uso: Actualizar información de un producto existente.
 */
public class ActualizarProductoUseCase {

    private final ProductoRepositoryPort productoRepositoryPort;

    public ActualizarProductoUseCase(ProductoRepositoryPort productoRepositoryPort) {
        this.productoRepositoryPort = productoRepositoryPort;
    }

    public Producto ejecutar(Long id, Producto datosActualizados) {
        Producto existente = productoRepositoryPort.buscarPorId(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Producto no encontrado con ID: " + id));

        datosActualizados.validar();

        existente.setNombre(datosActualizados.getNombre());
        existente.setDescripcion(datosActualizados.getDescripcion());
        existente.setPrecio(datosActualizados.getPrecio());
        existente.setStock(datosActualizados.getStock());

        return productoRepositoryPort.guardar(existente);
    }
}
