package com.microservicios.cliente.junit5;
import static org.junit.jupiter.api.Assertions.*;

import com.microservicios.cliente.exception.ErrorResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
public class ErrorResponseTest {
    private ErrorResponse errorResponse;

    @BeforeEach
    public void setUp() {
        // Configura un ErrorResponse antes de cada prueba
        errorResponse = new ErrorResponse("Error occurred", "Details of the error");
    }

    @Test
    public void testConstructor() {
        assertEquals("Error occurred", errorResponse.getMessage());
        assertEquals("Details of the error", errorResponse.getDetails());
        assertNotNull(errorResponse.getTimestamp()); // Verifica que el timestamp no sea null
    }

    @Test
    public void testSetMessage() {
        errorResponse.setMessage("New error message");
        assertEquals("New error message", errorResponse.getMessage());
    }

    @Test
    public void testSetTimestamp() {
        LocalDateTime newTimestamp = LocalDateTime.now();
        errorResponse.setTimestamp(newTimestamp);
        assertEquals(newTimestamp, errorResponse.getTimestamp());
    }

    @Test
    public void testSetDetails() {
        errorResponse.setDetails("New error details");
        assertEquals("New error details", errorResponse.getDetails());
    }

    @Test
    public void testGetMessage() {
        assertEquals("Error occurred", errorResponse.getMessage());
    }

    @Test
    public void testGetTimestamp() {
        assertNotNull(errorResponse.getTimestamp()); // Verifica que el timestamp no sea null
    }

    @Test
    public void testGetDetails() {
        assertEquals("Details of the error", errorResponse.getDetails());
    }
}
