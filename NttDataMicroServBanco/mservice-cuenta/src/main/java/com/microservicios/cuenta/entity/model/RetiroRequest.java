package com.microservicios.cuenta.entity.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;
@Data
@AllArgsConstructor
public class RetiroRequest {
    private String id;
    private String tipo;
    private Double monto;
    private LocalDate fecha;
    private String cuentaOrigen;
    private String cuentaDestino;
}
