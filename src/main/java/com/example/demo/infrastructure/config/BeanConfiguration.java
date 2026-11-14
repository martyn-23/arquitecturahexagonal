package com.example.demo.infrastructure.config;

import com.example.demo.application.usecase.*;
import com.example.demo.domain.port.PedidoRepositoryPort;
import com.example.demo.domain.port.ProductoRepositoryPort;
import com.example.demo.domain.port.UsuarioRepositoryPort;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Clase de configuración que instancia los casos de uso como Beans de Spring.
 * Esto permite que las capas de Dominio y Aplicación permanezcan completamente
 * puras e independientes del framework Spring.
 */
@Configuration
public class BeanConfiguration {

    // Casos de uso de Usuario
    @Bean
    public CrearUsuarioUseCase crearUsuarioUseCase(UsuarioRepositoryPort usuarioRepositoryPort) {
        return new CrearUsuarioUseCase(usuarioRepositoryPort);
    }

    @Bean
    public ConsultarUsuarioUseCase consultarUsuarioUseCase(UsuarioRepositoryPort usuarioRepositoryPort) {
        return new ConsultarUsuarioUseCase(usuarioRepositoryPort);
    }

    @Bean
    public ActualizarUsuarioUseCase actualizarUsuarioUseCase(UsuarioRepositoryPort usuarioRepositoryPort) {
        return new ActualizarUsuarioUseCase(usuarioRepositoryPort);
    }

    @Bean
    public EliminarUsuarioUseCase eliminarUsuarioUseCase(UsuarioRepositoryPort usuarioRepositoryPort) {
        return new EliminarUsuarioUseCase(usuarioRepositoryPort);
    }

    // Casos de uso de Producto
    @Bean
    public CrearProductoUseCase crearProductoUseCase(ProductoRepositoryPort productoRepositoryPort) {
        return new CrearProductoUseCase(productoRepositoryPort);
    }

    @Bean
    public ConsultarProductoUseCase consultarProductoUseCase(ProductoRepositoryPort productoRepositoryPort) {
        return new ConsultarProductoUseCase(productoRepositoryPort);
    }

    @Bean
    public ActualizarProductoUseCase actualizarProductoUseCase(ProductoRepositoryPort productoRepositoryPort) {
        return new ActualizarProductoUseCase(productoRepositoryPort);
    }

    @Bean
    public EliminarProductoUseCase eliminarProductoUseCase(ProductoRepositoryPort productoRepositoryPort) {
        return new EliminarProductoUseCase(productoRepositoryPort);
    }

    // Casos de uso de Pedido
    @Bean
    public CrearPedidoUseCase crearPedidoUseCase(PedidoRepositoryPort pedidoRepositoryPort,
                                                 ProductoRepositoryPort productoRepositoryPort,
                                                 UsuarioRepositoryPort usuarioRepositoryPort) {
        return new CrearPedidoUseCase(pedidoRepositoryPort, productoRepositoryPort, usuarioRepositoryPort);
    }

    @Bean
    public ConsultarPedidoUseCase consultarPedidoUseCase(PedidoRepositoryPort pedidoRepositoryPort) {
        return new ConsultarPedidoUseCase(pedidoRepositoryPort);
    }

    @Bean
    public ActualizarPedidoUseCase actualizarPedidoUseCase(PedidoRepositoryPort pedidoRepositoryPort,
                                                           ProductoRepositoryPort productoRepositoryPort) {
        return new ActualizarPedidoUseCase(pedidoRepositoryPort, productoRepositoryPort);
    }

    @Bean
    public AgregarItemPedidoUseCase agregarItemPedidoUseCase(PedidoRepositoryPort pedidoRepositoryPort,
                                                             ProductoRepositoryPort productoRepositoryPort) {
        return new AgregarItemPedidoUseCase(pedidoRepositoryPort, productoRepositoryPort);
    }

    @Bean
    public QuitarItemPedidoUseCase quitarItemPedidoUseCase(PedidoRepositoryPort pedidoRepositoryPort) {
        return new QuitarItemPedidoUseCase(pedidoRepositoryPort);
    }

    @Bean
    public ConfirmarPedidoUseCase confirmarPedidoUseCase(PedidoRepositoryPort pedidoRepositoryPort,
                                                         ProductoRepositoryPort productoRepositoryPort) {
        return new ConfirmarPedidoUseCase(pedidoRepositoryPort, productoRepositoryPort);
    }

    @Bean
    public EliminarPedidoUseCase eliminarPedidoUseCase(PedidoRepositoryPort pedidoRepositoryPort) {
        return new EliminarPedidoUseCase(pedidoRepositoryPort);
    }
}
