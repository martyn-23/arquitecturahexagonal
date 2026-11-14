package com.example.demo.application.usecase;

import com.example.demo.domain.exception.ReglaDeNegocioException;
import com.example.demo.domain.model.Producto;
import com.example.demo.domain.port.ProductoRepositoryPort;

/**
 * Caso de uso: Crear un producto nuevo en el catálogo.
 */
public class CrearProductoUseCase {

    private final ProductoRepositoryPort productoRepositoryPort;

    public CrearProductoUseCase(ProductoRepositoryPort productoRepositoryPort) {
        this.productoRepositoryPort = productoRepositoryPort;
    }

    public Producto ejecutar(Producto producto) {
        if (producto == null) {
            throw new ReglaDeNegocioException("Los datos del producto no pueden ser nulos");
        }
        producto.validar();
        return productoRepositoryPort.guardar(producto);
    }
}
