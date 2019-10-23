package org.unisse.sus.service.dto;
import java.time.Instant;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link org.unisse.sus.domain.Importancia} entity.
 */
public class ImportanciaDTO implements Serializable {

    private Long id;

    @NotNull
    private Instant criacao;


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

        ImportanciaDTO importanciaDTO = (ImportanciaDTO) o;
        if (importanciaDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), importanciaDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "ImportanciaDTO{" +
            "id=" + getId() +
            ", criacao='" + getCriacao() + "'" +
            ", registro=" + getRegistroId() +
            "}";
    }
}
