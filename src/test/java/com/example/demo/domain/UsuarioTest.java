package com.example.demo.domain;

import com.example.demo.domain.exception.ReglaDeNegocioException;
import com.example.demo.domain.model.Usuario;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UsuarioTest {

    @Test
    @DisplayName("Debe validar exitosamente un usuario con datos correctos")
    void debeValidarUsuarioCorrecto() {
        Usuario usuario = Usuario.builder()
                .nombre("Brayan Gomez")
                .email("brayan@example.com")
                .telefono("3001234567")
                .direccion("Calle 10 # 20-30")
                .build();

        assertDoesNotThrow(usuario::validar);
    }

    @Test
    @DisplayName("Debe lanzar excepción si el nombre está vacío")
    void debeFallarConNombreVacio() {
        Usuario usuario = Usuario.builder()
                .nombre("   ")
                .email("brayan@example.com")
                .build();

        assertThrows(ReglaDeNegocioException.class, usuario::validar);
    }

    @Test
    @DisplayName("Debe lanzar excepción si el email es inválido")
    void debeFallarConEmailInvalido() {
        Usuario usuario = Usuario.builder()
                .nombre("Brayan")
                .email("correo-invalido")
                .build();

        assertThrows(ReglaDeNegocioException.class, usuario::validar);
    }
}
