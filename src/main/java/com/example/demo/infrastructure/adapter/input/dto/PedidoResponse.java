package com.example.demo.infrastructure.adapter.input.dto;

import com.example.demo.domain.model.EstadoPedido;
import com.example.demo.domain.model.Pedido;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PedidoResponse {
    private Long id;
    private Long usuarioId;
    private String cliente;
    private LocalDateTime fecha;
    private EstadoPedido estado;
    private BigDecimal total;
    private List<DetallePedidoResponse> items;

    public static PedidoResponse fromDomain(Pedido pedido) {
        if (pedido == null) {
            return null;
        }
        List<DetallePedidoResponse> itemsResponse = pedido.getItems() != null
                ? pedido.getItems().stream().map(DetallePedidoResponse::fromDomain).collect(Collectors.toList())
                : Collections.emptyList();

        return PedidoResponse.builder()
                .id(pedido.getId())
                .usuarioId(pedido.getUsuarioId())
                .cliente(pedido.getCliente())
                .fecha(pedido.getFecha())
                .estado(pedido.getEstado())
                .total(pedido.getTotal())
                .items(itemsResponse)
                .build();
    }
}
