package com.microservicios.cuenta.business.impl;

import com.microservicios.cuenta.business.Externo.TransaccionServiceClient;
import com.microservicios.cuenta.business.Externo.TransaccionServiceMongoClient;
import com.microservicios.cuenta.business.TransaccionStrategy;
import com.microservicios.cuenta.entity.CuentaEntity;
import com.microservicios.cuenta.entity.model.Deposito;
import com.microservicios.cuenta.entity.model.DepositoMongo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DepositoStrategy implements TransaccionStrategy {
    @Autowired
    TransaccionServiceClient transaccionServiceClient;
    @Autowired
    TransaccionServiceMongoClient transaccionServiceMongoClient;

    @Override
    public void ejecutarTransaccion(CuentaEntity cuenta, Double monto) {
        //cuentaRepository.save(cuenta);

        // Llamar al microservicio de transacciones mysql
        Deposito deposito = new Deposito();
        deposito.setMonto(monto);
        deposito.setTipo("deposito");
        deposito.setNumCuenta(cuenta.getNumeroCuenta());
        deposito.setOdeposito("33453454354545");
        deposito.setNumTransaccion(transaccionServiceClient.obtenerTransaccion());

        transaccionServiceClient.crearTransaccionEnMicroservicio(deposito);

        // Llamar al microservicio de transacciones mongo
        DepositoMongo depositoRequest = new DepositoMongo();
        depositoRequest.setMonto(monto);
        depositoRequest.setCuentaOrigen(cuenta.getNumeroCuenta());

        transaccionServiceMongoClient.crearTransaccionDepositoEnMicroservicio(depositoRequest);

    }
}
