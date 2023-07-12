package br.com.barbearia.udia.barbeiro;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import static java.net.URI.create;

@RestController
public class CadastraNovoBarbeiroController {

    private final BarbeiroRepository repository;

    public CadastraNovoBarbeiroController(BarbeiroRepository repository) {
        this.repository = repository;
    }

    @PostMapping("v1/barbeiro")
    public ResponseEntity<?> cadastra(@RequestBody @Valid NovoBarbeiroRequest request){

        if (repository.existsByCpf(request.cpf())){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Barbeiro já cadastrado com este CPF no sistema!");
        }

        var novoBarbeiro = request.toModel();

        repository.save(novoBarbeiro);

        return ResponseEntity
                .created(create("barbeiro/%s".formatted(novoBarbeiro.getId()))).build();

    }
}
