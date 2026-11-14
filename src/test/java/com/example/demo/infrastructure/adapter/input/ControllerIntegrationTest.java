package com.example.demo.infrastructure.adapter.input;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(properties = {
        "spring.datasource.url=jdbc:h2:mem:tienda_db;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE",
        "spring.datasource.driver-class-name=org.h2.Driver",
        "spring.datasource.username=sa",
        "spring.datasource.password=",
        "spring.jpa.hibernate.ddl-auto=create-drop",
        "spring.jpa.show-sql=false"
})
@AutoConfigureMockMvc
class ControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("Flujo completo: Registrar Usuario -> Crear Producto -> Crear Pedido -> Confirmar Pedido")
    void flujoCompletoUsuariosYPedidos() throws Exception {
        // 1. Registrar un Usuario
        Map<String, String> usuarioBody = Map.of(
                "nombre", "Brayan Alexis",
                "email", "brayan.alexis@example.com",
                "telefono", "3119876543",
                "direccion", "Carrera 15 # 45-60"
        );

        String userResponseJson = mockMvc.perform(post("/api/usuarios")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(usuarioBody)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.nombre").value("Brayan Alexis"))
                .andReturn().getResponse().getContentAsString();

        Long usuarioId = objectMapper.readTree(userResponseJson).get("id").asLong();

        // 2. Crear un Producto en catálogo
        Map<String, Object> productoBody = Map.of(
                "nombre", "Teclado Mecanico RGB",
                "descripcion", "Switch Blue",
                "precio", 120.50,
                "stock", 15
        );

        String prodResponseJson = mockMvc.perform(post("/api/productos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(productoBody)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.stock").value(15))
                .andReturn().getResponse().getContentAsString();

        Long productoId = objectMapper.readTree(prodResponseJson).get("id").asLong();

        // 3. Crear Pedido en estado CARRITO
        Map<String, Object> pedidoBody = Map.of(
                "usuarioId", usuarioId,
                "cliente", "Brayan Alexis",
                "items", java.util.List.of(
                        Map.of("productoId", productoId, "cantidad", 2)
                )
        );

        String pedidoResponseJson = mockMvc.perform(post("/api/pedidos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(pedidoBody)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.estado").value("CARRITO"))
                .andExpect(jsonPath("$.total").value(241.00))
                .andReturn().getResponse().getContentAsString();

        Long pedidoId = objectMapper.readTree(pedidoResponseJson).get("id").asLong();

        // 4. Confirmar Pedido
        mockMvc.perform(patch("/api/pedidos/" + pedidoId + "/confirmar"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.estado").value("CONFIRMADO"));

        // 5. Verificar que el stock del producto disminuyó de 15 a 13
        mockMvc.perform(get("/api/productos/" + productoId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.stock").value(13));
    }
}
