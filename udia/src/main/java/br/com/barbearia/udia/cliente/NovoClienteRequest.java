package br.com.barbearia.udia.cliente;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record NovoClienteRequest (@NotBlank String nome, @NotBlank @Email String email){
    public Cliente toModel() {
        return new Cliente(nome,email);
    }
}
