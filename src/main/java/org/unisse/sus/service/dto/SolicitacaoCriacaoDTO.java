package org.unisse.sus.service.dto;
import java.time.Instant;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link org.unisse.sus.domain.SolicitacaoCriacao} entity.
 */
public class SolicitacaoCriacaoDTO implements Serializable {

    private Long id;

    @NotNull
    private Instant criacao;


    private Long unidadeId;

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

    public Long getUnidadeId() {
        return unidadeId;
    }

    public void setUnidadeId(Long unidadeSaudeId) {
        this.unidadeId = unidadeSaudeId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        SolicitacaoCriacaoDTO solicitacaoCriacaoDTO = (SolicitacaoCriacaoDTO) o;
        if (solicitacaoCriacaoDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), solicitacaoCriacaoDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "SolicitacaoCriacaoDTO{" +
            "id=" + getId() +
            ", criacao='" + getCriacao() + "'" +
            ", unidade=" + getUnidadeId() +
            "}";
    }
}
