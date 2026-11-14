package com.example.demo.domain.model;

import com.example.demo.domain.exception.ReglaDeNegocioException;
import lombok.*;

import java.time.LocalDateTime;

/**
 * Entidad de dominio pura para Usuario.
 * Totalmente desacoplada de frameworks y bases de datos.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Usuario {

    private Long id;
    private String nombre;
    private String email;
    private String telefono;
    private String direccion;
    @Builder.Default
    private LocalDateTime fechaRegistro = LocalDateTime.now();

    /**
     * Valida las reglas de negocio del usuario.
     */
    public void validar() {
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new ReglaDeNegocioException("El nombre del usuario no puede estar vacío");
        }
        if (email == null || email.trim().isEmpty() || !email.contains("@")) {
            throw new ReglaDeNegocioException("El correo electrónico del usuario debe ser válido");
        }
    }
}
