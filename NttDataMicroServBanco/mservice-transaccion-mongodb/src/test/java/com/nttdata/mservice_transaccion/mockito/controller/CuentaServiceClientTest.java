package com.nttdata.mservice_transaccion.mockito.controller;
import com.nttdata.mservice_transaccion.business.Externo.CuentaServiceClient;
import com.nttdata.mservice_transaccion.model.dto.CuentaRequest;
import com.nttdata.mservice_transaccion.model.dto.CuentaResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
public class CuentaServiceClientTest {
    @Mock
    private WebClient.Builder webClientBuilder;

    @Mock
    private WebClient webClient;

    @Mock
    private WebClient.RequestHeadersUriSpec requestHeadersUriSpec;

    @Mock
    private WebClient.RequestHeadersSpec requestHeadersSpec;

    @Mock
    private WebClient.RequestBodyUriSpec requestBodyUriSpec;

    @Mock
    private WebClient.RequestBodySpec requestBodySpec;

    @Mock
    private WebClient.ResponseSpec responseSpec;

    private CuentaServiceClient cuentaServiceClient;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        when(webClientBuilder.baseUrl(anyString())).thenReturn(webClientBuilder);
        when(webClientBuilder.build()).thenReturn(webClient);
        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        when(webClient.put()).thenReturn(requestBodyUriSpec);
        when(requestHeadersUriSpec.uri(anyString(), anyString())).thenReturn(requestHeadersSpec);
        when(requestBodyUriSpec.uri(anyString(), anyInt())).thenReturn(requestBodySpec);
        when(requestBodySpec.bodyValue(any())).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);

        cuentaServiceClient = new CuentaServiceClient(webClientBuilder);
    }

    @Test
    public void testObtenerCuentaPorNumero() {
        CuentaResponse cuentaResponse = new CuentaResponse();
        when(responseSpec.bodyToMono(CuentaResponse.class)).thenReturn(Mono.just(cuentaResponse));

        CuentaResponse result = cuentaServiceClient.obtenerCuentaPorNumero("12345");

        assertEquals(cuentaResponse, result);
        verify(requestHeadersUriSpec, times(1)).uri("/api/cuentas/numero-cuenta/{numeroCuenta}", "12345");
        verify(responseSpec, times(1)).bodyToMono(CuentaResponse.class);
    }

    @Test
    public void testObtenerCuentaPorNumero_Error() {
        when(responseSpec.bodyToMono(CuentaResponse.class)).thenReturn(Mono.error(new WebClientResponseException(500, "Internal Server Error", null, null, null)));

        CuentaResponse result = cuentaServiceClient.obtenerCuentaPorNumero("12345");

        assertNull(result);
        verify(requestHeadersUriSpec, times(1)).uri("/api/cuentas/numero-cuenta/{numeroCuenta}", "12345");
        verify(responseSpec, times(1)).bodyToMono(CuentaResponse.class);
    }

    @Test
    public void testActualizarSaldo() {
        CuentaResponse cuentaResponse = new CuentaResponse();
        when(responseSpec.bodyToMono(CuentaResponse.class)).thenReturn(Mono.just(cuentaResponse));

        CuentaRequest cuentaRequest = new CuentaRequest();
        CuentaResponse result = cuentaServiceClient.ActualizarSaldo(1, cuentaRequest);

        assertEquals(cuentaResponse, result);
        verify(requestBodyUriSpec, times(1)).uri("/api/cuentas/actualizar/{id}", 1);
        verify(requestBodySpec, times(1)).bodyValue(cuentaRequest);
        verify(responseSpec, times(1)).bodyToMono(CuentaResponse.class);
    }

    @Test
    public void testActualizarSaldo_Error() {
        when(responseSpec.bodyToMono(CuentaResponse.class)).thenReturn(Mono.error(new WebClientResponseException(500, "Internal Server Error", null, null, null)));

        CuentaRequest cuentaRequest = new CuentaRequest();
        CuentaResponse result = cuentaServiceClient.ActualizarSaldo(1, cuentaRequest);

        assertNull(result);
        verify(requestBodyUriSpec, times(1)).uri("/api/cuentas/actualizar/{id}", 1);
        verify(requestBodySpec, times(1)).bodyValue(cuentaRequest);
        verify(responseSpec, times(1)).bodyToMono(CuentaResponse.class);
    }
}
