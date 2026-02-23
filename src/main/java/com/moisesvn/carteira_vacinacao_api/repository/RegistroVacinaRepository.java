package com.moisesvn.carteira_vacinacao_api.repository;

import com.moisesvn.carteira_vacinacao_api.model.RegistroVacina;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * Repositório JPA para a entidade `RegistroVacina`.
 */
public interface RegistroVacinaRepository extends JpaRepository<RegistroVacina, Long> {

    /**
     * Lista todos os registros de vacina de uma pessoa específica.
     */
    List<RegistroVacina> findByPessoaId(Long pessoaId);

    /**
     * Busca um registro específico pelo esquema vacinal e pessoa.
     */
    Optional<RegistroVacina> findByPessoaIdAndEsquemaVacinalId(Long pessoaId, Long esquemaVacinalId);

    /**
     * Verifica se já existe um registro para a dose (esquema vacinal) e pessoa.
     */
    boolean existsByPessoaIdAndEsquemaVacinalId(Long pessoaId, Long esquemaVacinalId);

    /**
     * Lista todos os registros de uma pessoa para uma vacina específica.
     */
    List<RegistroVacina> findByPessoaIdAndEsquemaVacinalVacinaId(Long pessoaId, Long vacinaId);
}
