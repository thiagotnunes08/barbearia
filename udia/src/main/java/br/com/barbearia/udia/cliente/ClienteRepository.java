package br.com.barbearia.udia.cliente;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ClienteRepository extends JpaRepository<Cliente,Long> {
    boolean existsByEmail(String email);
}
