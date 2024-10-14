package com.microservicios.cuenta.business.impl;

import com.microservicios.cuenta.business.CuentaService;
import com.microservicios.cuenta.business.TransaccionStrategy;
import com.microservicios.cuenta.entity.CuentaEntity;
import com.microservicios.cuenta.entity.TipoCuenta;
import com.microservicios.cuenta.entity.TipoEstado;
import com.microservicios.cuenta.mapper.CuentaMapperEntity;
import com.microservicios.cuenta.mapper.CuentaMapperRequest;
import com.microservicios.cuenta.model.CuentaRequest;
import com.microservicios.cuenta.model.CuentaResponse;
import com.microservicios.cuenta.repository.CuentaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;
import static com.microservicios.cuenta.mapper.CuentaMapperEntity.getCuentaArrayEntityofCuentaArrayResponse;
import static com.microservicios.cuenta.util.CuentaUtil.clienteNoEncontrado;
import static com.microservicios.cuenta.util.CuentaUtil.generarNumeroCuentaUnico;


@Service
public class CuentaServiceImpl implements CuentaService {

    @Autowired
    private CuentaRepository cuentaRepository;



    @Autowired
    @Qualifier("retiroStrategy")
    TransaccionStrategy retiroStrategy;

    @Autowired
    @Qualifier("depositoStrategy")
    TransaccionStrategy depositoStrategy;



    @Override
    public CuentaResponse crearCuenta(CuentaRequest cuenta) {
        // Validar que el saldo inicial sea mayor a 0. segun la regla de negocio
        if (cuenta.getSaldo() <= 0) {
            throw new IllegalArgumentException("El saldo inicial debe ser mayor a 0");
        }
        //generar cuenta unico
        String numeroCuentaUnico = generarNumeroCuentaUnico(cuentaRepository);

        cuenta.setNumeroCuenta(numeroCuentaUnico);
        CuentaEntity cuentaEntity= Optional.of(CuentaMapperRequest.getCuentaEntityofCuentaResponse(cuenta))
                .map(cuentaRepository::save)
                .orElseThrow(() -> new IllegalArgumentException("Error al crear la cuenta"));

        return CuentaMapperEntity.getCuentaResponseofCuentaEntity(cuentaEntity);
    }




    @Override
    public CuentaResponse obtenerCuentaPorId(Long id) {
        CuentaEntity cuentaEntity= cuentaRepository.findById(Long.valueOf(id))
                .orElseThrow(clienteNoEncontrado(Long.valueOf(id)));
            return CuentaMapperEntity.getCuentaResponseofCuentaEntity(cuentaEntity);
    }

    @Override
    public List<CuentaResponse> obtenerTodasLasCuentas() {

        List<CuentaEntity> cuentaEntities= cuentaRepository.findAll().stream()
                .filter(cliente -> cliente.getId() != null)
                .collect(Collectors.toList());
       return getCuentaArrayEntityofCuentaArrayResponse(cuentaEntities);
    }

    @Override
    public void eliminarCuenta(Long id) {
        Optional.of(id)
                .map(cuentaRepository::findById)
                .filter(Optional::isPresent)
                .ifPresentOrElse(
                        cuenta -> cuentaRepository.deleteById(id),
                        () -> { throw new IllegalArgumentException("Cuenta no encontrada con ID: " + id); }
                );
    }

    @Override
    public CuentaResponse depositar(Long cuentaId, Double monto) {
        CuentaEntity cuenta = cuentaRepository.findById(Long.valueOf(cuentaId))
                .orElseThrow(() -> new IllegalArgumentException("Cuenta no encontrada con ID: " + cuentaId));
        cuenta.setSaldo(cuenta.getSaldo() + monto);
        depositoStrategy.ejecutarTransaccion(cuenta,monto);

        CuentaResponse cuentaResponse = new CuentaResponse();
        cuentaResponse.setId(Math.toIntExact(cuenta.getId()));
        cuentaResponse.setNumeroCuenta(cuenta.getNumeroCuenta());
        cuentaResponse.setSaldo(cuenta.getSaldo());
        cuentaResponse.setTipoCuenta(CuentaResponse.TipoCuentaEnum.valueOf(cuenta.getTipoCuenta().name()));
        cuentaResponse.setEstadoCuenta(CuentaResponse.EstadoCuentaEnum.valueOf(cuenta.getTipoestado().name()));
        cuentaResponse.setClienteId(Math.toIntExact(cuenta.getClienteId()));

        return cuentaResponse;
    }


    @Override
    public CuentaResponse retirar(Long cuentaId, Double monto) {
        CuentaEntity cuenta = cuentaRepository.findById(Long.valueOf(cuentaId))
                .orElseThrow(() -> new IllegalArgumentException("Cuenta no encontrada con ID: " + cuentaId));
        if (cuenta.getTipoCuenta() == TipoCuenta.AHORROS && cuenta.getSaldo() - monto < 0) {
            throw new IllegalArgumentException("No se puede retirar, saldo insuficiente en cuenta de ahorros.");
        }

        if (cuenta.getTipoCuenta() == TipoCuenta.CORRIENTE && cuenta.getSaldo() - monto < -500) {
            throw new IllegalArgumentException("No se puede retirar, el sobregiro no puede ser mayor a -500.");
        }

        cuenta.setSaldo(cuenta.getSaldo() - monto);
        retiroStrategy.ejecutarTransaccion(cuenta,monto);

        CuentaResponse cuentaResponse = new CuentaResponse();
        cuentaResponse.setId(Math.toIntExact(cuenta.getId()));
        cuentaResponse.setNumeroCuenta(cuenta.getNumeroCuenta());
        cuentaResponse.setSaldo(cuenta.getSaldo());
        cuentaResponse.setTipoCuenta(CuentaResponse.TipoCuentaEnum.valueOf(cuenta.getTipoCuenta().name()));
        cuentaResponse.setEstadoCuenta(CuentaResponse.EstadoCuentaEnum.valueOf(cuenta.getTipoestado().name()));
        cuentaResponse.setClienteId(Math.toIntExact(cuenta.getClienteId()));

        return cuentaResponse;
    }

    @Override
    public boolean verificarCuenta(Long clienteId) {
        return cuentaRepository.existsByClienteIdAndTipoestado(clienteId, TipoEstado.ACTIVA);
    }

    @Override
    public CuentaResponse obtenerCuentaPorNumCuenta(String numCuenta) {
        CuentaEntity cuentaEntity= cuentaRepository.findByNumeroCuenta(numCuenta);
        // Verificar si se encontró la cuenta
        if (cuentaEntity == null) {
            throw new NoSuchElementException("No se encontró la cuenta con el número proporcionado: " + numCuenta);

        }
        return CuentaMapperEntity.getCuentaResponseofCuentaEntity(cuentaEntity);

    }

    @Override
    public CuentaResponse ActualizaCuenta(Integer id, CuentaRequest cuentaRequest) {

        CuentaEntity cuenta = cuentaRepository.findById(Long.valueOf(id)).orElse(null);
        if (cuenta != null) {

            cuenta.setSaldo(cuentaRequest.getSaldo());
           // cuenta.setTipoCuenta(cuentaRequest.getTipoCuenta());
           // cuenta.setTipoestado(cuentaRequest.getEstadoCuenta());
            // Guarda la cuenta actualizada
            CuentaEntity updatedCuenta = cuentaRepository.save(cuenta);
            return CuentaMapperEntity.getCuentaResponseofCuentaEntity(updatedCuenta);
        } else {
            throw new NoSuchElementException("No se encontró la cuenta con el número proporcionado: " + id);
        }
    }
}
