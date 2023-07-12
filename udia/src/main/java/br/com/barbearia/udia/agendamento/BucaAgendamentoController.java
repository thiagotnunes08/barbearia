package br.com.barbearia.udia.agendamento;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
public class BucaAgendamentoController {
    private final AgendamentoRepository agendamentoRepository;

    public BucaAgendamentoController(AgendamentoRepository agendamentoRepository) {
        this.agendamentoRepository = agendamentoRepository;
    }

    @GetMapping("v1/agdendamento/{id}")
    public AgendamentoResponse busca(@PathVariable Long id){

        var agendamento = agendamentoRepository
                .findById(id)
                .orElseThrow(()
                        -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Agendamento inválido ou não encontrado!"));


        return new AgendamentoResponse(agendamento);

    }
}
