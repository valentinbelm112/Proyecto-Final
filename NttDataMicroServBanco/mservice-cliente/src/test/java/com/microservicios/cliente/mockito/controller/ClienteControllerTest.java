package com.microservicios.cliente.mockito.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.microservicios.cliente.business.ClienteService;
import com.microservicios.cliente.model.ClienteRequest;
import com.microservicios.cliente.model.ClienteResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

class ClienteControllerTest {

/*

    @Mock
    private ClienteService clienteService;

    WebTestClient client;;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        client = WebTestClient.bindToController(new ProductController(clienteService)).build();;
    }

    @Test
    @DisplayName("Crear un cliente exitosamente")
    void crearCliente() throws Exception {
        ClienteRequest clienteRequest = new ClienteRequest();
        clienteRequest.setNombre("Juan");
        clienteRequest.setApellido("Pérez");
        clienteRequest.setDni("12345678");
        clienteRequest.setEmail("juan.perez@example.com");

        ClienteResponse clienteResponse = new ClienteResponse();
        clienteResponse.setId(1);
        clienteResponse.setNombre("Juan");
        clienteResponse.setApellido("Pérez");
        clienteResponse.setDni("12345678");
        clienteResponse.setEmail("juan.perez@example.com");

        when(clienteService.crearCliente(any(ClienteRequest.class))).thenReturn(clienteResponse);

        mockMvc.perform(post("/api/cliente/crear")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(clienteRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nombre").value("Juan"));
    }

    @Test
    @DisplayName("Listar todos los clientes exitosamente")
    void listarClientes() throws Exception {
        ClienteResponse cliente1 = new ClienteResponse();
        cliente1.setId(1);
        cliente1.setNombre("Juan");
        cliente1.setApellido("Pérez");

        ClienteResponse cliente2 = new ClienteResponse();
        cliente2.setId(2);
        cliente2.setNombre("María");
        cliente2.setApellido("Gómez");

        List<ClienteResponse> clientes = Arrays.asList(cliente1, cliente2);
        when(clienteService.listarClientes()).thenReturn(clientes);

        mockMvc.perform(get("/api/cliente/listar"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nombre").value("Juan"))
                .andExpect(jsonPath("$[1].nombre").value("María"));
    }

    @Test
    @DisplayName("Obtener un cliente por ID exitosamente")
    void obtenerClientePorId() throws Exception {
        ClienteResponse clienteResponse = new ClienteResponse();
        clienteResponse.setId(1);
        clienteResponse.setNombre("Juan");
        clienteResponse.setApellido("Pérez");

        when(clienteService.obtenerClientePorId(anyLong())).thenReturn(clienteResponse);

        mockMvc.perform(get("/api/cliente/{id}", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Juan"));
    }

    @Test
    @DisplayName("Actualizar un cliente exitosamente")
    void actualizarCliente() throws Exception {
        Long clienteId = 1L;
        ClienteRequest clienteRequest = new ClienteRequest();
        clienteRequest.setNombre("Juan Actualizado");
        clienteRequest.setApellido("Pérez");

        ClienteResponse clienteResponse = new ClienteResponse();
        clienteResponse.setId(clienteId);
        clienteResponse.setNombre("Juan Actualizado");
        clienteResponse.setApellido("Pérez");

        when(clienteService.actualizarCliente(anyLong(), any(ClienteRequest.class))).thenReturn(clienteResponse);

        mockMvc.perform(put("/api/cliente/actualizar/{id}", clienteId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(clienteRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Juan Actualizado"));
    }

    @Test
    @DisplayName("Eliminar un cliente exitosamente")
    void eliminarCliente() throws Exception {
        Long clienteId = 1L;

        mockMvc.perform(delete("/api/cliente/eliminar/{id}", clienteId))
                .andExpect(status().isOk());
    }
    */

}