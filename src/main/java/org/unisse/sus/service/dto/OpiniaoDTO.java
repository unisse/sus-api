package org.unisse.sus.service.dto;
import java.time.Instant;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link org.unisse.sus.domain.Opiniao} entity.
 */
public class OpiniaoDTO implements Serializable {

    private Long id;

    @NotNull
    private Instant criacao;

    @NotNull
    private Boolean positiva;


    private Long comentarioId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getCriacao() {
        return criacao;
    }

    public void setCriacao(Instant criacao) {
        this.criacao = criacao;
    }

    public Boolean isPositiva() {
        return positiva;
    }

    public void setPositiva(Boolean positiva) {
        this.positiva = positiva;
    }

    public Long getComentarioId() {
        return comentarioId;
    }

    public void setComentarioId(Long comentarioId) {
        this.comentarioId = comentarioId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        OpiniaoDTO opiniaoDTO = (OpiniaoDTO) o;
        if (opiniaoDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), opiniaoDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "OpiniaoDTO{" +
            "id=" + getId() +
            ", criacao='" + getCriacao() + "'" +
            ", positiva='" + isPositiva() + "'" +
            ", comentario=" + getComentarioId() +
            "}";
    }
}
