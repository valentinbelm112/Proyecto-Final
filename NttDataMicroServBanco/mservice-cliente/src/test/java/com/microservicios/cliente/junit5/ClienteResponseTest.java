package com.microservicios.cliente.junit5;

import static org.junit.jupiter.api.Assertions.*;

import com.microservicios.cliente.model.ClienteResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ClienteResponseTest {

    private ClienteResponse clienteResponse;

    @BeforeEach
    public void setUp() {
        clienteResponse = new ClienteResponse();
        clienteResponse.setNombre("John");
        clienteResponse.setApellido("Doe");
        clienteResponse.setDni("12345678");
        clienteResponse.setEmail("john.doe@example.com");
        clienteResponse.setTelefono("987654321");
        clienteResponse.setDireccion("123 Main St");
    }



    @Test
    public void testHashCode() {
        ClienteResponse otroClienteResponse = new ClienteResponse();
        otroClienteResponse.setNombre("John");
        otroClienteResponse.setApellido("Doe");
        otroClienteResponse.setDni("12345678");
        otroClienteResponse.setEmail("john.doe@example.com");
        otroClienteResponse.setTelefono("987654321");
        otroClienteResponse.setDireccion("123 Main St");

        assertEquals(clienteResponse.hashCode(), otroClienteResponse.hashCode());
    }

    @Test
    public void testEquals() {
        ClienteResponse otroClienteResponse = new ClienteResponse();
        otroClienteResponse.setNombre("John");
        otroClienteResponse.setApellido("Doe");
        otroClienteResponse.setDni("12345678");
        otroClienteResponse.setEmail("john.doe@example.com");
        otroClienteResponse.setTelefono("987654321");
        otroClienteResponse.setDireccion("123 Main St");

        assertTrue(clienteResponse.equals(otroClienteResponse));
    }

    @Test
    public void testGetters() {
        assertEquals("John", clienteResponse.getNombre());
        assertEquals("Doe", clienteResponse.getApellido());
        assertEquals("12345678", clienteResponse.getDni());
        assertEquals("john.doe@example.com", clienteResponse.getEmail());
        assertEquals("987654321", clienteResponse.getTelefono());
        assertEquals("123 Main St", clienteResponse.getDireccion());
    }

    @Test
    public void testSetters() {
        clienteResponse.setNombre("Jane");
        assertEquals("Jane", clienteResponse.getNombre());

        clienteResponse.setApellido("Smith");
        assertEquals("Smith", clienteResponse.getApellido());

        clienteResponse.setDni("87654321");
        assertEquals("87654321", clienteResponse.getDni());

        clienteResponse.setEmail("jane.smith@example.com");
        assertEquals("jane.smith@example.com", clienteResponse.getEmail());

        clienteResponse.setTelefono("123456789");
        assertEquals("123456789", clienteResponse.getTelefono());

        clienteResponse.setDireccion("456 Main St");
        assertEquals("456 Main St", clienteResponse.getDireccion());
    }
}