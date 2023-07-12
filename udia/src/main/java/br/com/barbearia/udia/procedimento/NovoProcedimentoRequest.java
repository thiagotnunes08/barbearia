package br.com.barbearia.udia.procedimento;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record NovoProcedimentoRequest(@NotBlank String nome,@NotBlank String duracao, @NotNull @Positive BigDecimal preco) {

    public Procedimento toModel() {
        return new Procedimento(nome,duracao,preco);
    }
}
