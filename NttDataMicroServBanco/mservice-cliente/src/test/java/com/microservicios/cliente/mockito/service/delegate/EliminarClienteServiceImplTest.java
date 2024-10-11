package com.microservicios.cliente.mockito.service.delegate;

import com.microservicios.cliente.apidelegate.EliminarClienteServiceImpl;
import com.microservicios.cliente.business.ClienteService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class EliminarClienteServiceImplTest {

    @InjectMocks
    private EliminarClienteServiceImpl eliminarClienteServiceImpl;

    @Mock
    private ClienteService clienteService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void eliminarCliente_Success() {
        // Arrange
        Integer id = 1;
        String expectedMessage = "Cliente eliminado con éxito: " + id;

        when(clienteService.eliminarCliente(Long.valueOf(id))).thenReturn(expectedMessage);

        // Act
        ResponseEntity<String> response = eliminarClienteServiceImpl.eliminarCliente(id);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedMessage, response.getBody());
        verify(clienteService, times(1)).eliminarCliente(Long.valueOf(id));
    }

    @Test
    void eliminarCliente_ClienteNoEncontrado() {
        // Arrange
        Integer id = 1;
        when(clienteService.eliminarCliente(Long.valueOf(id)))
                .thenThrow(new IllegalArgumentException("Cliente no encontrado con ID: " + id));

        // Act & Assert
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
            eliminarClienteServiceImpl.eliminarCliente(id);
        });

        assertEquals("Cliente no encontrado con ID: 1", thrown.getMessage());
        verify(clienteService, times(1)).eliminarCliente(Long.valueOf(id));
    }

    @Test
    void eliminarCliente_CuentaActiva() {
        // Arrange
        Integer id = 1;
        when(clienteService.eliminarCliente(Long.valueOf(id)))
                .thenThrow(new IllegalArgumentException("No se puede eliminar el cliente porque tiene cuentas activas."));

        // Act & Assert
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
            eliminarClienteServiceImpl.eliminarCliente(id);
        });

        assertEquals("No se puede eliminar el cliente porque tiene cuentas activas.", thrown.getMessage());
        verify(clienteService, times(1)).eliminarCliente(Long.valueOf(id));
    }
}
