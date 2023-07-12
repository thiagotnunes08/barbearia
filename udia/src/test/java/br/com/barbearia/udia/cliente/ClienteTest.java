package br.com.barbearia.udia.cliente;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ClienteTest {

    @Test
    @DisplayName("deve instanciar um cliente valido")
    void test1() {
        var cliente = new Cliente("thiago", "thiago@thiago");
        assertNotNull(cliente);
    }

    @Test
    @DisplayName("nao instacia cliente com nome em branco")
    void test2() {
        assertThrows(IllegalArgumentException.class, () -> new Cliente("", "thiago@thiago"));
    }

    @Test
    @DisplayName("nao instacia cliente com email em branco")
    void test3() {
        assertThrows(IllegalArgumentException.class, () -> new Cliente("thiago", ""));

    }

    @Test
    @DisplayName("nao instacia cliente com email ou nome em branco")
    void test4() {
        assertThrows(IllegalArgumentException.class, () -> new Cliente("", ""));

    }
}