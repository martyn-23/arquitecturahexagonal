package com.example.demo.infrastructure.adapter.output;

import com.example.demo.domain.model.Usuario;
import com.example.demo.domain.port.UsuarioRepositoryPort;
import com.example.demo.infrastructure.persistence.UsuarioJpaRepository;
import com.example.demo.infrastructure.persistence.entity.UsuarioEntity;
import com.example.demo.infrastructure.persistence.mapper.UsuarioEntityMapper;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Adaptador de salida (Driven Adapter) para la persistencia de Usuarios.
 * Conecta el puerto de salida del dominio con Spring Data JPA.
 */
@Component
public class UsuarioRepositoryAdapter implements UsuarioRepositoryPort {

    private final UsuarioJpaRepository usuarioJpaRepository;
    private final UsuarioEntityMapper usuarioEntityMapper;

    public UsuarioRepositoryAdapter(UsuarioJpaRepository usuarioJpaRepository,
                                  UsuarioEntityMapper usuarioEntityMapper) {
        this.usuarioJpaRepository = usuarioJpaRepository;
        this.usuarioEntityMapper = usuarioEntityMapper;
    }

    @Override
    @Transactional
    public Usuario guardar(Usuario usuario) {
        UsuarioEntity entity = usuarioEntityMapper.toEntity(usuario);
        UsuarioEntity guardada = usuarioJpaRepository.save(entity);
        return usuarioEntityMapper.toDomain(guardada);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Usuario> buscarPorId(Long id) {
        return usuarioJpaRepository.findById(id)
                .map(usuarioEntityMapper::toDomain);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Usuario> buscarPorEmail(String email) {
        return usuarioJpaRepository.findByEmail(email)
                .map(usuarioEntityMapper::toDomain);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Usuario> listarTodos() {
        return usuarioJpaRepository.findAll().stream()
                .map(usuarioEntityMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void eliminarPorId(Long id) {
        usuarioJpaRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existePorId(Long id) {
        return usuarioJpaRepository.existsById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existePorEmail(String email) {
        return usuarioJpaRepository.existsByEmail(email);
    }
}
