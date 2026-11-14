package com.example.demo.application.usecase;

import com.example.demo.domain.exception.RecursoNoEncontradoException;
import com.example.demo.domain.port.UsuarioRepositoryPort;

/**
 * Caso de uso: Eliminar un usuario del sistema.
 */
public class EliminarUsuarioUseCase {

    private final UsuarioRepositoryPort usuarioRepositoryPort;

    public EliminarUsuarioUseCase(UsuarioRepositoryPort usuarioRepositoryPort) {
        this.usuarioRepositoryPort = usuarioRepositoryPort;
    }

    public void ejecutar(Long id) {
        if (!usuarioRepositoryPort.existePorId(id)) {
            throw new RecursoNoEncontradoException("Usuario no encontrado con ID: " + id);
        }
        usuarioRepositoryPort.eliminarPorId(id);
    }
}
