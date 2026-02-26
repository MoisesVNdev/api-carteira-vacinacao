package com.moisesvn.carteira_vacinacao_api.repository;

import com.moisesvn.carteira_vacinacao_api.model.Pessoa;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repositório JPA para a entidade `Pessoa`.
 */
public interface PessoaRepository extends JpaRepository<Pessoa, Long> {

    Optional<Pessoa> findByCns(String cns);

    Optional<Pessoa> findByCpf(String cpf);

    Optional<Pessoa> findByCpfAndCns(String cpf, String cns);

}
