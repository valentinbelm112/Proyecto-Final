package com.nttdata.mservice_transaccion.mockito;

import com.nttdata.mservice_transaccion.exception.CustomErrorResponse;
import com.nttdata.mservice_transaccion.exception.ErrorResponse;
import com.nttdata.mservice_transaccion.exception.GlobalExceptionHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.context.request.WebRequest;

import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class GlobalExceptionHandlerTest {

    @Mock
    private WebRequest webRequest;

    @InjectMocks
    private GlobalExceptionHandler globalExceptionHandler;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testHandleDataIntegrityViolationException() {
        DataIntegrityViolationException ex = new DataIntegrityViolationException("DNI duplicado");
        when(webRequest.getDescription(false)).thenReturn("uri=/api/test");

        ResponseEntity<ErrorResponse> response = globalExceptionHandler.handleDataIntegrityViolationException(ex, webRequest);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("*", response.getBody().getMessage());
    }

    @Test
    public void testHandleIllegalStateException() {
        IllegalStateException ex = new IllegalStateException("Estado ilegal");
        when(webRequest.getDescription(false)).thenReturn("uri=/api/test");

        ResponseEntity<ErrorResponse> response = globalExceptionHandler.handleIllegalStateException(ex, webRequest);

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertEquals("Estado ilegal", response.getBody().getMessage());
    }

    @Test
    public void testHandleValidationExceptions() {
        BindingResult bindingResult = mock(BindingResult.class);
        MethodArgumentNotValidException ex = new MethodArgumentNotValidException(null, bindingResult);
        FieldError fieldError = new FieldError("objectName", "field", "defaultMessage");
        when(bindingResult.getFieldErrors()).thenReturn(Collections.singletonList(fieldError));
        when(webRequest.getDescription(false)).thenReturn("uri=/api/test");

        ResponseEntity<List<CustomErrorResponse>> response = globalExceptionHandler.handleValidationExceptions(ex, webRequest);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        List<CustomErrorResponse> errors = response.getBody();
        assertEquals(1, errors.size());
        assertEquals("defaultMessage", errors.get(0).getMessage());
        assertEquals("field", errors.get(0).getField());
    }

    @Test
    public void testHandleIllegalArgumentException() {
        IllegalArgumentException ex = new IllegalArgumentException("Argumento no válido");
        when(webRequest.getDescription(false)).thenReturn("uri=/api/test");

        ResponseEntity<ErrorResponse> response = globalExceptionHandler.handleIllegalArgumentException(ex, webRequest);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Argumento no válido", response.getBody().getMessage());
    }

    @Test
    public void testHandleNoSuchElementException() {
        NoSuchElementException ex = new NoSuchElementException("Elemento no encontrado");

        ResponseEntity<String> response = globalExceptionHandler.handleNoSuchElementException(ex);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Elemento no encontrado", response.getBody());
    }
}
