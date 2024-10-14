package com.microservicios.cliente.mockito.service;

import com.microservicios.cliente.business.Externo.TransaccionServiceClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class TransaccionServiceClientTest {

    @Mock
    private WebClient.Builder webClientBuilder;

    @Mock
    private WebClient webClient;

    @Mock
    private WebClient.RequestHeadersUriSpec requestHeadersUriSpec;

    @Mock
    private WebClient.RequestHeadersSpec requestHeadersSpec;

    @Mock
    private WebClient.ResponseSpec responseSpec;

    private TransaccionServiceClient transaccionServiceClient;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        when(webClientBuilder.baseUrl(any(String.class))).thenReturn(webClientBuilder);
        when(webClientBuilder.build()).thenReturn(webClient);
        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(any(String.class), any(Long.class))).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);

        // Proporciona la instancia de WebClient.Builder al constructor
        transaccionServiceClient = new TransaccionServiceClient(webClientBuilder);
    }

    @Test
    public void testValidarCuentaActiva_Success() {
        when(responseSpec.bodyToMono(boolean.class)).thenReturn(Mono.just(true));

        boolean result = transaccionServiceClient.validarCuentaActiva(1L);

        assertTrue(result);
    }

    @Test
    public void testValidarCuentaActiva_Failure() {
        when(responseSpec.bodyToMono(boolean.class)).thenReturn(Mono.just(false));

        boolean result = transaccionServiceClient.validarCuentaActiva(1L);

        assertFalse(result);
    }

    @Test
    public void testValidarCuentaActiva_Exception() {
        when(responseSpec.bodyToMono(boolean.class)).thenThrow(RuntimeException.class);

        boolean result = transaccionServiceClient.validarCuentaActiva(1L);

        assertTrue(result); // Según tu implementación, debería devolver true en caso de excepción
    }
}