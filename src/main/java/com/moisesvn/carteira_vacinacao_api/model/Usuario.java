package com.moisesvn.carteira_vacinacao_api.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "usuario")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nome_completo", nullable = false, length = 255)
    private String nomeCompleto;

    @Column(nullable = false, unique = true, length = 255)
    private String email;

    @Column(nullable = false, length = 255)
    private String senha;

    @Column(name = "data_cadastro", nullable = false, updatable = false)
    private LocalDateTime dataCadastro;

    @PrePersist
    private void prePersist() {
        this.dataCadastro = LocalDateTime.now();
    }

    /**
     * Implementa equals baseado apenas no ID (padrão de entidades JPA).
     * Evita problemas com lazy loading e comparação correta de identidade.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Usuario)) return false;
        return id != null && id.equals(((Usuario) o).id);
    }

    /**
     * Implementa hashCode baseado apenas no ID (padrão de entidades JPA).
     * Garante consistência com o contrato de equals().
     */
    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}