package com.example.demo.application.usecase;

import com.example.demo.domain.exception.RecursoNoEncontradoException;
import com.example.demo.domain.model.Usuario;
import com.example.demo.domain.port.UsuarioRepositoryPort;

import java.util.List;

/**
 * Caso de uso: Consultar usuarios registrados (individual o listado general).
 */
public class ConsultarUsuarioUseCase {

    private final UsuarioRepositoryPort usuarioRepositoryPort;

    public ConsultarUsuarioUseCase(UsuarioRepositoryPort usuarioRepositoryPort) {
        this.usuarioRepositoryPort = usuarioRepositoryPort;
    }

    public Usuario buscarPorId(Long id) {
        return usuarioRepositoryPort.buscarPorId(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Usuario no encontrado con ID: " + id));
    }

    public List<Usuario> listarTodos() {
        return usuarioRepositoryPort.listarTodos();
    }
}
