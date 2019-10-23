package org.unisse.sus.service.dto;
import java.time.Instant;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link org.unisse.sus.domain.SituacaoUnidadeSaude} entity.
 */
public class SituacaoUnidadeSaudeDTO implements Serializable {

    private Long id;

    @NotNull
    private Instant criacao;

    @NotNull
    private String descricao;


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

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        SituacaoUnidadeSaudeDTO situacaoUnidadeSaudeDTO = (SituacaoUnidadeSaudeDTO) o;
        if (situacaoUnidadeSaudeDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), situacaoUnidadeSaudeDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "SituacaoUnidadeSaudeDTO{" +
            "id=" + getId() +
            ", criacao='" + getCriacao() + "'" +
            ", descricao='" + getDescricao() + "'" +
            "}";
    }
}
