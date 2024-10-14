package com.microservicios.cuenta.mockito.controller;


import com.microservicios.cuenta.business.Externo.TransaccionServiceClient;
import com.microservicios.cuenta.entity.model.Deposito;
import com.microservicios.cuenta.entity.model.Retiro;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

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
    private WebClient.RequestBodyUriSpec requestBodyUriSpec;

    @Mock
    private WebClient.RequestBodySpec requestBodySpec;

    @Mock
    private WebClient.ResponseSpec responseSpec;

    private TransaccionServiceClient transaccionServiceClient;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        when(webClientBuilder.baseUrl(any(String.class))).thenReturn(webClientBuilder);
        when(webClientBuilder.build()).thenReturn(webClient);
        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        when(webClient.post()).thenReturn(requestBodyUriSpec);
        when(requestHeadersUriSpec.uri(any(String.class))).thenReturn(requestHeadersSpec);
        when(requestBodyUriSpec.uri(any(String.class))).thenReturn(requestBodySpec);
        when(requestBodySpec.bodyValue(any())).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);

        transaccionServiceClient = new TransaccionServiceClient(webClientBuilder);
    }

    @Test
    public void testObtenerTransaccion() {
        when(responseSpec.bodyToMono(String.class)).thenReturn(Mono.just("transaccion123"));

        String result = transaccionServiceClient.obtenerTransaccion();

        assertEquals("transaccion123", result);
        verify(requestHeadersUriSpec, times(1)).uri("/api/transacciones/obtener/numtransaccion");
        verify(responseSpec, times(1)).bodyToMono(String.class);
    }

    @Test
    public void testObtenerTransaccion_Error() {
        when(responseSpec.bodyToMono(String.class)).thenReturn(Mono.error(new WebClientResponseException(500, "Internal Server Error", null, null, null)));

        String result = transaccionServiceClient.obtenerTransaccion();

        assertNull(result);
        verify(requestHeadersUriSpec, times(1)).uri("/api/transacciones/obtener/numtransaccion");
        verify(responseSpec, times(1)).bodyToMono(String.class);
    }

    @Test
    public void testCrearTransaccionEnMicroservicio() {
        Deposito deposito = new Deposito();
        when(responseSpec.bodyToMono(Deposito.class)).thenReturn(Mono.just(deposito));

        transaccionServiceClient.crearTransaccionEnMicroservicio(deposito);

        verify(requestBodyUriSpec, times(1)).uri("/api/transacciones/crear");
        verify(requestBodySpec, times(1)).bodyValue(deposito);
        verify(responseSpec, times(1)).bodyToMono(Deposito.class);
    }

    @Test
    public void testCrearTransaccionEnMicroservicio_Error() {
        Deposito deposito = new Deposito();
        when(responseSpec.bodyToMono(Deposito.class)).thenReturn(Mono.error(new WebClientResponseException(500, "Internal Server Error", null, null, null)));

        transaccionServiceClient.crearTransaccionEnMicroservicio(deposito);

        verify(requestBodyUriSpec, times(1)).uri("/api/transacciones/crear");
        verify(requestBodySpec, times(1)).bodyValue(deposito);
        verify(responseSpec, times(1)).bodyToMono(Deposito.class);
    }

    @Test
    public void testCrearTransaccionRetiroEnMicroservicio() {
        Retiro retiro = new Retiro();
        when(responseSpec.bodyToMono(Deposito.class)).thenReturn(Mono.just(new Deposito()));

        transaccionServiceClient.crearTransaccionRetiroEnMicroservicio(retiro);

        verify(requestBodyUriSpec, times(1)).uri("/api/transacciones/crear");
        verify(requestBodySpec, times(1)).bodyValue(retiro);
        verify(responseSpec, times(1)).bodyToMono(Deposito.class);
    }

    @Test
    public void testCrearTransaccionRetiroEnMicroservicio_Error() {
        Retiro retiro = new Retiro();
        when(responseSpec.bodyToMono(Deposito.class)).thenReturn(Mono.error(new WebClientResponseException(500, "Internal Server Error", null, null, null)));

        transaccionServiceClient.crearTransaccionRetiroEnMicroservicio(retiro);

        verify(requestBodyUriSpec, times(1)).uri("/api/transacciones/crear");
        verify(requestBodySpec, times(1)).bodyValue(retiro);
        verify(responseSpec, times(1)).bodyToMono(Deposito.class);
    }
}
