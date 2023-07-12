package br.com.barbearia.udia.barbeiro;

import org.springframework.data.jpa.repository.JpaRepository;

public interface BarbeiroRepository extends JpaRepository<Barbeiro,Long> {
    boolean existsByCpf(String cpf);
}
