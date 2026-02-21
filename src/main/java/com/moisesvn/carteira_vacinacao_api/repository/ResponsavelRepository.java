package com.moisesvn.carteira_vacinacao_api.repository;

import com.moisesvn.carteira_vacinacao_api.model.Responsavel;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ResponsavelRepository extends JpaRepository<Responsavel, Long> {

    boolean existsByUsuarioIdAndPessoaId(Long usuarioId, Long pessoaId);

    Optional<Responsavel> findByUsuarioIdAndPessoaId(Long usuarioId, Long pessoaId);

    List<Responsavel> findByUsuarioId(Long usuarioId);

    List<Responsavel> findByPessoaId(Long pessoaId);

}
