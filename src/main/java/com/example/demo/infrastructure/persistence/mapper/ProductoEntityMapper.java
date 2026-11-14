package com.example.demo.infrastructure.persistence.mapper;

import com.example.demo.domain.model.Producto;
import com.example.demo.infrastructure.persistence.entity.ProductoEntity;
import org.springframework.stereotype.Component;

/**
 * Mapeador entre la entidad de dominio Producto y la entidad JPA ProductoEntity.
 */
@Component
public class ProductoEntityMapper {

    public Producto toDomain(ProductoEntity entity) {
        if (entity == null) {
            return null;
        }
        return Producto.builder()
                .id(entity.getId())
                .nombre(entity.getNombre())
                .descripcion(entity.getDescripcion())
                .precio(entity.getPrecio())
                .stock(entity.getStock())
                .build();
    }

    public ProductoEntity toEntity(Producto domain) {
        if (domain == null) {
            return null;
        }
        return ProductoEntity.builder()
                .id(domain.getId())
                .nombre(domain.getNombre())
                .descripcion(domain.getDescripcion())
                .precio(domain.getPrecio())
                .stock(domain.getStock())
                .build();
    }
}
