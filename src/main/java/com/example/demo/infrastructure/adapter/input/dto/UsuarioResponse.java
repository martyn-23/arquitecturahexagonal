package com.example.demo.infrastructure.adapter.input.dto;

import com.example.demo.domain.model.Usuario;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UsuarioResponse {
    private Long id;
    private String nombre;
    private String email;
    private String telefono;
    private String direccion;
    private LocalDateTime fechaRegistro;

    public static UsuarioResponse fromDomain(Usuario usuario) {
        if (usuario == null) {
            return null;
        }
        return UsuarioResponse.builder()
                .id(usuario.getId())
                .nombre(usuario.getNombre())
                .email(usuario.getEmail())
                .telefono(usuario.getTelefono())
                .direccion(usuario.getDireccion())
                .fechaRegistro(usuario.getFechaRegistro())
                .build();
    }
}
