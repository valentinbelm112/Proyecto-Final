package com.nttdata.mservice_transaccion.mockito;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.nttdata.mservice_transaccion.business.Externo.CuentaServiceClient;
import com.nttdata.mservice_transaccion.model.*;
import com.nttdata.mservice_transaccion.model.dto.CuentaRequest;
import com.nttdata.mservice_transaccion.model.dto.CuentaResponse;
import com.nttdata.mservice_transaccion.repository.TransaccionRepository;
import com.nttdata.mservice_transaccion.business.TransaccionServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

class TransaccionServiceImplTest {

    @Mock
    private CuentaServiceClient cuentaServiceClient;

    @Mock
    private TransaccionRepository transaccionRepository;

    @InjectMocks
    private TransaccionServiceImpl transaccionService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void registrarDeposito_CuentaExistente() {
        // Arrange
        DepositoRequest depositoRequest = new DepositoRequest();
        depositoRequest.setCuentaOrigen("123456789");
        depositoRequest.setMonto(100.0);

        CuentaResponse cuentaResponse = new CuentaResponse();
        cuentaResponse.setId(1L);
        cuentaResponse.setNumeroCuenta("123456789");
        cuentaResponse.setSaldo(500.0);
        cuentaResponse.setEstadoCuenta("ACTIVA");

        Transaccion transaccion = new Transaccion();
        transaccion.setId(String.valueOf(1L));
        transaccion.setCuentaOrigen("123456789");

        when(cuentaServiceClient.obtenerCuentaPorNumero(depositoRequest.getCuentaOrigen()))
                .thenReturn(cuentaResponse);
        when(transaccionRepository.save(any(Transaccion.class))).thenReturn(transaccion);

        // Act
        TransaccionResponse result = transaccionService.registrarDeposito(depositoRequest);

        // Assert
        assertNotNull(result);
        assertEquals("123456789", result.getCuentaOrigen());
        verify(cuentaServiceClient, times(1)).obtenerCuentaPorNumero(depositoRequest.getCuentaOrigen());
        verify(transaccionRepository, times(1)).save(any(Transaccion.class));
        verify(cuentaServiceClient, times(1))
                .ActualizarSaldo(eq(1), any(CuentaRequest.class));
    }


    @Test
    void registrarDeposito_CuentaNoExistente() {
        // Arrange
        DepositoRequest depositoRequest = new DepositoRequest();
        depositoRequest.setCuentaOrigen("123456789");
        depositoRequest.setMonto(100.0);

        when(cuentaServiceClient.obtenerCuentaPorNumero(depositoRequest.getCuentaOrigen()))
                .thenReturn(null);

        // Act & Assert
        NoSuchElementException exception = assertThrows(NoSuchElementException.class,
                () -> transaccionService.registrarDeposito(depositoRequest));
        assertEquals("No se encontró la cuenta con el número proporcionado: 123456789", exception.getMessage());
        verify(cuentaServiceClient, times(1)).obtenerCuentaPorNumero(depositoRequest.getCuentaOrigen());
        verify(transaccionRepository, never()).save(any(Transaccion.class));
    }
    @Test
    void registrarRetiro_CuentaExistenteConSaldoSuficiente() {
        // Arrange
        RetiroRequest retiroRequest = new RetiroRequest();
        retiroRequest.setCuentaOrigen("123456789");
        retiroRequest.setMonto(50.0);

        CuentaResponse cuentaResponse = new CuentaResponse();
        cuentaResponse.setId(1L);
        cuentaResponse.setNumeroCuenta("123456789");
        cuentaResponse.setSaldo(100.0);
        cuentaResponse.setEstadoCuenta("ACTIVA");

        Transaccion transaccion = new Transaccion();
        transaccion.setId(String.valueOf(1L));
        transaccion.setCuentaOrigen("123456789");

        when(cuentaServiceClient.obtenerCuentaPorNumero(retiroRequest.getCuentaOrigen()))
                .thenReturn(cuentaResponse);
        when(transaccionRepository.save(any(Transaccion.class))).thenReturn(transaccion);

        // Act
        TransaccionResponse result = transaccionService.registrarRetiro(retiroRequest);

        // Assert
        assertNotNull(result);
        assertEquals("123456789", result.getCuentaOrigen());
        verify(cuentaServiceClient, times(1)).obtenerCuentaPorNumero(retiroRequest.getCuentaOrigen());
        verify(transaccionRepository, times(1)).save(any(Transaccion.class));
        verify(cuentaServiceClient, times(1)).ActualizarSaldo(eq(1), any(CuentaRequest.class));
    }

