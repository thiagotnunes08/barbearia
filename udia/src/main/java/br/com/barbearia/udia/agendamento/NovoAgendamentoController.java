package br.com.barbearia.udia.agendamento;

import br.com.barbearia.udia.barbeiro.BarbeiroRepository;
import br.com.barbearia.udia.cliente.ClienteRepository;
import br.com.barbearia.udia.procedimento.Procedimento;
import br.com.barbearia.udia.procedimento.ProcedimentoRepository;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class NovoAgendamentoController {

    private final BarbeiroRepository barbeiroRepository;
    private final ClienteRepository clienteRepository;

    private final AgendamentoRepository agendamentoRepository;

    private final ProcedimentoRepository procedimentoRepository;

    private final ValidacoesComHorarioService validaHorarioService;

    public NovoAgendamentoController(BarbeiroRepository barbeiroRepository, ClienteRepository clienteRepository, AgendamentoRepository agendamentoRepository, ProcedimentoRepository procedimentoRepository, ValidacoesComHorarioService validaHorarioService) {
        this.barbeiroRepository = barbeiroRepository;
        this.clienteRepository = clienteRepository;
        this.agendamentoRepository = agendamentoRepository;
        this.procedimentoRepository = procedimentoRepository;
        this.validaHorarioService = validaHorarioService;
    }

    @PostMapping("v1/agendamento")
    public ResponseEntity<?> agenda(@RequestBody @Valid NovoAgendamentoRequest request) {

        validaHorarioService.validacoesAgendamento(request, agendamentoRepository);

        var barbeiro = barbeiroRepository
                .findById(request.barbeiroId())
                .orElseThrow(() ->
                        new ResponseStatusException(HttpStatus.NOT_FOUND, "Barbeiro inválido ou nao existente."));

        //TODO:Ao colocar autenticação, pegar cliente logado.
        var cliente = clienteRepository
                .findById(request.clientId())
                .orElseThrow(() ->
                        new ResponseStatusException(HttpStatus.NOT_FOUND, "Cliente inválido ou nao existente."));

        var procedimentos = request.procedimentosIds()
                .stream()
                .map(id -> procedimentoRepository
                        .findById(id)
                        .orElseThrow(() ->
                                new ResponseStatusException(HttpStatus.NOT_FOUND,
                                        "Procedimento não encontrado!")))
                .toList();

        var novoAgendamento = request.toModel(barbeiro, cliente, procedimentos);

        agendamentoRepository.save(novoAgendamento);

        return ResponseEntity.created(URI.create("agendamento/" + novoAgendamento.getId())).build();

    }
}
