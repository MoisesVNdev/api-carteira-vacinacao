package com.moisesvn.carteira_vacinacao_api.model;

import lombok.*;

import java.io.Serializable;

/**
 * Classe para representar a chave composta de `PessoaAlergia`.
 * Utilizada com @IdClass em PessoaAlergia.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class PessoaAlergiaId implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long pessoa;
    private Long alergia;
}
