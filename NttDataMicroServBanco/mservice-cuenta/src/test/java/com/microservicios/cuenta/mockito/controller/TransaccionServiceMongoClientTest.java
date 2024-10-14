package com.microservicios.cuenta.mockito.controller;

import com.microservicios.cuenta.business.Externo.TransaccionServiceMongoClient;
import com.microservicios.cuenta.entity.model.Deposito;
import com.microservicios.cuenta.entity.model.DepositoMongo;
import com.microservicios.cuenta.entity.model.Retiro;
import com.microservicios.cuenta.entity.model.RetiroMongo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
public class TransaccionServiceMongoClientTest {
    @Mock
    private WebClient.Builder webClientBuilder;

    @Mock
    private WebClient webClient;

    @Mock
    private WebClient.RequestBodyUriSpec requestBodyUriSpec;

    @Mock
    private WebClient.RequestBodySpec requestBodySpec;

    @Mock
    private WebClient.RequestHeadersSpec requestHeadersSpec;

    @Mock
    private WebClient.ResponseSpec responseSpec;

    private TransaccionServiceMongoClient transaccionServiceMongoClient;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        when(webClientBuilder.baseUrl(any(String.class))).thenReturn(webClientBuilder);
        when(webClientBuilder.build()).thenReturn(webClient);
        when(webClient.post()).thenReturn(requestBodyUriSpec);
        when(requestBodyUriSpec.uri(any(String.class))).thenReturn(requestBodySpec);
        when(requestBodySpec.bodyValue(any())).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);

        transaccionServiceMongoClient = new TransaccionServiceMongoClient(webClientBuilder);
    }

    @Test
    public void testCrearTransaccionRetiroEnMicroservicio() {
        RetiroMongo retiro = new RetiroMongo();
        when(responseSpec.bodyToMono(Retiro.class)).thenReturn(Mono.just(new Retiro()));

        transaccionServiceMongoClient.crearTransaccionRetiroEnMicroservicio(retiro);

        verify(requestBodyUriSpec, times(1)).uri("/api/mongo/transacciones/retiro");
        verify(requestBodySpec, times(1)).bodyValue(retiro);
        verify(responseSpec, times(1)).bodyToMono(Retiro.class);
    }

    @Test
    public void testCrearTransaccionRetiroEnMicroservicio_Error() {
        RetiroMongo retiro = new RetiroMongo();
        when(responseSpec.bodyToMono(Retiro.class)).thenReturn(Mono.error(new WebClientResponseException(500, "Internal Server Error", null, null, null)));

        transaccionServiceMongoClient.crearTransaccionRetiroEnMicroservicio(retiro);

        verify(requestBodyUriSpec, times(1)).uri("/api/mongo/transacciones/retiro");
        verify(requestBodySpec, times(1)).bodyValue(retiro);
        verify(responseSpec, times(1)).bodyToMono(Retiro.class);
    }

    @Test
    public void testCrearTransaccionDepositoEnMicroservicio() {
        DepositoMongo deposito = new DepositoMongo();
        when(responseSpec.bodyToMono(Deposito.class)).thenReturn(Mono.just(new Deposito()));

        transaccionServiceMongoClient.crearTransaccionDepositoEnMicroservicio(deposito);

        verify(requestBodyUriSpec, times(1)).uri("/api/mongo/transacciones/deposito");
        verify(requestBodySpec, times(1)).bodyValue(deposito);
        verify(responseSpec, times(1)).bodyToMono(Deposito.class);
    }

    @Test
    public void testCrearTransaccionDepositoEnMicroservicio_Error() {
        DepositoMongo deposito = new DepositoMongo();
        when(responseSpec.bodyToMono(Deposito.class)).thenReturn(Mono.error(new WebClientResponseException(500, "Internal Server Error", null, null, null)));

        transaccionServiceMongoClient.crearTransaccionDepositoEnMicroservicio(deposito);

        verify(requestBodyUriSpec, times(1)).uri("/api/mongo/transacciones/deposito");
        verify(requestBodySpec, times(1)).bodyValue(deposito);
        verify(responseSpec, times(1)).bodyToMono(Deposito.class);
    }
}
