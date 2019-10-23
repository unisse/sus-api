package org.unisse.sus.domain;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.time.Instant;

/**
 * A Opiniao.
 */
@Entity
@Table(name = "opiniao")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Opiniao implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "criacao", nullable = false)
    private Instant criacao;

    @NotNull
    @Column(name = "positiva", nullable = false)
    private Boolean positiva;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties("opiniaos")
    private Comentario comentario;

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

    public Opiniao criacao(Instant criacao) {
        this.criacao = criacao;
        return this;
    }

    public void setCriacao(Instant criacao) {
        this.criacao = criacao;
    }

    public Boolean isPositiva() {
        return positiva;
    }

    public Opiniao positiva(Boolean positiva) {
        this.positiva = positiva;
        return this;
    }

    public void setPositiva(Boolean positiva) {
        this.positiva = positiva;
    }

    public Comentario getComentario() {
        return comentario;
    }

    public Opiniao comentario(Comentario comentario) {
        this.comentario = comentario;
        return this;
    }

    public void setComentario(Comentario comentario) {
        this.comentario = comentario;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Opiniao)) {
            return false;
        }
        return id != null && id.equals(((Opiniao) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Opiniao{" +
            "id=" + getId() +
            ", criacao='" + getCriacao() + "'" +
            ", positiva='" + isPositiva() + "'" +
            "}";
    }
}
