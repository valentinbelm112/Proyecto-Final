package com.microservicios.cuenta.mockito.controller;

import com.microservicios.cuenta.apidelegate.ObtenerCuentaNumCuDelegate;
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
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
public class ObtenerCuentaNumCuDelegateTest {

    @Mock
    private CuentaService cuentaService;

    @InjectMocks
    private ObtenerCuentaNumCuDelegate obtenerCuentaNumCuDelegate;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testObtenerCuentaPorNumCuenta() {
        CuentaResponse cuentaResponse = new CuentaResponse();
        cuentaResponse.setNumeroCuenta("12345");
        cuentaResponse.setSaldo(100.0);
        when(cuentaService.obtenerCuentaPorNumCuenta(anyString())).thenReturn(cuentaResponse);

        ResponseEntity<CuentaResponse> response = obtenerCuentaNumCuDelegate.obtenerCuentaPorNumCuenta("12345");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("12345", response.getBody().getNumeroCuenta());
        assertEquals(100.0, response.getBody().getSaldo());
    }

}
