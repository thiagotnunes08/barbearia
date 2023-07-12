package br.com.barbearia.udia.barbeiro;

import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.br.CPF;

public record NovoBarbeiroRequest(@NotBlank String nome, @NotBlank @CPF String cpf) {
    public Barbeiro toModel() {
        return new Barbeiro(nome,cpf);
    }
}
