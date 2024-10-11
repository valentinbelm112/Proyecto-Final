package com.microservicios.cliente.mockito.service.delegate;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.microservicios.cliente.apidelegate.ListarClienteIdIApiDelegate;
import com.microservicios.cliente.business.ClienteService;
import com.microservicios.cliente.model.ClienteResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class ListarClienteIdIApiDelegateTest {

    @InjectMocks
    private ListarClienteIdIApiDelegate listarClienteIdIApiDelegate; // Clase que estás probando

    @Mock
    private ClienteService clienteService; // Dependencia que estás simulando

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this); // Inicializa los mocks
    }

    @Test
    public void testObtenerClientePorId_Success() {
        // Configurar el comportamiento simulado
        Integer id = 1;
        ClienteResponse mockClienteResponse = new ClienteResponse(/* inicializa el objeto como sea necesario */);

        when(clienteService.obtenerClientePorId(Long.valueOf(id))).thenReturn(mockClienteResponse);

        // Llamar al método que se está probando
        ResponseEntity<ClienteResponse> response = listarClienteIdIApiDelegate.obtenerClientePorId(id);

        // Verificar resultados
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mockClienteResponse, response.getBody(), "El cuerpo de la respuesta debería coincidir con el cliente esperado");
    }

    @Test
    public void testObtenerClientePorId_NotFound() {
        // Configurar el comportamiento simulado
        Integer id = 1;

        when(clienteService.obtenerClientePorId(Long.valueOf(id))).thenReturn(null);

        // Llamar al método que se está probando
        ResponseEntity<ClienteResponse> response = listarClienteIdIApiDelegate.obtenerClientePorId(id);

        // Verificar resultados
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNull(response.getBody(), "El cuerpo de la respuesta debería ser nulo si el servicio no encuentra el cliente");
    }
}