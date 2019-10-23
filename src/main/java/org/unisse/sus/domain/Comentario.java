package org.unisse.sus.domain;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.time.Instant;

/**
 * A Comentario.
 */
@Entity
@Table(name = "comentario")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Comentario implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "criacao", nullable = false)
    private Instant criacao;

    @NotNull
    @Column(name = "comentario", nullable = false)
    private String comentario;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties("comentarios")
    private Registro registro;

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

    public Comentario criacao(Instant criacao) {
        this.criacao = criacao;
        return this;
    }

    public void setCriacao(Instant criacao) {
        this.criacao = criacao;
    }

    public String getComentario() {
        return comentario;
    }

    public Comentario comentario(String comentario) {
        this.comentario = comentario;
        return this;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    public Registro getRegistro() {
        return registro;
    }

    public Comentario registro(Registro registro) {
        this.registro = registro;
        return this;
    }

    public void setRegistro(Registro registro) {
        this.registro = registro;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Comentario)) {
            return false;
        }
        return id != null && id.equals(((Comentario) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Comentario{" +
            "id=" + getId() +
            ", criacao='" + getCriacao() + "'" +
            ", comentario='" + getComentario() + "'" +
            "}";
    }
}
