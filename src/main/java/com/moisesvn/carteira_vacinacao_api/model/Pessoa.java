package com.moisesvn.carteira_vacinacao_api.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

/**
 * Entidade JPA que representa a tabela `pessoa`.
 * Campos e constraints foram mapeados conforme o script SQL do projeto.
 */
@Entity
@Table(name = "pessoa")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Pessoa implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @Column(name = "nome_completo", nullable = false)
    private String nomeCompleto;

    @Column(name = "data_nascimento", nullable = false)
    private LocalDate dataNascimento;

    @Column(name = "cns", nullable = false, unique = true)
    private String cns;

    @Column(name = "cpf", unique = true)
    private String cpf;

    @Column(name = "nome_mae", nullable = false)
    private String nomeMae;

    @Column(name = "genero")
    private String genero;

    @Column(name = "nacionalidade")
    private String nacionalidade;

    @Column(name = "naturalidade")
    private String naturalidade;

    @Column(name = "tipo_sanguineo", length = 3)
    private String tipoSanguineo;

    @Column(name = "foto")
    private String foto;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
