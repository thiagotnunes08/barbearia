package br.com.barbearia.udia.agendamento;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record AgendamentoResponse(String cliente,String barbeiro,LocalDateTime horarioAtendimento,List<String> procedimentos,BigDecimal valor) {
    public AgendamentoResponse(Agendamento agendamento) {

      this(agendamento.getNomeCliente(),
              agendamento.getNomeBarbeiro(),
              agendamento.getHorarioAtendimento(),
              agendamento.getNomeProcedimentos(),
              agendamento.getValorTotal());
    }
}
