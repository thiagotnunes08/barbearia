package br.com.barbearia.udia.agendamento;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.Optional;

public interface AgendamentoRepository extends JpaRepository<Agendamento,Long> {

    @Query("select a FROM Agendamento a where a.horario=:horario and (a.barbeiro.id =:barbeiroId or a.cliente.id=:clientId)")
    Optional<Agendamento> buscaAgendamentoPorHorarioEClienteOuBarbeiro(LocalDateTime horario, Long barbeiroId, Long clientId);

}
