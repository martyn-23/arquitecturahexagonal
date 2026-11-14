package com.example.demo.domain.model;

import com.example.demo.domain.exception.ReglaDeNegocioException;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Entidad de dominio pura para Pedido.
 * Encapsula todas las reglas de negocio del ciclo de vida del pedido.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Pedido {

    private Long id;
    private Long usuarioId;
    private String cliente;
    @Builder.Default
    private LocalDateTime fecha = LocalDateTime.now();
    @Builder.Default
    private EstadoPedido estado = EstadoPedido.CARRITO;
    @Builder.Default
    private BigDecimal total = BigDecimal.ZERO;
    @Builder.Default
    private List<DetallePedido> items = new ArrayList<>();

    /**
     * Agrega un detalle o ítem al pedido.
     * Solo es válido cuando el pedido está en estado CARRITO.
     */
    public void agregarItem(DetallePedido item) {
        validarEstadoCarrito();
        if (item == null) {
            throw new ReglaDeNegocioException("El ítem a agregar no puede ser nulo");
        }
        item.calcularSubtotal();
        this.items.add(item);
        recalcularTotal();
    }

    /**
     * Remueve un ítem del pedido por su identificador.
     */
    public boolean quitarItem(Long itemId) {
        validarEstadoCarrito();
        if (itemId == null) {
            throw new ReglaDeNegocioException("El ID del ítem no puede ser nulo");
        }
        boolean removido = this.items.removeIf(item -> item.getId() != null && item.getId().equals(itemId));
        if (removido) {
            recalcularTotal();
        }
        return removido;
    }

    /**
     * Recalcula el monto total del pedido sumando los subtotales de cada ítem.
     */
    public void recalcularTotal() {
        if (this.items == null || this.items.isEmpty()) {
            this.total = BigDecimal.ZERO;
            return;
        }
        this.total = this.items.stream()
                .map(item -> item.getSubtotal() != null ? item.getSubtotal() : BigDecimal.ZERO)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    /**
     * Valida y confirma el pedido.
     * Verifica que esté en estado CARRITO, que contenga ítems y que cada producto cuente con stock suficiente.
     * Luego descuenta el stock de cada producto y cambia el estado a CONFIRMADO.
     */
    public void confirmar() {
        validarEstadoCarrito();
        if (this.items == null || this.items.isEmpty()) {
            throw new ReglaDeNegocioException("No se puede confirmar un pedido con el carrito vacío");
        }

        // Validar y descontar stock en los productos asociados
        for (DetallePedido item : this.items) {
            Producto producto = item.getProducto();
            if (producto == null) {
                throw new ReglaDeNegocioException("El ítem no contiene un producto válido asociado");
            }
            producto.descontarStock(item.getCantidad());
        }

        this.estado = EstadoPedido.CONFIRMADO;
    }

    /**
     * Cancela el pedido y restaura el stock si ya estaba confirmado.
     */
    public void cancelar() {
        if (this.estado == EstadoPedido.CANCELADO) {
            throw new ReglaDeNegocioException("El pedido ya se encuentra cancelado");
        }
        if (this.estado == EstadoPedido.CONFIRMADO && this.items != null) {
            for (DetallePedido item : this.items) {
                if (item.getProducto() != null) {
                    item.getProducto().aumentarStock(item.getCantidad());
                }
            }
        }
        this.estado = EstadoPedido.CANCELADO;
    }

    /**
     * Verifica que las operaciones de modificación solo se realicen en estado CARRITO.
     */
    public void validarEstadoCarrito() {
        if (this.estado != EstadoPedido.CARRITO) {
            throw new ReglaDeNegocioException("Solo se pueden modificar pedidos en estado CARRITO. Estado actual: " + this.estado);
        }
    }
}
