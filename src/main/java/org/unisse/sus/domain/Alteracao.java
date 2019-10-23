package org.unisse.sus.domain;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;

/**
 * A Alteracao.
 */
@Entity
@Table(name = "alteracao")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Alteracao implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "nome_campo", nullable = false)
    private String nomeCampo;

    @NotNull
    @Column(name = "valor_anterior", nullable = false)
    private String valorAnterior;

    @NotNull
    @Column(name = "valor_novo", nullable = false)
    private String valorNovo;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties("alteracaos")
    private SolicitacaoAtualizacao solicitacao;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNomeCampo() {
        return nomeCampo;
    }

    public Alteracao nomeCampo(String nomeCampo) {
        this.nomeCampo = nomeCampo;
        return this;
    }

    public void setNomeCampo(String nomeCampo) {
        this.nomeCampo = nomeCampo;
    }

    public String getValorAnterior() {
        return valorAnterior;
    }

    public Alteracao valorAnterior(String valorAnterior) {
        this.valorAnterior = valorAnterior;
        return this;
    }

    public void setValorAnterior(String valorAnterior) {
        this.valorAnterior = valorAnterior;
    }

    public String getValorNovo() {
        return valorNovo;
    }

    public Alteracao valorNovo(String valorNovo) {
        this.valorNovo = valorNovo;
        return this;
    }

    public void setValorNovo(String valorNovo) {
        this.valorNovo = valorNovo;
    }

    public SolicitacaoAtualizacao getSolicitacao() {
        return solicitacao;
    }

    public Alteracao solicitacao(SolicitacaoAtualizacao solicitacaoAtualizacao) {
        this.solicitacao = solicitacaoAtualizacao;
        return this;
    }

    public void setSolicitacao(SolicitacaoAtualizacao solicitacaoAtualizacao) {
        this.solicitacao = solicitacaoAtualizacao;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Alteracao)) {
            return false;
        }
        return id != null && id.equals(((Alteracao) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Alteracao{" +
            "id=" + getId() +
            ", nomeCampo='" + getNomeCampo() + "'" +
            ", valorAnterior='" + getValorAnterior() + "'" +
            ", valorNovo='" + getValorNovo() + "'" +
            "}";
    }
}
