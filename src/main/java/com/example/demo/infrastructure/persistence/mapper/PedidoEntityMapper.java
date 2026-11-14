package com.example.demo.infrastructure.persistence.mapper;

import com.example.demo.domain.model.DetallePedido;
import com.example.demo.domain.model.Pedido;
import com.example.demo.infrastructure.persistence.entity.DetallePedidoEntity;
import com.example.demo.infrastructure.persistence.entity.PedidoEntity;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Mapeador entre la entidad de dominio Pedido y la entidad JPA PedidoEntity.
 */
@Component
public class PedidoEntityMapper {

    private final ProductoEntityMapper productoMapper;

    public PedidoEntityMapper(ProductoEntityMapper productoMapper) {
        this.productoMapper = productoMapper;
    }

    public Pedido toDomain(PedidoEntity entity) {
        if (entity == null) {
            return null;
        }

        List<DetallePedido> domainItems = new ArrayList<>();
        if (entity.getItems() != null) {
            for (DetallePedidoEntity itemEntity : entity.getItems()) {
                domainItems.add(DetallePedido.builder()
                        .id(itemEntity.getId())
                        .pedidoId(entity.getId())
                        .producto(productoMapper.toDomain(itemEntity.getProducto()))
                        .cantidad(itemEntity.getCantidad())
                        .precioUnitario(itemEntity.getPrecioUnitario())
                        .subtotal(itemEntity.getSubtotal())
                        .build());
            }
        }

        return Pedido.builder()
                .id(entity.getId())
                .usuarioId(entity.getUsuarioId())
                .cliente(entity.getCliente())
                .fecha(entity.getFecha())
                .estado(entity.getEstado())
                .total(entity.getTotal())
                .items(domainItems)
                .build();
    }

    public PedidoEntity toEntity(Pedido domain) {
        if (domain == null) {
            return null;
        }

        PedidoEntity entity = PedidoEntity.builder()
                .id(domain.getId())
                .usuarioId(domain.getUsuarioId())
                .cliente(domain.getCliente())
                .fecha(domain.getFecha())
                .estado(domain.getEstado())
                .total(domain.getTotal())
                .items(new ArrayList<>())
                .build();

        if (domain.getItems() != null) {
            for (DetallePedido domainItem : domain.getItems()) {
                DetallePedidoEntity itemEntity = DetallePedidoEntity.builder()
                        .id(domainItem.getId())
                        .pedido(entity)
                        .producto(productoMapper.toEntity(domainItem.getProducto()))
                        .cantidad(domainItem.getCantidad())
                        .precioUnitario(domainItem.getPrecioUnitario())
                        .subtotal(domainItem.getSubtotal())
                        .build();
                entity.getItems().add(itemEntity);
            }
        }

        return entity;
    }
}
