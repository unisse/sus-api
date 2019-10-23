package org.unisse.sus.service.dto;
import java.time.Instant;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link org.unisse.sus.domain.Comentario} entity.
 */
public class ComentarioDTO implements Serializable {

    private Long id;

    @NotNull
    private Instant criacao;

    @NotNull
    private String comentario;


    private Long registroId;

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

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    public Long getRegistroId() {
        return registroId;
    }

    public void setRegistroId(Long registroId) {
        this.registroId = registroId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ComentarioDTO comentarioDTO = (ComentarioDTO) o;
        if (comentarioDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), comentarioDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "ComentarioDTO{" +
            "id=" + getId() +
            ", criacao='" + getCriacao() + "'" +
            ", comentario='" + getComentario() + "'" +
            ", registro=" + getRegistroId() +
            "}";
    }
}
