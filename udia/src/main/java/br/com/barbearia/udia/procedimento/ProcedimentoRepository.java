package br.com.barbearia.udia.procedimento;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ProcedimentoRepository extends JpaRepository<Procedimento,Long> {
    boolean existsByNome(String nome);
}
