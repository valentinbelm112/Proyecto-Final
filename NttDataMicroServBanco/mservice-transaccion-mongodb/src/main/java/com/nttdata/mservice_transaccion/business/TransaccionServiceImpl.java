package com.nttdata.mservice_transaccion.business;

import com.nttdata.mservice_transaccion.business.Externo.CuentaServiceClient;
import com.nttdata.mservice_transaccion.mapper.ResponseMapperTranferencia;
import com.nttdata.mservice_transaccion.mapper.TransferenciaMapperRequest;
import com.nttdata.mservice_transaccion.model.*;
import com.nttdata.mservice_transaccion.model.dto.CuentaRequest;
import com.nttdata.mservice_transaccion.model.dto.CuentaResponse;
import com.nttdata.mservice_transaccion.repository.TransaccionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
public class TransaccionServiceImpl implements TransaccionService{
    @Autowired
    CuentaServiceClient cuentaServiceClient;
    @Autowired
    TransaccionRepository transaccionRepository;

    @Override
    public TransaccionResponse registrarDeposito(DepositoRequest deposito) {
        // Paso 1: Verificar si la cuenta existe
        CuentaResponse cuentaResponse= cuentaServiceClient.obtenerCuentaPorNumero(deposito.getCuentaOrigen());

        // Verificar si la cuenta existe y si tiene saldo suficiente
        if (cuentaResponse == null) {
            throw new NoSuchElementException("No se encontró la cuenta con el número proporcionado: " + deposito.getCuentaOrigen());
        }
        // Paso 2: Registrar la transacción
        Transaccion transaccion= Optional.of(TransferenciaMapperRequest.getTransaccionofDepositoRequest(deposito))
                .map(transaccionRepository::save)
                .orElseThrow(() -> new IllegalArgumentException("Error al crear la cuenta"));

        try {
            // Paso 3: Actualizar el saldo de la cuenta
            CuentaRequest cuentaRequest = new CuentaRequest();
            cuentaRequest.setEstadoCuenta(cuentaResponse.getEstadoCuenta());
            cuentaRequest.setSaldo(cuentaResponse.getSaldo() + deposito.getMonto());
            cuentaRequest.setNumeroCuenta(cuentaResponse.getNumeroCuenta());
            cuentaRequest.setTipoCuenta(cuentaResponse.getTipoCuenta());
            // Simular error antes de actualizar el saldo
            //throw new RuntimeException("Error simulado al actualizar saldos");

            cuentaServiceClient.ActualizarSaldo(Math.toIntExact(cuentaResponse.getId()), cuentaRequest);

            // Devolver el resultado si todo es exitoso
            return ResponseMapperTranferencia.getTransaccionoResponseofTransaccion(transaccion);

        } catch (RuntimeException  e) {
            // Si ocurre un fallo en la actualización, revertir la transacción registrada
            transaccionRepository.delete(transaccion);
            throw new IllegalStateException("Error al actualizar el saldo, se revirtió la transacción", e);
        }
    }


    @Override
    public TransaccionResponse registrarRetiro(RetiroRequest retiro) {
        CuentaResponse cuentaResponse= cuentaServiceClient.obtenerCuentaPorNumero(retiro.getCuentaOrigen());

        // Verificar si la cuenta existe y si tiene saldo suficiente
        if (cuentaResponse == null) {
            throw new NoSuchElementException("No se encontró la cuenta con el número proporcionado: " + retiro.getCuentaOrigen());
        }

        // Verificar si hay saldo suficiente para realizar el retiro
        if (cuentaResponse.getSaldo() <= 0) {
            throw new IllegalArgumentException("No hay saldo suficiente para realizar el retiro.");
        }

        // Verificar que el monto a retirar no sea mayor que el saldo disponible
        if (retiro.getMonto() > cuentaResponse.getSaldo()) {
            throw new IllegalArgumentException("El monto a retirar excede el saldo disponible.");
        }
        Transaccion transaccion= Optional.of(TransferenciaMapperRequest.getTransaccionofRetiroRequest(retiro))
                .map(transaccionRepository::save)
                .orElseThrow(() -> new IllegalArgumentException("Error al crear la cuenta"));
        try{
            CuentaRequest cuentaRequest=new CuentaRequest();
            cuentaRequest.setEstadoCuenta(cuentaResponse.getEstadoCuenta());
            cuentaRequest.setSaldo(cuentaResponse.getSaldo() - retiro.getMonto());
            cuentaRequest.setNumeroCuenta(cuentaResponse.getNumeroCuenta());
            cuentaRequest.setTipoCuenta(cuentaResponse.getTipoCuenta());

            // Simular error antes de actualizar el saldo
            //throw new RuntimeException("Error simulado al actualizar saldos");

            cuentaServiceClient.ActualizarSaldo(Math.toIntExact(cuentaResponse.getId()),cuentaRequest);


            return  ResponseMapperTranferencia.getTransaccionoResponseofTransaccion(transaccion);

        }catch (Exception e){
            // Si ocurre un fallo en la actualización, revertir la transacción registrada
            transaccionRepository.delete(transaccion);
            throw new IllegalStateException("Error al actualizar el saldo, se revirtió la transacción", e);
        }



    }

