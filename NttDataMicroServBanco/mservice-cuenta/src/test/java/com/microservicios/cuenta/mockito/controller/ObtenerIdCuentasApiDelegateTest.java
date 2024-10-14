package com.microservicios.cuenta.mockito.controller;


import com.microservicios.cuenta.apidelegate.ObtenerIdCuentasApiDelegate;
import com.microservicios.cuenta.business.CuentaService;
import com.microservicios.cuenta.model.CuentaResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
public class ObtenerIdCuentasApiDelegateTest {
    @Mock
    private CuentaService cuentaService;

    @InjectMocks
    private ObtenerIdCuentasApiDelegate obtenerIdCuentasApiDelegate;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testObtenerCuentaPorId() {
        CuentaResponse cuentaResponse = new CuentaResponse();
        cuentaResponse.setId(1);
        cuentaResponse.setSaldo(100.0);
        when(cuentaService.obtenerCuentaPorId(anyLong())).thenReturn(cuentaResponse);

        ResponseEntity<CuentaResponse> response = obtenerIdCuentasApiDelegate.obtenerCuentaPorId(1);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(100.0, response.getBody().getSaldo());
    }
}
