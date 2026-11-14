package com.example.demo.application.usecase;

import com.example.demo.domain.exception.RecursoNoEncontradoException;
import com.example.demo.domain.exception.ReglaDeNegocioException;
import com.example.demo.domain.model.DetallePedido;
import com.example.demo.domain.model.EstadoPedido;
import com.example.demo.domain.model.Pedido;
import com.example.demo.domain.model.Producto;
import com.example.demo.domain.model.Usuario;
import com.example.demo.domain.port.PedidoRepositoryPort;
import com.example.demo.domain.port.ProductoRepositoryPort;
import com.example.demo.domain.port.UsuarioRepositoryPort;

import java.util.ArrayList;
import java.util.List;

/**
 * Caso de uso: Crear un nuevo pedido en estado CARRITO.
 * Puede inicializarse vacío o con ítems iniciales, validando los productos en el catálogo.
 */
public class CrearPedidoUseCase {

    private final PedidoRepositoryPort pedidoRepositoryPort;
    private final ProductoRepositoryPort productoRepositoryPort;
    private final UsuarioRepositoryPort usuarioRepositoryPort;

    public CrearPedidoUseCase(PedidoRepositoryPort pedidoRepositoryPort,
                              ProductoRepositoryPort productoRepositoryPort,
                              UsuarioRepositoryPort usuarioRepositoryPort) {
        this.pedidoRepositoryPort = pedidoRepositoryPort;
        this.productoRepositoryPort = productoRepositoryPort;
        this.usuarioRepositoryPort = usuarioRepositoryPort;
    }

    public record ItemEntrada(Long productoId, Integer cantidad) {}

    public Pedido ejecutar(Long usuarioId, String cliente, List<ItemEntrada> items) {
        String nombreCliente = cliente;

        // Si se provee un usuarioId, verificar existencia y asignar nombre si no se envió explícito
        if (usuarioId != null) {
            Usuario usuario = usuarioRepositoryPort.buscarPorId(usuarioId)
                    .orElseThrow(() -> new RecursoNoEncontradoException("Usuario no encontrado con ID: " + usuarioId));
            if (nombreCliente == null || nombreCliente.trim().isEmpty()) {
                nombreCliente = usuario.getNombre();
            }
        }

        if (nombreCliente == null || nombreCliente.trim().isEmpty()) {
            throw new ReglaDeNegocioException("El cliente o usuarioId es obligatorio para registrar el pedido");
        }

        Pedido pedido = Pedido.builder()
                .usuarioId(usuarioId)
                .cliente(nombreCliente.trim())
                .estado(EstadoPedido.CARRITO)
                .items(new ArrayList<>())
                .build();

        if (items != null) {
            for (ItemEntrada itemEntrada : items) {
                if (itemEntrada.cantidad() == null || itemEntrada.cantidad() <= 0) {
                    throw new ReglaDeNegocioException("La cantidad debe ser mayor que cero");
                }
                Producto producto = productoRepositoryPort.buscarPorId(itemEntrada.productoId())
                        .orElseThrow(() -> new RecursoNoEncontradoException("Producto no encontrado con ID: " + itemEntrada.productoId()));

                DetallePedido detalle = DetallePedido.builder()
                        .producto(producto)
                        .cantidad(itemEntrada.cantidad())
                        .precioUnitario(producto.getPrecio())
                        .build();

                pedido.agregarItem(detalle);
            }
        }

        pedido.recalcularTotal();
        return pedidoRepositoryPort.guardar(pedido);
    }
}
