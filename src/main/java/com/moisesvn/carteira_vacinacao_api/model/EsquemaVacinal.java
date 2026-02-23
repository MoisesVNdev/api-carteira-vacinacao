package com.moisesvn.carteira_vacinacao_api.model;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;

/**
 * Entidade JPA que representa a tabela `esquema_vacinal`.
 * Define o esquema de doses recomendadas para cada vacina do calendário vacinal PNI.
 */
@Entity
@Table(name = "esquema_vacinal")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class EsquemaVacinal implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "vacina_id", nullable = false)
    private Vacina vacina;

    @Column(name = "descricao_dose", nullable = false, length = 50)
    private String descricaoDose;

    @Column(name = "idade_recomendada_meses", nullable = false)
    private Integer idadeRecomendadaMeses;

    @Column(name = "intervalo_minimo_dias")
    private Integer intervaloMinimoDias;
}
