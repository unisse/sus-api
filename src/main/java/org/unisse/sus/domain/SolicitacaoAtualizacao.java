package org.unisse.sus.domain;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.time.Instant;

/**
 * A SolicitacaoAtualizacao.
 */
@Entity
@Table(name = "solicitacao_atualizacao")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class SolicitacaoAtualizacao implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "criacao", nullable = false)
    private Instant criacao;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties("solicitacaoAtualizacaos")
    private UnidadeSaude unidade;

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

    public SolicitacaoAtualizacao criacao(Instant criacao) {
        this.criacao = criacao;
        return this;
    }

    public void setCriacao(Instant criacao) {
        this.criacao = criacao;
    }

    public UnidadeSaude getUnidade() {
        return unidade;
    }

    public SolicitacaoAtualizacao unidade(UnidadeSaude unidadeSaude) {
        this.unidade = unidadeSaude;
        return this;
    }

    public void setUnidade(UnidadeSaude unidadeSaude) {
        this.unidade = unidadeSaude;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SolicitacaoAtualizacao)) {
            return false;
        }
        return id != null && id.equals(((SolicitacaoAtualizacao) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "SolicitacaoAtualizacao{" +
            "id=" + getId() +
            ", criacao='" + getCriacao() + "'" +
            "}";
    }
}
