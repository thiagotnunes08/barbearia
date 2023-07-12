package br.com.barbearia.udia.cliente;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import static java.net.URI.create;

@RestController
public class CadastraNovoClienteController {
    private final ClienteRepository repository;

    public CadastraNovoClienteController(ClienteRepository repository) {
        this.repository = repository;
    }
    @PostMapping("v1/cliente")
    @Transactional
    public ResponseEntity<?> cria(@RequestBody @Valid NovoClienteRequest request){

        if (repository.existsByEmail(request.email())){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Cliente já cadastrado com este email no sistema.");
        }

        var novoCliente = request.toModel();

        repository.save(novoCliente);

        return ResponseEntity.created(create("/clientes/%s".formatted(novoCliente.getId()))).build();
    }
}
