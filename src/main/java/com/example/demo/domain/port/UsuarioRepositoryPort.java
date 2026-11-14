package com.example.demo.domain.port;

import com.example.demo.domain.model.Usuario;

import java.util.List;
import java.util.Optional;

/**
 * Puerto de salida (Driven Port) para la persistencia y recuperación de Usuarios.
 * Definido por el Dominio para que la Infraestructura lo implemente.
 */
public interface UsuarioRepositoryPort {

    Usuario guardar(Usuario usuario);

    Optional<Usuario> buscarPorId(Long id);

    Optional<Usuario> buscarPorEmail(String email);

    List<Usuario> listarTodos();

    void eliminarPorId(Long id);

    boolean existePorId(Long id);

    boolean existePorEmail(String email);
}
