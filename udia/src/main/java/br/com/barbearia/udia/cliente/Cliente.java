package br.com.barbearia.udia.cliente;
import jakarta.persistence.*;
import org.springframework.util.Assert;
import java.time.LocalDateTime;
import static java.time.LocalDateTime.now;

@Entity
@Table(name = "cliente",uniqueConstraints = @UniqueConstraint(name = "UK_EMAIL",columnNames = "email"))
public class Cliente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String nome;
    @Column(nullable = false)
    private String email;
    @Column(nullable = false,updatable = false)
    private LocalDateTime cadastradoEm;

    public Cliente(String nome, String email) {

        Assert.hasText(nome,"nome não pode ser nulo!");
        Assert.hasText(email,"email nao pode ser nulo!");

        this.nome = nome;
        this.email = email;
        this.cadastradoEm = now();
    }
    /**
     * @deprecated = uso exclusivo do hibernate
     */
    @Deprecated
    public Cliente() {
    }
    public Long getId() {
        return id;
    }

    @Override
    public String toString() {
        return "Cliente{" +
                "id=" + id +
                ", nome='" + nome + '\'' +
                ", email='" + email + '\'' +
                ", cadastradoEm=" + cadastradoEm +
                '}';
    }

    public String getNome() {
        return nome;
    }
}
