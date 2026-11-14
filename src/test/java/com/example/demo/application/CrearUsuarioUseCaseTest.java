package com.example.demo.application;

import com.example.demo.application.usecase.CrearUsuarioUseCase;
import com.example.demo.domain.exception.ReglaDeNegocioException;
import com.example.demo.domain.model.Usuario;
import com.example.demo.domain.port.UsuarioRepositoryPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CrearUsuarioUseCaseTest {

    @Mock
    private UsuarioRepositoryPort usuarioRepositoryPort;

    private CrearUsuarioUseCase crearUsuarioUseCase;

    @BeforeEach
    void setUp() {
        crearUsuarioUseCase = new CrearUsuarioUseCase(usuarioRepositoryPort);
    }

    @Test
    @DisplayName("Debe crear un usuario cuando el email no está registrado")
    void debeCrearUsuarioExitosamente() {
        Usuario usuario = Usuario.builder()
                .nombre("Laura Diaz")
                .email("laura@example.com")
                .build();

        when(usuarioRepositoryPort.existePorEmail("laura@example.com")).thenReturn(false);
        when(usuarioRepositoryPort.guardar(any(Usuario.class))).thenAnswer(inv -> {
            Usuario u = inv.getArgument(0);
            u.setId(10L);
            return u;
        });

        Usuario resultado = crearUsuarioUseCase.ejecutar(usuario);

        assertNotNull(resultado.getId());
        assertEquals("Laura Diaz", resultado.getNombre());
        verify(usuarioRepositoryPort, times(1)).guardar(usuario);
    }

    @Test
    @DisplayName("Debe lanzar excepción si el email ya existe")
    void debeFallarSiEmailYaExiste() {
        Usuario usuario = Usuario.builder()
                .nombre("Laura Diaz")
                .email("laura@example.com")
                .build();

        when(usuarioRepositoryPort.existePorEmail("laura@example.com")).thenReturn(true);

        assertThrows(ReglaDeNegocioException.class, () -> crearUsuarioUseCase.ejecutar(usuario));
        verify(usuarioRepositoryPort, never()).guardar(any());
    }
}
