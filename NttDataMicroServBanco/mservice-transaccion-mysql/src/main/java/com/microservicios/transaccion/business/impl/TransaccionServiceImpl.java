package com.microservicios.transaccion.business.impl;

import com.microservicios.transaccion.business.TransaccionService;
import com.microservicios.transaccion.entity.Transaccion;
import com.microservicios.transaccion.model.TransaccionRequest;
import com.microservicios.transaccion.model.TransaccionResponse;
import com.microservicios.transaccion.repository.TransaccionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static com.microservicios.transaccion.mapper.TransaccionMapperRequest.transaccionmapperofTransaccionRequest;

@Service
public class TransaccionServiceImpl implements TransaccionService {

    @Autowired
    private  TransaccionRepository transaccionRepository;

    @Override
    public TransaccionResponse crearTransaccion(TransaccionRequest transaccionRequest) {
        Transaccion transaccion= Optional.of(transaccionRepository.save(transaccionmapperofTransaccionRequest(transaccionRequest)))
                .orElseThrow(() -> new RuntimeException("Error al crear la transacción"));

        return  transaccionmapperofTransaccionRequest(transaccion);
    }
    @Override
    public String generarNumeroCuentaUnico() {
        return Stream.generate(() -> generarNumAleatorioTransaccion(13)) // Genera un número de 13 dígitos
                .filter(numeroCuenta -> !transaccionRepository.existsByNumTransaccion(numeroCuenta)) // Filtra números que no existan en la base de datos
                .findFirst() // Toma el primer número que cumpla la condición
                .orElseThrow(() -> new IllegalStateException("No se pudo generar un número de cuenta único")); // Manejo de errores
    }

    private String generarNumAleatorioTransaccion(int longitud) {
        Random random = new Random();
        return IntStream.range(0, longitud)
                .map(i -> random.nextInt(10))
                .mapToObj(String::valueOf)
                .collect(Collectors.joining());
    }


}
