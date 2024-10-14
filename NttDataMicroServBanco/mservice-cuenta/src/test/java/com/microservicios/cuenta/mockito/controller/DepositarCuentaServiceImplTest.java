package com.microservicios.cuenta.mockito.controller;

import com.microservicios.cuenta.apidelegate.DepositarCuentaServiceImpl;
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
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.Mockito.when;
public class DepositarCuentaServiceImplTest {
    @Mock
    private CuentaService cuentaService;

    @InjectMocks
    private DepositarCuentaServiceImpl depositarCuentaServiceImpl;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testDepositar() {
        CuentaResponse cuentaResponse = new CuentaResponse();
        cuentaResponse.setId(1);
        cuentaResponse.setSaldo(200.0);
        when(cuentaService.depositar(anyLong(), anyDouble())).thenReturn(cuentaResponse);

        ResponseEntity<CuentaResponse> response = depositarCuentaServiceImpl.depositar(1, 100.0);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(200.0, response.getBody().getSaldo());
    }

    @Test
    public void testRetirar() {
        CuentaResponse cuentaResponse = new CuentaResponse();
        cuentaResponse.setId(1);
        cuentaResponse.setSaldo(50.0);
        when(cuentaService.retirar(anyLong(), anyDouble())).thenReturn(cuentaResponse);

        ResponseEntity<CuentaResponse> response = depositarCuentaServiceImpl.retirar(1, 50.0);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(50.0, response.getBody().getSaldo());
    }
}
