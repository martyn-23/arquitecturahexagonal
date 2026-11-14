package com.example.demo.infrastructure.persistence.mapper;

import com.example.demo.domain.model.Usuario;
import com.example.demo.infrastructure.persistence.entity.UsuarioEntity;
import org.springframework.stereotype.Component;

/**
 * Mapeador entre la entidad de dominio Usuario y la entidad JPA UsuarioEntity.
 */
@Component
public class UsuarioEntityMapper {

    public Usuario toDomain(UsuarioEntity entity) {
        if (entity == null) {
            return null;
        }
        return Usuario.builder()
                .id(entity.getId())
                .nombre(entity.getNombre())
                .email(entity.getEmail())
                .telefono(entity.getTelefono())
                .direccion(entity.getDireccion())
                .fechaRegistro(entity.getFechaRegistro())
                .build();
    }

    public UsuarioEntity toEntity(Usuario domain) {
        if (domain == null) {
            return null;
        }
        return UsuarioEntity.builder()
                .id(domain.getId())
                .nombre(domain.getNombre())
                .email(domain.getEmail())
                .telefono(domain.getTelefono())
                .direccion(domain.getDireccion())
                .fechaRegistro(domain.getFechaRegistro())
                .build();
    }
}
