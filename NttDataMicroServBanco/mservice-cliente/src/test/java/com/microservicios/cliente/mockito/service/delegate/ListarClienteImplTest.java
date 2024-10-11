package com.microservicios.cliente.mockito.service.delegate;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.microservicios.cliente.apidelegate.ListarClienteImpl;
import com.microservicios.cliente.business.ClienteService;
import com.microservicios.cliente.model.ClienteResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;
public class ListarClienteImplTest {
    @InjectMocks
    private ListarClienteImpl listarClienteImpl; // Clase que estás probando

    @Mock
    private ClienteService clienteService; // Dependencia que estás simulando

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this); // Inicializa los mocks
    }

    @Test
    public void testListarClientes_EmptyList() {
        // Configurar el comportamiento simulado
        when(clienteService.listarClientes()).thenReturn(new ArrayList<>());

        // Llamar al método que se está probando
        ResponseEntity<List<ClienteResponse>> response = listarClienteImpl.listarClientes();

        // Verificar resultados
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().isEmpty(), "La lista debería estar vacía");
    }

    @Test
    public void testListarClientes_NonEmptyList() {
        // Configurar el comportamiento simulado
        List<ClienteResponse> mockClientes = new ArrayList<>();
        mockClientes.add(new ClienteResponse(/* inicializa el objeto como sea necesario */));

        when(clienteService.listarClientes()).thenReturn(mockClientes);

        // Llamar al método que se está probando
        ResponseEntity<List<ClienteResponse>> response = listarClienteImpl.listarClientes();

        // Verificar resultados
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mockClientes.size(), response.getBody().size(), "El tamaño de la lista debería coincidir");
    }
}
