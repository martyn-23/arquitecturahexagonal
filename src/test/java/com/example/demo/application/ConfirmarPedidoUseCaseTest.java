package com.example.demo.application;

import com.example.demo.application.usecase.ConfirmarPedidoUseCase;
import com.example.demo.domain.model.DetallePedido;
import com.example.demo.domain.model.EstadoPedido;
import com.example.demo.domain.model.Pedido;
import com.example.demo.domain.model.Producto;
import com.example.demo.domain.port.PedidoRepositoryPort;
import com.example.demo.domain.port.ProductoRepositoryPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ConfirmarPedidoUseCaseTest {

    @Mock
    private PedidoRepositoryPort pedidoRepositoryPort;
    @Mock
    private ProductoRepositoryPort productoRepositoryPort;

    private ConfirmarPedidoUseCase confirmarPedidoUseCase;

    @BeforeEach
    void setUp() {
        confirmarPedidoUseCase = new ConfirmarPedidoUseCase(pedidoRepositoryPort, productoRepositoryPort);
    }

    @Test
    @DisplayName("Debe confirmar el pedido y actualizar el stock del producto a través del puerto")
    void debeConfirmarPedidoYActualizarStockEnPuerto() {
        Producto producto = Producto.builder()
                .id(1L)
                .nombre("Silla Ergonomica")
                .precio(new BigDecimal("250.00"))
                .stock(10)
                .build();

        DetallePedido item = DetallePedido.builder()
                .id(10L)
                .producto(producto)
                .cantidad(2)
                .precioUnitario(producto.getPrecio())
                .subtotal(new BigDecimal("500.00"))
                .build();

        Pedido pedido = Pedido.builder()
                .id(50L)
                .cliente("Juan")
                .estado(EstadoPedido.CARRITO)
                .items(new ArrayList<>(List.of(item)))
                .total(new BigDecimal("500.00"))
                .build();

        when(pedidoRepositoryPort.buscarPorId(50L)).thenReturn(Optional.of(pedido));
        when(pedidoRepositoryPort.guardar(any(Pedido.class))).thenAnswer(inv -> inv.getArgument(0));

        Pedido pedidoConfirmado = confirmarPedidoUseCase.ejecutar(50L);

        assertEquals(EstadoPedido.CONFIRMADO, pedidoConfirmado.getEstado());
        assertEquals(8, producto.getStock());
        verify(productoRepositoryPort, times(1)).guardar(producto);
        verify(pedidoRepositoryPort, times(1)).guardar(pedido);
    }
}
