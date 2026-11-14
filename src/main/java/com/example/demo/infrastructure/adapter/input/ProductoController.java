package com.example.demo.infrastructure.adapter.input;

import com.example.demo.application.usecase.ActualizarProductoUseCase;
import com.example.demo.application.usecase.ConsultarProductoUseCase;
import com.example.demo.application.usecase.CrearProductoUseCase;
import com.example.demo.application.usecase.EliminarProductoUseCase;
import com.example.demo.domain.model.Producto;
import com.example.demo.infrastructure.adapter.input.dto.ProductoRequest;
import com.example.demo.infrastructure.adapter.input.dto.ProductoResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Adaptador de entrada (Driving Adapter) REST para la gestión del catálogo de Productos.
 */
@RestController
@RequestMapping("/api/productos")
public class ProductoController {

    private final CrearProductoUseCase crearProductoUseCase;
    private final ConsultarProductoUseCase consultarProductoUseCase;
    private final ActualizarProductoUseCase actualizarProductoUseCase;
    private final EliminarProductoUseCase eliminarProductoUseCase;

    public ProductoController(CrearProductoUseCase crearProductoUseCase,
                              ConsultarProductoUseCase consultarProductoUseCase,
                              ActualizarProductoUseCase actualizarProductoUseCase,
                              EliminarProductoUseCase eliminarProductoUseCase) {
        this.crearProductoUseCase = crearProductoUseCase;
        this.consultarProductoUseCase = consultarProductoUseCase;
        this.actualizarProductoUseCase = actualizarProductoUseCase;
        this.eliminarProductoUseCase = eliminarProductoUseCase;
    }

    @GetMapping
    public ResponseEntity<List<ProductoResponse>> listar() {
        List<ProductoResponse> lista = consultarProductoUseCase.listarTodos().stream()
                .map(ProductoResponse::fromDomain)
                .collect(Collectors.toList());
        return ResponseEntity.ok(lista);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductoResponse> obtenerPorId(@PathVariable Long id) {
        Producto producto = consultarProductoUseCase.buscarPorId(id);
        return ResponseEntity.ok(ProductoResponse.fromDomain(producto));
    }

    @PostMapping
    public ResponseEntity<ProductoResponse> crear(
            @RequestBody(required = false) ProductoRequest request,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) BigDecimal price,
            @RequestParam(required = false) Integer stock,
            @RequestParam(required = false) String description) {

        String finalNombre = request != null && request.getNombre() != null ? request.getNombre() : name;
        String finalDesc = request != null && request.getDescripcion() != null ? request.getDescripcion() : description;
        BigDecimal finalPrecio = request != null && request.getPrecio() != null ? request.getPrecio() : price;
        Integer finalStock = request != null && request.getStock() != null ? request.getStock() : (stock != null ? stock : 0);

        Producto producto = Producto.builder()
                .nombre(finalNombre)
                .descripcion(finalDesc)
                .precio(finalPrecio)
                .stock(finalStock)
                .build();

        Producto creado = crearProductoUseCase.ejecutar(producto);
        return ResponseEntity.status(HttpStatus.CREATED).body(ProductoResponse.fromDomain(creado));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductoResponse> actualizar(@PathVariable Long id, @RequestBody ProductoRequest request) {
        Producto producto = Producto.builder()
                .nombre(request.getNombre())
                .descripcion(request.getDescripcion())
                .precio(request.getPrecio())
                .stock(request.getStock())
                .build();

        Producto actualizado = actualizarProductoUseCase.ejecutar(id, producto);
        return ResponseEntity.ok(ProductoResponse.fromDomain(actualizado));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        eliminarProductoUseCase.ejecutar(id);
        return ResponseEntity.noContent().build();
    }
}
