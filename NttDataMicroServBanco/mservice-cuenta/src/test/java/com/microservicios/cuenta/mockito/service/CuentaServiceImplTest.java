package com.microservicios.cuenta.mockito.service;
import com.microservicios.cuenta.business.TransaccionStrategy;
import com.microservicios.cuenta.business.impl.CuentaServiceImpl;
import com.microservicios.cuenta.entity.CuentaEntity;
import com.microservicios.cuenta.entity.TipoCuenta;
import com.microservicios.cuenta.entity.TipoEstado;
import com.microservicios.cuenta.model.CuentaRequest;
import com.microservicios.cuenta.model.CuentaResponse;
import com.microservicios.cuenta.repository.CuentaRepository;
import com.microservicios.cuenta.business.Externo.TransaccionServiceClient;
import com.microservicios.cuenta.business.Externo.TransaccionServiceMongoClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CuentaServiceImplTest {

    @InjectMocks
    private CuentaServiceImpl cuentaService;

    @Mock
    private CuentaRepository cuentaRepository;

    @Mock
    private TransaccionServiceClient transaccionServiceClient;


    @Mock
    private TransaccionStrategy transaccionStrategy;

    private CuentaEntity cuentaEntity;

    @BeforeEach
    public void setUp() {
        cuentaEntity = new CuentaEntity();
        cuentaEntity.setId(1L);
        cuentaEntity.setSaldo(100.0);
        cuentaEntity.setNumeroCuenta("123456789");
        cuentaEntity.setTipoCuenta(TipoCuenta.AHORROS);
        cuentaEntity.setTipoestado(TipoEstado.ACTIVA);
        cuentaEntity.setClienteId(2L);
    }

    @Test
    public void testCrearCuenta_ConSaldoPositivo() {
        CuentaRequest cuentaRequest = new CuentaRequest();
        cuentaRequest.setSaldo(100.0);
        cuentaRequest.setNumeroCuenta("123456789");
        cuentaRequest.setClienteId(2);
        cuentaRequest.setTipoCuenta(CuentaRequest.TipoCuentaEnum.CORRIENTE);
        cuentaRequest.setEstadoCuenta(CuentaRequest.EstadoCuentaEnum.ACTIVA);

        when(cuentaRepository.save(any(CuentaEntity.class))).thenReturn(cuentaEntity);

        CuentaResponse response = cuentaService.crearCuenta(cuentaRequest);

        assertNotNull(response);
        assertEquals("123456789", response.getNumeroCuenta());
        assertEquals(100.0, response.getSaldo());
    }

    @Test
    public void testCrearCuenta_SaldoNegativo_LanzaExcepcion() {
        CuentaRequest cuentaRequest = new CuentaRequest();
        cuentaRequest.setSaldo(-100.0);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            cuentaService.crearCuenta(cuentaRequest);
        });

        assertEquals("El saldo inicial debe ser mayor a 0", exception.getMessage());
    }

    @Test
    public void testObtenerCuentaPorId_CuentaExistente() {
        when(cuentaRepository.findById(1L)).thenReturn(Optional.of(cuentaEntity));

        CuentaResponse response = cuentaService.obtenerCuentaPorId(1L);

        assertNotNull(response);
        assertEquals("123456789", response.getNumeroCuenta());
    }





    @Test
    public void testRetirar_CuentaAhorros_SaldoInsuficiente_LanzaExcepcion() {
        cuentaEntity.setSaldo(30.0);
        when(cuentaRepository.findById(1L)).thenReturn(Optional.of(cuentaEntity));
        double monto = 50.0;

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            cuentaService.retirar(1L, monto);
        });

        assertEquals("No se puede retirar, saldo insuficiente en cuenta de ahorros.", exception.getMessage());
    }

  // Otros métodos de prueba se pueden agregar de manera similar
}