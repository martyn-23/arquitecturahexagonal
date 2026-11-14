package com.example.demo.domain.model;

import com.example.demo.domain.exception.ReglaDeNegocioException;
import lombok.*;

import java.math.BigDecimal;

/**
 * Entidad de dominio pura para Producto.
 * No contiene dependencias de JPA ni de frameworks externos.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Producto {

    private Long id;
    private String nombre;
    private String descripcion;
    private BigDecimal precio;
    private Integer stock;

    /**
     * Valida la consistencia de los datos del producto según las reglas de negocio.
     */
    public void validar() {
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new ReglaDeNegocioException("El nombre del producto no puede estar vacío");
        }
        if (precio == null || precio.compareTo(BigDecimal.ZERO) <= 0) {
            throw new ReglaDeNegocioException("El precio del producto debe ser mayor a cero");
        }
        if (stock == null || stock < 0) {
            throw new ReglaDeNegocioException("El stock no puede ser negativo");
        }
    }

    /**
     * Comprueba si hay suficiente stock disponible.
     */
    public boolean tieneStockSuficiente(int cantidad) {
        return this.stock != null && this.stock >= cantidad;
    }

    /**
     * Descuenta stock del producto aplicando regla de negocio.
     */
    public void descontarStock(int cantidad) {
        if (cantidad <= 0) {
            throw new ReglaDeNegocioException("La cantidad a descontar debe ser mayor a cero");
        }
        if (!tieneStockSuficiente(cantidad)) {
            throw new ReglaDeNegocioException("Stock insuficiente para el producto: " + this.nombre +
                    ". Solicitado: " + cantidad + ", Disponible: " + this.stock);
        }
        this.stock -= cantidad;
    }

    /**
     * Incrementa stock del producto.
     */
    public void aumentarStock(int cantidad) {
        if (cantidad <= 0) {
            throw new ReglaDeNegocioException("La cantidad a ingresar debe ser mayor a cero");
        }
        this.stock += cantidad;
    }
}
