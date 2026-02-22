package com.moisesvn.carteira_vacinacao_api.repository;

import com.moisesvn.carteira_vacinacao_api.model.PessoaAlergia;
import com.moisesvn.carteira_vacinacao_api.model.PessoaAlergiaId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * Repositório JPA para a entidade `PessoaAlergia`.
 */
public interface PessoaAlergiaRepository extends JpaRepository<PessoaAlergia, PessoaAlergiaId> {

    /**
     * Lista todos os vínculos de alergias para uma pessoa específica.
     */
    List<PessoaAlergia> findByPessoaId(Long pessoaId);

    /**
     * Busca um vínculo específico entre pessoa e alergia.
     */
    Optional<PessoaAlergia> findByPessoaIdAndAlergiaId(Long pessoaId, Long alergiaId);

    /**
     * Verifica se um vínculo já existe.
     */
    boolean existsByPessoaIdAndAlergiaId(Long pessoaId, Long alergiaId);
}
