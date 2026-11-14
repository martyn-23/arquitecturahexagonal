package com.example.demo.domain.model;

import com.example.demo.domain.exception.ReglaDeNegocioException;
import lombok.*;

import java.math.BigDecimal;

/**
 * Entidad de dominio pura para un Item o Detalle de Pedido.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DetallePedido {

    private Long id;
    private Long pedidoId;
    private Producto producto;
    private Integer cantidad;
    private BigDecimal precioUnitario;
    private BigDecimal subtotal;

    /**
     * Valida y calcula el subtotal del ítem según cantidad y precio unitario.
     */
    public void calcularSubtotal() {
        if (cantidad == null || cantidad <= 0) {
            throw new ReglaDeNegocioException("La cantidad del ítem debe ser mayor a cero");
        }
        if (precioUnitario == null || precioUnitario.compareTo(BigDecimal.ZERO) <= 0) {
            throw new ReglaDeNegocioException("El precio unitario debe ser mayor a cero");
        }
        this.subtotal = this.precioUnitario.multiply(BigDecimal.valueOf(this.cantidad));
    }
}
