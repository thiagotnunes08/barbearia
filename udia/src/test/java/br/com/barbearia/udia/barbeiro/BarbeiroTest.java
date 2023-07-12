package br.com.barbearia.udia.barbeiro;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

class BarbeiroTest {

    @Test
    @DisplayName("deve instanciar um barbeiro valido")
    void test1() {
        var barbeiro = new Barbeiro("thiago", "thiago@thiago");
        assertNotNull(barbeiro);
    }

    @Test
    @DisplayName("nao instacia barbeiro com nome em branco")
    void test2() {
        assertThrows(IllegalArgumentException.class, () -> new Barbeiro("", "thiago@thiago"));
    }

    @Test
    @DisplayName("nao instacia barbeiro com cpf em branco")
    void test3() {
        assertThrows(IllegalArgumentException.class, () -> new Barbeiro("thiago", ""));

    }

    @Test
    @DisplayName("nao instacia barbeiro com email ou nome em branco")
    void test4() {
        assertThrows(IllegalArgumentException.class, () -> new Barbeiro("", ""));

    }

}