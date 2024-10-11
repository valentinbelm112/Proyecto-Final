package com.microservicios.cliente.business.impl;

import com.microservicios.cliente.business.CuentaService;
import com.microservicios.cliente.business.Externo.TransaccionServiceClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TransaccionServiceAdapter implements CuentaService {

    @Autowired
    private TransaccionServiceClient transaccionServiceClient;

    @Override
    public boolean validarCuentaActiva(Long id) {
        // Lógica específica para llamar al TransaccionServiceClient
        return transaccionServiceClient.validarCuentaActiva(id);
    }
}
