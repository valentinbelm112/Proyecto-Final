package com.microservicios.cuenta.business;

import com.microservicios.cuenta.entity.CuentaEntity;

public interface TransaccionStrategy {

    void ejecutarTransaccion(CuentaEntity cuenta, Double monto);
}
