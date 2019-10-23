package org.unisse.sus.domain;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.time.Instant;

/**
 * A Denuncia.
 */
@Entity
@Table(name = "denuncia")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Denuncia implements Serializable {

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
    @JsonIgnoreProperties("denuncias")
    private Registro registro;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties("denuncias")
    private TipoDenuncia tipo;

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

    public Denuncia criacao(Instant criacao) {
        this.criacao = criacao;
        return this;
    }

    public void setCriacao(Instant criacao) {
        this.criacao = criacao;
    }

    public String getDescricao() {
        return descricao;
    }

    public Denuncia descricao(String descricao) {
        this.descricao = descricao;
        return this;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Registro getRegistro() {
        return registro;
    }

    public Denuncia registro(Registro registro) {
        this.registro = registro;
        return this;
    }

    public void setRegistro(Registro registro) {
        this.registro = registro;
    }

    public TipoDenuncia getTipo() {
        return tipo;
    }

    public Denuncia tipo(TipoDenuncia tipoDenuncia) {
        this.tipo = tipoDenuncia;
        return this;
    }

    public void setTipo(TipoDenuncia tipoDenuncia) {
        this.tipo = tipoDenuncia;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Denuncia)) {
            return false;
        }
        return id != null && id.equals(((Denuncia) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Denuncia{" +
            "id=" + getId() +
            ", criacao='" + getCriacao() + "'" +
            ", descricao='" + getDescricao() + "'" +
            "}";
    }
}
