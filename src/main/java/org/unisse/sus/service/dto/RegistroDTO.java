package org.unisse.sus.service.dto;
import java.time.Instant;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link org.unisse.sus.domain.Registro} entity.
 */
public class RegistroDTO implements Serializable {

    private Long id;

    @NotNull
    private Instant criacao;

    @Size(max = 2550)
    private String descricao;


    private Long tipoId;

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

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Long getTipoId() {
        return tipoId;
    }

    public void setTipoId(Long tipoRegistroId) {
        this.tipoId = tipoRegistroId;
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

        RegistroDTO registroDTO = (RegistroDTO) o;
        if (registroDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), registroDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "RegistroDTO{" +
            "id=" + getId() +
            ", criacao='" + getCriacao() + "'" +
            ", descricao='" + getDescricao() + "'" +
            ", tipo=" + getTipoId() +
            ", unidade=" + getUnidadeId() +
            "}";
    }
}
