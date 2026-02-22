package com.moisesvn.carteira_vacinacao_api.repository;

import com.moisesvn.carteira_vacinacao_api.model.Alergia;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Repositório JPA para a entidade `Alergia`.
 */
public interface AlergiaRepository extends JpaRepository<Alergia, Long> {

    Optional<Alergia> findByDescricao(String descricao);
}
