package br.com.barbearia.udia.procedimento;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class ProcedimentoTest {

    @Test
    @DisplayName("deve instaciar um procedimento válido")
    void test1() {

        var procedimento = new Procedimento("Barba","40 minutos", BigDecimal.TEN);
        assertNotNull(procedimento);
        assertNotNull(procedimento.getNome());
        assertNotNull(procedimento.getPreco());
    }

    @Test
    @DisplayName("nao instacia procedimento sem nome")
    void test2() {
        assertThrows(IllegalArgumentException.class, ()-> new Procedimento("","30 min",BigDecimal.TEN));
    }

    @Test
    @DisplayName("nao instacia procedimento sem duracao")
    void test3() {
        assertThrows(IllegalArgumentException.class, ()-> new Procedimento("corte","",BigDecimal.TEN));
    }

    @Test
    @DisplayName("nao instacia procedimento sem preco")
    void test4() {
        assertThrows(IllegalArgumentException.class, ()-> new Procedimento("corte","30 min",null));
    }

    @Test
    @DisplayName("nao instacia procedimento com preco negativo")
    void test5() {
        assertThrows(IllegalArgumentException.class, ()-> new Procedimento("corte","30 min",new BigDecimal("-1")));
    }
}