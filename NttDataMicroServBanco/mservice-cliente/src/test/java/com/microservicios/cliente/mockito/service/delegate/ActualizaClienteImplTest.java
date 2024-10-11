package com.microservicios.cliente.mockito.service.delegate;

import com.microservicios.cliente.apidelegate.ActualizaClienteImpl;
import com.microservicios.cliente.business.ClienteService;
import com.microservicios.cliente.model.ClienteRequest;
import com.microservicios.cliente.model.ClienteResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ActualizaClienteImplTest {

    @InjectMocks
    private ActualizaClienteImpl actualizaClienteImpl;

    @Mock
    private ClienteService clienteService;

    private ClienteRequest clienteRequest;
    private ClienteResponse clienteResponse;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        clienteRequest = new ClienteRequest();
        clienteRequest.setNombre("Juan");
        clienteRequest.setApellido("Pérez");
        clienteRequest.setDni("12345678");
        clienteRequest.setEmail("juan@example.com");

        clienteResponse = new ClienteResponse();
        clienteResponse.setId(1);
        clienteResponse.setNombre("Juan");
        clienteResponse.setApellido("Pérez");
        clienteResponse.setDni("12345678");
        clienteResponse.setEmail("juan@example.com");
    }

    @Test
    void actualizarCliente_Success() {
        // Arrange
        Integer id = 1;
        when(clienteService.actualizarCliente(eq(Long.valueOf(id)), any(ClienteRequest.class)))
                .thenReturn(clienteResponse);

        // Act
        ResponseEntity<ClienteResponse> response = actualizaClienteImpl.actualizarCliente(id, clienteRequest);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(clienteResponse, response.getBody());
        verify(clienteService, times(1)).actualizarCliente(Long.valueOf(id), clienteRequest);
    }

    @Test
    void actualizarCliente_ClienteNoEncontrado() {
        // Arrange
        Integer id = 1;
        when(clienteService.actualizarCliente(eq(Long.valueOf(id)), any(ClienteRequest.class)))
                .thenThrow(new IllegalArgumentException("Cliente no encontrado"));

        // Act & Assert
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
            actualizaClienteImpl.actualizarCliente(id, clienteRequest);
        });

        assertEquals("Cliente no encontrado", thrown.getMessage());
        verify(clienteService, times(1)).actualizarCliente(Long.valueOf(id), clienteRequest);
    }
}