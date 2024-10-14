package com.microservicios.cliente.mockito.controller;

import com.microservicios.cliente.api.CrearApiController;
import com.microservicios.cliente.api.CrearApiDelegate;
import com.microservicios.cliente.model.ClienteRequest;
import com.microservicios.cliente.model.ClienteResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class CrearApiControllerTest {

    @InjectMocks
    private CrearApiController crearApiController;

    @Mock
    private CrearApiDelegate crearApiDelegate;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);  // Inicia los mocks
    }

    @Test
    public void testConstructor_NullDelegate() {
        // Crea un controlador con un delegate nulo
        CrearApiController controller = new CrearApiController(null);
        assertNotNull(controller.getDelegate());  // Verifica que el delegate no sea nulo
    }

    @Test
    public void testGetDelegate() {
        // Verifica que el delegate inyectado sea el correcto
        assertEquals(crearApiDelegate, crearApiController.getDelegate());
    }

    @Test
    public void testCrearCliente() {

        // Inicializa ClienteRequest usando setters
        ClienteRequest clienteRequest = new ClienteRequest();
        clienteRequest.setNombre("Juan");
        clienteRequest.setApellido("Pérez");
        clienteRequest.setDni("71838725");
        clienteRequest.setEmail("juanpere@gmail.com");
        clienteRequest.setTelefono("987654321");
        clienteRequest.setDireccion("Av. Siempre Viva 742");

        // Inicializa ClienteResponse usando setters
        ClienteResponse clienteResponse = new ClienteResponse();
        clienteResponse.setId(1);
        clienteResponse.setNombre("Juan");
        clienteResponse.setApellido("Pérez");
        clienteResponse.setDni("71838725");
        clienteResponse.setEmail("juanpere@gmail.com");
        clienteResponse.setTelefono("987654321");
        clienteResponse.setDireccion("Av. Siempre Viva 742");

        when(crearApiDelegate.crearCliente(any(ClienteRequest.class)))
                .thenReturn(ResponseEntity.ok(clienteResponse));

        // Aquí invocarías el método de tu controlador que llama a crearApiDelegate.crearCliente()
        ResponseEntity<?> response = crearApiController.crearCliente(new ClienteRequest()); // Suponiendo que este es el método a probar

        // Verificaciones
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(clienteResponse, response.getBody());
        verify(crearApiDelegate).crearCliente(any(ClienteRequest.class));  // Verifica que se llamó al método del delegate
    }


}
