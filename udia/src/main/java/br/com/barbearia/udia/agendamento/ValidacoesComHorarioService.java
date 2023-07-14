package br.com.barbearia.udia.agendamento;

import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@Service
public class ValidacoesComHorarioService {

    /**
     * metodo criado para validar horarios para agendamento.
     *
     * @param request para chamar os metodos de validacoes.
     * @param repository para validar se o cliente ou barbeiro já tem horario marcado no horario informado.
     * @throws ResponseStatusException BAD_REQUEST_400.
     *
     */
    public void validacoesAgendamento(NovoAgendamentoRequest request, AgendamentoRepository repository) {

        if (request.horarioInvalido()) {
            throw new ResponseStatusException(BAD_REQUEST, "Horário inválido. Intervalo permitido é de uma em uma hora.");
        }

        if (request.diaDeFolga()) {
            throw new ResponseStatusException(BAD_REQUEST, "Barbearia não funciona domingo.");
        }

        if (request.horarioAlmoco()) {
            throw new ResponseStatusException(BAD_REQUEST, "Barbeiro estara em horário de almoço.");
        }

        if (repository.buscaAgendamentoPorHorarioEClienteOuBarbeiro(request.horario(), request.barbeiroId(), request.clienteId()).isPresent()) {
            throw new ResponseStatusException(BAD_REQUEST, "Já existe um agendamento para o barbeiro ou cliente neste horário.");
        }
    }
}
