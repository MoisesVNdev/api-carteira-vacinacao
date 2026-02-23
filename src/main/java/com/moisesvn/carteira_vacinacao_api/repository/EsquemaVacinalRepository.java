package com.moisesvn.carteira_vacinacao_api.repository;

import com.moisesvn.carteira_vacinacao_api.model.EsquemaVacinal;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Repositório JPA para a entidade `EsquemaVacinal`.
 */
public interface EsquemaVacinalRepository extends JpaRepository<EsquemaVacinal, Long> {

    /**
     * Lista todos os esquemas vacinais (doses) de uma vacina específica.
     */
    List<EsquemaVacinal> findByVacinaIdOrderByIdadeRecomendadaMesesAsc(Long vacinaId);

    /**
     * Lista todos os esquemas vacinais ordenados por idade recomendada.
     */
    List<EsquemaVacinal> findAllByOrderByIdadeRecomendadaMesesAsc();
}
