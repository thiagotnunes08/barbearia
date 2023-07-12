package br.com.barbearia.udia.procedimento;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
public class CadastraNovoProcedimentoController {

    private final ProcedimentoRepository procedimentoRepository;

    public CadastraNovoProcedimentoController(ProcedimentoRepository procedimentoRepository) {
        this.procedimentoRepository = procedimentoRepository;
    }


    @PostMapping("v1/procedimento")
    public ResponseEntity<?> cria(@Valid @RequestBody NovoProcedimentoRequest request, UriComponentsBuilder uriBuilder) {

        if (procedimentoRepository.existsByNome(request.nome())){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Procedimento já cadastrado no sistema");
        }
        var novoProcedimento = request.toModel();

        procedimentoRepository.save(novoProcedimento);

        return ResponseEntity.created(uriBuilder.path("/procedimento/{id}").buildAndExpand(novoProcedimento.getId()).toUri()).build();
    }
}
