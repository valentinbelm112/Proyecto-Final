package com.nttdata.mservice_transaccion.mockito.controller;

import com.nttdata.mservice_transaccion.apidelegate.TransaccionCuentaApiDelegate;
import com.nttdata.mservice_transaccion.business.TransaccionService;
import com.nttdata.mservice_transaccion.model.DepositoRequest;
import com.nttdata.mservice_transaccion.model.RetiroRequest;
import com.nttdata.mservice_transaccion.model.TransaccionResponse;
import com.nttdata.mservice_transaccion.model.TransferenciaRequest;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class TransaccionCuentaApiDelegateTest {

    @Mock
    private TransaccionService transaccionService;

    @InjectMocks
    private TransaccionCuentaApiDelegate transaccionCuentaApiDelegate;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testRegistrarRetiro() {
        TransaccionResponse transaccionResponse = new TransaccionResponse();
        when(transaccionService.registrarRetiro(any(RetiroRequest.class))).thenReturn(transaccionResponse);

        RetiroRequest retiroRequest = new RetiroRequest();
        ResponseEntity<TransaccionResponse> response = transaccionCuentaApiDelegate.registrarRetiro(retiroRequest);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(transaccionResponse, response.getBody());
    }

    @Test
    public void testRegistrarDeposito() {
        TransaccionResponse transaccionResponse = new TransaccionResponse();
        when(transaccionService.registrarDeposito(any(DepositoRequest.class))).thenReturn(transaccionResponse);

        DepositoRequest depositoRequest = new DepositoRequest();
        ResponseEntity<TransaccionResponse> response = transaccionCuentaApiDelegate.registrarDeposito(depositoRequest);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(transaccionResponse, response.getBody());
    }

    @Test
    public void testRegistrarTransferencia() {
        TransaccionResponse transaccionResponse = new TransaccionResponse();
        when(transaccionService.registrarTransferencia(any(TransferenciaRequest.class))).thenReturn(transaccionResponse);

        TransferenciaRequest transferenciaRequest = new TransferenciaRequest();
        ResponseEntity<TransaccionResponse> response = transaccionCuentaApiDelegate.registrarTransferencia(transferenciaRequest);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(transaccionResponse, response.getBody());
    }

    @Test
    public void testConsultarHistorialTransacciones() {
        TransaccionResponse transaccionResponse = new TransaccionResponse();
        List<TransaccionResponse> transaccionResponses = Collections.singletonList(transaccionResponse);
        when(transaccionService.consultarHistorialtransacciones()).thenReturn(transaccionResponses);

        ResponseEntity<List<TransaccionResponse>> response = transaccionCuentaApiDelegate.consultarHistorialTransacciones();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(transaccionResponses, response.getBody());
    }

    @Test
    public void testConsultarHistorialTransacciones_NoContent() {
        when(transaccionService.consultarHistorialtransacciones()).thenReturn(Collections.emptyList());

        ResponseEntity<List<TransaccionResponse>> response = transaccionCuentaApiDelegate.consultarHistorialTransacciones();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(Collections.emptyList(), response.getBody());
    }
}
