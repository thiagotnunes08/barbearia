package br.com.barbearia.udia.agendamento;

import br.com.barbearia.udia.barbeiro.Barbeiro;
import br.com.barbearia.udia.cliente.Cliente;
import br.com.barbearia.udia.procedimento.Procedimento;
import jakarta.persistence.*;
import org.springframework.util.Assert;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Agendamento {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(optional = false)
    private Barbeiro barbeiro;
    @ManyToOne(optional = false)
    private Cliente cliente;
    @Column(nullable = false)
    private LocalDateTime horario;
    @OneToMany
    private List<Procedimento> procedimento = new ArrayList<>();
    @Column(nullable = false)
    private LocalDateTime criadoEm;

    public Agendamento(Barbeiro barbeiro, Cliente cliente, LocalDateTime horario, List<Procedimento> procedimento) {

        Assert.notNull(barbeiro,"Barbeiro não pode ser nulo");
        Assert.notNull(cliente,"Cliente não pode ser nulo");
        Assert.notEmpty(procedimento,"Procedimento é obrigatório");

        this.barbeiro = barbeiro;
        this.cliente = cliente;
        this.horario = horario;
        this.procedimento.addAll(procedimento);
        this.criadoEm = LocalDateTime.now();
    }

    /**
     * @Deprecated=uso hibernate
     */

    @Deprecated
    public Agendamento() {
    }

    public Long getId() {
        return id;
    }

    public String getNomeCliente() {
        return cliente.getNome();
    }

    public String getNomeBarbeiro() {
        return barbeiro.getNome();
    }

    public LocalDateTime  getHorarioAtendimento() {
        return horario;
    }

    public List<String> getNomeProcedimentos() {
        return procedimento
                .stream()
                .map(p-> p.getNome()).toList();
    }

    public BigDecimal getValorTotal() {
        return procedimento
                .stream()
                .map(p-> p.getPreco())
                .reduce(BigDecimal.ZERO,BigDecimal::add);
    }
}
