package org.unisse.sus.service.dto;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link org.unisse.sus.domain.Alteracao} entity.
 */
public class AlteracaoDTO implements Serializable {

    private Long id;

    @NotNull
    private String nomeCampo;

    @NotNull
    private String valorAnterior;

    @NotNull
    private String valorNovo;


    private Long solicitacaoId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNomeCampo() {
        return nomeCampo;
    }

    public void setNomeCampo(String nomeCampo) {
        this.nomeCampo = nomeCampo;
    }

    public String getValorAnterior() {
        return valorAnterior;
    }

    public void setValorAnterior(String valorAnterior) {
        this.valorAnterior = valorAnterior;
    }

    public String getValorNovo() {
        return valorNovo;
    }

    public void setValorNovo(String valorNovo) {
        this.valorNovo = valorNovo;
    }

    public Long getSolicitacaoId() {
        return solicitacaoId;
    }

    public void setSolicitacaoId(Long solicitacaoAtualizacaoId) {
        this.solicitacaoId = solicitacaoAtualizacaoId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        AlteracaoDTO alteracaoDTO = (AlteracaoDTO) o;
        if (alteracaoDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), alteracaoDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "AlteracaoDTO{" +
            "id=" + getId() +
            ", nomeCampo='" + getNomeCampo() + "'" +
            ", valorAnterior='" + getValorAnterior() + "'" +
            ", valorNovo='" + getValorNovo() + "'" +
            ", solicitacao=" + getSolicitacaoId() +
            "}";
    }
}
