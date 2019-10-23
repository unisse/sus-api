package org.unisse.sus.domain;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.time.Instant;

/**
 * A SituacaoUnidadeSaude.
 */
@Entity
@Table(name = "situacao_unidade_saude")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class SituacaoUnidadeSaude implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "criacao", nullable = false)
    private Instant criacao;

    @NotNull
    @Column(name = "descricao", nullable = false)
    private String descricao;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getCriacao() {
        return criacao;
    }

    public SituacaoUnidadeSaude criacao(Instant criacao) {
        this.criacao = criacao;
        return this;
    }

    public void setCriacao(Instant criacao) {
        this.criacao = criacao;
    }

    public String getDescricao() {
        return descricao;
    }

    public SituacaoUnidadeSaude descricao(String descricao) {
        this.descricao = descricao;
        return this;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SituacaoUnidadeSaude)) {
            return false;
        }
        return id != null && id.equals(((SituacaoUnidadeSaude) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "SituacaoUnidadeSaude{" +
            "id=" + getId() +
            ", criacao='" + getCriacao() + "'" +
            ", descricao='" + getDescricao() + "'" +
            "}";
    }
}
