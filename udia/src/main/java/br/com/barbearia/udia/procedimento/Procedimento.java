package br.com.barbearia.udia.procedimento;

import jakarta.persistence.*;
import org.springframework.util.Assert;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(uniqueConstraints = @UniqueConstraint(name = "UK_NOME",columnNames = "nome"))
public class Procedimento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String nome;
    @Column(nullable = false)
    private String duracao;
    @Column(nullable = false)
    private BigDecimal preco;
    @Column(nullable = false,updatable = false)
    private LocalDateTime criadoEm;
    public Procedimento(String nome, String duracao, BigDecimal preco) {

        Assert.hasText(nome,"nome deve ser obrigatório.");
        Assert.hasText(duracao,"duração deve ser obrigatória.");
        Assert.notNull(preco,"preço deve ser obrigatório.");
        if (preco.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Preço deve ser positivo.");
        }

        this.nome = nome;
        this.duracao = duracao;
        this.preco = preco;
        this.criadoEm = LocalDateTime.now();
    }

    /**
     * @deprecated = uso exclusivo do hiberante
     */
    @Deprecated
    public Procedimento() {
    }
    public Long getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }
    public BigDecimal getPreco() {
        return preco;
    }
}
