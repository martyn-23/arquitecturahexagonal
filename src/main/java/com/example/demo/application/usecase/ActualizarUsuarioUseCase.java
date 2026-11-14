package com.example.demo.application.usecase;

import com.example.demo.domain.exception.RecursoNoEncontradoException;
import com.example.demo.domain.exception.ReglaDeNegocioException;
import com.example.demo.domain.model.Usuario;
import com.example.demo.domain.port.UsuarioRepositoryPort;

import java.util.Optional;

/**
 * Caso de uso: Actualizar información de un usuario existente.
 */
public class ActualizarUsuarioUseCase {

    private final UsuarioRepositoryPort usuarioRepositoryPort;

    public ActualizarUsuarioUseCase(UsuarioRepositoryPort usuarioRepositoryPort) {
        this.usuarioRepositoryPort = usuarioRepositoryPort;
    }

    public Usuario ejecutar(Long id, Usuario datosActualizados) {
        Usuario existente = usuarioRepositoryPort.buscarPorId(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Usuario no encontrado con ID: " + id));

        datosActualizados.validar();

        // Si cambió el email, validar que no esté ocupado por otro usuario
        if (!existente.getEmail().equalsIgnoreCase(datosActualizados.getEmail())) {
            Optional<Usuario> conMismoEmail = usuarioRepositoryPort.buscarPorEmail(datosActualizados.getEmail());
            if (conMismoEmail.isPresent() && !conMismoEmail.get().getId().equals(id)) {
                throw new ReglaDeNegocioException("El correo ya está en uso por otro usuario: " + datosActualizados.getEmail());
            }
        }

        existente.setNombre(datosActualizados.getNombre());
        existente.setEmail(datosActualizados.getEmail());
        existente.setTelefono(datosActualizados.getTelefono());
        existente.setDireccion(datosActualizados.getDireccion());

        return usuarioRepositoryPort.guardar(existente);
    }
}
