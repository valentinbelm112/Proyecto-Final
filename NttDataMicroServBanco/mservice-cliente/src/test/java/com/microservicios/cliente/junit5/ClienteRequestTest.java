package com.microservicios.cliente.junit5;

import com.microservicios.cliente.model.ClienteRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ClienteRequestTest {
    private ClienteRequest cliente;

    @BeforeEach
    public void setUp() {
        cliente = new ClienteRequest();
        cliente.setNombre("John");
        cliente.setApellido("Doe");
        cliente.setDni("12345678");
        cliente.setEmail("john.doe@example.com");
        cliente.setTelefono("987654321");
        cliente.setDireccion("123 Main St");
    }


    @Test
    public void testEqualsAndHashCode() {
        ClienteRequest otroCliente = new ClienteRequest();
        otroCliente.setNombre("John");
        otroCliente.setApellido("Doe");
        otroCliente.setDni("12345678");
        otroCliente.setEmail("john.doe@example.com");
        otroCliente.setTelefono("987654321");
        otroCliente.setDireccion("123 Main St");

        assertEquals(cliente, otroCliente);
        assertEquals(cliente.hashCode(), otroCliente.hashCode());
    }

    @Test
    public void testSettersAndGetters() {
        cliente.setNombre("Jane");
        assertEquals("Jane", cliente.getNombre());

        cliente.setApellido("Smith");
        assertEquals("Smith", cliente.getApellido());

        // Realiza pruebas para el resto de los setters y getters...
    }
}
