package com.microservicios.transaccion.entity;

import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;

@Entity
@AllArgsConstructor
@Data
public class Deposito  extends Transaccion{

    private String odeposito;

    public Deposito() {

    }
}
