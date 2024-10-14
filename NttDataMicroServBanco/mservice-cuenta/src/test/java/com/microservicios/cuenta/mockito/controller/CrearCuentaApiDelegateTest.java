package com.microservicios.cuenta.mockito.controller;


import com.microservicios.cuenta.apidelegate.CrearCuentaApiDelegate;
import com.microservicios.cuenta.business.CuentaService;
import com.microservicios.cuenta.model.CuentaRequest;
import com.microservicios.cuenta.model.CuentaResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
public class CrearCuentaApiDelegateTest {
    @Mock
    private CuentaService cuentaService;

    @InjectMocks
    private CrearCuentaApiDelegate crearCuentaApiDelegate;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCrearCuenta() {
        CuentaResponse cuentaResponse = new CuentaResponse();
        cuentaResponse.setId(1);
        cuentaResponse.setSaldo(100.0);
        when(cuentaService.crearCuenta(any(CuentaRequest.class))).thenReturn(cuentaResponse);

        CuentaRequest cuentaRequest = new CuentaRequest();
        cuentaRequest.setSaldo(100.0);

        ResponseEntity<CuentaResponse> response = crearCuentaApiDelegate.crearCuenta(cuentaRequest);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(100.0, response.getBody().getSaldo());
    }
}
