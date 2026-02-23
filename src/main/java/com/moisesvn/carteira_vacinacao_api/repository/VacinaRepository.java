package com.moisesvn.carteira_vacinacao_api.repository;

import com.moisesvn.carteira_vacinacao_api.model.Vacina;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Repositório JPA para a entidade `Vacina`.
 */
public interface VacinaRepository extends JpaRepository<Vacina, Long> {

    Optional<Vacina> findByNome(String nome);
}
