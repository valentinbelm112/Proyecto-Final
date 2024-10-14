package com.microservicios.cliente.mockito.controller;

import com.microservicios.cliente.api.EliminarApiController;
import com.microservicios.cliente.api.EliminarApiDelegate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class EliminarApiControllerTest {
    @InjectMocks
    private EliminarApiController eliminarApiController;

    @Mock
    private EliminarApiDelegate eliminarApiDelegate;



    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);  // Inicia mocks
    }

    @Test
    public void testDeleteEndpoint() {
        // Simular una operación de eliminación
        when(eliminarApiDelegate.eliminarCliente(anyInt())).thenReturn(ResponseEntity.ok().build());

        ResponseEntity<?> response = eliminarApiController.eliminarCliente(1);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(eliminarApiDelegate).eliminarCliente(anyInt());
    }

    @Test
    public void testEliminarCliente_ClienteNoExistente() {
        // Simula que no se encontró el cliente
        when(eliminarApiDelegate.eliminarCliente(999)).thenReturn(new ResponseEntity<>(HttpStatus.NOT_FOUND));

        ResponseEntity<?> response = eliminarApiController.eliminarCliente(999);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }
}
