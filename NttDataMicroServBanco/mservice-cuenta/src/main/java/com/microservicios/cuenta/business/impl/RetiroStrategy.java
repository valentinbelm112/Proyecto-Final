package com.microservicios.cuenta.business.impl;

import com.microservicios.cuenta.business.Externo.TransaccionServiceClient;
import com.microservicios.cuenta.business.Externo.TransaccionServiceMongoClient;
import com.microservicios.cuenta.business.TransaccionStrategy;
import com.microservicios.cuenta.entity.CuentaEntity;
import com.microservicios.cuenta.entity.model.Retiro;
import com.microservicios.cuenta.entity.model.RetiroMongo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RetiroStrategy implements TransaccionStrategy {

    @Autowired
    TransaccionServiceClient transaccionServiceClient;
    @Autowired
    TransaccionServiceMongoClient transaccionServiceMongoClient;

    @Override
    public void ejecutarTransaccion(CuentaEntity cuenta, Double monto) {
        cuenta.setSaldo(cuenta.getSaldo() - monto);

        // cuentaRepository.save(cuenta); se guardara en el micro-transaccion-mongodb

        // Llamar al microservicio de transacciones mysql
        Retiro retiro = new Retiro();
        retiro.setMonto(monto);
        retiro.setTipo("retiro");
        retiro.setNumCuenta(cuenta.getNumeroCuenta());
        retiro.setComision(0.0);
        retiro.setNumTransaccion(transaccionServiceClient.obtenerTransaccion());
        transaccionServiceClient.crearTransaccionRetiroEnMicroservicio(retiro);
        // Llamar al microservicio de transacciones mongo
        RetiroMongo retiroRequest = new RetiroMongo();
        retiroRequest.setMonto(monto);
        retiroRequest.setCuentaOrigen(cuenta.getNumeroCuenta());

        transaccionServiceMongoClient.crearTransaccionRetiroEnMicroservicio(retiroRequest);
    }
}
