package com.example.demo.domain;

import com.example.demo.domain.exception.ReglaDeNegocioException;
import com.example.demo.domain.model.DetallePedido;
import com.example.demo.domain.model.EstadoPedido;
import com.example.demo.domain.model.Pedido;
import com.example.demo.domain.model.Producto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas unitarias de Dominio Puro.
 * Demuestran que la lógica de negocio opera independientemente
 * de cualquier framework, base de datos o controlador.
 */
class PedidoTest {

    @Test
    @DisplayName("Debe calcular subtotal y total correctamente al agregar ítems al carrito")
    void debeCalcularTotalCorrectamente() {
        Producto teclado = Producto.builder()
                .id(1L)
                .nombre("Teclado Mecanico")
                .precio(new BigDecimal("100.00"))
                .stock(10)
                .build();

        Producto mouse = Producto.builder()
                .id(2L)
                .nombre("Mouse Gamer")
                .precio(new BigDecimal("50.00"))
                .stock(5)
                .build();

        Pedido pedido = Pedido.builder()
                .cliente("Brayan")
                .estado(EstadoPedido.CARRITO)
                .items(new ArrayList<>())
                .build();

        DetallePedido item1 = DetallePedido.builder()
                .producto(teclado)
                .cantidad(2)
                .precioUnitario(teclado.getPrecio())
                .build();

        DetallePedido item2 = DetallePedido.builder()
                .producto(mouse)
                .cantidad(1)
                .precioUnitario(mouse.getPrecio())
                .build();

        pedido.agregarItem(item1);
        pedido.agregarItem(item2);

        assertEquals(new BigDecimal("200.00"), item1.getSubtotal());
        assertEquals(new BigDecimal("50.00"), item2.getSubtotal());
        assertEquals(new BigDecimal("250.00"), pedido.getTotal());
        assertEquals(2, pedido.getItems().size());
    }

    @Test
    @DisplayName("Debe confirmar pedido y descontar stock de los productos")
    void debeConfirmarPedidoYDescontarStock() {
        Producto producto = Producto.builder()
                .id(1L)
                .nombre("Monitor 4K")
                .precio(new BigDecimal("300.00"))
                .stock(10)
                .build();

        Pedido pedido = Pedido.builder()
                .cliente("Carlos")
                .estado(EstadoPedido.CARRITO)
                .items(new ArrayList<>())
                .build();

        DetallePedido item = DetallePedido.builder()
                .producto(producto)
                .cantidad(3)
                .precioUnitario(producto.getPrecio())
                .build();

        pedido.agregarItem(item);
        pedido.confirmar();

        assertEquals(EstadoPedido.CONFIRMADO, pedido.getEstado());
        assertEquals(7, producto.getStock(), "El stock debe disminuir en 3 unidades");
    }

    @Test
    @DisplayName("Debe lanzar excepción al intentar confirmar un carrito vacío")
    void debeFallarAlConfirmarCarritoVacio() {
        Pedido pedido = Pedido.builder()
                .cliente("Carlos")
                .estado(EstadoPedido.CARRITO)
                .items(new ArrayList<>())
                .build();

        assertThrows(ReglaDeNegocioException.class, pedido::confirmar);
    }

    @Test
    @DisplayName("Debe lanzar excepción al confirmar si no hay stock suficiente")
    void debeFallarAlConfirmarSinStockSuficiente() {
        Producto producto = Producto.builder()
                .id(1L)
                .nombre("Laptop")
                .precio(new BigDecimal("1200.00"))
                .stock(2)
                .build();

        Pedido pedido = Pedido.builder()
                .cliente("Ana")
                .estado(EstadoPedido.CARRITO)
                .items(new ArrayList<>())
                .build();

        DetallePedido item = DetallePedido.builder()
                .producto(producto)
                .cantidad(5)
                .precioUnitario(producto.getPrecio())
                .build();

        pedido.agregarItem(item);

        assertThrows(ReglaDeNegocioException.class, pedido::confirmar);
        assertEquals(2, producto.getStock(), "El stock no debe modificarse si la confirmación falla");
    }

    @Test
    @DisplayName("Debe restaurar stock al cancelar un pedido previamente confirmado")
    void debeRestaurarStockAlCancelarPedidoConfirmado() {
        Producto producto = Producto.builder()
                .id(1L)
                .nombre("Auriculares")
                .precio(new BigDecimal("80.00"))
                .stock(10)
                .build();

        Pedido pedido = Pedido.builder()
                .cliente("David")
                .estado(EstadoPedido.CARRITO)
                .items(new ArrayList<>())
                .build();

        DetallePedido item = DetallePedido.builder()
                .producto(producto)
                .cantidad(4)
                .precioUnitario(producto.getPrecio())
                .build();

        pedido.agregarItem(item);
        pedido.confirmar();
        assertEquals(6, producto.getStock());

        pedido.cancelar();
        assertEquals(EstadoPedido.CANCELADO, pedido.getEstado());
        assertEquals(10, producto.getStock(), "El stock debe restaurarse al cancelar");
    }
}
