package com.microservicios.cliente.mockito.service;

import com.microservicios.cliente.business.impl.ClienteServiceImpl;
import com.microservicios.cliente.entity.ClienteEntity;
import com.microservicios.cliente.mapper.ClienteMapperEntity;
import com.microservicios.cliente.model.ClienteRequest;
import com.microservicios.cliente.model.ClienteResponse;
import com.microservicios.cliente.repository.ClienteRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;

@ExtendWith(MockitoExtension.class)
class ClienteServiceImplTest {

    @Mock
    private ClienteRepository clienteRepository;

    @InjectMocks
    private ClienteServiceImpl clienteService;

    @Test
    @DisplayName("Cuando se crea un cliente con exito")
    void whenCrearClienteOk() {
        ClienteRequest clienteRequest = new ClienteRequest();
        clienteRequest.setNombre("Juan");
        clienteRequest.setApellido("Pérez");
        clienteRequest.setDni("12345678");
        clienteRequest.setEmail("juan.perez@example.com");

        ClienteEntity clienteEntity = new ClienteEntity();
        clienteEntity.setId(1L);
        clienteEntity.setNombre("Juan");
        clienteEntity.setApellido("Pérez");
        clienteEntity.setDni("12345678");
        clienteEntity.setEmail("juan.perez@example.com");

        Mockito.when(clienteRepository.save(any(ClienteEntity.class)))
                .thenReturn(clienteEntity);

        ClienteResponse expectedResponse = ClienteMapperEntity.ClienteresponseMapperofClienteEntity(clienteEntity);

        ClienteResponse actualResponse = clienteService.crearCliente(clienteRequest);

        assertEquals(expectedResponse, actualResponse);
    }

    @Test
    @DisplayName("Cuando se lista los clientes con exito")
    void whenListarClientesOk() {
        // Crea varios clientes de prueba
        ClienteEntity cliente1 = new ClienteEntity();
        cliente1.setId(1L);
        cliente1.setNombre("Juan");
        cliente1.setApellido("Pérez");

        ClienteEntity cliente2 = new ClienteEntity();
        cliente2.setId(2L);
        cliente2.setNombre("María");
        cliente2.setApellido("Gómez");

        Mockito.when(clienteRepository.findAll())
                .thenReturn(Arrays.asList(cliente1, cliente2));

        List<ClienteResponse> expectedResponses = Arrays.asList(
                ClienteMapperEntity.ClienteresponseMapperofClienteEntity(cliente1),
                ClienteMapperEntity.ClienteresponseMapperofClienteEntity(cliente2)
        );

        List<ClienteResponse> actualResponses = clienteService.listarClientes();

        assertEquals(expectedResponses.size(), actualResponses.size());
        assertEquals(expectedResponses.get(0), actualResponses.get(0));
        assertEquals(expectedResponses.get(1), actualResponses.get(1));
    }

    @Test
    @DisplayName("Cuando se obtiene por id el cliente con exito")
    void whenObtenerClientePorIdOk() {
        ClienteEntity clienteEntity = new ClienteEntity();
        clienteEntity.setId(1L);
        clienteEntity.setNombre("Juan");
        clienteEntity.setApellido("Pérez");

        Mockito.when(clienteRepository.findById(anyLong()))
                .thenReturn(Optional.of(clienteEntity));

        ClienteResponse expectedResponse = ClienteMapperEntity.ClienteresponseMapperofClienteEntity(clienteEntity);

        ClienteResponse actualResponse = clienteService.obtenerClientePorId(1L);

        assertEquals(expectedResponse, actualResponse);
    }

    @Test
    @DisplayName("Cuando se actualiza un cliente  con exito")
    void whenActualizarClienteOk() {
        ClienteRequest clienteRequest = new ClienteRequest();
        clienteRequest.setNombre("Juan Actualizado");
        clienteRequest.setApellido("Pérez");

        ClienteEntity clienteEntity = new ClienteEntity();
        clienteEntity.setId(1L);
        clienteEntity.setNombre("Juan");
        clienteEntity.setApellido("Pérez");

        Mockito.when(clienteRepository.findById(anyLong()))
                .thenReturn(Optional.of(clienteEntity));
        Mockito.when(clienteRepository.save(any(ClienteEntity.class)))
                .thenReturn(clienteEntity);

        ClienteResponse actualResponse = clienteService.actualizarCliente(1L, clienteRequest);

        assertEquals("Juan Actualizado", actualResponse.getNombre());
    }
/*
    @Test
    @DisplayName("wCuando se elimina un cliente  con exito")
    void whenEliminarClienteOk() {
        Long clienteId = 1L;

        Mockito.when(clienteRepository.existsById(clienteId))
                .thenReturn(true);

        //clienteService.eliminarCliente(clienteId);

        Mockito.verify(clienteRepository).deleteById(clienteId);
    }*/
}
