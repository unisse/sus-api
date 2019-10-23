package org.unisse.sus.domain;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.time.Instant;

/**
 * A Registro.
 */
@Entity
@Table(name = "registro")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Registro implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "criacao", nullable = false)
    private Instant criacao;

    @Size(max = 2550)
    @Column(name = "descricao", length = 2550)
    private String descricao;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties("registros")
    private TipoRegistro tipo;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties("registros")
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

    public Registro criacao(Instant criacao) {
        this.criacao = criacao;
        return this;
    }

    public void setCriacao(Instant criacao) {
        this.criacao = criacao;
    }

    public String getDescricao() {
        return descricao;
    }

    public Registro descricao(String descricao) {
        this.descricao = descricao;
        return this;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public TipoRegistro getTipo() {
        return tipo;
    }

    public Registro tipo(TipoRegistro tipoRegistro) {
        this.tipo = tipoRegistro;
        return this;
    }

    public void setTipo(TipoRegistro tipoRegistro) {
        this.tipo = tipoRegistro;
    }

    public UnidadeSaude getUnidade() {
        return unidade;
    }

    public Registro unidade(UnidadeSaude unidadeSaude) {
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
        if (!(o instanceof Registro)) {
            return false;
        }
        return id != null && id.equals(((Registro) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Registro{" +
            "id=" + getId() +
            ", criacao='" + getCriacao() + "'" +
            ", descricao='" + getDescricao() + "'" +
            "}";
    }
}
