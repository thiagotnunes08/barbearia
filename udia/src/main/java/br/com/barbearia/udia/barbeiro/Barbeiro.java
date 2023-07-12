package br.com.barbearia.udia.barbeiro;

import jakarta.persistence.*;
import org.springframework.util.Assert;

import java.time.LocalDateTime;

@Entity
@Table(uniqueConstraints = @UniqueConstraint(name = "UK_CPF",columnNames = "cpf"))
public class Barbeiro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String nome;
    @Column(nullable = false)
    private String cpf;
    @Column(nullable = false,updatable = false)
    private LocalDateTime criadoEm;

    public Barbeiro(String nome, String cpf) {

        Assert.hasText(nome,"nome deve ser obrigatorio");
        Assert.hasText(cpf,"CPF deve ser obrigatorio");

        this.nome = nome;
        this.cpf = cpf;
        this.criadoEm = LocalDateTime.now();
    }

    /**
     * @Deprecated = uso hibernate
     */
    @Deprecated
    public Barbeiro() {
    }

    public Long getId() {
        return id;
    }

    @Override
    public String toString() {
        return "Barbeiro{" +
                "id=" + id +
                ", nome='" + nome + '\'' +
                ", cpf='" + cpf + '\'' +
                ", criadoEm=" + criadoEm +
                '}';
    }

    public String getNome() {
        return nome;
    }
}
