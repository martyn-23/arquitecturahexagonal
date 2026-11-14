package com.example.demo.application;

import com.example.demo.application.usecase.CrearPedidoUseCase;
import com.example.demo.domain.model.EstadoPedido;
import com.example.demo.domain.model.Pedido;
import com.example.demo.domain.model.Producto;
import com.example.demo.domain.port.PedidoRepositoryPort;
import com.example.demo.domain.port.ProductoRepositoryPort;
import com.example.demo.domain.port.UsuarioRepositoryPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CrearPedidoUseCaseTest {

    @Mock
    private PedidoRepositoryPort pedidoRepositoryPort;
    @Mock
    private ProductoRepositoryPort productoRepositoryPort;
    @Mock
    private UsuarioRepositoryPort usuarioRepositoryPort;

    private CrearPedidoUseCase crearPedidoUseCase;

    @BeforeEach
    void setUp() {
        crearPedidoUseCase = new CrearPedidoUseCase(pedidoRepositoryPort, productoRepositoryPort, usuarioRepositoryPort);
    }

    @Test
    @DisplayName("Debe crear un pedido con ítems y calcular el total correctamente")
    void debeCrearPedidoConItems() {
        Producto producto = Producto.builder()
                .id(1L)
                .nombre("Mouse Pad")
                .precio(new BigDecimal("20.00"))
                .stock(50)
                .build();

        when(productoRepositoryPort.buscarPorId(1L)).thenReturn(Optional.of(producto));
        when(pedidoRepositoryPort.guardar(any(Pedido.class))).thenAnswer(inv -> {
            Pedido p = inv.getArgument(0);
            p.setId(100L);
            return p;
        });

        List<CrearPedidoUseCase.ItemEntrada> items = List.of(
                new CrearPedidoUseCase.ItemEntrada(1L, 3)
        );

        Pedido resultado = crearPedidoUseCase.ejecutar(null, "Brayan Gomez", items);

        assertNotNull(resultado.getId());
        assertEquals(EstadoPedido.CARRITO, resultado.getEstado());
        assertEquals("Brayan Gomez", resultado.getCliente());
        assertEquals(new BigDecimal("60.00"), resultado.getTotal());
        assertEquals(1, resultado.getItems().size());
        verify(pedidoRepositoryPort, times(1)).guardar(any(Pedido.class));
    }
}
