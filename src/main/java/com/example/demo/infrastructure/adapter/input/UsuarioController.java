package com.example.demo.infrastructure.adapter.input;

import com.example.demo.application.usecase.ActualizarUsuarioUseCase;
import com.example.demo.application.usecase.ConsultarUsuarioUseCase;
import com.example.demo.application.usecase.CrearUsuarioUseCase;
import com.example.demo.application.usecase.EliminarUsuarioUseCase;
import com.example.demo.domain.model.Usuario;
import com.example.demo.infrastructure.adapter.input.dto.UsuarioRequest;
import com.example.demo.infrastructure.adapter.input.dto.UsuarioResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Adaptador de entrada (Driving Adapter) REST para la gestión de Usuarios.
 * Expone los endpoints HTTP y se comunica únicamente con los Casos de Uso.
 */
@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    private final CrearUsuarioUseCase crearUsuarioUseCase;
    private final ConsultarUsuarioUseCase consultarUsuarioUseCase;
    private final ActualizarUsuarioUseCase actualizarUsuarioUseCase;
    private final EliminarUsuarioUseCase eliminarUsuarioUseCase;

    public UsuarioController(CrearUsuarioUseCase crearUsuarioUseCase,
                             ConsultarUsuarioUseCase consultarUsuarioUseCase,
                             ActualizarUsuarioUseCase actualizarUsuarioUseCase,
                             EliminarUsuarioUseCase eliminarUsuarioUseCase) {
        this.crearUsuarioUseCase = crearUsuarioUseCase;
        this.consultarUsuarioUseCase = consultarUsuarioUseCase;
        this.actualizarUsuarioUseCase = actualizarUsuarioUseCase;
        this.eliminarUsuarioUseCase = eliminarUsuarioUseCase;
    }

    @PostMapping
    public ResponseEntity<UsuarioResponse> registrar(@RequestBody UsuarioRequest request) {
        Usuario usuario = Usuario.builder()
                .nombre(request.getNombre())
                .email(request.getEmail())
                .telefono(request.getTelefono())
                .direccion(request.getDireccion())
                .build();
        Usuario creado = crearUsuarioUseCase.ejecutar(usuario);
        return ResponseEntity.status(HttpStatus.CREATED).body(UsuarioResponse.fromDomain(creado));
    }

    @GetMapping
    public ResponseEntity<List<UsuarioResponse>> listar() {
        List<UsuarioResponse> lista = consultarUsuarioUseCase.listarTodos().stream()
                .map(UsuarioResponse::fromDomain)
                .collect(Collectors.toList());
        return ResponseEntity.ok(lista);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UsuarioResponse> obtenerPorId(@PathVariable Long id) {
        Usuario usuario = consultarUsuarioUseCase.buscarPorId(id);
        return ResponseEntity.ok(UsuarioResponse.fromDomain(usuario));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UsuarioResponse> actualizar(@PathVariable Long id, @RequestBody UsuarioRequest request) {
        Usuario datos = Usuario.builder()
                .nombre(request.getNombre())
                .email(request.getEmail())
                .telefono(request.getTelefono())
                .direccion(request.getDireccion())
                .build();
        Usuario actualizado = actualizarUsuarioUseCase.ejecutar(id, datos);
        return ResponseEntity.ok(UsuarioResponse.fromDomain(actualizado));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        eliminarUsuarioUseCase.ejecutar(id);
        return ResponseEntity.noContent().build();
    }
}
