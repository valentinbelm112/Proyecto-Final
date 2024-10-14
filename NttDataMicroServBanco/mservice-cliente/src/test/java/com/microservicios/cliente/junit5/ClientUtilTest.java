package com.microservicios.cliente.junit5;

import com.microservicios.cliente.util.clientUtil;
import org.junit.jupiter.api.Test;

import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ClientUtilTest {
    @Test
    public void testClienteNoEncontrado() {

        Long id = 123L;


        Supplier<IllegalArgumentException> supplier = clientUtil.clienteNoEncontrado(id);
        //System.out.println(supplier.get().getMessage());

        assertEquals("Cliente no encontrado con ID: " + id, supplier.get().getMessage());
    }
}
