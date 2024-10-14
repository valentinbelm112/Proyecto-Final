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
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Qualifier;

import java.util.Arrays;
import java.util.List;
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
    @Qualifier("depositoStrategy")
    private TransaccionStrategy depositoStrategy;

    @Mock
    @Qualifier("retiroStrategy")
    private TransaccionStrategy retiroStrategy;

    private CuentaEntity cuentaEntity;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
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


    @Test
    public void testEliminarCuenta() {
        CuentaEntity cuentaEntity = new CuentaEntity();
        cuentaEntity.setId(1L);
        when(cuentaRepository.findById(1L)).thenReturn(Optional.of(cuentaEntity));

        cuentaService.eliminarCuenta(1L);

        verify(cuentaRepository, times(1)).deleteById(1L);
    }

    @Test
    public void testObtenerTodasLasCuentas() {
        CuentaEntity cuentaEntity1 = new CuentaEntity();
        cuentaEntity1.setId(1L);
        cuentaEntity1.setTipoCuenta(TipoCuenta.CORRIENTE);
        cuentaEntity1.setTipoestado(TipoEstado.ACTIVA);
        cuentaEntity1.setClienteId(3L);
        CuentaEntity cuentaEntity2 = new CuentaEntity();
        cuentaEntity2.setId(2L);
        cuentaEntity2.setTipoCuenta(TipoCuenta.CORRIENTE);
        cuentaEntity2.setTipoestado(TipoEstado.ACTIVA);
        cuentaEntity2.setClienteId(4L);
        when(cuentaRepository.findAll()).thenReturn(Arrays.asList(cuentaEntity1, cuentaEntity2));

        List<CuentaResponse> cuentas = cuentaService.obtenerTodasLasCuentas();

        assertNotNull(cuentas);
        assertEquals(2, cuentas.size());
    }

    @Test
    public void testDepositar() {
        CuentaEntity cuentaEntity = new CuentaEntity();
        cuentaEntity.setId(1L);
        cuentaEntity.setSaldo(100.0);
        cuentaEntity.setTipoCuenta(TipoCuenta.CORRIENTE);
        cuentaEntity.setTipoestado(TipoEstado.ACTIVA);
        cuentaEntity.setClienteId(2L);
        when(cuentaRepository.findById(1L)).thenReturn(Optional.of(cuentaEntity));

        CuentaResponse cuentaResponse = cuentaService.depositar(1L, 50.0);

        assertNotNull(cuentaResponse);
        assertEquals(150.0, cuentaResponse.getSaldo());
        verify(depositoStrategy, times(1)).ejecutarTransaccion(cuentaEntity, 50.0);
    }
    @Test
    public void testRetirar() {
        CuentaEntity cuentaEntity = new CuentaEntity();
        cuentaEntity.setId(1L);
        cuentaEntity.setSaldo(100.0);
        cuentaEntity.setTipoCuenta(TipoCuenta.CORRIENTE);
        cuentaEntity.setTipoestado(TipoEstado.ACTIVA);
        cuentaEntity.setClienteId(2L);
        when(cuentaRepository.findById(1L)).thenReturn(Optional.of(cuentaEntity));

        CuentaResponse cuentaResponse = cuentaService.retirar(1L, 50.0);

        assertNotNull(cuentaResponse);
        assertEquals(50.0, cuentaResponse.getSaldo());
        verify(retiroStrategy, times(1)).ejecutarTransaccion(cuentaEntity, 50.0);
    }

    @Test
    public void testVerificarCuenta() {
        when(cuentaRepository.existsByClienteIdAndTipoestado(1L, TipoEstado.ACTIVA)).thenReturn(true);

        boolean result = cuentaService.verificarCuenta(1L);

        assertTrue(result);
    }

    @Test
    public void testObtenerCuentaPorNumCuenta() {
        CuentaEntity cuentaEntity = new CuentaEntity();
        cuentaEntity.setId(1L);
        cuentaEntity.setNumeroCuenta("12345");
        cuentaEntity.setTipoCuenta(TipoCuenta.CORRIENTE);
        cuentaEntity.setTipoestado(TipoEstado.ACTIVA);
        cuentaEntity.setClienteId(2L);
        when(cuentaRepository.findByNumeroCuenta("12345")).thenReturn(cuentaEntity);

        CuentaResponse cuentaResponse = cuentaService.obtenerCuentaPorNumCuenta("12345");

        assertNotNull(cuentaResponse);
        assertEquals("12345", cuentaResponse.getNumeroCuenta());
    }

    @Test
    public void testActualizaCuenta() {
        CuentaEntity cuentaEntity = new CuentaEntity();
        cuentaEntity.setId(1L);
        cuentaEntity.setSaldo(100.0);
        cuentaEntity.setTipoCuenta(TipoCuenta.CORRIENTE);
        cuentaEntity.setTipoestado(TipoEstado.ACTIVA);
        cuentaEntity.setClienteId(2L);
        when(cuentaRepository.findById(1L)).thenReturn(Optional.of(cuentaEntity));

        CuentaRequest cuentaRequest = new CuentaRequest();
        cuentaRequest.setSaldo(200.0);

        when(cuentaRepository.save(any(CuentaEntity.class))).thenReturn(cuentaEntity);

        CuentaResponse cuentaResponse = cuentaService.ActualizaCuenta(1, cuentaRequest);

        assertNotNull(cuentaResponse);
        assertEquals(200.0, cuentaResponse.getSaldo());
    }

    // Otros métodos de prueba se pueden agregar de manera similar
}