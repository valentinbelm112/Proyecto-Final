package com.microservicios.cuenta.mockito.controller;


import com.microservicios.cuenta.apidelegate.ActualizarCuentaApiDelegate;
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
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
public class ActualizarCuentaApiDelegateTest {

    @Mock
    private CuentaService cuentaService;

    @InjectMocks
    private ActualizarCuentaApiDelegate actualizarCuentaApiDelegate;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testActualizarCuenta() {
        CuentaResponse cuentaResponse = new CuentaResponse();
        cuentaResponse.setId(1);
        cuentaResponse.setSaldo(200.0);
        when(cuentaService.ActualizaCuenta(anyInt(), any(CuentaRequest.class))).thenReturn(cuentaResponse);

        CuentaRequest cuentaRequest = new CuentaRequest();
        cuentaRequest.setSaldo(200.0);

        ResponseEntity<CuentaResponse> response = actualizarCuentaApiDelegate.actualizarCuenta(1, cuentaRequest);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(200.0, response.getBody().getSaldo());
    }
}
