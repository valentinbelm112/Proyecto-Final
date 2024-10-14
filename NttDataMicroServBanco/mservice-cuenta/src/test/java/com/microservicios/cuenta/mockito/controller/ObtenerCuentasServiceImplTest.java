package com.microservicios.cuenta.mockito.controller;

import com.microservicios.cuenta.apidelegate.ObtenerCuentasServiceImpl;
import com.microservicios.cuenta.business.CuentaService;
import com.microservicios.cuenta.model.CuentaResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

public class ObtenerCuentasServiceImplTest {

    @Mock
    private CuentaService cuentaService;

    @InjectMocks
    private ObtenerCuentasServiceImpl obtenerCuentasServiceImpl;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testListarCuentas() {
        CuentaResponse cuentaResponse = new CuentaResponse();
        cuentaResponse.setId(1);
        cuentaResponse.setSaldo(100.0);
        List<CuentaResponse> cuentaResponses = Collections.singletonList(cuentaResponse);
        when(cuentaService.obtenerTodasLasCuentas()).thenReturn(cuentaResponses);

        ResponseEntity<List<CuentaResponse>> response = obtenerCuentasServiceImpl.listarCuentas();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
        assertEquals(100.0, response.getBody().get(0).getSaldo());
    }

    @Test
    public void testListarCuentas_NoContent() {
        when(cuentaService.obtenerTodasLasCuentas()).thenReturn(Collections.emptyList());

        ResponseEntity<List<CuentaResponse>> response = obtenerCuentasServiceImpl.listarCuentas();

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }
}