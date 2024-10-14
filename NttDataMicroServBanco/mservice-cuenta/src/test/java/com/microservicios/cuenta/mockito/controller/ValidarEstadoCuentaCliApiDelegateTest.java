package com.microservicios.cuenta.mockito.controller;
import com.microservicios.cuenta.apidelegate.ValidarEstadoCuentaCliApiDelegate;
import com.microservicios.cuenta.business.CuentaService;
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
public class ValidarEstadoCuentaCliApiDelegateTest {
    @Mock
    private CuentaService cuentaService;

    @InjectMocks
    private ValidarEstadoCuentaCliApiDelegate validarEstadoCuentaCliApiDelegate;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testVerificarCuentasActivas() {
        when(cuentaService.verificarCuenta(anyLong())).thenReturn(true);

        ResponseEntity<Boolean> response = validarEstadoCuentaCliApiDelegate.verificarCuentasActivas(1);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(true, response.getBody());
    }
}
