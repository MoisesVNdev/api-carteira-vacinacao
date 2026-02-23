package com.moisesvn.carteira_vacinacao_api.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Entidade JPA que representa a tabela `registro_vacina`.
 * Histórico de vacinação de cada pessoa (doses aplicadas).
 */
@Entity
@Table(name = "registro_vacina")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class RegistroVacina implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "pessoa_id", nullable = false)
    private Pessoa pessoa;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "esquema_vacinal_id", nullable = false)
    private EsquemaVacinal esquemaVacinal;

    @Column(name = "data_aplicacao", nullable = false)
    private LocalDate dataAplicacao;

    @Column(name = "lote", nullable = false, length = 50)
    private String lote;

    @Column(name = "fabricante", length = 100)
    private String fabricante;

    @Column(name = "vacinador", length = 150)
    private String vacinador;

    @Column(name = "local_aplicacao")
    private String localAplicacao;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;
}
