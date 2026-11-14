package com.example.demo.application.usecase;

import com.example.demo.domain.exception.ReglaDeNegocioException;
import com.example.demo.domain.model.Usuario;
import com.example.demo.domain.port.UsuarioRepositoryPort;

/**
 * Caso de uso: Registrar o crear un nuevo usuario en el sistema.
 * Aplica validaciones de reglas de negocio antes de persistir.
 */
public class CrearUsuarioUseCase {

    private final UsuarioRepositoryPort usuarioRepositoryPort;

    public CrearUsuarioUseCase(UsuarioRepositoryPort usuarioRepositoryPort) {
        this.usuarioRepositoryPort = usuarioRepositoryPort;
    }

    public Usuario ejecutar(Usuario usuario) {
        if (usuario == null) {
            throw new ReglaDeNegocioException("Los datos del usuario no pueden ser nulos");
        }
        usuario.validar();
        if (usuarioRepositoryPort.existePorEmail(usuario.getEmail())) {
            throw new ReglaDeNegocioException("Ya existe un usuario registrado con el correo: " + usuario.getEmail());
        }
        return usuarioRepositoryPort.guardar(usuario);
    }
}
