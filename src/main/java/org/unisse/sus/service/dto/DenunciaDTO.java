package org.unisse.sus.service.dto;
import java.time.Instant;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link org.unisse.sus.domain.Denuncia} entity.
 */
public class DenunciaDTO implements Serializable {

    private Long id;

    @NotNull
    private Instant criacao;

    @Size(max = 2550)
    private String descricao;


    private Long registroId;

    private Long tipoId;

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

    public Long getRegistroId() {
        return registroId;
    }

    public void setRegistroId(Long registroId) {
        this.registroId = registroId;
    }

    public Long getTipoId() {
        return tipoId;
    }

    public void setTipoId(Long tipoDenunciaId) {
        this.tipoId = tipoDenunciaId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        DenunciaDTO denunciaDTO = (DenunciaDTO) o;
        if (denunciaDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), denunciaDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "DenunciaDTO{" +
            "id=" + getId() +
            ", criacao='" + getCriacao() + "'" +
            ", descricao='" + getDescricao() + "'" +
            ", registro=" + getRegistroId() +
            ", tipo=" + getTipoId() +
            "}";
    }
}