    @Test
    void registrarRetiro_SaldoInsuficiente() {
        // Arrange
        RetiroRequest retiroRequest = new RetiroRequest();
        retiroRequest.setCuentaOrigen("123456789");
        retiroRequest.setMonto(200.0);

        CuentaResponse cuentaResponse = new CuentaResponse();
        cuentaResponse.setId(1L);
        cuentaResponse.setNumeroCuenta("123456789");
        cuentaResponse.setSaldo(100.0);
        cuentaResponse.setEstadoCuenta("ACTIVA");

        when(cuentaServiceClient.obtenerCuentaPorNumero(retiroRequest.getCuentaOrigen()))
                .thenReturn(cuentaResponse);

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> transaccionService.registrarRetiro(retiroRequest));
        assertEquals("El monto a retirar excede el saldo disponible.", exception.getMessage());
        verify(cuentaServiceClient, times(1)).obtenerCuentaPorNumero(retiroRequest.getCuentaOrigen());
        verify(transaccionRepository, never()).save(any(Transaccion.class));
    }
    @Test
    void registrarTransferencia_CuentasActivasYSaldoSuficiente() {
        // Arrange
        TransferenciaRequest transferenciaRequest = new TransferenciaRequest();
        transferenciaRequest.setCuentaOrigen("123456789");
        transferenciaRequest.setCuentaDestino("987654321");
        transferenciaRequest.setMonto(50.0);

        CuentaResponse cuentaOrigen = new CuentaResponse();
        cuentaOrigen.setId(1L);
        cuentaOrigen.setNumeroCuenta("123456789");
        cuentaOrigen.setSaldo(100.0);
        cuentaOrigen.setEstadoCuenta("ACTIVA");

        CuentaResponse cuentaDestino = new CuentaResponse();
        cuentaDestino.setId(2L);
        cuentaDestino.setNumeroCuenta("987654321");
        cuentaDestino.setSaldo(200.0);
        cuentaDestino.setEstadoCuenta("ACTIVA");

        Transaccion transaccion = new Transaccion();
        transaccion.setId(String.valueOf(1L));
        transaccion.setCuentaOrigen("123456789");
        transaccion.setCuentaDestino("987654321");

        when(cuentaServiceClient.obtenerCuentaPorNumero(transferenciaRequest.getCuentaOrigen()))
                .thenReturn(cuentaOrigen);
        when(cuentaServiceClient.obtenerCuentaPorNumero(transferenciaRequest.getCuentaDestino()))
                .thenReturn(cuentaDestino);
        when(transaccionRepository.save(any(Transaccion.class))).thenReturn(transaccion);

        // Act
        TransaccionResponse result = transaccionService.registrarTransferencia(transferenciaRequest);

        // Assert
        assertNotNull(result);
        assertEquals("123456789", result.getCuentaOrigen());
        assertEquals("987654321", result.getCuentaDestino());
        verify(cuentaServiceClient, times(1)).obtenerCuentaPorNumero(transferenciaRequest.getCuentaOrigen());
        verify(cuentaServiceClient, times(1)).obtenerCuentaPorNumero(transferenciaRequest.getCuentaDestino());
        verify(transaccionRepository, times(1)).save(any(Transaccion.class));
        verify(cuentaServiceClient, times(2)).ActualizarSaldo(anyInt(), any(CuentaRequest.class));
    }

    @Test
    void consultarHistorialtransacciones() {
        // Arrange
        Transaccion transaccion1 = new Transaccion();
        transaccion1.setId(String.valueOf(1L));
        transaccion1.setCuentaOrigen("123456789");

        Transaccion transaccion2 = new Transaccion();
        transaccion2.setId(String.valueOf(2L));
        transaccion2.setCuentaOrigen("987654321");

        when(transaccionRepository.findAll()).thenReturn(List.of(transaccion1, transaccion2));

        // Act
        List<TransaccionResponse> result = transaccionService.consultarHistorialtransacciones();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        verify(transaccionRepository, times(1)).findAll();
    }
}