    @Override
    public TransaccionResponse registrarTransferencia(TransferenciaRequest transferencia) {

        CuentaResponse cuentaOrigen = cuentaServiceClient.obtenerCuentaPorNumero(transferencia.getCuentaOrigen());

        // Verificar si la cuenta existe y si tiene saldo suficiente
        if (cuentaOrigen  == null) {
            throw new NoSuchElementException("No se encontró la cuenta origen ");
        }

        // Verificar si la cuenta origen está activa
        if (!cuentaOrigen .getEstadoCuenta().equals("ACTIVA")) {
            throw new NoSuchElementException("La cuenta origen no está activa");
        }

        CuentaResponse cuentaDestino = cuentaServiceClient.obtenerCuentaPorNumero(transferencia.getCuentaDestino());

        // Verificar si la cuenta existe y si tiene saldo suficiente
        if (cuentaDestino  == null) {
            throw new NoSuchElementException("Cuenta destino no encontrada " );
        }

        // Verificar si la cuenta origen está activa
        if (!cuentaDestino .getEstadoCuenta().equals("ACTIVA")) {
            throw new NoSuchElementException("La cuenta destino no está activa");
        }

        // Verificar si la cuenta origen tiene suficiente saldo
        if (cuentaOrigen.getSaldo() < transferencia.getMonto()) {
            throw new NoSuchElementException("Saldo insuficiente en la cuenta origen");
        }

        Transaccion transaccion= Optional.of(TransferenciaMapperRequest.getTransaccionofTranferenciaRequest(transferencia))
                .map(transaccionRepository::save)
                .orElseThrow(() -> new IllegalArgumentException("Error al crear la cuenta"));

        try {
            CuentaRequest cuentaRequest=new CuentaRequest();
            cuentaRequest.setEstadoCuenta(cuentaOrigen.getEstadoCuenta());
            cuentaRequest.setSaldo(cuentaOrigen.getSaldo() - transferencia.getMonto());
            cuentaRequest.setNumeroCuenta(cuentaOrigen.getNumeroCuenta());
            cuentaRequest.setTipoCuenta(cuentaOrigen.getTipoCuenta());
            System.out.println(cuentaRequest.getSaldo());
            System.out.println(Math.toIntExact(cuentaOrigen.getId()));


            cuentaServiceClient.ActualizarSaldo(Math.toIntExact(cuentaOrigen.getId()),cuentaRequest);

            CuentaRequest cuentaRequestDestino=new CuentaRequest();
            cuentaRequestDestino.setEstadoCuenta(cuentaDestino.getEstadoCuenta());
            cuentaRequestDestino.setSaldo(cuentaDestino.getSaldo() + transferencia.getMonto());
            cuentaRequestDestino.setNumeroCuenta(cuentaDestino.getNumeroCuenta());
            cuentaRequestDestino.setTipoCuenta(cuentaDestino.getTipoCuenta());



            cuentaServiceClient.ActualizarSaldo(Math.toIntExact(cuentaDestino.getId()),cuentaRequestDestino);



            return  ResponseMapperTranferencia.getTransaccionoResponseofTransaccion(transaccion);
        } catch (Exception e) {
            // Si ocurre un fallo en la actualización, revertir la transacción registrada
            transaccionRepository.delete(transaccion);
            throw new IllegalStateException("Error al actualizar los saldos, se revirtió la transacción", e);
        }


    }

    @Override
    public List<TransaccionResponse> consultarHistorialtransacciones() {
        // Recuperar todas las transacciones de la base de datos
        List<Transaccion> transacciones = transaccionRepository.findAll();

        // Mapear las transacciones a un formato de respuesta
        List<TransaccionResponse> historialTransacciones = transacciones.stream()
                .map(ResponseMapperTranferencia::getTransaccionoResponseofTransaccion)
                .collect(Collectors.toList());


        return historialTransacciones;
    }
}
