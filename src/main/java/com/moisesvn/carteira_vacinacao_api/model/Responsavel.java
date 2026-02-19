package com.moisesvn.carteira_vacinacao_api.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "responsavel", uniqueConstraints = {
    @UniqueConstraint(name = "uk_usuario_pessoa", columnNames = {"usuario_id", "pessoa_id"})
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Responsavel implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "pessoa_id", nullable = false)
    private Pessoa pessoa;

    @Column(name = "tipo_relacao", length = 50)
    private String tipoRelacao;

    @CreationTimestamp
    @Column(name = "data_criacao", updatable = false)
    private LocalDateTime dataCriacao;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Responsavel)) return false;
        return id != null && id.equals(((Responsavel) o).id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
