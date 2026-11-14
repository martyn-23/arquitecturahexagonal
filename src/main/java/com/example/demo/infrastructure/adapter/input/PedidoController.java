package com.example.demo.infrastructure.adapter.input;

import com.example.demo.application.usecase.*;
import com.example.demo.domain.model.Pedido;
import com.example.demo.infrastructure.adapter.input.dto.ItemRequest;
import com.example.demo.infrastructure.adapter.input.dto.PedidoRequest;
import com.example.demo.infrastructure.adapter.input.dto.PedidoResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Adaptador de entrada (Driving Adapter) REST para la gestión de Pedidos.
 */
@RestController
@RequestMapping("/api/pedidos")
public class PedidoController {

    private final CrearPedidoUseCase crearPedidoUseCase;
    private final ConsultarPedidoUseCase consultarPedidoUseCase;
    private final ActualizarPedidoUseCase actualizarPedidoUseCase;
    private final AgregarItemPedidoUseCase agregarItemPedidoUseCase;
    private final QuitarItemPedidoUseCase quitarItemPedidoUseCase;
    private final ConfirmarPedidoUseCase confirmarPedidoUseCase;
    private final EliminarPedidoUseCase eliminarPedidoUseCase;

    public PedidoController(CrearPedidoUseCase crearPedidoUseCase,
                            ConsultarPedidoUseCase consultarPedidoUseCase,
                            ActualizarPedidoUseCase actualizarPedidoUseCase,
                            AgregarItemPedidoUseCase agregarItemPedidoUseCase,
                            QuitarItemPedidoUseCase quitarItemPedidoUseCase,
                            ConfirmarPedidoUseCase confirmarPedidoUseCase,
                            EliminarPedidoUseCase eliminarPedidoUseCase) {
        this.crearPedidoUseCase = crearPedidoUseCase;
        this.consultarPedidoUseCase = consultarPedidoUseCase;
        this.actualizarPedidoUseCase = actualizarPedidoUseCase;
        this.agregarItemPedidoUseCase = agregarItemPedidoUseCase;
        this.quitarItemPedidoUseCase = quitarItemPedidoUseCase;
        this.confirmarPedidoUseCase = confirmarPedidoUseCase;
        this.eliminarPedidoUseCase = eliminarPedidoUseCase;
    }

    @GetMapping
    public ResponseEntity<List<PedidoResponse>> listar() {
        List<PedidoResponse> pedidos = consultarPedidoUseCase.listarTodos().stream()
                .map(PedidoResponse::fromDomain)
                .collect(Collectors.toList());
        return ResponseEntity.ok(pedidos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PedidoResponse> obtenerPorId(@PathVariable Long id) {
        Pedido pedido = consultarPedidoUseCase.buscarPorId(id);
        return ResponseEntity.ok(PedidoResponse.fromDomain(pedido));
    }

    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<PedidoResponse>> listarPorUsuario(@PathVariable Long usuarioId) {
        List<PedidoResponse> pedidos = consultarPedidoUseCase.listarPorUsuarioId(usuarioId).stream()
                .map(PedidoResponse::fromDomain)
                .collect(Collectors.toList());
        return ResponseEntity.ok(pedidos);
    }

    @PostMapping
    public ResponseEntity<PedidoResponse> crear(@RequestBody PedidoRequest request) {
        List<CrearPedidoUseCase.ItemEntrada> items = new ArrayList<>();
        if (request.getItems() != null) {
            for (ItemRequest item : request.getItems()) {
                items.add(new CrearPedidoUseCase.ItemEntrada(item.getProductoId(), item.getCantidad()));
            }
        }
        Pedido pedidoCreado = crearPedidoUseCase.ejecutar(request.getUsuarioId(), request.getCliente(), items);
        return ResponseEntity.status(HttpStatus.CREATED).body(PedidoResponse.fromDomain(pedidoCreado));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PedidoResponse> actualizar(@PathVariable Long id, @RequestBody PedidoRequest request) {
        List<ActualizarPedidoUseCase.ItemEntrada> items = new ArrayList<>();
        if (request.getItems() != null) {
            for (ItemRequest item : request.getItems()) {
                items.add(new ActualizarPedidoUseCase.ItemEntrada(item.getProductoId(), item.getCantidad()));
            }
        }
        Pedido pedidoActualizado = actualizarPedidoUseCase.ejecutar(id, request.getCliente(), items);
        return ResponseEntity.ok(PedidoResponse.fromDomain(pedidoActualizado));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        eliminarPedidoUseCase.ejecutar(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/items")
    public ResponseEntity<PedidoResponse> agregarItem(@PathVariable Long id, @RequestBody ItemRequest request) {
        Pedido pedido = agregarItemPedidoUseCase.ejecutar(id, request.getProductoId(), request.getCantidad());
        return ResponseEntity.ok(PedidoResponse.fromDomain(pedido));
    }

    @DeleteMapping("/{id}/items/{itemId}")
    public ResponseEntity<PedidoResponse> quitarItem(@PathVariable Long id, @PathVariable Long itemId) {
        Pedido pedido = quitarItemPedidoUseCase.ejecutar(id, itemId);
        return ResponseEntity.ok(PedidoResponse.fromDomain(pedido));
    }

    @PatchMapping("/{id}/confirmar")
    public ResponseEntity<PedidoResponse> confirmar(@PathVariable Long id) {
        Pedido pedido = confirmarPedidoUseCase.ejecutar(id);
        return ResponseEntity.ok(PedidoResponse.fromDomain(pedido));
    }
}
