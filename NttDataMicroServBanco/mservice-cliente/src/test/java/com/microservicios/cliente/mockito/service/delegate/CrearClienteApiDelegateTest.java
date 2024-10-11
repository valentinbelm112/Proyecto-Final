package com.microservicios.cliente.mockito.service.delegate;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.microservicios.cliente.apidelegate.CrearClienteApiDelegate;
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
public class CrearClienteApiDelegateTest {

    @InjectMocks
    private CrearClienteApiDelegate crearClienteApiDelegate; // Clase que estás probando

    @Mock
    private ClienteService clienteService; // Dependencia que estás simulando

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this); // Inicializa los mocks
    }

    @Test
    public void testCrearCliente_Success() {
        // Configurar el comportamiento simulado
        ClienteRequest clienteRequest = new ClienteRequest(/* inicializa el objeto como sea necesario */);
        ClienteResponse mockClienteResponse = new ClienteResponse(/* inicializa el objeto como sea necesario */);

        when(clienteService.crearCliente(clienteRequest)).thenReturn(mockClienteResponse);

        // Llamar al método que se está probando
        ResponseEntity<ClienteResponse> response = crearClienteApiDelegate.crearCliente(clienteRequest);

        // Verificar resultados
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(mockClienteResponse, response.getBody(), "El cuerpo de la respuesta debería coincidir con el cliente esperado");
    }

    @Test
    public void testCrearCliente_NullResponse() {
        // Configurar el comportamiento simulado
        ClienteRequest clienteRequest = new ClienteRequest(/* inicializa el objeto como sea necesario */);

        when(clienteService.crearCliente(clienteRequest)).thenReturn(null);

        // Llamar al método que se está probando
        ResponseEntity<ClienteResponse> response = crearClienteApiDelegate.crearCliente(clienteRequest);

        // Verificar resultados
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNull(response.getBody(), "El cuerpo de la respuesta debería ser nulo si el servicio devuelve null");
    }
}
