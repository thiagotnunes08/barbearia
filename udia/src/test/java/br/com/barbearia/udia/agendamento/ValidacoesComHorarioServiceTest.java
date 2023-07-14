package br.com.barbearia.udia.agendamento;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static java.time.LocalDateTime.of;

@SpringBootTest
@ActiveProfiles("test")
class ValidacoesComHorarioServiceTest {

    @Autowired
    private AgendamentoRepository agendamentoRepository;

    @Autowired
    private ValidacoesComHorarioService validacoesComHorarioService;

    @Test
    @DisplayName("deve passar por todas validacoes sem dar erro")
    void test1() {
        var request = new NovoAgendamentoRequest(3L, 3L, of(2023, 7, 15, 14, 0), List.of(1L));
          Assertions.assertDoesNotThrow(()-> validacoesComHorarioService.validacoesAgendamento(request,agendamentoRepository));
    }
    @Test
    @DisplayName("deve passar por todas validacoes sem dar erro")
    void test2() {
        var request = new NovoAgendamentoRequest(3L, 3L, of(2023, 7, 15, 14, 0), List.of(1L));
        Assertions.assertDoesNotThrow(()-> validacoesComHorarioService.validacoesAgendamento(request,agendamentoRepository));
    }
}