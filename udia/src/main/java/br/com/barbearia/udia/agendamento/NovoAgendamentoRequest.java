package br.com.barbearia.udia.agendamento;

import br.com.barbearia.udia.barbeiro.Barbeiro;
import br.com.barbearia.udia.cliente.Cliente;
import br.com.barbearia.udia.procedimento.Procedimento;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.util.List;

public record NovoAgendamentoRequest(@NotNull Long barbeiroId,
                                     @NotNull Long clienteId,
                                     @FutureOrPresent @NotNull @JsonFormat(pattern = "dd/MM/yyyy HH:mm") LocalDateTime horario,
                                     @NotEmpty List<Long> procedimentosIds) {

    public Agendamento toModel(Barbeiro barbeiro, Cliente cliente, List<Procedimento> procedimentos) {
        return new Agendamento(barbeiro, cliente, horario, procedimentos);
    }

    public boolean horarioInvalido() {
        return this.horario.getMinute() != 0;
    }

    public boolean diaDeFolga() {
        return this.horario.getDayOfWeek().equals(DayOfWeek.SUNDAY);
    }

    public boolean horarioAlmoco() {
        int horaSolicitada = this.horario.getHour();
        return horaSolicitada == 12 || horaSolicitada == 13;
    }
}
